package jpcsp.HLE.modules150;

import jpcsp.HLE.Modules;
import jpcsp.HLE.modules.HLEModule;
import jpcsp.HLE.modules.HLEModuleFunction;
import jpcsp.HLE.modules.HLEModuleManager;
import jpcsp.Memory;
import jpcsp.Processor;
import jpcsp.Allegrex.CpuState;

public class sceMeBooter_driver implements HLEModule {

    @Override
    public String getName() {
        return "sceMeBooter_driver";
    }

    @Override
    public void installModule(HLEModuleManager mm, int version) {
        if (version >= 150) {
            mm.addFunction(me_initFunction, 0x3BCB751E);
        }
    }

    @Override
    public void uninstallModule(HLEModuleManager mm, int version) {
        if (version >= 150) {
            mm.removeFunction(me_initFunction);
        }
    }

    public void me_init(Processor processor) {
        CpuState cpu = processor.cpu;
        Modules.log.debug("Unimplemented NID function me_init [0x3BCB751E]");
        cpu.gpr[2] = 0xDEADC0DE;
    }

    public final HLEModuleFunction me_initFunction = new HLEModuleFunction("sceMeBooter_driver", "me_init") {

        @Override
        public final void execute(Processor processor) {
            me_init(processor);
        }

        @Override
        public final String compiledString() {
            return "jpcsp.HLE.Modules.sceMeBooter_driverModule.me_init(processor);";
        }
    };
}

;
