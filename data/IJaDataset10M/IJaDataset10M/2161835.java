package au.vermilion.desktop.composer.GUI;

import au.vermilion.PC.relativelayout.RelativeLayout;
import au.vermilion.PC.relativelayout.RelativePlacement;
import static au.vermilion.Vermilion.logger;
import au.vermilion.composer.PatternCache;
import au.vermilion.composer.PatternObj;
import au.vermilion.desktop.MachineObj;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * A self-building interface window that shows a pattern's properties.
 */
public class JPatternPropertiesFrame extends JDialog implements ActionListener {

    /**
     * Points to the pattern in the composition which we are changing the
     * properties for.
     */
    private PatternObj pattern;

    private MachineObj machine;

    private PatternCache composer;

    private JTextField nameField;

    private JTextField lengthField;

    private JTextField sigField;

    protected JButton okButton;

    private JButton cancelButton;

    public boolean changesMade;

    /**
     * Creates a frame showing basic controls for handling the parameters on
     * a pattern.
     */
    public JPatternPropertiesFrame(PatternCache c, MachineObj m, PatternObj p, boolean isMaster) {
        pattern = p;
        machine = m;
        composer = c;
        RelativeLayout rel = new RelativeLayout(null);
        setLayout(rel);
        Dimension size = RelativeLayout.getSuggestedSize(0.25f, 0.2f);
        Container panel = getContentPane();
        JLabel l = new JLabel("Title:");
        panel.add(l, new RelativePlacement(0, 0, 333, 250));
        rel.registerForFontSizeChanges(l);
        nameField = new JTextField();
        nameField.setText(pattern.patternName);
        panel.add(nameField, new RelativePlacement(333, 0, 666, 250));
        rel.registerForFontSizeChanges(nameField);
        l = new JLabel("Length:");
        panel.add(l, new RelativePlacement(0, 250, 333, 250));
        rel.registerForFontSizeChanges(l);
        lengthField = new JTextField();
        lengthField.setText(String.valueOf(pattern.patternLength));
        panel.add(lengthField, new RelativePlacement(333, 250, 666, 250));
        rel.registerForFontSizeChanges(lengthField);
        l = new JLabel("Time Sig:");
        panel.add(l, new RelativePlacement(0, 500, 333, 250));
        rel.registerForFontSizeChanges(l);
        sigField = new JTextField();
        sigField.setText(String.valueOf(pattern.timeSig));
        panel.add(sigField, new RelativePlacement(333, 500, 666, 250));
        rel.registerForFontSizeChanges(sigField);
        cancelButton = new JButton("Cancel");
        panel.add(cancelButton, new RelativePlacement(333, 750, 333, 250));
        cancelButton.addActionListener(this);
        rel.registerForFontSizeChanges(cancelButton);
        okButton = new JButton("OK");
        panel.add(okButton, new RelativePlacement(666, 750, 333, 250));
        okButton.addActionListener(this);
        rel.registerForFontSizeChanges(okButton);
        setMinimumSize(size);
        setSize(size);
    }

    /**
     * Writes the values entered on the window into the pattern pointed to
     * by the constructor.
     */
    public void saveData() {
        try {
            String patternName = nameField.getText();
            int patternLength = Integer.parseInt(lengthField.getText());
            if (patternLength > 10000) patternLength = 10000;
            int timeSig = Integer.parseInt(sigField.getText());
            changesMade = (pattern.patternName.equals(patternName) == false || pattern.patternLength != patternLength || pattern.timeSig != timeSig);
            if (changesMade) composer.setPatternProperties(machine, pattern, patternName, patternLength, timeSig);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Exception updating pattern properties", ex);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okButton) saveData();
        setVisible(false);
    }

    private static final long serialVersionUID = -1L;
}
