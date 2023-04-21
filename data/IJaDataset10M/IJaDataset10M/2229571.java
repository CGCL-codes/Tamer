package ray.mgocc.title;

public class Bee extends WeekTitle {

    public String getName() {
        return "BEE";
    }

    public int getPriority() {
        return 10;
    }

    public int getPlayTimeThreshold() {
        return 3;
    }

    public void init() {
        super.init();
        add(new ScanRequirement());
    }

    class ScanRequirement extends Requirement {

        public String getName() {
            return "scan";
        }

        public double getScanRatio() {
            int round = getValue("Total.ROUND");
            int scan = getValue("Personal Scores.Scans Performed");
            return ((double) scan) / round;
        }

        private double getThreshold() {
            return 1.0;
        }

        public double getRatio() {
            return getScanRatio() / getThreshold();
        }

        public String toString() {
            return super.toString() + "(" + getThreshold() + "<=" + format(getScanRatio()) + ")";
        }
    }
}
