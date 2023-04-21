package de.hs_mannheim.visualscheduler.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import de.hs_mannheim.visualscheduler.gui.NavigationBar.NavigationBarObserver;
import de.hs_mannheim.visualscheduler.scheduling.ProcessDefinition;
import de.hs_mannheim.visualscheduler.scheduling.SchedulingResult;

/**
 * The Class MainGUI implements the GUI for the VisualScheduler. It provides next and previous buttons to switch between the
 * settings and result GUI.
 */
public class MainGUI extends JPanel implements ActionListener, NavigationBarObserver {

    private static final long serialVersionUID = 3463335861983048205L;

    private final SettingsGUI settingsGui = new SettingsGUI();

    private final ResultGUI resultGui = new ResultGUI();

    private final JMenuItem mitemSave = new JMenuItem("Save");

    private final JMenuItem mitemLoad = new JMenuItem("Load");

    public MainGUI() {
        BorderLayout borderLayout = new BorderLayout();
        borderLayout.setVgap(10);
        borderLayout.setHgap(10);
        setLayout(borderLayout);
        borderLayout = new BorderLayout();
        final NavigationBar control = new NavigationBar(2);
        control.addObserver(this);
        SubstanceLookAndFeel.setDecorationType(control, DecorationAreaType.FOOTER);
        final JMenuBar mb = new JMenuBar();
        final JMenu m = new JMenu("Processes");
        m.add(mitemSave);
        m.add(mitemLoad);
        mb.add(m);
        mitemSave.addActionListener(this);
        mitemLoad.addActionListener(this);
        add(mb, BorderLayout.NORTH);
        add(settingsGui, BorderLayout.CENTER);
        add(control, BorderLayout.SOUTH);
        randomize();
        repaint();
    }

    @Override
    public void actionPerformed(final ActionEvent arg0) {
        if (mitemSave == arg0.getSource()) {
            save();
        } else if (mitemLoad == arg0.getSource()) {
            load();
        }
    }

    @Override
    public void setPage(final int page) {
        System.out.println("Page " + page);
        if (page == 1) {
            final SchedulingResult result = settingsGui.getScheduler().performScheduling(settingsGui.getProcesses(), settingsGui.getTimeSliceLength());
            resultGui.setScheduling(result);
            remove(settingsGui);
            add(resultGui, BorderLayout.CENTER);
        } else {
            remove(resultGui);
            add(settingsGui, BorderLayout.CENTER);
        }
        validate();
        repaint();
    }

    /**
	 * Load Process-Definitions from a file. This <b>replaces</b> existing Definitions.
	 */
    private void load() {
        final JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(this);
        final File load = fc.getSelectedFile();
        try {
            final ObjectInputStream ois = new ObjectInputStream(new FileInputStream(load));
            try {
                settingsGui.setProcesses((ProcessDefinition[]) ois.readObject());
            } finally {
                ois.close();
            }
        } catch (final Exception e) {
            JOptionPane.showMessageDialog(this, e.toString());
            e.printStackTrace();
        }
    }

    /**
	 * Randomizes Process-Definitions. This <b>replaces</b> existing Definitions.
	 */
    private void randomize() {
        final ArrayList<ProcessDefinition> list = new ArrayList<ProcessDefinition>();
        for (int i = 0; i < 10; i++) {
            list.add(new ProcessDefinition((int) (Math.random() * 100), (int) (Math.random() * 50), 1, "Process " + i));
        }
        settingsGui.setProcesses(list.toArray(new ProcessDefinition[list.size()]));
    }

    /**
	 * Save Process-Definitions to a file.
	 */
    private void save() {
        final JFileChooser fc = new JFileChooser();
        fc.showSaveDialog(this);
        final File save = fc.getSelectedFile();
        try {
            final ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(save));
            try {
                oos.writeObject(settingsGui.getProcesses());
            } finally {
                oos.close();
            }
        } catch (final IOException e) {
            JOptionPane.showMessageDialog(this, e.toString());
            e.printStackTrace();
        }
    }
}
