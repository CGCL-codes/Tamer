package exec.visual;

import java.awt.Color;
import java.awt.Graphics;
import planning.collision.CollidableCircular;
import planning.collision.CollisionTreeNode;
import planning.model.IWorld;
import planning.model.Obstacle;
import planning.plan.Executer;
import planning.plan.IController;
import simulation.visual.IScreenLayer;
import simulation.visual.SimObjectDrawer;

public class CollisionTreeLayer implements IScreenLayer {

    public IExecutionContent executionContent;

    public CollisionTreeLayer(IExecutionContent executionContent) {
        this.executionContent = executionContent;
    }

    public void paint(Graphics g) {
        Executer executer = executionContent.getExecuter();
        if (executer == null) return;
        if (!executer.isDrawCollisionTrees()) return;
        Color savedColor = g.getColor();
        SimObjectDrawer drawer = executionContent.getSimulationScreen().drawer;
        g.setColor(Color.YELLOW.darker());
        for (IController<?> controller : executionContent.getExecuter().controllers) {
            IWorld world = controller.getWorld();
            for (Obstacle obstacle : world.getObstacles()) {
                CollisionTreeNode head = obstacle.getEntity().getCollisionTreeHead();
                drawCollisionTree(drawer, g, obstacle.getConfig(), head, obstacle.getScaling());
            }
        }
        g.setColor(savedColor);
    }

    private void drawCollisionTree(SimObjectDrawer drawer, Graphics g, double[] config, CollisionTreeNode head, double scaling) {
        CollidableCircular circular = head.getValue();
        double X = config[0] + ((circular.getX() * scaling * Math.cos(config[2])) - (circular.getY() * scaling * Math.sin(config[2])));
        double Y = config[1] + ((circular.getX() * scaling * Math.sin(config[2])) + (circular.getY() * scaling * Math.cos(config[2])));
        drawer.drawCircle(g, new double[] { X, Y }, head.getValue().getRadius() * scaling);
        for (CollisionTreeNode node : head.getChildren()) {
            drawCollisionTree(drawer, g, config, node, scaling);
        }
    }
}
