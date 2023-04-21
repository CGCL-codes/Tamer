package com.volantis.mcs.dissection.dom;

import java.io.PrintStream;

public class NodeCounts {

    public int elementCount;

    public int textCount;

    public int shardLinkCount;

    public int shardLinkGroupCount;

    public int shardLinkConditionalCount;

    public int dissectableAreaCount;

    public int keepTogetherCount;

    public int sharedContentCount;

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NodeCounts)) return false;
        final NodeCounts nodeCounts = (NodeCounts) o;
        if (dissectableAreaCount != nodeCounts.dissectableAreaCount) return false;
        if (elementCount != nodeCounts.elementCount) return false;
        if (keepTogetherCount != nodeCounts.keepTogetherCount) return false;
        if (shardLinkConditionalCount != nodeCounts.shardLinkConditionalCount) return false;
        if (shardLinkCount != nodeCounts.shardLinkCount) return false;
        if (shardLinkGroupCount != nodeCounts.shardLinkGroupCount) return false;
        if (textCount != nodeCounts.textCount) return false;
        if (sharedContentCount != nodeCounts.sharedContentCount) return false;
        return true;
    }

    public int hashCode() {
        int result;
        result = elementCount;
        result = 29 * result + textCount;
        result = 29 * result + shardLinkCount;
        result = 29 * result + shardLinkGroupCount;
        result = 29 * result + shardLinkConditionalCount;
        result = 29 * result + dissectableAreaCount;
        result = 29 * result + keepTogetherCount;
        result = 29 * result + sharedContentCount;
        return result;
    }

    public void print(PrintStream out) {
        out.println("Element Count: " + elementCount);
        out.println("Text Count: " + textCount);
        out.println("Dissectable Area Count: " + dissectableAreaCount);
        out.println("Shard Link Group Count: " + shardLinkGroupCount);
        out.println("Shard Link Count: " + shardLinkCount);
        out.println("Shard Link Conditional Count: " + shardLinkConditionalCount);
        out.println("Keep Together Count: " + keepTogetherCount);
        out.println("Shared Content Count: " + sharedContentCount);
    }
}
