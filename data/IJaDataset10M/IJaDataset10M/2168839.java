package za.co.me23.methods;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.TextField;
import za.co.me23.canvas.Me23Canvas;
import za.co.me23.canvas.Methode;
import za.co.me23.chat.SettingStore;
import za.co.me23.chat.Settings;
import za.co.me23.chat.Utility;
import za.co.me23.midlet.ME23;

/**
 *
 * @author Jean-Pierre
 */
public class UpdateProfileSettingsMethode extends Methode {

    public UpdateProfileSettingsMethode() {
        super("Updating");
    }

    public void methode() {
        if (Utility.verifyPinNumber(((TextField) ME23.getME23().updateProfileSettingsForm.get(1)).getString())) {
            SettingStore.getSettingStore().pin = ((TextField) ME23.getME23().updateProfileSettingsForm.get(1)).getString();
            if (Utility.verifyYourName(((TextField) ME23.getME23().updateProfileSettingsForm.get(2)).getString())) SettingStore.getSettingStore().statusMessage = Utility.replace(((TextField) ME23.getME23().updateProfileSettingsForm.get(2)).getString(), '|', " "); else SettingStore.getSettingStore().statusMessage = "...";
            if (Utility.verifyYourName(((TextField) ME23.getME23().updateProfileSettingsForm.get(3)).getString())) {
                SettingStore.getSettingStore().yourName = Utility.replace(((TextField) ME23.getME23().updateProfileSettingsForm.get(3)).getString(), '|', " ");
                ChoiceGroup hideMobileNumberCG = (ChoiceGroup) ME23.getME23().updateProfileSettingsForm.get(4);
                ChoiceGroup genderCG = (ChoiceGroup) ME23.getME23().updateProfileSettingsForm.get(5);
                ChoiceGroup yearOfBirthCG = (ChoiceGroup) ME23.getME23().updateProfileSettingsForm.get(6);
                SettingStore.getSettingStore().gender = genderCG.getSelectedIndex();
                SettingStore.getSettingStore().hideMobileNumber = hideMobileNumberCG.getSelectedIndex();
                SettingStore.getSettingStore().yearOfBirth = yearOfBirthCG.getSelectedIndex();
                SettingStore.getSettingStore().setSettings(false, true);
                setNextDisplay(null, Me23Canvas.getMe23Canvas());
                ME23.getME23().updateProfileSettingsForm = null;
            } else setNextDisplay(new Alert("Error", "Please fill in the your name field.", null, AlertType.ERROR), ME23.getME23().getUpdateProfileSettingsForm());
        } else setNextDisplay(new Alert("Error", "Pin number should be 6 or more numbers numbers.", null, AlertType.ERROR), ME23.getME23().getUpdateProfileSettingsForm());
    }
}
