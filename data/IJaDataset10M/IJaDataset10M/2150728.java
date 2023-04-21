package org.gstreamer.lowlevel;

import org.gstreamer.ClockTime;
import org.gstreamer.GObject;
import org.gstreamer.controller.ControlSource;
import org.gstreamer.lowlevel.GObjectAPI.GObjectClass;
import org.gstreamer.lowlevel.GObjectAPI.GParamSpec;
import org.gstreamer.lowlevel.GValueAPI.GValue;
import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Pointer;

public interface GstControlSourceAPI extends Library {

    GstControlSourceAPI GSTCONTROLSOURCE_API = GstNative.load("gstcontroller", GstControlSourceAPI.class);

    int GST_PADDING = GstAPI.GST_PADDING;

    public static final class TimedValue extends com.sun.jna.Structure {

        public static final String GTYPE_NAME = "GstTimedValue";

        public volatile ClockTime timestamp;

        public volatile GValue value;
    }

    public static final class ValueArray extends com.sun.jna.Structure {

        public static final String GTYPE_NAME = "GstValueArray";

        public volatile String property_name;

        public volatile int nbsamples;

        public volatile ClockTime sample_interval;

        public volatile Pointer values;
    }

    public static interface GstControlSourceGetValue extends Callback {

        public boolean callback(ControlSource self, ClockTime timestamp, GValue value);
    }

    public static interface GstControlSourceGetValueArray extends Callback {

        public boolean callback(ControlSource self, ClockTime timestamp, ValueArray value_array);
    }

    public static interface GstControlSourceBind extends Callback {

        public boolean callback(ControlSource self, GParamSpec pspec);
    }

    public static final class GstControlSourceStruct extends com.sun.jna.Structure {

        public volatile GObject parent;

        public volatile GstControlSourceGetValue get_value;

        public volatile GstControlSourceGetValueArray get_value_array;

        public volatile boolean bound;

        public volatile Pointer[] _gst_reserved = new Pointer[GST_PADDING];
    }

    public static final class GstControlSourceClass extends com.sun.jna.Structure {

        public volatile GObjectClass parent_class;

        public volatile GstControlSourceBind bind;

        public volatile Pointer[] _gst_reserved = new Pointer[GST_PADDING];
    }

    GType gst_control_source_get_type();

    boolean gst_control_source_get_value(ControlSource self, ClockTime timestamp, GValue value);

    boolean gst_control_source_get_value_array(ControlSource self, ClockTime timestamp, ValueArray value_array);

    boolean gst_control_source_bind(ControlSource self, GParamSpec pspec);
}
