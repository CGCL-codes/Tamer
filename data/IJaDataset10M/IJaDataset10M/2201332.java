package org.freelords.ui.skin;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.freelords.ui.WrappedControl;
import org.freelords.util.ImagePainter;

/** Provides an image border around a composite.
  *
  * <p>
  * This is done by taking a parent widget, and tiling it with 3x3 subwidgets. The edge
  * widgets are all set up as canvas that display parts of the border, while the middle widget
  * is for the framed content.
  * </p>
  *
  * @author James Andrews
  */
public class ImageBorder implements WrappedControl {

    /** The parental widget */
    private Composite rparent;

    /** The middle (content) widget */
    private Composite middle;

    /** Returns the upper parent of the widget. */
    public Composite getControl() {
        return rparent;
    }

    /** Sets up the border.
	  * 
	  * The code is a bit ugly, and involves some dark magic. If you are interested, you can
	  * cross-check it with the code in {@link org.freelords.ui.skin.BorderCache} to see that it
	  * works.
	  *
	  * @param parentX the parent that holds all the border widgets as children
	  * @param borderInfo a BorderCache that provides the border images.
	  */
    public ImageBorder(Composite parentX, BorderCache borderInfo) {
        rparent = new Composite(parentX, SWT.NONE);
        rparent.setBackground(parentX.getDisplay().getSystemColor(SWT.COLOR_BLACK));
        rparent.setBackgroundMode(SWT.INHERIT_FORCE);
        rparent.setLayout(new FillLayout());
        Composite parent = new Composite(rparent, SWT.NONE);
        GridLayout gl = new GridLayout(3, false);
        gl.horizontalSpacing = 0;
        gl.verticalSpacing = 0;
        parent.setLayout(gl);
        GridData gd;
        gd = new GridData(borderInfo.getWidth(), borderInfo.getHeight());
        gd.grabExcessHorizontalSpace = false;
        gd.grabExcessVerticalSpace = false;
        createBorderBit(new Canvas(parent, SWT.NONE), new ImagePainter(borderInfo.getVerticalImage(), new Rectangle(0, 0, borderInfo.getWidth(), borderInfo.getHeight()), false, false, SWT.RIGHT, SWT.BOTTOM), gd);
        gd = new GridData(SWT.FILL, SWT.BOTTOM, true, false);
        gd.heightHint = borderInfo.getHeight();
        createBorderBit(new Canvas(parent, SWT.NONE), new ImagePainter(borderInfo.getHorizontalImage(), new Rectangle(0, 0, borderInfo.getHorizontalStretch(), borderInfo.getHeight()), true, false, SWT.CENTER, SWT.BOTTOM), gd);
        gd = new GridData(borderInfo.getWidth(), borderInfo.getHeight());
        gd.grabExcessHorizontalSpace = false;
        gd.grabExcessVerticalSpace = false;
        createBorderBit(new Canvas(parent, SWT.NONE), new ImagePainter(borderInfo.getVerticalImage(), new Rectangle(borderInfo.getWidth(), 0, borderInfo.getWidth(), borderInfo.getHeight()), false, false), gd);
        gd = new GridData(SWT.RIGHT, SWT.FILL, false, true);
        gd.widthHint = borderInfo.getWidth();
        createBorderBit(new Canvas(parent, SWT.NONE), new ImagePainter(borderInfo.getVerticalImage(), new Rectangle(0, borderInfo.getHeight(), borderInfo.getWidth(), borderInfo.getVerticalStretch()), false, true), gd);
        middle = new Composite(parent, SWT.NONE);
        middle.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        middle.setLayout(new FillLayout());
        gd = new GridData(SWT.LEFT, SWT.FILL, false, true);
        gd.widthHint = borderInfo.getHeight();
        createBorderBit(new Canvas(parent, SWT.NONE), new ImagePainter(borderInfo.getVerticalImage(), new Rectangle(borderInfo.getWidth(), borderInfo.getHeight(), borderInfo.getWidth(), borderInfo.getVerticalStretch()), false, true), gd);
        gd = new GridData(borderInfo.getWidth(), borderInfo.getHeight());
        gd.grabExcessHorizontalSpace = false;
        gd.grabExcessVerticalSpace = false;
        createBorderBit(new Canvas(parent, SWT.NONE), new ImagePainter(borderInfo.getVerticalImage(), new Rectangle(0, borderInfo.getVerticalStretch() + borderInfo.getHeight(), borderInfo.getWidth(), borderInfo.getHeight()), false, false), gd);
        gd = new GridData(SWT.FILL, SWT.TOP, true, false);
        gd.heightHint = borderInfo.getHeight();
        createBorderBit(new Canvas(parent, SWT.NONE), new ImagePainter(borderInfo.getHorizontalImage(), new Rectangle(0, borderInfo.getHeight(), borderInfo.getHorizontalStretch(), borderInfo.getHeight()), true, false), gd);
        gd = new GridData(borderInfo.getWidth(), borderInfo.getHeight());
        gd.grabExcessHorizontalSpace = false;
        gd.grabExcessVerticalSpace = false;
        createBorderBit(new Canvas(parent, SWT.NONE), new ImagePainter(borderInfo.getVerticalImage(), new Rectangle(borderInfo.getWidth(), borderInfo.getVerticalStretch() + borderInfo.getHeight(), borderInfo.getWidth(), borderInfo.getHeight()), false, false), gd);
    }

    /** Returns the middle widget to fill with framed content. */
    public Composite getMiddle() {
        return middle;
    }

    /** */
    private void createBorderBit(Canvas canvas, ImagePainter painter, GridData gridData) {
        canvas.addPaintListener(painter);
        canvas.setLayoutData(gridData);
    }
}
