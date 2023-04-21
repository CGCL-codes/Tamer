package jpcsp.HLE.modules150;

import jpcsp.HLE.Modules;
import jpcsp.HLE.modules.HLEModule;
import jpcsp.HLE.modules.HLEModuleFunction;
import jpcsp.HLE.modules.HLEModuleManager;
import jpcsp.Memory;
import jpcsp.Processor;
import jpcsp.Allegrex.CpuState;

public class memlmd implements HLEModule {

    @Override
    public String getName() {
        return "memlmd";
    }

    @Override
    public void installModule(HLEModuleManager mm, int version) {
        if (version >= 150) {
            mm.addFunction(sceUtilsGetLoadModuleABLengthFunction, 0x8BDB1A3E);
            mm.addFunction(sceUtilsGetLoadModuleABLengthByPollingFunction, 0x185F0A2A);
        }
    }

    @Override
    public void uninstallModule(HLEModuleManager mm, int version) {
        if (version >= 150) {
            mm.removeFunction(sceUtilsGetLoadModuleABLengthFunction);
            mm.removeFunction(sceUtilsGetLoadModuleABLengthByPollingFunction);
        }
    }

    public void sceUtilsGetLoadModuleABLength(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceUtilsGetLoadModuleABLength [0x8BDB1A3E]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceUtilsGetLoadModuleABLengthByPolling(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceUtilsGetLoadModuleABLengthByPolling [0x185F0A2A]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public final HLEModuleFunction sceUtilsGetLoadModuleABLengthFunction = new HLEModuleFunction("memlmd", "sceUtilsGetLoadModuleABLength") {

        @Override
        public final void execute(Processor processor) {
            sceUtilsGetLoadModuleABLength(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.memlmdModule.sceUtilsGetLoadModuleABLength(processor);";
        }
    };

    public final HLEModuleFunction sceUtilsGetLoadModuleABLengthByPollingFunction = new HLEModuleFunction("memlmd", "sceUtilsGetLoadModuleABLengthByPolling") {

        @Override
        public final void execute(Processor processor) {
            sceUtilsGetLoadModuleABLengthByPolling(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.memlmdModule.sceUtilsGetLoadModuleABLengthByPolling(processor);";
        }
    };
}

;
