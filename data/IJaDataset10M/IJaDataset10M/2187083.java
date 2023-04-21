package jdos.cpu;

import jdos.cpu.core_dynamic.*;
import jdos.cpu.core_share.Constants;
import jdos.cpu.core_share.Data;
import jdos.debug.Debug;
import jdos.hardware.Memory;
import jdos.misc.Log;
import jdos.misc.setup.Config;

public class Core_dynamic {

    public static final int CACHE_MAXSIZE = 4096 * 2;

    public static final int CACHE_PAGES = 512;

    public static final int CACHE_BLOCKS = 128 * 1024;

    public static final int CACHE_ALIGN = 16;

    public static final int DYN_HASH_SHIFT = 4;

    public static final int DYN_PAGE_HASH = 4096 >> DYN_HASH_SHIFT;

    public static final int DYN_LINKS = 16;

    public static final int SMC_CURRENT_BLOCK = 0xffff;

    public static int instruction_count = 128;

    public static void CPU_Core_Dynamic_Init() {
    }

    public static void CPU_Core_Dynamic_Cache_Init(boolean enable_cache) {
        Cache.cache_init(enable_cache);
    }

    public static void CPU_Core_Dynamic_Cache_Close() {
    }

    public static final class CodePageHandlerDynRecRef {

        public CodePageHandlerDynRec value;
    }

    private static final CodePageHandlerDynRecRef chandlerRef = new CodePageHandlerDynRecRef();

    public static final CPU.CPU_Decoder CPU_Core_Dynrec_Trap_Run = new CPU.CPU_Decoder() {

        public int call() {
            int oldCycles = CPU.CPU_Cycles;
            CPU.CPU_Cycles = 1;
            CPU.cpu.trap_skip = false;
            int ret = Core_normal.CPU_Core_Normal_Run.call();
            if (!CPU.cpu.trap_skip) CPU.CPU_HW_Interrupt(1);
            CPU.CPU_Cycles = oldCycles - 1;
            CPU.cpudecoder = CPU_Core_Dynamic_Run;
            return ret;
        }
    };

    private static CacheBlockDynRec LinkBlocks(CacheBlockDynRec running, int ret) {
        CacheBlockDynRec block = null;
        int temp_ip = CPU.Segs_CSphys + CPU_Regs.reg_eip;
        Paging.PageHandler handler = Paging.get_tlb_readhandler(temp_ip);
        if (handler instanceof CodePageHandlerDynRec) {
            CodePageHandlerDynRec temp_handler = (CodePageHandlerDynRec) handler;
            if ((temp_handler.flags & Paging.PFLAG_HASCODE) != 0) {
                block = temp_handler.FindCacheBlock((int) (temp_ip & 4095));
                if (block == null) return null;
                running.LinkTo(ret == Constants.BR_Link2 ? 1 : 0, block);
                return block;
            }
        }
        return null;
    }

    public static final CPU.CPU_Decoder CPU_Core_Dynamic_Run = new CPU.CPU_Decoder() {

        public int call() {
            Core.base_ds = CPU.Segs_DSphys;
            Core.base_ss = CPU.Segs_SSphys;
            Core.base_val_ds = CPU_Regs.ds;
            while (CPU.CPU_Cycles > 0) {
                int ip_point = CPU.Segs_CSphys + CPU_Regs.reg_eip;
                Paging.PageHandler handler = Paging.get_tlb_readhandler(ip_point);
                CodePageHandlerDynRec chandler = null;
                int page_ip_point = ip_point & 4095;
                if (handler != null && handler instanceof CodePageHandlerDynRec) chandler = (CodePageHandlerDynRec) handler;
                if (chandler == null) {
                    if (Decoder_basic.MakeCodePage(ip_point, chandlerRef)) {
                        CPU.CPU_Exception(CPU.cpu.exception.which, CPU.cpu.exception.error);
                        continue;
                    }
                    chandler = chandlerRef.value;
                }
                if (chandler == null) return Core_normal.CPU_Core_Normal_Run.call();
                CacheBlockDynRec block = chandler.FindCacheBlock(page_ip_point);
                int ret = 0;
                if (block == null) {
                    if (chandler.invalidation_map == null || (chandler.invalidation_map.p[page_ip_point] < 4)) {
                        block = Decoder.CreateCacheBlock(chandler, ip_point, instruction_count);
                    } else {
                        int old_cycles = CPU.CPU_Cycles;
                        CPU.CPU_Cycles = 1;
                        int nc_retcode = Core_normal.CPU_Core_Normal_Run.call();
                        if (nc_retcode == 0) {
                            CPU.CPU_Cycles = old_cycles - 1;
                            continue;
                        }
                        CPU.CPU_CycleLeft += old_cycles;
                        return nc_retcode;
                    }
                }
                while (true) {
                    if (block != null) {
                        if (Config.DYNAMIC_CORE_VERIFY) {
                            int offset = Paging.getDirectIndexRO(CPU.Segs_CSphys + CPU_Regs.reg_eip);
                            for (int i = 0; i < block.originalByteCode.length; i++) {
                                if (block.originalByteCode[i] != Memory.host_readbs(i + offset)) {
                                    Log.exit("Dynamic core cache has been modified without its knowledge:\n    cs:ip=" + Integer.toString(CPU.Segs_CSphys, 16) + ":" + Integer.toString(CPU_Regs.reg_eip, 16) + "\n    index=" + i + "\n    " + Integer.toString(block.originalByteCode[i] & 0xFF, 16) + " cached value\n    " + Integer.toString(Memory.host_readb(offset), 16) + " memory value @ " + offset + "\n    block=" + block);
                                }
                            }
                        }
                        ret = block.code.call();
                    }
                    switch(ret) {
                        case Constants.BR_Link1:
                            {
                                CacheBlockDynRec next = block.link1.to;
                                if (next == null) block = LinkBlocks(block, ret); else block = next;
                                if (block != null && CPU.CPU_Cycles > 0) continue;
                                break;
                            }
                        case Constants.BR_Link2:
                            {
                                CacheBlockDynRec next = block.link2.to;
                                if (next == null) block = LinkBlocks(block, ret); else block = next;
                                if (block != null && CPU.CPU_Cycles > 0) continue;
                                break;
                            }
                        case Constants.BR_Normal:
                        case Constants.BR_Jump:
                            if (Config.C_HEAVY_DEBUG) if (Debug.DEBUG_HeavyIsBreakpoint()) return Debug.debugCallback;
                            break;
                        case Constants.BR_CallBack:
                            Flags.FillFlags();
                            return Data.callback;
                        case Constants.BR_Illegal:
                            CPU.CPU_Exception(6, 0);
                            break;
                        default:
                            Log.exit("Invalid return code " + ret);
                    }
                    break;
                }
            }
            Flags.FillFlags();
            return Callback.CBRET_NONE;
        }
    };
}
