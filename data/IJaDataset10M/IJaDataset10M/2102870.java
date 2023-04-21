package uk.co.caprica.vlcj.mac;

import java.awt.Dimension;
import com.apple.eawt.CocoaComponent;

/**
 * 
 */
public class VlcjMacVideoSurfaceCocoaComponent extends CocoaComponent {

    private static final long serialVersionUID = 1L;

    static {
        System.loadLibrary("VlcjMacVideoSurface");
    }

    public native long createNSViewLong();

    @Override
    public int createNSView() {
        return 0;
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(2, 2);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 340);
    }
}
