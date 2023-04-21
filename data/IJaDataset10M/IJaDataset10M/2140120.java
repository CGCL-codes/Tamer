package org.dcm4che2.iod.module.dx;

import org.dcm4che2.data.DicomObject;
import org.dcm4che2.iod.module.composite.GeneralImageModule;

/**
 * @author Antonio Magni <dcm4ceph@antoniomagni.org>
 * @author Gunter Zeilinger <gunterze@gmail.com>
 *
 */
public class MammographyImageModule extends GeneralImageModule {

    public MammographyImageModule(DicomObject dcmobj) {
        super(dcmobj);
    }
}
