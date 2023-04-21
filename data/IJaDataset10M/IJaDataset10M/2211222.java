package de.icehorsetools.iceoffice.plugin.entry;

import org.ugat.wiser.componentHandler.UnCheckBoxHandler;
import org.ugat.wiser.componentHandler.UnComboBoxHandler;
import org.ugat.wiser.componentHandler.UnInputDoubleHandler;
import org.ugat.wiser.plugin.AUnPluginHandler;

/**
 * @author tkr
 * @version $Id: EntryBasicPl.java 250 2008-05-14 22:47:40Z kruegertom $
 */
public class EntryBasicPl extends AUnPluginHandler {

    public void setComponents() {
        this.addComponentHandler(new UnComboBoxHandler("test", this.findComponentByName("cbTest")));
        this.addComponentHandler(new UnComboBoxHandler("participant", this.findComponentByName("cbParticipant")));
        this.addComponentHandler(new UnCheckBoxHandler("rr", this.findComponentByName("chbRr")));
        this.addComponentHandler(new UnCheckBoxHandler("lateentry", this.findComponentByName("chbLateentry")));
        this.addComponentHandler(new UnInputDoubleHandler("qualification", this.findComponentByName("iQualification")));
    }
}
