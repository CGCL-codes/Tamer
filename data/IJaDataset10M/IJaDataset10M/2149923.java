package org.velma.treeviewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.event.EventListenerList;
import javax.swing.filechooser.FileFilter;
import org.velma.SelectableBySequence;
import org.velma.data.MSASelection;
import org.velma.data.msa.MSA;
import org.velma.event.ExpandedEvent;
import org.velma.event.ExpandedListener;
import org.velma.event.SelectionEvent;
import org.velma.event.SelectionListener;

/**
 * A view for displaying my version of a phylogenetic tree. The display is
 * fairly simple; the most interesting part is the expandable/collapsable
 * subtrees.
 * 
 * @author Andy Walsh
 * 
 */
public class TreePanel extends JPanel implements ComponentListener, ExpandedListener, MouseListener, MouseMotionListener, SelectableBySequence {

    private static final long serialVersionUID = -352099563741023628L;

    protected EventListenerList listenerList;

    protected SelectionEvent selectionEvent = null;

    protected JInternalFrame frame;

    protected JPopupMenu popupMenu;

    private Color boxColor = new Color(255, 0, 0, 20);

    protected boolean isVisible;

    private boolean addToSelection;

    private boolean isSelecting;

    private int boxStartX;

    private int boxStartY;

    private int boxStopX;

    private int boxStopY;

    private double scale_factor;

    protected MyTree tree;

    protected String viewName;

    public Hashtable<String, Integer> treeToAlignment;

    private MSA ma;

    private String[] keyChain;

    private MSASelection selection;

    public TreePanel(MSA ma, MSASelection selection, String viewName, String[] keyChain, MyTree tree, Hashtable<String, Integer> treeToAlignment) {
        super();
        this.ma = ma;
        this.selection = selection;
        this.tree = tree;
        this.keyChain = keyChain;
        this.viewName = viewName;
        tree.addExpandedListener(this);
        setSize(getPreferredSize());
        setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        isSelecting = false;
        scale_factor = Double.NaN;
        addMouseListener(this);
        addMouseMotionListener(this);
        addComponentListener(this);
        listenerList = new EventListenerList();
        this.treeToAlignment = treeToAlignment;
        String treeLabels[] = tree.getLeafLabels();
        for (String seqName : treeLabels) this.treeToAlignment.put(seqName, ma.getIndexOfSequence(seqName));
        makePopupMenu();
    }

    public MSASelection getMSASelection() {
        return selection;
    }

    public String[] getKeyChain() {
        return keyChain;
    }

    private void calcScaleFactor() {
        Rectangle bounds = new Rectangle(this.getSize());
        Insets insets = this.getInsets();
        FontMetrics fm = getFontMetrics(getFont());
        double maxdist = Double.MIN_VALUE;
        String label = "";
        MyNode prev = tree.getRoot();
        MyNode curr = prev.descend();
        while (true) {
            if (curr != null) if (curr.distanceToRoot() > maxdist) {
                maxdist = curr.distanceToRoot();
                label = curr.toString();
            }
            if (curr == null) {
                curr = prev;
                prev = curr.ascend();
                curr = prev;
                if (prev == null) break;
                prev = curr.ascend();
            } else {
                prev = curr;
                curr = prev.descend();
            }
        }
        scale_factor = ((double) (bounds.width - (insets.left + insets.right + 50)) - (double) fm.stringWidth(label)) / maxdist;
    }

    public void componentHidden(ComponentEvent arg0) {
    }

    public void componentMoved(ComponentEvent arg0) {
    }

    public void componentResized(ComponentEvent e) {
        calcScaleFactor();
        repaint();
    }

    public void componentShown(ComponentEvent arg0) {
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1200, tree.getNumberOfLeavesDisplayed() * (getFontMetrics(getFont()).getHeight() + 2));
    }

    public double getScaleFactor() {
        calcScaleFactor();
        return scale_factor;
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) tree.calcContainingNode(e.getPoint(), new Rectangle(getSize()), getInsets(), scale_factor);
    }

    public void mouseDragged(MouseEvent e) {
        if (isSelecting) {
            boxStopX = e.getX();
            boxStopY = e.getY();
            repaint();
        }
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void mouseMoved(MouseEvent e) {
        if (!isSelecting) {
            boolean redraw = tree.highlightContainingNode(e.getPoint(), new Rectangle(this.getSize()), this.getInsets(), getScaleFactor());
            if (redraw) this.repaint();
        }
    }

    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            isSelecting = true;
            addToSelection = e.getModifiers() == (InputEvent.CTRL_MASK | InputEvent.BUTTON1_MASK) || e.getModifiers() == (InputEvent.SHIFT_MASK | InputEvent.BUTTON1_MASK);
            boxStartX = e.getX();
            boxStartY = e.getY();
        }
        this.maybeShowPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            boxStopX = e.getX();
            boxStopY = e.getY();
            isSelecting = false;
            tree.calcSelected(boxStartX, boxStartY, boxStopX, boxStopY, new Rectangle(this.getSize()), this.getInsets(), getScaleFactor(), addToSelection);
            boolean[] leafSelection = tree.getSelectedLeaves();
            for (int i = 0; i < leafSelection.length; i++) if (leafSelection[i]) selection.selectSeq(i); else selection.deselectSeq(i);
            repaint();
            fireSelectionEvent();
        }
        this.maybeShowPopup(e);
    }

    protected JMenuBar getJMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem saveImage = new JMenuItem(new AbstractAction("Save as Image") {

            private static final long serialVersionUID = -1818418587333517233L;

            public void actionPerformed(ActionEvent e) {
                JFileChooser pngChooser = new JFileChooser(System.getProperty("user.dir"));
                pngChooser.setFileFilter(new FileFilter() {

                    @Override
                    public boolean accept(File f) {
                        return f.isDirectory() || f.getName().toLowerCase().endsWith("png");
                    }

                    @Override
                    public String getDescription() {
                        return "PNG Files";
                    }
                });
                pngChooser.setDialogTitle("Save to PNG File");
                int returnVal = pngChooser.showSaveDialog(getTopLevelAncestor());
                if (returnVal == JFileChooser.APPROVE_OPTION) saveToImageFile(pngChooser.getSelectedFile().getAbsolutePath());
            }
        });
        menu.add(saveImage);
        menuBar.add(menu);
        return menuBar;
    }

    public void saveToImageFile(String filename) {
        if (!filename.endsWith("png")) filename += ".png";
        try {
            int w = getWidth();
            int h = getHeight();
            BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = bufferedImage.createGraphics();
            g2d.setBackground(new Color(0, 0, 0, 0));
            paint(g2d);
            g2d.dispose();
            ImageIO.write(bufferedImage, "png", new File(filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected JInternalFrame makeJInternalFrame(Dimension size) {
        JInternalFrame frame = new JInternalFrame(viewName, true, true, true, true);
        JScrollPane scrollPane = new JScrollPane(this);
        frame.getContentPane().add(scrollPane);
        frame.setJMenuBar(getJMenuBar());
        frame.pack();
        frame.setSize(new Dimension(Math.min(size.width, frame.getSize().width), Math.min(size.height, frame.getSize().height)));
        return frame;
    }

    private void makePopupMenu() {
        popupMenu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Filter on selected sequences");
        popupMenu.add(menuItem);
    }

    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            popupMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    @Override
    public void paint(Graphics g) {
        tree.paint(g, new Rectangle(getSize()), getInsets(), getScaleFactor());
        if (isSelecting) {
            Color gColor = g.getColor();
            int x = Math.min(boxStartX, boxStopX), y = Math.min(boxStartY, boxStopY), width = Math.abs(boxStopX - boxStartX), height = Math.abs(boxStopY - boxStartY);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, width, height);
            g.setColor(boxColor);
            g.fillRect(x, y, width, height);
            g.setColor(gColor);
        }
    }

    public void addSelectionListener(SelectionListener l) {
        listenerList.add(SelectionListener.class, l);
    }

    public void fireSelectionEvent() {
        SelectionListener listeners[] = listenerList.getListeners(SelectionListener.class);
        if (selectionEvent == null || selectionEvent.getSource() == null) selectionEvent = new SelectionEvent(this, keyChain, SelectionEvent.SEQUENCE_SELECTION_EVENT);
        for (SelectionListener l : listeners) l.selectionChanged(selectionEvent);
    }

    public String getViewName() {
        return viewName;
    }

    public JInternalFrame openInFrame(Dimension size) {
        if (frame == null) frame = makeJInternalFrame(size);
        frame.setVisible(true);
        isVisible = true;
        return frame;
    }

    public void removeSelectionListener(SelectionListener l) {
        listenerList.remove(SelectionListener.class, l);
    }

    public void updateSelection() {
        tree.setSelectedLeaves(selection.getSeqSelected());
        repaint();
    }

    public void invertSelectedSequences() {
    }

    public void selectAllSequences() {
    }

    public void selectNoSequences() {
    }

    public JInternalFrame getContainingFrame() {
        return frame;
    }

    public void closeFrame() {
        if (frame == null) return;
        frame.dispose();
    }

    public void setContainingFrameBounds(Rectangle r) {
        frame.setBounds(r);
    }

    public void nodeExpanded(ExpandedEvent e) {
        revalidate();
    }
}
