package org.dcm4che2.iod.module;

import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.iod.validation.ValidationContext;
import org.dcm4che2.iod.validation.ValidationResult;

/**
 * @author gunter zeilinger(gunterze@gmail.com)
 * @version $Revision: 720 $ $Date: 2006-11-26 12:40:54 -0500 (Sun, 26 Nov 2006) $
 * @since Jun 9, 2006
 * 
 */
public class Module {

    private static final int[] INT0 = {};

    protected final DicomObject dcmobj;

    public Module(DicomObject dcmobj) {
        if (dcmobj == null) {
            throw new NullPointerException("dcmobj");
        }
        this.dcmobj = dcmobj;
    }

    public DicomObject getDicomObject() {
        return dcmobj;
    }

    protected int[] getType1Tags() {
        return INT0;
    }

    protected int[] getType2Tags() {
        return INT0;
    }

    public void init() {
        int[] tags = getType2Tags();
        for (int i = 0; i < tags.length; i++) {
            dcmobj.putNull(tags[i], null);
        }
    }

    /**
	 * Check validity of this Module.
	 * <p>
	 * Checks all Type 1 tags to make sure they exist and are non-zero. Check
	 * all Type 2 tags to make sure they exist.
	 * 
	 * @param ctx
	 * @param result
	 */
    public void validate(ValidationContext ctx, ValidationResult result) {
        int[] tags = getType1Tags();
        for (int i = 0; i < tags.length; i++) {
            if (!dcmobj.containsValue(tags[i])) {
                if (!dcmobj.contains(tags[i])) {
                    result.logMissingAttribute(tags[i]);
                } else {
                    result.logMissingValue(tags[i]);
                }
            }
        }
        tags = getType2Tags();
        for (int i = 0; i < tags.length; i++) {
            if (!dcmobj.contains(tags[i])) {
                result.logMissingAttribute(tags[i]);
            }
        }
    }

    protected void updateSequence(int tag, Module module) {
        if (module != null) {
            dcmobj.putNestedDicomObject(tag, module.getDicomObject());
        } else {
            dcmobj.remove(Tag.ReferencedStudySequence);
        }
    }

    protected void updateSequence(int tag, Module[] module) {
        if (module != null) {
            DicomElement sq = dcmobj.putSequence(tag);
            for (int i = 0; i < module.length; i++) {
                sq.addDicomObject(module[i].getDicomObject());
            }
        } else {
            dcmobj.remove(tag);
        }
    }
}
