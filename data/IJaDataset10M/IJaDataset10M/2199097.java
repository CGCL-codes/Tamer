package net.sourceforge.nattable.examples.examples._102_Configuration;

import net.sourceforge.nattable.NatTable;
import net.sourceforge.nattable.config.DefaultNatTableStyleConfiguration;
import net.sourceforge.nattable.examples.AbstractNatExample;
import net.sourceforge.nattable.examples.runner.StandaloneNatExampleRunner;
import net.sourceforge.nattable.ui.menu.HeaderMenuConfiguration;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class Using_a_predefined_configuration_object extends AbstractNatExample {

    public static void main(String[] args) throws Exception {
        StandaloneNatExampleRunner.run(new Using_a_predefined_configuration_object());
    }

    @Override
    public String getDescription() {
        return "NatTable is highly configurable. The configuration objects also very modular. NatTable has default configuration that " + "comes attached to its various layer implementations, so that in many cases all you need to do is assemble a layer stack " + "and all the key/mouse bindings, styling, etc. that you need to interact with and visualize the functionality within your " + "layer assembly will automatically be there for you to use.\n" + "\n" + "If you want to augment, customize, or override the default configuration you can do this by adding an IConfiguration to " + "a layer in your layer stack. IConfiguration objects allow you to encapsulate three kinds of configurations:\n" + "  * UI binding configuration - keyboard and mouse bindings\n" + "  * ConfigRegistry configuration - configuration associated with display modes and config labels (e.g. styles, cell " + " painters, cell editors, etc.)\n" + "  * Layer configuration - configuration pertaining to the entire layer (e.g. layer painters, persistors, etc.)\n" + "\n" + "This example shows how to apply a pre-canned configuration to a NatTable instance. It attaches a right-click popup menu " + "to the column header. Note that in order to add a configuration to NatTable, you have to set the autoconfigure parameter " + "in the NatTable constructor to false and then you must explicitly call NatTable.configure() after you have added your " + "configurations. Otherwise NatTable will automatically call configure() on itself during construction.";
    }

    public Control createExampleControl(Composite parent) {
        NatTable natTable = new NatTable(parent, false);
        natTable.addConfiguration(new DefaultNatTableStyleConfiguration());
        natTable.addConfiguration(new HeaderMenuConfiguration(natTable));
        natTable.configure();
        return natTable;
    }
}
