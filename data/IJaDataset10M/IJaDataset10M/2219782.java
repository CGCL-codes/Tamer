package hu.scytha.comparator;

import hu.scytha.common.LocalFile;
import hu.scytha.main.Settings;
import org.eclipse.swt.widgets.Display;

public class Test {

    /**
    * @param args
    */
    public static void main(String[] args) {
        Display display = Display.getDefault();
        Settings.init();
        LocalFile f1 = new LocalFile("/home/beroba/test/fonts.conf");
        LocalFile f2 = new LocalFile("/home/beroba/test/fonts2.conf");
        LineComparator sLeft = new LineComparator(f1, true);
        LineComparator sRight = new LineComparator(f2, true);
        RangeDifference[] diff = RangeDifferencer.findDifferences(sLeft, sRight);
        ContentComparatorViewer dlg = new ContentComparatorViewer(f1, f2, diff);
        dlg.setBlockOnOpen(true);
        dlg.open();
        if (!display.readAndDispatch()) display.sleep();
        display.dispose();
    }
}
