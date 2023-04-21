package gui;

import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/** Common AWT-Application behaviour */
public class AwtApplication extends Frame {

    private static final long serialVersionUID = 1L;

    protected final MenuBar menuBar;

    protected final Menu fileMenu;

    protected final ExitListener exitListener;

    protected final MenuItem openItem;

    private static final int MAX_ARRAY_L = 300;

    /** Construct a template AWT application with menu and window listener. */
    protected AwtApplication(final String title) {
        super(title);
        int[] array1 = new int[MAX_ARRAY_L];
        for (int i = 0; i < MAX_ARRAY_L; i++) {
            array1[i] = (int) Math.random() * 300 + 1;
            ;
        }
        exitListener = new ExitListener();
        addWindowListener(exitListener);
        menuBar = new MenuBar();
        setMenuBar(menuBar);
        fileMenu = new Menu("File");
        menuBar.add(fileMenu);
        final MenuItem exitItem = new MenuItem("Exit", new MenuShortcut('Q'));
        openItem = new MenuItem("Open", new MenuShortcut('O'));
        fileMenu.add(openItem);
        fileMenu.add(exitItem);
        exitItem.addActionListener(exitListener);
    }

    /** Useful helper for AWT applications to exit. */
    class ExitListener extends WindowAdapter implements ActionListener {

        @Override
        public void windowClosing(final WindowEvent event) {
            System.exit(0);
        }

        @Override
        public void actionPerformed(final ActionEvent event) {
            System.exit(0);
        }
    }
}
