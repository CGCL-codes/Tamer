package org.yaoqiang.bpmn.editor.dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import org.json.JSONException;
import org.json.JSONObject;
import org.yaoqiang.graph.editor.dialog.PanelContainer;
import com.mxgraph.util.mxResources;

/**
 * JSONCheckboxPanel
 * 
 * @author Shi Yaoqiang(shi_yaoqiang@yahoo.com)
 */
public class JSONCheckboxPanel extends JSONPanel {

    private static final long serialVersionUID = 1L;

    protected String key;

    protected JCheckBox jcb;

    public JSONCheckboxPanel(PanelContainer pc, JSONObject owner) {
        this(pc, owner, null, true);
    }

    public JSONCheckboxPanel(final PanelContainer pc, JSONObject owner, String key, boolean isEnabled) {
        super(pc, owner);
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.key = key;
        JLabel jl = new JLabel(" " + mxResources.get(key));
        jcb = new JCheckBox();
        jcb.setBorder(BorderFactory.createEmptyBorder());
        boolean value = owner.optBoolean(key);
        jcb.setSelected(value);
        jcb.setEnabled(isEnabled);
        jcb.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                pc.panelChanged();
            }
        });
        this.add(jcb, BorderLayout.WEST);
        this.add(Box.createHorizontalGlue(), BorderLayout.EAST);
        this.add(jl, BorderLayout.CENTER);
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

    public void saveObjects() {
        try {
            if (jcb.isEnabled()) {
                ((JSONObject) owner).put(this.key, jcb.isSelected());
            } else {
                ((JSONObject) owner).put(this.key, (Object) null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        jcb.setEnabled(b);
    }
}
