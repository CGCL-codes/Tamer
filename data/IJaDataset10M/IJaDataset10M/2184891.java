package fastcol;

import java.util.LinkedList;

class PriorityQueue {

    public PriorityQueue() {
        priorities = new LinkedList[256];
        minPriority = 0;
        minPriorityIndex = 0;
        for (int i = 0; i < 256; i++) priorities[i] = new LinkedList();
    }

    public void add(int i, Object obj) {
        int j = (i - minPriority) + minPriorityIndex;
        if (j > 255) j -= 256;
        priorities[j].addLast(obj);
    }

    public Object getFirst() {
        int i = 0;
        while (priorities[minPriorityIndex].isEmpty()) {
            minPriorityIndex++;
            if (minPriorityIndex == 256) minPriorityIndex = 0;
            if (++i == 256) return null;
        }
        minPriority += i;
        Object obj = priorities[minPriorityIndex].removeFirst();
        return obj;
    }

    void reset() {
        minPriority = 0;
        minPriorityIndex = 0;
        for (int i = 0; i < 256; i++) priorities[i].clear();
    }

    private LinkedList priorities[];

    int minPriority;

    int minPriorityIndex;
}
