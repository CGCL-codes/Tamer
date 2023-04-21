package net.sourceforge.scrollrack;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;

public class ViewCardDialog {

    public ViewCardDialog(Shell parent, CardInfo info, Gallery gallery) {
        Shell dialog = new Shell(parent, SWT.SHELL_TRIM);
        dialog.setText("View Card");
        FillLayout layout = new FillLayout();
        dialog.setLayout(layout);
        if (info.art == null) {
            gallery.get_cardart(info);
        }
        CardCanvas canvas = new CardCanvas(dialog, 0, gallery.get_painter());
        canvas.set_cardinfo(info);
        dialog.pack();
        dialog.open();
    }
}
