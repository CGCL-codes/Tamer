package net.sf.jmoney.entrytable;

import net.sf.jmoney.JMoneyPlugin;
import net.sf.jmoney.model2.Entry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

/**
 * This class represents a button that appears in each row and, when pressed,
 * drops down a shell showing details of the other entries.
 * 
 * This class is similar to OtherEntriesBlock but there are no columns in the
 * main table for the properties in the other entries, the shell has its own
 * columns with their own headers inside the shell.
 * 
 * @author Nigel Westbury
 */
public class OtherEntriesButton extends CellBlock<EntryData, EntryRowControl> {

    private static final int DROPDOWN_BUTTON_WIDTH = 15;

    private static Image downArrowImage = null;

    private Block<Entry, SplitEntryRowControl> otherEntriesRootBlock;

    public OtherEntriesButton(Block<Entry, SplitEntryRowControl> otherEntriesRootBlock) {
        super(DROPDOWN_BUTTON_WIDTH, 0);
        this.otherEntriesRootBlock = otherEntriesRootBlock;
    }

    @Override
    public ICellControl<EntryData> createCellControl(final EntryRowControl parent) {
        RowSelectionTracker<SplitEntryRowControl> rowTracker = new RowSelectionTracker<SplitEntryRowControl>();
        FocusCellTracker cellTracker = new FocusCellTracker();
        if (downArrowImage == null) {
            ImageDescriptor descriptor = JMoneyPlugin.createImageDescriptor("icons/comboArrow.gif");
            downArrowImage = descriptor.createImage();
        }
        return new ButtonCellControl(parent, downArrowImage, "Show the other entries in this transaction.") {

            @Override
            protected void run(EntryRowControl rowControl) {
                final OtherEntriesShell shell = new OtherEntriesShell(parent.getShell(), SWT.ON_TOP, rowControl.getUncommittedEntryData(), otherEntriesRootBlock, false);
                Display display = parent.getDisplay();
                Rectangle rect = display.map(parent, null, this.getControl().getBounds());
                shell.open(rect);
            }
        };
    }

    @Override
    public void createHeaderControls(Composite parent) {
        new Label(parent, SWT.NONE);
    }
}
