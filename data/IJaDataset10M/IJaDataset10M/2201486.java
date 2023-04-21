package org.one.stone.soup.grfxML.plugin.function;

import org.one.stone.soup.grfxML.GrfxMLEngine;
import org.one.stone.soup.grfxML.plugin.SimplePlugin;
import org.one.stone.soup.grfxML.plugin.grfxMLCaster;
import org.one.stone.soup.mjdb.data.field.DataLinker;
import org.one.stone.soup.mjdb.data.field.Field;
import org.one.stone.soup.mjdb.data.field.LockException;

public class StateInverter extends SimplePlugin {

    public static final int ARG_WATCH = 0;

    public static final int ARG_NOTWATCH = 1;

    org.one.stone.soup.grfxML.DataState watch = new org.one.stone.soup.grfxML.DataState();

    org.one.stone.soup.grfxML.DataState notWatch = new org.one.stone.soup.grfxML.DataState();

    private boolean currentState;

    /**
 * StateInverter constructor comment.
 */
    public StateInverter(GrfxMLEngine engine) {
        super(engine);
    }

    /**
 * initialize method comment.
 */
    public void initialize() {
        currentState = !watch.getValue();
    }

    /**
 * process method comment.
 */
    public void process() {
        try {
            if (currentState != watch.getValue()) {
                currentState = watch.getValue();
                notWatch.setValue(!currentState, this);
            }
        } catch (LockException le) {
        }
    }

    /**
 * register method comment.
 */
    public void register(DataLinker store) {
        watch = grfxMLCaster.cast(watch, getArg(watch, ARG_WATCH, store));
        notWatch = grfxMLCaster.cast(notWatch, getArg(notWatch, ARG_NOTWATCH, store));
    }

    /**
 * replace method comment.
 */
    public void replace(Field oldObj, Field newObj) {
        watch = grfxMLCaster.replace(watch, oldObj, newObj);
        notWatch = grfxMLCaster.replace(notWatch, oldObj, newObj);
    }

    /**
 * stop method comment.
 */
    public void stop() {
    }
}
