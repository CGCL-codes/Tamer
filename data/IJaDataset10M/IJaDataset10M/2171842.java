package ui;

import java.io.IOException;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.KeyListener;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.decor.*;

public class UserInterface extends UiApplication {

    public static void main(String[] args) throws IOException {
        UserInterface theApp = new UserInterface();
        theApp.enterEventDispatcher();
    }

    public UserInterface() throws IOException {
        pushScreen(new UIScreen());
    }

    timerScreen displayTimer;

    final class UIScreen extends MainScreen {

        BitmapField picture;

        VerticalFieldManager topField;

        VerticalFieldManager bottomField;

        int levelSelect = 0;

        int gametype = 0;

        int nWarnTime = 10;

        ObjectChoiceField levelSelector;

        ObjectChoiceField gametypeSelector;

        NumericChoiceField warnSelector;

        ButtonField startButton;

        Bitmap mainBG;

        private VerticalFieldManager _manager;

        FieldChangeListener buttonListener = new FieldChangeListener() {

            public void fieldChanged(Field field, int context) {
                levelSelect = levelSelector.getSelectedIndex();
                gametype = gametypeSelector.getSelectedIndex();
                nWarnTime = warnSelector.getSelectedValue();
                displayTimer = new timerScreen(levelSelect, gametype, nWarnTime);
                pushScreen(displayTimer);
            }
        };

        public UIScreen() throws IOException {
            super(Field.USE_ALL_HEIGHT | Field.USE_ALL_WIDTH | Field.FIELD_BOTTOM);
            _manager = (VerticalFieldManager) getMainManager();
            Background bg = BackgroundFactory.createBitmapBackground(Bitmap.getBitmapResource("bg.png"));
            _manager.setBackground(bg);
            runStuff();
        }

        protected void onExposed() {
            levelSelector.setFocus();
            startButton.setFocus();
        }

        public boolean onSavePrompt() {
            return true;
        }

        public void displayPopup() {
            VerticalFieldManager aboutInfo = new VerticalFieldManager();
            aboutInfo.add(new LabelField("Created by Piglet =D", LabelField.FIELD_HCENTER));
            aboutInfo.add(new SeparatorField());
            aboutInfo.add(new LabelField("dailejl@auburn.edu", LabelField.FIELD_HCENTER));
            PopupScreen aboutScreen = new PopupScreen(aboutInfo);
            pushScreen(aboutScreen);
        }

        public void runStuff() throws IOException {
            LabelField title = new LabelField("Halo:CE Timer", Field.FIELD_HCENTER);
            setTitle(title);
            bottomField = new VerticalFieldManager(Field.FIELD_BOTTOM);
            startButton = new ButtonField("Start", ButtonField.CONSUME_CLICK | Field.FIELD_BOTTOM | Field.FIELD_HCENTER);
            startButton.setChangeListener(buttonListener);
            String levels[] = { "Chill Out", "Prisoner", "Damnation", "Rat Race", "Hang 'Em High", "Battle Creek" };
            String gametypes[] = { "Slayer", "CTF", "King", "Skull" };
            levelSelector = new ObjectChoiceField("Select a level: ", levels, 0, Field.FIELD_BOTTOM);
            gametypeSelector = new ObjectChoiceField("Select a gametype: ", gametypes, 0, Field.FIELD_BOTTOM);
            warnSelector = new NumericChoiceField("Warning time:", 0, 30, 1, nWarnTime);
            bottomField.add(levelSelector);
            bottomField.add(gametypeSelector);
            bottomField.add(warnSelector);
            bottomField.add(startButton);
            setStatus(bottomField);
        }
    }
}
