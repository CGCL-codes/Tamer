package view;

import interfaces.Observable;
import interfaces.Observer;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JComponent;
import model.S3DetObject;
import model.SequenceImpl;
import util.Alignment;
import util.ColorScheme;
import util.Constants;
import util.SelectionManager;
import util.SequenceOrderer;
import view.Structure3DFrame.JmolPanel;

/**
 * This class does all the sequence painting,
 * implements Observable, as the structure frame listens to the mouse events on the alignment panel.
 * @author Thilo Muth
 *
 */
public class SequencePainter extends JComponent implements Observable, Observer {

    /**
	 * Graphics2D object.
	 */
    private Graphics2D graphics2d;

    /**
	 * Graphics object.
	 */
    private Graphics graphics;

    /**
	 * Alignment view properties.
	 */
    private AlignViewProps viewProps;

    /**
	 * Font Metrics
	 */
    private FontMetrics fontMetrics;

    /**
	 * Alignment panel
	 */
    private AlignPanel alignPanel;

    /**
	 * Integer to color hash map
	 */
    private HashMap<Integer, Color> colColorMap;

    /**
	 * Image width.
	 */
    private int iWidth;

    /**
	 * Image height.
	 */
    private int iHeight;

    /**
	 * s3det object
	 */
    private S3DetObject s3detobj;

    /**
	 * List of observers.
	 */
    private Vector observersList;

    /**
	 * Current position in the sequence.
	 */
    private int currentPosition;

    /**
	 * Selected sequence
	 */
    private SequenceImpl selectedSequence;

    /**
	 * Boolean for highlighting when mouse was clicked.
	 */
    public boolean bHighlight = false;

    /**
	 * Residue position of mouse event.
	 */
    private int highX;

    /**
	 * Sum of the scroll x values.
	 */
    private int sumScrollX;

    /**
	 * The JmolPanel.
	 */
    private JmolPanel jmolPanel;

    /**
	 * The utility alignment class.
	 */
    private Alignment alignment;

    /**
	 * Is export flag.
	 */
    private boolean isExport;

    /**
	 * Constructor of the SeqRenderer.
	 * In an optional case it uses the s3detobject to order the sequences and color
	 * @param viewProps
	 * @param alignPanel
	 * @param s3detobj
	 */
    public SequencePainter(AlignViewProps viewProps, AlignPanel alignPanel, S3DetObject s3detobj) {
        this.viewProps = viewProps;
        this.alignPanel = alignPanel;
        this.s3detobj = s3detobj;
        setLayout(new BorderLayout());
        observersList = new Vector();
        this.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (SelectionManager.isEnabled()) {
                    highX = getAAPosition(e);
                    bHighlight = true;
                    repaint();
                    notifyOnMouseClick(e);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        graphics2d = (Graphics2D) g.create();
        graphics2d.setFont(viewProps.getFont());
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        int yEnd = viewProps.getYEnd() * viewProps.getCharHeight();
        g.fillRect(0, 0, getWidth(), yEnd);
        iWidth = getWidth();
        iHeight = getHeight();
        g.setColor(Color.WHITE);
        g.fillRect(0, yEnd, getWidth(), getHeight() - yEnd);
        drawPanel(graphics2d, viewProps.getXStart(), viewProps.getXEnd(), viewProps.getYStart(), viewProps.getYEnd(), 0);
        highlightResidue(g, highX);
        g.setColor(Color.WHITE);
    }

    /**
	 * This methods draws the panel.
	 * @param g1
	 * @param startRes
	 * @param endRes
	 * @param startSeq
	 * @param endSeq
	 * @param offset
	 */
    public void drawPanel(Graphics g1, int startRes, int endRes, int startSeq, int endSeq, int offset) {
        draw(g1, startRes, endRes, startSeq, endSeq, offset);
    }

    /**
	 * This is the final draw method.
	 * @param g
	 * @param startRes
	 * @param endRes
	 * @param startSeq
	 * @param endSeq
	 * @param offset
	 */
    private void draw(Graphics g, int startRes, int endRes, int startSeq, int endSeq, int offset) {
        g.setFont(viewProps.getFont());
        init(g);
        SequenceImpl nextSequence;
        if (s3detobj == null) {
            for (int i = startSeq; i < endSeq; i++) {
                nextSequence = viewProps.getAlObj().getSequenceAt(i);
                drawSeq(nextSequence, startRes, endRes, offset + ((i - startSeq) * viewProps.getCharHeight()));
            }
        } else {
            SequenceOrderer seqOrderer = new SequenceOrderer(viewProps.getAlObj(), s3detobj);
            SequenceImpl[] sequences = seqOrderer.getClusterSequences();
            int j = startSeq;
            int startBox = 0;
            int endBox = 0;
            for (int i = startSeq; i < endSeq; i++) {
                nextSequence = sequences[i];
                drawSeq(nextSequence, startRes, endRes, offset + ((i - startSeq) * viewProps.getCharHeight()));
                if (nextSequence.isClusterStart()) {
                    startBox = i - startSeq;
                    if (isExport) {
                        drawHorizontal(nextSequence, 0, viewProps.getCharWidth() * nextSequence.getLength(), offset + (startBox * viewProps.getCharHeight()), nextSequence.getClusterColor());
                    } else {
                        drawHorizontal(nextSequence, 0, Constants.SEQPANEL_WIDTH - 1, offset + (startBox * viewProps.getCharHeight()), nextSequence.getClusterColor());
                    }
                }
                if (nextSequence.isClusterEnd()) {
                    endBox = i + 1 - startSeq;
                    if (isExport) {
                        drawHorizontal(nextSequence, 0, (viewProps.getCharWidth() * nextSequence.getLength()) - 1, offset + (endBox * viewProps.getCharHeight() - 2), nextSequence.getClusterColor());
                    } else {
                        drawHorizontal(nextSequence, 0, Constants.SEQPANEL_WIDTH - 1, offset + (endBox * viewProps.getCharHeight() - 2), nextSequence.getClusterColor());
                    }
                }
                if (isExport) {
                    drawVertical(nextSequence, 0, (startBox * viewProps.getCharHeight() + 1), (endBox * viewProps.getCharHeight()), nextSequence.getClusterColor());
                    drawVertical(nextSequence, (viewProps.getCharWidth() * nextSequence.getLength()) - 1, (startBox * viewProps.getCharHeight() + 1), (endBox * viewProps.getCharHeight()), nextSequence.getClusterColor());
                } else {
                    drawVertical(nextSequence, 0, (startBox * viewProps.getCharHeight() + 1), (endBox * viewProps.getCharHeight()), nextSequence.getClusterColor());
                    drawVertical(nextSequence, Constants.SEQPANEL_WIDTH - 1, (startBox * viewProps.getCharHeight() + 1), (endBox * viewProps.getCharHeight()), nextSequence.getClusterColor());
                }
                j++;
            }
        }
    }

    /**
	 * Initializes the graphics object.
	 * @param g
	 */
    public void init(Graphics g) {
        graphics = g;
        fontMetrics = g.getFontMetrics();
    }

    /**
	 * This method colors the columns
	 * @param currentColor
	 * @param xPos
	 * @param yPos
	 */
    public void colorColumn(Color currentColor, int xPos, int yPos) {
        int width = viewProps.getCharWidth();
        Color standardColor = Color.WHITE;
        if (standardColor != currentColor) {
            if (currentColor != null) {
                graphics.setColor(currentColor);
                graphics.fillRect(viewProps.getCharWidth() * (xPos - viewProps.getXStart()), yPos, width, viewProps.getCharHeight());
            }
            width += viewProps.getCharWidth();
        }
    }

    /**
	 * This methods draws the background.
	 * @param start
	 * @param end
	 * @param yPos
	 */
    public void drawBackground(int start, int end, int yPos) {
        Color columnColor;
        int i = start;
        while (i < end) {
            if (colColorMap != null) {
                if (colColorMap.containsKey(i)) {
                    columnColor = colColorMap.get(i);
                    colorColumn(columnColor, i - 1, yPos);
                }
            } else {
                break;
            }
            i++;
        }
    }

    /**
	 * This methods draws an horizontal line (used for the group boxes).
	 * @param xStart
	 * @param xEnd
	 * @param yPos
	 */
    public void drawHorizontal(SequenceImpl seq, int xStart, int xEnd, int yPos, Color linecolor) {
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setColor(linecolor);
        g2d.setStroke(new BasicStroke(2.0f));
        double startX = new Integer(xStart).doubleValue();
        double endX = new Integer(xEnd).doubleValue();
        double startY = new Integer(yPos).doubleValue();
        double endY = new Integer(yPos).doubleValue();
        g2d.clearRect(xStart, yPos, xEnd - xStart, 2);
        Line2D line2d = new Line2D.Double(startX, startY, endX, endY);
        g2d.draw(line2d);
    }

    /**
	 * This methods draws an vertical line (used for the group boxes).
	 * @param seq
	 * @param xPos
	 * @param yStart
	 * @param yEnd
	 * @param linecolor
	 */
    public void drawVertical(SequenceImpl seq, int xPos, int yStart, int yEnd, Color linecolor) {
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setColor(linecolor);
        g2d.setStroke(new BasicStroke(1.5f));
        double startX = new Integer(xPos).doubleValue();
        double endX = new Integer(xPos).doubleValue();
        double startY = new Integer(yStart).doubleValue();
        double endY = new Integer(yEnd).doubleValue();
        Line2D line2d = new Line2D.Double(startX, startY, endX, endY);
        g2d.draw(line2d);
    }

    /**
	 * This methods actually draws the sequence. 
	 * @param seq
	 * @param start
	 * @param end
	 * @param yPos
	 */
    public void drawSeq(SequenceImpl seq, int start, int end, int yPos) {
        int yStart = yPos;
        yPos += viewProps.getCharHeight() - viewProps.getCharHeight() / 5;
        int charOffset = 0;
        char letter;
        if (end + 1 >= seq.getLength()) {
            end = seq.getLength() - 1;
        }
        ColorScheme scheme = new ColorScheme();
        for (int i = start; i <= end; i++) {
            letter = seq.getLetterAt(i);
            if (scheme.containsKey(letter)) {
                graphics.setColor(ColorScheme.getColor(letter));
                graphics.fillRect(viewProps.getCharWidth() * (i - viewProps.getXStart()), yStart, viewProps.getCharWidth(), viewProps.getCharHeight());
            }
            graphics.setColor(Color.BLACK);
            Font font = new Font("Sans Serif", Font.PLAIN, 12);
            graphics.setFont(font);
            charOffset = (viewProps.getCharWidth() - fontMetrics.charWidth(letter)) / 2;
            graphics.drawString(String.valueOf(letter), charOffset + viewProps.getCharWidth() * (i - start), yPos);
        }
    }

    /**
	 * This method does the scroll painting.
	 * @param xValue
	 * @param yValue
	 */
    public void scrollPainting(int xValue, int yValue) {
        graphics2d.copyArea(xValue * viewProps.getCharWidth(), yValue * viewProps.getCharHeight(), iWidth, iHeight, -xValue * viewProps.getCharWidth(), -yValue * viewProps.getCharHeight());
        int xStart = viewProps.getXStart();
        int xEnd = viewProps.getXEnd();
        int yStart = viewProps.getYStart();
        int yEnd = viewProps.getYEnd();
        int transX = 0;
        int transY = 0;
        if (xValue > 0) {
            xEnd++;
            transX = (xEnd - xStart - xValue) * viewProps.getCharWidth();
            xStart = xEnd - xValue;
            sumScrollX += xValue;
        } else if (xValue < 0) {
            xEnd = xStart - xValue - 1;
            sumScrollX += xValue;
        } else if (yValue > 0) {
            yStart = yEnd - yValue;
            if (yStart < viewProps.getYStart()) {
                yStart = viewProps.getYStart();
            } else {
                transY = iHeight - (yValue * viewProps.getCharHeight());
            }
        } else if (yValue < 0) {
            yEnd = yStart - yValue;
            if (yEnd > viewProps.getYEnd()) {
                yEnd = viewProps.getYEnd();
            }
        }
        graphics2d.translate(transX, transY);
        drawPanel(graphics2d, xStart, xEnd, yStart, yEnd, 0);
        graphics2d.translate(-transX, -transY);
        repaint();
    }

    /**
	 * Notifies the observers on a mouse click.
	 * 
	 */
    private void notifyOnMouseClick(MouseEvent ev) {
        selectedSequence = viewProps.getAlObj().getSequenceAt(getSequence(ev));
        currentPosition = selectedSequence.findPosition(getAAPosition(ev));
        if (!Constants.isGap(selectedSequence.getLetterAt(currentPosition - 1))) {
            notifyObservers();
        }
    }

    /**
	 * Highlights a residue for a mouse click.
	 * @param ev
	 */
    private void highlightResidue(Graphics g, int x) {
        if (bHighlight) {
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2.0f));
            int xPos = x * viewProps.getCharWidth() - sumScrollX * viewProps.getCharWidth();
            double startX = new Integer(xPos).doubleValue();
            double endX = new Integer(xPos + viewProps.getCharWidth()).doubleValue();
            double startY = new Integer(0).doubleValue();
            double endY = new Integer(viewProps.getCharHeight() * viewProps.getYEnd()).doubleValue();
            Line2D leftLine = new Line2D.Double(startX, startY, startX, endY);
            Line2D rightLine = new Line2D.Double(endX, startY, endX, endY);
            Line2D bottomLine = new Line2D.Double(startX, endY, endX, endY);
            g2d.draw(leftLine);
            g2d.draw(rightLine);
            g2d.draw(bottomLine);
        }
    }

    /**
	 * Returns the position of a amino acid for a mouse event.
	 * 
	 * @param evt
	 * @return pos Integer
	 */
    private int getAAPosition(MouseEvent evt) {
        int pos = 0;
        int x = evt.getX();
        pos = (x / viewProps.getCharWidth()) + viewProps.getXStart();
        return pos;
    }

    /**
	 * Returns the sequence for a mouse event.
	 * 
	 * @param evt
	 * @return seq Integer
	 */
    private int getSequence(MouseEvent evt) {
        int seq = 0;
        int y = evt.getY();
        seq = Math.min((y / viewProps.getCharHeight()) + viewProps.getYStart(), viewProps.getAlObj().getHeight() - 1);
        return seq;
    }

    /**
	 * Returns the alignment panel.
	 * @return alignPanel AlignPanel
	 */
    public AlignPanel getAlignPanel() {
        return alignPanel;
    }

    /**
	 * Notify the observer.
	 */
    public void notifyObservers() {
        for (int i = 0; i < observersList.size(); i++) {
            Observer observer = (Observer) observersList.elementAt(i);
            observer.update(this);
        }
    }

    public void notifyUpperPanels() {
        for (int i = 0; i < 2; i++) {
            Observer observer = (Observer) observersList.elementAt(i);
            observer.update(this);
        }
    }

    /**
	 * Registers the observer.
	 */
    public void register(Observer obs) {
        observersList.addElement(obs);
    }

    /**
	 * Deregisters the observer.
	 */
    public void deregister(Observer obs) {
        observersList.removeElement(obs);
    }

    /**
	 * Returns the current position.
	 * @return currentPosition int
	 */
    public int getCurrentPosition() {
        return currentPosition;
    }

    /**
	 * Returns the selected sequence.
	 * @return selectedSequence SequenceImpl
	 */
    public SequenceImpl getSelectedSequence() {
        return selectedSequence;
    }

    /**
	 * Updates the data from the observable object.
	 */
    public void update(Observable object) {
        String select;
        int pos, alignPos;
        if (object instanceof JmolPanel) {
            jmolPanel = (JmolPanel) object;
            select = jmolPanel.getSelection()[1].toString();
            pos = new Integer(select.substring(select.indexOf(']') + 1, select.indexOf(':')));
            alignPos = getAlignPos(pos);
            if (alignPos != -1) {
                jmolPanel.eval("select " + SelectionManager.getLastSelection() + ":" + SelectionManager.getSelectedChain() + "; color cpk; wireframe off; ");
                jmolPanel.eval("select " + pos + ":" + SelectionManager.getSelectedChain() + "; color green; wireframe 60; ");
                SelectionManager.setLastSelection(pos);
                highX = alignPos;
                bHighlight = true;
                repaint();
            }
        }
    }

    /**
	 * Sets the jMolPanel and registers the seqPainter.
	 * @param jmolPanel
	 */
    public void setJmolPanel(JmolPanel jmolPanel) {
        this.jmolPanel = jmolPanel;
        this.jmolPanel.register(this);
    }

    /**
	 * Unsets the seqPainter.
	 */
    public void unsetJmolPanel() {
        this.jmolPanel.deregister(this);
    }

    /**
	 * Returns for a given position in the alignment the position in the PDB sequence.
	 * @param alignPosition
	 * @return
	 */
    private int getAlignPos(int pdbPos) {
        int alignPos;
        HashMap<Integer, Integer> map = alignment.getPdb2alignPositionMap();
        if (map.containsKey(pdbPos)) {
            alignPos = map.get(pdbPos);
        } else {
            alignPos = -1;
        }
        return alignPos;
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }

    public void setExport(boolean isExport) {
        this.isExport = isExport;
    }

    public void setSumScrollX(int sumScrollX) {
        this.sumScrollX = sumScrollX;
    }

    public int getSumScrollX() {
        return sumScrollX;
    }
}
