package org.ascape.examples.bions;

import org.ascape.model.Agent;
import org.ascape.model.LocatedAgent;
import org.ascape.model.Scape;
import org.ascape.model.rule.Rule;
import org.ascape.model.space.Continuous2D;
import org.ascape.model.space.Coordinate2DContinuous;
import org.ascape.model.space.SubContinuous2D;
import org.ascape.util.Conditional;
import org.ascape.util.vis.ColorFeature;
import org.ascape.view.vis.Overhead2DContinuousView;

public class Model_Q_ContinuousSpace extends Scape {

    /**
     * 
     */
    private static final long serialVersionUID = 6908142220214361373L;

    private Scape territory;

    private Scape redBions;

    private Scape orangeBions;

    private Scape blueBions;

    private int initialSize = 100;

    private double minVision = 10;

    private double maxVision = 15;

    private double minVelocity = 1.0;

    private double maxVelocity = 2.0;

    public class Bion extends LocatedAgent {

        /**
         * 
         */
        private static final long serialVersionUID = 1465257738623963424L;

        protected double vision;

        protected double velocity;

        public void initialize() {
            super.initialize();
            vision = randomInRange(minVision, maxVision);
            velocity = randomInRange(minVelocity, maxVelocity);
            setAgentSize(1);
        }

        public double getVision() {
            return vision;
        }

        public void setVision(double vision) {
            this.vision = vision;
        }

        public double getVelocity() {
            return velocity;
        }

        public void setVelocity(double velocity) {
            this.velocity = velocity;
        }
    }

    class RedBion extends Bion {

        /**
         * 
         */
        private static final long serialVersionUID = -8087225336656891911L;

        public Object getPlatformColor() {
            return ColorFeature.RED;
        }

        public String getName() {
            return "Red Bion at " + RedBion.this.getCoordinate();
        }
    }

    class OrangeBion extends Bion {

        /**
         * 
         */
        private static final long serialVersionUID = -6419258005253043572L;

        public Object getPlatformColor() {
            return ColorFeature.ORANGE;
        }

        public String getName() {
            return "Orange Bion at " + OrangeBion.this.getCoordinate();
        }
    }

    class BlueBion extends Bion {

        /**
         * 
         */
        private static final long serialVersionUID = 7255175846367767254L;

        public Object getPlatformColor() {
            return ColorFeature.BLUE;
        }

        public String getName() {
            return "Blue Bion at " + BlueBion.this.getCoordinate();
        }
    }

    class MoveTowardConditionRule extends Rule {

        /**
         * 
         */
        private static final long serialVersionUID = 7232297596488751244L;

        String name;

        Conditional condition;

        public MoveTowardConditionRule(String name, Conditional condition) {
            super(name);
            this.name = name;
            this.condition = condition;
        }

        public void execute(Agent a) {
            LocatedAgent target = territory.findNearest(((LocatedAgent) a).getCoordinate(), condition, false, ((Bion) a).getVision());
            if (target != null) {
                ((Bion) a).moveToward(target.getCoordinate(), ((Bion) a).getVelocity());
            }
        }
    }

    ;

    class MoveAwayConditionRule extends MoveTowardConditionRule {

        /**
         * 
         */
        private static final long serialVersionUID = -9171389514833109396L;

        public MoveAwayConditionRule(String name, Conditional condition) {
            super(name, condition);
        }

        public void execute(Agent a) {
            LocatedAgent target = territory.findNearest(((LocatedAgent) a).getCoordinate(), condition, false, ((Bion) a).getVision());
            if (target != null) {
                ((Bion) a).moveAway(target.getCoordinate(), ((Bion) a).getVelocity());
            }
        }
    }

    ;

    public static Conditional CONTAINS_RED = new Conditional() {

        /**
         * 
         */
        private static final long serialVersionUID = -3845517710729322797L;

        public boolean meetsCondition(Object o) {
            return o instanceof RedBion;
        }
    };

    public static Conditional CONTAINS_ORANGE = new Conditional() {

        /**
         * 
         */
        private static final long serialVersionUID = 8377185056087828144L;

        public boolean meetsCondition(Object o) {
            return o instanceof OrangeBion;
        }
    };

    public static Conditional CONTAINS_BLUE = new Conditional() {

        /**
         * 
         */
        private static final long serialVersionUID = 1513704421006648535L;

        public boolean meetsCondition(Object o) {
            return o instanceof BlueBion;
        }
    };

    public final Rule MOVE_TOWARD_RED_RULE = new MoveTowardConditionRule("Move Toward Red", CONTAINS_RED);

    public final Rule MOVE_TOWARD_ORANGE_RULE = new MoveTowardConditionRule("Move Toward Orange", CONTAINS_ORANGE);

    public final Rule MOVE_TOWARD_BLUE_RULE = new MoveTowardConditionRule("Move Toward Blue", CONTAINS_BLUE);

    public final Rule MOVE_AWAY_RED_RULE = new MoveAwayConditionRule("Move Away Red", CONTAINS_RED);

    public final Rule MOVE_AWAY_ORANGE_RULE = new MoveAwayConditionRule("Move Away Orange", CONTAINS_ORANGE);

    public final Rule MOVE_AWAY_BLUE_RULE = new MoveAwayConditionRule("Move Away Blue", CONTAINS_BLUE);

    public void createScape() {
        super.createScape();
        territory = new Scape(new Continuous2D());
        territory.setName("Bionland");
        territory.setExtent(new Coordinate2DContinuous(100.0, 100.0));
        Bion protoBion = new Bion();
        protoBion.initialize();
        territory.setPrototypeAgent(protoBion);
        territory.setAutoCreate(false);
        territory.setPeriodic(true);
        add(territory);
        territory.setExecutionOrder(RULE_ORDER);
        redBions = new Scape(new SubContinuous2D());
        createBions(redBions, new RedBion(), "Red");
        redBions.getRules().setSelected(MOVE_TOWARD_ORANGE_RULE, true);
        redBions.getRules().setSelected(MOVE_AWAY_BLUE_RULE, true);
        orangeBions = new Scape(new SubContinuous2D());
        createBions(orangeBions, new OrangeBion(), "Orange");
        orangeBions.getRules().setSelected(MOVE_TOWARD_BLUE_RULE, true);
        orangeBions.getRules().setSelected(MOVE_AWAY_RED_RULE, true);
        blueBions = new Scape(new SubContinuous2D());
        createBions(blueBions, new BlueBion(), "Blue");
        blueBions.getRules().setSelected(MOVE_TOWARD_RED_RULE, true);
        blueBions.getRules().setSelected(MOVE_AWAY_ORANGE_RULE, true);
    }

    public void initialize() {
        super.initialize();
        territory.clear();
        redBions.setSize(initialSize);
        orangeBions.setSize(initialSize);
        blueBions.setSize(initialSize);
    }

    protected void createBions(Scape bions, LocatedAgent protoAgent, String name) {
        bions.setSuperScape(territory);
        bions.setName(name);
        bions.setPrototypeAgent(protoAgent);
        bions.addInitialRule(MOVE_RANDOM_LOCATION_RULE);
        bions.addRule(MOVE_TOWARD_RED_RULE, false);
        bions.addRule(MOVE_TOWARD_ORANGE_RULE, false);
        bions.addRule(MOVE_TOWARD_BLUE_RULE, false);
        bions.addRule(MOVE_AWAY_RED_RULE, false);
        bions.addRule(MOVE_AWAY_ORANGE_RULE, false);
        bions.addRule(MOVE_AWAY_BLUE_RULE, false);
        add(bions);
    }

    public void createViews() {
        super.createViews();
        Overhead2DContinuousView mapView = new Overhead2DContinuousView();
        territory.addView(mapView);
    }

    public int getInitialPopulationSize() {
        return initialSize;
    }

    public void setInitialPopulationSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public double getMinimumVision() {
        return minVision;
    }

    public void setMinimumVision(double minVision) {
        this.minVision = minVision;
    }

    public double getMaximumVision() {
        return maxVision;
    }

    public void setMaximumVision(double maxVision) {
        this.maxVision = maxVision;
    }

    public double getMinVelocity() {
        return minVelocity;
    }

    public void setMinVelocity(double minVelocity) {
        this.minVelocity = minVelocity;
    }

    public double getMaxVelocity() {
        return maxVelocity;
    }

    public void setMaxVelocity(double maxVelocity) {
        this.maxVelocity = maxVelocity;
    }

    public boolean isPeriodic() {
        return territory.isPeriodic();
    }

    public void setPeriodic(boolean periodic) {
        territory.setPeriodic(periodic);
    }
}
