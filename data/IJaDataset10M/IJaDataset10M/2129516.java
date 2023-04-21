package android.ddm;

import org.apache.harmony.dalvik.ddmc.Chunk;
import org.apache.harmony.dalvik.ddmc.ChunkHandler;
import org.apache.harmony.dalvik.ddmc.DdmServer;
import android.util.Config;
import android.util.Log;
import java.nio.ByteBuffer;

/**
 * Handle an EXIT chunk.
 */
public class DdmHandleExit extends ChunkHandler {

    public static final int CHUNK_EXIT = type("EXIT");

    private static DdmHandleExit mInstance = new DdmHandleExit();

    private DdmHandleExit() {
    }

    /**
     * Register for the messages we're interested in.
     */
    public static void register() {
        DdmServer.registerHandler(CHUNK_EXIT, mInstance);
    }

    /**
     * Called when the DDM server connects.  The handler is allowed to
     * send messages to the server.
     */
    public void connected() {
    }

    /**
     * Called when the DDM server disconnects.  Can be used to disable
     * periodic transmissions or clean up saved state.
     */
    public void disconnected() {
    }

    /**
     * Handle a chunk of data.  We're only registered for "EXIT".
     */
    public Chunk handleChunk(Chunk request) {
        if (Config.LOGV) Log.v("ddm-exit", "Handling " + name(request.type) + " chunk");
        ByteBuffer in = wrapChunk(request);
        int statusCode = in.getInt();
        Runtime.getRuntime().halt(statusCode);
        return null;
    }
}
