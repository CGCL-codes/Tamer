package org.xmlvm.ios;

import java.util.*;
import org.xmlvm.XMLVMSkeletonOnly;

@XMLVMSkeletonOnly
public class CFSocketSignature {

    public int protocolFamily;

    public int socketType;

    public int protocol;

    public CFData address;

    /** Default constructor */
    CFSocketSignature() {
    }
}
