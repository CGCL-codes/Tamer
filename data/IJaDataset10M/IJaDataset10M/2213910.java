package org.mitre.rt.client.synchronize.transactions.update;

import org.apache.log4j.*;
import org.mitre.rt.common.*;
import org.mitre.rt.rtclient.*;
import org.mitre.rt.rtclient.RTDocument.RT.*;
import org.mitre.rt.rtclient.ApplicationType.*;
import org.mitre.rt.client.synchronize.transactions.AbsUpdateTransaction;
import org.mitre.rt.client.xml.*;

/**
 *
 * @author BWORRELL
 */
public class RecommendationTypeUpdateTransaction extends AbsUpdateTransaction {

    protected static final Logger logger = Logger.getLogger(RecommendationTypeUpdateTransaction.class.getPackage().getName());

    private final RecommendationHelper recommendationHelper = new RecommendationHelper();

    public RecommendationTypeUpdateTransaction(VersionedItemType transObject, Recommendations container, UpdateResponse response, ApplicationType application, String name) {
        super(transObject, container, response, application, name);
    }

    @Override
    protected void handleNewTransaction() {
        Recommendations recs = (Recommendations) super.container;
        RecommendationType newRec = recs.addNewRecommendation();
        newRec.set(super.response.getServerItem());
    }

    @Override
    protected void handleModifiedTransaction() {
        RecommendationType myRec = (RecommendationType) transObject, serverRec = (RecommendationType) response.getServerItem();
        boolean changed = recommendationHelper.mergeRecommendations(super.parentApp, myRec, serverRec);
        super.defaultModifiedTransaction();
        if (changed == true) recommendationHelper.markModified((RecommendationType) transObject);
    }

    @Override
    protected void handleDeletedTransaction() {
        super.defaultModifiedTransaction();
    }
}
