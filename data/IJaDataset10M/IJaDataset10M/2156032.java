package gui.settings;

import gui.state.EditorState;
import gui.state.PropertyTab;
import gui.utils.ComponentSwitcher;
import gui.utils.Size10Label;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;
import javax.media.opengl.GL;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FogTab extends JPanel implements PropertyTab {

    private static final long serialVersionUID = -4414644578892580003L;

    private JTextField colour, density, start, end;

    private JComboBox mode;

    JCheckBox fogEnabled;

    ComponentSwitcher switcher;

    protected final int[] NONE = {}, EXP = { 0, 1, 2, 3, 4, 5 }, LINEAR = { 0, 1, 2, 3, 6, 7, 8, 9 };

    public FogTab() {
        setLayout(new BorderLayout());
        final FogTab f = this;
        JPanel north = new JPanel();
        north.add(fogEnabled = new JCheckBox("enable fog", false));
        fogEnabled.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (!fogEnabled.isSelected()) switcher.show(NONE, 0); else if (mode.getSelectedIndex() == 2) switcher.show(LINEAR, 2); else switcher.show(EXP, 2);
                writeProperties(EditorState.getAppProperties());
                f.repaint();
            }
        });
        add(north, BorderLayout.NORTH);
        colour = new JTextField("0.7 0.7 0.7");
        colour.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                writeProperties(EditorState.getAppProperties());
            }
        });
        density = new JTextField("0.00005");
        density.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                writeProperties(EditorState.getAppProperties());
            }
        });
        start = new JTextField("0.0");
        start.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                writeProperties(EditorState.getAppProperties());
            }
        });
        end = new JTextField("100000");
        end.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                writeProperties(EditorState.getAppProperties());
            }
        });
        mode = new JComboBox();
        mode.addItem("exponential");
        mode.addItem("exponential squared");
        mode.addItem("linear");
        mode.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (!fogEnabled.isSelected()) switcher.show(NONE, 0); else if (mode.getSelectedIndex() == 2) switcher.show(LINEAR, 2); else switcher.show(EXP, 2);
                writeProperties(EditorState.getAppProperties());
                invalidate();
            }
        });
        Component[] comps = { new Size10Label("fog equation:"), mode, new Size10Label("fog colour:"), colour, new Size10Label("density:"), density, new Size10Label("start:"), start, new Size10Label("end"), end };
        switcher = new ComponentSwitcher(comps);
        switcher.show(NONE, 0);
        add(switcher);
    }

    public void writeProperties(Properties properties) {
        String fogEnable = fogEnabled.isSelected() ? "true" : "false";
        properties.setProperty("fog.enable", fogEnable);
        properties.setProperty("fog.equation", indexToGL(mode.getSelectedIndex()));
        properties.setProperty("fog.colour", colour.getText());
        properties.setProperty("fog.density", density.getText());
    }

    public void populateFromProperties(Properties properties) {
        boolean fogEnabled = Boolean.parseBoolean(properties.getProperty("fog.enable"));
        this.fogEnabled.setSelected(fogEnabled);
        mode.setSelectedIndex(glToIndex(properties.getProperty("fog.equation")));
        colour.setText(properties.getProperty("fog.colour"));
        density.setText(properties.getProperty("fog.density"));
    }

    public boolean isModified() {
        return false;
    }

    protected int glToIndex(String glMode) {
        switch(Integer.parseInt(glMode)) {
            case GL.GL_EXP:
                return 0;
            case GL.GL_EXP2:
                return 1;
            default:
                return 2;
        }
    }

    protected String indexToGL(int index) {
        switch(index) {
            case 0:
                return GL.GL_EXP + "";
            case 1:
                return GL.GL_EXP2 + "";
            default:
                return GL.GL_LINEAR + "";
        }
    }
}
