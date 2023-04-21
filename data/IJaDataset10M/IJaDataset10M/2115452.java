package org.thechiselgroup.choosel.protovis.client;

import org.thechiselgroup.choosel.protovis.client.jsutil.JsBooleanFunction;
import org.thechiselgroup.choosel.protovis.client.jsutil.JsDoubleFunction;
import org.thechiselgroup.choosel.protovis.client.jsutil.JsFunction;
import org.thechiselgroup.choosel.protovis.client.jsutil.JsStringFunction;

/**
 * 
 * @author Bradley Blashko
 */
public final class PVLine extends PVAbstractMark<PVLine> {

    public static final class Type extends PVMarkType<PVLine> {

        protected Type() {
        }
    }

    /**
     * @see #lineJoin(String)
     */
    public static final String LINE_JOIN_BEVEL = "bevel";

    /**
     * @see #lineJoin(String)
     */
    public static final String LINE_JOIN_NONE = null;

    /**
     * @see #lineJoin(String)
     */
    public static final String LINE_JOIN_MITER = "miter";

    /**
     * @see #lineJoin(String)
     */
    public static final String LINE_JOIN_ROUND = "round";

    /**
     * @see #lineJoin(String)
     */
    public static final String LINE_JOIN_LINEAR = "linear";

    public static native PVLine create();

    protected PVLine() {
    }

    public final native PVLine antialias(boolean antialias);

    public final native double eccentricity();

    public final native PVLine eccentricity(double eccentricity);

    public final native PVLine eccentricity(JsDoubleFunction f);

    public final native PVColor fillStyle();

    public final native PVLine fillStyle(JsFunction<PVColor> f);

    public final native PVLine fillStyle(JsStringFunction f);

    public final native PVLine fillStyle(PVColor color);

    public final native PVLine fillStyle(String color);

    /**
     * @see PVInterpolationMethod
     */
    public final native String interpolate();

    /**
     * @see PVInterpolationMethod
     */
    public final native PVLine interpolate(JsStringFunction f);

    /**
     * @see PVInterpolationMethod
     */
    public final native PVLine interpolate(String interpolate);

    public final native String lineJoin();

    /**
     * @see #LINE_JOIN_BEVEL
     * @see #LINE_JOIN_MITER
     * @see #LINE_JOIN_ROUND
     * @see #LINE_JOIN_NONE
     * @see #LINE_JOIN_LINEAR
     */
    public final native PVLine lineJoin(JsStringFunction f);

    /**
     * @see #LINE_JOIN_BEVEL
     * @see #LINE_JOIN_MITER
     * @see #LINE_JOIN_ROUND
     * @see #LINE_JOIN_NONE
     * @see #LINE_JOIN_LINEAR
     */
    public final native PVLine lineJoin(String lineJoin);

    public final native double lineWidth();

    public final native PVLine lineWidth(double lineWidth);

    public final native PVLine lineWidth(JsDoubleFunction f);

    public final native boolean segmented();

    public final native PVLine segmented(boolean segmented);

    public final native PVLine segmented(JsBooleanFunction f);

    public final native PVColor strokeStyle();

    public final native PVLine strokeStyle(JsFunction<PVColor> f);

    public final native PVLine strokeStyle(JsStringFunction f);

    public final native PVLine strokeStyle(PVColor strokeStyle);

    public final native PVLine strokeStyle(String strokeStyle);

    public final native double tension();

    public final native PVLine tension(double tension);

    public final native PVLine tension(JsDoubleFunction f);
}
