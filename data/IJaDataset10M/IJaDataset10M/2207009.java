package gui;

import com.l2fprod.common.swing.JTaskPane;
import com.l2fprod.common.swing.JTaskPaneGroup;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingManager;
import org.flexdock.view.View;
import org.flexdock.view.Viewport;

/**
 *
 * @author  Gouda
 */
public class ImageColorizer extends javax.swing.JFrame {

    View manager;

    /** Creates new form MoviesColorization */
    public ImageColorizer() {
        initComponents();
        setTitle("Image Colorization");
        progress = new ProgressBar(this, false);
        newproject = new NewProject(this, true);
        colorizationPanel = new ImageColoringPanel();
        JPanel bar = colorizationPanel.getEmptyPanel();
        Viewport viewport = new Viewport();
        View startPage = createStartPage();
        viewport.dock(startPage);
        add(viewport, BorderLayout.CENTER);
        View page = createPropertyView();
        page.setSize(200, getSize().height);
        page.setMinimumSize(new Dimension(200, getSize().height));
        page.setMaximumSize(new Dimension(200, getSize().height));
        manager = createManagerPanel();
        JTabbedPane tabbedPane = new JTabbedPane();
        viewport.dock((Dockable) manager, "WEST");
        viewport.dock((Dockable) page, "EAST");
        DockingManager.registerDockable((Dockable) startPage);
        DockingManager.registerDockable((Dockable) page);
        DockingManager.setSplitProportion((Dockable) startPage, 0.8f);
        DockingManager.setSplitProportion((Dockable) page, 0.9f);
        DockingManager.setSplitProportion((Dockable) manager, 0.2f);
        pack();
        repaint();
        DockingManager.setMinimized((Dockable) page, true);
        width = getSize().width;
        height = getSize().height;
        loadImage();
    }

    private JPanel createTasksPage() {
        JPanel panel = new JPanel();
        JTaskPane taskPaneContainer = new JTaskPane();
        JTaskPaneGroup actionPane = createActionPaneImage();
        taskPaneContainer.add(actionPane);
        actionPane = createActionPaneProject();
        taskPaneContainer.add(actionPane);
        actionPane = createActionPaneColorization();
        taskPaneContainer.add(actionPane);
        panel.setLayout(new BorderLayout());
        panel.add(taskPaneContainer, BorderLayout.CENTER);
        return panel;
    }

    private JTaskPaneGroup createActionPaneProject() {
        JTaskPaneGroup actionPane = new JTaskPaneGroup();
        actionPane.setTitle("Project");
        actionPane.setSpecial(true);
        Action action = new AbstractAction("Load Project") {

            public void actionPerformed(ActionEvent e) {
                loadProject();
            }
        };
        action.putValue(Action.SMALL_ICON, new ImageIcon("./Resources/icons/open-20x20.png"));
        action.putValue(Action.SHORT_DESCRIPTION, "Loads project to continue working on colorization");
        actionPane.add(action);
        action = new AbstractAction("Save Project") {

            public void actionPerformed(ActionEvent e) {
                saveProject();
            }
        };
        action.putValue(Action.SMALL_ICON, new ImageIcon("./Resources/icons/save-20x20.png"));
        action.putValue(Action.SHORT_DESCRIPTION, "Saves project to a file");
        actionPane.add(action);
        return actionPane;
    }

    private void saveProject() {
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showSaveDialog(this);
        String path = "c:\\";
        String fileName = "TestSave";
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            path = fc.getSelectedFile().getParent() + "\\";
            fileName = fc.getSelectedFile().getName();
        } else {
            return;
        }
        ImageManip.save(path + fileName + "-Map.jpg", colorizationPanel.getColorMap());
        ImageManip.save(path + fileName + "-Marked.jpg", colorizationPanel.getMarked());
        ImageManip.save(path + fileName + "-original.jpg", colorizationPanel.getOriginal());
        byte[] buffer = new byte[18024];
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(path + fileName + ".zip"));
            out.setLevel(Deflater.DEFAULT_COMPRESSION);
            FileInputStream in1 = new FileInputStream(path + fileName + "-Map.jpg");
            FileInputStream in2 = new FileInputStream(path + fileName + "-Marked.jpg");
            FileInputStream in3 = new FileInputStream(path + fileName + "-original.jpg");
            out.putNextEntry(new ZipEntry(fileName + "-Map.jpg"));
            int len;
            while ((len = in1.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            out.putNextEntry(new ZipEntry(fileName + "-Marked.jpg"));
            len = 0;
            while ((len = in2.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            out.putNextEntry(new ZipEntry(fileName + "-original.jpg"));
            len = 0;
            while ((len = in3.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            out.close();
            in1.close();
            in2.close();
            in3.close();
            File originalFile = new File(path + fileName + "-original.jpg");
            File mapFile = new File(path + fileName + "-Map.jpg");
            File markedFile = new File(path + fileName + "-Marked.jpg");
            originalFile.delete();
            mapFile.delete();
            markedFile.delete();
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void loadProject() {
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(this);
        String path = "c:\\";
        String fileName = "TestSave";
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            path = fc.getSelectedFile().getParent() + "\\";
            fileName = fc.getSelectedFile().getName();
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
        } else {
            return;
        }
        try {
            int BUFFER = 2048;
            String inFileName = path + fileName + ".zip";
            String destinationDirectory = path;
            File sourceZipFile = new File(inFileName);
            File unzipDestinationDirectory = new File(destinationDirectory);
            ZipFile zipFile = new ZipFile(sourceZipFile, ZipFile.OPEN_READ);
            Enumeration zipFileEntries = zipFile.entries();
            while (zipFileEntries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
                String currentEntry = entry.getName();
                File destFile = new File(unzipDestinationDirectory, currentEntry);
                File destinationParent = destFile.getParentFile();
                destinationParent.mkdirs();
                if (!entry.isDirectory()) {
                    BufferedInputStream is = new BufferedInputStream(zipFile.getInputStream(entry));
                    int currentByte;
                    byte data[] = new byte[BUFFER];
                    FileOutputStream fos = new FileOutputStream(destFile);
                    BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
                    while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, currentByte);
                    }
                    dest.flush();
                    dest.close();
                    is.close();
                }
            }
            zipFile.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        try {
            File originalFile = new File(path + fileName + "-original.jpg");
            File mapFile = new File(path + fileName + "-Map.jpg");
            File markedFile = new File(path + fileName + "-Marked.jpg");
            colorizationPanel.setColorMap(ImageIO.read(mapFile));
            workingFrame.setImage(ImageIO.read(originalFile));
            colorizationPanel.colorizeNextFrame(workingFrame);
            originalFile.delete();
            mapFile.delete();
            markedFile.delete();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private JTaskPaneGroup createActionPaneImage() {
        JTaskPaneGroup actionPane = new JTaskPaneGroup();
        actionPane.setTitle("Image");
        actionPane.setSpecial(true);
        Action action = new AbstractAction("Load Image") {

            public void actionPerformed(ActionEvent e) {
                loadMenuItemActionPerformed(e);
            }
        };
        action.putValue(Action.SMALL_ICON, new ImageIcon("./Resources/icons/image-file-20x20.png"));
        action.putValue(Action.SHORT_DESCRIPTION, "Loads image to colorize");
        actionPane.add(action);
        action = new AbstractAction("Save Image") {

            public void actionPerformed(ActionEvent e) {
                saveMenuItemActionPerformed(e);
            }
        };
        action.putValue(Action.SMALL_ICON, new ImageIcon("./Resources/icons/save-20x20.png"));
        action.putValue(Action.SHORT_DESCRIPTION, "Saves image to to image file");
        actionPane.add(action);
        return actionPane;
    }

    Action makeAction(String title, String tooltiptext, String iconPath) {
        Action action = new AbstractAction(title) {

            public void actionPerformed(ActionEvent e) {
            }
        };
        action.putValue(Action.SMALL_ICON, new ImageIcon(iconPath));
        action.putValue(Action.SHORT_DESCRIPTION, tooltiptext);
        return action;
    }

    private JTaskPaneGroup createActionPaneFiles() {
        JTaskPaneGroup actionPane = new JTaskPaneGroup();
        actionPane.setTitle("Project");
        actionPane.setSpecial(true);
        Action action = new AbstractAction("New Project") {

            public void actionPerformed(ActionEvent e) {
                newMovieMenuItemActionPerformed(e);
            }
        };
        action.putValue(Action.SMALL_ICON, new ImageIcon("./Resources/icons/new-20x20.png"));
        action.putValue(Action.SHORT_DESCRIPTION, "Creates new project to colorize a movie");
        actionPane.add(action);
        action = new AbstractAction("Load Project") {

            public void actionPerformed(ActionEvent e) {
                loadMenuItemActionPerformed(e);
            }
        };
        action.putValue(Action.SMALL_ICON, new ImageIcon("./Resources/icons/open-20x20.png"));
        action.putValue(Action.SHORT_DESCRIPTION, "Loads project to continue working on colorization");
        actionPane.add(action);
        action = new AbstractAction("Save Project") {

            public void actionPerformed(ActionEvent e) {
                saveMenuItemActionPerformed(e);
            }
        };
        action.putValue(Action.SMALL_ICON, new ImageIcon("./Resources/icons/save-20x20.png"));
        action.putValue(Action.SHORT_DESCRIPTION, "Saves project to a file only seperate mode is supported");
        actionPane.add(action);
        return actionPane;
    }

    private JTaskPaneGroup createActionPaneColorization() {
        JTaskPaneGroup actionPane = new JTaskPaneGroup();
        actionPane.setTitle("Colorization");
        actionPane.setSpecial(true);
        Action action = makeAction("Colorize", "Press to colorize the black and white pictures", "./Resources/icons/propertysheet20x20.png");
        actionPane.add(action);
        return actionPane;
    }

    private JTaskPaneGroup createActionPaneDetails() {
        return null;
    }

    private View createColorsPalette() {
        View view = new View("colors.palette", "Color Palette");
        view.addAction("pin");
        JPanel panel = colorizationPanel.getColorPalette();
        view.setContentPane(panel);
        return view;
    }

    private View createToolsPalette() {
        View view = new View("tools.palette", "Tools Palette");
        view.addAction("pin");
        JPanel panel = colorizationPanel.getToolsPalette();
        view.setContentPane(panel);
        return view;
    }

    private View createStartPage() {
        String id = "startPage";
        View view = new View(id, "Colorization", null);
        view.setTitlebar(null);
        view.setContentPane(colorizationPanel);
        return view;
    }

    private View createPropertyView() {
        View view = new View("Property.List", "Properties List");
        view.addAction("pin");
        JPanel p = new JPanel();
        PropertySheetPage page = new PropertySheetPage();
        p.setLayout(new BorderLayout());
        p.add(page, BorderLayout.CENTER);
        p.setBorder(new LineBorder(Color.GRAY, 1));
        JTextField t = new JTextField("Properties List");
        t.setPreferredSize(new Dimension(100, 20));
        p.add(t, BorderLayout.NORTH);
        view.setContentPane(p);
        return view;
    }

    private View createManagerPanel() {
        View view = new View("manager.list", "Manager");
        view.addAction("pin");
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Tasks", createTasksPage());
        tabbedPane.add("Color Palette", colorizationPanel.getColorPalette());
        tabbedPane.add("Tree", colorizationPanel.getToolsPalette());
        view.setContentPane(tabbedPane);
        return view;
    }

    private void initComponents() {
        buttonGroup1 = new javax.swing.ButtonGroup();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newMovieMenuItem = new javax.swing.JMenuItem();
        loadMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        exitMenuItem = new javax.swing.JMenuItem();
        runMenu = new javax.swing.JMenu();
        mixedMode = new javax.swing.JRadioButtonMenuItem();
        seperateMode = new javax.swing.JRadioButtonMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        startMenuItem = new javax.swing.JMenuItem();
        stopMenuItem = new javax.swing.JMenuItem();
        nextFrameMenuItem = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        helpContentsMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }

            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {

            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        fileMenu.setText("File");
        newMovieMenuItem.setText("New Movie");
        newMovieMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMovieMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(newMovieMenuItem);
        loadMenuItem.setText("Load Project");
        loadMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(loadMenuItem);
        saveMenuItem.setText("Save Project");
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveMenuItem);
        fileMenu.add(jSeparator1);
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);
        jMenuBar1.add(fileMenu);
        runMenu.setText("Run");
        buttonGroup1.add(mixedMode);
        mixedMode.setText("Mixed Mode");
        runMenu.add(mixedMode);
        buttonGroup1.add(seperateMode);
        seperateMode.setSelected(true);
        seperateMode.setText("Seperate Mode");
        runMenu.add(seperateMode);
        runMenu.add(jSeparator2);
        startMenuItem.setText("Start Correlation");
        startMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startMenuItemActionPerformed(evt);
            }
        });
        runMenu.add(startMenuItem);
        stopMenuItem.setText("Stop");
        stopMenuItem.setActionCommand("Stop Correlation");
        stopMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopMenuItemActionPerformed(evt);
            }
        });
        runMenu.add(stopMenuItem);
        nextFrameMenuItem.setText("Next Frame");
        nextFrameMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextFrameMenuItemActionPerformed(evt);
            }
        });
        runMenu.add(nextFrameMenuItem);
        jMenuBar1.add(runMenu);
        jMenu1.setText("Help");
        helpContentsMenuItem.setText("Help Contents");
        jMenu1.add(helpContentsMenuItem);
        aboutMenuItem.setText("About");
        jMenu1.add(aboutMenuItem);
        jMenuBar1.add(jMenu1);
        setJMenuBar(jMenuBar1);
        pack();
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        if (!isSaved) {
            Object[] options = { "Save", "Don't Save", "Cancel" };
            int n = JOptionPane.showOptionDialog(this, "Donot you want to save the changes ?", "Save Changes", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            System.out.println(n);
            if (n == 0) {
                saveMenuItemActionPerformed(null);
            } else {
                if (n == 1) {
                } else {
                    if (n == 2) {
                        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
                    }
                }
            }
        }
    }

    private void formComponentResized(java.awt.event.ComponentEvent evt) {
        width = getSize().width;
        height = getSize().height;
    }

    private void formWindowClosed(java.awt.event.WindowEvent evt) {
        System.exit(0);
    }

    private void loadMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            OpenFileDialog ofd = new OpenFileDialog(this, true);
            ofd.setVisible(true);
            String imagePath = ofd.getSelectedFilePath();
            if (imagePath == null) return;
            BufferedImage loaded = ImageIO.read(new File(imagePath));
            workingFrame.setImage(loaded);
            colorizationPanel.setImageToColorize(workingFrame);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        ImageFrame coloredFrame = (ImageFrame) colorizationPanel.getColoredFrame();
        if (coloredFrame == null) {
            JOptionPane.showMessageDialog(this, "No Colored Image To Save");
            return;
        }
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showSaveDialog(this);
        String path = "c:\\";
        String fileName = "TestSave";
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            ImageManip.save(fc.getSelectedFile().getAbsolutePath(), coloredFrame.getColoredImage());
        } else {
            return;
        }
    }

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }

    private void nextFrameMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void menuItemAction(ActionEvent e) {
    }

    private void stopMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void startMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void newMovieMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    }

    public static String imageURL = null;

    public void loadImage() {
        if (imageURL == null) return;
        try {
            String imagePath = imageURL;
            BufferedImage loaded = ImageIO.read(new File(imagePath));
            workingFrame.setImage(loaded);
            colorizationPanel.setImageToColorize(workingFrame);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        final SplashScreen screen = new SplashScreen(1000);
        screen.showSplash();
        if (args.length > 1) ImageColorizer.imageURL = args[1];
        try {
            Thread.sleep(100);
        } catch (Exception x) {
        }
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                ImageColorizer frame = new ImageColorizer();
                frame.setExtendedState(frame.MAXIMIZED_BOTH);
                frame.setVisible(true);
                screen.close();
            }
        });
        System.out.println("Test");
    }

    private NewProject newproject;

    private int width;

    private int height;

    private ProgressBar progress = null;

    private boolean isSaved = true;

    private Timer correlateTimer = null;

    private Thread controllerThread = null;

    private ImageColoringPanel colorizationPanel = null;

    ImageFrame workingFrame = new ImageFrame();

    private javax.swing.JMenuItem aboutMenuItem;

    private javax.swing.ButtonGroup buttonGroup1;

    private javax.swing.JMenuItem exitMenuItem;

    private javax.swing.JMenu fileMenu;

    private javax.swing.JMenuItem helpContentsMenuItem;

    private javax.swing.JMenu jMenu1;

    private javax.swing.JMenuBar jMenuBar1;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JSeparator jSeparator2;

    private javax.swing.JMenuItem loadMenuItem;

    private javax.swing.JRadioButtonMenuItem mixedMode;

    private javax.swing.JMenuItem newMovieMenuItem;

    private javax.swing.JMenuItem nextFrameMenuItem;

    private javax.swing.JMenu runMenu;

    private javax.swing.JMenuItem saveMenuItem;

    private javax.swing.JRadioButtonMenuItem seperateMode;

    private javax.swing.JMenuItem startMenuItem;

    private javax.swing.JMenuItem stopMenuItem;
}
