package net.tourbook.application;

import net.tourbook.ui.ImageCombo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

/**
 * Because the {@link ImageCombo} looks very bad on osx, a normal combo box is used on osx and the
 * image combo is used on win32 and linux
 */
public class TourTypeCombo {

    private static final boolean osx = "carbon".equals(SWT.getPlatform());

    private Composite _comboContainer;

    private Combo _comboTourTypeOSX;

    private ImageCombo _comboTourType;

    TourTypeCombo(final Composite parent, final int style) {
        if (osx) {
            _comboTourTypeOSX = new Combo(parent, style);
        } else {
            _comboContainer = new Composite(parent, SWT.NONE);
            final GridLayout gl = new GridLayout();
            gl.marginWidth = 0;
            gl.marginHeight = 0;
            gl.horizontalSpacing = 0;
            gl.verticalSpacing = 0;
            _comboContainer.setLayout(gl);
            _comboTourType = new ImageCombo(_comboContainer, style);
            _comboTourType.setLayoutData(new GridData(SWT.NONE, SWT.CENTER, false, true));
        }
    }

    void add(final String filterName, final Image filterImage) {
        if (osx) {
            _comboTourTypeOSX.add(filterName);
        } else {
            _comboTourType.add(filterName, filterImage);
        }
    }

    void addDisposeListener(final DisposeListener disposeListener) {
        if (osx) {
            _comboTourTypeOSX.addDisposeListener(disposeListener);
        } else {
            _comboTourType.addDisposeListener(disposeListener);
        }
    }

    void addSelectionListener(final SelectionListener selectionListener) {
        if (osx) {
            _comboTourTypeOSX.addSelectionListener(selectionListener);
        } else {
            _comboTourType.addSelectionListener(selectionListener);
        }
    }

    Composite getControl() {
        if (osx) {
            return _comboTourTypeOSX;
        } else {
            return _comboContainer;
        }
    }

    int getSelectionIndex() {
        if (_comboContainer.isDisposed()) {
            return -1;
        }
        if (osx) {
            return _comboTourTypeOSX.getSelectionIndex();
        } else {
            return _comboTourType.getSelectionIndex();
        }
    }

    void removeAll() {
        if (osx) {
            _comboTourTypeOSX.removeAll();
        } else {
            _comboTourType.removeAll();
        }
    }

    void select(final int index) {
        if (osx) {
            _comboTourTypeOSX.select(index);
        } else {
            _comboTourType.select(index);
        }
    }

    void setLayoutData(final GridData gridData) {
        if (osx) {
            _comboTourTypeOSX.setLayoutData(gridData);
        } else {
            _comboTourType.setLayoutData(gridData);
        }
    }

    void setToolTipText(final String tooltip) {
        if (osx) {
            _comboTourTypeOSX.setToolTipText(tooltip);
        } else {
            _comboTourType.setToolTipText(tooltip);
        }
    }

    void setVisibleItemCount(final int count) {
        if (osx) {
            _comboTourTypeOSX.setVisibleItemCount(count);
        } else {
            _comboTourType.setVisibleItemCount(count);
        }
    }
}
