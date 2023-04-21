package uk.ac.liv.jt.format.elements;

import uk.ac.liv.jt.types.RGBA;

/**
 * Class designed to abstract appearance (material) data from JReality
 * 
 * @author przym
 */
public class AppearanceData {

    public float shineness;

    public RGBA ambient, emission, specular, diffuse;
}
