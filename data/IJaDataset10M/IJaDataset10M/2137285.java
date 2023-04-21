package ist.ac.simulador.modules;

import ist.ac.simulador.confguis.GuiPromProperties;
import ist.ac.simulador.nucleo.*;

/**
 *
 * @author  cnr
 */
public class ModulePROMCsRd extends ModuleMemory {

    /** Creates a new instance of ModulePROM */
    public ModulePROMCsRd(String name, String config) {
        super(name.equals("") ? "PROM" : name, config);
    }

    protected void guiInit() {
        GuiPromProperties configGui = new GuiPromProperties();
        setConfigGui(configGui);
        configGui.setElement(this);
    }

    public void setPorts() throws SException {
        addRead();
        addEnable();
        super.setPorts();
    }
}
