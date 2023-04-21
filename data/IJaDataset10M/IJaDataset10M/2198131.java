package jpcsp.HLE.modules150;

import jpcsp.HLE.Modules;
import jpcsp.HLE.modules.HLEModule;
import jpcsp.HLE.modules.HLEModuleFunction;
import jpcsp.HLE.modules.HLEModuleManager;
import jpcsp.Memory;
import jpcsp.Processor;
import jpcsp.Allegrex.CpuState;

public class sceAtrac3plus implements HLEModule {

    @Override
    public String getName() {
        return "sceAtrac3plus";
    }

    @Override
    public void installModule(HLEModuleManager mm, int version) {
        if (version >= 150) {
            mm.addFunction(sceAtracStartEntryFunction, 0xD1F59FDB);
            mm.addFunction(sceAtracEndEntryFunction, 0xD5C28CC0);
            mm.addFunction(sceAtracGetAtracIDFunction, 0x780F88D1);
            mm.addFunction(sceAtracReleaseAtracIDFunction, 0x61EB33F5);
            mm.addFunction(sceAtracSetDataFunction, 0x0E2A73AB);
            mm.addFunction(sceAtracSetHalfwayBufferFunction, 0x3F6E26B5);
            mm.addFunction(sceAtracSetDataAndGetIDFunction, 0x7A20E7AF);
            mm.addFunction(sceAtracSetHalfwayBufferAndGetIDFunction, 0x0FAE370E);
            mm.addFunction(sceAtracDecodeDataFunction, 0x6A8C3CD5);
            mm.addFunction(sceAtracGetRemainFrameFunction, 0x9AE849A7);
            mm.addFunction(sceAtracGetStreamDataInfoFunction, 0x5D268707);
            mm.addFunction(sceAtracAddStreamDataFunction, 0x7DB31251);
            mm.addFunction(sceAtracGetSecondBufferInfoFunction, 0x83E85EA0);
            mm.addFunction(sceAtracSetSecondBufferFunction, 0x83BF7AFD);
            mm.addFunction(sceAtracGetNextDecodePositionFunction, 0xE23E3A35);
            mm.addFunction(sceAtracGetSoundSampleFunction, 0xA2BBA8BE);
            mm.addFunction(sceAtracGetChannelFunction, 0x31668BAA);
            mm.addFunction(sceAtracGetMaxSampleFunction, 0xD6A5F2F7);
            mm.addFunction(sceAtracGetNextSampleFunction, 0x36FAABFB);
            mm.addFunction(sceAtracGetBitrateFunction, 0xA554A158);
            mm.addFunction(sceAtracGetLoopStatusFunction, 0xFAA4F89B);
            mm.addFunction(sceAtracSetLoopNumFunction, 0x868120B5);
            mm.addFunction(sceAtracGetBufferInfoForResetingFunction, 0xCA3CA3D2);
            mm.addFunction(sceAtracResetPlayPositionFunction, 0x644E5607);
            mm.addFunction(sceAtracGetInternalErrorInfoFunction, 0xE88F759B);
        }
    }

    @Override
    public void uninstallModule(HLEModuleManager mm, int version) {
        if (version >= 150) {
            mm.removeFunction(sceAtracStartEntryFunction);
            mm.removeFunction(sceAtracEndEntryFunction);
            mm.removeFunction(sceAtracGetAtracIDFunction);
            mm.removeFunction(sceAtracReleaseAtracIDFunction);
            mm.removeFunction(sceAtracSetDataFunction);
            mm.removeFunction(sceAtracSetHalfwayBufferFunction);
            mm.removeFunction(sceAtracSetDataAndGetIDFunction);
            mm.removeFunction(sceAtracSetHalfwayBufferAndGetIDFunction);
            mm.removeFunction(sceAtracDecodeDataFunction);
            mm.removeFunction(sceAtracGetRemainFrameFunction);
            mm.removeFunction(sceAtracGetStreamDataInfoFunction);
            mm.removeFunction(sceAtracAddStreamDataFunction);
            mm.removeFunction(sceAtracGetSecondBufferInfoFunction);
            mm.removeFunction(sceAtracSetSecondBufferFunction);
            mm.removeFunction(sceAtracGetNextDecodePositionFunction);
            mm.removeFunction(sceAtracGetSoundSampleFunction);
            mm.removeFunction(sceAtracGetChannelFunction);
            mm.removeFunction(sceAtracGetMaxSampleFunction);
            mm.removeFunction(sceAtracGetNextSampleFunction);
            mm.removeFunction(sceAtracGetBitrateFunction);
            mm.removeFunction(sceAtracGetLoopStatusFunction);
            mm.removeFunction(sceAtracSetLoopNumFunction);
            mm.removeFunction(sceAtracGetBufferInfoForResetingFunction);
            mm.removeFunction(sceAtracResetPlayPositionFunction);
            mm.removeFunction(sceAtracGetInternalErrorInfoFunction);
        }
    }

    public void sceAtracStartEntry(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceAtracStartEntry [0xD1F59FDB]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceAtracEndEntry(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceAtracEndEntry [0xD5C28CC0]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceAtracGetAtracID(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceAtracGetAtracID [0x780F88D1]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceAtracReleaseAtracID(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceAtracReleaseAtracID [0x61EB33F5]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceAtracSetData(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceAtracSetData [0x0E2A73AB]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceAtracSetHalfwayBuffer(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceAtracSetHalfwayBuffer [0x3F6E26B5]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceAtracSetDataAndGetID(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceAtracSetDataAndGetID [0x7A20E7AF]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceAtracSetHalfwayBufferAndGetID(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceAtracSetHalfwayBufferAndGetID [0x0FAE370E]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceAtracDecodeData(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceAtracDecodeData [0x6A8C3CD5]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceAtracGetRemainFrame(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceAtracGetRemainFrame [0x9AE849A7]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceAtracGetStreamDataInfo(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceAtracGetStreamDataInfo [0x5D268707]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceAtracAddStreamData(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceAtracAddStreamData [0x7DB31251]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceAtracGetSecondBufferInfo(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceAtracGetSecondBufferInfo [0x83E85EA0]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceAtracSetSecondBuffer(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceAtracSetSecondBuffer [0x83BF7AFD]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceAtracGetNextDecodePosition(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceAtracGetNextDecodePosition [0xE23E3A35]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceAtracGetSoundSample(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceAtracGetSoundSample [0xA2BBA8BE]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceAtracGetChannel(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceAtracGetChannel [0x31668BAA]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceAtracGetMaxSample(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceAtracGetMaxSample [0xD6A5F2F7]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceAtracGetNextSample(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceAtracGetNextSample [0x36FAABFB]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceAtracGetBitrate(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceAtracGetBitrate [0xA554A158]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceAtracGetLoopStatus(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceAtracGetLoopStatus [0xFAA4F89B]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceAtracSetLoopNum(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceAtracSetLoopNum [0x868120B5]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceAtracGetBufferInfoForReseting(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceAtracGetBufferInfoForReseting [0xCA3CA3D2]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceAtracResetPlayPosition(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceAtracResetPlayPosition [0x644E5607]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceAtracGetInternalErrorInfo(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceAtracGetInternalErrorInfo [0xE88F759B]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public final HLEModuleFunction sceAtracStartEntryFunction = new HLEModuleFunction("sceAtrac3plus", "sceAtracStartEntry") {

        @Override
        public final void execute(Processor processor) {
            sceAtracStartEntry(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceAtrac3plusModule.sceAtracStartEntry(processor);";
        }
    };

    public final HLEModuleFunction sceAtracEndEntryFunction = new HLEModuleFunction("sceAtrac3plus", "sceAtracEndEntry") {

        @Override
        public final void execute(Processor processor) {
            sceAtracEndEntry(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceAtrac3plusModule.sceAtracEndEntry(processor);";
        }
    };

    public final HLEModuleFunction sceAtracGetAtracIDFunction = new HLEModuleFunction("sceAtrac3plus", "sceAtracGetAtracID") {

        @Override
        public final void execute(Processor processor) {
            sceAtracGetAtracID(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceAtrac3plusModule.sceAtracGetAtracID(processor);";
        }
    };

    public final HLEModuleFunction sceAtracReleaseAtracIDFunction = new HLEModuleFunction("sceAtrac3plus", "sceAtracReleaseAtracID") {

        @Override
        public final void execute(Processor processor) {
            sceAtracReleaseAtracID(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceAtrac3plusModule.sceAtracReleaseAtracID(processor);";
        }
    };

    public final HLEModuleFunction sceAtracSetDataFunction = new HLEModuleFunction("sceAtrac3plus", "sceAtracSetData") {

        @Override
        public final void execute(Processor processor) {
            sceAtracSetData(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceAtrac3plusModule.sceAtracSetData(processor);";
        }
    };

    public final HLEModuleFunction sceAtracSetHalfwayBufferFunction = new HLEModuleFunction("sceAtrac3plus", "sceAtracSetHalfwayBuffer") {

        @Override
        public final void execute(Processor processor) {
            sceAtracSetHalfwayBuffer(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceAtrac3plusModule.sceAtracSetHalfwayBuffer(processor);";
        }
    };

    public final HLEModuleFunction sceAtracSetDataAndGetIDFunction = new HLEModuleFunction("sceAtrac3plus", "sceAtracSetDataAndGetID") {

        @Override
        public final void execute(Processor processor) {
            sceAtracSetDataAndGetID(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceAtrac3plusModule.sceAtracSetDataAndGetID(processor);";
        }
    };

    public final HLEModuleFunction sceAtracSetHalfwayBufferAndGetIDFunction = new HLEModuleFunction("sceAtrac3plus", "sceAtracSetHalfwayBufferAndGetID") {

        @Override
        public final void execute(Processor processor) {
            sceAtracSetHalfwayBufferAndGetID(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceAtrac3plusModule.sceAtracSetHalfwayBufferAndGetID(processor);";
        }
    };

    public final HLEModuleFunction sceAtracDecodeDataFunction = new HLEModuleFunction("sceAtrac3plus", "sceAtracDecodeData") {

        @Override
        public final void execute(Processor processor) {
            sceAtracDecodeData(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceAtrac3plusModule.sceAtracDecodeData(processor);";
        }
    };

    public final HLEModuleFunction sceAtracGetRemainFrameFunction = new HLEModuleFunction("sceAtrac3plus", "sceAtracGetRemainFrame") {

        @Override
        public final void execute(Processor processor) {
            sceAtracGetRemainFrame(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceAtrac3plusModule.sceAtracGetRemainFrame(processor);";
        }
    };

    public final HLEModuleFunction sceAtracGetStreamDataInfoFunction = new HLEModuleFunction("sceAtrac3plus", "sceAtracGetStreamDataInfo") {

        @Override
        public final void execute(Processor processor) {
            sceAtracGetStreamDataInfo(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceAtrac3plusModule.sceAtracGetStreamDataInfo(processor);";
        }
    };

    public final HLEModuleFunction sceAtracAddStreamDataFunction = new HLEModuleFunction("sceAtrac3plus", "sceAtracAddStreamData") {

        @Override
        public final void execute(Processor processor) {
            sceAtracAddStreamData(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceAtrac3plusModule.sceAtracAddStreamData(processor);";
        }
    };

    public final HLEModuleFunction sceAtracGetSecondBufferInfoFunction = new HLEModuleFunction("sceAtrac3plus", "sceAtracGetSecondBufferInfo") {

        @Override
        public final void execute(Processor processor) {
            sceAtracGetSecondBufferInfo(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceAtrac3plusModule.sceAtracGetSecondBufferInfo(processor);";
        }
    };

    public final HLEModuleFunction sceAtracSetSecondBufferFunction = new HLEModuleFunction("sceAtrac3plus", "sceAtracSetSecondBuffer") {

        @Override
        public final void execute(Processor processor) {
            sceAtracSetSecondBuffer(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceAtrac3plusModule.sceAtracSetSecondBuffer(processor);";
        }
    };

    public final HLEModuleFunction sceAtracGetNextDecodePositionFunction = new HLEModuleFunction("sceAtrac3plus", "sceAtracGetNextDecodePosition") {

        @Override
        public final void execute(Processor processor) {
            sceAtracGetNextDecodePosition(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceAtrac3plusModule.sceAtracGetNextDecodePosition(processor);";
        }
    };

    public final HLEModuleFunction sceAtracGetSoundSampleFunction = new HLEModuleFunction("sceAtrac3plus", "sceAtracGetSoundSample") {

        @Override
        public final void execute(Processor processor) {
            sceAtracGetSoundSample(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceAtrac3plusModule.sceAtracGetSoundSample(processor);";
        }
    };

    public final HLEModuleFunction sceAtracGetChannelFunction = new HLEModuleFunction("sceAtrac3plus", "sceAtracGetChannel") {

        @Override
        public final void execute(Processor processor) {
            sceAtracGetChannel(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceAtrac3plusModule.sceAtracGetChannel(processor);";
        }
    };

    public final HLEModuleFunction sceAtracGetMaxSampleFunction = new HLEModuleFunction("sceAtrac3plus", "sceAtracGetMaxSample") {

        @Override
        public final void execute(Processor processor) {
            sceAtracGetMaxSample(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceAtrac3plusModule.sceAtracGetMaxSample(processor);";
        }
    };

    public final HLEModuleFunction sceAtracGetNextSampleFunction = new HLEModuleFunction("sceAtrac3plus", "sceAtracGetNextSample") {

        @Override
        public final void execute(Processor processor) {
            sceAtracGetNextSample(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceAtrac3plusModule.sceAtracGetNextSample(processor);";
        }
    };

    public final HLEModuleFunction sceAtracGetBitrateFunction = new HLEModuleFunction("sceAtrac3plus", "sceAtracGetBitrate") {

        @Override
        public final void execute(Processor processor) {
            sceAtracGetBitrate(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceAtrac3plusModule.sceAtracGetBitrate(processor);";
        }
    };

    public final HLEModuleFunction sceAtracGetLoopStatusFunction = new HLEModuleFunction("sceAtrac3plus", "sceAtracGetLoopStatus") {

        @Override
        public final void execute(Processor processor) {
            sceAtracGetLoopStatus(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceAtrac3plusModule.sceAtracGetLoopStatus(processor);";
        }
    };

    public final HLEModuleFunction sceAtracSetLoopNumFunction = new HLEModuleFunction("sceAtrac3plus", "sceAtracSetLoopNum") {

        @Override
        public final void execute(Processor processor) {
            sceAtracSetLoopNum(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceAtrac3plusModule.sceAtracSetLoopNum(processor);";
        }
    };

    public final HLEModuleFunction sceAtracGetBufferInfoForResetingFunction = new HLEModuleFunction("sceAtrac3plus", "sceAtracGetBufferInfoForReseting") {

        @Override
        public final void execute(Processor processor) {
            sceAtracGetBufferInfoForReseting(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceAtrac3plusModule.sceAtracGetBufferInfoForReseting(processor);";
        }
    };

    public final HLEModuleFunction sceAtracResetPlayPositionFunction = new HLEModuleFunction("sceAtrac3plus", "sceAtracResetPlayPosition") {

        @Override
        public final void execute(Processor processor) {
            sceAtracResetPlayPosition(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceAtrac3plusModule.sceAtracResetPlayPosition(processor);";
        }
    };

    public final HLEModuleFunction sceAtracGetInternalErrorInfoFunction = new HLEModuleFunction("sceAtrac3plus", "sceAtracGetInternalErrorInfo") {

        @Override
        public final void execute(Processor processor) {
            sceAtracGetInternalErrorInfo(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceAtrac3plusModule.sceAtracGetInternalErrorInfo(processor);";
        }
    };
}

;
