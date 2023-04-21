package mclib.proto.protocombat;

import java.awt.Dimension;
import java.awt.Point;
import mclib.slick.Core;
import mclib.slick.component.Dialog;
import mclib.slick.component.TextButton;
import mclib.slick.component.interfaces.ClickAction;

public class PreferencesDialog extends Dialog {

    public PreferencesDialog() {
        super(new Dimension(500, 500));
        TextButton resolution = new TextButton("Resolution", new Point(10, 10));
        resolution.onMouseRelease(new ResAction());
        add(resolution);
        TextButton sound = new TextButton("Sound", new Point(10, 50));
        sound.onMouseRelease(new SoundAction());
        add(sound);
        TextButton okButton = new TextButton("OK", new Point(440, 455));
        okButton.onMouseRelease(new OkAction());
        add(okButton);
        TextButton cancelButton = new TextButton("Cancel", new Point(10, 455));
        cancelButton.onMouseRelease(new CancelAction());
        add(cancelButton);
    }

    private static class ResAction implements ClickAction {

        public void performAction() {
            Core.getStaticCore().add(new ResolutionDialog());
        }
    }

    private static class SoundAction implements ClickAction {

        public void performAction() {
            Core.getStaticCore().add(new SoundDialog());
        }
    }

    public class OkAction implements ClickAction {

        public void performAction() {
            setRemove(true);
        }
    }

    public class CancelAction implements ClickAction {

        public void performAction() {
            setRemove(true);
        }
    }
}
