package spellcast.ui;

import io.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import spellcast.gestures.*;

/**
 * This class is a Flyweight Factory for creating the Icons needed
 * for the spellcast Gestures.  
 *
 * @author Barrie Treloar
 */
public class GestureIcon {

    /**
     * A flag to indicate that left handed images are desired.
     */
    public static int LEFT_HANDED = 1;

    /**
     * A flag to indicate that right handed images are desired.
     */
    public static int RIGHT_HANDED = 2;

    private static Icon antiSpellIcon;

    private static Icon clapLefthandIcon;

    private static Icon clapRighthandIcon;

    private static Icon digitLefthandIcon;

    private static Icon digitRighthandIcon;

    private static Icon emptyHandIcon;

    private static Icon fingersLefthandIcon;

    private static Icon fingersRighthandIcon;

    private static Icon hiddenHandIcon;

    private static Icon knifeLefthandIcon;

    private static Icon knifeRighthandIcon;

    private static Icon palmLefthandIcon;

    private static Icon palmRighthandIcon;

    private static Icon snapLefthandIcon;

    private static Icon snapRighthandIcon;

    private static Icon waveLefthandIcon;

    private static Icon waveRighthandIcon;

    private static int GESTURE_WIDTH = 48;

    private static int GESTURE_HEIGHT = 48;

    private static int GESTURE_SCALE_HINTS = Image.SCALE_SMOOTH;

    public static Icon getAntiSpellIcon() {
        if (antiSpellIcon == null) {
            antiSpellIcon = createIconFromResource("images/Anti-Spell.gif");
        }
        return antiSpellIcon;
    }

    public static Icon getClapLefthandIcon() {
        if (clapLefthandIcon == null) {
            clapLefthandIcon = createIconFromResource("images/Clap-Lefthand.gif");
        }
        return clapLefthandIcon;
    }

    public static Icon getClapRighthandIcon() {
        if (clapRighthandIcon == null) {
            clapRighthandIcon = createIconFromResource("images/Clap-Righthand.gif");
        }
        return clapRighthandIcon;
    }

    public static Icon getDigitLefthandIcon() {
        if (digitLefthandIcon == null) {
            digitLefthandIcon = createIconFromResource("images/Digit-Lefthand.gif");
        }
        return digitLefthandIcon;
    }

    public static Icon getDigitRighthandIcon() {
        if (digitRighthandIcon == null) {
            digitRighthandIcon = createIconFromResource("images/Digit-Righthand.gif");
        }
        return digitRighthandIcon;
    }

    public static Icon getEmptyHandIcon() {
        if (emptyHandIcon == null) {
            emptyHandIcon = createIconFromResource("images/Empty-Hand.gif");
        }
        return emptyHandIcon;
    }

    public static Icon getFingersLefthandIcon() {
        if (fingersLefthandIcon == null) {
            fingersLefthandIcon = createIconFromResource("images/Fingers-Lefthand.gif");
        }
        return fingersLefthandIcon;
    }

    public static Icon getFingersRighthandIcon() {
        if (fingersRighthandIcon == null) {
            fingersRighthandIcon = createIconFromResource("images/Fingers-Righthand.gif");
        }
        return fingersRighthandIcon;
    }

    public static Icon getHiddenHandIcon() {
        if (hiddenHandIcon == null) {
            hiddenHandIcon = createIconFromResource("images/Hidden-Hand.gif");
        }
        return hiddenHandIcon;
    }

    public static Icon getKnifeLefthandIcon() {
        if (knifeLefthandIcon == null) {
            knifeLefthandIcon = createIconFromResource("images/Knife-Lefthand.gif");
        }
        return knifeLefthandIcon;
    }

    public static Icon getKnifeRighthandIcon() {
        if (knifeRighthandIcon == null) {
            knifeRighthandIcon = createIconFromResource("images/Knife-Righthand.gif");
        }
        return knifeRighthandIcon;
    }

    public static Icon getPalmLefthandIcon() {
        if (palmLefthandIcon == null) {
            palmLefthandIcon = createIconFromResource("images/Palm-Lefthand.gif");
        }
        return palmLefthandIcon;
    }

    public static Icon getPalmRighthandIcon() {
        if (palmRighthandIcon == null) {
            palmRighthandIcon = createIconFromResource("images/Palm-Righthand.gif");
        }
        return palmRighthandIcon;
    }

    public static Icon getSnapLefthandIcon() {
        if (snapLefthandIcon == null) {
            snapLefthandIcon = createIconFromResource("images/Snap-Lefthand.gif");
        }
        return snapLefthandIcon;
    }

    public static Icon getSnapRighthandIcon() {
        if (snapRighthandIcon == null) {
            snapRighthandIcon = createIconFromResource("images/Snap-Righthand.gif");
        }
        return snapRighthandIcon;
    }

    public static Icon getWaveLefthandIcon() {
        if (waveLefthandIcon == null) {
            waveLefthandIcon = createIconFromResource("images/Wave-Lefthand.gif");
        }
        return waveLefthandIcon;
    }

    public static Icon getWaveRighthandIcon() {
        if (waveRighthandIcon == null) {
            waveRighthandIcon = createIconFromResource("images/Wave-Righthand.gif");
        }
        return waveRighthandIcon;
    }

    private static Icon createIconFromResource(String imageName) {
        byte[] imageData = getImageBytesFromResource(imageName);
        MediaTracker tracker = new MediaTracker(new Label());
        Image original = Toolkit.getDefaultToolkit().createImage(imageData);
        tracker.addImage(original, 0);
        try {
            tracker.waitForAll();
            if (tracker.isErrorAny()) {
                System.out.println("Failed to load Image: " + imageName);
            }
        } catch (InterruptedException ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
        }
        tracker.removeImage(original);
        Image scaled = original.getScaledInstance(GESTURE_WIDTH, GESTURE_HEIGHT, GESTURE_SCALE_HINTS);
        tracker.addImage(scaled, 0);
        try {
            tracker.waitForAll();
            if (tracker.isErrorAny()) {
                System.out.println("Failed to load Image: " + imageName);
            }
        } catch (InterruptedException ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
        }
        tracker.removeImage(scaled);
        Icon result = new ImageIcon(scaled);
        return result;
    }

    private static byte[] getImageBytesFromResource(String imageName) {
        byte[] result = null;
        InputStream imageInputStream = ClassLoader.getSystemResourceAsStream(imageName);
        if (imageInputStream == null) {
            throw new ImageNotAvailableException("Could not find Gesture Image for : " + imageName);
        }
        try {
            result = IOUtilities.readAllBytes(imageInputStream);
        } catch (IOException e) {
            throw new ImageNotAvailableException("Could not find Gesture Image for : " + imageName + ". Reason = " + e.getMessage());
        } finally {
            try {
                imageInputStream.close();
            } catch (IOException ignored) {
            }
        }
        return result;
    }

    public static Icon getIconForGesture(Gesture g, int hand) {
        Icon result = null;
        if (hand != LEFT_HANDED && hand != RIGHT_HANDED) {
            throw new IllegalArgumentException("Unknown hand specified to getIconForGesture(): " + hand);
        }
        if (GestureFactory.instance().getEmptyHand().equals(g)) {
            result = getEmptyHandIcon();
        } else if (GestureFactory.instance().getPalm().equals(g)) {
            if (hand == LEFT_HANDED) {
                result = getPalmLefthandIcon();
            } else {
                result = getPalmRighthandIcon();
            }
        } else if (GestureFactory.instance().getDigit().equals(g)) {
            if (hand == LEFT_HANDED) {
                result = getDigitLefthandIcon();
            } else {
                result = getDigitRighthandIcon();
            }
        } else if (GestureFactory.instance().getFingers().equals(g)) {
            if (hand == LEFT_HANDED) {
                result = getFingersLefthandIcon();
            } else {
                result = getFingersRighthandIcon();
            }
        } else if (GestureFactory.instance().getWave().equals(g)) {
            if (hand == LEFT_HANDED) {
                result = getWaveLefthandIcon();
            } else {
                result = getWaveRighthandIcon();
            }
        } else if (GestureFactory.instance().getClap().equals(g)) {
            if (hand == LEFT_HANDED) {
                result = getClapLefthandIcon();
            } else {
                result = getClapRighthandIcon();
            }
        } else if (GestureFactory.instance().getSnap().equals(g)) {
            if (hand == LEFT_HANDED) {
                result = getSnapLefthandIcon();
            } else {
                result = getSnapRighthandIcon();
            }
        } else if (GestureFactory.instance().getKnife().equals(g)) {
            if (hand == LEFT_HANDED) {
                result = getKnifeLefthandIcon();
            } else {
                result = getKnifeRighthandIcon();
            }
        } else {
            throw new RuntimeException("Unknown Gesture specified in getIconForGesture: " + g);
        }
        return result;
    }
}
