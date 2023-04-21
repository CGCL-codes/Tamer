package org.jdmp.sigmen.client.menu;

import javax.swing.JInternalFrame;

public abstract class UpdatableJInternalFrame extends JInternalFrame implements Updatable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5058965511902492576L;

    public UpdatableJInternalFrame() {
    }

    public UpdatableJInternalFrame(String title) {
        super(title);
    }

    public UpdatableJInternalFrame(String title, boolean resizable) {
        super(title, resizable);
    }

    public UpdatableJInternalFrame(String title, boolean resizable, boolean closable) {
        super(title, resizable, closable);
    }

    public UpdatableJInternalFrame(String title, boolean resizable, boolean closable, boolean maximizable) {
        super(title, resizable, closable, maximizable);
    }

    public UpdatableJInternalFrame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
    }
}
