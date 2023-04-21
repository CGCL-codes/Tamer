package ray.mgocc.title;

public class Jaws extends WeekTitle {

    public String getName() {
        return "JAWS";
    }

    public int getPriority() {
        return 8;
    }

    public int getPlayTimeThreshold() {
        return 5;
    }

    public void init() {
        super.init();
        addKillRequirement(1.25);
        add(new KnifeRequirement());
    }

    class KnifeRequirement extends Requirement {

        public String getName() {
            return "knife";
        }

        public double getKnifeRatio() {
            int knife = getValue("Personal Scores.Knife Kills");
            int kill = getValue("Total.ALL.KILLS");
            return ((double) knife) / kill;
        }

        private double getThreshold() {
            return 0.075;
        }

        public double getRatio() {
            return getKnifeRatio() / getThreshold();
        }

        public String toString() {
            return super.toString() + "(" + getThreshold() + "<=" + format(getKnifeRatio()) + ")";
        }
    }
}
