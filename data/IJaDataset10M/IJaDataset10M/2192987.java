package jpcsp.HLE.modules150;

import jpcsp.HLE.Modules;
import jpcsp.HLE.modules.HLEModule;
import jpcsp.HLE.modules.HLEModuleFunction;
import jpcsp.HLE.modules.HLEModuleManager;
import jpcsp.Memory;
import jpcsp.Processor;
import jpcsp.Allegrex.CpuState;

public class sceCodec_driver implements HLEModule {

    @Override
    public String getName() {
        return "sceCodec_driver";
    }

    @Override
    public void installModule(HLEModuleManager mm, int version) {
        if (version >= 150) {
            mm.addFunction(sceCodecInitEntryFunction, 0xBD8E0977);
            mm.addFunction(sceCodecStopEntryFunction, 0x02133959);
            mm.addFunction(sceCodecOutputEnableFunction, 0x856E7487);
            mm.addFunction(sceCodecOutputDisableFunction, 0x359C2B9F);
            mm.addFunction(sceCodecInputEnableFunction, 0xC513C747);
            mm.addFunction(sceCodecInputDisableFunction, 0x31B2E41E);
            mm.addFunction(sceCodecSetOutputVolumeFunction, 0x261C6EE8);
            mm.addFunction(sceCodecSetHeadphoneVolumeFunction, 0x6D945509);
            mm.addFunction(sceCodecSetSpeakerVolumeFunction, 0x40D5C897);
            mm.addFunction(sceCodecSetFrequencyFunction, 0xDFBCACF3);
            mm.addFunction(sceCodec_driver_56494D70Function, 0x56494D70);
            mm.addFunction(sceCodec_driver_4515AE04Function, 0x4515AE04);
            mm.addFunction(sceCodecSetVolumeOffsetFunction, 0xEEB91526);
            mm.addFunction(sceCodec_driver_3064C53DFunction, 0x3064C53D);
            mm.addFunction(sceCodecSelectVolumeTableFunction, 0x20C61103);
        }
    }

    @Override
    public void uninstallModule(HLEModuleManager mm, int version) {
        if (version >= 150) {
            mm.removeFunction(sceCodecInitEntryFunction);
            mm.removeFunction(sceCodecStopEntryFunction);
            mm.removeFunction(sceCodecOutputEnableFunction);
            mm.removeFunction(sceCodecOutputDisableFunction);
            mm.removeFunction(sceCodecInputEnableFunction);
            mm.removeFunction(sceCodecInputDisableFunction);
            mm.removeFunction(sceCodecSetOutputVolumeFunction);
            mm.removeFunction(sceCodecSetHeadphoneVolumeFunction);
            mm.removeFunction(sceCodecSetSpeakerVolumeFunction);
            mm.removeFunction(sceCodecSetFrequencyFunction);
            mm.removeFunction(sceCodec_driver_56494D70Function);
            mm.removeFunction(sceCodec_driver_4515AE04Function);
            mm.removeFunction(sceCodecSetVolumeOffsetFunction);
            mm.removeFunction(sceCodec_driver_3064C53DFunction);
            mm.removeFunction(sceCodecSelectVolumeTableFunction);
        }
    }

    public void sceCodecInitEntry(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCodecInitEntry [0xBD8E0977]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCodecStopEntry(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCodecStopEntry [0x02133959]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCodecOutputEnable(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCodecOutputEnable [0x856E7487]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCodecOutputDisable(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCodecOutputDisable [0x359C2B9F]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCodecInputEnable(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCodecInputEnable [0xC513C747]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCodecInputDisable(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCodecInputDisable [0x31B2E41E]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCodecSetOutputVolume(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCodecSetOutputVolume [0x261C6EE8]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCodecSetHeadphoneVolume(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCodecSetHeadphoneVolume [0x6D945509]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCodecSetSpeakerVolume(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCodecSetSpeakerVolume [0x40D5C897]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCodecSetFrequency(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCodecSetFrequency [0xDFBCACF3]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCodec_driver_56494D70(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCodec_driver_56494D70 [0x56494D70]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCodec_driver_4515AE04(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCodec_driver_4515AE04 [0x4515AE04]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCodecSetVolumeOffset(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCodecSetVolumeOffset [0xEEB91526]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCodec_driver_3064C53D(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCodec_driver_3064C53D [0x3064C53D]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceCodecSelectVolumeTable(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceCodecSelectVolumeTable [0x20C61103]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public final HLEModuleFunction sceCodecInitEntryFunction = new HLEModuleFunction("sceCodec_driver", "sceCodecInitEntry") {

        @Override
        public final void execute(Processor processor) {
            sceCodecInitEntry(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCodec_driverModule.sceCodecInitEntry(processor);";
        }
    };

    public final HLEModuleFunction sceCodecStopEntryFunction = new HLEModuleFunction("sceCodec_driver", "sceCodecStopEntry") {

        @Override
        public final void execute(Processor processor) {
            sceCodecStopEntry(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCodec_driverModule.sceCodecStopEntry(processor);";
        }
    };

    public final HLEModuleFunction sceCodecOutputEnableFunction = new HLEModuleFunction("sceCodec_driver", "sceCodecOutputEnable") {

        @Override
        public final void execute(Processor processor) {
            sceCodecOutputEnable(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCodec_driverModule.sceCodecOutputEnable(processor);";
        }
    };

    public final HLEModuleFunction sceCodecOutputDisableFunction = new HLEModuleFunction("sceCodec_driver", "sceCodecOutputDisable") {

        @Override
        public final void execute(Processor processor) {
            sceCodecOutputDisable(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCodec_driverModule.sceCodecOutputDisable(processor);";
        }
    };

    public final HLEModuleFunction sceCodecInputEnableFunction = new HLEModuleFunction("sceCodec_driver", "sceCodecInputEnable") {

        @Override
        public final void execute(Processor processor) {
            sceCodecInputEnable(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCodec_driverModule.sceCodecInputEnable(processor);";
        }
    };

    public final HLEModuleFunction sceCodecInputDisableFunction = new HLEModuleFunction("sceCodec_driver", "sceCodecInputDisable") {

        @Override
        public final void execute(Processor processor) {
            sceCodecInputDisable(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCodec_driverModule.sceCodecInputDisable(processor);";
        }
    };

    public final HLEModuleFunction sceCodecSetOutputVolumeFunction = new HLEModuleFunction("sceCodec_driver", "sceCodecSetOutputVolume") {

        @Override
        public final void execute(Processor processor) {
            sceCodecSetOutputVolume(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCodec_driverModule.sceCodecSetOutputVolume(processor);";
        }
    };

    public final HLEModuleFunction sceCodecSetHeadphoneVolumeFunction = new HLEModuleFunction("sceCodec_driver", "sceCodecSetHeadphoneVolume") {

        @Override
        public final void execute(Processor processor) {
            sceCodecSetHeadphoneVolume(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCodec_driverModule.sceCodecSetHeadphoneVolume(processor);";
        }
    };

    public final HLEModuleFunction sceCodecSetSpeakerVolumeFunction = new HLEModuleFunction("sceCodec_driver", "sceCodecSetSpeakerVolume") {

        @Override
        public final void execute(Processor processor) {
            sceCodecSetSpeakerVolume(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCodec_driverModule.sceCodecSetSpeakerVolume(processor);";
        }
    };

    public final HLEModuleFunction sceCodecSetFrequencyFunction = new HLEModuleFunction("sceCodec_driver", "sceCodecSetFrequency") {

        @Override
        public final void execute(Processor processor) {
            sceCodecSetFrequency(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCodec_driverModule.sceCodecSetFrequency(processor);";
        }
    };

    public final HLEModuleFunction sceCodec_driver_56494D70Function = new HLEModuleFunction("sceCodec_driver", "sceCodec_driver_56494D70") {

        @Override
        public final void execute(Processor processor) {
            sceCodec_driver_56494D70(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCodec_driverModule.sceCodec_driver_56494D70(processor);";
        }
    };

    public final HLEModuleFunction sceCodec_driver_4515AE04Function = new HLEModuleFunction("sceCodec_driver", "sceCodec_driver_4515AE04") {

        @Override
        public final void execute(Processor processor) {
            sceCodec_driver_4515AE04(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCodec_driverModule.sceCodec_driver_4515AE04(processor);";
        }
    };

    public final HLEModuleFunction sceCodecSetVolumeOffsetFunction = new HLEModuleFunction("sceCodec_driver", "sceCodecSetVolumeOffset") {

        @Override
        public final void execute(Processor processor) {
            sceCodecSetVolumeOffset(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCodec_driverModule.sceCodecSetVolumeOffset(processor);";
        }
    };

    public final HLEModuleFunction sceCodec_driver_3064C53DFunction = new HLEModuleFunction("sceCodec_driver", "sceCodec_driver_3064C53D") {

        @Override
        public final void execute(Processor processor) {
            sceCodec_driver_3064C53D(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCodec_driverModule.sceCodec_driver_3064C53D(processor);";
        }
    };

    public final HLEModuleFunction sceCodecSelectVolumeTableFunction = new HLEModuleFunction("sceCodec_driver", "sceCodecSelectVolumeTable") {

        @Override
        public final void execute(Processor processor) {
            sceCodecSelectVolumeTable(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceCodec_driverModule.sceCodecSelectVolumeTable(processor);";
        }
    };
}

;
