package org.ximtec.igesture.core;

import org.ximtec.igesture.util.additions3d.Note3D;

/**
 * Describes a gesture by a set of gesture samples.
 * 
 * @version 1.0, Dec 2006
 * @author Ueli Kurmann, igesture@uelikurmann.ch
 * @author Beat Signer, signer@inf.ethz.ch
 */
public class SampleDescriptor3D extends DefaultSampleDescriptor<Note3D> {

    @Override
    public String getName() {
        return SampleDescriptor3D.class.getSimpleName();
    }
}
