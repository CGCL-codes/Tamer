package model.storage;

public class SaveSettingsException extends Exception {

    public SaveSettingsException(String file) {
        super(Storage.MSG_SAVE_SETTINGS_ERROR + " [" + file + "]");
    }
}
