package people.needs;

import java.util.Comparator;

public class HumanNeedsComparator implements Comparator<IHumanNeeds> {

    private static HumanNeedsComparator instance;

    public static HumanNeedsComparator getInstance() {
        if (instance == null) {
            instance = new HumanNeedsComparator();
        }
        return instance;
    }

    private HumanNeedsComparator() {
    }

    public int compare(IHumanNeeds need1, IHumanNeeds need2) {
        if (need1.getNeed() > need2.getNeed()) {
            return -1;
        } else {
            if (need1.getNeed() < need2.getNeed()) {
                return 1;
            }
        }
        return 0;
    }
}
