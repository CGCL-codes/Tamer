package net.assimilator.qos.measurable.memory;

import java.text.*;
import net.assimilator.watch.CalculableDisplayAttributes;
import net.assimilator.watch.DefaultCalculableView;
import net.assimilator.watch.FontDescriptor;

/**
 * View elements for a CalculableMemory
 */
public class CalculableMemoryView extends DefaultCalculableView {

    private static final CalculableDisplayAttributes defaultDisplayAttributes = new CalculableDisplayAttributes("Memory", new FontDescriptor("Dialog", java.awt.Font.PLAIN, 10), 0, (NumberFormat) new DecimalFormat("##0.000"), "Samples", new FontDescriptor("Dialog", java.awt.Font.PLAIN, 10), 0, (NumberFormat) new DecimalFormat("####0"), "Java Virtual Machine Memory", new FontDescriptor("Dialog", java.awt.Font.BOLD, 12), CalculableMemory.class);

    /** 
     * Creates CalculableMemoryView 
     */
    public CalculableMemoryView() {
        super(CalculableMemoryView.defaultDisplayAttributes);
    }

    /** 
     * Creates new CalculableMemoryView 
     *
     * @param calcDisplayAttrs The CalculableDisplayAttributes used to format the graph
     */
    public CalculableMemoryView(CalculableDisplayAttributes calcDisplayAttrs) {
        setCalculableDisplayAttributes(calcDisplayAttrs);
    }

    protected double getTopLineValue() {
        return (1.0);
    }

    protected double getBottomLineValue() {
        return (0.0);
    }
}
