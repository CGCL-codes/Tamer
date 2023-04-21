package gnagck.block.attributes;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import gnagck.util.DoubleDialog;
import javax.swing.JComponent;

/**
 * Attracts Actors to the Block.
 * @author royer
 *
 */
public class Attract extends BlockAttribute {

    /**
	 * The strength of the attraction
	 */
    private double strength;

    /**
	 * The minimum strength of the attraction
	 */
    private final double minStrength = 0.0;

    /**
	 * The maximum strength of the attraction
	 */
    private final double maxStrength = 1.0;

    /**
	 * Constructor.
	 */
    public Attract() {
        strength = (minStrength + maxStrength) / 2.0;
    }

    @Override
    public BlockAttribute clone() {
        Attract a = new Attract();
        a.strength = strength;
        return a;
    }

    @Override
    public void doEdit(JComponent parent) {
        DoubleDialog dlg = new DoubleDialog("Strength", strength, minStrength, maxStrength, 0.1);
        dlg.setTitle("Attract...");
        dlg.setModal(true);
        dlg.setVisible(true);
        if (!dlg.isCancelled()) {
            strength = dlg.getValue();
        }
    }

    @Override
    public void write(DataOutput dout) throws IOException {
        dout.writeDouble(strength);
        dout.writeDouble(minStrength);
        dout.writeDouble(maxStrength);
    }

    @Override
    public void read(DataInput din) throws IOException {
        strength = din.readDouble();
        din.readDouble();
        din.readDouble();
    }
}
