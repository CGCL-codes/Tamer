package org.sqlexp.controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * Floating control owning its own dialog.
 * @author Matthieu Réjou
 */
public abstract class ExpFloatingControl extends ExpControl {

    private final Shell dialog, parentShell;

    /**
	 * @param parent control
	 * @param style of associated shell
	 */
    public ExpFloatingControl(final Control parent, final int style) {
        super(new Shell(parent.getShell(), style), new FillLayout());
        this.dialog = getShell();
        this.parentShell = parent.getShell();
        this.dialog.setLayout(new FillLayout());
    }

    /**
	 * @return the dialog
	 */
    protected final Shell getDialog() {
        return this.dialog;
    }

    /**
	 * @return parent shell
	 */
    protected final Shell getParentShell() {
        return this.parentShell;
    }

    /**
	 * Define a default location.<br>
	 * <code>Size</code> have to be defined before.
	 * @param align TOP or BOTTOM | LEAD or TRAIL
	 * {@link SWT}
	 */
    protected final void setLocation(final int align) {
        Point loc = this.dialog.getLocation();
        Point size = this.dialog.getSize();
        Point parentLoc = this.parentShell.getLocation();
        Point parentSize = this.parentShell.getSize();
        if ((align & SWT.TOP) != 0) {
            loc.y = parentLoc.y;
        } else if ((align & SWT.BOTTOM) != 0) {
            loc.y = parentLoc.y + parentSize.y - size.y;
        }
        if ((align & SWT.LEAD) != 0) {
            loc.x = parentLoc.x;
        } else if ((align & SWT.TRAIL) != 0) {
            loc.x = parentLoc.x + parentSize.x - size.x;
        }
        setLocation(loc);
    }

    /**
	 * Enable disposal when focus is lost.
	 */
    protected final void enableDisposeOnLostFocus() {
        this.dialog.addShellListener(new ShellAdapter() {

            @Override
            public void shellDeactivated(final ShellEvent e) {
                ExpFloatingControl.this.dispose();
            }
        });
    }

    @Override
    public void dispose() {
        if (!this.dialog.isDisposed()) {
            this.dialog.close();
        }
        super.dispose();
    }

    /**
	 * @return true if visible
	 * @see org.eclipse.swt.widgets.Shell#isVisible()
	 */
    @Override
    public boolean isVisible() {
        return this.dialog.isVisible();
    }

    /**
	 * @param x
	 * @param y
	 * @see org.eclipse.swt.widgets.Control#setLocation(int, int)
	 */
    @Override
    public void setLocation(final int x, final int y) {
        this.dialog.setLocation(x, y);
    }

    /**
	 * @param location
	 * @see org.eclipse.swt.widgets.Control#setLocation(org.eclipse.swt.graphics.Point)
	 */
    @Override
    public void setLocation(final Point location) {
        this.dialog.setLocation(location);
    }

    /**
	 * @param width
	 * @param height
	 * @see org.eclipse.swt.widgets.Control#setSize(int, int)
	 */
    @Override
    public void setSize(final int width, final int height) {
        this.dialog.setSize(width, height);
    }

    /**
	 * @param size
	 * @see org.eclipse.swt.widgets.Control#setSize(org.eclipse.swt.graphics.Point)
	 */
    @Override
    public void setSize(final Point size) {
        this.dialog.setSize(size);
    }

    /**
	 * @return location
	 * @see org.eclipse.swt.widgets.Shell#getLocation()
	 */
    @Override
    public Point getLocation() {
        return this.dialog.getLocation();
    }

    /**
	 * @return size
	 * @see org.eclipse.swt.widgets.Shell#getSize()
	 */
    @Override
    public Point getSize() {
        return this.dialog.getSize();
    }

    /**
	 * @param size
	 * @see org.eclipse.swt.widgets.Shell#setMinimumSize(org.eclipse.swt.graphics.Point)
	 */
    public void setMinimumSize(final Point size) {
        this.dialog.setMinimumSize(size);
    }

    @Override
    public void pack() {
        this.dialog.pack();
    }

    /**
	 * @param visible
	 * @see org.eclipse.swt.widgets.Shell#setVisible(boolean)
	 */
    @Override
    public void setVisible(final boolean visible) {
        this.dialog.setVisible(visible);
    }

    /**
	 * 
	 * @see org.eclipse.swt.widgets.Shell#open()
	 */
    public void open() {
        this.dialog.open();
    }
}
