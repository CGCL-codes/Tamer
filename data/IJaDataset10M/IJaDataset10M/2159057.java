package editor.modelEditor;

import graphics.GLCamera;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.swing.*;
import com.sun.opengl.util.Animator;
import utilities.Location;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ModelEditor extends Frame implements Runnable, FocusListener {

    Frame owner;

    GLCamera c;

    GLCanvas canvas;

    ModelDisplay md;

    KeyActionListener ka;

    Animator animator;

    Model m;

    public ModelEditor() {
        super("Model Editor");
        owner = this;
        m = new Model();
        c = new GLCamera(new Location(0, 10, 0), new Location(0, 0, -5), 0, 0);
        md = new ModelDisplay(c, m);
        canvas = new GLCanvas(new GLCapabilities());
        canvas.addGLEventListener(md);
        canvas.addFocusListener(this);
        setSize(920, 600);
        setBackground(Color.LIGHT_GRAY);
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        canvas.setPreferredSize(new Dimension(700, 550));
        add(canvas, c);
        c.gridx = 1;
        add(new BuilderPanel(m), c);
        setLocationRelativeTo(null);
        animator = new Animator(canvas);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                new ExitDialog(owner, animator);
            }
        });
        ka = new KeyActionListener();
        addKeyListener(ka);
        setMenuBar(new ModelEditorMenuBar(this, m, animator, this.c));
        animator.start();
        new Thread(this).start();
        setResizable(false);
        setVisible(true);
    }

    public void run() {
        c.centerView(new Location(0, 0, 0));
        for (; ; ) {
            ka.updateCamera(c);
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
            }
        }
    }

    public static void main(String[] args) {
        new ModelEditor();
    }

    public void focusGained(FocusEvent arg0) {
        requestFocus();
    }

    public void focusLost(FocusEvent arg0) {
    }
}

/**
 * the dialog for saving models
 * @author Jack
 *
 */
final class SaveDialog extends JDialog {

    Model m;

    public SaveDialog(Frame owner, Model model) {
        super(owner, "Save...", true);
        m = model;
        setSize(500, 350);
        setLocationRelativeTo(owner);
        File dir = new File(System.getProperty("user.dir"));
        JFileChooser fc = new JFileChooser(dir);
        int save = fc.showSaveDialog(this);
        if (save == JFileChooser.APPROVE_OPTION && fc.getSelectedFile() != null) {
            try {
                File f = fc.getSelectedFile();
                FileOutputStream fos = new FileOutputStream(f);
                DataOutputStream dos = new DataOutputStream(fos);
                m.writeModel(dos);
            } catch (IOException e) {
            }
        }
    }
}

/**
 * the dialog for loading models
 * @author Jack
 *
 */
final class LoadDialog extends JDialog {

    Model m;

    public LoadDialog(Frame owner, Model model) {
        super(owner, "Load...", true);
        m = model;
        setSize(500, 350);
        setLocationRelativeTo(owner);
        File dir = new File(System.getProperty("user.dir"));
        JFileChooser fc = new JFileChooser(dir);
        int save = fc.showOpenDialog(this);
        if (save == JFileChooser.APPROVE_OPTION && fc.getSelectedFile() != null) {
            try {
                File f = fc.getSelectedFile();
                FileInputStream fos = new FileInputStream(f);
                DataInputStream dos = new DataInputStream(fos);
                m.readModel(dos);
            } catch (IOException e) {
            }
        }
    }
}

final class CenterViewDialog extends JDialog {

    JDialog dialog;

    JTextField x = new JTextField(15);

    JTextField y = new JTextField(15);

    JTextField z = new JTextField(15);

    GLCamera c;

    public CenterViewDialog(Frame owner, GLCamera camera) {
        super(owner, "Center View", true);
        c = camera;
        dialog = this;
        setSize(220, 180);
        setLocationRelativeTo(owner);
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(createPanel("X:", x));
        p.add(createPanel("Y:", y));
        p.add(createPanel("Z:", z));
        JPanel selection = new JPanel();
        selection.setLayout(new FlowLayout());
        JButton accept = new JButton("Accept");
        JButton cancel = new JButton("Cancel");
        selection.add(accept);
        selection.add(cancel);
        p.add(selection);
        add(p);
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        accept.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    double xpos = Double.parseDouble(x.getText());
                    double ypos = Double.parseDouble(y.getText());
                    double zpos = Double.parseDouble(z.getText());
                    c.centerView(new Location(xpos, ypos, zpos));
                    dispose();
                } catch (NumberFormatException a) {
                    new ArgumentProblemDialog(dialog, "Entered numbers are not doubles");
                }
            }
        });
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }

    private JPanel createPanel(String label, Component c) {
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
        p.add(new JLabel(label));
        p.add(c);
        return p;
    }
}

/**
 * created when there are problems with the arguments that a user has put in
 * @author Jack
 *
 */
final class ArgumentProblemDialog extends JDialog {

    public ArgumentProblemDialog(JDialog owner, String problem) {
        super(owner, "Argument Problem", true);
        setSize(300, 100);
        setLocationRelativeTo(owner);
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(new JLabel("There are problems with the enertered arguments:"));
        p.add(new JLabel(problem));
        JButton accept = new JButton("Accept");
        p.add(accept);
        accept.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        add(p);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }
}

final class ExitDialog extends JDialog {

    Animator a;

    public ExitDialog(Frame owner, Animator animator) {
        super(owner, "Exit Editor", true);
        a = animator;
        setSize(300, 100);
        setLocationRelativeTo(owner);
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        JButton exit = new JButton("Exit");
        exit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {

                    public void run() {
                        a.stop();
                        System.exit(0);
                    }
                }).start();
            }
        });
        setLayout(new FlowLayout());
        add(new JLabel("Are you sure you want to exit?"));
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
        p.add(exit);
        p.add(cancel);
        add(p);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }
}

final class GridSettingsDialog extends JDialog {

    JDialog dialog;

    JComboBox drawGrid;

    JTextField width = new JTextField("" + ModelEditorConstants.gridWidth, 15);

    JTextField depth = new JTextField("" + ModelEditorConstants.gridDepth, 15);

    JTextField spacing = new JTextField("" + ModelEditorConstants.gridSpacing, 15);

    public GridSettingsDialog(Frame owner) {
        super(owner, "Grid Settings", true);
        dialog = this;
        setSize(300, 260);
        setLocationRelativeTo(owner);
        String[] choices = { "True", "False" };
        drawGrid = new JComboBox(choices);
        JButton accept = new JButton("Accept");
        JButton cancel = new JButton("Cancel");
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(createPanel("Draw Grid", drawGrid));
        p.add(createPanel("Grid Width", width));
        p.add(createPanel("Grid Depth", depth));
        p.add(createPanel("Grid Spacing", spacing));
        JPanel allPanel = new JPanel();
        allPanel.setLayout(new BoxLayout(allPanel, BoxLayout.Y_AXIS));
        allPanel.add(p);
        JPanel p2 = new JPanel();
        p2.setLayout(new FlowLayout());
        p2.add(accept);
        p2.add(cancel);
        allPanel.add(p2);
        add(allPanel);
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        accept.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    int w = Integer.parseInt(width.getText());
                    int d = Integer.parseInt(depth.getText());
                    int s = Integer.parseInt(spacing.getText());
                    ModelEditorConstants.gridWidth = w;
                    ModelEditorConstants.gridDepth = d;
                    ModelEditorConstants.gridSpacing = s;
                    System.out.println(drawGrid.getSelectedItem());
                    ModelEditorConstants.drawGrid = Boolean.parseBoolean((String) drawGrid.getSelectedItem());
                    dispose();
                } catch (NumberFormatException a) {
                    new ArgumentProblemDialog(dialog, "Entered numbers must be integers");
                }
            }
        });
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }

    private JPanel createPanel(String label, Component c) {
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
        p.add(new JLabel(label));
        p.add(c);
        return p;
    }
}

/**
 * the menu bar for the model editor
 * @author Jack
 *
 */
final class ModelEditorMenuBar extends MenuBar {

    Frame owner;

    Model m;

    Animator animator;

    GLCamera c;

    public ModelEditorMenuBar(Frame o, Model mdl, Animator a, GLCamera camera) {
        owner = o;
        m = mdl;
        animator = a;
        c = camera;
        Menu file = new Menu("File");
        MenuItem save = new MenuItem("Save");
        save.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new SaveDialog(owner, m);
            }
        });
        MenuItem load = new MenuItem("Load");
        load.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new LoadDialog(owner, m);
            }
        });
        MenuItem exit = new MenuItem("Exit");
        exit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new ExitDialog(owner, animator);
            }
        });
        file.add(save);
        file.add(load);
        file.addSeparator();
        file.add(exit);
        Menu view = new Menu("View");
        MenuItem centerView = new MenuItem("Center View");
        centerView.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new CenterViewDialog(owner, c);
            }
        });
        view.add(centerView);
        MenuItem gridSettings = new MenuItem("Grid Settings");
        gridSettings.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new GridSettingsDialog(owner);
            }
        });
        view.add(gridSettings);
        MenuItem cameraSettings = new MenuItem("Camera Settings");
        view.add(cameraSettings);
        Menu model = new Menu("Model");
        MenuItem removeVertex = new MenuItem("Remove Vertex");
        model.add(removeVertex);
        MenuItem translateVertex = new MenuItem("Translate Vertex");
        model.add(translateVertex);
        MenuItem translateOrigin = new MenuItem("Translate Origin");
        model.add(translateOrigin);
        model.addSeparator();
        MenuItem description = new MenuItem("Model Description");
        model.add(description);
        MenuItem modelDisplay = new MenuItem("Model Display");
        model.add(modelDisplay);
        add(file);
        add(view);
        add(model);
    }
}
