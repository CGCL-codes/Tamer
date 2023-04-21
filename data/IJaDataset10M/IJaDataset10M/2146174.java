package ircam.jmax.editors.sequence.renderers;

import ircam.jmax.editors.sequence.*;
import ircam.jmax.editors.sequence.track.*;
import ircam.jmax.editors.sequence.track.Event;
import ircam.jmax.toolkit.*;
import ircam.fts.client.*;
import java.awt.*;

/**
* The piano-roll event renderer in a Score with an ambitus: the line-based event, 
 * with a length , variable width, black color, a label.
 */
public class AmbitusEventRenderer implements SeqObjectRenderer {

    public AmbitusEventRenderer() {
    }

    /**
* constructor.
 */
    public AmbitusEventRenderer(SequenceGraphicContext theGc) {
        gc = theGc;
    }

    /**
* draw the given event in the given graphic context.
 * It takes into account the selection state.
 */
    public void render(Object obj, Graphics g, boolean selected) {
        render(obj, g, selected, gc);
    }

    /**
* draw the given event in the given graphic context.
 * It takes into account the selection state.
 */
    public void render(Object obj, Graphics g, boolean selected, GraphicContext theGc) {
        if (((Event) obj).isHighlighted()) render(obj, g, Event.HIGHLIGHTED, theGc); else if (selected) render(obj, g, Event.SELECTED, theGc); else render(obj, g, Event.DESELECTED, theGc);
    }

    public void render(Object obj, Graphics g, int state, GraphicContext theGc) {
        Event e = (Event) obj;
        SequenceGraphicContext gc = (SequenceGraphicContext) theGc;
        PartitionAdapter pa = (PartitionAdapter) gc.getAdapter();
        int x = pa.getX(e);
        int y = pa.getY(e);
        int length = pa.getLenght(e);
        String label = pa.getLabel(e);
        int height = pa.getHeigth(e);
        String type = pa.getType(e);
        int cue = pa.getCue(e);
        double offset = pa.getOffset(e);
        if (type.equals("note")) {
            if (height > Adapter.NOTE_DEFAULT_HEIGTH) {
                height = height * 2 + Adapter.NOTE_DEFAULT_HEIGTH;
                y = y - height / 2;
            } else {
                height = Adapter.NOTE_DEFAULT_HEIGTH;
                y = y - height + 2;
            }
        } else {
            if (height == 0) height = Adapter.NOTE_DEFAULT_HEIGTH;
            if (type.equals("rest")) y = y - height / 2; else y = y - height + 2;
        }
        if (type.equals("rest")) {
            Color col, bordCol, offsetCol;
            switch(state) {
                default:
                case Event.SELECTED:
                    col = restSelColor;
                    bordCol = Color.red;
                    offsetCol = offSelColor;
                    break;
                case Event.DESELECTED:
                    col = restColor;
                    bordCol = Color.black;
                    offsetCol = offsetColor;
                    break;
                case Event.HIGHLIGHTED:
                    col = restHighlightColor;
                    bordCol = Color.green;
                    offsetCol = offHighlightColor;
                    break;
            }
            g.setColor(col);
            g.fillRect(x, y, length, height);
            if (offset > 0 && offset < pa.getInvLenght(e)) {
                int off_w = pa.getWidth(offset);
                g.fillRect(x, y, off_w, height);
                g.setColor(offsetCol);
                g.drawLine(x + off_w, y, x + off_w, y + height - 1);
            }
            g.setColor(bordCol);
            g.drawLine(x, y, x, y + height - 1);
            g.drawLine(x + 1, y, x + 1, y + height - 1);
            g.drawLine(x + length, y, x + length, y + height - 1);
            g.drawLine(x + length - 1, y, x + length - 1, y + height - 1);
            g.fillRect(x, y + height / 2 - 1, length, 3);
        } else if (type.equals("unvoiced")) {
            Color col, bordCol;
            switch(state) {
                default:
                case Event.SELECTED:
                    col = restSelColor;
                    bordCol = Color.red;
                    break;
                case Event.DESELECTED:
                    col = restColor;
                    bordCol = Color.black;
                    break;
                case Event.HIGHLIGHTED:
                    col = restHighlightColor;
                    bordCol = Color.green;
                    break;
            }
            g.setColor(col);
            g.fillRect(x, y, length, height);
            g.setColor(bordCol);
            g.drawRect(x, y, length, height);
        } else if (type.equals("note") && height > Adapter.NOTE_DEFAULT_HEIGTH) {
            Color col, bordCol, noteCol;
            switch(state) {
                default:
                case Event.SELECTED:
                    col = restSelColor;
                    noteCol = selectedColor;
                    bordCol = noteSelColor;
                    break;
                case Event.DESELECTED:
                    col = restColor;
                    noteCol = deselectedColor;
                    bordCol = noteColor;
                    break;
                case Event.HIGHLIGHTED:
                    col = restHighlightColor;
                    noteCol = highlightColor;
                    bordCol = noteHighlightColor;
                    break;
            }
            g.setColor(col);
            g.fillRect(x, y, length, height);
            g.setColor(bordCol);
            g.drawRect(x, y, length, height);
            g.setColor(noteCol);
            g.fillRect(x, y + height / 2 - 1, length, Adapter.NOTE_DEFAULT_HEIGTH);
            if (pa.getViewMode() == MidiTrackEditor.NMS_VIEW) {
                int alt = pa.getAlteration(e);
                g.setFont(SequenceFonts.getFont(36));
                switch(alt) {
                    case PartitionAdapter.ALTERATION_DIESIS:
                        g.drawString(SequenceFonts.diesis, x - 8, y + 5);
                        break;
                    case PartitionAdapter.ALTERATION_BEMOLLE:
                        g.drawString(SequenceFonts.bemolle, x - 8, y + 5);
                }
            }
        } else {
            switch(state) {
                case Event.SELECTED:
                    g.setColor(selectedColor);
                    break;
                case Event.DESELECTED:
                    g.setColor(deselectedColor);
                    break;
                case Event.HIGHLIGHTED:
                    g.setColor(highlightColor);
                    break;
            }
            if (type.equals("trill")) {
                double tw = 0.5 * (double) length / Math.ceil((double) length / 12.0);
                double i = 0;
                while (i + tw <= length) {
                    int d = (int) i;
                    i += tw;
                    g.drawLine(x + d, y + height, x + (int) i, y);
                    g.drawLine(x + 1 + d, y + height, x + 1 + (int) i, y);
                    d = (int) i;
                    i += tw;
                    g.drawLine(x + d, y, x + (int) i, y + height);
                    g.drawLine(x + 1 + d, y, x + 1 + (int) i, y + height);
                }
            } else if (gc.getSelection().getModel() != gc.getDataModel()) g.drawRect(x, y, length, height); else g.fillRect(x, y, length, height);
            if (pa.getViewMode() == MidiTrackEditor.NMS_VIEW) {
                int alt = pa.getAlteration(e);
                g.setFont(SequenceFonts.getFont(36));
                switch(alt) {
                    case PartitionAdapter.ALTERATION_DIESIS:
                        g.drawString(SequenceFonts.diesis, x - 8, y + 5);
                        break;
                    case PartitionAdapter.ALTERATION_BEMOLLE:
                        g.drawString(SequenceFonts.bemolle, x - 8, y + 5);
                }
            }
        }
        if (!label.equals("")) {
            g.setFont(SequencePanel.rulerFont);
            g.drawString(label, x, y - 5);
        }
        if (cue != -1) {
            int cuex = 7;
            g.setFont(SequencePanel.rulerFont);
            g.drawLine(x, 2, x, gc.getGraphicDestination().getSize().height - 2);
            g.drawLine(x, 2, x - CUE_WIDTH, 2);
            if (cue > 9) cuex = gc.getGraphicDestination().getFontMetrics(SequencePanel.rulerFont).stringWidth("" + cue) + 1;
            g.drawString("" + cue, x - cuex, CUE_HEIGHT);
        }
    }

    public void renderBounds(Object obj, Graphics g, boolean selected, GraphicContext theGc) {
        Event e = (Event) obj;
        PartitionAdapter pa = (PartitionAdapter) ((SequenceGraphicContext) theGc).getAdapter();
        int x = pa.getX(e);
        int y = pa.getY(e);
        int length = pa.getLenght(e);
        int height = pa.getHeigth(e);
        String type = pa.getType(e);
        int cue = pa.getCue(e);
        if (type.equals("note")) {
            if (height > Adapter.NOTE_DEFAULT_HEIGTH) {
                height = height * 2 + Adapter.NOTE_DEFAULT_HEIGTH;
                y = y - height / 2;
            } else {
                height = Adapter.NOTE_DEFAULT_HEIGTH;
                y = y - height + 2;
            }
        } else {
            if (height == 0) height = Adapter.NOTE_DEFAULT_HEIGTH;
            if (type.equals("rest")) y = y - height / 2; else y = y - height + 2;
        }
        if (selected) g.setColor(Color.red); else g.setColor(Color.black);
        g.drawRect(x, y, length, height);
        if (cue != -1) {
            g.drawLine(x, 2, x, g.getClipBounds().height - 2);
            g.drawLine(x - CUE_WIDTH, 2, x, 2);
        }
    }

    /**
* returns true if the given event contains the given (graphic) point
 */
    public boolean contains(Object obj, int x, int y) {
        return contains(obj, x, y, gc);
    }

    /**
* returns true if the given event contains the given (graphic) point
 */
    public boolean contains(Object obj, int x, int y, GraphicContext theGc) {
        Event e = (Event) obj;
        SequenceGraphicContext gc = (SequenceGraphicContext) theGc;
        int evtx = gc.getAdapter().getX(e);
        int evty = gc.getAdapter().getY(e);
        int evtlength = gc.getAdapter().getLenght(e);
        int evtheight = gc.getAdapter().getHeigth(e);
        String type = ((PartitionAdapter) gc.getAdapter()).getType(e);
        int cue = ((PartitionAdapter) gc.getAdapter()).getCue(e);
        if ((cue > -1) && ((x >= evtx - 1 && x <= evtx + 1)) || (x >= evtx - CUE_WIDTH && x <= evtx + 1 && y >= 2 && y <= 2 + CUE_HEIGHT)) return true;
        if (type.equals("rest")) return (evtx <= x && (evtx + evtlength >= x) && evty - evtheight / 2 <= y && evty + evtheight / 2 >= y); else if (type.equals("note")) {
            if (evtheight > Adapter.NOTE_DEFAULT_HEIGTH) return (evtx <= x && (evtx + evtlength >= x) && evty - evtheight - Adapter.NOTE_DEFAULT_HEIGTH / 2 <= y && evty + evtheight + Adapter.NOTE_DEFAULT_HEIGTH / 2 >= y); else return (evtx <= x && (evtx + evtlength >= x) && evty - evtheight + 2 <= y && evty + 2 >= y);
        } else return (evtx <= x && (evtx + evtlength >= x) && evty - evtheight + 2 <= y && evty + 2 >= y);
    }

    Rectangle eventRect = new Rectangle();

    Rectangle tempRect = new Rectangle();

    /**
* returns true if the representation of the given event "touches" the given rectangle
 */
    public boolean touches(Object obj, int x, int y, int w, int h) {
        return touches(obj, x, y, w, h, gc);
    }

    /**
* returns true if the representation of the given event "touches" the given rectangle
 */
    public boolean touches(Object obj, int x, int y, int w, int h, GraphicContext theGc) {
        Event e = (Event) obj;
        SequenceGraphicContext gc = (SequenceGraphicContext) theGc;
        int evtx = gc.getAdapter().getX(e);
        int evty = gc.getAdapter().getY(e);
        int evtlength = gc.getAdapter().getLenght(e);
        int evtheight = gc.getAdapter().getHeigth(e);
        String type = ((PartitionAdapter) gc.getAdapter()).getType(e);
        tempRect.setBounds(x, y, w, h);
        if (type.equals("rest")) eventRect.setBounds(evtx, evty - evtheight / 2, evtlength, evtheight); else if (type.equals("note")) {
            if (evtheight > Adapter.NOTE_DEFAULT_HEIGTH) eventRect.setBounds(evtx, evty - evtheight - Adapter.NOTE_DEFAULT_HEIGTH / 2, evtlength, evtheight * 2 + Adapter.NOTE_DEFAULT_HEIGTH); else eventRect.setBounds(evtx, evty - evtheight + 2, evtlength, evtheight);
        } else eventRect.setBounds(evtx, evty - evtheight + 2, evtlength, evtheight);
        return tempRect.intersects(eventRect);
    }

    public static AmbitusEventRenderer getRenderer() {
        if (staticInstance == null) staticInstance = new AmbitusEventRenderer();
        return staticInstance;
    }

    static final int NOTE_DEFAULT_WIDTH = 5;

    public SequenceGraphicContext gc;

    public static AmbitusEventRenderer staticInstance;

    static final int CUE_WIDTH = 8;

    static final int CUE_HEIGHT = 13;

    Color restColor = new Color(165, 165, 165, 60);

    Color restSelColor = new Color(255, 0, 0, 60);

    Color restHighlightColor = new Color(0, 255, 0, 60);

    Color offsetColor = new Color(165, 165, 165, 150);

    Color offSelColor = new Color(255, 0, 0, 150);

    Color offHighlightColor = new Color(0, 255, 0, 150);

    Color noteColor = new Color(165, 165, 165, 100);

    Color noteSelColor = new Color(255, 0, 0, 100);

    Color noteHighlightColor = new Color(0, 255, 0, 100);

    Color deselectedColor = new Color(0, 0, 0, 180);

    Color selectedColor = new Color(255, 0, 0, 180);

    Color highlightColor = new Color(0, 255, 0, 180);

    int oldX, oldY;
}
