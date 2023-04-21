package org.hfbk.vis;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.MenuItem;
import java.awt.MouseInfo;
import java.awt.Panel;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FilenameFilter;
import org.hfbk.ui.IconButton;
import org.hfbk.ui.Tooltip;
import org.hfbk.ui.UIUtils;
import org.hfbk.util.ImageLoader;
import org.hfbk.util.PrefsDialog;
import org.hfbk.util.Sleeper;
import org.hfbk.vis.visnode.VisTooltips;
import org.lwjgl.LWJGLException;

/**
 * 
 * the main window, containing the VisClient canvas and some simple UI to select
 * sources, enter keywords and further options.
 * 
 * @author Paul
 * 
 */
public class VisClientWindow extends Frame {

    VisClientPanel clientPanel = new VisClientPanel();

    public VisClientWindow() throws LWJGLException {
        super("Vis / Client");
        setUndecorated(true);
        setIconImage(ImageLoader.getRessourceImage("icons/vis.png"));
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        setLayout(new BorderLayout());
        add(clientPanel);
        add(buildUI(), BorderLayout.SOUTH);
        UIUtils.blackify(this);
        pack();
        setSize(800, 600);
        setVisible(true);
        clientPanel.requestFocus();
    }

    PopupMenu buildSourcesMenu(final TextField sourceField) {
        PopupMenu sourcePopup = new PopupMenu();
        ActionListener sourceItemListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                sourceField.setText(e.getActionCommand());
                clientPanel.requestFocus();
            }
        };
        for (final String source : Prefs.current.sources.split(",")) {
            MenuItem itm = new MenuItem(source);
            itm.addActionListener(sourceItemListener);
            sourcePopup.add(itm);
        }
        MenuItem itm = new MenuItem("Edit...");
        itm.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new PrefsDialog("sources");
            }
        });
        sourcePopup.add(itm);
        return sourcePopup;
    }

    PopupMenu buildScriptsMenu() {
        PopupMenu menu = new PopupMenu();
        ActionListener scriptItemListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String script = e.getActionCommand();
                VisClientScriptHelper.getScripter(clientPanel.client).source("scripts/" + script);
                clientPanel.requestFocus();
            }
        };
        String[] files = new File("scripts").list(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.endsWith(".vs");
            }
        });
        for (final String script : files) {
            MenuItem itm = new MenuItem(script);
            itm.addActionListener(scriptItemListener);
            menu.add(itm);
        }
        return menu;
    }

    /**
	 * creates the main ui bar at the bottom, containing: source and keyword
	 * fields, the source field with a editable menu. checkoxes for enabling
	 * log, osk, map. field for log username
	 * 
	 * window manipulatiors: close, move, iconify, resize/fullscreen.
	 * 
	 * a keylistener is attached to the client canvas to steal focus if keys
	 * typed are not ctrl-... this allows for convenient entering of keywords.
	 */
    public Panel buildrequester() {
        Panel requester = new Panel();
        requester.add(new Label("request"));
        return requester;
    }

    public Panel buildUI() {
        Panel bar = new Panel();
        bar.setLayout(new BorderLayout());
        Panel p = new Panel();
        p.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        final TextField sourceField = new TextField("google");
        sourceField.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    PopupMenu menu = buildSourcesMenu(sourceField);
                    sourceField.add(menu);
                    menu.show(sourceField, e.getX(), e.getY());
                    sourceField.requestFocus();
                }
            }
        });
        p.add(new Label("Source:"));
        p.add(sourceField);
        final TextField keywordField = new TextField(20);
        keywordField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String source = sourceField.getText(), keyword = keywordField.getText();
                clientPanel.client.root.fetch(source, keyword, null);
                keywordField.setText("");
                clientPanel.requestFocus();
            }

            ;
        });
        p.add(new Label("Keyword:"));
        sourceField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                keywordField.requestFocus();
            }
        });
        p.add(keywordField);
        clientPanel.addKeyListener(new KeyAdapter() {

            public void keyTyped(KeyEvent e) {
                if (Prefs.current.autoFocus && !e.isControlDown() && !Character.isISOControl(e.getKeyChar())) {
                    keywordField.requestFocus();
                    keywordField.setText(keywordField.getText() + e.getKeyChar());
                    keywordField.setCaretPosition(keywordField.getText().length());
                }
            }
        });
        try {
            p.add(PrefsDialog.getPrefsCheckbox(Prefs.current.getClass().getField("log")));
            p.add(new Label("log as:"));
            p.add(PrefsDialog.getPrefsStringInput(Prefs.current.getClass().getField("user"), 10));
            p.add(PrefsDialog.getPrefsCheckbox(Prefs.current.getClass().getField("osk")));
            p.add(new Label("OSK"));
            p.add(PrefsDialog.getPrefsCheckbox(Prefs.current.getClass().getField("map")));
            p.add(new Label("map"));
        } catch (Exception e1) {
            throw new RuntimeException(e1);
        }
        Checkbox helpCheck = new Checkbox();
        new Tooltip("toggle tooltips in space", helpCheck);
        helpCheck.addItemListener(new ItemListener() {

            VisTooltips tt;

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED && tt == null) {
                    tt = new VisTooltips();
                    clientPanel.client.hud.add(tt);
                } else {
                    clientPanel.client.hud.remove(tt);
                    tt = null;
                }
            }
        });
        p.add(helpCheck);
        p.add(new Label("tooltips"));
        bar.add(p, BorderLayout.CENTER);
        p = new Panel();
        IconButton helpButton = new IconButton("icons/help.png", "quick help");
        helpButton.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                VisClientScriptHelper.getScripter(clientPanel.client).source("scripts/help.vs");
            }
        });
        p.add(helpButton);
        IconButton scriptButton = new IconButton("icons/scripts.png", "run scripts");
        scriptButton.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                PopupMenu menu = buildScriptsMenu();
                e.getComponent().add(menu);
                menu.show(e.getComponent(), e.getX(), e.getY());
                sourceField.requestFocus();
            }
        });
        p.add(scriptButton);
        IconButton prefsButton = new IconButton("icons/prefs.png", "open preferences dialog");
        prefsButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new PrefsDialog();
            }
        });
        p.add(prefsButton);
        IconButton closeButton = new IconButton("icons/close.png", "close");
        closeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        p.add(closeButton);
        IconButton iconifyButton = new IconButton("icons/iconify.png", "move window");
        iconifyButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setExtendedState(Frame.ICONIFIED);
            }
        });
        p.add(iconifyButton);
        final IconButton moveButton = new IconButton("icons/move.png", "move window");
        moveButton.addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseDragged(MouseEvent e) {
                int x = e.getX(), y = e.getY();
                Point loc = getLocation();
                setLocation(loc.x + x, loc.y + y);
            }
        });
        p.add(moveButton);
        final IconButton fullButton = new IconButton("icons/resize.png", "toggle fullscreen or drag to resize");
        fullButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (getExtendedState() == Frame.MAXIMIZED_BOTH) {
                    setExtendedState(Frame.NORMAL);
                    moveButton.setVisible(true);
                } else {
                    setExtendedState(Frame.MAXIMIZED_BOTH);
                    moveButton.setVisible(false);
                }
            }
        });
        fullButton.addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseDragged(MouseEvent e) {
                Point wPos = getLocationOnScreen();
                Point mPos = MouseInfo.getPointerInfo().getLocation();
                setSize(mPos.x - wPos.x + 10, mPos.y - wPos.y + 10);
                validate();
            }
        });
        p.add(fullButton);
        bar.add(p, BorderLayout.EAST);
        return bar;
    }

    public void dispose() {
        setVisible(false);
        remove(clientPanel);
        clientPanel.client.reset();
        clientPanel = null;
        Sleeper.sleep(500);
        super.dispose();
        System.exit(0);
    }
}
