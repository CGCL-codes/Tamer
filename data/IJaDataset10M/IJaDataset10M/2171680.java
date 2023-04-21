package org.kalypso.nofdpidss.core.base.gml.model.state;

import javax.xml.namespace.QName;
import org.kalypso.nofdp.idss.schema.schemata.gml.GmlConstants;
import org.kalypsodeegree.model.feature.Feature;

/**
 * @author Dirk Kuch
 */
public interface IAction extends Feature {

    public static final QName QN_ACTION_ID = new QName(GmlConstants.NS_STATUS, "actionID");

    public static final QName QN_CURRENT_STATE = new QName(GmlConstants.NS_STATUS, "currentState");

    public static final QName QN_USED_IN_MODE = new QName(GmlConstants.NS_STATUS, "usedInModes");

    IModule getModule();

    String getName();
}
