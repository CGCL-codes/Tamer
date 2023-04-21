package com.go.teaservlet.io;

import java.io.IOException;

/******************************************************************************
 * A ByteBuffer implementation that initially stores its data in a
 * DefaultByteBuffer, but after a certain threshold is reached, spills over
 * into a FileByteBuffer.
 *
 * @author Brian S O'Neill
 * @version
 * <!--$$Revision: 3 $-->, <!--$$JustDate:--> 01/02/20 <!-- $-->
 * @deprecated Moved to com.go.trove.io package.
 */
public class SpilloverByteBuffer extends com.go.trove.io.SpilloverByteBuffer implements ByteBuffer {

    public SpilloverByteBuffer(Group group) {
        super(group);
    }

    public void appendSurrogate(ByteData s) throws IOException {
        super.appendSurrogate(s);
    }

    public void addCaptureBuffer(ByteBuffer buffer) throws IOException {
        super.addCaptureBuffer(buffer);
    }

    public void removeCaptureBuffer(ByteBuffer buffer) throws IOException {
        super.removeCaptureBuffer(buffer);
    }

    public abstract static class Group extends com.go.trove.io.SpilloverByteBuffer.Group {

        public Group(long threshold) {
            super(threshold);
        }
    }
}
