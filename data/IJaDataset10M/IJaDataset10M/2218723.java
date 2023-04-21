package com.peterhi.client.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import com.peterhi.beans.Role;
import com.peterhi.client.ui.constants.Images;
import com.peterhi.client.ui.constants.Strings;

/**
 * UI Utility class to standardize UI operations.
 * @author YUN TAO HAI (hytparadisee)
 *
 */
public class Util {

    public static Image getRoleImage(Role role) {
        if (role == null) {
            return Images.absent32;
        }
        switch(role) {
            case Moderator:
                return Images.moderator32;
            case Teacher:
                return Images.teacher32;
            case Student:
                return Images.student32;
            default:
                return Images.absent32;
        }
    }

    /**
	 * Center a {@link Shell} relative to it's parent {@link Shell}.
	 * If the parent {@link Shell} is not specified (i.e. <c>null</c>)
	 * then it will be centered to the current {@link Display}.
	 * @param sh The {@link Shell} to center.
	 * @param parent The parent {@link Shell}, or <c>null</c>.
	 * 
	 * @see org.eclipse.swt.widgets.Shell
	 * @see org.eclipse.swt.widgets.Display
	 */
    public static void center(Shell sh, Shell parent) {
        int parentx;
        int parenty;
        int parentWidth;
        int parentHeight;
        int shellWidth;
        int shellHeight;
        if (parent != null) {
            parentx = parent.getLocation().x;
            parenty = parent.getLocation().y;
            parentWidth = parent.getSize().x;
            parentHeight = parent.getSize().y;
        } else {
            parentx = 0;
            parenty = 0;
            parentWidth = Display.getCurrent().getBounds().width;
            parentHeight = Display.getCurrent().getBounds().height;
        }
        shellWidth = sh.getSize().x;
        shellHeight = sh.getSize().y;
        sh.setLocation(parentx + (parentWidth - shellWidth) / 2, parenty + (parentHeight - shellHeight) / 2);
    }

    /**
	 * Shorthand for resizing a {@link Shell}
	 * such that it's <c>width</c> and <c>height</c> are
	 * 3/4 of the {@link Display} respectively. And center
	 * this {@link Shell} to the center of the {@link Display}.
	 * @param sh The {@link Shell} to pack.
	 */
    public static void pack(Shell sh) {
        Display display = Display.getDefault();
        sh.setSize((int) (display.getBounds().width * 0.75f), (int) (display.getBounds().height * 0.75f));
        Util.center(sh, null);
    }

    /**
	 * Set a new background <c>Color</c> and dispose the old one.
	 * This is done by passing a <c>RGB</c> value. The old <c>Color</c> will
	 * be disposed automatically.
	 * @param c The <c>Control</c> whose background is to be changed.
	 * @param rgb The new <c>RGB</c> color.
	 * 
	 * @see org.eclipse.swt.graphics.Color
	 * @see org.eclipse.swt.graphics.RGB
	 */
    public static void setBackground(Control c, RGB rgb) {
        Color old = c.getBackground();
        Color _new = new Color(c.getDisplay(), rgb);
        c.setBackground(_new);
        old.dispose();
    }

    /**
	 * Set a new background {@link Color} and dispose the old one.
	 * This is done by passing a {@link Color} object. The old {@link Color} will
	 * be disposed automatically.
	 * @param c The {@link Control} whose background is to be changed.
	 * @param cr The new {@link Color} object.
	 * 
	 * @see org.eclipse.swt.graphics.Color
	 */
    public static void setBackground(Control c, Color cr) {
        Color old = c.getBackground();
        c.setBackground(cr);
        old.dispose();
    }

    /**
	 * Set a new background {@link Color} and dispose the old one.
	 * This is done by passing a {@link SWT} Color constant. The old {@link Color} will
	 * be disposed automatically.
	 * @param c The {@link Control} whose background is to be changed.
	 * @param swt The new {@link SWT} constant.
	 * 
	 * @see org.eclipse.swt.SWT
	 */
    public static void setBackground(Control c, int swt) {
        setBackground(c, c.getDisplay().getSystemColor(swt));
    }

    /**
	 * Set a new foreground {@link Color} and dispose the old one.
	 * This is done by passing a {@link RGB} value. The old {@link Color} will
	 * be disposed automatically.
	 * @param c The {@link Color} whose foreground is to be changed.
	 * @param rgb The new {@link RGB} color.
	 * 
	 * @see org.eclipse.swt.graphics.Color
	 * @see org.eclipse.swt.graphics.RGB
	 */
    public static void setForeground(Control c, RGB rgb) {
        Color old = c.getForeground();
        Color _new = new Color(c.getDisplay(), rgb);
        c.setForeground(_new);
        old.dispose();
    }

    /**
	 * Set a new foreground {@link Color} and dispose the old one.
	 * This is done by passing a {@link Color} object. The old {@link Color} will
	 * be disposed automatically.
	 * @param c The {@link Control} whose foreground is to be changed
	 * @param cr The new {@link Color} object
	 * 
	 * @see org.eclipse.swt.graphics.Color
	 */
    public static void setForeground(Control c, Color cr) {
        Color old = c.getForeground();
        c.setForeground(cr);
        old.dispose();
    }

    /**
	 * Set a new foreground {@link Color} and dispose the old one.
	 * This is done by passing a {@link SWT} Color constant. The old {@link Color} will
	 * be disposed automatically.
	 * @param c The {@link Control} whose foreground is to be changed.
	 * @param swt The new {@link SWT} constant.
	 * 
	 * @see org.eclipse.swt.SWT
	 */
    public static void setForeground(Control c, int swt) {
        setForeground(c, c.getDisplay().getSystemColor(swt));
    }

    /**
	 * Set a new {@link Font} and replace the old one. This is done by
	 * passing a {@link FontData} value.
	 * <br> <br>
	 * IMPORTANT: Current implementation does not dispose the
	 * old {@link Font}. This is because the JVM will halt
	 * abruptly and this is not acceptable. So expect resources
	 * not to be released.
	 * @param c The {@link Control} whose font is to be changed.
	 * @param fb The new {@link FontData} value.
	 */
    public static void setFont(Control c, FontData fb) {
        Font _new = new Font(c.getDisplay(), fb);
        c.setFont(_new);
    }

    /**
	 * Set a new {@link Font} and replace the old one. This is done by
	 * passing a <c>font name</c>, <c>font size</c>, and <c>font style</c>.
	 * <br><br>
	 * IMPORTANT: Current implementation does not dispose the
	 * old {@link Font}. This is because the JVM will halt
	 * abruptly and this is not acceptable. So expect resources
	 * not to be released.
	 * @param c The {@link Control} whose font is to be changed.
	 * @param name The font name.
	 * @param size The font size (height).
	 * @param style the font style, please use a {@link SWT} constant.
	 * 
	 * @see org.eclipse.swt.SWT
	 */
    public static void setFont(Control c, String name, int size, int style) {
        Font _new = new Font(c.getDisplay(), name, size, style);
        c.setFont(_new);
    }

    /**
	 * Try to modify a {@link Object} that is the layout data of a
	 * {@link Composite}. It will set the <c>widthHint</c> and
	 * <c>heightHint</c> of the layout data, if possible. This method
	 * does nothing if the layout data of the {@link Composite> does not
	 * have such attributes.
	 * @param c The {@link Composite} to modify.
	 * @param whint The width hint.
	 * @param hhint The height hint.
	 * 
	 * @see org.eclipse.swt.widgets.Composite
	 */
    public static void setHints(Composite c, int whint, int hhint) {
        Object ld = c.getLayoutData();
        if (ld instanceof GridData) {
            GridData gd = (GridData) ld;
            gd.widthHint = whint;
            gd.heightHint = hhint;
        } else if (ld instanceof RowData) {
            RowData rd = (RowData) ld;
            rd.width = whint;
            rd.height = hhint;
        }
    }

    /**
	 * Gets a {@link GridData} that fills the entire area.
	 * @return The {@link GridData}.
	 */
    public static GridData gridData() {
        return gridData(GridData.FILL, GridData.FILL, true, true, -1, -1);
    }

    /**
	 * Gets a {@link GridData} with specified parameters.
	 * @param align
	 * @param valign
	 * @param hfill
	 * @param vfill
	 * @param whint
	 * @param hhint
	 * @return The {@link GridData}.
	 * 
	 * @see org.eclipse.swt.layout.GridData
	 */
    public static GridData gridData(int align, int valign, boolean hfill, boolean vfill, int whint, int hhint) {
        GridData data = new GridData(align, valign, hfill, vfill);
        data.widthHint = whint;
        data.heightHint = hhint;
        return data;
    }

    /**
	 * Gets a {@link GridLayout} that has no margin, no space-between and can
	 * contain only one cell.
	 * @return The {@link GridLayout}.
	 */
    public static GridLayout gridLayout() {
        return gridLayout(0, 0, 0, 0, 1);
    }

    /**
	 * Gets a {@link GridLayout} with specified parameters.
	 * @param marginWidth
	 * @param marginHeight
	 * @param hspace
	 * @param vspace
	 * @return The {@link GridLayout}.
	 * 
	 * @see org.eclipse.swt.layout.GridLayout
	 */
    public static GridLayout gridLayout(int marginWidth, int marginHeight, int hspace, int vspace) {
        return gridLayout(marginWidth, marginHeight, hspace, vspace, 1);
    }

    /**
	 * Gets a {@link GridLayout} with specified parameters.
	 * @param marginWidth
	 * @param marginHeight
	 * @param hspace
	 * @param vspace
	 * @param numOfCol
	 * @return The {@link GridLayout}.
	 * 
	 * @see org.eclipse.swt.layout.GridLayout
	 */
    public static GridLayout gridLayout(int marginWidth, int marginHeight, int hspace, int vspace, int numOfCol) {
        GridLayout lay = new GridLayout();
        lay.marginWidth = marginWidth;
        lay.marginHeight = marginHeight;
        lay.horizontalSpacing = hspace;
        lay.verticalSpacing = vspace;
        lay.numColumns = numOfCol;
        return lay;
    }

    /**
	 * Gets a {@link StackLayout} with no margin.
	 * @return The {@link StackLayout}.
	 */
    public static StackLayout stackLayout() {
        return stackLayout(0, 0);
    }

    /**
	 * Gets a {@link StackLayout} with margins specified.
	 * @param marginWidth
	 * @param marginHeight
	 * @return The {@link StyleLayout}.
	 */
    public static StackLayout stackLayout(int marginWidth, int marginHeight) {
        StackLayout lay = new StackLayout();
        lay.marginWidth = marginWidth;
        lay.marginHeight = marginHeight;
        return lay;
    }

    /**
	 * Gets a {@link RowLayout} with no margin.
	 * @return The {@link RowLayout}.
	 */
    public static RowLayout rowLayout() {
        RowLayout lay = new RowLayout();
        lay.marginWidth = 0;
        lay.marginHeight = 0;
        lay.spacing = 0;
        lay.pack = true;
        return lay;
    }

    /**
	 * Checks whether the current operating system already has
	 * double buffer enabled. And returns whether the application
	 * should do the double buffering in its application context.
	 * This is merely a check against what the current OS is. In
	 * other words, it fetches the '<c>os.name</c>' java property
	 * and see whether this OS does automatic double buffering
	 * already. If it does, then there is no need for the application
	 * to double buffer again (where triple buffer is not really needed).
	 * <br><br>
	 * Please note that when creating {@link Widget}s or {@link Control}s. You must
	 * check this before actually creating them (i.e. construct them).
	 * This is because if a {@link Control} is created with the specified
	 * {@link SWT} bits, it cannot be modified. And if you want to double buffer
	 * a widget, you must specify the creation of your widget with
	 * {@link SWT#NO_BACKGROUND} to make it work.
	 * @return <c>true<c> if the application double buffering is required,
	 * otherwise <c>false</c>.
	 * @see org.eclipse.swt.SWT#NO_BACKGROUND
	 * @see System#getProperty(String)
	 */
    public static boolean shouldDoubleBuffer() {
        String s = System.getProperty("os.name");
        if (s.startsWith("Windows")) {
            return true;
        } else if (s.startsWith("Linux")) {
            return false;
        } else {
            return false;
        }
    }

    public static int error(Shell sh, String msg) {
        if (isDisposed(sh)) {
            return -1;
        }
        MessageBox mb = new MessageBox(sh, SWT.ICON_ERROR);
        mb.setText(Strings.Error);
        mb.setMessage(msg);
        return mb.open();
    }

    public static int info(Shell sh, String msg) {
        if (isDisposed(sh)) {
            return -1;
        }
        MessageBox mb = new MessageBox(sh, SWT.ICON_INFORMATION);
        mb.setText(Strings.Info);
        mb.setMessage(msg);
        return mb.open();
    }

    public static int yesno(Shell sh, String title, String msg) {
        if (isDisposed(sh)) {
            return -1;
        }
        MessageBox mb = new MessageBox(sh, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
        mb.setText(title);
        mb.setMessage(msg);
        return mb.open();
    }

    private static boolean isDisposed(Object c) {
        if (Display.getDefault().isDisposed()) {
            return true;
        }
        if (c instanceof Widget) {
            if (((Widget) c).isDisposed()) {
                return true;
            }
        }
        return false;
    }
}
