package mtt.appoptions;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import mtt.appoptions.events.ShowApplicationOptionsViewEvent;
import mtt.menu.events.ShowMainMenuEvent;
import mtt.rms.dataObjects.ApplicationOptions;
import mtt.rms.recordstores.options.events.GetApplicationOptionsEvent;
import mtt.rms.recordstores.options.events.SetApplicationOptionsEvent;
import mtt.rms.recordstores.options.events.UpdateApplicationOptionsEvent;
import hmvc.j2me.controller.AController;
import hmvc.j2me.events.IEvent;
import hmvc.j2me.events.ShowDisplayAbleEvent;
import hmvc.j2me.events.TerminateEvent;

/**
 * 
 * 
 * @author jens.meiss
 */
public class ApplicationOptionsController extends AController implements CommandListener {

    private ApplicationOptionsModel model;

    private ApplicationOptionsView view;

    public ApplicationOptionsController() {
        setModel(new ApplicationOptionsModel());
        setView(new ApplicationOptionsView());
    }

    public void commandAction(Command c, Displayable arg1) {
        if (c == getView().getCommandStore()) {
            store();
        }
        fireEventUp(new ShowMainMenuEvent());
        fireEvent(new TerminateEvent(), false, true, false);
    }

    private void store() {
        if (getModel().getApplicationOptions() == null) {
            getModel().setApplicationOptions(new ApplicationOptions());
        }
        ApplicationOptions applicationOptions = getModel().getApplicationOptions();
        applicationOptions.setUrlSyncServer(getView().getTextFieldUrlSyncServer().getString());
        fireEventUp(new UpdateApplicationOptionsEvent(applicationOptions));
    }

    private ApplicationOptionsModel getModel() {
        return model;
    }

    private ApplicationOptionsView getView() {
        return view;
    }

    public void init() {
        Form form = new Form("MTT - Optionen");
        form.setCommandListener(this);
        getView().setForm(form);
        TextField textFieldUrlSyncServer = new TextField("URL des SyncServers", "", 100, TextField.URL);
        getView().setTextFieldUrlSyncServer(textFieldUrlSyncServer);
        Command commandBack = new Command("zurück", Command.BACK, 0);
        getView().setCommandBack(commandBack);
        Command commandStore = new Command("speichern", Command.ITEM, 0);
        getView().setCommandStore(commandStore);
        form.append(textFieldUrlSyncServer);
        form.addCommand(commandBack);
        form.addCommand(commandStore);
        register(ShowApplicationOptionsViewEvent.class);
        register(SetApplicationOptionsEvent.class);
        fireEventUp(new GetApplicationOptionsEvent(this));
    }

    public void receiveEvent(IEvent event) {
        if (event instanceof SetApplicationOptionsEvent) {
            SetApplicationOptionsEvent setApplicationOptionsEvent = (SetApplicationOptionsEvent) event;
            getModel().setApplicationOptions(setApplicationOptionsEvent.getApplicationOptions());
            getView().getTextFieldUrlSyncServer().setString(setApplicationOptionsEvent.getApplicationOptions().getUrlSyncServer());
        } else if (event instanceof ShowApplicationOptionsViewEvent) {
            fireEventUp(new ShowDisplayAbleEvent(getView().getDisplayable()));
        }
    }

    private void setModel(ApplicationOptionsModel model) {
        this.model = model;
    }

    private void setView(ApplicationOptionsView view) {
        this.view = view;
    }

    public void terminate() {
    }
}
