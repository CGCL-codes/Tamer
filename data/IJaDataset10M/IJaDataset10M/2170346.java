package org.jcryptool.visual.ECDH.ui.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class ECDHView extends ViewPart {

    private Composite parent;

    /**
	 * The constructor.
	 */
    public ECDHView() {
    }

    /**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
    public void createPartControl(Composite parent) {
        this.parent = parent;
        final ScrolledComposite sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);
        ECDHComposite c = new ECDHComposite(sc, SWT.NONE, this);
        sc.setContent(c);
        sc.setMinSize(c.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent.getShell(), "org.jcryptool.visual.ecdh.ecdhview");
    }

    /**
	 * Passing the focus request to the viewer's control.
	 */
    @Override
    public void setFocus() {
        parent.setFocus();
    }
}
