package listo.client.dialogs.autocompletion;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import listo.client.Preferences;

@Singleton
public class SnoozeTimeCompleter extends BaseCompleter {

    @Inject
    public SnoozeTimeCompleter(Preferences preferences) {
        int[] snoozeOptions = preferences.getReminderOptions();
        for (int snoozeOption : snoozeOptions) {
            availableOptions.add(String.format("%s minutes", snoozeOption));
        }
    }
}
