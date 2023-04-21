package org.nex.ts.server.cube.model;

import org.nex.ts.smp.api.ISubjectProxy;
import org.nex.ts.smp.api.ITopicSpacesOntology;
import org.openrdf.model.URI;
import org.nex.ts.server.cube.api.ICubeOntology;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

/**
 * @author Owner
 *
 */
public class CUBE {

    public static final String NAMESPACE = ISubjectProxy.PROXY_BASE_URI;

    public static final URI CUBE_LEGEND_ROOT, CUBE_CLASS, CUBE_TAG, CUBE_CELL, CUBE_TOPIC, CUBE_DIMENSION, TOPIC_FACET, TOPIC_FACET_LIST_PROPERTY, CUBE_CELL_TOPIC_LIST_PROPERTY, CUBE_DIMENSION_LIST_PROPERTY, DIMENSION_ELEMENT_LIST_PROPERTY, DIMENSION_NAME_STRING_PROPERTY, HOST_CUBE_PROPERTY, HOST_DIMENSION_PROPERTY, DIMENSION_COLUMN_NUMBER_PROPERTY;

    static {
        ValueFactory factory = ValueFactoryImpl.getInstance();
        CUBE_LEGEND_ROOT = factory.createURI(NAMESPACE, ICubeOntology.CUBE_LEGEND_ROOT);
        CUBE_CLASS = factory.createURI(NAMESPACE, ICubeOntology.CUBE_CLASS);
        CUBE_TAG = factory.createURI(NAMESPACE, ICubeOntology.CUBE_TAG);
        CUBE_CELL = factory.createURI(NAMESPACE, ICubeOntology.CUBE_CELL);
        CUBE_TOPIC = factory.createURI(NAMESPACE, ICubeOntology.CUBE_TOPIC);
        CUBE_DIMENSION = factory.createURI(NAMESPACE, ICubeOntology.CUBE_DIMENSION);
        TOPIC_FACET = factory.createURI(NAMESPACE, ICubeOntology.TOPIC_FACET);
        TOPIC_FACET_LIST_PROPERTY = factory.createURI(NAMESPACE, ICubeOntology.TOPIC_FACET_LIST_PROPERTY);
        CUBE_CELL_TOPIC_LIST_PROPERTY = factory.createURI(NAMESPACE, ICubeOntology.CUBE_CELL_TOPIC_LIST_PROPERTY);
        CUBE_DIMENSION_LIST_PROPERTY = factory.createURI(NAMESPACE, ICubeOntology.CUBE_DIMENSION_LIST_PROPERTY);
        DIMENSION_ELEMENT_LIST_PROPERTY = factory.createURI(NAMESPACE, ICubeOntology.DIMENSION_ELEMENT_LIST_PROPERTY);
        DIMENSION_NAME_STRING_PROPERTY = factory.createURI(NAMESPACE, ICubeOntology.DIMENSION_NAME_STRING_PROPERTY);
        HOST_CUBE_PROPERTY = factory.createURI(NAMESPACE, ICubeOntology.HOST_CUBE_PROPERTY);
        HOST_DIMENSION_PROPERTY = factory.createURI(NAMESPACE, ICubeOntology.HOST_DIMENSION_PROPERTY);
        DIMENSION_COLUMN_NUMBER_PROPERTY = factory.createURI(NAMESPACE, ICubeOntology.DIMENSION_COLUMN_NUMBER_PROPERTY);
    }
}
