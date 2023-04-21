package ircam.jmax.editors.sequence.track;

import ircam.jmax.editors.sequence.*;
import ircam.jmax.editors.sequence.renderers.*;
import ircam.jmax.editors.sequence.menus.*;
import ircam.jmax.toolkit.*;
import java.awt.*;
import java.beans.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

/**
 * A Monodimensional view for a generic Sequence. 
 * This kind of editor use a MonoDimensionalAdapter
 * to map the y values. The value returned is always half of the panel,
 * and settings of y are simply ignored. */
public class FloatTrackEditor extends MonoTrackEditor {

    public FloatTrackEditor(Geometry g, Track trk, boolean isInSequence) {
        super(g, trk, isInSequence);
        if (track.getProperty("maximumValue") == null) track.setProperty("maximumValue", new Float(FloatValue.DEFAULT_MAX_VALUE));
        if (track.getProperty("minimumValue") == null) track.setProperty("minimumValue", new Float(FloatValue.DEFAULT_MIN_VALUE));
        if (track.getProperty("viewMode") == null) track.setProperty("viewMode", new Integer(viewMode));
        setRenderer(new IntegerTrackRenderer(gc));
        super.setAdapter(new FloatAdapter(geometry, gc, MONODIMENSIONAL_TRACK_OFFSET));
    }

    public void reinit() {
        track.setProperty("maximumValue", new Float(FloatValue.DEFAULT_MAX_VALUE));
        track.setProperty("minimumValue", new Float(FloatValue.DEFAULT_MIN_VALUE));
        setViewMode(PEAKS_VIEW);
    }

    void updateRange(Object obj) {
        float max = ((IntegerAdapter) gc.getAdapter()).getMaximumValue();
        float min = ((IntegerAdapter) gc.getAdapter()).getMinimumValue();
        float value = ((Float) ((TrackEvent) obj).getProperty("value")).floatValue();
        if (value > max) track.setProperty("maximumValue", new Float(value + 1));
        if (value < min) track.setProperty("minimumValue", new Float(value - 1));
    }

    int viewMode = PEAKS_VIEW;

    public static final int PEAKS_VIEW = 2;

    public static final int STEPS_VIEW = 3;

    public static final int BREAK_POINTS_VIEW = 4;
}
