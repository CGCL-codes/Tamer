package org.yaoqiang.bpmn.editor.ui.dialog.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.yaoqiang.bpmn.editor.ui.dialog.PanelContainer;
import org.yaoqiang.bpmn.editor.ui.dialog.XMLPanel;
import org.yaoqiang.bpmn.model.elements.data.DataState;
import com.mxgraph.util.mxResources;

/**
 * DataStatePanel
 * 
 * @author Shi Yaoqiang(shi_yaoqiang@yahoo.com)
 */
public class DataStatePanel extends XMLPanel {

    private static final long serialVersionUID = 1L;

    protected JTextField jtf;

    public DataStatePanel(PanelContainer pc, DataState owner) {
        super(pc, owner);
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JLabel jl = new JLabel(mxResources.get(owner.toName()) + ": ");
        jtf = new JTextField();
        jtf.setText(owner.getName());
        Dimension textDim = new Dimension(150, 26);
        jtf.setMinimumSize(new Dimension(textDim));
        jtf.setMaximumSize(new Dimension(textDim));
        jtf.setPreferredSize(new Dimension(textDim));
        jtf.setEnabled(true);
        JPanel statePanel = new JPanel();
        statePanel.setLayout(new BorderLayout());
        statePanel.add(new JLabel("["), BorderLayout.WEST);
        statePanel.add(jtf, BorderLayout.CENTER);
        statePanel.add(new JLabel("]"), BorderLayout.EAST);
        this.add(jl, BorderLayout.WEST);
        this.add(Box.createHorizontalGlue(), BorderLayout.EAST);
        this.add(statePanel, BorderLayout.CENTER);
    }

    public String getText() {
        return jtf.getText().trim();
    }

    public void setElements() {
        ((DataState) owner).setName(getText());
    }
}
