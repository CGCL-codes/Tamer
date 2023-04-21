package org.chefxp.controls;

import java.util.Iterator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.ftp4che.reply.Reply;

public class FTPReplyLogger extends Composite {

    private List logger;

    public FTPReplyLogger(FTPTabItem parent, int style) {
        super(parent, style);
        FillLayout fillLayout = new FillLayout();
        fillLayout.type = SWT.HORIZONTAL;
        this.setLayout(fillLayout);
        logger = new List(this, SWT.V_SCROLL | SWT.H_SCROLL);
        logger.pack();
        this.pack();
    }

    public void addReplyEntry(Reply reply) {
        java.util.List lines = reply.getLines();
        for (Iterator it = lines.iterator(); it.hasNext(); ) {
            logger.add((String) it.next());
        }
    }

    public void clearList() {
        logger.removeAll();
    }
}
