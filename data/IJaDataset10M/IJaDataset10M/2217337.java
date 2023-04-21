package viewControllers;

import physics.GameObject;
import viewControllers.robot.RobotViewController;

public class ViewControllersFactory {

    public static ViewController createRobotViewController(GameObject object) {
        return new RobotViewController(object);
    }

    public static CarrotViewController createCarrotViewController(GameObject object) {
        return new CarrotViewController(object);
    }

    public static ExplosionViewController createExplosionViewController(GameObject object) {
        return new ExplosionViewController(object);
    }

    public static KnifeViewController createKnifeViewController(GameObject object) {
        return new KnifeViewController(object);
    }
}
