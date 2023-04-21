package org.mbari.vars.mini;

import com.google.inject.Inject;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventTopicSubscriber;
import org.mbari.vars.services.DataCreationService;
import org.mbari.vars.services.DataLookupService;
import org.mbari.vars.services.DataPersistenceService;
import org.mbari.vars.services.VideoService;
import org.mbari.vars.services.VideoServiceException;
import vars.IUserAccount;
import vars.annotation.IAssociation;
import vars.annotation.IObservation;
import vars.annotation.IVideoArchive;
import vars.annotation.IVideoFrame;

/**
 *
 *
 * @version    2008.12.30 at 01:28:31 PST
 * @author     Brian Schlining [brian@mbari.org]
 */
public class AppFrameController {

    private final DataCreationService dataCreationService;

    private final DataLookupService dataLookupService;

    private final DataPersistenceService dataPersistenceService;

    private final VideoService videoService;

    private final EventTopicSubscriber deleteObservationsSubscriber;

    private final EventTopicSubscriber persistObservationsSubscriber;

    private final EventTopicSubscriber updateUserAccountSubscriber;

    private final EventTopicSubscriber deleteAssociationsSubscriber;

    /**
     * Constructs ...
     *
     *
     * @param dataCreationService
     * @param dataLookupService
     * @param dataPersistenceService
     * @param userLookupService
     * @param userPersistenceService
     */
    @Inject
    public AppFrameController(DataCreationService dataCreationService, DataLookupService dataLookupService, DataPersistenceService dataPersistenceService, VideoService videoService) {
        this.dataCreationService = dataCreationService;
        this.dataLookupService = dataLookupService;
        this.dataPersistenceService = dataPersistenceService;
        this.videoService = videoService;
        deleteObservationsSubscriber = new DeleteObservationsSubscriber();
        persistObservationsSubscriber = new PersistObservationsSubscriber();
        updateUserAccountSubscriber = new UpdateUserAccountSubscriber();
        deleteAssociationsSubscriber = new DeleteAssociationsSubscriber();
        EventBus.subscribe(Lookup.DELETE_OBSERVATIONS_TOPIC, deleteObservationsSubscriber);
        EventBus.subscribe(Lookup.PERSIST_OBSERVATIONS_TOPIC, persistObservationsSubscriber);
        EventBus.subscribe(Lookup.SET_USERACCOUNT_TOPIC, updateUserAccountSubscriber);
        EventBus.subscribe(Lookup.DELETE_ASSOCIATIONS_TOPIC, deleteAssociationsSubscriber);
        Lookup.currentVideoArchiveDispatcher().addPropertyChangeListener(new VideoArchiveChangeListener());
    }

    public DataCreationService getDataCreationService() {
        return dataCreationService;
    }

    /**
     * TODO: Add JavaDoc
     * @return
     */
    public DataLookupService getDataLookupService() {
        return dataLookupService;
    }

    /**
     * TODO: Add JavaDoc
     * @return
     */
    public DataPersistenceService getDataPersistenceService() {
        return dataPersistenceService;
    }

    public VideoService getVideoService() {
        return videoService;
    }

    /**
     * This subscriber deletes lists of observations from the database.
     */
    private class DeleteObservationsSubscriber implements EventTopicSubscriber<List<IObservation>> {

        public void onEvent(String topic, List<IObservation> data) {
            if (Lookup.DELETE_OBSERVATIONS_TOPIC.equals(topic)) {
                for (IObservation observation : data) {
                    dataPersistenceService.makeTransient(observation);
                    final IVideoFrame videoFrame = observation.getVideoFrame();
                    observation.getVideoFrame().removeObservation(observation);
                    if (videoFrame.getObservations().size() == 0) {
                        dataPersistenceService.makeTransient(videoFrame);
                        videoFrame.getVideoArchive().removeVideoFrame(videoFrame);
                    }
                }
            }
        }
    }

    private class DeleteAssociationsSubscriber implements EventTopicSubscriber<List<IAssociation>> {

        public void onEvent(String topic, List<IAssociation> data) {
            if (Lookup.DELETE_ASSOCIATIONS_TOPIC.equals(topic)) {
                for (IAssociation association : data) {
                    IObservation observation = association.getObservation();
                    dataPersistenceService.makeTransient(association);
                    observation.removeAssociation(association);
                }
            }
        }
    }

    /**
     * In order to greatly simplify the database IO in this app. I'm using
     * eventbus to help persist and delete objects. That way I can localize
     * the database code in just a couple of places.
     *
     * This Subscriber persists list of observations
     */
    private class PersistObservationsSubscriber implements EventTopicSubscriber<List<IObservation>> {

        public void onEvent(String topic, List<IObservation> data) {
            if (Lookup.PERSIST_OBSERVATIONS_TOPIC.equals(topic)) {
                for (IObservation observation : data) {
                    dataPersistenceService.makePersistent(observation);
                }
            }
        }
    }

    /**
     * Subscriber that monitors for userAccounts. If it's not already in the
     * database then it get's inserted. The userAcount is then assigned to
     * a {@link org.mbari.util.Dispatcher} that can be retriefed via {@link Lookup}
     */
    private class UpdateUserAccountSubscriber implements EventTopicSubscriber<IUserAccount> {

        public void onEvent(String topic, IUserAccount data) {
            if (Lookup.SET_USERACCOUNT_TOPIC.equals(topic)) {
                IUserAccount userAccount = dataLookupService.findUserAccountByName(data.getUserName());
                if (userAccount == null) {
                    dataPersistenceService.makePersistent(data);
                    userAccount = data;
                }
                Lookup.currentUserDispatcher().setValueObject(userAccount);
            }
        }
    }

    private class VideoArchiveChangeListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            final IVideoArchive videoArchive = (IVideoArchive) evt.getNewValue();
            final String videoArchiveName = videoArchive != null ? videoArchive.getVideoArchiveName() : null;
            try {
                videoService.openVideo(videoArchiveName);
            } catch (VideoServiceException e) {
                EventBus.publish(Lookup.NONFATAL_ERROR_TOPIC, e);
            }
        }
    }
}
