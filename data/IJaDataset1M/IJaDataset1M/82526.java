package background;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import org.gjt.sp.jedit.AbstractOptionPane;
import org.gjt.sp.jedit.GUIUtilities;
import org.gjt.sp.jedit.MiscUtilities;
import org.gjt.sp.jedit.jEdit;

public class BackgroundOptionPane extends AbstractOptionPane {

    private JButton btnBackground;

    private JTextField tfBackground;

    private JCheckBox blend;

    private JButton blendColor;

    private JSlider blendAlpha;

    private JComboBox imagePosition;

    public static final String[] IMAGE_POSITIONS = { "center", "strech", "tile" };

    static String[] POSITION_LABELS = new String[IMAGE_POSITIONS.length];

    static {
        for (int i = 0; i < IMAGE_POSITIONS.length; i++) {
            POSITION_LABELS[i] = jEdit.getProperty("options.background.image-" + IMAGE_POSITIONS[i], IMAGE_POSITIONS[i]);
        }
    }

    public BackgroundOptionPane() {
        super("background");
    }

    protected void _init() {
        this.tfBackground = new JTextField();
        this.tfBackground.setText(jEdit.getProperty("background.file"));
        this.btnBackground = new JButton(jEdit.getProperty("options.background.choose-file"));
        this.btnBackground.addActionListener(new FileActionHandler());
        JPanel filePanel = new JPanel(new BorderLayout(5, 0));
        filePanel.add(this.tfBackground, BorderLayout.CENTER);
        filePanel.add(this.btnBackground, BorderLayout.EAST);
        addComponent(jEdit.getProperty("options.background.file"), filePanel);
        imagePosition = new JComboBox(POSITION_LABELS);
        String position = jEdit.getProperty("background.position", "tile");
        for (int i = 0; i < IMAGE_POSITIONS.length; i++) {
            if (position.equals(IMAGE_POSITIONS[i])) {
                imagePosition.setSelectedIndex(i);
            }
        }
        addComponent(jEdit.getProperty("options.background.image-position"), imagePosition);
        this.blend = new JCheckBox(jEdit.getProperty("options.background.blend"), jEdit.getBooleanProperty("background.blend", false));
        this.blend.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                boolean selected = BackgroundOptionPane.this.blend.isSelected();
                BackgroundOptionPane.this.blendColor.setEnabled(selected);
                BackgroundOptionPane.this.blendAlpha.setEnabled(selected);
            }
        });
        addComponent(this.blend);
        this.blendColor = this.createColorButton("background.blend-color", jEdit.getColorProperty("view.bgColor", Color.white));
        this.blendColor.setEnabled(this.blend.isSelected());
        addComponent(jEdit.getProperty("options.background.blend-color"), this.blendColor);
        int alpha = jEdit.getIntegerProperty("background.blend-alpha", 127);
        if (alpha < 0) {
            alpha = 0;
        }
        if (alpha > 255) {
            alpha = 255;
        }
        this.blendAlpha = new JSlider(0, 255, alpha);
        this.blendAlpha.setEnabled(this.blend.isSelected());
        this.blendAlpha.setPaintTicks(true);
        this.blendAlpha.setMajorTickSpacing(32);
        Hashtable blendAlphaLabels = new Hashtable();
        blendAlphaLabels.put(new Integer(0), new JLabel("Transparent"));
        blendAlphaLabels.put(new Integer(127), new JLabel("Translucent"));
        blendAlphaLabels.put(new Integer(255), new JLabel("Opaque"));
        this.blendAlpha.setPaintLabels(true);
        this.blendAlpha.setLabelTable(blendAlphaLabels);
        addComponent(jEdit.getProperty("options.background.blend-alpha"), this.blendAlpha);
    }

    protected void _save() {
        jEdit.setProperty("background.file", this.tfBackground.getText());
        jEdit.setBooleanProperty("background.blend", this.blend.isSelected());
        jEdit.setColorProperty("background.blend-color", this.blendColor.getBackground());
        jEdit.setIntegerProperty("background.blend-alpha", this.blendAlpha.getValue());
        jEdit.setProperty("background.position", IMAGE_POSITIONS[imagePosition.getSelectedIndex()]);
    }

    private class FileActionHandler implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            JTextField field = BackgroundOptionPane.this.tfBackground;
            String[] paths = GUIUtilities.showVFSFileDialog(jEdit.getActiveView(), MiscUtilities.getParentOfPath(field.getText()), JFileChooser.OPEN_DIALOG, false);
            if (paths != null) {
                field.setText(paths[0]);
            }
        }
    }

    private JButton createColorButton(String property, Color defaultColor) {
        JButton b = new JButton(" ");
        b.setBackground(jEdit.getColorProperty(property, defaultColor));
        b.addActionListener(new ColorActionHandler());
        b.setRequestFocusEnabled(false);
        return b;
    }

    private class ColorActionHandler implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            JButton button = (JButton) evt.getSource();
            Color c = JColorChooser.showDialog(BackgroundOptionPane.this, jEdit.getProperty("colorChooser.title"), button.getBackground());
            if (c != null) {
                button.setBackground(c);
            }
        }
    }
}
