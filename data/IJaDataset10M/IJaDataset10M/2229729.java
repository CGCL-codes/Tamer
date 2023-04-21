package net.sourceforge.picdev.ui;

import net.sourceforge.picdev.annotations.AnnotationUtil;
import net.sourceforge.picdev.annotations.EComponent;
import net.sourceforge.picdev.annotations.PropertyInfo;
import net.sourceforge.picdev.components.RS232;
import net.sourceforge.picdev.core.Component;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author Klaus Friedel
 *         Date: 12.12.2007
 *         Time: 06:30:11
 */
public class PropertyPanel extends JPanel {

    Component editComponent;

    JLabel title = new JLabel();

    JPanel panel = new JPanel(new GridBagLayout());

    JPanel helperPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

    JScrollPane tableScroll = new JScrollPane(helperPanel);

    private class PropertyHandler implements PropertyEditorFactory.PropertyValue {

        PropertyInfo pi;

        Component comp;

        private PropertyHandler(PropertyInfo pi, Component sourceBean) {
            this.comp = sourceBean;
            this.pi = pi;
        }

        public Object get() {
            return pi.get(comp);
        }

        public void set(Object value) {
            Object old = get();
            pi.set(comp, value);
            firePropertyChange(pi.getName(), old, value);
        }
    }

    public PropertyPanel() {
        helperPanel.add(panel);
        setLayout(new BorderLayout());
        title.setHorizontalAlignment(JLabel.CENTER);
        add(title, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
    }

    public void setEComponent(Component comp) {
        this.editComponent = comp;
        if (comp != null) {
            analyzeComponent();
        } else {
            clearView();
        }
    }

    public Component getEComponent() {
        return editComponent;
    }

    private void analyzeComponent() {
        EComponent classAnno = editComponent.getClass().getAnnotation(EComponent.class);
        List<PropertyInfo> propertyInfoList = AnnotationUtil.findProperties(editComponent.getClass());
        updateView(classAnno, propertyInfoList);
    }

    private void updateView(EComponent classAnno, List<PropertyInfo> propertyInfoList) {
        title.setText(classAnno.name());
        panel.removeAll();
        int y = 0;
        for (PropertyInfo info : propertyInfoList) {
            final JLabel label = new JLabel();
            label.setText(info.getName());
            label.setToolTipText(info.getDescription());
            GridBagConstraints gbc;
            gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = y;
            gbc.anchor = GridBagConstraints.WEST;
            panel.add(label, gbc);
            JComponent editor = PropertyEditorFactory.createFor(info, new PropertyHandler(info, editComponent));
            gbc = new GridBagConstraints();
            gbc.gridx = 1;
            gbc.gridy = y;
            gbc.insets = new Insets(2, 12, 2, 2);
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            panel.add(editor, gbc);
            y++;
        }
        panel.revalidate();
        panel.repaint();
    }

    private void clearView() {
        title.setText("");
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
    }

    public void setShowTitle(boolean show) {
        title.setVisible(show);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new com.incors.plaf.kunststoff.KunststoffLookAndFeel());
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 600);
            PropertyPanel panel = new PropertyPanel();
            frame.add(panel);
            panel.setEComponent(new RS232());
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
