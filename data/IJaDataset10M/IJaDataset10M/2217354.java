package org.kalypso.nofdpidss.core.base.gml.model.project.base;

import javax.xml.namespace.QName;
import org.kalypso.nofdp.idss.schema.schemata.gml.GmlConstants;
import org.kalypsodeegree.model.feature.Feature;

/**
 * @author Dirk Kuch
 */
public interface ICriterionRankingWeightMember extends Feature {

    public static final QName QN_WEIGHT = new QName(GmlConstants.NS_ASSESSMENT, "weight");

    public static final QName QN_LINKED_CRITERION_DEFINITION = new QName(GmlConstants.NS_ASSESSMENT, "criterionDefLinkedMember");

    public Double getWeight();

    ICriterionDefinition getLinkedCriterionDefinition();
}
