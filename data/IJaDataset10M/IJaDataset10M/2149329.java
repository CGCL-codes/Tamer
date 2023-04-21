package de.guidoludwig.jtrade.domain;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;
import com.jgoodies.binding.PresentationModel;
import de.guidoludwig.af.ApplicationFrame;
import de.guidoludwig.jtrade.archive.ui.ArchiveModel;
import de.guidoludwig.jtrade.db4o.JTDB;
import de.guidoludwig.jtrade.tradelist.ui.ArtistModel;
import de.guidoludwig.jtrade.tradelist.ui.ShowModel;
import de.guidoludwig.jtrade.util.SwingUtil;

/**
 * @author <a href="mailto:jtrade@gigabss.de">Guido Ludwig</a>
 * @version $Revision: 1.8 $
 */
public class JTChangeTracker {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public static final JTChangeTracker INSTANCE = new JTChangeTracker();

    private JTChangeTracker() {
    }

    /**
	 * Stores all open changes IN THE EDT !
	 */
    public void storeOpenChanges() {
        boolean inEDT = JTDB.INSTANCE.isRunInEDT();
        JTDB.INSTANCE.setRunInEDT(true);
        if (ArtistModel.INSTANCE.isChanged()) {
            storeArtist(ArtistModel.INSTANCE.getBean());
        }
        if (ShowModel.INSTANCE.isChanged()) {
            storeShow(ShowModel.INSTANCE.getBean());
        }
        if (ArchiveModel.INSTANCE.isChanged()) {
            storeArchive(ArchiveModel.INSTANCE.getBean());
        }
        JTDB.INSTANCE.setRunInEDT(inEDT);
    }

    public void init() {
        ArtistModel.INSTANCE.addPropertyChangeListener(PresentationModel.PROPERTYNAME_BEAN, new ArtistChangeHandler());
        ShowModel.INSTANCE.addPropertyChangeListener(PresentationModel.PROPERTYNAME_BEAN, new ShowChangeHandler());
        ArchiveModel.INSTANCE.addPropertyChangeListener(PresentationModel.PROPERTYNAME_BEAN, new ArchiveChangeHandler());
    }

    public synchronized boolean allowesExit() {
        ApplicationFrame.INSTANCE.setStatus("Finish current edit");
        SwingUtil.finishCurrentEdit();
        ApplicationFrame.INSTANCE.setStatus("Prepare for store open changes");
        JTDB.INSTANCE.setRunInEDT(true);
        ApplicationFrame.INSTANCE.setStatus("Store open changes");
        storeOpenChanges();
        int tries = 0;
        while (JTDB.INSTANCE.hasRunningTasks()) {
            tries++;
            if (tries > 10) {
                Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("more than " + tries + " tries to store open changes, giving up");
                break;
            }
            String status = "Waiting 0.2 seconds for " + JTDB.INSTANCE.getNumberOfRunningTasks() + " tasks to finish.";
            ApplicationFrame.INSTANCE.setStatus(status);
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(status);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private void storeArtist(Artist artist) {
        if (artist != null) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).fine("Storing " + artist.getName());
            JTDB.INSTANCE.store(artist);
        }
    }

    private void storeShow(Show show) {
        if (show != null) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).fine("Storing " + show.getShowDate());
            JTDB.INSTANCE.store(show);
        }
    }

    private void storeArchive(Archive archive) {
        if (archive != null) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).fine("Storing " + archive.getName());
            JTDB.INSTANCE.store(archive);
        }
    }

    private class ArtistChangeHandler implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent arg0) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).finest("Artist Change " + arg0.getOldValue() + "-" + arg0.getNewValue());
            storeArtist((Artist) arg0.getOldValue());
        }
    }

    private class ShowChangeHandler implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent arg0) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).finest("Show Change " + arg0.getOldValue() + "-" + arg0.getNewValue());
            storeShow((Show) arg0.getOldValue());
        }
    }

    private class ArchiveChangeHandler implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent arg0) {
            storeArchive((Archive) arg0.getOldValue());
        }
    }
}
