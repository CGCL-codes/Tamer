package org.gstreamer.message;

import org.gstreamer.GstObject;
import org.gstreamer.Message;
import org.gstreamer.lowlevel.GstMessageAPI;
import org.gstreamer.lowlevel.GstNative;
import com.sun.jna.Pointer;

/**
 * This message is generated and posted in the sink elements of a {@link org.gstreamer.Bin}.
 * The bin will only forward the EOS message to the application if all sinks 
 * have posted an EOS message.
 */
public class EOSMessage extends Message {

    private static interface API extends GstMessageAPI {

        Pointer ptr_gst_message_new_eos(GstObject src);
    }

    private static final API gst = GstNative.load(API.class);

    /**
     * Creates a new eos message.
     * @param init internal initialization data.
     */
    public EOSMessage(Initializer init) {
        super(init);
    }

    /**
     * Creates a new eos message.
     * @param src The object originating the message.
     */
    public EOSMessage(GstObject src) {
        this(initializer(gst.ptr_gst_message_new_eos(src)));
    }
}
