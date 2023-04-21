package jpcsp.HLE.modules150;

import jpcsp.HLE.Modules;
import jpcsp.HLE.modules.HLEModule;
import jpcsp.HLE.modules.HLEModuleFunction;
import jpcsp.HLE.modules.HLEModuleManager;
import jpcsp.Memory;
import jpcsp.Processor;
import jpcsp.Allegrex.CpuState;

public class sceLed_driver implements HLEModule {

    @Override
    public String getName() {
        return "sceLed_driver";
    }

    @Override
    public void installModule(HLEModuleManager mm, int version) {
        if (version >= 150) {
            mm.addFunction(sceLedInitFunction, 0xB0B6A883);
            mm.addFunction(sceLedEndFunction, 0xA8542C48);
            mm.addFunction(sceLedSuspendFunction, 0xDE91D3A4);
            mm.addFunction(sceLedResumeFunction, 0xA13B3D38);
            mm.addFunction(sceLedSetModeFunction, 0xEA24BE03);
        }
    }

    @Override
    public void uninstallModule(HLEModuleManager mm, int version) {
        if (version >= 150) {
            mm.removeFunction(sceLedInitFunction);
            mm.removeFunction(sceLedEndFunction);
            mm.removeFunction(sceLedSuspendFunction);
            mm.removeFunction(sceLedResumeFunction);
            mm.removeFunction(sceLedSetModeFunction);
        }
    }

    public void sceLedInit(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceLedInit [0xB0B6A883]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceLedEnd(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceLedEnd [0xA8542C48]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceLedSuspend(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceLedSuspend [0xDE91D3A4]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceLedResume(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceLedResume [0xA13B3D38]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceLedSetMode(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceLedSetMode [0xEA24BE03]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public final HLEModuleFunction sceLedInitFunction = new HLEModuleFunction("sceLed_driver", "sceLedInit") {

        @Override
        public final void execute(Processor processor) {
            sceLedInit(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceLed_driverModule.sceLedInit(processor);";
        }
    };

    public final HLEModuleFunction sceLedEndFunction = new HLEModuleFunction("sceLed_driver", "sceLedEnd") {

        @Override
        public final void execute(Processor processor) {
            sceLedEnd(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceLed_driverModule.sceLedEnd(processor);";
        }
    };

    public final HLEModuleFunction sceLedSuspendFunction = new HLEModuleFunction("sceLed_driver", "sceLedSuspend") {

        @Override
        public final void execute(Processor processor) {
            sceLedSuspend(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceLed_driverModule.sceLedSuspend(processor);";
        }
    };

    public final HLEModuleFunction sceLedResumeFunction = new HLEModuleFunction("sceLed_driver", "sceLedResume") {

        @Override
        public final void execute(Processor processor) {
            sceLedResume(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceLed_driverModule.sceLedResume(processor);";
        }
    };

    public final HLEModuleFunction sceLedSetModeFunction = new HLEModuleFunction("sceLed_driver", "sceLedSetMode") {

        @Override
        public final void execute(Processor processor) {
            sceLedSetMode(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceLed_driverModule.sceLedSetMode(processor);";
        }
    };
}

;
