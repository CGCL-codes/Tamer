package org.jowidgets.spi.impl.dummy;

import org.jowidgets.spi.IOptionalWidgetsFactorySpi;
import org.jowidgets.spi.widgets.ICalendarSpi;
import org.jowidgets.spi.widgets.IDirectoryChooserSpi;
import org.jowidgets.spi.widgets.IFileChooserSpi;
import org.jowidgets.spi.widgets.setup.ICalendarSetupSpi;
import org.jowidgets.spi.widgets.setup.IDirectoryChooserSetupSpi;
import org.jowidgets.spi.widgets.setup.IFileChooserSetupSpi;

public class DummyOptionalWidgetsFactory implements IOptionalWidgetsFactorySpi {

    @Override
    public boolean hasFileChooser() {
        return false;
    }

    @Override
    public IFileChooserSpi createFileChooser(final Object parentUiReference, final IFileChooserSetupSpi setup) {
        return null;
    }

    @Override
    public boolean hasDirectoryChooser() {
        return false;
    }

    @Override
    public IDirectoryChooserSpi createDirectoryChooser(final Object parentUiReference, final IDirectoryChooserSetupSpi setup) {
        return null;
    }

    @Override
    public boolean hasCalendar() {
        return false;
    }

    @Override
    public ICalendarSpi createCalendar(final Object parentUiReference, final ICalendarSetupSpi setup) {
        return null;
    }
}
