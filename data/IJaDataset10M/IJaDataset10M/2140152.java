package org.kalypso.nofdpidss.core.base.gml.model.project.base;

import javax.xml.namespace.QName;
import org.kalypso.nofdp.idss.schema.schemata.gml.GmlConstants;
import org.kalypsodeegree.model.feature.Feature;

/**
 * @author Dirk Kuch
 */
public interface ICriterionValueBenefitRating extends Feature {

    public static final QName QN_LINKED_CRITERION = new QName(GmlConstants.NS_ASSESSMENT, "abstractCriterionLinkedMember");

    public static final QName QN_RESULT = new QName(GmlConstants.NS_ASSESSMENT, "result");

    ICriterion getLinkedCriterion();

    Double getResult();
}
