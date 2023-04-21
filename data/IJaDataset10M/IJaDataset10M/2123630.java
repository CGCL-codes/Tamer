package nngl.common.editor;

import javax.swing.*;
import static java.awt.BorderLayout.*;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.util.Properties;

/**
 * Panel for editing graph
 */
public class EditPanel extends JPanel implements ActionListener, ItemListener {

    private final PaintPanel paintPanel;

    private final JButton submit;

    private final JComboBox count;

    private final EditorListener mainFrame;

    /**
     * @param cv initial count of vertexes in editing graph
     * @param mf main frame of the project
     */
    public EditPanel(int cv, EditorListener mf, int min, int max, Properties properties) {
        super(new BorderLayout());
        mainFrame = mf;
        paintPanel = new PaintPanel(cv, properties);
        add(paintPanel, CENTER);
        JPanel controlPanel = new JPanel();
        submit = new JButton(properties.getProperty("button.submit"));
        submit.addActionListener(this);
        controlPanel.add(submit, WEST);
        count = new JComboBox();
        for (int i = min; i <= max; i++) {
            count.addItem(i);
        }
        count.addItemListener(this);
        count.setSelectedIndex(cv - min);
        controlPanel.add(count, EAST);
        add(controlPanel, SOUTH);
    }

    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o == submit) {
            mainFrame.submit(paintPanel.getGraph());
        }
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == count) {
            int cv = (Integer) e.getItem();
            paintPanel.setCountVertexes(cv);
            paintPanel.repaint();
        }
    }
}
