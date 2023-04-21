package org.yaoqiang.bpmn.editor.dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import org.yaoqiang.bpmn.model.elements.XMLElement;
import org.yaoqiang.graph.editor.dialog.PanelContainer;
import org.yaoqiang.graph.util.Constants;
import com.mxgraph.util.mxResources;

/**
 * CheckboxPanel
 * 
 * @author Shi Yaoqiang(shi_yaoqiang@yahoo.com)
 */
public class CheckboxPanel extends XMLPanel {

    private static final long serialVersionUID = 1L;

    protected String title;

    protected JCheckBox jcb;

    protected boolean defaultValue;

    public CheckboxPanel(PanelContainer pc, XMLElement owner, String title) {
        this(pc, owner, title, true, true);
    }

    public CheckboxPanel(PanelContainer pc, XMLElement owner, boolean defaultValue) {
        this(pc, owner, null, true, defaultValue);
    }

    public CheckboxPanel(PanelContainer pc, XMLElement owner, String title, boolean isEnabled, boolean defaultValue) {
        super(pc, owner);
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.title = title;
        this.defaultValue = defaultValue;
        JLabel jl = new JLabel(mxResources.get(title == null ? owner.toName() : title) + ": ");
        jcb = new JCheckBox();
        jcb.setBorder(BorderFactory.createEmptyBorder());
        boolean value = Boolean.parseBoolean(owner == null ? "false" : owner.toValue());
        if (title != null) {
            value = Constants.SETTINGS.getProperty(title, "1").equals("1");
        }
        if (title == null && owner.toValue().length() == 0) {
            jcb.setSelected(defaultValue);
        } else {
            jcb.setSelected(value);
        }
        jcb.setEnabled(isEnabled);
        jcb.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                getPanelContainer().panelChanged();
            }
        });
        this.add(jl, BorderLayout.WEST);
        this.add(Box.createHorizontalGlue(), BorderLayout.EAST);
        this.add(jcb, BorderLayout.CENTER);
    }

    public JCheckBox getCheckBox() {
        return jcb;
    }

    public boolean isSelected() {
        return jcb.isSelected();
    }

    public void setSelected(boolean b) {
        jcb.setSelected(b);
    }

    public void addActionListener(ActionListener l) {
        jcb.addActionListener(l);
    }

    public void setElements() {
        if (jcb.isSelected()) {
            if (title != null) {
                Constants.SETTINGS.put(title, "1");
            } else {
                if (defaultValue) {
                    getOwner().setValue("");
                } else {
                    getOwner().setValue("true");
                }
            }
        } else {
            if (title != null) {
                Constants.SETTINGS.put(title, "0");
            } else {
                if (!defaultValue) {
                    getOwner().setValue("");
                } else {
                    getOwner().setValue("false");
                }
            }
        }
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        jcb.setEnabled(b);
    }
}
