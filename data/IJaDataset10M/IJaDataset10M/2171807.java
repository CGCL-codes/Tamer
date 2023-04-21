package jpcsp.HLE.modules150;

import jpcsp.HLE.Modules;
import jpcsp.HLE.modules.HLEModule;
import jpcsp.HLE.modules.HLEModuleFunction;
import jpcsp.HLE.modules.HLEModuleManager;
import jpcsp.Memory;
import jpcsp.Processor;
import jpcsp.Allegrex.CpuState;

public class sceNetInet implements HLEModule {

    @Override
    public String getName() {
        return "sceNetInet";
    }

    @Override
    public void installModule(HLEModuleManager mm, int version) {
        if (version >= 150) {
            mm.addFunction(sceNetInetInitFunction, 0x17943399);
            mm.addFunction(sceNetInetTermFunction, 0xA9ED66B9);
            mm.addFunction(sceNetInetAcceptFunction, 0xDB094E1B);
            mm.addFunction(sceNetInetBindFunction, 0x1A33F9AE);
            mm.addFunction(sceNetInetCloseFunction, 0x8D7284EA);
            mm.addFunction(sceNetInetCloseWithRSTFunction, 0x805502DD);
            mm.addFunction(sceNetInetConnectFunction, 0x410B34AA);
            mm.addFunction(sceNetInetGetpeernameFunction, 0xE247B6D6);
            mm.addFunction(sceNetInetGetsocknameFunction, 0x162E6FD5);
            mm.addFunction(sceNetInetGetsockoptFunction, 0x4A114C7C);
            mm.addFunction(sceNetInetListenFunction, 0xD10A1A7A);
            mm.addFunction(sceNetInetPollFunction, 0xFAABB1DD);
            mm.addFunction(sceNetInetRecvFunction, 0xCDA85C99);
            mm.addFunction(sceNetInetRecvfromFunction, 0xC91142E4);
            mm.addFunction(sceNetInetRecvmsgFunction, 0xEECE61D2);
            mm.addFunction(sceNetInetSelectFunction, 0x5BE8D595);
            mm.addFunction(sceNetInetSendFunction, 0x7AA671BC);
            mm.addFunction(sceNetInetSendtoFunction, 0x05038FC7);
            mm.addFunction(sceNetInetSendmsgFunction, 0x774E36F4);
            mm.addFunction(sceNetInetSetsockoptFunction, 0x2FE71FE7);
            mm.addFunction(sceNetInetShutdownFunction, 0x4CFE4E56);
            mm.addFunction(sceNetInetSocketFunction, 0x8B7B220F);
            mm.addFunction(sceNetInetSocketAbortFunction, 0x80A21ABD);
            mm.addFunction(sceNetInetGetErrnoFunction, 0xFBABE411);
            mm.addFunction(sceNetInetGetTcpcbstatFunction, 0xB3888AD4);
            mm.addFunction(sceNetInetGetUdpcbstatFunction, 0x39B0C7D3);
            mm.addFunction(sceNetInetInetAddrFunction, 0xB75D5B0A);
            mm.addFunction(sceNetInetInetAtonFunction, 0x1BDF5D13);
            mm.addFunction(sceNetInetInetNtopFunction, 0xD0792666);
            mm.addFunction(sceNetInetInetPtonFunction, 0xE30B8C19);
        }
    }

    @Override
    public void uninstallModule(HLEModuleManager mm, int version) {
        if (version >= 150) {
            mm.removeFunction(sceNetInetInitFunction);
            mm.removeFunction(sceNetInetTermFunction);
            mm.removeFunction(sceNetInetAcceptFunction);
            mm.removeFunction(sceNetInetBindFunction);
            mm.removeFunction(sceNetInetCloseFunction);
            mm.removeFunction(sceNetInetCloseWithRSTFunction);
            mm.removeFunction(sceNetInetConnectFunction);
            mm.removeFunction(sceNetInetGetpeernameFunction);
            mm.removeFunction(sceNetInetGetsocknameFunction);
            mm.removeFunction(sceNetInetGetsockoptFunction);
            mm.removeFunction(sceNetInetListenFunction);
            mm.removeFunction(sceNetInetPollFunction);
            mm.removeFunction(sceNetInetRecvFunction);
            mm.removeFunction(sceNetInetRecvfromFunction);
            mm.removeFunction(sceNetInetRecvmsgFunction);
            mm.removeFunction(sceNetInetSelectFunction);
            mm.removeFunction(sceNetInetSendFunction);
            mm.removeFunction(sceNetInetSendtoFunction);
            mm.removeFunction(sceNetInetSendmsgFunction);
            mm.removeFunction(sceNetInetSetsockoptFunction);
            mm.removeFunction(sceNetInetShutdownFunction);
            mm.removeFunction(sceNetInetSocketFunction);
            mm.removeFunction(sceNetInetSocketAbortFunction);
            mm.removeFunction(sceNetInetGetErrnoFunction);
            mm.removeFunction(sceNetInetGetTcpcbstatFunction);
            mm.removeFunction(sceNetInetGetUdpcbstatFunction);
            mm.removeFunction(sceNetInetInetAddrFunction);
            mm.removeFunction(sceNetInetInetAtonFunction);
            mm.removeFunction(sceNetInetInetNtopFunction);
            mm.removeFunction(sceNetInetInetPtonFunction);
        }
    }

    public void sceNetInetInit(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceNetInetInit [0x17943399]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceNetInetTerm(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceNetInetTerm [0xA9ED66B9]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceNetInetAccept(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceNetInetAccept [0xDB094E1B]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceNetInetBind(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceNetInetBind [0x1A33F9AE]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceNetInetClose(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceNetInetClose [0x8D7284EA]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceNetInetCloseWithRST(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceNetInetCloseWithRST [0x805502DD]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceNetInetConnect(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceNetInetConnect [0x410B34AA]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceNetInetGetpeername(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceNetInetGetpeername [0xE247B6D6]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceNetInetGetsockname(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceNetInetGetsockname [0x162E6FD5]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceNetInetGetsockopt(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceNetInetGetsockopt [0x4A114C7C]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceNetInetListen(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceNetInetListen [0xD10A1A7A]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceNetInetPoll(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceNetInetPoll [0xFAABB1DD]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceNetInetRecv(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceNetInetRecv [0xCDA85C99]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceNetInetRecvfrom(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceNetInetRecvfrom [0xC91142E4]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceNetInetRecvmsg(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceNetInetRecvmsg [0xEECE61D2]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceNetInetSelect(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceNetInetSelect [0x5BE8D595]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceNetInetSend(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceNetInetSend [0x7AA671BC]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceNetInetSendto(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceNetInetSendto [0x05038FC7]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceNetInetSendmsg(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceNetInetSendmsg [0x774E36F4]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceNetInetSetsockopt(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceNetInetSetsockopt [0x2FE71FE7]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceNetInetShutdown(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceNetInetShutdown [0x4CFE4E56]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceNetInetSocket(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceNetInetSocket [0x8B7B220F]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceNetInetSocketAbort(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceNetInetSocketAbort [0x80A21ABD]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceNetInetGetErrno(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceNetInetGetErrno [0xFBABE411]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceNetInetGetTcpcbstat(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceNetInetGetTcpcbstat [0xB3888AD4]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceNetInetGetUdpcbstat(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceNetInetGetUdpcbstat [0x39B0C7D3]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceNetInetInetAddr(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceNetInetInetAddr [0xB75D5B0A]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceNetInetInetAton(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceNetInetInetAton [0x1BDF5D13]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceNetInetInetNtop(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceNetInetInetNtop [0xD0792666]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public void sceNetInetInetPton(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function sceNetInetInetPton [0xE30B8C19]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public final HLEModuleFunction sceNetInetInitFunction = new HLEModuleFunction("sceNetInet", "sceNetInetInit") {

        @Override
        public final void execute(Processor processor) {
            sceNetInetInit(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceNetInetModule.sceNetInetInit(processor);";
        }
    };

    public final HLEModuleFunction sceNetInetTermFunction = new HLEModuleFunction("sceNetInet", "sceNetInetTerm") {

        @Override
        public final void execute(Processor processor) {
            sceNetInetTerm(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceNetInetModule.sceNetInetTerm(processor);";
        }
    };

    public final HLEModuleFunction sceNetInetAcceptFunction = new HLEModuleFunction("sceNetInet", "sceNetInetAccept") {

        @Override
        public final void execute(Processor processor) {
            sceNetInetAccept(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceNetInetModule.sceNetInetAccept(processor);";
        }
    };

    public final HLEModuleFunction sceNetInetBindFunction = new HLEModuleFunction("sceNetInet", "sceNetInetBind") {

        @Override
        public final void execute(Processor processor) {
            sceNetInetBind(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceNetInetModule.sceNetInetBind(processor);";
        }
    };

    public final HLEModuleFunction sceNetInetCloseFunction = new HLEModuleFunction("sceNetInet", "sceNetInetClose") {

        @Override
        public final void execute(Processor processor) {
            sceNetInetClose(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceNetInetModule.sceNetInetClose(processor);";
        }
    };

    public final HLEModuleFunction sceNetInetCloseWithRSTFunction = new HLEModuleFunction("sceNetInet", "sceNetInetCloseWithRST") {

        @Override
        public final void execute(Processor processor) {
            sceNetInetCloseWithRST(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceNetInetModule.sceNetInetCloseWithRST(processor);";
        }
    };

    public final HLEModuleFunction sceNetInetConnectFunction = new HLEModuleFunction("sceNetInet", "sceNetInetConnect") {

        @Override
        public final void execute(Processor processor) {
            sceNetInetConnect(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceNetInetModule.sceNetInetConnect(processor);";
        }
    };

    public final HLEModuleFunction sceNetInetGetpeernameFunction = new HLEModuleFunction("sceNetInet", "sceNetInetGetpeername") {

        @Override
        public final void execute(Processor processor) {
            sceNetInetGetpeername(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceNetInetModule.sceNetInetGetpeername(processor);";
        }
    };

    public final HLEModuleFunction sceNetInetGetsocknameFunction = new HLEModuleFunction("sceNetInet", "sceNetInetGetsockname") {

        @Override
        public final void execute(Processor processor) {
            sceNetInetGetsockname(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceNetInetModule.sceNetInetGetsockname(processor);";
        }
    };

    public final HLEModuleFunction sceNetInetGetsockoptFunction = new HLEModuleFunction("sceNetInet", "sceNetInetGetsockopt") {

        @Override
        public final void execute(Processor processor) {
            sceNetInetGetsockopt(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceNetInetModule.sceNetInetGetsockopt(processor);";
        }
    };

    public final HLEModuleFunction sceNetInetListenFunction = new HLEModuleFunction("sceNetInet", "sceNetInetListen") {

        @Override
        public final void execute(Processor processor) {
            sceNetInetListen(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceNetInetModule.sceNetInetListen(processor);";
        }
    };

    public final HLEModuleFunction sceNetInetPollFunction = new HLEModuleFunction("sceNetInet", "sceNetInetPoll") {

        @Override
        public final void execute(Processor processor) {
            sceNetInetPoll(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceNetInetModule.sceNetInetPoll(processor);";
        }
    };

    public final HLEModuleFunction sceNetInetRecvFunction = new HLEModuleFunction("sceNetInet", "sceNetInetRecv") {

        @Override
        public final void execute(Processor processor) {
            sceNetInetRecv(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceNetInetModule.sceNetInetRecv(processor);";
        }
    };

    public final HLEModuleFunction sceNetInetRecvfromFunction = new HLEModuleFunction("sceNetInet", "sceNetInetRecvfrom") {

        @Override
        public final void execute(Processor processor) {
            sceNetInetRecvfrom(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceNetInetModule.sceNetInetRecvfrom(processor);";
        }
    };

    public final HLEModuleFunction sceNetInetRecvmsgFunction = new HLEModuleFunction("sceNetInet", "sceNetInetRecvmsg") {

        @Override
        public final void execute(Processor processor) {
            sceNetInetRecvmsg(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceNetInetModule.sceNetInetRecvmsg(processor);";
        }
    };

    public final HLEModuleFunction sceNetInetSelectFunction = new HLEModuleFunction("sceNetInet", "sceNetInetSelect") {

        @Override
        public final void execute(Processor processor) {
            sceNetInetSelect(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceNetInetModule.sceNetInetSelect(processor);";
        }
    };

    public final HLEModuleFunction sceNetInetSendFunction = new HLEModuleFunction("sceNetInet", "sceNetInetSend") {

        @Override
        public final void execute(Processor processor) {
            sceNetInetSend(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceNetInetModule.sceNetInetSend(processor);";
        }
    };

    public final HLEModuleFunction sceNetInetSendtoFunction = new HLEModuleFunction("sceNetInet", "sceNetInetSendto") {

        @Override
        public final void execute(Processor processor) {
            sceNetInetSendto(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceNetInetModule.sceNetInetSendto(processor);";
        }
    };

    public final HLEModuleFunction sceNetInetSendmsgFunction = new HLEModuleFunction("sceNetInet", "sceNetInetSendmsg") {

        @Override
        public final void execute(Processor processor) {
            sceNetInetSendmsg(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceNetInetModule.sceNetInetSendmsg(processor);";
        }
    };

    public final HLEModuleFunction sceNetInetSetsockoptFunction = new HLEModuleFunction("sceNetInet", "sceNetInetSetsockopt") {

        @Override
        public final void execute(Processor processor) {
            sceNetInetSetsockopt(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceNetInetModule.sceNetInetSetsockopt(processor);";
        }
    };

    public final HLEModuleFunction sceNetInetShutdownFunction = new HLEModuleFunction("sceNetInet", "sceNetInetShutdown") {

        @Override
        public final void execute(Processor processor) {
            sceNetInetShutdown(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceNetInetModule.sceNetInetShutdown(processor);";
        }
    };

    public final HLEModuleFunction sceNetInetSocketFunction = new HLEModuleFunction("sceNetInet", "sceNetInetSocket") {

        @Override
        public final void execute(Processor processor) {
            sceNetInetSocket(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceNetInetModule.sceNetInetSocket(processor);";
        }
    };

    public final HLEModuleFunction sceNetInetSocketAbortFunction = new HLEModuleFunction("sceNetInet", "sceNetInetSocketAbort") {

        @Override
        public final void execute(Processor processor) {
            sceNetInetSocketAbort(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceNetInetModule.sceNetInetSocketAbort(processor);";
        }
    };

    public final HLEModuleFunction sceNetInetGetErrnoFunction = new HLEModuleFunction("sceNetInet", "sceNetInetGetErrno") {

        @Override
        public final void execute(Processor processor) {
            sceNetInetGetErrno(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceNetInetModule.sceNetInetGetErrno(processor);";
        }
    };

    public final HLEModuleFunction sceNetInetGetTcpcbstatFunction = new HLEModuleFunction("sceNetInet", "sceNetInetGetTcpcbstat") {

        @Override
        public final void execute(Processor processor) {
            sceNetInetGetTcpcbstat(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceNetInetModule.sceNetInetGetTcpcbstat(processor);";
        }
    };

    public final HLEModuleFunction sceNetInetGetUdpcbstatFunction = new HLEModuleFunction("sceNetInet", "sceNetInetGetUdpcbstat") {

        @Override
        public final void execute(Processor processor) {
            sceNetInetGetUdpcbstat(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceNetInetModule.sceNetInetGetUdpcbstat(processor);";
        }
    };

    public final HLEModuleFunction sceNetInetInetAddrFunction = new HLEModuleFunction("sceNetInet", "sceNetInetInetAddr") {

        @Override
        public final void execute(Processor processor) {
            sceNetInetInetAddr(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceNetInetModule.sceNetInetInetAddr(processor);";
        }
    };

    public final HLEModuleFunction sceNetInetInetAtonFunction = new HLEModuleFunction("sceNetInet", "sceNetInetInetAton") {

        @Override
        public final void execute(Processor processor) {
            sceNetInetInetAton(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceNetInetModule.sceNetInetInetAton(processor);";
        }
    };

    public final HLEModuleFunction sceNetInetInetNtopFunction = new HLEModuleFunction("sceNetInet", "sceNetInetInetNtop") {

        @Override
        public final void execute(Processor processor) {
            sceNetInetInetNtop(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceNetInetModule.sceNetInetInetNtop(processor);";
        }
    };

    public final HLEModuleFunction sceNetInetInetPtonFunction = new HLEModuleFunction("sceNetInet", "sceNetInetInetPton") {

        @Override
        public final void execute(Processor processor) {
            sceNetInetInetPton(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceNetInetModule.sceNetInetInetPton(processor);";
        }
    };
}

;
