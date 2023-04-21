package wood.view.viewport;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lawu.math.Vector;
import org.apache.log4j.Logger;
import wood.log.MessageQueue;
import wood.log.WoodLevel;
import wood.view.draw.Canvas;
import wood.view.draw.DrawableIDManager;

public class ConsoleViewport<C extends Canvas> extends Viewport<C> {

    private final List<WoodLevel> levels = new ArrayList<WoodLevel>();

    private final MessageQueue gameAppender;

    private boolean enabled = true;

    private boolean focused = false;

    private int fontSize = 15;

    private int leftBorder = 10;

    private int rightBorder = 10;

    private int bottomBorder = 10;

    private int topBorder = 8;

    private int spaceBetween = 1;

    private Font font = new Font("New Courier", Font.BOLD, fontSize);

    public ConsoleViewport() {
        this.setDrawableID(DrawableIDManager.getInstance().getUniqueID());
        levels.add(WoodLevel.GAME);
        levels.add(WoodLevel.CHAT);
        levels.add(WoodLevel.DEBUG);
        gameAppender = new MessageQueue(levels, 20);
        Logger logger = Logger.getLogger("wood");
        logger.addAppender(gameAppender);
    }

    @Override
    public void doDraw(C canvas) {
        if (!enabled) return;
        float alpha = (float) (focused ? 0.7 : 0.3);
        canvas.setBrushColor(new Color(0, 0, 0, alpha));
        canvas.drawPolygon(getBounds());
        canvas.setBrushColor(new Color(11, 121, 176, (int) (alpha * 255)));
        canvas.setFont(font);
        double drawableHeight = (getBoundsSize().getHeight() - topBorder - bottomBorder + spaceBetween);
        double drawableWidth = getBoundsSize().getWidth() - leftBorder - rightBorder;
        int numMessages = Math.max(0, (int) (drawableHeight / (fontSize + spaceBetween)));
        Iterator<String> messages = gameAppender.getMessages(numMessages);
        Vector loc = getBounds().getLowerLeft().add(new Vector(leftBorder, bottomBorder));
        int index = 0;
        while (messages.hasNext() && index < numMessages) {
            String wholeMessage = messages.next();
            String currentMessage = "> " + wholeMessage;
            ArrayList<String> drawMessages = new ArrayList<String>();
            int cutoff = currentMessage.length();
            while (canvas.getSizeOfText(currentMessage).getWidth() > drawableWidth) {
                while (canvas.getSizeOfText(currentMessage.substring(0, cutoff)).getWidth() > drawableWidth && cutoff > 1) --cutoff;
                int spaceLoc = currentMessage.lastIndexOf(' ', cutoff - 1);
                if (spaceLoc > 0) cutoff = spaceLoc;
                drawMessages.add(0, currentMessage.substring(0, cutoff));
                if (spaceLoc > 0) cutoff += 1;
                currentMessage = "  " + currentMessage.substring(cutoff - 1);
            }
            drawMessages.add(0, currentMessage);
            for (String message : drawMessages) if (index < numMessages) {
                canvas.drawText(message, loc.getX(), loc.getY());
                loc = loc.add(new Vector(0, fontSize + spaceBetween));
                ++index;
            }
        }
        canvas.clearBrushColor();
    }

    @Override
    public void init(C canvas) {
    }

    public void enable() {
        this.enabled = true;
    }

    @Override
    public void disable() {
        this.enabled = false;
    }

    public void setFocus(boolean focused) {
        this.focused = focused;
    }

    public void toggleFocused() {
        this.focused = !this.focused;
    }
}
