package net.sf.compositor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import net.sf.compositor.util.Config;
import net.sf.compositor.util.ResourceLoader;
import net.sf.compositor.util.StringUtil;

/**
 * Generates a label.
 */
public class LabelGenerator extends Generator {

    protected LabelGenerator(final App app, final String clarse) {
        super(app, clarse, false);
    }

    protected LabelGenerator(final App app) {
        super(app, "javax.swing.JLabel", false);
    }

    @Override
    protected void setAttributes(final App app, final JComponent component, final String windowName, final String componentName, final Config infoMap, final int indent) {
        if (infoMap.containsKey("align")) {
            final String alignment = infoMap.getProperty("align");
            if ("right".equals(alignment)) {
                ((JLabel) component).setHorizontalAlignment(SwingConstants.RIGHT);
            } else if ("center".equals(alignment) || "centre".equals(alignment)) {
                ((JLabel) component).setHorizontalAlignment(SwingConstants.CENTER);
            } else if ("left".equals(alignment)) {
                ((JLabel) component).setHorizontalAlignment(SwingConstants.LEFT);
            } else {
                s_log.warn("Unrecognised alignment: " + alignment);
            }
        }
        if (infoMap.containsKey("verticalAlign")) {
            final String alignment = infoMap.getProperty("verticalAlign");
            if ("top".equals(alignment)) {
                ((JLabel) component).setVerticalAlignment(SwingConstants.TOP);
            } else if ("center".equals(alignment) || "centre".equals(alignment)) {
                ((JLabel) component).setVerticalAlignment(SwingConstants.CENTER);
            } else if ("bottom".equals(alignment)) {
                ((JLabel) component).setVerticalAlignment(SwingConstants.BOTTOM);
            } else {
                s_log.warn("Unrecognised vertical alignment: " + alignment);
            }
        }
        if (infoMap.containsKey("icon")) {
            ((JLabel) component).setIcon(ResourceLoader.getIcon(infoMap.getProperty("icon")));
        }
        if (infoMap.containsKey("for")) {
            final String forName = infoMap.getProperty("for");
            app.runAfterUiBuilt(new Runnable() {

                public void run() {
                    ((JLabel) component).setLabelFor(app.get(windowName + '.' + forName).getComponent());
                }
            });
        }
        if (infoMap.containsKey("horizontalTextPosition")) {
            final JLabel label = ((JLabel) component);
            final String htp = infoMap.getProperty("horizontalTextPosition");
            if ("left".equals(htp)) {
                label.setHorizontalTextPosition(SwingConstants.LEFT);
            } else if ("center".equals(htp) || "centre".equals(htp)) {
                label.setHorizontalTextPosition(SwingConstants.CENTER);
            } else if ("right".equals(htp)) {
                label.setHorizontalTextPosition(SwingConstants.RIGHT);
            } else if ("leading".equals(htp)) {
                label.setHorizontalTextPosition(SwingConstants.LEADING);
            } else if ("trailing".equals(htp)) {
                label.setHorizontalTextPosition(SwingConstants.TRAILING);
            } else {
                s_log.warn("Unrecognised horizontal text position: " + htp);
            }
        }
        if (infoMap.containsKey("verticalTextPosition")) {
            final JLabel label = ((JLabel) component);
            final String vtp = infoMap.getProperty("verticalTextPosition");
            if ("top".equals(vtp)) {
                label.setVerticalTextPosition(SwingConstants.TOP);
            } else if ("center".equals(vtp) || "centre".equals(vtp)) {
                label.setVerticalTextPosition(SwingConstants.CENTER);
            } else if ("bottom".equals(vtp)) {
                label.setVerticalTextPosition(SwingConstants.BOTTOM);
            } else {
                s_log.warn("Unrecognised vertical text position: " + vtp);
            }
        }
        if (infoMap.containsKey("action")) {
            final String actionName = infoMap.getProperty("action");
            final JLabel label = (JLabel) component;
            final AppAction action = app.getAction(actionName);
            if (null == action) {
                s_log.warn("No action " + actionName + " found for label " + windowName + '.' + componentName);
            } else {
                label.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseClicked(final MouseEvent e) {
                        action.actionPerformed(new ActionEvent(e.getSource(), e.getID(), actionName, e.getWhen(), e.getModifiers()));
                    }

                    @Override
                    public String toString() {
                        return "I listen for clicks on " + windowName + '.' + componentName;
                    }
                });
                label.setText(StringUtil.toSafeString(action.getValue(Action.NAME)));
                if (null == label.getIcon()) {
                    label.setIcon((Icon) action.getValue(Action.SMALL_ICON));
                }
                label.setToolTipText(action.getToolTip());
            }
        }
    }

    @Override
    void setContent(final Component component, final String content, final int indent) {
        final JLabel label = (JLabel) component;
        final ButtonDecor decor = ButtonDecor.getInstance(content, indent);
        label.setText(decor.getText());
        if (decor.hasMnemonic()) {
            label.setDisplayedMnemonic(decor.getMnemonic());
            label.setDisplayedMnemonicIndex(decor.getMnemonicIndex());
        }
    }
}
