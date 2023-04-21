package playground.balmermi.census2000.data;

public class MyPerson implements Comparable<MyPerson> {

    public final Integer p_id;

    public final Household hh;

    public int age;

    public boolean male;

    public boolean swiss;

    public boolean employed;

    public String car_avail;

    public boolean license;

    public int curr_educ;

    public int passed_educ;

    public MyPerson(Integer p_id, Household hh) {
        this.p_id = p_id;
        this.hh = hh;
    }

    public int compareTo(MyPerson other) {
        if (this.p_id < other.p_id) {
            return -1;
        } else if (this.p_id > other.p_id) {
            return 1;
        } else {
            return 0;
        }
    }

    public final int getAge() {
        return this.age;
    }

    public final String getSex() {
        if (male) {
            return "m";
        } else {
            return "f";
        }
    }

    public final boolean isMale() {
        return this.male;
    }

    public final String getLicense() {
        if (license) {
            return "yes";
        } else {
            return "no";
        }
    }

    public final boolean hasLicense() {
        return this.license;
    }

    public final String getCarAvail() {
        return car_avail;
    }

    public final String getEmployed() {
        if (employed) {
            return "yes";
        } else {
            return "no";
        }
    }

    public final Household getHousehold() {
        return this.hh;
    }

    public final boolean isSwiss() {
        return this.swiss;
    }

    @Override
    public final String toString() {
        return "[p_id=" + this.p_id + "]" + "[hh_id=" + this.hh.hh_id + "]" + "[age=" + this.age + "]" + "[male=" + this.male + "]" + "[swiss=" + this.swiss + "]" + "[employed=" + this.employed + "]" + "[car_avail=" + this.car_avail + "]" + "[license=" + this.license + "]" + "[curr_educ=" + this.curr_educ + "]" + "[passed_educ=" + this.passed_educ + "]";
    }
}
