package org.j3d.renderer.java3d.loaders;

import com.sun.j3d.loaders.LoaderBase;
import org.j3d.loaders.HeightMapSource;

/**
 * Base Loader definition for all loaders that can load terrain data.
 * <p>
 *
 *
 * @author  Justin Couch
 * @version $Revision: 1.1 $
 */
public abstract class HeightMapLoader extends LoaderBase implements HeightMapSource {

    /**
     * Construct a new default loader with no flags set
     */
    public HeightMapLoader() {
    }

    /**
     * Construct a new loader with the given flags set.
     *
     * @param flags The list of flags to be set
     */
    public HeightMapLoader(int flags) {
        super(flags);
    }
}
