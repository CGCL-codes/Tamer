package ray.mgocc.title;

public class Tortoise extends WeekTitle {

    public String getName() {
        return "TORTOISE";
    }

    public int getPriority() {
        return 14;
    }

    public int getPlayTimeThreshold() {
        return 3;
    }

    public void init() {
        super.init();
        add(new BoxRequirement());
    }

    class BoxRequirement extends Requirement {

        public String getName() {
            return "box";
        }

        private int getBoxCount() {
            return getValue("Personal Scores.Cardboard Box Uses");
        }

        private int getThreshold() {
            int round = getValue("Total.ROUND");
            return round * 15;
        }

        public double getRatio() {
            double ratio;
            int threshold = getThreshold();
            if (0 == threshold) {
                ratio = 0;
            } else {
                ratio = ((double) getBoxCount()) / threshold;
            }
            return ratio;
        }

        public String toString() {
            return super.toString() + "(" + getThreshold() + "<=" + getBoxCount() + ")";
        }
    }
}
