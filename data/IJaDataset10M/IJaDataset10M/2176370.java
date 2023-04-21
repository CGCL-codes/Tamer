package fretboard.editor.components.music;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import fretboard.editor.components.datas.ApplicationState;
import fretboard.editor.components.events.EventManager;
import fretboard.editor.music.Clef;
import fretboard.editor.music.Inflection;
import fretboard.editor.music.Note;
import fretboard.editor.music.TemperedNote;
import fretboard.guitar.utils.ResourceUtils;
import fretboard.guitar.utils.Valign;

/**
 * This is the base class for Staff display components.
 * 
 * @author ccordenier
 * 
 */
public abstract class BaseStaff extends Component {

    private static final long serialVersionUID = -4514055912067909531L;

    protected static final int NB_INTER = 16;

    /** The space between lines in pixels */
    protected static final int SPACE = 10;

    /** The space between notes */
    protected static final int NOTE_SPACE = 5;

    protected static final int MARGIN = 20;

    protected static final int BASE_LINE = 110;

    protected static BufferedImage G_CLEF;

    protected static BufferedImage SHARP;

    protected static BufferedImage DBL_SHARP;

    protected static BufferedImage FLAT;

    protected static BufferedImage DBL_FLAT;

    protected static BufferedImage NATURAL;

    protected static BufferedImage RONDE;

    protected static BufferedImage A;

    protected static BufferedImage B;

    protected static BufferedImage C;

    protected static BufferedImage D;

    protected static BufferedImage E;

    protected static BufferedImage F;

    protected static BufferedImage G;

    static {
        G_CLEF = ResourceUtils.readImage("/imgs/clefG.png");
        FLAT = ResourceUtils.readImage("/imgs/flat.png");
        DBL_FLAT = ResourceUtils.readImage("/imgs/double_flat.png");
        SHARP = ResourceUtils.readImage("/imgs/sharp.png");
        DBL_SHARP = ResourceUtils.readImage("/imgs/double_sharp.png");
        NATURAL = ResourceUtils.readImage("/imgs/natural.png");
        RONDE = ResourceUtils.readImage("/imgs/ronde.png");
        A = ResourceUtils.readImage("/imgs/a.png");
        B = ResourceUtils.readImage("/imgs/b.png");
        C = ResourceUtils.readImage("/imgs/c.png");
        D = ResourceUtils.readImage("/imgs/d.png");
        E = ResourceUtils.readImage("/imgs/e.png");
        F = ResourceUtils.readImage("/imgs/f.png");
        G = ResourceUtils.readImage("/imgs/g.png");
    }

    protected ApplicationState as = ApplicationState.getInstance();

    protected EventManager eventManager = EventManager.getInstance();

    /** Stores the current dimension used to display the current selected scale */
    protected Dimension staffDim;

    private Graphics2D graphics;

    public BaseStaff() {
        this.setBackground(Color.WHITE);
        this.setup();
    }

    @Override
    public void paint(Graphics g) {
        this.staffDim = this.getStaffDimension();
        Graphics2D g2 = (Graphics2D) g;
        BufferedImage staff = new BufferedImage(staffDim.width, staffDim.height, BufferedImage.TYPE_INT_ARGB);
        this.graphics = staff.createGraphics();
        this.graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.drawStaffBase();
        this.drawStaff(this.graphics);
        g2.setBackground(Color.WHITE);
        g2.drawImage(staff, null, 0, 0);
        this.graphics.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        this.staffDim = this.getStaffDimension();
        return staffDim;
    }

    /**
	 * This method draw the keys and the staff lines.
	 * 
	 */
    private void drawStaffBase() {
        this.graphics.setColor(Color.WHITE);
        this.graphics.fillRect(0, 0, staffDim.width, staffDim.height);
        this.graphics.drawImage(G_CLEF, MARGIN, MARGIN + 47, null);
        this.graphics.setPaint(Color.BLACK);
        for (int i = 0; i < 5; i++) {
            this.graphics.drawLine(MARGIN, getLineY(i), staffDim.width - MARGIN, getLineY(i));
        }
    }

    /**
	 * Setup the initial parameters used by the current display that doesn't
	 * need to be recalculate.
	 * 
	 */
    protected void setup() {
    }

    /**
	 * This method can be called to calculate the position of an image that will
	 * be displayed on the staff
	 * 
	 * @param imageHeight
	 *            The height of the image that must be drawn.
	 * @param position
	 *            The position relative to base line.
	 * @param align
	 *            The align attribute of the element to draw.
	 * @return
	 */
    protected int getY(int imageHeight, int position, Valign align, int offset) {
        if (Valign.CENTER.equals(align)) {
            return (BASE_LINE - ((position * (SPACE / 2)) + (imageHeight / 2))) + MARGIN + offset;
        } else {
            if (Valign.TOP.equals(align)) {
                return (BASE_LINE - ((position * (SPACE / 2)) + imageHeight)) + MARGIN + offset;
            } else {
                return (BASE_LINE - ((position * (SPACE / 2)))) + MARGIN + offset;
            }
        }
    }

    /**
	 * Calculate the y coordinate of a line that must be drawn.
	 * 
	 * @param pos
	 *            The position of the line.
	 * @return The y coordinate.
	 */
    protected int getLineY(int pos) {
        return BASE_LINE + MARGIN - (pos * SPACE);
    }

    /**
	 * This method can draw missing interlines.
	 * 
	 * @param x
	 *            The starting x where to draw the line.
	 * @param pos
	 *            The position relative to the base line.
	 * @param graphics
	 *            The graphics used to draw the line.
	 */
    protected void drawInterlines(int x, int pos, Graphics2D graphics) {
        int nbLine = pos / 2;
        if (nbLine < 0) {
            for (int j = -1; j >= nbLine; j--) {
                graphics.drawLine(x - (NOTE_SPACE / 2), getLineY(j), x + RONDE.getWidth() + (NOTE_SPACE / 2), getLineY(j));
            }
        }
        if (nbLine >= 5) {
            for (int j = 5; j <= nbLine; j++) {
                graphics.drawLine(x - (NOTE_SPACE / 2), getLineY(j), x + RONDE.getWidth() + (NOTE_SPACE / 2), getLineY(j));
            }
        }
    }

    /**
	 * This method is called to draw a note on the staff.
	 * 
	 * @param graphics
	 *            The graphics used to display the note.
	 * @param x
	 *            The position in pixel used to draw the note.
	 * @param note
	 *            The note to display.
	 * @param previousNote
	 *            The previous note.
	 * @param selectedNote
	 *            the selected note.
	 * @return The size in pixels of the drawn note.
	 */
    protected int drawNote(Graphics2D graphics, int x, Note note, Note previousNote, boolean displayNote) {
        int pos = Clef.G.findPosition(note);
        x += NOTE_SPACE;
        if (note.getInflection().equals(Inflection.SHARP)) {
            drawInterlines(x, pos, graphics);
            if (note.getNbInflections() == 1) {
                int y = getY(SHARP.getHeight(), pos, Valign.CENTER, 0);
                graphics.drawImage(SHARP, x, y, null);
                x += SHARP.getWidth();
            } else {
                if (note.getNbInflections() == 2) {
                    int y = getY(DBL_SHARP.getHeight(), pos, Valign.CENTER, 0);
                    graphics.drawImage(DBL_SHARP, x, y, null);
                    x += DBL_SHARP.getWidth();
                }
            }
        } else {
            if (note.getInflection().equals(Inflection.FLAT)) {
                drawInterlines(x, pos, graphics);
                if (note.getNbInflections() == 1) {
                    int y = getY(FLAT.getHeight(), pos, Valign.TOP, 6);
                    graphics.drawImage(FLAT, x, y, null);
                    x += FLAT.getWidth();
                } else {
                    if (note.getNbInflections() == 2) {
                        int y = getY(DBL_FLAT.getHeight(), pos, Valign.TOP, 6);
                        graphics.drawImage(DBL_FLAT, x, y, null);
                        x += DBL_FLAT.getWidth();
                    }
                }
            } else {
                if (previousNote != null) {
                    if (previousNote.getTemperedNote().equals(note.getTemperedNote()) && !(Inflection.NATURAL.equals(previousNote.getInflection())) && Inflection.NATURAL.equals(note.getInflection())) {
                        drawInterlines(x, pos, graphics);
                        int y = getY(NATURAL.getHeight(), pos, Valign.CENTER, 0);
                        graphics.drawImage(NATURAL, x, y, null);
                        x += NATURAL.getWidth();
                    }
                }
            }
        }
        drawInterlines(x, pos, graphics);
        BufferedImage noteImg = RONDE;
        if (displayNote || as.isSelected(note)) {
            if (TemperedNote.A.equals(note.getTemperedNote())) {
                noteImg = A;
            }
            if (TemperedNote.B.equals(note.getTemperedNote())) {
                noteImg = B;
            }
            if (TemperedNote.C.equals(note.getTemperedNote())) {
                noteImg = C;
            }
            if (TemperedNote.D.equals(note.getTemperedNote())) {
                noteImg = D;
            }
            if (TemperedNote.E.equals(note.getTemperedNote())) {
                noteImg = E;
            }
            if (TemperedNote.F.equals(note.getTemperedNote())) {
                noteImg = F;
            }
            if (TemperedNote.G.equals(note.getTemperedNote())) {
                noteImg = G;
            }
        }
        int noteFigureY = getY(noteImg.getHeight(), pos, Valign.CENTER, 0);
        graphics.drawImage(noteImg, x, noteFigureY, null);
        x += noteImg.getWidth();
        x += NOTE_SPACE;
        return x;
    }

    /**
	 * Used to display a chord on staff without preceding notes. This method can
	 * be used for games.
	 * 
	 * @param graphics
	 * @param x
	 * @param displayNote
	 * @param notes
	 * @return
	 */
    protected int drawNote(Graphics2D graphics, int x, boolean displayNote, Note... notes) {
        int xPos = x + NOTE_SPACE;
        int notePos = xPos;
        Integer highestPos = null;
        Integer lowestPos = null;
        for (Note note : notes) {
            int pos = Clef.G.findPosition(note);
            if (highestPos == null || highestPos < pos) {
                highestPos = pos;
            }
            if (lowestPos == null || lowestPos > pos) {
                lowestPos = pos;
            }
            if (note.getInflection().equals(Inflection.SHARP)) {
                if (note.getNbInflections() == 1) {
                    if ((xPos + SHARP.getWidth()) > notePos) {
                        notePos = xPos + SHARP.getWidth();
                    }
                } else {
                    if (note.getNbInflections() == 2) {
                        if ((xPos + DBL_SHARP.getWidth()) > notePos) {
                            notePos = xPos + DBL_SHARP.getWidth();
                        }
                    }
                }
            } else {
                if (note.getInflection().equals(Inflection.FLAT)) {
                    if (note.getNbInflections() == 1) {
                        if ((xPos + FLAT.getWidth()) > notePos) {
                            notePos = xPos + FLAT.getWidth();
                        }
                    } else {
                        if (note.getNbInflections() == 2) {
                            if ((xPos + DBL_FLAT.getWidth()) > notePos) {
                                notePos = xPos + DBL_FLAT.getWidth();
                            }
                        }
                    }
                }
            }
        }
        if (lowestPos != null) {
            drawInterlines(xPos, highestPos, graphics);
            drawInterlines(xPos, lowestPos, graphics);
            drawInterlines(notePos, highestPos, graphics);
            drawInterlines(notePos, lowestPos, graphics);
        }
        for (Note note : notes) {
            int pos = Clef.G.findPosition(note);
            if (note.getInflection().equals(Inflection.SHARP)) {
                if (note.getNbInflections() == 1) {
                    int y = getY(SHARP.getHeight(), pos, Valign.CENTER, 0);
                    graphics.drawImage(SHARP, xPos, y, null);
                } else {
                    if (note.getNbInflections() == 2) {
                        int y = getY(DBL_SHARP.getHeight(), pos, Valign.CENTER, 0);
                        graphics.drawImage(DBL_SHARP, xPos, y, null);
                    }
                }
            } else {
                if (note.getInflection().equals(Inflection.FLAT)) {
                    if (note.getNbInflections() == 1) {
                        int y = getY(FLAT.getHeight(), pos, Valign.TOP, 6);
                        graphics.drawImage(FLAT, xPos, y, null);
                    } else {
                        if (note.getNbInflections() == 2) {
                            int y = getY(DBL_FLAT.getHeight(), pos, Valign.TOP, 6);
                            graphics.drawImage(DBL_FLAT, xPos, y, null);
                        }
                    }
                }
            }
            BufferedImage noteImg = RONDE;
            if (displayNote || as.isSelected(note)) {
                if (TemperedNote.A.equals(note.getTemperedNote())) {
                    noteImg = A;
                }
                if (TemperedNote.B.equals(note.getTemperedNote())) {
                    noteImg = B;
                }
                if (TemperedNote.C.equals(note.getTemperedNote())) {
                    noteImg = C;
                }
                if (TemperedNote.D.equals(note.getTemperedNote())) {
                    noteImg = D;
                }
                if (TemperedNote.E.equals(note.getTemperedNote())) {
                    noteImg = E;
                }
                if (TemperedNote.F.equals(note.getTemperedNote())) {
                    noteImg = F;
                }
                if (TemperedNote.G.equals(note.getTemperedNote())) {
                    noteImg = G;
                }
            }
            int noteFigureY = getY(noteImg.getHeight(), pos, Valign.CENTER, 0);
            graphics.drawImage(noteImg, notePos, noteFigureY, null);
        }
        xPos += RONDE.getWidth();
        xPos += NOTE_SPACE;
        return xPos;
    }

    /**
	 * This method is called after the base staff lines has be drawn.
	 * 
	 * @param graphics
	 */
    protected abstract void drawStaff(Graphics2D graphics);

    /**
	 * This method is called to obtain the staff dimensions.
	 * 
	 * @return
	 */
    protected abstract Dimension getStaffDimension();
}
