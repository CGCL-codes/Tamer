package mpc.bluetooth;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDletStateChangeException;

public class PeddyPaperMenu {

    private PeddyPaperPlayer midlet;

    private Login log;

    private List mainMenu;

    private Command exitCmd;

    private Form editProfileForm;

    private Command exitEditProfileCmd;

    private Command editProfile;

    private TextField firstname;

    private TextField lastname;

    private DateField birthday;

    private TextField eMail;

    private TextField username;

    private TextField password;

    private ImageItem photo;

    private List currentScores;

    private Command exitScoresCmd;

    private List availableActivities;

    private Command exitActivitiesCmd;

    public PeddyPaperMenu(PeddyPaperPlayer midlet, Login log) {
        this.midlet = midlet;
        this.log = log;
        init();
    }

    private void init() {
        mainMenu = new List("Peddy-papper Menu", List.IMPLICIT);
        mainMenu.append("Edit Profile", null);
        mainMenu.append("View Scores", null);
        mainMenu.append("View Activities", null);
        exitCmd = new Command("Exit", Command.EXIT, 2);
        mainMenu.addCommand(this.exitCmd);
        mainMenu.setCommandListener(new CommandListener() {

            public void commandAction(Command c, Displayable d) {
                if (c == List.SELECT_COMMAND) {
                    int pos = mainMenu.getSelectedIndex();
                    switch(pos) {
                        case 0:
                            showEditProfile();
                            break;
                        case 1:
                            showViewScores();
                            break;
                        case 2:
                            showViewActivities();
                            break;
                    }
                } else if (c == exitCmd) {
                    try {
                        destroyApp(false);
                    } catch (MIDletStateChangeException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        });
        editProfileForm = new Form("Edit Profile");
        exitEditProfileCmd = new Command("Back", Command.EXIT, 2);
        editProfile = new Command("Edit", Command.OK, 2);
        editProfileForm.addCommand(exitEditProfileCmd);
        editProfileForm.addCommand(editProfile);
        firstname = new TextField("First Name", "", 10, TextField.INITIAL_CAPS_WORD);
        lastname = new TextField("Last Name", "", 10, TextField.INITIAL_CAPS_WORD);
        birthday = new DateField("Birthday", DateField.DATE);
        eMail = new TextField("Email:", "", 10, TextField.EMAILADDR);
        username = new TextField("Username:", "", 10, TextField.ANY);
        password = new TextField("Password:", "", 10, TextField.PASSWORD);
        photo = new ImageItem("Picture", null, Item.LAYOUT_DEFAULT, null);
        editProfileForm.append(firstname);
        editProfileForm.append(lastname);
        editProfileForm.append(birthday);
        editProfileForm.append(eMail);
        editProfileForm.append(username);
        editProfileForm.append(password);
        editProfileForm.append(photo);
        editProfileForm.setCommandListener(new CommandListener() {

            public void commandAction(Command c, Displayable d) {
                if (c == exitEditProfileCmd) {
                    showMainMenu();
                } else if (c == editProfile) {
                    doEditProfile();
                }
            }
        });
        currentScores = new List("Current Scores", List.IMPLICIT);
        exitScoresCmd = new Command("Back", Command.EXIT, 2);
        currentScores.addCommand(exitScoresCmd);
        currentScores.setCommandListener(new CommandListener() {

            public void commandAction(Command c, Displayable d) {
                if (c == exitScoresCmd) {
                    showMainMenu();
                }
            }
        });
        availableActivities = new List("Available Activities", List.IMPLICIT);
        exitActivitiesCmd = new Command("Back", Command.EXIT, 2);
        availableActivities.addCommand(exitActivitiesCmd);
        availableActivities.setCommandListener(new CommandListener() {

            public void commandAction(Command c, Displayable d) {
                if (c == exitActivitiesCmd) {
                    showMainMenu();
                }
            }
        });
    }

    protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
        midlet.display().setCurrent(log.loginWindow());
    }

    protected void pauseApp() {
    }

    protected void startApp() throws MIDletStateChangeException {
        showMainMenu();
    }

    /**
	 * Menu Related Methods
	 */
    private void showMainMenu() {
        midlet.display().setCurrent(this.mainMenu);
    }

    private void showEditProfile() {
        midlet.display().setCurrent(this.editProfileForm);
    }

    private void doEditProfile() {
        Alert a = new Alert("Edit", "Profile Edited!", null, null);
        a.setTimeout(2000);
        midlet.display().setCurrent(a, mainMenu);
    }

    private void showViewScores() {
        midlet.display().setCurrent(currentScores);
    }

    private void showViewActivities() {
        midlet.display().setCurrent(availableActivities);
    }
}
