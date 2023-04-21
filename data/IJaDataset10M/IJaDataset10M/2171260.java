package org.ncsa.foodlog.data.profiles;

/**
 * import java.io.IOException
 */
public class PhysicalActivity extends Profile {

    public PhysicalActivity() {
        super(true);
    }

    public PhysicalActivity(String aName, int aCalories) {
        super(aName, aCalories, true);
    }

    public PhysicalActivity(String aName) {
        super(aName, true);
    }

    public PhysicalActivity(PhysicalActivity other) {
        super(other);
    }

    public int compareTo(Object arg0) {
        if (arg0 instanceof Profile) {
            Profile other = (Profile) arg0;
            if (arg0 instanceof Meal) return 1;
            int result = getTag().compareTo(other.getTag());
            if (result != 0) {
                return result;
            }
        }
        return 1;
    }

    public Profile cloneAndScale(int multiple) {
        PhysicalActivity result = new PhysicalActivity(this);
        result.scale(multiple);
        return result;
    }

    public String formattedString() {
        return (formats.formatTitle("PA: " + getTag().getName(), "delimited.name") + formats.formatNumberWithSpacer(getCalories(), "delimited.totalCalories"));
    }
}
