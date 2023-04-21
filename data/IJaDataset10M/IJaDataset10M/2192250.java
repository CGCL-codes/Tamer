package org.palo.api;

/**
 * A so called virtual cube is used to make a certain cube view state persistent. 
 * And hence a virtual cube cannot have real dimensions instead it has virtual
 * dimensions. This is because virtual cubes and virtual dimensions are treated
 * specially by the palo server 
 * 
 * @author Stepan Rutz
 * @version $Id$
 * @deprecated please use <code>CubeView</code>s and <code>Axis</code>s to 
 * persist a certain cube state
 */
public interface VirtualCubeDefinition {

    /**
	 * Returns the source cube of this virtual cube. The source cube is nothing
	 * else than the cube which current state is saved.
	 * @return the source <code>Cube</code>
	 */
    Cube getSourceCube();

    /**
     * Returns the name to use for the virtual cube
     * <b>NOTE:</b> the virtual cube name must fulfill the constraints regarding
     * a cube name, namely its uniqueness within its containing database 
     * @return the virtual cube name
     */
    String getName();

    /**
     * Returns the virtual dimension definitions which build up this virtual
     * cube
     * @return the <code>VirtualDimensionDefinition</code>s
     */
    VirtualDimensionDefinition[] getVirtualDimensionDefinitions();
}
