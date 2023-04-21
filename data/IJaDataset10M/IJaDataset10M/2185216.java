package jpcsp.HLE.modules150;

import jpcsp.HLE.Modules;
import jpcsp.HLE.modules.HLEModule;
import jpcsp.HLE.modules.HLEModuleFunction;
import jpcsp.HLE.modules.HLEModuleManager;
import jpcsp.Memory;
import jpcsp.Processor;
import jpcsp.Allegrex.CpuState;

public class sceVaudio implements HLEModule {

    @Override
    public String getName() {
        return "sceVaudio";
    }

    @Override
    public void installModule(HLEModuleManager mm, int version) {
        if (version >= 150) {
            mm.addFunction(sceVaudioOutputBlockingFunction, 0x8986295E);
            mm.addFunction(sceVaudioChReserveFunction, 0x03B6807D);
            mm.addFunction(sceVaudioChReleaseFunction, 0x67585DFD);
            mm.addFunction(sceVaudio_346FBE94Function, 0x346FBE94);
        }
    }

    @Override
    public void uninstallModule(HLEModuleManager mm, int version) {
        if (version >= 150) {
            mm.removeFunction(sceVaudioOutputBlockingFunction);
            mm.removeFunction(sceVaudioChReserveFunction);
            mm.removeFunction(sceVaudioChReleaseFunction);
            mm.removeFunction(sceVaudio_346FBE94Function);
        }
    }

    public void sceVaudioOutputBlocking(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceVaudioOutputBlocking [0x8986295E]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceVaudioChReserve(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceVaudioChReserve [0x03B6807D]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceVaudioChRelease(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceVaudioChRelease [0x67585DFD]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceVaudio_346FBE94(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceVaudio_346FBE94 [0x346FBE94]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public final HLEModuleFunction sceVaudioOutputBlockingFunction = new HLEModuleFunction("sceVaudio", "sceVaudioOutputBlocking") {

        @Override
        public final void execute(Processor processor) {
            sceVaudioOutputBlocking(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceVaudioModule.sceVaudioOutputBlocking(processor);";
        }
    };

    public final HLEModuleFunction sceVaudioChReserveFunction = new HLEModuleFunction("sceVaudio", "sceVaudioChReserve") {

        @Override
        public final void execute(Processor processor) {
            sceVaudioChReserve(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceVaudioModule.sceVaudioChReserve(processor);";
        }
    };

    public final HLEModuleFunction sceVaudioChReleaseFunction = new HLEModuleFunction("sceVaudio", "sceVaudioChRelease") {

        @Override
        public final void execute(Processor processor) {
            sceVaudioChRelease(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceVaudioModule.sceVaudioChRelease(processor);";
        }
    };

    public final HLEModuleFunction sceVaudio_346FBE94Function = new HLEModuleFunction("sceVaudio", "sceVaudio_346FBE94") {

        @Override
        public final void execute(Processor processor) {
            sceVaudio_346FBE94(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceVaudioModule.sceVaudio_346FBE94(processor);";
        }
    };
}

;
