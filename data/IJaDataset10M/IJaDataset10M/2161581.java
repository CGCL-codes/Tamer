package jpcsp.HLE.modules150;

import jpcsp.HLE.Modules;
import jpcsp.HLE.modules.HLEModule;
import jpcsp.HLE.modules.HLEModuleFunction;
import jpcsp.HLE.modules.HLEModuleManager;
import jpcsp.Memory;
import jpcsp.Processor;
import jpcsp.Allegrex.CpuState;

public class sceSuspendForUser implements HLEModule {

    @Override
    public String getName() {
        return "sceSuspendForUser";
    }

    @Override
    public void installModule(HLEModuleManager mm, int version) {
        if (version >= 150) {
            mm.addFunction(sceKernelPowerLockFunction, 0xEADB1BD7);
            mm.addFunction(sceKernelPowerUnlockFunction, 0x3AEE7261);
            mm.addFunction(sceKernelPowerTickFunction, 0x090CCB3F);
            mm.addFunction(sceKernelVolatileMemLockFunction, 0x3E0271D3);
            mm.addFunction(sceKernelVolatileMemTryLockFunction, 0xA14F40B2);
            mm.addFunction(sceKernelVolatileMemUnlockFunction, 0xA569E425);
        }
    }

    @Override
    public void uninstallModule(HLEModuleManager mm, int version) {
        if (version >= 150) {
            mm.removeFunction(sceKernelPowerLockFunction);
            mm.removeFunction(sceKernelPowerUnlockFunction);
            mm.removeFunction(sceKernelPowerTickFunction);
            mm.removeFunction(sceKernelVolatileMemLockFunction);
            mm.removeFunction(sceKernelVolatileMemTryLockFunction);
            mm.removeFunction(sceKernelVolatileMemUnlockFunction);
        }
    }

    public void sceKernelPowerLock(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceKernelPowerLock [0xEADB1BD7]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceKernelPowerUnlock(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceKernelPowerUnlock [0x3AEE7261]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceKernelPowerTick(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceKernelPowerTick [0x090CCB3F]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceKernelVolatileMemLock(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceKernelVolatileMemLock [0x3E0271D3]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceKernelVolatileMemTryLock(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceKernelVolatileMemTryLock [0xA14F40B2]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceKernelVolatileMemUnlock(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceKernelVolatileMemUnlock [0xA569E425]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public final HLEModuleFunction sceKernelPowerLockFunction = new HLEModuleFunction("sceSuspendForUser", "sceKernelPowerLock") {

        @Override
        public final void execute(Processor processor) {
            sceKernelPowerLock(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceSuspendForUserModule.sceKernelPowerLock(processor);";
        }
    };

    public final HLEModuleFunction sceKernelPowerUnlockFunction = new HLEModuleFunction("sceSuspendForUser", "sceKernelPowerUnlock") {

        @Override
        public final void execute(Processor processor) {
            sceKernelPowerUnlock(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceSuspendForUserModule.sceKernelPowerUnlock(processor);";
        }
    };

    public final HLEModuleFunction sceKernelPowerTickFunction = new HLEModuleFunction("sceSuspendForUser", "sceKernelPowerTick") {

        @Override
        public final void execute(Processor processor) {
            sceKernelPowerTick(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceSuspendForUserModule.sceKernelPowerTick(processor);";
        }
    };

    public final HLEModuleFunction sceKernelVolatileMemLockFunction = new HLEModuleFunction("sceSuspendForUser", "sceKernelVolatileMemLock") {

        @Override
        public final void execute(Processor processor) {
            sceKernelVolatileMemLock(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceSuspendForUserModule.sceKernelVolatileMemLock(processor);";
        }
    };

    public final HLEModuleFunction sceKernelVolatileMemTryLockFunction = new HLEModuleFunction("sceSuspendForUser", "sceKernelVolatileMemTryLock") {

        @Override
        public final void execute(Processor processor) {
            sceKernelVolatileMemTryLock(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceSuspendForUserModule.sceKernelVolatileMemTryLock(processor);";
        }
    };

    public final HLEModuleFunction sceKernelVolatileMemUnlockFunction = new HLEModuleFunction("sceSuspendForUser", "sceKernelVolatileMemUnlock") {

        @Override
        public final void execute(Processor processor) {
            sceKernelVolatileMemUnlock(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceSuspendForUserModule.sceKernelVolatileMemUnlock(processor);";
        }
    };
}

;
