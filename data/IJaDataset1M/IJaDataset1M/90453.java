package org.n52.sos.encode;

import net.opengis.gml.AbstractTimeGeometricPrimitiveType;
import net.opengis.gml.FeatureDocument;
import org.n52.sos.ogc.gml.time.ISosTime;
import org.n52.sos.ogc.om.sampleFeatures.SosAbstractFeature;
import org.n52.sos.ogc.ows.OwsExceptionReport;

/**
 * offers methods for encoding gml elements and gml:Feature extensions;
 * implementation must be defined in conf.sos.gmlEncoder of config.properties file
 * 
 * ATTENTION: class needs XMLBeans generated jars of GML schema and schema extensions!!
 * 
 * @author Christoph Stasch
 *
 */
public interface IGMLEncoder {

    /**
     * creates an XMLBeans representation of passed SOS representation of feature; this could be also a feature collection; the type of the features
     * depend on the implementation of this method
     * 
     * @param absFeature
     *        SOS representation of the feature, which should be encoded
     * @return FeatureDocument
     *        XMLBeans representation of feature
     * @throws OwsExceptionReport 
     *          if feature type is not supported by the implementation of the IGMLEncoder
     *        
     */
    public FeatureDocument createFeature(SosAbstractFeature absFeature) throws OwsExceptionReport;

    /**
     * creates XmlBeans representation of gml:TimeObjectPropertyType from passed sos time object
     * 
     * @param time
     *        ISosTime implementation, which should be created an XmlBeans O&M representation from
     * @return Returns XmlBean representing the passed time in GML format
     */
    public AbstractTimeGeometricPrimitiveType createTime(ISosTime time);
}
