package org.gvsig.gpe.parser;

/**
 * This interface defines the "contract" between the consumer
 * application and libGPE. It has methods that will be invoke
 * by the parser every time that an event happens
 * @author Jorge Piera Llodr� (jorge.piera@iver.es)
 * @author Carlos S�nchez Peri��n (sanchez_carper@gva.es)
 */
public interface IGPEContentHandler extends IGPEContentHandlerSFP0 {

    /**
	 * This method is thrown when the parser find a new 
	 * bounding box.
	 * @param id
	 * Bounding box identifier
	 * @param coords
	 * A coordinates iterator
	 * @param srs
	 * Spatial reference system
	 * @return
	 * The consumer application object that represents
	 *  a bounding box
	 */
    public Object startBbox(String id, ICoordinateIterator coords, String srs);

    /**
	 * This method indicates that the parser thas finished to
	 * parse the bounding box.
	 * @param bbox
	 * The consumer application object that represents a
	 * bounding box
	 */
    public void endBbox(Object bbox);

    /**
	 * It is thrown every time that a new layer is detected.
	 * @param id
	 * Layer identifier
	 * @param namespace
	 * XML namespace
	 * @param name
	 * Layer name
	 * @param description
	 * Layer description
	 * @param srs
	 * Layer spatial reference system
	 * @param attributes
	 * Layer attributes
	 * @param parentLayer
	 * Layer that contains it
	 * @param bBox
	 * Layer bounding box
	 * @return
	 * The consumer application object that represents a layer
	 */
    public Object startLayer(String id, String namespace, String name, String description, String srs, IAttributesIterator attributes, Object parentLayer, Object bBox);

    /**
	 * It is thrown when a layer has been finished to parse.
	 * @param layer
	 * The consumer application object that represents a layer
	 */
    public void endLayer(Object layer);

    /**
	 * It adds a name to one layer that has been previously 
	 * created using the startLayer method.
	 * @param name
	 * Layer name
	 * @param layer
	 * The consumer application object that represents a layer
	 */
    public void addNameToLayer(String name, Object layer);

    /**
	 * It adds the description to one layer that has been previously 
	 * created using the startLayer method.
	 * @param description
	 * Layer description
	 * @param layer
	 * The consumer application object that represents a layer
	 */
    public void addDescriptionToLayer(String description, Object layer);

    /**
	 * It adds a spatial reference system to one layer that 
	 * has been previously created using the startLayer method.
	 * @param srs
	 * Spatial reference system
	 * @param layer
	 * The consumer application object that represents a layer
	 */
    public void addSrsToLayer(String srs, Object Layer);

    /**
	 * It establish the relationship parent-child between two layers
	 * that have been previously created using the startLayer method.
	 * @param parent
	 * The consumer application object that represents the parent layer
	 * @param layer
	 * The consumer application object that represents the child layer
	 */
    public void addParentLayerToLayer(Object parent, Object layer);

    /**
	 * It adds a bounding box to one layer that  has been previously 
	 * created using the startLayer method.
	 * @param bbox
	 * Layer bounding box
	 * @param layer
	 * The consumer application object that represents a layer
	 */
    public void addBboxToLayer(Object bbox, Object layer);

    /**
	 * It is thrown when the parser has found a new feature.
	 * @param id
	 * Feature identifier
	 * @param name
	 * Feature name
	 * @param namespace
	 * XML namespace
	 * @param attributes
	 * Feature attributes
	 * @param layer
	 * Consumer application object that represents a layer
	 * @return
	 * Consumer application object that represents a feature
	 */
    public Object startFeature(String id, String namespace, String name, IAttributesIterator attributes, Object layer);

    /**
	 * This method is thrown when the parser has finished to
	 * parse a feature.
	 * @param feature
	 * Consumer application object that represents a feature
	 */
    public void endFeature(Object feature);

    /**
	 * This method adds a name to one layer that has been
	 * previously created using the startFeature method. 
	 * @param name
	 * Feature name
	 * @param feature
     * Consumer application object that represents a feature
	 */
    public void addNameToFeature(String name, Object feature);

    /**
	 * This method adds a feature to one layer that has been 
	 * previously created using the startLayer method. 
	 * @param feature
	 * Consumer application object that represents a feature
	 * @param layer
	 * Consumer application object that represents a layer
	 */
    public void addFeatureToLayer(Object feature, Object layer);

    /**
	 * It is thrown when the parser has found a new element
	 * @param namespace
	 * XML namespace
	 * @param name
	 * Element name
	 * @param value
	 * Element value
	 * @param attributes
	 * Element attributes
	 * @param parentElement
	 * The parent of this element (if exists)
	 * @return
	 * Consumer application object that represents an element 
	 */
    public Object startElement(String namespace, String name, Object value, IAttributesIterator attributes, Object parentElement);

    /**
	 * This method is thrown when the parser find the end 
	 * of an element.
	 * @param element
	 * Consumer application object that represents an element 
	 */
    public void endElement(Object element);

    /**
	 * It is thrown to establish a relationship parent-child
	 * between two elements.
	 * @param parent
	 * Consumer application object that represents the parent element 
	 * @param element
	 * Consumer application object that represents the child element 
	 */
    public void addParentElementToElement(Object parent, Object element);

    /**
	 * This method adds an element to one feature that has been 
	 * previously created using the startFeature method
	 * @param element
	 * Consumer application object that represents an element 
	 * @param feature
	 * Consumer application object that represents the feature 
	 */
    public void addElementToFeature(Object element, Object feature);

    /**
	 * This method indicates that the parser has found a point. 
	 * @param id
	 * Point identifier
	 * @param coords
	 * A coordinates iterator
	 * @param srs
	 * Spatial reference system
	 * @return
	 * Consumer application object that represents a point 
	 */
    public Object startPoint(String id, ICoordinateIterator coords, String srs);

    /**
	 * It is thrown when the point has been completely parsed.
	 * @param point
	 * Consumer application object that represents a point 
	 */
    public void endPoint(Object point);

    /**
	 * This method indicates that the parser has found a lineString. 
	 * @param id
	 * LineString identifier
	 * @param coords
	 * A coordinates iterator
	 * @param srs
	 * Spatial reference system
	 * @return
	 * Consumer application object that represents a lineString 
	 */
    public Object startLineString(String id, ICoordinateIterator coords, String srs);

    /**
     * It is thrown when the lineString has been completely parsed.
	 * @param lineString
	 * Consumer application object that represents a lineString 
	 */
    public void endLineString(Object lineString);

    /**
	 * This method indicates that the parser has found a linearRing. 
	 * @param id
	 * LinearRing identifier
	 * @param coords
	 * A coordinates iterator
	 * @param srs
	 * Spatial reference system
	 * @return
	 * Consumer application object that represents a linarRing 
	 */
    public Object startLinearRing(String id, ICoordinateIterator coords, String srs);

    /**
	 * It is thrown when the linearRing has been completely parsed.
	 * @param linearRing
	 * Consumer application object that represents a linearRing 
	 */
    public void endLinearRing(Object linearRing);

    /**
	 * This method indicates that the parser has found a polygon. 
	 * @param id
	 * Polygon identifier
	 * @param coords
	 * A coordinates iterator
	 * @param srs
	 * Spatial reference system
	 * @return
	 * Consumer application object that represents a polygon 
	 */
    public Object startPolygon(String id, ICoordinateIterator coords, String srs);

    /**
	 * It is thrown when the polygon has been completely parsed.
	 * @param polygon
	 * Consumer application object that represents a polygon 
	 */
    public void endPolygon(Object polygon);

    /**
	 * This method associates a innerPolygon with its polygon
	 * @param innerPolygon
     * Consumer application object that represents a innerPolygon 
	 * @param Polygon
	 * Consumer application object that represents a polygon 
	 */
    public void addInnerPolygonToPolygon(Object innerPolygon, Object Polygon);

    /**
	 * This method indicates that the parser has found a InnerPolygon. 
	 * @param id
	 * InnerPolygon identifier
	 * @param coords
	 * A coordinates iterator
	 * @param srs
	 * Spatial reference system
	 * @return
	 * Consumer application object that represents a innerPolygon 
	 */
    public Object startInnerPolygon(String id, ICoordinateIterator coords, String srs);

    /**
	 * It is thrown when the innerPolygon has been completely parsed.
	 * @param innerPolygon
	 * Consumer application object that represents a innerPolygon 
	 */
    public void endInnerPolygon(Object innerPolygon);

    /**
	 * This method indicates that the parser has found a multipoint. 
	 * @param id
	 * MultiPoint identifier
	 * @param srs
	 * Spatial reference system
	 * @return
	 * Consumer application object that represents a multiPoint 
	 */
    public Object startMultiPoint(String id, String srs);

    /**
	 * It is thrown when the multiPoint has been completely parsed
	 * @param multiPoint
	 * Consumer application object that represents a multiPoint
	 */
    public void endMultiPoint(Object multiPoint);

    /**
	 * It is thrown to add a point to one multiPoint.
	 * @param point
     * Consumer application object that represents a point
	 * @param multiPoint
	 * Consumer application object that represents a multiPoint
	 */
    public void addPointToMultiPoint(Object point, Object multiPoint);

    /**
	 * This method indicates that the parser has found a multiLineString. 
	 * @param id
	 * MultiLineString identifier
	 * @param srs
	 * Spatial reference system
	 * @return
	 * Consumer application object that represents a multiLineString 
	 */
    public Object startMultiLineString(String id, String srs);

    /**
	 * It is thrown when the multiLineString has been completely parsed
	 * @param multiLineString
	 * Consumer application object that represents a multiLineString
	 */
    public void endMultiLineString(Object multiLineString);

    /**
	 * It is thrown to add a lineString to one lineString.
	 * @param lineString
     * Consumer application object that represents a lineString
	 * @param multiLineString
	 * Consumer application object that represents a multiLineString
	 */
    public void addLineStringToMultiLineString(Object lineString, Object multiLineString);

    /**
	 * This method indicates that the parser has found a multiPolygon. 
	 * @param id
	 * MultiPolygon identifier
	 * @param srs
	 * Spatial reference system
	 * @return
	 * Consumer application object that represents a multiPolygon 
	 */
    public Object startMultiPolygon(String id, String srs);

    /**
	 * It is thrown when the multiPolygon has been completely parsed
	 * @param multiPolygon
	 * Consumer application object that represents a multiPolygon
	 */
    public void endMultiPolygon(Object multiPolygon);

    /**
	 * It is thrown to add a polygon to one multiPolygon.
	 * @param polygon
     * Consumer application object that represents a polygon
	 * @param multiPolygon
	 * Consumer application object that represents a multiPolygon
	 */
    public void addPolygonToMultiPolygon(Object polygon, Object multiPolygon);

    /**
	 * This method indicates that the parser has found a multiGeometry. 
	 * @param id
	 * MultiGeometry identifier
	 * @param srs
	 * Spatial reference system
	 * @return
	 * Consumer application object that represents a multiGeometry
	 */
    public Object startMultiGeometry(String id, String srs);

    /**
	 * It is thrown when the multiGeometry has been completely parsed
	 * @param multiGeometry
	 * Consumer application object that represents a multiGeometry
	 */
    public void endMultiGeometry(Object multiGeometry);

    /**
	 * It is thrown to add a geometry to one multiGeometry.
	 * @param geometry
     * Consumer application object that represents a geometry
	 * @param multiGeometry
	 * Consumer application object that represents a multiGeometry
	 */
    public void addGeometryToMultiGeometry(Object geometry, Object multiGeometry);

    /**
	 * This method adds a bounding box to a feature.
	 * @param bbox
	 * Consumer application object that represents a bounding
	 * box
	 * @param feature
	 * Consumer application object that represents a feature
	 */
    public void addBboxToFeature(Object bbox, Object feature);

    /**
	 * This method adds a geometry to a feature. 
	 * @param geometry
	 * Consumer application object that represents a geometry
	 * @param feature
	 * Consumer application object that represents a feature
	 */
    public void addGeometryToFeature(Object geometry, Object feature);

    /**
	 * It is thrown when the parser has found a new metadata tag
	 * @param type
	 * String with the type of metadata, if is a description or any else
	 * @param data
	 * String with the value of the data.
	 * @param attributes 
	 * Object to pass the Atributtes
	 * @return
	 * Consumer application object that represents metadata 
	 */
    public Object startMetadata(String type, String data, IAttributesIterator attributes);

    /**
	 * This method adds metadata to feature. 
	 * @param metadata
	 * Consumer application object that represents metadata
	 * @param feature
	 * Consumer application object that represents a feature
	 */
    public void addMetadataToFeature(Object metadata, Object feature);

    /**
	 * This method adds metadata to complex metadata. 
	 * @param metadata
	 * Consumer application object that represents metadata
	 * @param parent
	 * Consumer application object that represents the complex metadata
	 */
    public void addMetadataToMetadata(Object metadata, Object parent);

    /**
	 * This method is thrown when the parser find the end of the metadata
	 * of an element.
	 * @param metadata
	 * Consumer application object that represents metadata 
	 */
    public void endMetadata(Object metadata);

    /**
	 * This method is thrown when the parser find the end of and element time tag
	 * @param time
	 * Consumer application object that represents time 
	 */
    public void endTime(Object time);

    /**
	 * It is thrown when the parser has found a new time tag tag
	 * @param type
	 * String with the type of time, if is a description or any else
	 * @param data
	 * String with the value of the data.
	 * @return
	 * Consumer application object that represents time
	 */
    public Object startTime(String name, String description, String type, String time, Object previous, Object next);

    public Object startTime(String type, String time);

    /**
	 * This method adds time to feature. 
	 * @param time
	 * Consumer application object that represents time
	 * @param feature
	 * Consumer application object that represents a feature
	 */
    public void addTimeToFeature(Object time, Object feature);
}
