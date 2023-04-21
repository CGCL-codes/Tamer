package org.xmlvm.ios;

import java.util.*;
import org.xmlvm.XMLVMSkeletonOnly;

@XMLVMSkeletonOnly
public class MIDIControlTransform {

    public byte controlType;

    public byte remappedControlType;

    public short controlNumber;

    public short transform;

    public short param;

    /** Default constructor */
    MIDIControlTransform() {
    }
}
