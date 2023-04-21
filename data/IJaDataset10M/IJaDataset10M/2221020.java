package bio.molecule.control;

import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import bio.molecule.MolecularSpace;
import bio.molecule.legend.LegendPanel;

public class ColorControls extends JPanel {

    private static final long serialVersionUID = 1L;

    private JRadioButton[] colors = new JRadioButton[4];

    private ButtonGroup colorsGroup;

    private LegendPanel legend;

    private MolecularSpace mspace;

    public ColorControls() {
        setBorder(BorderFactory.createTitledBorder("Classification"));
        setLayout(new GridLayout(3, 2));
        colors[0] = new JRadioButton("Charge");
        colors[0].setName("C");
        add(colors[0]);
        colors[1] = new JRadioButton("Hydrophobicity");
        colors[1].setName("H");
        add(colors[1]);
        colors[2] = new JRadioButton("Polarity");
        colors[2].setName("P");
        add(colors[2]);
        colors[3] = new JRadioButton("Default");
        colors[3].setSelected(true);
        colors[3].setName("D");
        add(colors[3]);
        colorsGroup = new ButtonGroup();
        colorsGroup.add(colors[0]);
        colorsGroup.add(colors[1]);
        colorsGroup.add(colors[2]);
        colorsGroup.add(colors[3]);
    }

    public JRadioButton[] getColors() {
        return colors;
    }

    public void setColors(JRadioButton[] colors) {
        this.colors = colors;
    }

    public ButtonGroup getColorsGroup() {
        return colorsGroup;
    }

    public void setColorsGroup(ButtonGroup colorsGroup) {
        this.colorsGroup = colorsGroup;
    }

    public void setLegendPanel(LegendPanel panel) {
        this.legend = panel;
    }

    public void addColorControlListener(ColorControlListener colorcontrolListener) {
        for (int i = 0; i < colors.length; i++) {
            colors[i].addItemListener(colorcontrolListener);
        }
    }

    public LegendPanel getLegend() {
        return legend;
    }

    public void setLegend(LegendPanel legend) {
        this.legend = legend;
    }

    public MolecularSpace getMolecularSpace() {
        return mspace;
    }

    public void setMolecularSpace(MolecularSpace mspace) {
        this.mspace = mspace;
    }
}
