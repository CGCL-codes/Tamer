package net.sourceforge.nattable.examples.demo;

import net.sourceforge.nattable.NatTable;
import net.sourceforge.nattable.examples.AbstractNatExample;
import net.sourceforge.nattable.examples.runner.StandaloneNatExampleRunner;
import net.sourceforge.nattable.layer.stack.DummyGridLayerStack;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class SmallDefaultNatTableExample extends AbstractNatExample {

    public static void main(String[] args) throws Exception {
        StandaloneNatExampleRunner.run(new SmallDefaultNatTableExample());
    }

    public Control createExampleControl(Composite parent) {
        return new NatTable(parent, new DummyGridLayerStack(5, 10));
    }
}
