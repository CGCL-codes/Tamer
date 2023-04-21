package net.sf.josceleton.core.api.entity.body;

/**
 * Kind of an <code>enum</code>.
 */
public interface BodyPart {

    /** @return a user display friendly text representation of this body part; e.g.: "Head" or "Left Hand". */
    String getLabel();

    /** @return the unique name of a joint part as known from osceleton; e.g.: "head" or "l_hand". */
    String getOsceletonId();
}
