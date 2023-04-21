package net.alinnistor.nk.visual.actions;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.plaf.basic.BasicButtonUI;
import net.alinnistor.nk.domain.Context;

/**
 * @author <a href="mailto:nad7ir@yahoo.com">Alin NISTOR</a>
 */
public class NKActionRenderer {

    public static final String BUTTON_KEY = "buttonkey";

    List<Action> actions = new ArrayList<Action>();

    public void addAction(Action action) {
        this.actions.add(action);
    }

    public JPopupMenu asJPopupMenu() {
        JPopupMenu jpm = new JPopupMenu();
        for (Action action : actions) {
            if (action instanceof Separator) {
                jpm.addSeparator();
            } else {
                JMenuItem jmitem = new JMenuItem(action.getValue(AbstractAction.NAME).toString());
                KeyStroke ks = (KeyStroke) action.getValue(Context.ACCELERATOR);
                if (ks != null) {
                    jmitem.setAccelerator(ks);
                }
                jmitem.addActionListener(action);
                jpm.add(jmitem);
            }
        }
        return jpm;
    }

    public JMenu asJMenu(String name) {
        JMenu jpm = new JMenu(name);
        jpm.setForeground(new Color(79, 0, 79));
        for (Action action : actions) {
            if (action instanceof Separator) {
                jpm.addSeparator();
            } else {
                JMenuItem jmitem = new JMenuItem(action.getValue(AbstractAction.NAME).toString());
                KeyStroke ks = (KeyStroke) action.getValue(Context.ACCELERATOR);
                if (ks != null) {
                    jmitem.setAccelerator(ks);
                }
                jmitem.addActionListener(action);
                jpm.add(jmitem);
            }
        }
        return jpm;
    }

    public void asJToolBarButtons(JToolBar jtools) {
        for (final Action action : actions) {
            if (action instanceof Separator) {
                jtools.addSeparator();
            } else {
                final Icon icon = (Icon) action.getValue(Action.SMALL_ICON);
                JButton jb = new JButton(icon) {

                    {
                        int size = 22;
                        setPreferredSize(new Dimension(icon.getIconWidth(), size));
                        setToolTipText(action.getValue(AbstractAction.NAME).toString());
                        setUI(new BasicButtonUI());
                    }
                };
                jb.addActionListener(action);
                jtools.add(jb);
            }
        }
    }

    public void asLineButtons(JPanel panel) {
        for (final Action action : actions) {
            if (action instanceof TranslatorAction) {
                TranslatorAction tra = (TranslatorAction) action;
                panel.add(tra.getJlang());
            } else if (action instanceof Separator) {
                panel.add(new JLabel(" "));
            } else {
                final Icon icon = (Icon) action.getValue(Action.SMALL_ICON);
                JButton jb = null;
                if (action instanceof SwitchAction) {
                    SwitchAction sa = (SwitchAction) action;
                    jb = sa.getJbSwitch();
                    jb.setIcon(icon);
                } else {
                    jb = new JButton(icon);
                }
                jb.setPreferredSize(new Dimension(icon.getIconWidth() + 2, 17));
                jb.setToolTipText(action.getValue(AbstractAction.NAME).toString());
                jb.setUI(new BasicButtonUI());
                jb.setContentAreaFilled(false);
                jb.setBorderPainted(false);
                jb.addActionListener(action);
                panel.add(jb);
                action.putValue(BUTTON_KEY, jb);
            }
        }
    }
}
