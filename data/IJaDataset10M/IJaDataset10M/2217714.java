package interfaces.spawnMenu;

import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.IButtonPressedListener;
import org.fenggui.layout.BorderLayout;
import org.fenggui.layout.BorderLayoutData;
import fileHandling.language.interfaces.OptionsWindowText;
import fileHandling.language.interfaces.SpawnMenuText;
import interfaces.GUISource;
import interfaces.language.LanguageButton;
import interfaces.language.LanguageLabel;
import interfaces.language.LanguageWindow;

public class TeamChangeFailedWindow extends LanguageWindow {

    public TeamChangeFailedWindow() {
        super(true, SpawnMenuText.Unable_to_change_Team);
        content.setLayoutManager(new BorderLayout());
        LanguageLabel label = new LanguageLabel(SpawnMenuText.Team_is_full, GUISource.bigFont);
        label.setLayoutData(BorderLayoutData.CENTER);
        content.addWidget(label);
        LanguageButton button = new LanguageButton(OptionsWindowText.Ok, GUISource.biggestFont);
        button.setLayoutData(BorderLayoutData.SOUTH);
        button.addButtonPressedListener(new IButtonPressedListener() {

            @Override
            public void buttonPressed(ButtonPressedEvent e) {
                close();
            }
        });
        content.addWidget(button);
        layout();
    }
}
