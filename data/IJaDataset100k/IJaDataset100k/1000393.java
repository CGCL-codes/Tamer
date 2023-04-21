package net.sourceforge.jscreengrabber.gui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sourceforge.jscreengrabber.animations.ExitAnimation;
import net.sourceforge.jscreengrabber.animations.MinimizeCompositeAnimator;
import net.sourceforge.jscreengrabber.animations.RestoreCompositeAnimator;
import net.sourceforge.jscreengrabber.utils.GrabberGraphics;
import net.sourceforge.jscreengrabber.utils.Options;
import net.sourceforge.jscreengrabber.utils.OptionsEngine;
import net.sourceforge.jscreengrabber.utils.OptionsProcessor;
import com.l2fprod.common.swing.JDirectoryChooser;

/**
 * This method will set the system tray and handle it's events.
 * 
 * @author Savvas Dalkitsis
 */
public class GrabberTray {

    private static TrayIcon tray = null;

    /**
	 * This point will hold the location of the window before it was minimized.
	 */
    private static Point savedLocation = new Point();

    /**
	 * This point will hold the size of the window before it was minimized.
	 */
    private static Dimension savedSize = new Dimension();

    private GrabberTray() {
        SystemTray sysTray = SystemTray.getSystemTray();
        PopupMenu popup = new PopupMenu();
        MenuItem dirItem = new MenuItem("Set a default directory to save images");
        MenuItem clearDirItem = new MenuItem("Clear the default directory");
        MenuItem minimizedToggle = new MenuItem("Toggle start up minimized");
        MenuItem borderItem = new MenuItem("Change border width");
        MenuItem colorItem = new MenuItem("Change border color");
        MenuItem aboutItem = new MenuItem("About");
        MenuItem exitItem = new MenuItem("Exit");
        dirItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JDirectoryChooser();
                if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    setDefaultDir(fc.getSelectedFile());
                }
            }
        });
        clearDirItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setDefaultDir(null);
            }
        });
        minimizedToggle.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                toggleMinimized();
            }
        });
        borderItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                final JDialog d = new JDialog((JFrame) null, "Change Border Width", false);
                final Integer borderWidth = (Integer) OptionsProcessor.process(OptionsEngine.getOptions().get(Options.BorderSize.getID()), Integer.class);
                final JSlider borderSlider = new JSlider(1, 20, borderWidth);
                JButton btOk = new JButton("OK");
                JButton btCancel = new JButton("Cancel");
                borderSlider.addChangeListener(new ChangeListener() {

                    @Override
                    public void stateChanged(ChangeEvent e) {
                        setBorderWidth(borderSlider.getValue());
                    }
                });
                btOk.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        d.dispose();
                    }
                });
                btCancel.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        setBorderWidth(borderWidth);
                        d.dispose();
                    }
                });
                borderSlider.setMajorTickSpacing(2);
                borderSlider.setMinorTickSpacing(1);
                borderSlider.setPaintLabels(true);
                borderSlider.setPaintTicks(true);
                JPanel tmpPanel = new JPanel(new BorderLayout());
                tmpPanel.add(btOk, BorderLayout.WEST);
                tmpPanel.add(btCancel, BorderLayout.EAST);
                tmpPanel.add(Box.createHorizontalStrut(50), BorderLayout.CENTER);
                d.getContentPane().setLayout(new BoxLayout(d.getContentPane(), BoxLayout.Y_AXIS));
                d.getContentPane().add(borderSlider);
                d.getContentPane().add(tmpPanel);
                d.setIconImage(GrabberGraphics.saveImg);
                d.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                d.pack();
                d.setLocationRelativeTo(null);
                d.setVisible(true);
            }
        });
        colorItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Color col = (Color) OptionsProcessor.process(OptionsEngine.getOptions().get(Options.Color.getID()), Color.class);
                Color selection = JColorChooser.showDialog(null, "Choose a color for the window", col);
                if (selection != null) {
                    setBorderColor(selection);
                }
            }
        });
        aboutItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                AboutDialog.showDialog();
            }
        });
        exitItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                if (GrabberWindow.getWindow().getLocation().equals(new Point(screen.width, screen.height))) {
                    GrabberWindow.getWindow().setLocation(savedLocation);
                    GrabberWindow.getWindow().setSize(savedSize);
                }
                new ExitAnimation().getAnimator(200).start();
            }
        });
        popup.add(dirItem);
        popup.add(clearDirItem);
        popup.add(minimizedToggle);
        popup.addSeparator();
        popup.add(borderItem);
        popup.add(colorItem);
        popup.addSeparator();
        popup.add(aboutItem);
        popup.add(exitItem);
        tray = new TrayIcon(GrabberGraphics.saveImg, "JScreenGrabber", popup);
        tray.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    if (!GrabberWindow.getWindow().isVisible()) {
                        GrabberWindow.getWindow().setVisible(true);
                        new RestoreCompositeAnimator(savedLocation, savedSize).start();
                    } else {
                        GrabberTray.setSavedLocation(GrabberWindow.getWindow().getLocation());
                        GrabberTray.setSavedSize(GrabberWindow.getWindow().getSize());
                        new MinimizeCompositeAnimator().start();
                    }
                }
            }
        });
        tray.setImageAutoSize(true);
        try {
            sysTray.add(tray);
        } catch (AWTException e1) {
            e1.printStackTrace();
        }
    }

    /**
	 * This method will toggle the start up minimized option.
	 */
    private void toggleMinimized() {
        boolean current = (Boolean) OptionsProcessor.process(OptionsEngine.getOptions().get(Options.StartMinimized.getID()), Boolean.class);
        OptionsEngine.replace(Options.StartMinimized.getID(), Boolean.toString(!current));
        String message = null;
        if (!current) {
            message = "From now on the program will start minimized.";
        } else {
            message = "From now on the program will not be minimized when it starts.";
        }
        tray.displayMessage("Start up minimized behaviour changed", message, TrayIcon.MessageType.INFO);
    }

    /**
	 * This method will set the provided directory as the default one in the
	 * options. If null is provided then "" is saved.
	 * 
	 * @param dir
	 *            The directory to be set as the default. If null is provided
	 *            then "" is saved.
	 */
    private void setDefaultDir(File dir) {
        String s = "";
        if (dir != null) s = dir.getAbsolutePath();
        OptionsEngine.replace(Options.Directory.getID(), s);
        if (s.equals("")) tray.displayMessage("Cleared default directory", "The default directory has been cleared. Next time you try to save an image, a dialog will be displayed to allow you to select a location.", TrayIcon.MessageType.INFO); else tray.displayMessage("Default directory saved", s + " has been set as the default directory. From now on when you save an image, it will be saved in that location automatically with an incremental name.", TrayIcon.MessageType.INFO);
    }

    /**
	 * This method will set the provided width for the border in the options. If
	 * a value is less than or equal to 0 nothing happens.
	 * 
	 * @param width
	 *            The width to set as a border width. If a value is less than or
	 *            equal to 0 nothing happens.
	 */
    private void setBorderWidth(int width) {
        if (width <= 0) return;
        OptionsEngine.replace(Options.BorderSize.getID(), width + "");
        GrabberWindow.getWindow().getContentPane().dispatchEvent(new ComponentEvent(GrabberWindow.getWindow().getContentPane(), ComponentEvent.COMPONENT_RESIZED));
    }

    /**
	 * This method will set the provided color for the border in the options.
	 * 
	 * @param c
	 *            The color to set as the border color.
	 */
    private void setBorderColor(Color c) {
        OptionsEngine.replace(Options.Color.getID(), c.getRed() + ":" + c.getGreen() + ":" + c.getBlue());
        GrabberGraphics.createImages();
        GrabberWindow.getWindow().getContentPane().repaint();
    }

    /**
	 * Returns the internal reference to the system tray icon.
	 * 
	 * @return The internal reference to the system tray icon.
	 */
    public static TrayIcon getTray() {
        if (tray == null) new GrabberTray();
        return tray;
    }

    /**
	 * This method will save the location of the window before it is minimized
	 * in order to restore it to the correct place.
	 * 
	 * @param p
	 *            The location of the window.
	 */
    public static void setSavedLocation(Point p) {
        savedLocation = p;
    }

    /**
	 * This method will save the dimension of the window before it is minimized
	 * in order to restore it to the correct size.
	 * 
	 * @param s
	 *            The size of the window.
	 */
    public static void setSavedSize(Dimension s) {
        savedSize = s;
    }
}
