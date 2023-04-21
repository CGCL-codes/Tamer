package sidekick.css;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.EditBus;
import org.gjt.sp.jedit.EBComponent;
import org.gjt.sp.jedit.msg.PropertiesChanged;

/**
 * This is a toolbar to be added to SideKick. It provides the ability to sort
 * by line or name and toggle line numbers on and off without having to open
 * the plugin option panes.
 */
public class CSSModeToolBar extends JPanel {

    private JMenuItem byLine;

    private JMenuItem byName;

    private JCheckBoxMenuItem toggleLineNumbers;

    private EBComponent parent;

    public static final int SORT_BY_LINE = 0;

    public static final int SORT_BY_NAME = 1;

    /**
     * @param parent An EBComponent to include with PropertyChanged messages.
     * This will be a JavaParser.
     */
    public CSSModeToolBar(EBComponent parent) {
        this.parent = parent;
        installComponents();
        installListeners();
    }

    private void installComponents() {
        JMenuBar menuBar = new JMenuBar();
        JMenu sortMenu = new JMenu(jEdit.getProperty("options.sidekick.css.sortBy", "Sorting"));
        byLine = new JMenuItem(jEdit.getProperty("options.sidekick.css.sortByLine", "Line"));
        byName = new JMenuItem(jEdit.getProperty("options.sidekick.css.sortByName", "Name"));
        toggleLineNumbers = new JCheckBoxMenuItem(jEdit.getProperty("options.sidekick.css.showLineNums", "Line Numbers"));
        toggleLineNumbers.setSelected(jEdit.getBooleanProperty("sidekick.css.showLineNums", false));
        add(menuBar);
        menuBar.add(sortMenu);
        sortMenu.add(byLine);
        sortMenu.add(byName);
        menuBar.add(toggleLineNumbers);
    }

    private void installListeners() {
        byLine.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                jEdit.setIntegerProperty("sidekick.css.sortBy", SORT_BY_LINE);
                EditBus.send(new PropertiesChanged(parent));
            }
        });
        byName.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                jEdit.setIntegerProperty("sidekick.css.sortBy", SORT_BY_NAME);
                EditBus.send(new PropertiesChanged(parent));
            }
        });
        toggleLineNumbers.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                jEdit.setBooleanProperty("sidekick.css.showLineNums", toggleLineNumbers.isSelected());
                EditBus.send(new PropertiesChanged(parent));
            }
        });
    }
}
