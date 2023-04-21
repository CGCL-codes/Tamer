package org.rococoa.cocoa.foundation;

public abstract class NSImage extends NSObject {

    public abstract void setScalesWhenResized(boolean scaleWhenResizing);

    public abstract void setSize(NSSize size);

    public abstract NSData TIFFRepresentation();
}
