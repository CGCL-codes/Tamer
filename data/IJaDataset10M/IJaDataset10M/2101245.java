package simpull2core.demo.general;

import pulpcore.Build;
import pulpcore.CoreSystem;
import pulpcore.image.CoreFont;
import pulpcore.platform.ConsoleScene;
import pulpcore.scene.Scene2D;
import pulpcore.sprite.Button;
import pulpcore.sprite.FilledSprite;
import pulpcore.sprite.Group;
import pulpcore.sprite.Label;
import pulpcore.sprite.Sprite;
import pulpcore.Stage;

public class UncaughtExceptionScene extends Scene2D {

    static boolean uploadedThisSession = false;

    Button retryButton;

    Button consoleButton;

    @Override
    public void load() {
        add(new FilledSprite(0x0000aa));
        if (!uploadedThisSession && "true".equals(CoreSystem.getAppProperty("talkback"))) {
            uploadedThisSession = true;
            CoreSystem.uploadTalkBackFields("/talkback.py");
        }
        CoreFont font = CoreFont.getSystemFont().tint(0xffffff);
        Group message = Label.createMultilineLabel(font, "Oops! An error occurred.", Stage.getWidth() / 2, 150, Stage.getWidth() - 20);
        message.setAnchor(Sprite.CENTER);
        add(message);
        if (Build.DEBUG) {
            consoleButton = Button.createLabeledButton("Show Console", Stage.getWidth() / 2, 300);
            consoleButton.setAnchor(Sprite.CENTER);
            add(consoleButton);
        }
        retryButton = Button.createLabeledButton("Restart", Stage.getWidth() / 2, 350);
        retryButton.setAnchor(Sprite.CENTER);
        add(retryButton);
    }

    @Override
    public void update(int elapsedTime) {
        if (retryButton.isClicked()) {
            Stage.setScene(new LoadingScene());
        }
        if (Build.DEBUG && consoleButton.isClicked()) {
            Stage.pushScene(new ConsoleScene());
        }
    }
}
