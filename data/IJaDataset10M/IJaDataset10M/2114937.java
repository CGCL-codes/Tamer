package simulation.visual;

import java.awt.Color;
import java.awt.Graphics;
import simulation.model.DynamicObject;
import simulation.model.SimObject;
import simulation.model.Simulator;

public class SimulationLayer implements IScreenLayer {

    Simulator simulator;

    WorldScreenMapper converter;

    public static final int SELECTED_TO_SHOW = 0;

    public static final int SELECTED_TO_MOVE = 1;

    public static final int SELECTED_TO_ROTATE = 2;

    int selectionType = -1;

    /**
	 * Selected with mouse double click, to move or resize or delete..
	 */
    private SimObject selectedObject;

    /**
	 * Selected robot!..
	 */
    private DynamicObject selectedRobot;

    /**
	 * The robot, whose target is selected!.
	 */
    private DynamicObject selectedTarget;

    /**
	 * Which point is selected for the selected object.
	 * So while moving, mouse will be at this point always.
	 * It will provide a better moving opportunity..
	 */
    private double[] selectionPoint;

    /**
	 * To draw the obstacles, robots, and their targets..
	 */
    private SimObjectDrawer drawer;

    public SimulationLayer(Simulator simulator, WorldScreenMapper converter, SimObjectDrawer drawer) {
        this.simulator = simulator;
        this.converter = converter;
        this.drawer = drawer;
    }

    /**
     * Select the object which contains the screenPoint..
     * @param screenPoint
     * @return selected object or null..
     */
    public boolean selectObject(double[] screenPoint, int selectionType) {
        if (selectionType != SELECTED_TO_MOVE && selectionType != SELECTED_TO_ROTATE && selectionType != SELECTED_TO_SHOW) return false;
        this.selectionType = selectionType;
        selectedRobot = null;
        selectedObject = null;
        selectedTarget = null;
        double[] worldPoint = new double[2];
        converter.screenToWorld(screenPoint, worldPoint);
        for (DynamicObject co : simulator.worldContent.dynamicObjects) {
            if (co.targetContains(worldPoint)) {
                selectedTarget = co;
                selectionPoint = selectedTarget.targetLocalPosition(worldPoint);
                return true;
            }
        }
        for (DynamicObject co : simulator.worldContent.dynamicObjects) {
            if (co.contains(worldPoint)) {
                selectedRobot = co;
                selectionPoint = selectedRobot.localPosition(worldPoint);
                return true;
            }
        }
        for (SimObject co : simulator.worldContent.staticObjects) {
            if (co.contains(worldPoint)) {
                selectedObject = co;
                selectionPoint = selectedObject.localPosition(worldPoint);
                return true;
            }
        }
        return false;
    }

    /**
     * If object really translated return true, otherwise return false..
     * @param screenPoint
     * @returnCircle
     */
    public boolean translate(double[] screenPoint) {
        if (selectionType == SELECTED_TO_MOVE) {
            moveSelected(screenPoint);
            return true;
        } else if (selectionType == SELECTED_TO_ROTATE) {
            rotateSelected(screenPoint);
            return true;
        }
        return false;
    }

    public void moveSelected(double[] screenPoint) {
        double[] worldPoint = new double[2];
        converter.screenToWorld(screenPoint, worldPoint);
        if (selectedObject != null) {
            double[] position = selectedObject.getPosition();
            position[0] = worldPoint[0] - selectionPoint[0];
            position[1] = worldPoint[1] - selectionPoint[1];
        } else if (selectedRobot != null) {
            double[] position = selectedRobot.getPosition();
            position[0] = worldPoint[0] - selectionPoint[0];
            position[1] = worldPoint[1] - selectionPoint[1];
        } else if (selectedTarget != null) {
            double[] target = selectedTarget.getTarget();
            target[0] = worldPoint[0] - selectionPoint[0];
            target[1] = worldPoint[1] - selectionPoint[1];
        }
    }

    public void rotateSelected(double[] screenPoint) {
        double[] worldPoint = new double[2];
        converter.screenToWorld(screenPoint, worldPoint);
        double[] posDif = { 0, 0 };
        if (selectedObject != null) {
            double[] position = selectedObject.getPosition();
            posDif[0] = worldPoint[0] - position[0];
            posDif[1] = worldPoint[1] - position[1];
        } else if (selectedRobot != null) {
            double[] position = selectedRobot.getPosition();
            posDif[0] = worldPoint[0] - position[0];
            posDif[1] = worldPoint[1] - position[1];
        } else if (selectedTarget != null) {
            double[] target = selectedTarget.getTarget();
            posDif[0] = worldPoint[0] - target[0];
            posDif[1] = worldPoint[1] - target[1];
        }
        double theta = Math.atan2(posDif[1], posDif[0]);
        if (selectedObject != null) {
            double[] position = selectedObject.getPosition();
            position[2] = theta;
        } else if (selectedRobot != null) {
            double[] position = selectedRobot.getPosition();
            position[2] = theta;
        } else if (selectedTarget != null) {
            double[] target = selectedTarget.getTarget();
            target[2] = theta;
        }
    }

    public void paint(Graphics g) {
        Color savedColor = g.getColor();
        for (SimObject obstacle : simulator.worldContent.staticObjects) {
            drawer.fillObstacle(g, obstacle.color, Color.LIGHT_GRAY, obstacle.getPosition(), obstacle.getShapeId(), obstacle.getScale(), false);
        }
        if (selectedObject != null) {
            drawer.fillObstacle(g, Color.YELLOW, Color.RED, selectedObject.getPosition(), selectedObject.getShapeId(), selectedObject.getScale(), false);
            drawer.drawNoOrientation(g, Color.BLACK, selectedObject.getPosition(), selectedObject.getShapeId(), selectedObject.getScale());
        }
        for (DynamicObject robot : simulator.worldContent.dynamicObjects) {
            drawer.fillObstacle(g, robot.color, Color.YELLOW, robot.getPosition(), robot.getShapeId(), robot.getScale(), false);
        }
        if (selectedRobot != null) {
            drawer.fillObstacle(g, Color.YELLOW, Color.RED, selectedRobot.getPosition(), selectedRobot.getShapeId(), selectedRobot.getScale(), false);
            drawer.drawNoOrientation(g, Color.BLACK, selectedRobot.getPosition(), selectedRobot.getShapeId(), selectedRobot.getScale());
        }
        for (DynamicObject robot : simulator.worldContent.dynamicObjects) {
            if (robot.getTarget() != null) {
                drawer.fillObstacle(g, robot.targetColor, Color.RED, robot.getTarget(), robot.getShapeId(), robot.getScale() * 2.0 / 3.0, true);
            }
        }
        if (selectedRobot != null) {
            drawer.fillObstacle(g, Color.BLACK, Color.RED, selectedRobot.getTarget(), selectedRobot.getShapeId(), selectedRobot.getScale() * 2.0 / 3.0, true);
            drawer.drawNoOrientation(g, Color.YELLOW, selectedRobot.getTarget(), selectedRobot.getShapeId(), selectedRobot.getScale() * 2.0 / 3.0);
        }
        g.setColor(Color.YELLOW);
        if (selectedTarget != null) {
            drawer.fillObstacle(g, Color.BLACK, Color.RED, selectedTarget.getPosition(), selectedTarget.getShapeId(), selectedTarget.getScale(), false);
            drawer.drawNoOrientation(g, Color.YELLOW, selectedTarget.getPosition(), selectedTarget.getShapeId(), selectedTarget.getScale());
        }
        if (selectedTarget != null) {
            drawer.fillObstacle(g, Color.YELLOW, Color.RED, selectedTarget.getTarget(), selectedTarget.getShapeId(), selectedTarget.getScale() * 2.0 / 3.0, true);
            drawer.drawNoOrientation(g, Color.BLACK, selectedTarget.getTarget(), selectedTarget.getShapeId(), selectedTarget.getScale() * 2.0 / 3.0);
        }
        g.setColor(savedColor);
    }

    public void deselectObject() {
        selectedObject = null;
        selectedRobot = null;
        selectedTarget = null;
    }

    public String getSelectedObjectName() {
        if (selectedObject != null) return selectedObject.name;
        return null;
    }

    public String getSelectedRobotName() {
        if (selectedRobot != null) return selectedRobot.name;
        return null;
    }

    public String getSelectedTargetName() {
        if (selectedTarget != null) return selectedTarget.name;
        return null;
    }

    public boolean selectObject(String name, int selectionType) {
        SimObject object = simulator.getObject(name);
        if (object == null) return false;
        selectedObject = object;
        this.selectionType = selectionType;
        return true;
    }

    public boolean selectRobot(String name, int selectionType) {
        SimObject object = simulator.getObject(name);
        if (object == null) return false;
        selectedRobot = (DynamicObject) object;
        this.selectionType = selectionType;
        return true;
    }
}
