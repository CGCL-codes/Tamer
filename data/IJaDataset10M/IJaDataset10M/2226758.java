package mylittlestudent.views;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import mylittlestudent.controller.interfaces.IViewDataFacade;

/**
 *
 * @author Susanne
 */
public class ActionsHelpView extends List implements CommandListener {

    public final Command backCommand = new Command("Zur�ck", Command.BACK, 2);

    private IViewDataFacade viewData;

    /** Creates a new instance of ActionsHelpView */
    public ActionsHelpView() {
        super("Hilfe", Choice.IMPLICIT);
        this.viewData = ViewDataFactory.getViewData();
        this.addCommand(backCommand);
        this.setCommandListener(this);
        this.append("Schlafen: steigert Zufriedenheit und Gesundheit, senkt Wissen", null);
        this.append("Nebenjob: steigert Zufriedenheit und Punkte, senkt Gesundheit", null);
        this.append("Lernen: senkt Zufriedenheit, steigert Wissen", null);
        this.append("Party besuchen: senkt Gesundheit, Wissen und Punkte, steigert Zufriedenheit", null);
        this.append("Sport treiben: steigert Zufriedenheit und Gesundheit", null);
        this.append("Vorlesung besuchen: senkt Zufriedenheit, steigert Wissen", null);
    }

    public void commandAction(Command command, Displayable displayable) {
        if (command == backCommand) {
            viewData.setView(new ActionsView());
        }
    }
}
