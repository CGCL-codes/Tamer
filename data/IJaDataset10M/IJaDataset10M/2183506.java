package freeguide.plugins.ui.vertical.simple.filter.gui.helper;

import freeguide.plugins.ui.vertical.simple.filter.ProgrammeFilter;

/**
 * An interface for setting dialogs that shall be used from a
 * GenericFilterMenu At first the dialog is instantiated, then setFilter() is
 * called, and then the init() method. After that has happened, the dialog
 * may be shown.
 *
 * @author Christian Weiske (cweiske at cweiske.de)
 */
public interface SettingDialog {

    /**
     * DOCUMENT_ME!
     *
     * @return DOCUMENT_ME!
     */
    public boolean isClosedWithOk();

    /**
     * DOCUMENT_ME!
     */
    public void init();

    /**
     * DOCUMENT_ME!
     *
     * @param filter DOCUMENT_ME!
     */
    public void setFilter(ProgrammeFilter filter);

    /**
     * DOCUMENT_ME!
     */
    public void show();
}
