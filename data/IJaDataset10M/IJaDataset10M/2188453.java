package uk.org.dataforce.g15.plugins.clock;

import java.awt.Point;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import uk.org.dataforce.g15.plugins.Plugin;
import uk.org.dataforce.g15.G15Control;
import uk.org.dataforce.g15.G15Position;
import uk.org.dataforce.g15.FontSize;
import uk.org.dataforce.g15.G15Wrapper;
import uk.org.dataforce.g15.fonts.G15Font;
import uk.org.dataforce.g15.fonts.Font_BigNumber;

public class Clock extends Plugin {

    /** The drawing screen. */
    G15Wrapper myScreen;

    /** The controller. */
    G15Control myController;

    /** Progress Bar position */
    int progressPos = 0;

    /** Clock Font */
    final G15Font font = new Font_BigNumber(17, 42);

    /**
	 * Drawing Mode.
	 *
	 * 0 = Small
	 * 1 = Medium
	 * 2 = Large
	 * 3 = Full screen
	 */
    int drawingMode = 3;

    /** Draw seconds or not. */
    boolean drawSeconds = true;

    /** clearScreen before drawing or not. */
    boolean clearScreen = false;

    /**
	 * Called when the plugin is loaded.
	 *
	 * @param control The G15Control that owns this plugin
	 * @param wrapper The screen that this plugin owns
	 */
    public void onLoad(G15Control control, G15Wrapper wrapper) {
        myController = control;
        myScreen = wrapper;
    }

    /**
	 * Called if the screen changes.
	 *
	 * @param wrapper The screen that this plugin now owns
	 */
    public void changeScreen(G15Wrapper wrapper) {
        myScreen = wrapper;
    }

    /**
	 * Called when the plugin is about to be unloaded.
	 */
    public void onUnload() {
    }

    /**
	 * Called every 1/2 second for drawing related tasks when this screen is active.
	 */
    public void onRedraw() {
        DateFormat dateFormat;
        if (clearScreen) {
            myScreen.clearScreen(false);
            clearScreen = false;
        }
        if (drawSeconds) {
            dateFormat = new SimpleDateFormat("HH:mm:ss");
        } else {
            dateFormat = new SimpleDateFormat("HH:mm");
        }
        if (drawingMode < 3) {
            FontSize fontSize = FontSize.LARGE;
            switch(drawingMode) {
                case 0:
                    fontSize = FontSize.SMALL;
                    break;
                case 1:
                    fontSize = FontSize.MEDIUM;
                    break;
            }
            myScreen.drawText(fontSize, new Point(0, (myScreen.getHeight() / 2) - 5), G15Position.CENTER, dateFormat.format(new Date()));
        } else {
            final String text = dateFormat.format(new Date());
            int xPos = 0;
            if (!drawSeconds) {
                xPos = font.getSize().width + 14;
            }
            for (int i = 0; i < text.length(); ++i) {
                if (i > 0) {
                    xPos = xPos + 3;
                }
                int number;
                try {
                    number = Integer.parseInt("" + text.charAt(i));
                    myScreen.drawPixels(new Point(xPos, 1), font.getSize().width, font.getSize().height, font.getPixels(number + 48));
                } catch (NumberFormatException e) {
                    myScreen.drawPixels(new Point(xPos, 1), font.getSize().width, font.getSize().height, font.getPixels(':'));
                }
                xPos = xPos + font.getSize().width;
            }
        }
        myScreen.silentDraw();
    }

    /**
	 * Called when this plugin becomes active.
	 * This needs to FULLY redraw the screen.
	 */
    public void onActivate() {
        onRedraw();
    }

    /**
	 * Called when this plugin becomes active.
	 */
    public void onDeactivate() {
    }

    /**
	 * Called when LCD Button 1 is pressed.
	 */
    public void onLCD1() {
        ++drawingMode;
        if (drawingMode > 3) {
            drawingMode = 0;
        }
        clearScreen = true;
    }

    /**
	 * Called when LCD Button 2 is pressed.
	 */
    public void onLCD2() {
        --drawingMode;
        if (drawingMode < 0) {
            drawingMode = 3;
        }
        clearScreen = true;
    }

    /**
	 * Called when LCD Button 3 is pressed.
	 */
    public void onLCD3() {
        drawSeconds = !drawSeconds;
        clearScreen = true;
    }

    /**
	 * Called when LCD Button 4 is pressed.
	 */
    public void onLCD4() {
    }

    /**
	 * Get the plugin version.
	 *
	 * @return Plugin Version
	 */
    public int getVersion() {
        return 1;
    }

    /**
	 * Get information about the plugin.
	 * result[0] == Author
	 * result[1] == Description
	 *
	 * @return Information about the plugin
	 */
    public String[] getInformation() {
        return new String[] { "Dataforce", "Example Clock Plugin" };
    }
}
