package org.kalypso.nofdpidss.core.base.gml.model.geodata;

import javax.xml.namespace.QName;
import org.kalypso.nofdp.idss.schema.schemata.gml.GmlConstants;
import org.kalypsodeegree.model.feature.Feature;

/**
 * @author Dirk Kuch
 */
public interface IAllowedString extends Feature {

    public static final QName QN_NAME = new QName(GmlConstants.NS_GEODATA, "name");

    public static final QName QN_LANGUAGE_MEMBER = new QName(GmlConstants.NS_GEODATA, "allowedStringsPerLanguageMember");

    public String getMainTerm();

    public String getDescription();

    public String getName();
}
