package utils;

import java.util.ArrayList;
import java.util.List;

public class AgeGroup {

    private List<String> ageGroups = new ArrayList<String>();

    public AgeGroup() {
        ageGroups.add("18 - 24");
        ageGroups.add("25 - 29");
        ageGroups.add("30 - 34");
        ageGroups.add("35 - 39");
        ageGroups.add("40 - 44");
        ageGroups.add("45 - 49");
        ageGroups.add("50 - 54");
        ageGroups.add("55 - 59");
        ageGroups.add("60 - 64");
        ageGroups.add("65 - 69");
        ageGroups.add("70 - 74");
        ageGroups.add("75 - 79");
        ageGroups.add("80 - 84");
        ageGroups.add("85 - 89");
        ageGroups.add("90 - 94");
        ageGroups.add("95 - 99");
        ageGroups.add("100 - 104");
        ageGroups.add("105 - 109");
    }

    public List<String> getAgeGroups() {
        return ageGroups;
    }
}
