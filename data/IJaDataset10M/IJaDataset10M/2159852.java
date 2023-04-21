package net.sf.evemsp.ui.forms;

import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.StringItem;
import net.sf.evemsp.EveMobileSkillPlanner;
import net.sf.evemsp.model.CharRecord;

/**
 * Show character details and edit implants.
 * 
 * @author Jaabaa
 */
public class UntrainedForm extends Form implements CommandListener {

    private EveMobileSkillPlanner esme;

    private Vector untrainedGroups;

    /**
	 * Constructor
	 */
    public UntrainedForm(EveMobileSkillPlanner esme, CharRecord record, Vector untrainedGroups) {
        super(record.getName());
        this.esme = esme;
        this.untrainedGroups = untrainedGroups;
        addCommand(esme.goMain);
        setCommandListener(this);
        for (int i = 0; i < this.untrainedGroups.size(); i++) {
            UntrainedGroup ug = (UntrainedGroup) untrainedGroups.elementAt(i);
            StringItem item = new StringItem(null, ug.sg.getName() + " (" + ug.skillDefinitions.size() + ")");
            item.setLayout(Item.LAYOUT_2 | Item.LAYOUT_LEFT | Item.LAYOUT_EXPAND | Item.LAYOUT_NEWLINE_AFTER);
            append(item);
        }
    }

    /**
	 * @see javax.microedition.lcdui.CommandListener#commandAction(Command, Displayable)
	 */
    public void commandAction(Command cmd, Displayable disp) {
        esme.commandAction(esme.goMain, disp);
    }
}
