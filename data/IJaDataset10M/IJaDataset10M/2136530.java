package com.iver.cit.gvsig.fmap.core;

/**
 * Interface that represents the objects able to be converted or imported from SLD
 * SLD: Sytled Layer Descriptor v1.0.0(OpenGIS Implementation Specification)
 * @author laura
 */
public interface ISLDCompatible {

    public String toSLD();

    public String fromSLD(String sld);
}
