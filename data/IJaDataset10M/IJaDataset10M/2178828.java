package org.goldang.stringtuner;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Displayable;

/** Encapsulates the behavior of the Instrument/Tuning choice form.
 *
 *  The user's choice (on dismissing the form) sets the value of 
 *  Instrument.current, which is used by other modules.
 *
 *  When the menu is opened, it gets the initial choice of the instrument
 *  to present by a call to InstrumentTuning.
 */
class TuningsDialog extends Form implements CommandListener {

    private final Command backCmd = new Command(Strings.BACK, Command.BACK, 1);

    private final StringTuner myApp;

    private final ChoiceGroup myChoices;

    public TuningsDialog(StringTuner app) {
        super(Strings.APP_NAME);
        myApp = app;
        final String[] names = InstrumentTuning.getNames();
        final String lastInstr = InstrumentTuning.getLastInstr();
        final int ind = findIndexOfName(names, lastInstr);
        myChoices = new ChoiceGroup(Strings.TUNINGS, Choice.EXCLUSIVE, names, null);
        myChoices.setSelectedIndex(ind, true);
        append(myChoices);
        addCommand(backCmd);
        setCommandListener(this);
    }

    private int findIndexOfName(String[] names, String name) {
        for (int i = 0; i < names.length; i++) if (names[i].equals(name)) return i;
        return 0;
    }

    public void commandAction(Command command, Displayable screen) {
        if (command.equals(backCmd)) {
            final int itm = myChoices.getSelectedIndex();
            InstrumentTuning.saveLastInstr(itm);
            myApp.resetMainScreen();
        }
    }
}
