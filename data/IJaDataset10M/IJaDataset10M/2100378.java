package apollo.dataadapter;

import java.util.Vector;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import apollo.util.NumericKeyFilter;
import apollo.gui.ChromosomeField;
import apollo.gui.Style;

class LocationGAMEPanel extends GAMEPanel {

    private static final String label = "Location";

    private ChromosomeField chrField;

    private JTextField startField;

    private JTextField endField;

    LocationGAMEPanel() {
        super("Location", label, DataInputType.BASE_PAIR_RANGE);
    }

    protected void buildGUI() {
        JLabel chromLabel = makeJLabelWithFont("Chromosome ");
        Style gameStyle = apollo.gui.Config.getStyle("apollo.dataadapter.GAMEAdapter");
        Vector chroms = null;
        if (gameStyle != null) chroms = gameStyle.getChromosomes();
        chrField = new ChromosomeField(chroms);
        chrField.getComponent().setPreferredSize(new Dimension(70, 0));
        chrField.getComponent().setMaximumSize(new Dimension(0, 25));
        JLabel startLabel = makeJLabelWithFont("Start ");
        startField = makeNumericTextField();
        JLabel endLabel = makeJLabelWithFont("End ");
        endField = makeNumericTextField();
        Box box = new Box(BoxLayout.X_AXIS);
        box.add(chromLabel);
        box.add(chrField.getComponent());
        box.add(startLabel);
        box.add(startField);
        box.add(endLabel);
        box.add(endField);
        box.add(Box.createHorizontalGlue());
        getPanel().add(box);
    }

    protected String getCurrentInput() {
        return "Chr " + chrField.getChromosome() + " " + startField.getText() + " " + endField.getText();
    }

    protected void setEditorsHistory(Vector history) {
    }

    private JTextField makeNumericTextField() {
        JTextField jtf = new JTextField();
        jtf.addKeyListener(NumericKeyFilter.getFilter());
        jtf.setPreferredSize(new Dimension(75, 0));
        jtf.setMaximumSize(new Dimension(0, 22));
        return jtf;
    }

    protected JLabel makeJLabelWithFont(String label) {
        JLabel jl = super.makeJLabelWithFont(label);
        jl.setPreferredSize(new Dimension(110, 0));
        jl.setHorizontalAlignment(SwingConstants.RIGHT);
        jl.setVerticalAlignment(SwingConstants.CENTER);
        return jl;
    }
}
