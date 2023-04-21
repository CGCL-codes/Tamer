package br.com.mobilit.test.exemple.screen;

import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Form;
import br.com.mobilit.components.application.IMoSOAFramework;
import br.com.mobilit.control.ApplicationMIDlet;
import br.com.mobilit.test.exemple.MobServicesMIDlet;

public class SettingsScreen extends Form {

    private static final int LAYOUT_CENTER = 0;

    private final IMoSOAFramework moSOAFramework = ApplicationMIDlet.getFrameworkMgt();

    private Command okCommand;

    private Command backCommand;

    private ChoiceGroup choice;

    private int currentLanguage;

    public SettingsScreen() {
        super("");
        this.setTitle(moSOAFramework.getString("settings.title"));
        this.choice = new ChoiceGroup(moSOAFramework.getString("settings.option.language"), ChoiceGroup.EXCLUSIVE);
        this.okCommand = new Command(moSOAFramework.getString("ok.command.label"), Command.SCREEN, 0);
        this.backCommand = new Command(moSOAFramework.getString("back.command.label"), Command.EXIT, 0);
        this.choice.setLayout(LAYOUT_CENTER);
        this.choice.setLabel(moSOAFramework.getString("settings.option.language"));
        this.choice.append(moSOAFramework.getString("settings.en.language"), null);
        this.choice.append(moSOAFramework.getString("settings.pt.language"), null);
        this.currentLanguage = 0;
        this.append(this.choice);
        this.addCommand(this.backCommand);
        this.addCommand(this.okCommand);
    }

    public void reLoadScreen() {
        this.loadCommands();
        this.setTitle(moSOAFramework.getString("settings.title"));
        this.choice.deleteAll();
        this.choice.setLabel(moSOAFramework.getString("settings.option.language"));
        this.choice.append(moSOAFramework.getString("settings.en.language"), null);
        this.choice.append(moSOAFramework.getString("settings.pt.language"), null);
        this.choice.setSelectedIndex(this.currentLanguage, true);
    }

    private void loadCommands() {
        if (this.okCommand != null) {
            this.removeCommand(this.okCommand);
            this.okCommand = new Command(moSOAFramework.getString("ok.command.label"), Command.SCREEN, 0);
        }
        if (this.backCommand != null) {
            this.removeCommand(this.backCommand);
            this.backCommand = new Command(moSOAFramework.getString("back.command.label"), Command.EXIT, 0);
        }
        this.addCommand(this.backCommand);
        this.addCommand(this.okCommand);
    }

    public Command getOkCommand() {
        return okCommand;
    }

    public void setOkCommand(Command okCommand) {
        this.okCommand = okCommand;
    }

    public Command getBackCommand() {
        return backCommand;
    }

    public String getLanguage() {
        String ret = null;
        int slectedIndex = this.choice.getSelectedIndex();
        switch(slectedIndex) {
            case 0:
                ret = MobServicesMIDlet.EN_LANGUAGE;
                this.currentLanguage = 0;
                break;
            case 1:
                ret = MobServicesMIDlet.PT_LANGUAGE;
                this.currentLanguage = 1;
                break;
            default:
                ret = MobServicesMIDlet.PT_LANGUAGE;
                break;
        }
        return ret;
    }

    public void setCurrentLanguage(String language) {
        if (language.equalsIgnoreCase(MobServicesMIDlet.EN_LANGUAGE)) {
            this.choice.setSelectedIndex(0, true);
            this.currentLanguage = 0;
        } else if (language.equalsIgnoreCase(MobServicesMIDlet.PT_LANGUAGE)) {
            this.choice.setSelectedIndex(1, true);
        }
    }
}
