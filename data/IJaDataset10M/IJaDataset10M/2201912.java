package net.sourceforge.nattable.examples.tutorial.painter;

import net.sourceforge.nattable.NatTable;
import net.sourceforge.nattable.examples.AbstractNatExample;
import net.sourceforge.nattable.examples.runner.StandaloneNatExampleRunner;
import net.sourceforge.nattable.painter.layer.NatGridLayerPainter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class NatGridLayerPainterExample extends AbstractNatExample {

    public static void main(String[] args) throws Exception {
        StandaloneNatExampleRunner.run(800, 600, new NatGridLayerPainterExample());
    }

    @Override
    public String getName() {
        return "NatTable with grid painting of remainder space";
    }

    public Control createExampleControl(Composite parent) {
        NatTable natTable = new NatTable(parent);
        NatGridLayerPainter layerPainter = new NatGridLayerPainter(natTable);
        natTable.setLayerPainter(layerPainter);
        return natTable;
    }
}
