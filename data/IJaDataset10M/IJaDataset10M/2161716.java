package com.jcraft.weirdx;

final class ClipPixmap implements Clip {

    Pixmap pixmap;

    ClipPixmap(Pixmap p) {
        super();
        pixmap = p;
    }

    public Object getMask() {
        return pixmap;
    }
}
