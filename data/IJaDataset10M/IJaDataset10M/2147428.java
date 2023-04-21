package src.backend.math.select;

import java.util.List;

public class AbstractSelectData {

    private int index;

    protected AbstractSelectData(int ind) {
        this.index = ind;
    }

    public int getIndex() {
        return index;
    }

    public static int contains(List<AbstractSelectData> list, int targetIndex) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIndex() == targetIndex) {
                return i;
            }
        }
        return -1;
    }
}
