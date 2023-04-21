package si.cit.eprojekti.enews.controller;

import org.apache.log4j.Priority;
import si.cit.eprojekti.enews.NewsSchema;
import si.cit.eprojekti.enews.controller.newsFilesStates.*;
import com.jcorporate.expresso.core.controller.DBController;

/**
 *	
 *	News Files Controller for News files storage
 *
 * 	@author taks
 *	@version 1.0
 *
 */
public class NewsFilesController extends DBController {

    private String thisClass = new String(this.getClass().getName() + ".");

    private static org.apache.log4j.Category standardLog = org.apache.log4j.Category.getInstance("pvn.standard.enews");

    private static org.apache.log4j.Category debugLog = org.apache.log4j.Category.getInstance("pvn.debug.enews");

    /**
	 * @return	String
	 */
    public String getTitle() {
        return getString("newsFilesControllerDesc");
    }

    /**
	 *  Only external states
	 */
    public NewsFilesController() {
        super();
        setSchema(NewsSchema.class);
        String myName = new String(thisClass + "Controller()");
        try {
            UploadFilesState uploadFilesState = new UploadFilesState("uploadFilesState", "uploadFilesStateDesc");
            uploadFilesState.addRequiredParameter("projectId");
            addState(uploadFilesState);
            DetailFilesState detailFilesState = new DetailFilesState("detailFilesState", "detailFilesStateDesc");
            detailFilesState.addRequiredParameter("projectId");
            addState(detailFilesState);
            FilesUpdateState filesUpdateState = new FilesUpdateState("filesUpdateState", "filesUpdateStateDesc");
            filesUpdateState.addRequiredParameter("projectId");
            filesUpdateState.addRequiredParameter("fileId");
            addState(filesUpdateState);
            FilesDeleteState filesDeleteState = new FilesDeleteState("filesDeleteState", "filesDeleteStateDesc");
            filesDeleteState.addRequiredParameter("projectId");
            filesDeleteState.addRequiredParameter("fileId");
            addState(filesDeleteState);
            setInitialState("uploadFilesState");
        } catch (Exception e) {
            if (standardLog.isEnabledFor(Priority.WARN)) standardLog.warn(" :: Exception in \"" + this.getClass().getName() + "\" : " + e.toString());
            if (debugLog.isDebugEnabled()) debugLog.debug(" :: Exception in \"" + this.getClass().getName() + "\" : " + e.toString(), e.fillInStackTrace());
        } finally {
        }
    }
}
