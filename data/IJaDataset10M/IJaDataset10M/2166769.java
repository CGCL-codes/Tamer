package org.magiclight.topology2;

import org.magiclight.common.MagiclightExceptionInvalidArgument;
import org.magiclight.topology2.validate.TopologyValidateException;

/**
 * A collection of faces<br>
 * Definition from ISO/CD 10303-42:1992: A connected_face_set is a set of faces such that the
 * domain of faces together with their bounding edges and vertices is connected.
 * <pre>
 * 1. The union of the domains of the faces and their bounding loops shall be arcwise connected.
 *</pre>
 *
 * @author Mtec030
 */
public abstract class TItemConnectedFaceSet extends TItem {

    /**
	 * The set of faces arcwise connected along common edges or vertices. 
	 */
    private TItemFace[] faces;

    protected TItemConnectedFaceSet(TSharedData sd) {
        super(sd);
    }

    @Override
    public int getItemType() {
        return IItemTypes.TYPE_SHELL;
    }

    public TItemFace[] getFaces() {
        return faces;
    }

    public void setFaces(TItemFace[] faces) {
        if (faces == null || faces.length == 0) throw new MagiclightExceptionInvalidArgument("point cannot be null or empty");
        this.faces = faces;
    }

    @Override
    public void validate() {
        if (faces == null || faces.length == 0) throw new TopologyValidateException("Face list cannot be null or an empty list", TopologyValidateException.ERROR_INVALID_MEMBER, 0);
    }

    public int getItemCount() {
        return faces.length;
    }

    @Override
    public TItem getItem(int index) {
        return faces[index];
    }
}
