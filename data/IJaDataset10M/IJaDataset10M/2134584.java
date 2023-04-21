package net.sourceforge.nattable.examples.misc.elemental;

import net.sourceforge.nattable.NatTable;
import net.sourceforge.nattable.examples.AbstractNatExample;
import net.sourceforge.nattable.examples.runner.StandaloneNatExampleRunner;
import net.sourceforge.nattable.grid.data.DummyBodyDataProvider;
import net.sourceforge.nattable.layer.DataLayer;
import net.sourceforge.nattable.selection.SelectionLayer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class SelectionDataLayerExample extends AbstractNatExample {

    public static void main(String[] args) throws Exception {
        StandaloneNatExampleRunner.run(new SelectionDataLayerExample());
    }

    public Control createExampleControl(Composite parent) {
        return new NatTable(parent, new SelectionLayer(new DataLayer(new DummyBodyDataProvider(1000000, 1000000))));
    }
}
