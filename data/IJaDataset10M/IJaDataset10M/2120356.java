package org.linkedgeodata.dao.nodestore;

import java.awt.geom.Point2D;
import java.util.Map;
import com.hp.hpl.jena.rdf.model.Model;

/**
 * A class for mapping nodes between java objects and RDF represenations.
 * The reason for this class is, that the same java object may have
 * multiple RDF representations based on some configuration.
 * 
 */
public interface INodeMapper {

    void transform(Map<Long, Point2D> points, Model out);

    Map<Long, Point2D> extract(Model model);
}
