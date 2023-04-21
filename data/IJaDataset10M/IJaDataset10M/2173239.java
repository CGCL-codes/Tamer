package org.alliance.core.comm.rpc;

import org.alliance.core.comm.SearchHit;
import org.alliance.core.comm.T;
import org.alliance.core.file.filedatabase.FileDescriptor;
import org.alliance.core.file.hash.Hash;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * Sent when a friend needs information about the correct ip/port of a common friend of ours.
 *
 * Recieved when we need info about a friend that we haven't got the correct ip/port to.
 *
 * Created by IntelliJ IDEA.
 * User: maciek
 * Date: 2005-dec-28
 * Time: 18:42:48
 */
public class SearchHitsV2 extends CompressedRPC {

    public static final int MAX_SEARCH_HITS = 200;

    private ArrayList<SearchHit> hits = new ArrayList<SearchHit>();

    public SearchHitsV2() {
    }

    public void addHit(FileDescriptor fd) {
        hits.add(new SearchHit(fd));
    }

    public void serializeCompressed(DataOutputStream out) throws IOException {
        int n = hits.size();
        if (n > MAX_SEARCH_HITS) n = MAX_SEARCH_HITS;
        int i = 0;
        for (SearchHit sh : hits) {
            out.writeLong(sh.getSize());
            out.write(sh.getRoot().array());
            out.writeUTF(sh.getPath());
            out.writeByte((byte) sh.getHashedDaysAgo());
            i++;
            if (i > n) break;
        }
        out.writeLong(-1);
    }

    public void executeCompressed(DataInputStream in) throws IOException {
        for (; ; ) {
            long size = in.readLong();
            if (size == -1) break;
            Hash h = new Hash();
            in.readFully(h.array());
            String path = in.readUTF();
            int daysAgo = in.readUnsignedByte();
            hits.add(new SearchHit(h, path, size, daysAgo));
        }
        if (T.t) T.info("Received " + hits.size() + " hits.");
        manager.getCore().getUICallback().searchHits(fromGuid, hops, hits);
    }

    public int getNHits() {
        return hits.size();
    }
}
