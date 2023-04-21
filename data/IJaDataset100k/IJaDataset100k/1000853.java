package net.sourceforge.kas.cViewer.java;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;
import net.sourceforge.jeuclid.MathMLSerializer;
import net.sourceforge.kas.cMathML.MathMLAdapter;
import net.sourceforge.kas.cViewer.selector.java.JMathSelectorDialog;

public class MathFrame extends JFrame {

    public void setScrollPane1(JScrollPane scrollPane1) {
        this.scrollPane1 = scrollPane1;
    }

    private static final int DEFAULT_HEIGHT = 250;

    private static final int DEFAULT_WIDTH = 1000;

    private static final long serialVersionUID = 20090301L;

    private File lastPath;

    private JPanel jContentPane;

    private JMenuBar jMenuBar;

    private JMenu fileMenu;

    private JMenu helpMenu;

    private JMenuItem openMenuItem;

    private JMenuItem exportMenuItem;

    private JMenuItem aboutMenuItem;

    private JMenuItem howToMenuItem;

    private JMenuItem treeViewItem;

    private JMenuItem selDialogItem;

    private JMenuItem optionItem;

    private JSplitPane splitPaneVM;

    private JSplitPane splitPaneCV;

    private JScrollPane scrollPane1;

    private JScrollPane scrollPane;

    private MyOptionsDialog optionsDialog;

    private JMathSelectorDialog selDialog;

    private TransferObject stateTransfer;

    private JMathComponent mathComponent;

    private JMathViewer viewComponent;

    private JTextField textField;

    private CountLabel countLabel;

    private Counter counter;

    /**
     * This is the default constructor.
     */
    public MathFrame() {
        this.setTitle(Messages.getString("CViewer.windowTitle"));
        this.setSize(MathFrame.DEFAULT_WIDTH, MathFrame.DEFAULT_HEIGHT);
        this.setJMenuBar(this.getJMenuBar());
        this.setContentPane(this.getJContentPane());
        this.addWindowFocusListener(new WindowAdapter() {

            @Override
            public void windowGainedFocus(final WindowEvent e) {
                MathFrame.this.getMathComponent().requestFocusInWindow();
            }
        });
    }

    @Override
    public JMenuBar getJMenuBar() {
        if (this.jMenuBar == null) {
            this.jMenuBar = new JMenuBar();
            this.jMenuBar.add(this.getFileMenu());
            this.jMenuBar.add(this.getHelpMenu());
        }
        return this.jMenuBar;
    }

    private JMenu getHelpMenu() {
        if (this.helpMenu == null) {
            this.helpMenu = new JMenu();
            this.helpMenu.setText(Messages.getString("CViewer.helpMenu"));
            this.helpMenu.add(this.getHowToItem());
            this.helpMenu.add(this.getTreeViewItem());
            this.helpMenu.add(this.getOptionMenuItem());
            this.helpMenu.add(this.getSelectorMenuItem());
            this.helpMenu.add(this.getAboutMenuItem());
        }
        return this.helpMenu;
    }

    private JMenuItem getSelectorMenuItem() {
        if (this.selDialogItem == null) {
            this.selDialogItem = new JMenuItem();
            this.selDialogItem.setText(Messages.getString("CViewer.SelTree"));
            this.selDialogItem.addActionListener(new ActionListener() {

                public void actionPerformed(final ActionEvent e) {
                    MathFrame.this.displaySelView();
                }
            });
        }
        return this.selDialogItem;
    }

    private void displaySelView() {
        selDialog = ViewerFactory.getInst().getSelViewDialog();
        selDialog.update();
        selDialog.setParent(this);
        selDialog.pack();
        final Point loc = this.getLocation();
        loc.translate(this.getWidth() / 2, this.getHeight() / 2);
        selDialog.setLocation(loc);
        selDialog.setVisible(true);
    }

    private JMenuItem getTreeViewItem() {
        if (this.treeViewItem == null) {
            this.treeViewItem = new JMenuItem();
            this.treeViewItem.setText(Messages.getString("CViewer.helpTree"));
            this.treeViewItem.addActionListener(new ActionListener() {

                public void actionPerformed(final ActionEvent e) {
                    MathFrame.this.displayTreeView();
                }
            });
        }
        return this.treeViewItem;
    }

    private void displayTreeView() {
        final JFrame aDialog = ViewerFactory.getInst().getTreeViewDialog();
        aDialog.pack();
        final Point loc = this.getLocation();
        loc.translate(this.getWidth(), 0);
        aDialog.setLocation(loc);
        aDialog.setVisible(true);
    }

    private JMenuItem getHowToItem() {
        if (this.howToMenuItem == null) {
            this.howToMenuItem = new JMenuItem();
            this.howToMenuItem.setText(Messages.getString("CViewer.helpKeys"));
            this.howToMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(final ActionEvent e) {
                    ViewerFactory.getInst().getHowToDialog();
                }
            });
        }
        return this.howToMenuItem;
    }

    private JMenuItem getAboutMenuItem() {
        if (this.aboutMenuItem == null) {
            this.aboutMenuItem = new JMenuItem();
            this.aboutMenuItem.setText(Messages.getString("CViewer.helpAbout"));
            this.aboutMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(final ActionEvent e) {
                    ViewerFactory.getInst().getAboutDialog();
                }
            });
        }
        return this.aboutMenuItem;
    }

    private JMenuItem getOptionMenuItem() {
        if (this.optionItem == null) {
            this.optionItem = new JMenuItem();
            this.optionItem.setText(Messages.getString("CViewer.helpOptions"));
            this.optionItem.addActionListener(new ActionListener() {

                public void actionPerformed(final ActionEvent e) {
                    final Dialog dialog = MathFrame.this.getOptionsDialog();
                    dialog.setLocationRelativeTo(MathFrame.this);
                    dialog.setVisible(true);
                }
            });
        }
        return this.optionItem;
    }

    public MyOptionsDialog getOptionsDialog() {
        if (this.optionsDialog == null) {
            this.optionsDialog = new MyOptionsDialog(this.getStateTransfer());
            this.optionsDialog.pack();
        }
        return this.optionsDialog;
    }

    public TransferObject getStateTransfer() {
        if (this.stateTransfer == null) {
            final String[] strings = new String[4];
            strings[0] = Messages.getString("Options.c1");
            strings[1] = Messages.getString("Options.c2");
            strings[2] = Messages.getString("Options.c3");
            strings[3] = Messages.getString("Options.c4");
            this.stateTransfer = new TransferObject(strings);
            this.stateTransfer.setResult("--23");
        }
        return this.stateTransfer;
    }

    private JMenu getFileMenu() {
        if (this.fileMenu == null) {
            this.fileMenu = new JMenu();
            this.fileMenu.setText(Messages.getString("CViewer.fileMenu"));
            this.fileMenu.add(this.getOpenMenuItem());
            this.fileMenu.add(this.getExportMenuItem());
        }
        return this.fileMenu;
    }

    private JMenuItem getOpenMenuItem() {
        if (this.openMenuItem == null) {
            this.openMenuItem = new JMenuItem();
            this.openMenuItem.setText(Messages.getString("CViewer.fileOpen"));
            this.openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), true));
            this.openMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(final ActionEvent e) {
                    MathFrame.this.openFile();
                }
            });
        }
        return this.openMenuItem;
    }

    private void openFile() {
        final File file = this.selectFileToOpen(this);
        this.setTitle("KAS " + file.getName());
        this.getCounter().reset();
        BufferedReader r;
        String result = "";
        String line;
        try {
            r = new BufferedReader(new FileReader(file));
            while ((line = r.readLine()) != null) {
                result = result + line;
            }
            r.close();
        } catch (final IOException e) {
            System.out.println("Fehler beim Lesen der Datei");
        }
        result = MathMLAdapter.adapt(result);
        System.out.println("MathFrame-openFile");
        System.out.println(result);
        this.getMathComponent().setNewContent(result);
        this.getViewComponent().setContent(result);
        ViewerFactory.getInst().getTreeViewDialog().update();
        this.getMathComponent().requestFocusInWindow();
    }

    private File selectFileToOpen(final Frame parent) {
        File selectedFile;
        final JFileChooser fChooser = new JFileChooser(this.lastPath);
        fChooser.setFileFilter(new FileFilter() {

            private final String[] okFileExtensions = new String[] { "txt", "mml" };

            @Override
            public boolean accept(File file) {
                for (String extension : okFileExtensions) {
                    if (file.getName().toLowerCase().endsWith(extension) || file.isDirectory()) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public String getDescription() {
                return "MathML/TXT Dateien";
            }
        });
        if (fChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            selectedFile = fChooser.getSelectedFile();
        } else {
            selectedFile = null;
        }
        if (selectedFile != null) {
            this.lastPath = selectedFile.getParentFile();
        }
        return selectedFile;
    }

    private JMenuItem getExportMenuItem() {
        if (this.exportMenuItem == null) {
            this.exportMenuItem = new JMenuItem();
            this.exportMenuItem.setText(Messages.getString("CViewer.export"));
            this.exportMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), true));
            this.exportMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(final ActionEvent e) {
                    MathFrame.this.exportFile();
                }
            });
        }
        return this.exportMenuItem;
    }

    private void exportFile() {
        final JFileChooser fChooser = new JFileChooser(this.lastPath);
        if (fChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            final File selectedFile = fChooser.getSelectedFile();
            if (selectedFile != null) {
                final String fileName = selectedFile.getAbsolutePath();
                JMathElementHandler.removeCalcTyp(this.getMathComponent().getDocument());
                final String s = MathMLSerializer.serializeDocument(this.getMathComponent().getDocument(), false, false);
                try {
                    final BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
                    bw.write(s);
                    bw.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
        JMathElementHandler.parseDom(this.getMathComponent().getDocument());
    }

    public JPanel getJContentPane() {
        if (this.jContentPane == null) {
            this.jContentPane = new JPanel();
            this.jContentPane.setLayout(new BorderLayout());
            this.jContentPane.add(this.getButtonPanel(), BorderLayout.WEST);
            this.jContentPane.add(this.getSplitPaneVM(), BorderLayout.CENTER);
        }
        return this.jContentPane;
    }

    private JSplitPane getSplitPaneVM() {
        if (this.splitPaneVM == null) {
            this.splitPaneVM = new JSplitPane(JSplitPane.VERTICAL_SPLIT, this.getSplitPaneCountView(), this.getScrollPane());
            this.splitPaneVM.setOneTouchExpandable(true);
            this.splitPaneVM.setResizeWeight(0.1);
        }
        return this.splitPaneVM;
    }

    private JScrollPane getScrollPane() {
        if (this.scrollPane == null) {
            this.scrollPane = new JScrollPane();
            this.scrollPane.setViewportView(this.getMathComponent());
        }
        return this.scrollPane;
    }

    protected JScrollPane getScrollPane1() {
        if (this.scrollPane1 == null) {
            this.scrollPane1 = new JScrollPane();
            this.scrollPane1.setViewportView(this.getViewComponent());
        }
        return this.scrollPane1;
    }

    public JMathViewer getViewComponent() {
        if (this.viewComponent == null) {
            this.viewComponent = new JMathViewer();
        }
        return this.viewComponent;
    }

    public JSplitPane getSplitPaneCountView() {
        if (this.splitPaneCV == null) {
            this.splitPaneCV = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.getCountLabel(), this.getScrollPane1());
            this.splitPaneCV.setOneTouchExpandable(true);
            this.splitPaneCV.setResizeWeight(0);
        }
        return this.splitPaneCV;
    }

    public JLabel getCountLabel() {
        if (this.countLabel == null) {
            this.countLabel = new CountLabel();
            final Font curFont = this.countLabel.getFont();
            this.countLabel.setFont(new Font(curFont.getFontName(), curFont.getStyle(), 20));
            this.countLabel.setPreferredSize(new Dimension(40, 20));
            this.countLabel.setHorizontalAlignment(SwingConstants.CENTER);
            this.countLabel.setText("" + this.getCounter().getCount());
            this.getCounter().addObserver(this.countLabel);
        }
        return this.countLabel;
    }

    public class CountLabel extends JLabel implements Observer {

        public static final long serialVersionUID = 20090429;

        public void update(final Observable obs, final Object o) {
            this.setText("" + ((Counter) obs).getCount());
        }
    }

    public Counter getCounter() {
        if (this.counter == null) {
            this.counter = new Counter();
        }
        return this.counter;
    }

    public JMathComponent getMathComponent() {
        if (this.mathComponent == null) {
            this.mathComponent = ViewerFactory.getInst().getMathComponent();
            this.mathComponent.initialize();
            this.mathComponent.addKeyListener(new JMathKeyListener(this.mathComponent));
            final JMathMouseListener jMML = new JMathMouseListener(this.mathComponent, this.getTextField());
            this.mathComponent.addMouseListener(jMML);
            this.mathComponent.addMouseMotionListener(jMML);
        }
        return this.mathComponent;
    }

    public JPanel getButtonPanel() {
        final JPanel result = new JPanel();
        result.setLayout(new GridLayout(9, 2));
        result.add(new JButton(this.getMathComponent().getActionByName(Messages.getString("CViewer.buttonOut"))));
        result.add(new JButton(this.getMathComponent().getActionByName(Messages.getString("CViewer.buttonIn"))));
        result.add(new JButton(this.getMathComponent().getActionByName(Messages.getString("CViewer.buttonPrev"))));
        result.add(new JButton(this.getMathComponent().getActionByName(Messages.getString("CViewer.buttonNext"))));
        result.add(new JButton(this.getMathComponent().getActionByName(Messages.getString("CViewer.buttonPlus"))));
        result.add(new JButton(this.getMathComponent().getActionByName(Messages.getString("CViewer.buttonMinus"))));
        result.add(new JButton(this.getMathComponent().getActionByName(Messages.getString("CViewer.buttonLeft"))));
        result.add(new JButton(this.getMathComponent().getActionByName(Messages.getString("CViewer.buttonRight"))));
        result.add(new JButton(this.getMathComponent().getActionByName(Messages.getString("CViewer.buttonFence"))));
        result.add(new JButton(this.getMathComponent().getActionByName(Messages.getString("CViewer.buttonDefence"))));
        result.add(new JButton(this.getMathComponent().getActionByName(Messages.getString("CViewer.buttonChooser"))));
        result.add(new JButton(this.getMathComponent().getActionByName(Messages.getString("CViewer.buttonMine"))));
        result.add(new JButton(this.getMathComponent().getActionByName(Messages.getString("CViewer.buttonExtract"))));
        result.add(new JButton(this.getMathComponent().getActionByName(Messages.getString("CViewer.buttonCombine"))));
        result.add(this.getTextField());
        result.add(new JButton(this.getMathComponent().getActionByName(Messages.getString("CViewer.buttonSplit"))));
        result.add(new JButton(this.getMathComponent().getActionByName(Messages.getString("CViewer.buttonUndo"))));
        result.add(new JButton(this.getMathComponent().getActionByName(Messages.getString("CViewer.buttonRedo"))));
        return result;
    }

    public JTextField getTextField() {
        if (this.textField == null) {
            this.textField = new JTextField(6);
            final Font f = new Font("DialogInput", 1, 16);
            this.textField.setFont(f);
        }
        return this.textField;
    }
}
