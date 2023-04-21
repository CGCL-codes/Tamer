package org.dcm4chex.archive.web.maverick.mcmc.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.dcm4chex.archive.ejb.interfaces.MediaComposer;
import org.dcm4chex.archive.ejb.interfaces.MediaComposerHome;
import org.dcm4chex.archive.ejb.interfaces.MediaDTO;
import org.dcm4chex.archive.util.EJBHomeFactory;
import org.dcm4chex.archive.web.maverick.BasicFormPagingModel;
import org.dcm4chex.archive.web.maverick.mcmc.MCMConsoleCtrl;

/**
 * @author franz.willer
 *
 * The Model for Media Creation Managment WEB interface.
 */
public class MCMModel extends BasicFormPagingModel {

    /** The session attribute name to store the model in http session. */
    public static final String MCMMODEL_ATTR_NAME = "mcmModel";

    /** 
     * Status for action 'queue' 
     * <p>
     * This value is used in xsl to create queue action button for a media with this status.
     */
    public static final String STATI_FOR_QUEUE = String.valueOf(MediaDTO.OPEN);

    /** Holds the current list of media for the view. */
    private MediaList mediaList;

    /** Holds the filter for media search. */
    private MCMFilter filter;

    /** Holds availability status of MCM SCP */
    private boolean mcmNotAvail = false;

    /**
     * Holds the 'checked' flag for 'checkMCM' checkbox.
     * <p>
     * checkMCM request parameter is only present if mcmNotAvail is true.
     * <p>
     * The availability check of mcm is done either before 'queue' action or 
     * if checkMCM request parameter is true.
     */
    private boolean checkAvail = false;

    /**
     * Creates the model.
     * <p>
     * Perform an initial media search with the default filter. <br>
     * (search for all media with status COLLECTING)
     * <p>
     * performs an initial availability check for MCM_SCP service.
     */
    private MCMModel(HttpServletRequest request) {
        super(request);
        getFilter();
        filterMediaList(true);
        mcmNotAvail = !MCMConsoleCtrl.getMcmScuDelegate().checkMcmScpAvail();
    }

    /**
     * Get the model for an http request.
     * <p>
     * Look in the session for an associated model via <code>MCMMODEL_ATTR_NAME</code><br>
     * If there is no model stored in session (first request) a new model is created and stored in session.
     * 
     * @param request A http request.
     * 
     * @return The model for given request.
     */
    public static final MCMModel getModel(HttpServletRequest request) {
        MCMModel model = (MCMModel) request.getSession().getAttribute(MCMMODEL_ATTR_NAME);
        if (model == null) {
            model = new MCMModel(request);
            request.getSession().setAttribute(MCMMODEL_ATTR_NAME, model);
        }
        return model;
    }

    public String getModelName() {
        return "MCM";
    }

    /**
     * Returns the status for 'queue' action.
     * <p>
     * This value is used used in the view to create a 'queue' action button/link 
     * for media with this status.
     * 
     * @return The status for queue action
     */
    public String getStatiForQueue() {
        return STATI_FOR_QUEUE;
    }

    /**
     * Returns current list of media.
     * 
     * @return List of media.
     */
    public List getMediaList() {
        return mediaList;
    }

    /**
     * Returns the Filter that is used to search media.
     * 
     * @return current filter.
     */
    public MCMFilter getFilter() {
        if (filter == null) filter = new MCMFilter();
        return filter;
    }

    /**
     * Performa a media search with current filter settings.
     * <p>
     * If <code>newSearch is true</code> the <code>offset</code> is set to <code>0</code> (get first result page).
     * <p>
     * The result of the search is stored in <code>mediaList</code> and <code>total</code> is updated
     * with the total number of results for this search.
     * 
     * @param newSearch
     */
    public void filterMediaList(boolean newSearch) {
        if (newSearch) setOffset(0);
        try {
            Collection col = new ArrayList();
            Long start = null;
            Long end = null;
            int[] stati = null;
            if (filter.selectedStati() != null) {
                stati = filter.selectedStati();
            }
            if (!mcmNotAvail && (stati == null || filter.getSelectedStatiAsString().indexOf(String.valueOf(MediaDTO.BURNING)) != -1)) {
                MCMConsoleCtrl.getMcmScuDelegate().updateMediaStatus();
            }
            Integer offset = new Integer(getOffset());
            Integer limit = new Integer(getLimit());
            if (MCMFilter.DATE_FILTER_ALL.equals(filter.getCreateOrUpdateDate())) {
                setTotal(lookupMediaComposer().findByCreatedTime(col, start, end, stati, offset, limit, filter.isDescent()));
            } else {
                start = filter.startDateAsLong();
                end = filter.endDateAsLong();
                if (MCMFilter.CREATED_FILTER.equals(filter.getCreateOrUpdateDate())) {
                    setTotal(lookupMediaComposer().findByCreatedTime(col, start, end, stati, offset, limit, filter.isDescent()));
                } else if (MCMFilter.UPDATED_FILTER.equals(filter.getCreateOrUpdateDate())) {
                    setTotal(lookupMediaComposer().findByUpdatedTime(col, start, end, stati, offset, limit, filter.isDescent()));
                }
            }
            mediaList = new MediaList(col);
            col.clear();
        } catch (Exception x) {
            log.error("", x);
            mediaList = new MediaList();
        }
    }

    /**
     * Returns the MediaComposer bean.
     * 
     * @return The MediaComposer bean.
     * @throws Exception
     */
    protected MediaComposer lookupMediaComposer() throws Exception {
        MediaComposerHome home = (MediaComposerHome) EJBHomeFactory.getFactory().lookup(MediaComposerHome.class, MediaComposerHome.JNDI_NAME);
        return home.create();
    }

    /**
     * @return Returns the mcmNotAvail.
     */
    public boolean isMcmNotAvail() {
        return mcmNotAvail;
    }

    /**
     * @param mcmNotAvail The mcmNotAvail to set.
     */
    public void setMcmNotAvail(boolean mcmNotAvail) {
        this.mcmNotAvail = mcmNotAvail;
    }

    /**
     * @return Returns the checkAvail.
     */
    public boolean isCheckAvail() {
        return checkAvail;
    }

    /**
     * @param checkAvail The checkAvail to set.
     */
    public void setCheckAvail(boolean checkAvail) {
        this.checkAvail = checkAvail;
    }

    /**
     * Update the mediaStatus of media with given pk.
     * <p>
     * This method updates the status with information in current model and in MediaLocal bean.
     * 
     * @param mediaPk		The pk of the media to change.
     * @param status		The new media staus.
     * @param statusInfo 	Info text for the new status.
     */
    public void updateMediaStatus(int mediaPk, int status, String statusInfo) {
        MediaData md = mediaDataFromList(mediaPk);
        if (md != null) {
            md.setMediaStatus(status);
            md.setMediaStatusInfo(statusInfo);
            try {
                this.lookupMediaComposer().setMediaStatus(mediaPk, status, statusInfo);
            } catch (Exception e) {
                log.error(e.toString());
            }
        }
    }

    /**
     * Returns the MediaData object for given media pk.
     * 
     * @param mediaPk PK of a media.
     * 
     * @return The MediaData object.
     */
    public MediaData mediaDataFromList(int mediaPk) {
        int pos = getMediaList().indexOf(new MediaData(mediaPk));
        if (pos != -1) {
            return (MediaData) this.mediaList.get(pos);
        }
        return null;
    }

    public void gotoCurrentPage() {
        filterMediaList(false);
    }
}
