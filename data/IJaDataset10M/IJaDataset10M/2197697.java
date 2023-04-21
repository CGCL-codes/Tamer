package org.ogre4j;

import org.xbig.base.*;

public interface IPlaneBoundedVolumeList extends INativeObject, org.std.Ivector<org.ogre4j.IPlaneBoundedVolume> {

    /** **/
    public void assign(int num, org.ogre4j.IPlaneBoundedVolume val);

    /** **/
    public org.ogre4j.IPlaneBoundedVolume at(int loc);

    /** **/
    public org.ogre4j.IPlaneBoundedVolume back();

    /** **/
    public int capacity();

    /** **/
    public void clear();

    /** **/
    public boolean empty();

    /** **/
    public org.ogre4j.IPlaneBoundedVolume front();

    /** **/
    public int max_size();

    /** **/
    public void pop_back();

    /** **/
    public void push_back(org.ogre4j.IPlaneBoundedVolume val);

    /** **/
    public void reserve(int size);

    /** **/
    public int size();
}
