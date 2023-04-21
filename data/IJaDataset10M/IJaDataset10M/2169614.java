package ch.odi.justblog.gui.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Hashtable;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.text.StyledEditorKit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This is a graphical HTML editor for Blog entries.
 *
 * @author oglueck
 */
public class Editor extends JPanel {

    private Hashtable actions = new Hashtable();

    private JEditorPane ep;

    private JToolBar toolBar;

    /**
     * Creates a new editor component.
     */
    public Editor() {
        ep = new JEditorPane("text/html", "");
        setLayout(new BorderLayout());
        add(ep, BorderLayout.CENTER);
        initActionTable();
        initToolbar();
    }

    public void setHtml(String html) {
        ep.setText(html);
    }

    public String getHtml() {
        return ep.getText();
    }

    public void setEnabled(boolean enabled) {
        ep.setEnabled(enabled);
        toolBar.setEnabled(enabled);
    }

    private void initActionTable() {
        Log log = LogFactory.getLog(this.getClass());
        StyledEditorKit editorKit = (StyledEditorKit) ep.getEditorKit();
        Action[] actionArray = editorKit.getActions();
        for (int i = 0; i < actionArray.length; i++) {
            Action action = actionArray[i];
            actions.put(action.getValue(Action.NAME), action);
            log.debug("editor supports action: " + action.getValue(Action.NAME));
        }
    }

    private void initToolbar() {
        toolBar = new MyToolBar();
        add(toolBar, BorderLayout.NORTH);
    }

    private class MyToolBar extends JToolBar {

        public MyToolBar() {
            JButton boldButton = new JButton("bold");
            boldButton.addActionListener((Action) actions.get("font-bold"));
            this.add(boldButton);
            JButton italicButton = new JButton("italic");
            italicButton.addActionListener((Action) actions.get("font-italic"));
            this.add(italicButton);
        }

        /**
         * Sets the status of all the buttons.
         */
        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);
            Component[] components = this.getComponents();
            for (int i = 0; i < components.length; i++) {
                components[i].setEnabled(enabled);
            }
        }
    }
}
