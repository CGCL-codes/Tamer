package org.gstreamer.message;

import org.gstreamer.Format;
import org.gstreamer.GstObject;
import org.gstreamer.Message;
import org.gstreamer.lowlevel.GstMessageAPI;
import org.gstreamer.lowlevel.GstNative;
import com.sun.jna.Pointer;

/**
 * This message is posted by elements that finish playback of a segment as a 
 * result of a segment seek. 
 * <p>
 * This message is received by the application after all elements that posted a segment_start
 * have posted the segment_done.
 */
public class SegmentDoneMessage extends Message {

    private static interface API extends GstMessageAPI {

        Pointer ptr_gst_message_new_segment_done(GstObject src, Format format, long position);
    }

    private static final API gst = GstNative.load(API.class);

    /**
     * Creates a new segment-done message.
     * 
     * @param init internal initialization data.
     */
    public SegmentDoneMessage(Initializer init) {
        super(init);
    }

    /**
     * Creates a new segment done message.
     * 
     * @param src the object originating the message.
     * @param format the format of the position being done
     * @param position the position of the segment being done
     */
    public SegmentDoneMessage(GstObject src, Format format, long position) {
        this(initializer(gst.ptr_gst_message_new_segment_done(src, format, position)));
    }

    /**
     * Gets the format of the position in this message.
     * 
     * @return the format of the position.
     */
    public Format getFormat() {
        Format[] format = new Format[1];
        gst.gst_message_parse_segment_done(this, format, null);
        return format[0];
    }

    /**
     * Gets the position of the segment that is done.
     * 
     * @return the position.
     */
    public long getPosition() {
        long[] position = { 0 };
        gst.gst_message_parse_segment_done(this, null, position);
        return position[0];
    }
}
