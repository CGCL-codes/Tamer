package ray.mgocc.title;

public class Crocodile extends WeekTitle {

    public String getName() {
        return "CROCODILE";
    }

    public int getPriority() {
        return 9;
    }

    public int getPlayTimeThreshold() {
        return 5;
    }

    public void init() {
        super.init();
        addKillRequirement(1.5);
    }
}
