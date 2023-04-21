package alesis.fusion;

import alesis.fusion.chunks.SignedObject;
import java.nio.ByteBuffer;

/**
 *
 * @author jam
 * @todo Java does not allow to handle Smpl, Loop or Inst chunks, so it is
 * necessary to implement them in order to support wav to afs conversion
 */
public class WavSample {

    abstract static class Chunk implements SignedObject {

        String signature;

        ByteBuffer buffer;

        public String getSignature() {
            return (signature + "    ").substring(0, Constant.SIGNATURE_LENGTH);
        }

        public void setSignature(String signature) {
            this.signature = (signature + "    ").substring(0, Constant.SIGNATURE_LENGTH);
        }
    }

    static class SmplChunk extends Chunk {
    }

    static class InstChunk extends Chunk {
    }

    static class Loop {
    }
}
