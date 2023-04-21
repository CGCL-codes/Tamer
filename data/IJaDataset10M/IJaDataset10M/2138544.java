package eric;

import rene.zirkel.ZirkelCanvas;
import rene.zirkel.construction.Selector;
import rene.zirkel.graphics.TrackPainter;
import rene.zirkel.objects.ConstructionObject;
import rene.zirkel.objects.PointObject;
import rene.zirkel.objects.TrackObject;
import rene.zirkel.tools.ObjectTracker;

/**
 * 
 * @author erichake
 */
public class JLocusObjectTracker extends ObjectTracker implements TrackPainter, Runnable, Selector {

    public JLocusObjectTracker() {
    }

    public JLocusObjectTracker(final ConstructionObject p, final PointObject pm, final ConstructionObject o, final ZirkelCanvas zc, final boolean animate, final boolean paint, final ConstructionObject po[]) {
        super(p, pm, o, zc, animate, paint, po);
    }

    /**
	 * it simply create the locus object, without any animation
	 */
    @Override
    public void run() {
        animate(ZC);
    }

    /**
	 * No animation : it simply create the locus object
	 * 
	 * @param zc
	 */
    @Override
    public void animate(final ZirkelCanvas zc) {
        zc.getConstruction().DontPaint = true;
        TrackObject t = null;
        if (P instanceof PointObject) {
            t = new JLocusTrackObject(zc.getConstruction(), P, PO, PN, O, PM);
        } else {
            t = new TrackObject(zc.getConstruction(), P, PO, PN, O, PM);
        }
        t.setDefaults();
        zc.addObject(t);
        reset(zc);
        zc.validate();
        zc.getConstruction().DontPaint = false;
    }
}
