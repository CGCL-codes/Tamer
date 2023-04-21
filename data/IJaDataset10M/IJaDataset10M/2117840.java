package org.pachyderm.woc;

import org.apache.log4j.Logger;
import org.pachyderm.apollo.app.MCPage;
import org.pachyderm.authoring.AddMediaStep;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;

/**
 * @author jarcher
 *
 */
public class StepMultiCommonInformation extends AddMediaStep {

    private static Logger LOG = Logger.getLogger(StepMultiCommonInformation.class.getName());

    private static final long serialVersionUID = 1412874635365704181L;

    public NSDictionary<String, String> failedItem;

    public NSMutableDictionary commonMetadata = new NSMutableDictionary();

    /**
	 * @param context
	 */
    public StepMultiCommonInformation(WOContext context) {
        super(context);
    }

    /**
	 * go through and populate the metadata for each resource in uploadedItems with the default values provided
	 * 
	 * @return
	 */
    public WOComponent nextStep() {
        for (NSMutableDictionary<String, String> stuff : getPageInControl().getUploadedItems()) {
            try {
                LOG.info("processing uploaded item: " + stuff);
                String url = (String) stuff.valueForKey("url");
                stuff.addEntriesFromDictionary(commonMetadata);
                LOG.info("processing uploaded item in StepMultiCommonInformation: " + stuff);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return getPageInControl().nextStep();
    }

    /**
	 * @return
	 */
    public WOComponent cancel() {
        return ((MCPage) context().page()).getNextPage();
    }
}
