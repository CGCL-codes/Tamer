package vivace.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import javax.sound.midi.ShortMessage;
import javax.swing.JPanel;
import vivace.helper.GUIHelper;
import vivace.helper.GridHelper;
import vivace.helper.ProjectHelper;
import vivace.model.Action;
import vivace.model.App;
import vivace.model.NoteEvent;
import vivace.model.UI;

/**
 * Represents a note event in the piano roll
 */
public class EventBar extends JPanel implements Comparable<EventBar>, Observer {

    private static final long serialVersionUID = 5534260502853126750L;

    /**
	 * Collection over the visible event bars and their corresponding note.
	 * Each event bar adds itself to the collection when it appears.
	 */
    public static HashMap<NoteEvent, EventBar> visible = new HashMap<NoteEvent, EventBar>();

    /**
	 * Collection all event bars and their corresponding note.
	 * Each event bar adds itself to the collection on creation.
	 */
    public static HashMap<NoteEvent, EventBar> all = new HashMap<NoteEvent, EventBar>();

    /** Enum that declares the different behaviours when the mouse is dragged **/
    public static enum MouseDragBehaviour {

        IGNORE, DRAG, RESIZE_FROM_LEFT, RESIZE_FROM_RIGHT
    }

    ;

    private static MouseDragBehaviour currentDragBehaviour = MouseDragBehaviour.IGNORE;

    /** Returns the drag behaviour used for all instances of event bar */
    public static MouseDragBehaviour getCurrentDragBehaviour() {
        return currentDragBehaviour;
    }

    /** Sets the drag behaviour used for all instances of event bar */
    public static void setCurrentDragBehaviour(MouseDragBehaviour value) {
        currentDragBehaviour = value;
    }

    private NoteEvent note;

    private boolean selected = false;

    private int width, height, x, y;

    private Color color;

    /**
	 * Returns the note 
	 * @return
	 */
    public NoteEvent getNote() {
        return note;
    }

    /**
	 * Returns the position of the bar
	 * @return
	 */
    public Point getPosition() {
        return new Point(x, y);
    }

    /**
	 * Sets the position of the bar
	 * @param x
	 * @param y
	 */
    public void setPosition(int x, int y) {
        setBounds(x, y, width, height);
    }

    /**
	 * Returns whether the bar is selected or not
	 * @return
	 */
    public boolean getSelected() {
        return selected;
    }

    /**
	 * Sets whether the bar should be selected
	 * @param selected
	 */
    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }

    /**
	 * Sets the width in pixels of the bars
	 * @param width
	 */
    public void setWidth(int width) {
        if (width < 6) {
            width = 6;
        }
        setPreferredSize(new Dimension(width, height));
        this.width = width;
        repaint();
    }

    /**
	 * Creates a new empty event bar
	 */
    public EventBar() {
        setOpaque(false);
        this.note = null;
        this.selected = false;
        this.color = Color.LIGHT_GRAY;
        this.width = GUIHelper.MIN_NOTE_WIDTH;
        this.height = Keyboard.SMALLNOTE_HEIGHT;
    }

    /**
	 * Creates a new event bar connected to the note event 
	 * @param note
	 */
    public EventBar(NoteEvent note) {
        setOpaque(false);
        this.note = note;
        this.selected = App.UI.getNoteSelection().contains(note);
        this.setToolTipText(ProjectHelper.getNoteName(note.getNoteValue(), true));
        updateBounds();
    }

    /** Updates the bounds for the event bar **/
    public void updateBounds() {
        float[] hsb = new float[3];
        Color tmpColor = App.UI.getTrackColor(note.getTrackIndex());
        Color.RGBtoHSB(tmpColor.getRed(), tmpColor.getGreen(), tmpColor.getBlue(), hsb);
        float ratio = (float) note.getVelocity() / 127;
        hsb[1] = (float) (0.50 + ratio * 0.50);
        ;
        hsb[2] = (float) (0.6 + ratio * 0.40);
        this.color = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
        x = GridHelper.tickToXPosition(note.getNoteOn().getTick());
        y = GridHelper.noteToYPosition(((ShortMessage) note.getNoteOn().getMessage()).getData1());
        width = Math.max(GUIHelper.MIN_NOTE_WIDTH, GridHelper.tickToXPosition(note.getNoteOff().getTick()) - x);
        height = Keyboard.SMALLNOTE_HEIGHT;
        selected = App.UI.getNoteSelection().contains(this.note);
        setBounds(x, y, width, height);
    }

    int cachedZoomLevel = App.UI.getZoomLevel();

    @Override
    public void paintComponent(Graphics g) {
        visible.put(note, this);
        if (App.UI.getDiscoMode() && App.Project.getSequencer().isRunning() && note != null) {
            long tick = App.Project.getSequencer().getTickPosition();
            boolean isPlayed = note.getNoteOn().getTick() <= (tick - App.Project.getLatencyInTicks()) && note.getNoteOff().getTick() >= (tick - App.Project.getLatencyInTicks());
            if (isPlayed != selected) setSelected(isPlayed);
        }
        if (selected) {
            g.setColor(Color.BLACK);
            g.drawRect(1, 1, width - 2, height - 2);
            g.fillRect(1, 1, width - 2, height - 2);
            g.setColor(color);
            g.drawRect(0, 0, width - 2, height - 2);
        } else {
            g.setColor(Color.BLACK);
            g.drawRect(1, 1, width - 2, height - 2);
            g.setColor(color);
            g.fillRect(1, 1, width - 2, height - 2);
            g.setColor(Color.BLACK);
            g.drawRect(0, 0, width - 2, height - 2);
        }
    }

    /**
	 * Increases the eventbar's x- and y-position. 
	 * The input parameters can be negative.
	 * @param x
	 * @param y
	 */
    public void moveBy(int x, int y) {
        Rectangle bounds = this.getBounds();
        moveTo(bounds.x + x, bounds.y + y);
    }

    /**
	 * Moves the event bar to the position
	 * @param x
	 * @param y
	 */
    public void moveTo(int x, int y) {
        if (x < 1) {
            x = 1;
        }
        if (y < 1) {
            y = 1;
        }
        setBounds(x, y, width, height);
    }

    /**
	 * Increases the eventbar's width. Which direction
	 * the widht grows to depends on the current drag behaviour. 
	 * The input parameters can be negative.
	 * @param inc
	 */
    public void resizeBy(int inc) {
        Rectangle bounds = this.getBounds();
        resizeTo(bounds.x + inc);
    }

    /**
	 * Resize the event bar to the x-coordinate. 
	 * @param x
	 */
    public void resizeTo(int x) {
        Rectangle bounds = this.getBounds();
        int newWidth = 0, newX = 0;
        if (currentDragBehaviour == MouseDragBehaviour.RESIZE_FROM_LEFT) {
            newWidth = bounds.x + width - x;
            newX = x;
        } else if (currentDragBehaviour == MouseDragBehaviour.RESIZE_FROM_RIGHT) {
            newWidth = x - bounds.x;
            newX = bounds.x;
        }
        if (newWidth < 6) {
            return;
        }
        Rectangle newBounds = new Rectangle(newX, bounds.y, newWidth, height);
        width = newWidth;
        setBounds(newBounds);
    }

    @Override
    public int compareTo(EventBar eb) {
        return getX() - eb.getX();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (App.UI.getPerspective() != UI.Perspective.PIANOROLL) {
            return;
        }
        Action action = (Action) arg;
        switch(action) {
            case ZOOMLEVEL_CHANGED:
                updateBounds();
        }
    }
}
