package org.jcvi.profile;

/**
 * @author dkatzel
 *
 *
 */
public interface AlignmentProfile {

    AlignmentProfileElement getProfileElementFor(int offset);

    int getLength();
}
