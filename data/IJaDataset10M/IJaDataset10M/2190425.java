package graphmatcher.gui.optionpanel;

import graphmatcher.gui.LabeledTextField;
import graphmatcher.matcher.MatchingOptions;
import graphmatcher.matcher.shapecontext.ShapeContextGraphMatcher;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;

public class ShapeContextOptionPanel extends IOptionPanel {

    private LabeledTextField numberOfAngleBinsField, numberOfRadiusBinsField;

    private JCheckBox rotationCheckBox;

    public ShapeContextOptionPanel() {
        setBorder(BorderFactory.createTitledBorder("Optionen"));
        setLayout(new GridLayout(0, 1));
        numberOfAngleBinsField = new LabeledTextField("#Kreissektoren", "8", true);
        add(numberOfAngleBinsField);
        numberOfRadiusBinsField = new LabeledTextField("#Ringe", "4", true);
        add(numberOfRadiusBinsField);
        rotationCheckBox = new JCheckBox("Rotation ber�cksichtigen", true);
        add(rotationCheckBox);
    }

    @Override
    public String getMatcherID() {
        return ShapeContextGraphMatcher.matcherID;
    }

    @Override
    public MatchingOptions getMatchingOptions() {
        MatchingOptions options = new MatchingOptions(ShapeContextGraphMatcher.matcherID);
        options.numberOfAngleBins = Integer.parseInt(numberOfAngleBinsField.getText());
        options.numberOfRadiusBins = Integer.parseInt(numberOfRadiusBinsField.getText());
        options.setNoRotation(!rotationCheckBox.isSelected());
        return options;
    }

    @Override
    public String getDefaultFileName() {
        return "shapecontextMatcher.txt";
    }

    @Override
    public void setEditable(boolean editable) {
        numberOfAngleBinsField.setEditable(editable);
        numberOfRadiusBinsField.setEditable(editable);
        rotationCheckBox.setEnabled(editable);
    }
}
