package net.openchrom.chromatogram.msd.ui.swt.components.baseline;

import org.eclipse.swt.widgets.Composite;
import net.openchrom.chromatogram.msd.ui.swt.components.AbstractLineSeriesUI;

/**
 * Use this class to show different views of baselines.
 * 
 * @author eselmeister
 */
public abstract class AbstractViewBaselineUI extends AbstractLineSeriesUI {

    public AbstractViewBaselineUI(Composite parent, int style) {
        super(parent, style);
    }
}
