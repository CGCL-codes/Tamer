package de.icehorsetools.iceoffice.plugin.testsection;

import org.ugat.wiser.componentHandler.AUnComponentHandler;
import org.ugat.wiser.componentHandler.UnCheckBoxHandler;
import org.ugat.wiser.componentHandler.UnComboBoxHandler;
import org.ugat.wiser.componentHandler.UnInputDoubleHandler;
import org.ugat.wiser.componentHandler.UnInputIntHandler;
import org.ugat.wiser.componentHandler.UnInputTextHandler;
import org.ugat.wiser.plugin.AUnPluginHandler;

public class TestsectionSubPl extends AUnPluginHandler {

    public void setComponents() {
        this.addComponentHandler(new UnInputDoubleHandler("factor", this.findComponentByName("iFactor"), AUnComponentHandler.NULLABLE));
        this.addComponentHandler(new UnInputDoubleHandler("markhi", this.findComponentByName("iMarkhi"), AUnComponentHandler.NULLABLE));
        this.addComponentHandler(new UnInputDoubleHandler("marklow", this.findComponentByName("iMarklow"), AUnComponentHandler.NULLABLE));
        this.addComponentHandler(new UnInputTextHandler("name", this.findComponentByName("iName"), 0, 50));
        this.addComponentHandler(new UnCheckBoxHandler("out", this.findComponentByName("chbOut")));
        this.addComponentHandler(new UnCheckBoxHandler("recycle", this.findComponentByName("chbRecycle")));
        this.addComponentHandler(new UnInputIntHandler("section", this.findComponentByName("iSection"), AUnComponentHandler.NULLABLE));
        this.addComponentHandler(new UnComboBoxHandler("status", this.findComponentByName("cbStatus"), AUnComponentHandler.REQUIRED));
    }
}
