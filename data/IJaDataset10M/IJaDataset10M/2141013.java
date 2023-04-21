package com.memomics.cytoscape_plugin.model;

import com.alitora.asapi.publicModel.ASAPI_EdgeType;
import com.alitora.asapi.publicModel.ASAPI_HyperEdgeType;
import com.alitora.asapi.publicModel.ASAPI_HyperNodeType;
import com.alitora.asapi.publicModel.ASAPI_NodeType;
import com.alitora.asapi.publicModel.ASAPI_TypeBase;
import com.alitora.kharmony.client.khquery.KHQualifier;
import com.alitora.kharmony.client.khquery.KHSelectQuery;
import com.alitora.kharmony.client.khquery.component.EdgeTypeSelector;
import com.alitora.kharmony.client.khquery.component.HyperEdgeTypeSelector;
import com.alitora.kharmony.client.khquery.component.HyperNodeTypeSelector;
import com.alitora.kharmony.client.khquery.component.NodeTypeSelector;
import com.alitora.kharmony.client.khquery.component.Selector;
import com.alitora.kharmony.client.khquery.constraint.KHPropertyConstraint;
import com.alitora.kharmony.client.khquery.constraint.KHQualifierConstraint;
import com.alitora.kharmony.client.khquery.container.KHBooleanANDQueryContainer;
import com.alitora.kharmony.client.model.KHProperty;
import com.alitora.kharmony.client.model.KHPropertyString;
import com.alitora.kharmony.client.model.KHPropertyUMIS;
import com.alitora.kharmony.client.model.KHPropertyUntokenizedString;
import com.alitora.kharmony.client.model.types.PropertyDef;
import com.alitora.kharmony.client.model.types.PropertyType;
import com.alitora.kharmony.client.util.PropertyUtil;
import com.alitora.umis.UMIS;

public class QueryUtils {

    public static KHQualifier getSegmentNameQualifier(String segmentName) {
        KHQualifier qualifier = new KHQualifier();
        PropertyDef segNamePDef = new PropertyDef(KHQualifier.SEGMENTNAME, "kharmony", PropertyType.UNTOKENIZED_STRING, "Segment name prop");
        qualifier.setProperty(PropertyUtil.createProperty(KHPropertyUntokenizedString.class, segNamePDef, segmentName));
        return qualifier;
    }
}
