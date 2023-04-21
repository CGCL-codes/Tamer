package jpcsp.HLE.modules150;

import jpcsp.HLE.Modules;
import jpcsp.HLE.modules.HLEModule;
import jpcsp.HLE.modules.HLEModuleFunction;
import jpcsp.HLE.modules.HLEModuleManager;
import jpcsp.Memory;
import jpcsp.Processor;
import jpcsp.Allegrex.CpuState;

public class sceHprm_driver implements HLEModule {

    @Override
    public String getName() {
        return "sceHprm_driver";
    }

    @Override
    public void installModule(HLEModuleManager mm, int version) {
        if (version >= 150) {
            mm.addFunction(sceHprmInitFunction, 0x1C5BC5A0);
            mm.addFunction(sceHprmEndFunction, 0x588845DA);
            mm.addFunction(sceHprmSuspendFunction, 0x526BB7F4);
            mm.addFunction(sceHprmResumeFunction, 0x2C7B8B05);
            mm.addFunction(sceHprmSetConnectCallbackFunction, 0xD22913DB);
            mm.addFunction(sceHprmRegisterCallbackFunction, 0xC7154136);
            mm.addFunction(sceHprmUnregisterCallbackFunction, 0x444ED0B7);
            mm.addFunction(sceHprm_driver_71B5FB67Function, 0x71B5FB67);
            mm.addFunction(sceHprmIsRemoteExistFunction, 0x208DB1BD);
            mm.addFunction(sceHprmIsHeadphoneExistFunction, 0x7E69EDA4);
            mm.addFunction(sceHprmIsMicrophoneExistFunction, 0x219C58F1);
            mm.addFunction(sceHprmResetFunction, 0x4D1E622C);
            mm.addFunction(sceHprmGetInternalStateFunction, 0x7B038374);
            mm.addFunction(sceHprm_driver_F04591FAFunction, 0xF04591FA);
            mm.addFunction(sceHprm_driver_971AE8FBFunction, 0x971AE8FB);
            mm.addFunction(sceHprmGetModelFunction, 0xBAD0828E);
            mm.addFunction(sceHprmPeekCurrentKeyFunction, 0x1910B327);
            mm.addFunction(sceHprmPeekLatchFunction, 0x2BCEC83E);
            mm.addFunction(sceHprmReadLatchFunction, 0x40D2F9F0);
        }
    }

    @Override
    public void uninstallModule(HLEModuleManager mm, int version) {
        if (version >= 150) {
            mm.removeFunction(sceHprmInitFunction);
            mm.removeFunction(sceHprmEndFunction);
            mm.removeFunction(sceHprmSuspendFunction);
            mm.removeFunction(sceHprmResumeFunction);
            mm.removeFunction(sceHprmSetConnectCallbackFunction);
            mm.removeFunction(sceHprmRegisterCallbackFunction);
            mm.removeFunction(sceHprmUnregisterCallbackFunction);
            mm.removeFunction(sceHprm_driver_71B5FB67Function);
            mm.removeFunction(sceHprmIsRemoteExistFunction);
            mm.removeFunction(sceHprmIsHeadphoneExistFunction);
            mm.removeFunction(sceHprmIsMicrophoneExistFunction);
            mm.removeFunction(sceHprmResetFunction);
            mm.removeFunction(sceHprmGetInternalStateFunction);
            mm.removeFunction(sceHprm_driver_F04591FAFunction);
            mm.removeFunction(sceHprm_driver_971AE8FBFunction);
            mm.removeFunction(sceHprmGetModelFunction);
            mm.removeFunction(sceHprmPeekCurrentKeyFunction);
            mm.removeFunction(sceHprmPeekLatchFunction);
            mm.removeFunction(sceHprmReadLatchFunction);
        }
    }

    public void sceHprmInit(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceHprmInit [0x1C5BC5A0]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceHprmEnd(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceHprmEnd [0x588845DA]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceHprmSuspend(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceHprmSuspend [0x526BB7F4]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceHprmResume(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceHprmResume [0x2C7B8B05]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceHprmSetConnectCallback(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceHprmSetConnectCallback [0xD22913DB]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceHprmRegisterCallback(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceHprmRegisterCallback [0xC7154136]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceHprmUnregisterCallback(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceHprmUnregisterCallback [0x444ED0B7]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceHprm_driver_71B5FB67(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceHprm_driver_71B5FB67 [0x71B5FB67]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceHprmIsRemoteExist(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceHprmIsRemoteExist [0x208DB1BD]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceHprmIsHeadphoneExist(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceHprmIsHeadphoneExist [0x7E69EDA4]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceHprmIsMicrophoneExist(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceHprmIsMicrophoneExist [0x219C58F1]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceHprmReset(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceHprmReset [0x4D1E622C]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceHprmGetInternalState(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceHprmGetInternalState [0x7B038374]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceHprm_driver_F04591FA(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceHprm_driver_F04591FA [0xF04591FA]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceHprm_driver_971AE8FB(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceHprm_driver_971AE8FB [0x971AE8FB]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceHprmGetModel(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceHprmGetModel [0xBAD0828E]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceHprmPeekCurrentKey(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceHprmPeekCurrentKey [0x1910B327]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceHprmPeekLatch(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceHprmPeekLatch [0x2BCEC83E]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceHprmReadLatch(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceHprmReadLatch [0x40D2F9F0]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public final HLEModuleFunction sceHprmInitFunction = new HLEModuleFunction("sceHprm_driver", "sceHprmInit") {

        @Override
        public final void execute(Processor processor) {
            sceHprmInit(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceHprm_driverModule.sceHprmInit(processor);";
        }
    };

    public final HLEModuleFunction sceHprmEndFunction = new HLEModuleFunction("sceHprm_driver", "sceHprmEnd") {

        @Override
        public final void execute(Processor processor) {
            sceHprmEnd(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceHprm_driverModule.sceHprmEnd(processor);";
        }
    };

    public final HLEModuleFunction sceHprmSuspendFunction = new HLEModuleFunction("sceHprm_driver", "sceHprmSuspend") {

        @Override
        public final void execute(Processor processor) {
            sceHprmSuspend(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceHprm_driverModule.sceHprmSuspend(processor);";
        }
    };

    public final HLEModuleFunction sceHprmResumeFunction = new HLEModuleFunction("sceHprm_driver", "sceHprmResume") {

        @Override
        public final void execute(Processor processor) {
            sceHprmResume(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceHprm_driverModule.sceHprmResume(processor);";
        }
    };

    public final HLEModuleFunction sceHprmSetConnectCallbackFunction = new HLEModuleFunction("sceHprm_driver", "sceHprmSetConnectCallback") {

        @Override
        public final void execute(Processor processor) {
            sceHprmSetConnectCallback(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceHprm_driverModule.sceHprmSetConnectCallback(processor);";
        }
    };

    public final HLEModuleFunction sceHprmRegisterCallbackFunction = new HLEModuleFunction("sceHprm_driver", "sceHprmRegisterCallback") {

        @Override
        public final void execute(Processor processor) {
            sceHprmRegisterCallback(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceHprm_driverModule.sceHprmRegisterCallback(processor);";
        }
    };

    public final HLEModuleFunction sceHprmUnregisterCallbackFunction = new HLEModuleFunction("sceHprm_driver", "sceHprmUnregisterCallback") {

        @Override
        public final void execute(Processor processor) {
            sceHprmUnregisterCallback(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceHprm_driverModule.sceHprmUnregisterCallback(processor);";
        }
    };

    public final HLEModuleFunction sceHprm_driver_71B5FB67Function = new HLEModuleFunction("sceHprm_driver", "sceHprm_driver_71B5FB67") {

        @Override
        public final void execute(Processor processor) {
            sceHprm_driver_71B5FB67(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceHprm_driverModule.sceHprm_driver_71B5FB67(processor);";
        }
    };

    public final HLEModuleFunction sceHprmIsRemoteExistFunction = new HLEModuleFunction("sceHprm_driver", "sceHprmIsRemoteExist") {

        @Override
        public final void execute(Processor processor) {
            sceHprmIsRemoteExist(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceHprm_driverModule.sceHprmIsRemoteExist(processor);";
        }
    };

    public final HLEModuleFunction sceHprmIsHeadphoneExistFunction = new HLEModuleFunction("sceHprm_driver", "sceHprmIsHeadphoneExist") {

        @Override
        public final void execute(Processor processor) {
            sceHprmIsHeadphoneExist(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceHprm_driverModule.sceHprmIsHeadphoneExist(processor);";
        }
    };

    public final HLEModuleFunction sceHprmIsMicrophoneExistFunction = new HLEModuleFunction("sceHprm_driver", "sceHprmIsMicrophoneExist") {

        @Override
        public final void execute(Processor processor) {
            sceHprmIsMicrophoneExist(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceHprm_driverModule.sceHprmIsMicrophoneExist(processor);";
        }
    };

    public final HLEModuleFunction sceHprmResetFunction = new HLEModuleFunction("sceHprm_driver", "sceHprmReset") {

        @Override
        public final void execute(Processor processor) {
            sceHprmReset(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceHprm_driverModule.sceHprmReset(processor);";
        }
    };

    public final HLEModuleFunction sceHprmGetInternalStateFunction = new HLEModuleFunction("sceHprm_driver", "sceHprmGetInternalState") {

        @Override
        public final void execute(Processor processor) {
            sceHprmGetInternalState(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceHprm_driverModule.sceHprmGetInternalState(processor);";
        }
    };

    public final HLEModuleFunction sceHprm_driver_F04591FAFunction = new HLEModuleFunction("sceHprm_driver", "sceHprm_driver_F04591FA") {

        @Override
        public final void execute(Processor processor) {
            sceHprm_driver_F04591FA(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceHprm_driverModule.sceHprm_driver_F04591FA(processor);";
        }
    };

    public final HLEModuleFunction sceHprm_driver_971AE8FBFunction = new HLEModuleFunction("sceHprm_driver", "sceHprm_driver_971AE8FB") {

        @Override
        public final void execute(Processor processor) {
            sceHprm_driver_971AE8FB(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceHprm_driverModule.sceHprm_driver_971AE8FB(processor);";
        }
    };

    public final HLEModuleFunction sceHprmGetModelFunction = new HLEModuleFunction("sceHprm_driver", "sceHprmGetModel") {

        @Override
        public final void execute(Processor processor) {
            sceHprmGetModel(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceHprm_driverModule.sceHprmGetModel(processor);";
        }
    };

    public final HLEModuleFunction sceHprmPeekCurrentKeyFunction = new HLEModuleFunction("sceHprm_driver", "sceHprmPeekCurrentKey") {

        @Override
        public final void execute(Processor processor) {
            sceHprmPeekCurrentKey(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceHprm_driverModule.sceHprmPeekCurrentKey(processor);";
        }
    };

    public final HLEModuleFunction sceHprmPeekLatchFunction = new HLEModuleFunction("sceHprm_driver", "sceHprmPeekLatch") {

        @Override
        public final void execute(Processor processor) {
            sceHprmPeekLatch(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceHprm_driverModule.sceHprmPeekLatch(processor);";
        }
    };

    public final HLEModuleFunction sceHprmReadLatchFunction = new HLEModuleFunction("sceHprm_driver", "sceHprmReadLatch") {

        @Override
        public final void execute(Processor processor) {
            sceHprmReadLatch(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceHprm_driverModule.sceHprmReadLatch(processor);";
        }
    };
}

;
