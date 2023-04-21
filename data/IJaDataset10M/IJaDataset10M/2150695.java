package org.fgraph.twine;

import java.util.*;
import java.util.regex.*;
import org.fgraph.*;

/**
 *  Parses a status node's text looking for hashtags and
 *  creates appropriate graph structures to link them.
 *
 *  @version   $Revision: 564 $
 *  @author    Paul Speed
 */
public class HashTagParser {

    private static Pattern HASH = Pattern.compile("#\\w+");

    private TwitterGraphAccess access;

    public HashTagParser(TwitterGraphAccess access) {
        this.access = access;
    }

    public int process(Node status) {
        if (Boolean.TRUE.equals(status.get("truncated"))) return 0;
        String s = status.get("text");
        if (s == null) return 0;
        int count = 0;
        Matcher m = HASH.matcher(s);
        while (m.find()) {
            String h = m.group();
            Node hashNode = access.getNode(TwitterGraphAccess.PROP_HASH_KEY, h.toLowerCase(), true);
            if (status.connection(hashNode, TwitterEdges.EDGE_HASHTAG, Direction.OUT) == null) {
                count++;
                status.addEdge(hashNode, TwitterEdges.EDGE_HASHTAG);
            }
        }
        return count;
    }
}
