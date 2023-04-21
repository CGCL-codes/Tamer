package jpcsp.HLE.modules150;

import jpcsp.HLE.Modules;
import jpcsp.HLE.modules.HLEModule;
import jpcsp.HLE.modules.HLEModuleFunction;
import jpcsp.HLE.modules.HLEModuleManager;
import jpcsp.Memory;
import jpcsp.Processor;
import jpcsp.Allegrex.CpuState;

public class scePEQ_driver implements HLEModule {

    @Override
    public String getName() {
        return "scePEQ_driver";
    }

    @Override
    public void installModule(HLEModuleManager mm, int version) {
        if (version >= 150) {
            mm.addFunction(scePEQ_driver_213DE849Function, 0x213DE849);
            mm.addFunction(scePEQ_driver_FC45514BFunction, 0xFC45514B);
            mm.addFunction(scePEQ_driver_F7EA0632Function, 0xF7EA0632);
            mm.addFunction(scePEQ_driver_ED13C3B5Function, 0xED13C3B5);
        }
    }

    @Override
    public void uninstallModule(HLEModuleManager mm, int version) {
        if (version >= 150) {
            mm.removeFunction(scePEQ_driver_213DE849Function);
            mm.removeFunction(scePEQ_driver_FC45514BFunction);
            mm.removeFunction(scePEQ_driver_F7EA0632Function);
            mm.removeFunction(scePEQ_driver_ED13C3B5Function);
        }
    }

    public void scePEQ_driver_213DE849(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function scePEQ_driver_213DE849 [0x213DE849]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void scePEQ_driver_FC45514B(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function scePEQ_driver_FC45514B [0xFC45514B]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void scePEQ_driver_F7EA0632(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function scePEQ_driver_F7EA0632 [0xF7EA0632]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void scePEQ_driver_ED13C3B5(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function scePEQ_driver_ED13C3B5 [0xED13C3B5]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public final HLEModuleFunction scePEQ_driver_213DE849Function = new HLEModuleFunction("scePEQ_driver", "scePEQ_driver_213DE849") {

        @Override
        public final void execute(Processor processor) {
            scePEQ_driver_213DE849(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.scePEQ_driverModule.scePEQ_driver_213DE849(processor);";
        }
    };

    public final HLEModuleFunction scePEQ_driver_FC45514BFunction = new HLEModuleFunction("scePEQ_driver", "scePEQ_driver_FC45514B") {

        @Override
        public final void execute(Processor processor) {
            scePEQ_driver_FC45514B(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.scePEQ_driverModule.scePEQ_driver_FC45514B(processor);";
        }
    };

    public final HLEModuleFunction scePEQ_driver_F7EA0632Function = new HLEModuleFunction("scePEQ_driver", "scePEQ_driver_F7EA0632") {

        @Override
        public final void execute(Processor processor) {
            scePEQ_driver_F7EA0632(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.scePEQ_driverModule.scePEQ_driver_F7EA0632(processor);";
        }
    };

    public final HLEModuleFunction scePEQ_driver_ED13C3B5Function = new HLEModuleFunction("scePEQ_driver", "scePEQ_driver_ED13C3B5") {

        @Override
        public final void execute(Processor processor) {
            scePEQ_driver_ED13C3B5(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.scePEQ_driverModule.scePEQ_driver_ED13C3B5(processor);";
        }
    };
}

;
