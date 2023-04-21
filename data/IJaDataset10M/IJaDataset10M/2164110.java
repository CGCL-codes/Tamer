package jdos.cpu.core_dynamic;

import jdos.cpu.Core_dynamic;
import jdos.cpu.Paging;
import jdos.hardware.Memory;
import jdos.misc.Log;
import jdos.util.IntRef;
import jdos.util.Ptr;
import jdos.util.ShortRef;

public class Decoder_basic {

    public static final int REP_NONE = 0;

    public static final int REP_NZ = 1;

    public static final int REP_Z = 2;

    private static final ShortRef rdval = new ShortRef(0);

    private static final IntRef phys_page = new IntRef(0);

    static int mf_functions_num = 0;

    public static final class _mf_functions {

        Ptr pos;

        int ftype;
    }

    static final _mf_functions[] mf_functions = new _mf_functions[64];

    static {
        for (int i = 0; i < mf_functions.length; i++) {
            mf_functions[i] = new _mf_functions();
        }
    }

    public static void InitFlagsOptimization() {
        mf_functions_num = 0;
    }

    public static boolean MakeCodePage(int lin_addr, Core_dynamic.CodePageHandlerDynRecRef cph) {
        Memory.mem_readb(lin_addr);
        Paging.PageHandler handler = Paging.get_tlb_readhandler(lin_addr);
        if ((handler.flags & Paging.PFLAG_HASCODE) != 0) {
            cph.value = (CodePageHandlerDynRec) handler;
            return false;
        }
        if ((handler.flags & Paging.PFLAG_NOCODE) != 0) {
            cph.value = null;
            return false;
        }
        int lin_page = lin_addr >>> 12;
        phys_page.value = lin_page;
        if (!Paging.PAGING_MakePhysPage(phys_page)) {
            Log.log_msg("DYNREC:Can't find physpage for lin addr " + Integer.toString(lin_addr, 16));
            cph.value = null;
            return false;
        }
        if (Cache.cache.free_pages == null) {
            if (Cache.cache.used_pages != Decoder.decode.page.code) Cache.cache.used_pages.ClearRelease(); else {
                if ((Cache.cache.used_pages.next != null) && (Cache.cache.used_pages.next != Decoder.decode.page.code)) Cache.cache.used_pages.next.ClearRelease(); else {
                    Log.log_msg("DYNREC:Invalid cache links");
                    Cache.cache.used_pages.ClearRelease();
                }
            }
        }
        CodePageHandlerDynRec cpagehandler = Cache.cache.free_pages;
        Cache.cache.free_pages = Cache.cache.free_pages.next;
        cpagehandler.prev = Cache.cache.last_page;
        cpagehandler.next = null;
        if (Cache.cache.last_page != null) Cache.cache.last_page.next = cpagehandler;
        Cache.cache.last_page = cpagehandler;
        if (Cache.cache.used_pages == null) Cache.cache.used_pages = cpagehandler;
        cpagehandler.SetupAt(phys_page.value, handler);
        Memory.MEM_SetPageHandler(phys_page.value, 1, cpagehandler);
        Paging.PAGING_UnlinkPages(lin_page, 1);
        cph.value = cpagehandler;
        return false;
    }
}
