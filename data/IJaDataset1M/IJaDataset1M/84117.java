package flowchart.app;

import flowchart.app.Diagram.Msg;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.Printable;
import java.util.*;
import javax.swing.*;
import flowchart.nodes.*;
import flowchart.editors.*;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;

/**
 * A class representing the combined view and controller
 * of the diagram application.
 */
public class DiagramView extends JPanel implements Observer, Printable {

    protected Diagram diagram;

    private JMenuBar mymenu;

    private JPopupMenu popup_normal;

    private JPopupMenu popup_object;

    private JToolBar mytb;

    private JTextField text;

    private GraphicsObjectEditor selectededitor = null;

    private HitTestInfo hit = null;

    private File file;

    private boolean hasSelectedObjects;

    private boolean snaptogrid;

    private boolean isInEditMode;

    private boolean singleAddMode;

    private boolean saved;

    /**
     * Constructor for view
     * @param t handle for textfield
     * @param defpic picture
     * @param m handle for menubar
     * @param tb handle for toolbar
     */
    public DiagramView(JTextField t, Picture defpic, JMenuBar m, JToolBar tb) {
        mymenu = m;
        mytb = tb;
        text = t;
        file = null;
        hasSelectedObjects = false;
        snaptogrid = false;
        isInEditMode = false;
        singleAddMode = false;
        saved = true;
        Controller c = new Controller();
        this.addMouseListener(c);
        this.addMouseMotionListener(c);
        diagram = new Diagram();
        diagram.addObserver(this);
        diagram.addObserver(c);
        diagram.setPicture(defpic);
    }

    /**
     * Is file saved
     * @return true if saved
     */
    public boolean saved() {
        return saved;
    }

    /**
     * The Observer function
     * Repaint view if model is changed, write messages from model
     * @param o
     * @param arg
     */
    public void update(Observable o, Object arg) {
        Diagram.Msg msg = (Diagram.Msg) arg;
        System.out.println("view: diagram says:" + msg.getMessage());
        repaint();
    }

    /**
     * Paint view
     * @param graphics
     */
    public void paint(Graphics graphics) {
        super.paint(graphics);
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        diagram.getPicture().draw(g);
        if (selectededitor != null) {
            selectededitor.drawHandles((Graphics2D) g, Color.WHITE, Color.BLUE);
        }
    }

    /**
     * Show error dialog
     * @param errorMsg error message
     */
    private void showErrorDialog(String errorMsg) {
        JOptionPane.showMessageDialog(null, errorMsg, "Virhe", JOptionPane.ERROR_MESSAGE);
    }

    /**
     *  Print Picture
     */
    public void print() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);
        boolean ok = job.printDialog();
        if (ok) {
            try {
                job.print();
            } catch (PrinterException ex) {
                showErrorDialog("Print error");
                System.out.println(ex.getMessage());
            }
        }
    }

    /**
     *  Saves file
     *
     * @return boolean true if file saveed succsfully
     */
    public boolean saveFile() {
        if (file == null) {
            if (saveFileAs()) {
                return true;
            } else {
                return false;
            }
        } else {
            diagram.SaveToFile(file);
            return true;
        }
    }

    /**
     *  Save File as dialog
     * @return boolean if file picked succesfully
     */
    public boolean saveFileAs() {
        JFileChooser fc = new JFileChooser("Save..");
        fc.addChoosableFileFilter(new ExtFilter());
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int returnVal = fc.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            diagram.SaveToFile(f);
            return true;
        } else {
            System.out.println("Save command cancelled by user.");
            return false;
        }
    }

    /**
     * Save confirmation dialog
     * @return integer yes=0 discard=1 cancel=2
     */
    public int showUnsavedConfirm() {
        Object[] options = { "Save ", "Discard", "Cancel" };
        int n = JOptionPane.showOptionDialog(this, "Save your changes?", "Save", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
        return n;
    }

    /**
     * Openfile dialog
     * Allows user to pic file witch will be opened by diagram
     */
    public void openFile() {
        JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter(new ExtFilter());
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            diagram.loadFromFile(f);
        }
    }

    /**
     *  Print method for the Jpanel to print picture
     *  "draws" picture for printer object.
     * @param graphics
     * @param pageFormat
     * @param pageIndex
     * @return
     * @throws java.awt.print.PrinterException
     */
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return (NO_SUCH_PAGE);
        } else {
            Graphics2D g = (Graphics2D) graphics;
            g.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            diagram.getPicture().draw(g);
            return (PAGE_EXISTS);
        }
    }

    /**
     * an inner class representing the controller
     */
    private class Controller implements MouseListener, MouseMotionListener, KeyListener, Observer {

        private static final int NODE = 0;

        private static final int CONNECTOR = 1;

        private static final int SELECT = 2;

        private static final int CNODE = 3;

        private int curx, cury;

        private int mode = SELECT;

        private int idcnt = 1;

        private ArrayList buttons;

        private ArrayList staticbuttons;

        /** Returns an ImageIcon, or null if the path was invalid. */
        protected ImageIcon createImageIcon(String resourcefile, String description) {
            String path = "/flowchart/resources/" + resourcefile;
            java.net.URL imgURL = getClass().getResource(path);
            if (imgURL != null) {
                return new ImageIcon(imgURL, description);
            } else {
                System.err.println("Couldn't find file: " + path);
                return null;
            }
        }

        Controller() {
            text.setText(null);
            text.setVisible(true);
            text.setBorder(null);
            text.addKeyListener(this);
            buttons = new java.util.ArrayList();
            staticbuttons = new java.util.ArrayList();
            AbstractButton ab = null;
            AbstractButton pop = null;
            JMenu file = new JMenu("File");
            JMenuItem newdg = new JMenuItem(new SingleAction("New"));
            ab = newdg;
            buttons.add(ab);
            newdg.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
            ab.setEnabled(false);
            file.add(newdg);
            JMenuItem open = new JMenuItem(new SingleAction("Open.."));
            open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
            ab = open;
            buttons.add(ab);
            file.add(open);
            JMenuItem save = new JMenuItem(new SingleAction("Save"));
            save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
            ab = save;
            buttons.add(ab);
            file.add(save);
            JMenuItem saveas = new JMenuItem(new SingleAction("Save as.."));
            ab = saveas;
            buttons.add(ab);
            file.add(saveas);
            JMenuItem print = new JMenuItem(new SingleAction("Print.."));
            print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK));
            ab = print;
            buttons.add(ab);
            file.add(print);
            JMenuItem exit = new JMenuItem(new SingleAction("Exit"));
            exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_DOWN_MASK));
            ab = exit;
            buttons.add(ab);
            file.add(exit);
            mymenu.add(file);
            ActionMap am = getActionMap();
            ModeAction m = new ModeAction(NODE, "rectangle");
            m.putValue("type", "rectangle");
            am.put("rectangle", m);
            m = new ModeAction(CNODE, "circle");
            m.putValue("type", "circle");
            am.put("circle", m);
            m = new ModeAction(CONNECTOR, "connect");
            m.putValue("type", "connect");
            am.put("connector", m);
            m = new ModeAction(SELECT, "select");
            m.putValue("type", "move");
            am.put("select", m);
            am.put("delete", new SingleAction("delete"));
            am.put("edit title", new SingleAction("edit title"));
            am.put("snap to grid", new SingleAction("snap to grid"));
            am.put("New", new SingleAction("New"));
            am.put("Open..", new SingleAction("Open.."));
            am.put("Save", new SingleAction("Save"));
            am.put("Save as..", new SingleAction("Save as.."));
            am.put("Print..", new SingleAction("Print.."));
            am.put("Exit", new SingleAction("Exit"));
            am.put("single add mode", new SingleAction("single add mode"));
            JMenu add = new JMenu("Add");
            popup_normal = new JPopupMenu();
            popup_object = new JPopupMenu();
            pop = new JCheckBoxMenuItem(am.get("select"));
            pop.setSelected(true);
            popup_normal.add(pop);
            buttons.add(pop);
            popup_normal.addSeparator();
            ab = new JCheckBoxMenuItem(am.get("rectangle"));
            add.add(ab);
            pop = new JCheckBoxMenuItem(am.get("rectangle"));
            popup_normal.add(pop);
            buttons.add(ab);
            buttons.add(pop);
            ab = new JCheckBoxMenuItem(am.get("circle"));
            add.add(ab);
            pop = new JCheckBoxMenuItem(am.get("circle"));
            popup_normal.add(pop);
            buttons.add(ab);
            buttons.add(pop);
            ab = new JCheckBoxMenuItem(am.get("connector"));
            add.add(ab);
            pop = new JCheckBoxMenuItem(am.get("connector"));
            popup_normal.add(pop);
            buttons.add(ab);
            buttons.add(pop);
            mymenu.add(add);
            JMenu edit = new JMenu("Edit");
            ab = new JMenuItem(am.get("edit title"));
            edit.add(ab);
            pop = new JMenuItem(am.get("edit title"));
            popup_object.add(pop);
            staticbuttons.add(ab);
            staticbuttons.add(pop);
            InputMap im = ab.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            JMenuItem del;
            del = new JMenuItem(am.get("delete"));
            del.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "delete");
            del.setActionMap(am);
            ab = del;
            edit.add(ab);
            pop = new JMenuItem(am.get("delete"));
            popup_object.add(pop);
            staticbuttons.add(ab);
            staticbuttons.add(pop);
            mymenu.add(edit);
            JMenu options = new JMenu("Options");
            ab = new JCheckBoxMenuItem(am.get("snap to grid"));
            options.add(ab);
            staticbuttons.add(ab);
            ab = new JCheckBoxMenuItem(am.get("single add mode"));
            ab.setSelected(false);
            options.add(ab);
            staticbuttons.add(ab);
            mymenu.add(options);
            JToggleButton t;
            t = new JToggleButton(am.get("select"));
            ImageIcon icon = createImageIcon("nuoli.gif", "nuoli");
            t.setIcon(icon);
            t.setText("");
            im = t.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "select");
            t.setActionMap(am);
            t.setToolTipText("select or move nodes and connectors");
            t.setSelected(true);
            ab = t;
            mytb.add(ab);
            buttons.add(ab);
            t = new JToggleButton(am.get("rectangle"));
            icon = createImageIcon("nelio.gif", "nelio");
            t.setIcon(icon);
            t.setText(" ");
            im = t.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, 0), "rectangle");
            t.setActionMap(am);
            t.setToolTipText("add normal nodes");
            ab = t;
            buttons.add(ab);
            mytb.add(ab);
            t = new JToggleButton(am.get("circle"));
            icon = createImageIcon("pallo.gif", "pallo");
            t.setIcon(icon);
            t.setText("  ");
            im = t.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_B, 0), "circle");
            t.setActionMap(am);
            t.setToolTipText("add circle nodes");
            ab = t;
            buttons.add(ab);
            mytb.add(ab);
            t = new JToggleButton(am.get("connector"));
            icon = createImageIcon("konnektori.gif", "konnektori");
            t.setIcon(icon);
            t.setText("||");
            im = t.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0), "connector");
            t.setActionMap(am);
            t.setToolTipText("draw connector between nodes");
            ab = t;
            mytb.add(ab);
            buttons.add(ab);
            Dimension d = new Dimension(20, mytb.getHeight());
            mytb.addSeparator(d);
            JButton b = new JButton(am.get("edit title"));
            icon = createImageIcon("teksti.gif", "teksti");
            b.setIcon(icon);
            b.setEnabled(false);
            im = b.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0), "edit title");
            b.setActionMap(am);
            b.setToolTipText("edit title text of node or connector");
            ab = b;
            staticbuttons.add(ab);
            mytb.add(ab);
            b = new JButton(am.get("delete"));
            icon = createImageIcon("roskis.gif", "delete");
            b.setIcon(icon);
            b.setEnabled(false);
            im = b.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "delete");
            b.setActionMap(am);
            b.setToolTipText("delete selected node");
            ab = b;
            staticbuttons.add(ab);
            mytb.add(ab);
        }

        /**
         * Observes changes in model
         * @param o model
         * @param arg status messages from model
         */
        public void update(Observable o, Object arg) {
            Diagram.Msg msg = (Diagram.Msg) arg;
            System.out.println("Controller: diagram says:" + msg.getMessage());
            if (msg.equals(Msg.PICTURE_SAVED) || msg.equals(Msg.PICTURE_INITIALIZED) || msg.equals(Msg.PICTURE_LOADED)) {
                saved = true;
            } else {
                saved = false;
            }
            if (msg.equals(Msg.PICTURE_INITIALIZED)) {
                file = null;
                selectededitor = null;
                hit = null;
                updateButtons("move");
            }
            if (msg.equals(Msg.OBJECT_DELETED)) {
                updateButtons("move");
                selectededitor = null;
                hit = null;
            }
            if (msg.equals(Msg.PICTURE_SAVED) || msg.equals(Msg.PICTURE_LOADED)) {
                file = (java.io.File) msg.get("file");
                updateButtons("move");
            }
            if (msg.equals(Msg.OBJECT_SELECTED)) {
                selectededitor = (GraphicsObjectEditor) msg.get("editor");
                hit = (HitTestInfo) msg.get("hit");
                repaint();
            }
            if (msg.equals(Msg.OBJECT_DESELECTED)) {
                selectededitor = null;
                hit = null;
                repaint();
            }
            if (msg.equals(Msg.FILE_ERROR)) {
                showErrorDialog(Msg.FILE_ERROR);
            }
            if (singleAddMode) {
                if (msg.equals(Msg.CONNECTOR_CREATED) || msg.equals(Msg.NODE_CREATED) || msg.equals(Msg.OBJECT_TITLE_CHANGED) || msg.equals(Msg.CIRCLE_CREATED)) {
                    mode = SELECT;
                    updateButtons("move");
                    updateStaticButtons();
                }
            }
            if (selectededitor != null) {
                hasSelectedObjects = true;
            } else {
                hasSelectedObjects = false;
            }
            updateStaticButtons();
        }

        /**
         * Helper method for obtaining editor for currently selected object
         */
        private GraphicsObjectEditor getObjectEditor(int x, int y) {
            hit = null;
            GraphicsObjectEditor se = null;
            Iterator<GraphicsObjectEditor> i = diagram.editors.iterator();
            while (i.hasNext()) {
                GraphicsObjectEditor editor = i.next();
                hit = editor.hitTest(x, y);
                if (hit.code != HitTestInfo.NOHIT) {
                    se = editor;
                    break;
                }
            }
            if (se == null) {
                repaint();
            }
            return se;
        }

        /**
         *  Edit objects title
         */
        private void editTitle() {
            updateStaticButtons();
            isInEditMode = true;
            setButtonsForEdit();
            AbstractGraphicsObject obj = (AbstractGraphicsObject) selectededitor.getGraphicsObject();
            int x = (int) obj.getBounds().getX();
            int y = (int) obj.getBounds().getY();
            int width = (int) obj.getBounds().getWidth();
            int height = (int) obj.getBounds().getHeight();
            if (obj instanceof GraphicsConnector) {
                text.setBounds(x + (width / 2) - 50, y + (height / 2) + 25, 100, 20);
            } else {
                text.setBounds(x + 1, y + 48, width - 2, height - 2);
            }
            text.setVisible(true);
            text.setText(obj.getTitle());
            text.requestFocus();
            text.selectAll();
        }

        /**
         *  Delete selected object
         */
        public void delete() {
            try {
                diagram.removeObject(selectededitor.getGraphicsObject());
            } catch (Exception ex) {
            }
        }

        /**
         *  Select/deselect objects
         *  Get connector draw start position
         *  Create nodes
         *  Stop title edit
         * @param e mousepressed event
         */
        public void mousePressed(MouseEvent e) {
            if (isInEditMode) {
                if (hasSelectedObjects) {
                    AbstractGraphicsObject obj = (AbstractGraphicsObject) selectededitor.getGraphicsObject();
                    diagram.setTitle(obj, text.getText());
                    text.setVisible(false);
                    text.setText(null);
                    isInEditMode = false;
                }
            }
            setButtonsForEdit();
            if (mode == NODE) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    diagram.createNode("Untitled" + idcnt++, e.getX(), e.getY());
                }
            } else if (mode == CNODE) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    diagram.createCircle("Untitled" + idcnt++, e.getX(), e.getY());
                }
            } else if (mode == SELECT) {
                diagram.SelectObject(e.getX(), e.getY());
            } else if (mode == CONNECTOR) {
                selectededitor = null;
                diagram.SelectObject(e.getX(), e.getY());
                if (hasSelectedObjects) {
                    Rectangle bo = selectededitor.getGraphicsObject().getBounds();
                    curx = bo.x + bo.width / 2;
                    cury = bo.y + bo.height / 2;
                }
            }
        }

        /**
         *  Draw or erase temporary connector
         */
        private void drawTempConn() {
            Graphics g = getGraphics();
            g.setXORMode(getBackground());
            g.setColor(Color.black);
            if (hasSelectedObjects) {
                Rectangle bo = selectededitor.getGraphicsObject().getBounds();
                g.drawLine(bo.x + bo.width / 2, bo.y + bo.height / 2, curx, cury);
            }
            g.dispose();
        }

        /**
         * Handle mouse drag event for resizing/moving nodes
         * and drawing temp connector
         * @param e mouse drag event
         */
        public void mouseDragged(MouseEvent e) {
            if (mode != CONNECTOR) {
                if (hit != null && hit.code != HitTestInfo.NOHIT && hasSelectedObjects) {
                    selectededitor.moveHandle(hit, e.getX(), e.getY());
                    repaint();
                }
            }
            if (mode == CONNECTOR) {
                drawTempConn();
                curx = e.getX();
                cury = e.getY();
                drawTempConn();
            }
        }

        /**
         * Handle mouse release event for opening contextmenus
         * and drawing connector
         * @param e Mouse event
         */
        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) {
                if (hasSelectedObjects) {
                    popup_object.show(e.getComponent(), e.getX(), e.getY());
                } else {
                    popup_normal.show(e.getComponent(), e.getX(), e.getY());
                }
            } else if (mode == CONNECTOR) {
                drawTempConn();
                if (hasSelectedObjects) {
                    GraphicsObject cstart = selectededitor.getGraphicsObject();
                    GraphicsObjectEditor go = this.getObjectEditor(e.getX(), e.getY());
                    if (go != null) {
                        GraphicsObject cend = go.getGraphicsObject();
                        if (cend != null) {
                            if (!cstart.equals(cend)) {
                                diagram.createConnector("Conn " + idcnt++, cstart, cend);
                                diagram.SelectObject(e.getX(), e.getY());
                            }
                        }
                    } else {
                        diagram.SelectObject(e.getX(), e.getY());
                    }
                }
            }
        }

        /**
         * Handle mouse doubleclick for editing titles
         * @param e mouse event
         */
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                if (mode == SELECT) {
                    hit = null;
                    selectededitor = null;
                    selectededitor = this.getObjectEditor(e.getX(), e.getY());
                    if (hasSelectedObjects) {
                        editTitle();
                    }
                }
            }
            updateStaticButtons();
        }

        /**
         *  Handle keyreleased event for title edit textfield
         * @param e key event
         */
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                updateStaticButtons();
                diagram.setTitle(selectededitor.getGraphicsObject(), text.getText());
                text.setVisible(false);
                text.setText(null);
                isInEditMode = false;
            }
            updateStaticButtons();
        }

        /**
         * Disables buttons when in editmode
         */
        public void setButtonsForEdit() {
            Iterator i = buttons.iterator();
            while (i.hasNext()) {
                AbstractButton abu = (AbstractButton) i.next();
                abu.setEnabled(!isInEditMode);
            }
            i = staticbuttons.iterator();
            while (i.hasNext()) {
                AbstractButton abu = (AbstractButton) i.next();
                abu.setEnabled(!isInEditMode);
            }
        }

        /**
         *  Set node type1,type2, connector or move button selected
         * @param cmd button to set enabled
         */
        public void updateButtons(String cmd) {
            Iterator i = buttons.iterator();
            while (i.hasNext()) {
                AbstractButton abu = (AbstractButton) i.next();
                System.out.println("action asd ac " + abu.getAction().getValue("type"));
                if (abu.getAction().getValue("type") != null) {
                    if (abu.getAction().getValue("type").equals(cmd)) {
                        abu.setSelected(true);
                    } else {
                        abu.setSelected(false);
                    }
                }
                if (!cmd.equals("connector")) {
                    selectededitor = null;
                }
            }
        }

        /**
         * enable/disable contextmenu buttons
         * set snap to grid and single add mode menuitems state
         */
        public void updateStaticButtons() {
            Iterator i = staticbuttons.iterator();
            while (i.hasNext()) {
                AbstractButton abu = (AbstractButton) i.next();
                abu.getAction().getValue(Action.ACCELERATOR_KEY);
                if (abu.getActionCommand().equals("edit title") || abu.getActionCommand().equals("delete")) {
                    abu.setEnabled(hasSelectedObjects);
                } else if (abu.getActionCommand().equals("snap to grid")) {
                    abu.setSelected(snaptogrid);
                } else if (abu.getActionCommand().equals("single add mode")) {
                    abu.setSelected(singleAddMode);
                }
            }
        }

        /**
         * Mouse listener not in use
         * @param e
         */
        public void mouseEntered(MouseEvent e) {
        }

        /**
         * Mouse listener not in use
         * @param e
         */
        public void mouseExited(MouseEvent e) {
        }

        /**
         * Mouse listener not in use
         * @param e
         */
        public void mouseMoved(MouseEvent e) {
        }

        /**
         * Key listener not in use
         * @param e
         */
        public void keyTyped(KeyEvent e) {
        }

        /**
         * Key listener not in use
         * @param e
         */
        public void keyPressed(KeyEvent e) {
        }

        /**
         * an inner class for changing the mode
         */
        private class ModeAction extends AbstractAction {

            private int m;

            /**
             *  Constructor for mode action
             * @param m mode
             * @param txt name
             */
            ModeAction(int m, String txt) {
                super(txt, null);
                this.m = m;
            }

            /**
             * Updates mode and sets correct button enabled
             * @param e actionevent
             */
            public void actionPerformed(ActionEvent e) {
                mode = this.m;
                updateStaticButtons();
                AbstractButton ab = (AbstractButton) e.getSource();
                updateButtons(ab.getAction().getValue("type").toString());
            }
        }

        /**
         * an inner class for non mode changing actions
         */
        private class SingleAction extends AbstractAction {

            /**
             * Constructor
             * @param txt name
             */
            SingleAction(String txt) {
                super(txt, null);
            }

            /**
             * Handles events raised by menuitems witch dont change mode
             * @param e action event
             */
            public void actionPerformed(ActionEvent e) {
                updateStaticButtons();
                AbstractButton ab = (AbstractButton) e.getSource();
                if (ab.getActionCommand().equals("delete")) {
                    delete();
                } else if (e.getActionCommand().equalsIgnoreCase("exit")) {
                    if (!saved) {
                        int result = showUnsavedConfirm();
                        if (result == 0) {
                            if (saveFile()) {
                                System.exit(0);
                            }
                        } else if (result == 1) {
                            System.exit(0);
                        }
                    } else {
                        System.exit(0);
                    }
                } else if (e.getActionCommand().equalsIgnoreCase("print..")) {
                    print();
                } else if (e.getActionCommand().equalsIgnoreCase("new")) {
                    if (!saved) {
                        int result = showUnsavedConfirm();
                        if (result == 0) {
                            if (saveFile()) {
                                diagram.resetPicture();
                            }
                        } else if (result == 1) {
                            diagram.resetPicture();
                        }
                    } else {
                        diagram.resetPicture();
                    }
                } else if (e.getActionCommand().equalsIgnoreCase("Open..")) {
                    if (!saved) {
                        int result = showUnsavedConfirm();
                        if (result == 0) {
                            if (saveFile()) {
                                openFile();
                            }
                        } else if (result == 1) {
                            openFile();
                        }
                    } else {
                        openFile();
                    }
                } else if (e.getActionCommand().equalsIgnoreCase("save")) {
                    saveFile();
                } else if (e.getActionCommand().equalsIgnoreCase("Save as..")) {
                    saveFileAs();
                } else if (e.getActionCommand().equalsIgnoreCase("edit title")) {
                    editTitle();
                } else if (e.getActionCommand().equalsIgnoreCase("single add mode")) {
                    if (singleAddMode) {
                        singleAddMode = false;
                    } else {
                        singleAddMode = true;
                    }
                } else if (ab.getActionCommand().equalsIgnoreCase("snap to grid")) {
                    if (snaptogrid) {
                        snaptogrid = false;
                        diagram.setGrid(0);
                    } else {
                        diagram.setGrid(15);
                        snaptogrid = true;
                    }
                }
                updateStaticButtons();
            }
        }
    }
}
