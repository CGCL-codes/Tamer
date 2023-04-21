package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.IsoTypeReader;
import java.io.IOException;
import java.nio.ByteBuffer;

@Descriptor(tags = 0x00)
public abstract class BaseDescriptor {

    int tag;

    int sizeOfInstance;

    int sizeBytes;

    public BaseDescriptor() {
    }

    public int getTag() {
        return tag;
    }

    public int getSize() {
        return sizeOfInstance + 1 + sizeBytes;
    }

    public int getSizeOfInstance() {
        return sizeOfInstance;
    }

    public int getSizeBytes() {
        return sizeBytes;
    }

    public final void parse(int tag, ByteBuffer bb) throws IOException {
        this.tag = tag;
        int i = 0;
        int tmp = IsoTypeReader.readUInt8(bb);
        i++;
        sizeOfInstance = tmp & 0x7f;
        while (tmp >>> 7 == 1) {
            tmp = IsoTypeReader.readUInt8(bb);
            i++;
            sizeOfInstance = sizeOfInstance << 7 | tmp & 0x7f;
        }
        sizeBytes = i;
        ByteBuffer detailSource = bb.slice();
        detailSource.limit(sizeOfInstance);
        parseDetail(detailSource);
        assert detailSource.remaining() == 0 : this.getClass().getSimpleName() + " has not been fully parsed";
        bb.position(bb.position() + sizeOfInstance);
    }

    public abstract void parseDetail(ByteBuffer bb) throws IOException;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("BaseDescriptor");
        sb.append("{tag=").append(tag);
        sb.append(", sizeOfInstance=").append(sizeOfInstance);
        sb.append('}');
        return sb.toString();
    }
}
