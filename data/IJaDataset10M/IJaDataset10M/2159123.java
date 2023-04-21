package org.simbrain.world.visionworld.filter.editor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Filter editors.
 */
public final class FilterEditors {

    /** Random filter editor. */
    public static final FilterEditor RANDOM = new RandomFilterEditor();

    /** Uniform filter editor. */
    public static final FilterEditor UNIFORM = new UniformFilterEditor();

    /** Pixel accumulator editor. */
    public static final FilterEditor PIXEL_ACCUMULATOR = new PixelAccumulatorEditor();

    /** Pixel accumulator editor. */
    public static final FilterEditor RGB = new RgbFilterEditor();

    /** Private array of filter editors. */
    private static final FilterEditor[] values = new FilterEditor[] { PIXEL_ACCUMULATOR, RANDOM, UNIFORM, RGB };

    /** Public list of filter editors. */
    public static final List<FilterEditor> VALUES = Collections.unmodifiableList(Arrays.asList(values));
}
