package srma;

import java.util.*;
import srma.*;

public class AlignHeapNodeComparator implements Comparator<AlignHeapNode> {

    private AlignHeap.HeapType type;

    public AlignHeapNodeComparator(AlignHeap.HeapType type) {
        this.type = type;
    }

    public int compare(AlignHeapNode a, AlignHeapNode b) {
        if (a.node.contig < b.node.contig) {
            return (AlignHeap.HeapType.MINHEAP == this.type) ? -1 : 1;
        } else if (a.node.contig > b.node.contig) {
            return (AlignHeap.HeapType.MINHEAP == this.type) ? 1 : -1;
        }
        if (a.node.position < b.node.position) {
            return (AlignHeap.HeapType.MINHEAP == this.type) ? -1 : 1;
        } else if (a.node.position > b.node.position) {
            return (AlignHeap.HeapType.MINHEAP == this.type) ? 1 : -1;
        }
        if (a.readOffset < b.readOffset) {
            return -1;
        } else if (a.readOffset > b.readOffset) {
            return 1;
        }
        if (a.node.type < b.node.type) {
            return -1;
        } else if (a.node.type > b.node.type) {
            return 1;
        }
        if (a.node.base < b.node.base) {
            return -1;
        } else if (a.node.base > b.node.base) {
            return 1;
        }
        if (a.score < b.score) {
            return -1;
        } else if (a.score > b.score) {
            return 1;
        }
        return 0;
    }
}
