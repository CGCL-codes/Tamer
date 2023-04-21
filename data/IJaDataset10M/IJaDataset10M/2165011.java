package org.kalypso.nofdpidss.core.base.flow.network;

import javax.xml.namespace.QName;
import org.kalypso.nofdp.idss.schema.schemata.gml.GmlConstants;

/**
 * @author Dirk Kuch
 */
public interface IWeirNode extends IStructureNode {

    public static final QName QN_TYPE = new QName(GmlConstants.NS_1DMODEL, "WeirNode");

    Double getDischargeCoeff();

    String getFlowDirection();

    Double getLateralContractionCoeff();
}
