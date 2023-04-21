package jpcsp.HLE.modules150;

import jpcsp.HLE.Modules;
import jpcsp.HLE.modules.HLEModule;
import jpcsp.HLE.modules.HLEModuleFunction;
import jpcsp.HLE.modules.HLEModuleManager;
import jpcsp.Memory;
import jpcsp.Processor;
import jpcsp.Allegrex.CpuState;

public class sceCtrl_driver implements HLEModule {

    @Override
    public String getName() {
        return "sceCtrl_driver";
    }

    @Override
    public void installModule(HLEModuleManager mm, int version) {
        if (version >= 150) {
            mm.addFunction(sceCtrlInitFunction, 0x3E65A0EA);
            mm.addFunction(sceCtrlEndFunction, 0xE03956E9);
            mm.addFunction(sceCtrlSuspendFunction, 0xC3F607F3);
            mm.addFunction(sceCtrlResumeFunction, 0xC245B57B);
            mm.addFunction(sceCtrlSetSamplingCycleFunction, 0x6A2774F3);
            mm.addFunction(sceCtrlGetSamplingCycleFunction, 0x02BAAD91);
            mm.addFunction(sceCtrlSetSamplingModeFunction, 0x1F4011E6);
            mm.addFunction(sceCtrlGetSamplingModeFunction, 0xDA6B76A1);
            mm.addFunction(sceCtrlPeekBufferPositiveFunction, 0x3A622550);
            mm.addFunction(sceCtrlPeekBufferNegativeFunction, 0xC152080A);
            mm.addFunction(sceCtrlReadBufferPositiveFunction, 0x1F803938);
            mm.addFunction(sceCtrlReadBufferNegativeFunction, 0x60B81F86);
            mm.addFunction(sceCtrlPeekLatchFunction, 0xB1D0E5CD);
            mm.addFunction(sceCtrlReadLatchFunction, 0x0B588501);
            mm.addFunction(sceCtrlSetIdleCancelKeyFunction, 0xA88E8D22);
            mm.addFunction(sceCtrlGetIdleCancelKeyFunction, 0xB7CEAED4);
            mm.addFunction(sceCtrlSetIdleCancelThresholdFunction, 0xA7144800);
            mm.addFunction(sceCtrlGetIdleCancelThresholdFunction, 0x687660FA);
            mm.addFunction(sceCtrl_driver_348D99D4Function, 0x348D99D4);
            mm.addFunction(sceCtrl_driver_AF5960F3Function, 0xAF5960F3);
            mm.addFunction(sceCtrlClearRapidFireFunction, 0xA68FD260);
            mm.addFunction(sceCtrlSetRapidFireFunction, 0x6841BE1A);
            mm.addFunction(sceCtrlSetButtonInterceptFunction, 0x7CA723DC);
            mm.addFunction(sceCtrlGetButtonInterceptFunction, 0x5E77BC8A);
            mm.addFunction(sceCtrl_driver_5C56C779Function, 0x5C56C779);
        }
    }

    @Override
    public void uninstallModule(HLEModuleManager mm, int version) {
        if (version >= 150) {
            mm.removeFunction(sceCtrlInitFunction);
            mm.removeFunction(sceCtrlEndFunction);
            mm.removeFunction(sceCtrlSuspendFunction);
            mm.removeFunction(sceCtrlResumeFunction);
            mm.removeFunction(sceCtrlSetSamplingCycleFunction);
            mm.removeFunction(sceCtrlGetSamplingCycleFunction);
            mm.removeFunction(sceCtrlSetSamplingModeFunction);
            mm.removeFunction(sceCtrlGetSamplingModeFunction);
            mm.removeFunction(sceCtrlPeekBufferPositiveFunction);
            mm.removeFunction(sceCtrlPeekBufferNegativeFunction);
            mm.removeFunction(sceCtrlReadBufferPositiveFunction);
            mm.removeFunction(sceCtrlReadBufferNegativeFunction);
            mm.removeFunction(sceCtrlPeekLatchFunction);
            mm.removeFunction(sceCtrlReadLatchFunction);
            mm.removeFunction(sceCtrlSetIdleCancelKeyFunction);
            mm.removeFunction(sceCtrlGetIdleCancelKeyFunction);
            mm.removeFunction(sceCtrlSetIdleCancelThresholdFunction);
            mm.removeFunction(sceCtrlGetIdleCancelThresholdFunction);
            mm.removeFunction(sceCtrl_driver_348D99D4Function);
            mm.removeFunction(sceCtrl_driver_AF5960F3Function);
            mm.removeFunction(sceCtrlClearRapidFireFunction);
            mm.removeFunction(sceCtrlSetRapidFireFunction);
            mm.removeFunction(sceCtrlSetButtonInterceptFunction);
            mm.removeFunction(sceCtrlGetButtonInterceptFunction);
            mm.removeFunction(sceCtrl_driver_5C56C779Function);
        }
    }

    public void sceCtrlInit(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCtrlInit [0x3E65A0EA]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCtrlEnd(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCtrlEnd [0xE03956E9]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCtrlSuspend(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCtrlSuspend [0xC3F607F3]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCtrlResume(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCtrlResume [0xC245B57B]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCtrlSetSamplingCycle(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCtrlSetSamplingCycle [0x6A2774F3]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCtrlGetSamplingCycle(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCtrlGetSamplingCycle [0x02BAAD91]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCtrlSetSamplingMode(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCtrlSetSamplingMode [0x1F4011E6]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCtrlGetSamplingMode(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCtrlGetSamplingMode [0xDA6B76A1]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCtrlPeekBufferPositive(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCtrlPeekBufferPositive [0x3A622550]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCtrlPeekBufferNegative(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCtrlPeekBufferNegative [0xC152080A]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCtrlReadBufferPositive(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCtrlReadBufferPositive [0x1F803938]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCtrlReadBufferNegative(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCtrlReadBufferNegative [0x60B81F86]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCtrlPeekLatch(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCtrlPeekLatch [0xB1D0E5CD]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCtrlReadLatch(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCtrlReadLatch [0x0B588501]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCtrlSetIdleCancelKey(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCtrlSetIdleCancelKey [0xA88E8D22]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCtrlGetIdleCancelKey(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCtrlGetIdleCancelKey [0xB7CEAED4]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCtrlSetIdleCancelThreshold(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCtrlSetIdleCancelThreshold [0xA7144800]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCtrlGetIdleCancelThreshold(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCtrlGetIdleCancelThreshold [0x687660FA]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCtrl_driver_348D99D4(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCtrl_driver_348D99D4 [0x348D99D4]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCtrl_driver_AF5960F3(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCtrl_driver_AF5960F3 [0xAF5960F3]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCtrlClearRapidFire(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCtrlClearRapidFire [0xA68FD260]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCtrlSetRapidFire(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCtrlSetRapidFire [0x6841BE1A]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCtrlSetButtonIntercept(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCtrlSetButtonIntercept [0x7CA723DC]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCtrlGetButtonIntercept(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCtrlGetButtonIntercept [0x5E77BC8A]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCtrl_driver_5C56C779(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCtrl_driver_5C56C779 [0x5C56C779]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public final HLEModuleFunction sceCtrlInitFunction = new HLEModuleFunction("sceCtrl_driver", "sceCtrlInit") {

        @Override
        public final void execute(Processor processor) {
            sceCtrlInit(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCtrl_driverModule.sceCtrlInit(processor);";
        }
    };

    public final HLEModuleFunction sceCtrlEndFunction = new HLEModuleFunction("sceCtrl_driver", "sceCtrlEnd") {

        @Override
        public final void execute(Processor processor) {
            sceCtrlEnd(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCtrl_driverModule.sceCtrlEnd(processor);";
        }
    };

    public final HLEModuleFunction sceCtrlSuspendFunction = new HLEModuleFunction("sceCtrl_driver", "sceCtrlSuspend") {

        @Override
        public final void execute(Processor processor) {
            sceCtrlSuspend(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCtrl_driverModule.sceCtrlSuspend(processor);";
        }
    };

    public final HLEModuleFunction sceCtrlResumeFunction = new HLEModuleFunction("sceCtrl_driver", "sceCtrlResume") {

        @Override
        public final void execute(Processor processor) {
            sceCtrlResume(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCtrl_driverModule.sceCtrlResume(processor);";
        }
    };

    public final HLEModuleFunction sceCtrlSetSamplingCycleFunction = new HLEModuleFunction("sceCtrl_driver", "sceCtrlSetSamplingCycle") {

        @Override
        public final void execute(Processor processor) {
            sceCtrlSetSamplingCycle(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCtrl_driverModule.sceCtrlSetSamplingCycle(processor);";
        }
    };

    public final HLEModuleFunction sceCtrlGetSamplingCycleFunction = new HLEModuleFunction("sceCtrl_driver", "sceCtrlGetSamplingCycle") {

        @Override
        public final void execute(Processor processor) {
            sceCtrlGetSamplingCycle(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCtrl_driverModule.sceCtrlGetSamplingCycle(processor);";
        }
    };

    public final HLEModuleFunction sceCtrlSetSamplingModeFunction = new HLEModuleFunction("sceCtrl_driver", "sceCtrlSetSamplingMode") {

        @Override
        public final void execute(Processor processor) {
            sceCtrlSetSamplingMode(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCtrl_driverModule.sceCtrlSetSamplingMode(processor);";
        }
    };

    public final HLEModuleFunction sceCtrlGetSamplingModeFunction = new HLEModuleFunction("sceCtrl_driver", "sceCtrlGetSamplingMode") {

        @Override
        public final void execute(Processor processor) {
            sceCtrlGetSamplingMode(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCtrl_driverModule.sceCtrlGetSamplingMode(processor);";
        }
    };

    public final HLEModuleFunction sceCtrlPeekBufferPositiveFunction = new HLEModuleFunction("sceCtrl_driver", "sceCtrlPeekBufferPositive") {

        @Override
        public final void execute(Processor processor) {
            sceCtrlPeekBufferPositive(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCtrl_driverModule.sceCtrlPeekBufferPositive(processor);";
        }
    };

    public final HLEModuleFunction sceCtrlPeekBufferNegativeFunction = new HLEModuleFunction("sceCtrl_driver", "sceCtrlPeekBufferNegative") {

        @Override
        public final void execute(Processor processor) {
            sceCtrlPeekBufferNegative(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCtrl_driverModule.sceCtrlPeekBufferNegative(processor);";
        }
    };

    public final HLEModuleFunction sceCtrlReadBufferPositiveFunction = new HLEModuleFunction("sceCtrl_driver", "sceCtrlReadBufferPositive") {

        @Override
        public final void execute(Processor processor) {
            sceCtrlReadBufferPositive(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCtrl_driverModule.sceCtrlReadBufferPositive(processor);";
        }
    };

    public final HLEModuleFunction sceCtrlReadBufferNegativeFunction = new HLEModuleFunction("sceCtrl_driver", "sceCtrlReadBufferNegative") {

        @Override
        public final void execute(Processor processor) {
            sceCtrlReadBufferNegative(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCtrl_driverModule.sceCtrlReadBufferNegative(processor);";
        }
    };

    public final HLEModuleFunction sceCtrlPeekLatchFunction = new HLEModuleFunction("sceCtrl_driver", "sceCtrlPeekLatch") {

        @Override
        public final void execute(Processor processor) {
            sceCtrlPeekLatch(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCtrl_driverModule.sceCtrlPeekLatch(processor);";
        }
    };

    public final HLEModuleFunction sceCtrlReadLatchFunction = new HLEModuleFunction("sceCtrl_driver", "sceCtrlReadLatch") {

        @Override
        public final void execute(Processor processor) {
            sceCtrlReadLatch(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCtrl_driverModule.sceCtrlReadLatch(processor);";
        }
    };

    public final HLEModuleFunction sceCtrlSetIdleCancelKeyFunction = new HLEModuleFunction("sceCtrl_driver", "sceCtrlSetIdleCancelKey") {

        @Override
        public final void execute(Processor processor) {
            sceCtrlSetIdleCancelKey(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCtrl_driverModule.sceCtrlSetIdleCancelKey(processor);";
        }
    };

    public final HLEModuleFunction sceCtrlGetIdleCancelKeyFunction = new HLEModuleFunction("sceCtrl_driver", "sceCtrlGetIdleCancelKey") {

        @Override
        public final void execute(Processor processor) {
            sceCtrlGetIdleCancelKey(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCtrl_driverModule.sceCtrlGetIdleCancelKey(processor);";
        }
    };

    public final HLEModuleFunction sceCtrlSetIdleCancelThresholdFunction = new HLEModuleFunction("sceCtrl_driver", "sceCtrlSetIdleCancelThreshold") {

        @Override
        public final void execute(Processor processor) {
            sceCtrlSetIdleCancelThreshold(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCtrl_driverModule.sceCtrlSetIdleCancelThreshold(processor);";
        }
    };

    public final HLEModuleFunction sceCtrlGetIdleCancelThresholdFunction = new HLEModuleFunction("sceCtrl_driver", "sceCtrlGetIdleCancelThreshold") {

        @Override
        public final void execute(Processor processor) {
            sceCtrlGetIdleCancelThreshold(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCtrl_driverModule.sceCtrlGetIdleCancelThreshold(processor);";
        }
    };

    public final HLEModuleFunction sceCtrl_driver_348D99D4Function = new HLEModuleFunction("sceCtrl_driver", "sceCtrl_driver_348D99D4") {

        @Override
        public final void execute(Processor processor) {
            sceCtrl_driver_348D99D4(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCtrl_driverModule.sceCtrl_driver_348D99D4(processor);";
        }
    };

    public final HLEModuleFunction sceCtrl_driver_AF5960F3Function = new HLEModuleFunction("sceCtrl_driver", "sceCtrl_driver_AF5960F3") {

        @Override
        public final void execute(Processor processor) {
            sceCtrl_driver_AF5960F3(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCtrl_driverModule.sceCtrl_driver_AF5960F3(processor);";
        }
    };

    public final HLEModuleFunction sceCtrlClearRapidFireFunction = new HLEModuleFunction("sceCtrl_driver", "sceCtrlClearRapidFire") {

        @Override
        public final void execute(Processor processor) {
            sceCtrlClearRapidFire(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCtrl_driverModule.sceCtrlClearRapidFire(processor);";
        }
    };

    public final HLEModuleFunction sceCtrlSetRapidFireFunction = new HLEModuleFunction("sceCtrl_driver", "sceCtrlSetRapidFire") {

        @Override
        public final void execute(Processor processor) {
            sceCtrlSetRapidFire(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCtrl_driverModule.sceCtrlSetRapidFire(processor);";
        }
    };

    public final HLEModuleFunction sceCtrlSetButtonInterceptFunction = new HLEModuleFunction("sceCtrl_driver", "sceCtrlSetButtonIntercept") {

        @Override
        public final void execute(Processor processor) {
            sceCtrlSetButtonIntercept(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCtrl_driverModule.sceCtrlSetButtonIntercept(processor);";
        }
    };

    public final HLEModuleFunction sceCtrlGetButtonInterceptFunction = new HLEModuleFunction("sceCtrl_driver", "sceCtrlGetButtonIntercept") {

        @Override
        public final void execute(Processor processor) {
            sceCtrlGetButtonIntercept(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCtrl_driverModule.sceCtrlGetButtonIntercept(processor);";
        }
    };

    public final HLEModuleFunction sceCtrl_driver_5C56C779Function = new HLEModuleFunction("sceCtrl_driver", "sceCtrl_driver_5C56C779") {

        @Override
        public final void execute(Processor processor) {
            sceCtrl_driver_5C56C779(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCtrl_driverModule.sceCtrl_driver_5C56C779(processor);";
        }
    };
}

;
