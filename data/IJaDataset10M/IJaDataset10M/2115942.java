package org.jfonia.view.main.header;

import org.jfonia.notation.Notation;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import org.jfonia.assets.fonts.Fughetta;
import org.jfonia.assets.fonts.TextGlyph;
import org.jfonia.connect5.basics.Observer;
import org.jfonia.constants.ViewConstants;
import org.jfonia.view.panels.LeadSheetPanel;

/**
 *
 * @author Rik Bauwens
 */
public class GlyphLabel extends JComponent implements MouseListener {

    private String glyphId;

    private TextGlyph textGlyph;

    private Color backgroundColor;

    private NotationType notationType;

    private int value;

    public GlyphLabel(Component parent, String glyphId, NotationType notationType, int value) {
        backgroundColor = ViewConstants.OPTION_BACKGROUND_COLOR;
        this.glyphId = glyphId;
        setPreferredSize(new Dimension(ViewConstants.OPTION_BUTTON_SIZE, ViewConstants.OPTION_BUTTON_SIZE));
        setMaximumSize(getPreferredSize());
        addMouseListener(this);
        this.notationType = notationType;
        this.value = value;
        if (notationType == NotationType.NOTEHEAD) {
            Notation.getInstance().getNoteHeadNode().addObserver(new Observer() {

                public void onNotify(Object source) {
                    setSelected(getValue() == Notation.getInstance().getNoteHead());
                    repaint();
                }
            });
            setSelected(getValue() == Notation.getInstance().getNoteHead());
        } else if (notationType == NotationType.ACCIDENTAL) {
            Notation.getInstance().getAccidentalNode().addObserver(new Observer() {

                public void onNotify(Object source) {
                    setSelected(getValue() == Notation.getInstance().getAccidental());
                    repaint();
                }
            });
            setSelected(getValue() == Notation.getInstance().getAccidental());
        } else if (notationType == NotationType.REST) {
            Notation.getInstance().getRestNode().addObserver(new Observer() {

                public void onNotify(Object source) {
                    setSelected(getValue() == Notation.getInstance().getRest());
                    repaint();
                }
            });
            setSelected(getValue() == Notation.getInstance().getRest());
        } else if (notationType == NotationType.CLEF) {
            Notation.getInstance().getClefNode().addObserver(new Observer() {

                public void onNotify(Object source) {
                    setSelected(getValue() == Notation.getInstance().getClef());
                    repaint();
                }
            });
            setSelected(getValue() == Notation.getInstance().getClef());
        } else if (notationType == NotationType.KEY_SIGNATURE) {
            Notation.getInstance().getKeySignatureNode().addObserver(new Observer() {

                public void onNotify(Object source) {
                    setSelected(getValue() == Notation.getInstance().getKeySignatureDirection());
                    repaint();
                }
            });
            setSelected(getValue() == Notation.getInstance().getKeySignatureDirection());
        }
        repaint();
    }

    private int getValue() {
        return value;
    }

    private void setGlyph(String glyphId) {
        this.textGlyph = Fughetta.getGlyph(glyphId);
        textGlyph.setSize(ViewConstants.SYMBOL_SIZE);
    }

    private void setSelected(boolean selected) {
        if (selected) backgroundColor = ViewConstants.OPTION_SELECTED_COLOR; else backgroundColor = ViewConstants.OPTION_BACKGROUND_COLOR;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        try {
            Fughetta.init(g2);
        } catch (Exception e) {
            Logger.getLogger(LeadSheetPanel.class.getName()).log(Level.SEVERE, null, e);
        }
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getPreferredSize().width, getPreferredSize().height, ViewConstants.ROUNDRECT_ARC_WIDTH, ViewConstants.ROUNDRECT_ARC_HEIGHT);
        g2.setColor(Color.BLACK);
        if (textGlyph == null) setGlyph(glyphId);
        textGlyph.paint(g2, getWidth() / 2 - (Math.abs(textGlyph.getBounds().getCenterX())), getHeight() / 2 + (Math.abs(textGlyph.getBounds().getCenterY())));
    }

    public void mouseClicked(MouseEvent e) {
        if (notationType == NotationType.NOTEHEAD) Notation.getInstance().setNoteHead(value); else if (notationType == NotationType.ACCIDENTAL) Notation.getInstance().setAccidental(value); else if (notationType == NotationType.REST) Notation.getInstance().setRest(value); else if (notationType == NotationType.CLEF) Notation.getInstance().setClef(value); else if (notationType == NotationType.KEY_SIGNATURE) Notation.getInstance().setKeySignatureDirection(value);
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
        if (backgroundColor != ViewConstants.OPTION_SELECTED_COLOR) {
            backgroundColor = ViewConstants.OPTION_HIGHLIGHTED_COLOR;
            repaint();
        }
    }

    public void mouseExited(MouseEvent e) {
        if (backgroundColor != ViewConstants.OPTION_SELECTED_COLOR) {
            backgroundColor = ViewConstants.OPTION_BACKGROUND_COLOR;
            repaint();
        }
    }
}
