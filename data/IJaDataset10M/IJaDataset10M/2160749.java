package jpcsp.HLE.modules150;

import jpcsp.HLE.Modules;
import jpcsp.HLE.modules.HLEModule;
import jpcsp.HLE.modules.HLEModuleFunction;
import jpcsp.HLE.modules.HLEModuleManager;
import jpcsp.Memory;
import jpcsp.Processor;
import jpcsp.Allegrex.CpuState;

public class sceMemab_driver implements HLEModule {

    @Override
    public String getName() {
        return "sceMemab_driver";
    }

    @Override
    public void installModule(HLEModuleManager mm, int version) {
        if (version >= 150) {
            mm.addFunction(sceMemab_driver_01FD1BA0Function, 0x01FD1BA0);
            mm.addFunction(sceMemab_driver_0C134D3FFunction, 0x0C134D3F);
            mm.addFunction(sceMemab_driver_A4720EEDFunction, 0xA4720EED);
            mm.addFunction(sceMemab_driver_DC598EF3Function, 0xDC598EF3);
            mm.addFunction(sceMemab_driver_8DAC2343Function, 0x8DAC2343);
            mm.addFunction(sceMemab_driver_DAE8A629Function, 0xDAE8A629);
            mm.addFunction(sceMemab_driver_CF49BCBBFunction, 0xCF49BCBB);
            mm.addFunction(sceMemab_driver_E6D3ACE2Function, 0xE6D3ACE2);
            mm.addFunction(sceMemab_driver_B54E1AA7Function, 0xB54E1AA7);
            mm.addFunction(sceMemab_driver_F1D2B9B0Function, 0xF1D2B9B0);
            mm.addFunction(sceMemab_driver_4DF566D3Function, 0x4DF566D3);
            mm.addFunction(sceMemab_driver_F94CA9B8Function, 0xF94CA9B8);
            mm.addFunction(sceMemab_driver_34EBFB6AFunction, 0x34EBFB6A);
            mm.addFunction(sceMemab_driver_531E144BFunction, 0x531E144B);
        }
    }

    @Override
    public void uninstallModule(HLEModuleManager mm, int version) {
        if (version >= 150) {
            mm.removeFunction(sceMemab_driver_01FD1BA0Function);
            mm.removeFunction(sceMemab_driver_0C134D3FFunction);
            mm.removeFunction(sceMemab_driver_A4720EEDFunction);
            mm.removeFunction(sceMemab_driver_DC598EF3Function);
            mm.removeFunction(sceMemab_driver_8DAC2343Function);
            mm.removeFunction(sceMemab_driver_DAE8A629Function);
            mm.removeFunction(sceMemab_driver_CF49BCBBFunction);
            mm.removeFunction(sceMemab_driver_E6D3ACE2Function);
            mm.removeFunction(sceMemab_driver_B54E1AA7Function);
            mm.removeFunction(sceMemab_driver_F1D2B9B0Function);
            mm.removeFunction(sceMemab_driver_4DF566D3Function);
            mm.removeFunction(sceMemab_driver_F94CA9B8Function);
            mm.removeFunction(sceMemab_driver_34EBFB6AFunction);
            mm.removeFunction(sceMemab_driver_531E144BFunction);
        }
    }

    public void sceMemab_driver_01FD1BA0(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceMemab_driver_01FD1BA0 [0x01FD1BA0]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceMemab_driver_0C134D3F(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceMemab_driver_0C134D3F [0x0C134D3F]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceMemab_driver_A4720EED(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceMemab_driver_A4720EED [0xA4720EED]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceMemab_driver_DC598EF3(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceMemab_driver_DC598EF3 [0xDC598EF3]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceMemab_driver_8DAC2343(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceMemab_driver_8DAC2343 [0x8DAC2343]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceMemab_driver_DAE8A629(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceMemab_driver_DAE8A629 [0xDAE8A629]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceMemab_driver_CF49BCBB(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceMemab_driver_CF49BCBB [0xCF49BCBB]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceMemab_driver_E6D3ACE2(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceMemab_driver_E6D3ACE2 [0xE6D3ACE2]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceMemab_driver_B54E1AA7(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceMemab_driver_B54E1AA7 [0xB54E1AA7]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceMemab_driver_F1D2B9B0(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceMemab_driver_F1D2B9B0 [0xF1D2B9B0]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceMemab_driver_4DF566D3(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceMemab_driver_4DF566D3 [0x4DF566D3]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceMemab_driver_F94CA9B8(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceMemab_driver_F94CA9B8 [0xF94CA9B8]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceMemab_driver_34EBFB6A(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceMemab_driver_34EBFB6A [0x34EBFB6A]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceMemab_driver_531E144B(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceMemab_driver_531E144B [0x531E144B]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public final HLEModuleFunction sceMemab_driver_01FD1BA0Function = new HLEModuleFunction("sceMemab_driver", "sceMemab_driver_01FD1BA0") {

        @Override
        public final void execute(Processor processor) {
            sceMemab_driver_01FD1BA0(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceMemab_driverModule.sceMemab_driver_01FD1BA0(processor);";
        }
    };

    public final HLEModuleFunction sceMemab_driver_0C134D3FFunction = new HLEModuleFunction("sceMemab_driver", "sceMemab_driver_0C134D3F") {

        @Override
        public final void execute(Processor processor) {
            sceMemab_driver_0C134D3F(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceMemab_driverModule.sceMemab_driver_0C134D3F(processor);";
        }
    };

    public final HLEModuleFunction sceMemab_driver_A4720EEDFunction = new HLEModuleFunction("sceMemab_driver", "sceMemab_driver_A4720EED") {

        @Override
        public final void execute(Processor processor) {
            sceMemab_driver_A4720EED(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceMemab_driverModule.sceMemab_driver_A4720EED(processor);";
        }
    };

    public final HLEModuleFunction sceMemab_driver_DC598EF3Function = new HLEModuleFunction("sceMemab_driver", "sceMemab_driver_DC598EF3") {

        @Override
        public final void execute(Processor processor) {
            sceMemab_driver_DC598EF3(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceMemab_driverModule.sceMemab_driver_DC598EF3(processor);";
        }
    };

    public final HLEModuleFunction sceMemab_driver_8DAC2343Function = new HLEModuleFunction("sceMemab_driver", "sceMemab_driver_8DAC2343") {

        @Override
        public final void execute(Processor processor) {
            sceMemab_driver_8DAC2343(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceMemab_driverModule.sceMemab_driver_8DAC2343(processor);";
        }
    };

    public final HLEModuleFunction sceMemab_driver_DAE8A629Function = new HLEModuleFunction("sceMemab_driver", "sceMemab_driver_DAE8A629") {

        @Override
        public final void execute(Processor processor) {
            sceMemab_driver_DAE8A629(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceMemab_driverModule.sceMemab_driver_DAE8A629(processor);";
        }
    };

    public final HLEModuleFunction sceMemab_driver_CF49BCBBFunction = new HLEModuleFunction("sceMemab_driver", "sceMemab_driver_CF49BCBB") {

        @Override
        public final void execute(Processor processor) {
            sceMemab_driver_CF49BCBB(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceMemab_driverModule.sceMemab_driver_CF49BCBB(processor);";
        }
    };

    public final HLEModuleFunction sceMemab_driver_E6D3ACE2Function = new HLEModuleFunction("sceMemab_driver", "sceMemab_driver_E6D3ACE2") {

        @Override
        public final void execute(Processor processor) {
            sceMemab_driver_E6D3ACE2(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceMemab_driverModule.sceMemab_driver_E6D3ACE2(processor);";
        }
    };

    public final HLEModuleFunction sceMemab_driver_B54E1AA7Function = new HLEModuleFunction("sceMemab_driver", "sceMemab_driver_B54E1AA7") {

        @Override
        public final void execute(Processor processor) {
            sceMemab_driver_B54E1AA7(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceMemab_driverModule.sceMemab_driver_B54E1AA7(processor);";
        }
    };

    public final HLEModuleFunction sceMemab_driver_F1D2B9B0Function = new HLEModuleFunction("sceMemab_driver", "sceMemab_driver_F1D2B9B0") {

        @Override
        public final void execute(Processor processor) {
            sceMemab_driver_F1D2B9B0(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceMemab_driverModule.sceMemab_driver_F1D2B9B0(processor);";
        }
    };

    public final HLEModuleFunction sceMemab_driver_4DF566D3Function = new HLEModuleFunction("sceMemab_driver", "sceMemab_driver_4DF566D3") {

        @Override
        public final void execute(Processor processor) {
            sceMemab_driver_4DF566D3(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceMemab_driverModule.sceMemab_driver_4DF566D3(processor);";
        }
    };

    public final HLEModuleFunction sceMemab_driver_F94CA9B8Function = new HLEModuleFunction("sceMemab_driver", "sceMemab_driver_F94CA9B8") {

        @Override
        public final void execute(Processor processor) {
            sceMemab_driver_F94CA9B8(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceMemab_driverModule.sceMemab_driver_F94CA9B8(processor);";
        }
    };

    public final HLEModuleFunction sceMemab_driver_34EBFB6AFunction = new HLEModuleFunction("sceMemab_driver", "sceMemab_driver_34EBFB6A") {

        @Override
        public final void execute(Processor processor) {
            sceMemab_driver_34EBFB6A(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceMemab_driverModule.sceMemab_driver_34EBFB6A(processor);";
        }
    };

    public final HLEModuleFunction sceMemab_driver_531E144BFunction = new HLEModuleFunction("sceMemab_driver", "sceMemab_driver_531E144B") {

        @Override
        public final void execute(Processor processor) {
            sceMemab_driver_531E144B(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceMemab_driverModule.sceMemab_driver_531E144B(processor);";
        }
    };
}

;
