package org.one.stone.soup.grfxML.plugin.function;

import org.one.stone.soup.grfxML.GrfxMLEngine;
import org.one.stone.soup.grfxML.plugin.SimplePlugin;
import org.one.stone.soup.grfxML.plugin.grfxMLCaster;
import org.one.stone.soup.mjdb.data.field.DataLinker;
import org.one.stone.soup.mjdb.data.field.Field;
import org.one.stone.soup.mjdb.data.field.LockException;

public class PointInverter extends SimplePlugin {

    public static final int ARG_WATCH = 0;

    public static final int ARG_NEGWATCH = 1;

    org.one.stone.soup.grfxML.DataPoint watch = new org.one.stone.soup.grfxML.DataPoint();

    org.one.stone.soup.grfxML.DataPoint negWatch = new org.one.stone.soup.grfxML.DataPoint();

    private int currentX;

    private int currentY;

    /**
 * StateInverter constructor comment.
 */
    public PointInverter(GrfxMLEngine engine) {
        super(engine);
    }

    /**
 * initialize method comment.
 */
    public void initialize() {
        try {
            currentX = watch.getValueX();
            currentY = watch.getValueY();
            negWatch.setValueX(-currentX, this);
            negWatch.setValueY(-currentY, this);
        } catch (LockException le) {
        }
    }

    /**
 * process method comment.
 */
    public void process() {
        try {
            if (currentX != watch.getValueX() || currentY != watch.getValueY()) {
                currentX = watch.getValueX();
                currentY = watch.getValueY();
                negWatch.setValueX(-currentX, this);
                negWatch.setValueY(-currentY, this);
            }
        } catch (LockException le) {
        }
    }

    /**
 * register method comment.
 */
    public void register(DataLinker store) {
        watch = grfxMLCaster.cast(watch, getArg(watch, ARG_WATCH, store));
        negWatch = grfxMLCaster.cast(negWatch, getArg(negWatch, ARG_NEGWATCH, store));
    }

    /**
 * replace method comment.
 */
    public void replace(Field oldObj, Field newObj) {
        watch = grfxMLCaster.replace(watch, oldObj, newObj);
        negWatch = grfxMLCaster.replace(negWatch, oldObj, newObj);
    }

    /**
 * stop method comment.
 */
    public void stop() {
    }
}
