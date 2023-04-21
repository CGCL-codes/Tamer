package mathgame.editor;

import mathgame.common.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;

public class LevelEditorPane extends JPanel implements MouseListener, MouseMotionListener {

    public static final int NUMBER_OF_SUBJECT_WEIGHTS = 3;

    public static final int DEFAULT_GRID_SIZE = 20;

    public static final int[] LAYER_MASKS = { 1, 2, 4, 8 };

    static final String[] databaseSubjectsSorted;

    private Color GRID_COLOR = new Color(195, 195, 195);

    static {
        String[] subj = Database.getInstance().getSubjects();
        ArrayList<String> v = new ArrayList<String>(subj.length);
        for (int i = 0; i < subj.length; i++) v.add(subj[i]);
        Collections.sort(v);
        subj = v.toArray(subj);
        databaseSubjectsSorted = subj;
    }

    Editor parent;

    private LayeredPictureChooser lpc;

    private Rectangle toDrawRectangle;

    private Rectangle selectionRect;

    private boolean selection;

    private PositionMap[] positionMaps;

    private int width;

    private int height;

    int time = 200;

    String tune = new String();

    String backgroundImagePath = new String();

    int[] parts = new int[3];

    int[] subjSelected = new int[Database.getInstance().getSubjects().length];

    int level = (Database.getInstance().getMinLevel() + Database.getInstance().getMaxLevel()) / 2;

    int keyLimit = 50;

    int forceLimit = -1;

    int forceInterval = 0;

    private BufferedImage backgroundImage;

    private Paint backgroundPaint;

    private Rectangle2D.Float backgroundAnchor = new Rectangle2D.Float();

    private boolean showGrid;

    private boolean snapToGrid;

    private boolean selectLayer;

    private int gridSize;

    private double scaleFactor;

    private int visibleLayers;

    private int currentLayer;

    private int prevSelected;

    private File currentFile;

    private LinkedList<Sprite> selectedSprites = new LinkedList<Sprite>();

    private JPopupMenu rightClickMenu;

    public Sprite player;

    private SimpleFileFilter fileFilter;

    public JFileChooser fileChooser;

    protected JMenuItem exit;

    protected JMenuItem save;

    protected JMenuItem saveAs;

    protected JMenuItem open;

    protected JMenuItem opennew;

    protected JCheckBoxMenuItem pickLayer;

    protected JCheckBoxMenuItem snap;

    protected JCheckBoxMenuItem show;

    protected JMenuItem size;

    protected JMenuItem properties;

    protected JMenuItem setBackground;

    protected JMenuItem removeBackground;

    protected JMenuItem testRun;

    protected JMenuItem showMap;

    protected JMenuItem props;

    protected JMenuItem delete;

    private int diffX = 0;

    private int diffY = 0;

    private int prevX = 0;

    private int prevY = 0;

    private boolean dragged = false;

    private boolean selected = false;

    private boolean buttonOne = true;

    private boolean addSprites = false;

    private class AWTKeyListener implements AWTEventListener {

        public void eventDispatched(AWTEvent event) {
            int code = ((KeyEvent) event).getKeyCode();
            if (event.getID() == KeyEvent.KEY_PRESSED) {
                if (code == KeyEvent.VK_DELETE) {
                    try {
                        deleteSelectedSprites();
                    } catch (Exception err) {
                        err.printStackTrace();
                    }
                } else if (code == KeyEvent.VK_CONTROL) {
                    if (lpc.isSelected()) setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                }
            } else if (event.getID() == KeyEvent.KEY_RELEASED) {
                if (code == KeyEvent.VK_CONTROL) setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }

    private class AllSpritesIterator implements Iterator {

        private PositionMap currentMap;

        private Iterator currentIterator;

        public AllSpritesIterator() {
            currentMap = positionMaps[LevelToolbox.BACKGROUND];
            currentIterator = currentMap.iterator();
        }

        public boolean hasNext() {
            if (currentIterator.hasNext()) return true; else if (currentMap == positionMaps[LevelToolbox.BACKGROUND]) {
                currentMap = positionMaps[LevelToolbox.BACKLAYER];
                currentIterator = currentMap.iterator();
            } else if (currentMap == positionMaps[LevelToolbox.BACKLAYER]) {
                currentMap = positionMaps[LevelToolbox.PLAYERLAYER];
                currentIterator = currentMap.iterator();
            } else if (currentMap == positionMaps[LevelToolbox.PLAYERLAYER]) {
                currentMap = positionMaps[LevelToolbox.FRONTLAYER];
                currentIterator = currentMap.iterator();
            } else return false;
            return hasNext();
        }

        public Object next() {
            return (Object) nextSprite();
        }

        public Sprite nextSprite() {
            if (hasNext()) {
                return (Sprite) currentIterator.next();
            }
            return null;
        }

        public void remove() {
        }
    }

    private class NewLevelDialog extends JDialog implements ActionListener {

        private JTextField widthField = new JTextField(5);

        private JTextField heightField = new JTextField(5);

        private JButton okButton = new JButton("OK");

        private JButton cancelButton = new JButton("Avbryt");

        public NewLevelDialog(Component parent) {
            Container cont = getContentPane();
            cont.setLayout(new BorderLayout());
            okButton.addActionListener(this);
            getRootPane().setDefaultButton(okButton);
            cancelButton.addActionListener(this);
            widthField.setText("" + width);
            heightField.setText("" + height);
            JPanel center = new JPanel();
            JPanel buttonPanel = new JPanel();
            JLabel label = new JLabel("Ange banans dimensioner:");
            JLabel xLabel = new JLabel(" x ");
            center.add(label);
            center.add(widthField);
            center.add(xLabel);
            center.add(heightField);
            buttonPanel.add(okButton);
            buttonPanel.add(cancelButton);
            cont.add(center);
            cont.add(buttonPanel, BorderLayout.SOUTH);
            pack();
            setLocationRelativeTo(parent);
            setResizable(false);
            setTitle("Ny bana...");
            setVisible(true);
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == cancelButton) {
                setVisible(false);
                dispose();
            } else {
                try {
                    int w = Integer.parseInt(widthField.getText());
                    int h = Integer.parseInt(heightField.getText());
                    newLevel(w, h);
                    setVisible(false);
                    dispose();
                } catch (Exception err) {
                    JOptionPane.showMessageDialog(parent, "Felaktiga dimensioner angivna.", "Fel", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class MenuListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == exit) {
                if (Main.launchedStandalone) Common.exit(); else {
                    parent.setVisible(false);
                    parent.dispose();
                }
            } else if (e.getSource() == pickLayer) {
                selectLayer = pickLayer.getState();
            } else if (e.getSource() == snap) {
                snapToGrid = snap.getState();
                repaint();
            } else if (e.getSource() == show) {
                showGrid = show.getState();
                repaint();
            } else if (e.getSource() == size) {
                queryGridSize();
            } else if (e.getSource() == setBackground) {
                setBackground();
            } else if (e.getSource() == removeBackground) {
                clearBackground();
            } else if (e.getSource() == testRun) {
                testRun();
            } else if (e.getSource() == showMap) {
                showMap();
            } else if (e.getSource() == properties) {
                queryProperties();
            } else if (e.getSource() == save) {
                save(currentFile);
            } else if (e.getSource() == saveAs) {
                saveAs();
            } else if (e.getSource() == open) {
                open();
            } else if (e.getSource() == opennew) {
                opennew();
            } else if (e.getSource() == props) {
                if (selectedSprites.size() == 1) showPropertyFrame((SpriteData) selectedSprites.getFirst()); else if (selectedSprites.size() > 1) JOptionPane.showMessageDialog(parent, "F�r m�nga objekt markerade.", "Fel", JOptionPane.ERROR_MESSAGE); else JOptionPane.showMessageDialog(parent, "Inget objekt markerat.", "Fel", JOptionPane.ERROR_MESSAGE);
            } else if (e.getSource() == delete) {
                try {
                    deleteSelectedSprites();
                } catch (Exception err) {
                    err.printStackTrace();
                }
            }
        }
    }

    public LevelEditorPane(Editor parent, LayeredPictureChooser lpc, int width, int height) {
        this.parent = parent;
        this.lpc = lpc;
        this.width = width;
        this.height = height;
        parts[0] = 100;
        parts[1] = 0;
        parts[2] = 0;
        toDrawRectangle = new Rectangle();
        selectionRect = new Rectangle();
        selection = false;
        fileChooser = new JFileChooser(System.getProperty("user.dir"));
        fileFilter = new SimpleFileFilter();
        fileFilter.addExtension("lev");
        fileFilter.setDescription("Banfiler (*.lev)");
        fileChooser.addChoosableFileFilter(fileFilter);
        positionMaps = new PositionMap[LAYER_MASKS.length];
        for (int i = 0; i < positionMaps.length; i++) positionMaps[i] = new PositionMap(width, height);
        try {
            MGSv1Parser parser = new MGSv1Parser(ResourceConstants.GIRLPICSFILE, true);
            player = new SpriteData(ImageArchive.getImage(parser.getFrame("standingRight0"), false), new SpriteType("", SpriteType.PLAYER), LevelToolbox.PLAYERLAYER, 20, 200);
            positionMaps[LevelToolbox.PLAYERLAYER].addSprite(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
        showGrid = true;
        snapToGrid = true;
        selectLayer = true;
        gridSize = DEFAULT_GRID_SIZE;
        scaleFactor = 1.0;
        visibleLayers = LAYER_MASKS[LevelToolbox.BACKGROUND] + LAYER_MASKS[LevelToolbox.BACKLAYER] + LAYER_MASKS[LevelToolbox.PLAYERLAYER] + LAYER_MASKS[LevelToolbox.FRONTLAYER];
        currentLayer = LAYER_MASKS[LevelToolbox.PLAYERLAYER];
        prevSelected = -1;
        addMouseListener(this);
        addMouseMotionListener(this);
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTKeyListener(), AWTEvent.KEY_EVENT_MASK);
        parent.setTitle("Banverktyg [osparad fil]");
        setUpMenu();
        setSize(width, height);
        setVisible(true);
    }

    public int getVisibleLayers() {
        return visibleLayers;
    }

    public void setVisibleLayers(int visibleLayers) {
        this.visibleLayers = visibleLayers;
    }

    public boolean shouldSelectLayer() {
        return selectLayer;
    }

    public Editor getEditor() {
        return parent;
    }

    public int getGridSize() {
        return gridSize;
    }

    public PositionMap[] getSprites() {
        return positionMaps;
    }

    public void zoom(double scaleFactor) {
        this.scaleFactor = scaleFactor;
        width = (int) (width * scaleFactor);
        height = (int) (height * scaleFactor);
        repaint();
    }

    public void zoomIn() {
        scaleFactor = scaleFactor * 1.5;
        width = (int) (width * scaleFactor);
        height = (int) (height * scaleFactor);
        repaint();
    }

    public void zoomOut() {
        scaleFactor = scaleFactor / 1.5;
        width = (int) (width * scaleFactor);
        height = (int) (height * scaleFactor);
        repaint();
    }

    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public static int getLayer(int layerNumber) {
        return LAYER_MASKS[layerNumber];
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (g instanceof Graphics2D) paintLevel((Graphics2D) g); else throw new RuntimeException("g not instancof Graphics2D");
    }

    private void paintLevel(Graphics2D g) {
        Rectangle vr = getVisibleRect();
        if (backgroundImage == null) {
            g.setColor(Color.WHITE);
            g.fillRect(vr.x, vr.y, vr.width, vr.height);
        } else {
            g.setPaint(backgroundPaint);
            g.fill(vr);
        }
        if ((lpc.getSelectedLayers() & LAYER_MASKS[LevelToolbox.BACKGROUND]) != 0) paintSprites(g, positionMaps[0].getSpriteIterator(vr.x, vr.y, vr.width, vr.height));
        if (showGrid) paintGrid(g);
        for (int i = 1; i < positionMaps.length; i++) if ((lpc.getSelectedLayers() & LAYER_MASKS[i]) != 0) paintSprites(g, positionMaps[i].getSpriteIterator(vr.x, vr.y, vr.width, vr.height));
        if (lpc.isSelected()) {
            g.setColor(Color.blue);
            g.drawRect(toDrawRectangle.x, toDrawRectangle.y, toDrawRectangle.width, toDrawRectangle.height);
        }
        if (selection) {
            g.setColor(new Color(255, 100, 100));
            g.drawRect(selectionRect.x, selectionRect.y, selectionRect.width, selectionRect.height);
        }
    }

    private void paintSprites(Graphics g, Iterator spriteIterator) {
        while (spriteIterator.hasNext()) {
            Sprite e = (Sprite) spriteIterator.next();
            if (e instanceof SpriteData) {
                ((SpriteData) e).show(g);
            } else {
                e.show(g, 0, 0);
            }
        }
    }

    private void paintGrid(Graphics2D g) {
        g.setColor(GRID_COLOR);
        for (int i = 0; i <= width; i += gridSize) g.drawLine(i, 0, i, height);
        for (int i = 0; i <= height; i += gridSize) g.drawLine(0, i, width, i);
    }

    /**
     * Selects a sprite at the location given by x and y.
     */
    private void selectSprite(int x, int y) {
        Sprite sprite;
        visibleLayers = lpc.getSelectedLayers();
        for (int i = positionMaps.length - 1; i >= 0; i--) {
            if ((visibleLayers & LAYER_MASKS[i]) != 0) {
                if ((sprite = positionMaps[i].getSprite(x, y)) != null) {
                    if (sprite instanceof SpriteData) {
                        selectedSprites.add(sprite);
                        ((SpriteData) sprite).selected = true;
                        repaint();
                        return;
                    }
                }
            }
        }
    }

    private void selectSprites(int x, int y, int width, int height) {
        clearSelectedSprites();
        visibleLayers = lpc.getSelectedLayers();
        Rectangle testRect = new Rectangle(x, y, width, height);
        for (int i = 0; i < positionMaps.length; i++) {
            if ((visibleLayers & LAYER_MASKS[i]) != 0) {
                Iterator it = positionMaps[i].getFastIterator(x, y, width, height);
                for (Sprite s = (Sprite) it.next(); s != null; s = (Sprite) it.next()) {
                    if (testRect.contains(s.x, s.y, s.width, s.height)) {
                        if (s instanceof SpriteData) {
                            ((SpriteData) s).selected = true;
                            selectedSprites.add(s);
                        }
                    }
                }
            }
        }
        repaint();
    }

    private void clearSelectedSprites() {
        while (!selectedSprites.isEmpty()) {
            SpriteData s = (SpriteData) selectedSprites.removeFirst();
            s.selected = false;
        }
        repaint();
    }

    private void deleteSelectedSprites() throws Exception {
        boolean playerFound = false;
        while (!selectedSprites.isEmpty()) {
            SpriteData s = (SpriteData) selectedSprites.removeFirst();
            if (s != player) positionMaps[s.layer].removeSprite(s); else playerFound = true;
        }
        if (playerFound) selectedSprites.add(player);
        repaint();
    }

    private void showPropertyFrame(SpriteData s) {
        new PropertyDialog(this, s, "Egenskaper f�r " + SpriteType.getName(s.type));
    }

    public void saveLevel(File file) {
        EditorLevelToolbox l = new EditorLevelToolbox(this);
        l.save(file);
        parent.setTitle("Banverktyg [" + file.getName() + "]");
    }

    public void loadLevel(File file) {
        EditorLevelToolbox l = new EditorLevelToolbox(this);
        try {
            l.load(file.toURL());
            player = l.getPlayerForEditor();
            positionMaps = l.getSprites();
            time = l.getTimeLimit();
            tune = l.getGameTune();
            parts = l.getParts();
            level = l.getDegreeOfDifficulty();
            keyLimit = l.getKeyLimit();
            forceLimit = l.getForceLimit();
            forceInterval = l.getForceInterval();
            backgroundImagePath = l.getBackgroundImagePath();
            backgroundImage = l.getBackgroundImage();
            if (backgroundImage != null) {
                backgroundAnchor.setRect(0, 0, backgroundImage.getWidth(null), backgroundImage.getHeight(null));
                backgroundPaint = new TexturePaint(backgroundImage, backgroundAnchor);
            }
            setSubjects(l.getSubjects(), l.getSubjWeights());
            selectedSprites = new LinkedList<Sprite>();
            changeSize(l.getWidth(), l.getHeight());
            parent.setTitle("Banverktyg [" + file.getName() + "]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getTimeLimit() {
        return time;
    }

    public String getGameTune() {
        return tune;
    }

    public int[] getParts() {
        return parts;
    }

    public int getDegreeOfDifficulty() {
        return level;
    }

    public String getSubjects() {
        StringBuffer result = new StringBuffer();
        String[] subs = databaseSubjectsSorted;
        for (int i = 0; i < subs.length && i < subjSelected.length; i++) {
            if (subjSelected[i] > 0) result.append(subs[i] + " ");
        }
        return result.toString();
    }

    public String getSubjWeights() {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < subjSelected.length; i++) {
            if (subjSelected[i] > 0) result.append(subjSelected[i] + " ");
        }
        return result.toString();
    }

    public int getKeyLimit() {
        return keyLimit;
    }

    public void setSubjects(String[] subjects, int[] weights) {
        String[] subs = databaseSubjectsSorted;
        for (int i = 0; i < subs.length; i++) {
            subjSelected[i] = 0;
            for (int j = 0; j < subjects.length && j < weights.length; j++) {
                if (subjects[j].equalsIgnoreCase(subs[i])) {
                    subjSelected[i] = weights[j];
                    break;
                }
            }
        }
    }

    private void changeSize(int width, int height) {
        this.width = width;
        this.height = height;
        setSize(width, height);
        parent.changedSize();
        repaint();
    }

    private int snapToGrid(int coord) {
        return (coord / gridSize) * gridSize;
    }

    public Iterator getSpriteIterator() {
        return new AllSpritesIterator();
    }

    public void mouseClicked(MouseEvent e) {
        if (buttonOne) {
            clearSelectedSprites();
            selectSprite(e.getX(), e.getY());
            if (selectedSprites.size() == 1) {
                Sprite s = (Sprite) selectedSprites.getFirst();
                selectionRect.x = s.x;
                selectionRect.y = s.y;
                selectionRect.width = s.width;
                selectionRect.height = s.height;
            }
        }
    }

    private void addSprite(Sprite toAdd) throws Exception {
        positionMaps[LevelToolbox.getLayerNumber(lpc.getCurrentLayer())].addSprite(toAdd);
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
        toDrawRectangle = new Rectangle(-1, -1, 0, 0);
        repaint();
    }

    public void mousePressed(MouseEvent e) {
        selection = false;
        addSprites = false;
        if (e.getButton() > 1) {
            buttonOne = false;
            if (lpc.isSelected()) {
                lpc.deselect();
                repaint();
            }
        } else {
            buttonOne = true;
        }
        if (lpc.isSelected()) {
            if ((e.getModifiers() & InputEvent.CTRL_MASK) == 0) {
                try {
                    SpriteData toAdd = lpc.getSpriteType().createCompatibleSpriteData(lpc.getImage(), LevelToolbox.getLayerNumber(lpc.getCurrentLayer()), toDrawRectangle.x, toDrawRectangle.y);
                    addSprite((Sprite) toAdd);
                    repaint();
                } catch (Exception err) {
                    err.printStackTrace();
                }
            } else {
                selectionRect.x = e.getX();
                selectionRect.y = e.getY();
                prevX = selectionRect.x;
                prevY = selectionRect.y;
                selectionRect.width = 0;
                selectionRect.height = 0;
                selection = true;
                addSprites = true;
            }
        } else {
            if (selectedSprites.isEmpty() || !selectionRect.contains(new Point(e.getX(), e.getY()))) {
                selectionRect.x = e.getX();
                selectionRect.y = e.getY();
                prevX = selectionRect.x;
                prevY = selectionRect.y;
                selectionRect.width = 0;
                selectionRect.height = 0;
                selection = true;
            } else {
                int minx = Integer.MAX_VALUE;
                int miny = Integer.MAX_VALUE;
                int maxx = Integer.MIN_VALUE;
                int maxy = Integer.MIN_VALUE;
                Iterator it = selectedSprites.listIterator();
                while (it.hasNext()) {
                    Sprite s = (Sprite) it.next();
                    if (s.x < minx) minx = s.x;
                    if (s.y < miny) miny = s.y;
                    if (s.x + s.width > maxx) maxx = s.x + s.width;
                    if (s.y + s.height > maxy) maxy = s.y + s.height;
                }
                selectionRect.x = minx;
                selectionRect.y = miny;
                selectionRect.width = maxx - minx;
                selectionRect.height = maxy - miny;
                if (snapToGrid) {
                    selectionRect.x = snapToGrid(selectionRect.x);
                    selectionRect.y = snapToGrid(selectionRect.y);
                    selectionRect.width = snapToGrid(selectionRect.width);
                    selectionRect.height = snapToGrid(selectionRect.height);
                }
                diffX = e.getX() - selectionRect.x;
                diffY = e.getY() - selectionRect.y;
                dragged = false;
                if (e.getButton() > 1) {
                    rightClickMenu.show(this, e.getX(), e.getY());
                }
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (addSprites) {
            int x1 = selectionRect.x;
            int y1 = selectionRect.y;
            int x2 = selectionRect.x + selectionRect.width;
            int y2 = selectionRect.y + selectionRect.height;
            if (snapToGrid) {
                if (x1 % gridSize != 0) x1 += gridSize;
                if (y1 % gridSize != 0) y1 += gridSize;
                x1 = snapToGrid(x1);
                y1 = snapToGrid(y1);
                x2 = snapToGrid(x2);
                y2 = snapToGrid(y2);
            }
            try {
                int w = lpc.getImage().getWidth(null);
                int h = lpc.getImage().getHeight(null);
                for (int i = x1; i <= x2 - w; i += w) {
                    for (int j = y1; j <= y2 - h; j += h) {
                        SpriteData toAdd = lpc.getSpriteType().createCompatibleSpriteData(lpc.getImage(), LevelToolbox.getLayerNumber(lpc.getCurrentLayer()), i, j);
                        try {
                            addSprite((Sprite) toAdd);
                        } catch (Exception err) {
                        }
                    }
                }
            } catch (Exception exc) {
            }
            repaint();
        } else if (selection && !selectionRect.isEmpty()) {
            selectSprites(selectionRect.x, selectionRect.y, selectionRect.width, selectionRect.height);
        }
        addSprites = false;
        selection = false;
        diffX = 0;
        diffY = 0;
    }

    public void mouseDragged(MouseEvent e) {
        if (!selection && !selectedSprites.isEmpty() && buttonOne && !lpc.isSelected()) {
            int x = e.getX() - diffX;
            int y = e.getY() - diffY;
            if (x < 0) x = 0;
            if (x + selectionRect.width > width) x = width - selectionRect.width;
            if (y < 0) y = 0;
            if (y + selectionRect.height > height) y = height - selectionRect.height;
            if (snapToGrid) {
                x = snapToGrid(x);
                y = snapToGrid(y);
            }
            int dx = selectionRect.x - x;
            int dy = selectionRect.y - y;
            Iterator it = selectedSprites.listIterator();
            while (it.hasNext()) {
                SpriteData s = (SpriteData) it.next();
                try {
                    positionMaps[s.layer].removeSprite(s);
                    int oldx = s.x;
                    int oldy = s.y;
                    s.x -= dx;
                    s.y -= dy;
                    if (snapToGrid) {
                        s.x = snapToGrid(s.x);
                        s.y = snapToGrid(s.y);
                    }
                    s.hasMoved(oldx, oldy);
                    positionMaps[s.layer].addSprite(s);
                } catch (Exception err) {
                }
            }
            selectionRect.x = x;
            selectionRect.y = y;
            dragged = true;
            repaint();
        } else if (selection) {
            if (e.getX() > selectionRect.x) {
                if (selectionRect.x + selectionRect.width <= e.getX()) selectionRect.width = e.getX() - selectionRect.x; else if (prevX < e.getX()) {
                    selectionRect.width -= e.getX() - selectionRect.x;
                    selectionRect.x = e.getX();
                } else selectionRect.width += e.getX() - (selectionRect.x + selectionRect.width);
            } else {
                selectionRect.width += selectionRect.x - e.getX();
                selectionRect.x = e.getX();
            }
            if (e.getY() > selectionRect.y) {
                if (selectionRect.y + selectionRect.height <= e.getY()) selectionRect.height = e.getY() - selectionRect.y; else if (prevY < e.getY()) {
                    selectionRect.height -= e.getY() - selectionRect.y;
                    selectionRect.y = e.getY();
                } else selectionRect.height += e.getY() - (selectionRect.y + selectionRect.height);
            } else {
                selectionRect.height += selectionRect.y - e.getY();
                selectionRect.y = e.getY();
            }
            prevX = e.getX();
            prevY = e.getY();
            repaint();
        }
    }

    public void mouseMoved(MouseEvent e) {
        if (lpc.isSelected()) {
            int x = e.getX();
            int y = e.getY();
            if (snapToGrid) {
                x = (x / gridSize) * gridSize;
                y = (y / gridSize) * gridSize;
            }
            toDrawRectangle.x = x;
            toDrawRectangle.y = y;
            toDrawRectangle.width = lpc.getSelectedSize().width;
            toDrawRectangle.height = lpc.getSelectedSize().height;
            repaint();
        }
    }

    private void setUpMenu() {
        MenuListener mList = new MenuListener();
        exit = new JMenuItem("Avsluta");
        exit.setMnemonic(KeyEvent.VK_A);
        exit.addActionListener(mList);
        save = new JMenuItem("Spara bana");
        save.setMnemonic(KeyEvent.VK_S);
        save.addActionListener(mList);
        saveAs = new JMenuItem("Spara bana som...");
        saveAs.addActionListener(mList);
        open = new JMenuItem("�ppna bana...");
        open.addActionListener(mList);
        opennew = new JMenuItem("Ny bana...");
        opennew.setMnemonic(KeyEvent.VK_N);
        opennew.addActionListener(mList);
        pickLayer = new JCheckBoxMenuItem("V�lj lager automatiskt");
        pickLayer.setState(selectLayer);
        pickLayer.setMnemonic(KeyEvent.VK_V);
        pickLayer.addActionListener(mList);
        snap = new JCheckBoxMenuItem("F�st vid rutn�t");
        snap.setState(snapToGrid);
        snap.setMnemonic(KeyEvent.VK_A);
        snap.addActionListener(mList);
        show = new JCheckBoxMenuItem("Visa rutn�t");
        show.setState(showGrid);
        show.setMnemonic(KeyEvent.VK_R);
        show.addActionListener(mList);
        size = new JMenuItem("Storlek p� rutn�t...");
        size.setMnemonic(KeyEvent.VK_S);
        size.addActionListener(mList);
        setBackground = new JMenuItem("Anv�nd som bakgrund");
        setBackground.setMnemonic(KeyEvent.VK_B);
        setBackground.addActionListener(mList);
        removeBackground = new JMenuItem("Ta bort bakgrund");
        removeBackground.setMnemonic(KeyEvent.VK_C);
        removeBackground.addActionListener(mList);
        testRun = new JMenuItem("Provk�r banan");
        testRun.setMnemonic(KeyEvent.VK_P);
        testRun.addActionListener(mList);
        showMap = new JMenuItem("Visa �versiktskarta");
        showMap.setMnemonic(KeyEvent.VK_K);
        showMap.addActionListener(mList);
        properties = new JMenuItem("Egenskaper...");
        properties.setMnemonic(KeyEvent.VK_E);
        properties.addActionListener(mList);
        JMenuBar menubar = new JMenuBar();
        JMenu file = new JMenu("Arkiv");
        file.setMnemonic(KeyEvent.VK_A);
        file.add(opennew);
        file.add(open);
        file.add(save);
        file.add(saveAs);
        file.addSeparator();
        file.add(exit);
        JMenu options = new JMenu("Alternativ");
        options.setMnemonic(KeyEvent.VK_T);
        options.add(pickLayer);
        options.add(show);
        options.add(snap);
        options.add(size);
        options.addSeparator();
        options.add(setBackground);
        options.add(removeBackground);
        options.add(testRun);
        options.add(showMap);
        options.add(properties);
        menubar.add(file);
        menubar.add(options);
        parent.setJMenuBar(menubar);
        rightClickMenu = new JPopupMenu();
        props = new JMenuItem("Egenskaper");
        props.addActionListener(mList);
        props.setMnemonic(KeyEvent.VK_E);
        delete = new JMenuItem("Ta bort");
        delete.addActionListener(mList);
        delete.setMnemonic(KeyEvent.VK_T);
        rightClickMenu.add(props);
        rightClickMenu.add(delete);
    }

    private void testRun() {
        String tempDir = System.getProperty("java.io.tmpdir");
        File tempFile = new File(tempDir, "temp.lev");
        EditorLevelToolbox l = new EditorLevelToolbox(this);
        l.save(tempFile);
        mathgame.game.MainWindow.LAUNCHED_FROM_EDITOR = true;
        new mathgame.game.MainWindow(null, tempFile.getAbsolutePath(), true);
    }

    private void showMap() {
        Dimension dim = new Dimension(positionMaps[0].getMapWidth() / 5, positionMaps[0].getMapHeight() / 5);
        String name = currentFile == null ? "osparad fil" : currentFile.getName();
        new KeyMapFrame(parent, "�versiktskarta [" + name + "]", positionMaps, dim);
    }

    private void save(File file) {
        if (file == null) {
            saveAs();
        } else {
            currentFile = file;
            saveLevel(file);
        }
    }

    private void saveAs() {
        if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (file.exists()) {
                if (JOptionPane.showConfirmDialog(parent, "Filen \"" + file.getAbsolutePath() + "\" finns redan.\n" + "Forts�tt?", "V�lj fil", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION) return;
            }
            System.out.println("CP");
            if (fileChooser.getFileFilter() == fileFilter) {
                try {
                    String path = file.getCanonicalPath();
                    System.out.println("path = " + path);
                    if (!path.endsWith(".lev")) path = path + ".lev";
                    file = new File(path);
                } catch (IOException ioe) {
                }
            }
            save(file);
        }
    }

    private void open() {
        if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            if (fileChooser.getSelectedFile().exists()) {
                loadLevel(fileChooser.getSelectedFile());
            } else {
                opennew();
            }
        }
    }

    private void opennew() {
        new NewLevelDialog((Component) parent);
    }

    private void queryGridSize() {
        String answer = JOptionPane.showInputDialog(parent, "V�lj storlek p� rutn�t.", "" + gridSize);
        try {
            if (answer != null) {
                gridSize = Integer.parseInt(answer);
                parent.changeScrollBarIncrementTo(gridSize, gridSize);
                repaint();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, "Felaktig storlek angiven.", "Fel", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setBackground() {
        if (!lpc.isSelected()) {
            JOptionPane.showMessageDialog(parent, "Ingen bild vald.", "Fel", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (positionMaps[LevelToolbox.BACKGROUND].iterator().hasNext()) {
            if (JOptionPane.showConfirmDialog(parent, "Varning. Alla bilder i bakgrundslagret\nkommer att skrivas �ver!\n\nForts�tt?", "Varning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) {
                return;
            }
        }
        try {
            backgroundImagePath = lpc.getSpriteType().imageFilename;
            backgroundImage = lpc.getImage();
            backgroundAnchor.setRect(0, 0, backgroundImage.getWidth(null), backgroundImage.getHeight(null));
            backgroundPaint = new TexturePaint(backgroundImage, backgroundAnchor);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, "Ov�ntat fel intr�ffade. Se konsolen f�r felutskrift.", "Fel", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        repaint();
    }

    private void clearBackground() {
        backgroundImagePath = "";
        backgroundImage = null;
        repaint();
    }

    private void queryProperties() {
        new LevelPropertiesDialog(this);
    }

    private void newLevel(int width, int height) {
        changeSize(width, height);
        positionMaps = new PositionMap[LAYER_MASKS.length];
        for (int i = 0; i < positionMaps.length; i++) positionMaps[i] = new PositionMap(width, height);
        try {
            MGSv1Parser parser = new MGSv1Parser(ResourceConstants.GIRLPICSFILE, true);
            player = new SpriteData(ImageArchive.getImage(parser.getFrame("standingRight0"), false), new SpriteType("", SpriteType.PLAYER), LevelToolbox.PLAYERLAYER, 20, 200);
            positionMaps[LevelToolbox.PLAYERLAYER].addSprite(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
        selectedSprites = new LinkedList<Sprite>();
        currentFile = null;
        parent.setTitle("Banverktyg [osparad fil]");
        repaint();
    }
}
