package org.compiere.model;

import java.util.*;
import javax.servlet.http.*;

/**
 * Container Model
 * 
 * @author Yves Sandfort
 * @version $Id$
 */
@SuppressWarnings("serial")
public class MAd extends X_CM_Ad {

    /**
	 * Standard constructor for AD
	 * @param ctx Context
	 * @param CM_Ad_ID ID
	 * @param trxName Transaction
	 */
    public MAd(Properties ctx, int CM_Ad_ID, String trxName) {
        super(ctx, CM_Ad_ID, trxName);
    }

    /**
	 * Get's the relevant current Impression value which is Actual+Start
	 * @return int
	 */
    public int getCurrentImpression() {
        return getActualImpression() + getStartImpression();
    }

    /**
	 * Adds an Impression to the current Ad
	 * We will deactivate the Ad as soon as one of the Max Criterias are fullfiled
	 */
    public void addImpression() {
        setActualImpression(getActualImpression() + 1);
        if (getCurrentImpression() >= getMaxImpression()) setIsActive(false);
        save();
    }

    /**
	 * Get Next of this Category, this Procedure will return the next Ad in a category and expire it if needed
	 * @param ctx Context
	 * @param CM_Ad_Cat_ID Category
	 * @param trxName Transaction
	 * @return MAd
	 */
    public static MAd getNext(Properties ctx, int CM_Ad_Cat_ID, String trxName) {
        MAd thisAd = null;
        int[] thisAds = MAd.getAllIDs("CM_Ad", "ActualImpression+StartImpression<MaxImpression AND CM_Ad_Cat_ID=" + CM_Ad_Cat_ID, trxName);
        if (thisAds != null) {
            for (int i = 0; i < thisAds.length; i++) {
                MAd tempAd = new MAd(ctx, thisAds[i], trxName);
                if (thisAd == null) thisAd = tempAd;
                if (tempAd.getCurrentImpression() <= thisAd.getCurrentImpression()) thisAd = tempAd;
            }
        }
        if (thisAd != null) thisAd.addImpression();
        return thisAd;
    }

    /**
	 * Add Click Record to Log
	 * @param request ServletReqeust
	 */
    public void addClick(HttpServletRequest request) {
        setActualClick(getActualClick() + 1);
        if (getActualClick() > getMaxClick()) setIsActive(true);
        save();
    }
}
