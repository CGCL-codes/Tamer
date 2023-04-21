package viewControllers.robot;

import globals.GlobalObjects;
import javax.media.opengl.GL;
import physics.GameObject;
import viewControllers.ViewController;

public class RobotViewController extends ViewController {

    public RobotViewController(GameObject object) {
        super(object);
        jp.getCenter().setValues(0, 0.15, 0);
        jp.setScale(0.015);
        Torso body = new Torso(GlobalObjects.getGraphic("robot_torso"));
        body.rotate(0, 0, 0);
        body.getCenter().setValues(0, 0.5, 0);
        body.setScale(1.5);
        this.addPart(body);
        Leg legR = new Leg(GlobalObjects.getGraphic("robot_leg"));
        legR.getCenter().setValues(-1.5, -2, 0);
        legR.direction = -1;
        this.addPart(legR);
        Leg legL = new Leg(GlobalObjects.getGraphic("robot_leg"));
        legL.getCenter().setValues(1.5, -2, 0);
        this.addPart(legL);
        Leg handR = new Leg(GlobalObjects.getGraphic("robot_hand1"));
        handR.getCenter().setValues(3.25, 2, 0);
        handR.direction = -1;
        this.addPart(handR);
        Leg handL = new Leg(GlobalObjects.getGraphic("robot_hand2"));
        handL.getCenter().setValues(-3.25, 2, 0);
        this.addPart(handL);
        object.setGraphicalObject(this);
        this.setPosition(object.getPosition());
        RobotWalkAnimator rwa = new RobotWalkAnimator(parts);
        setAnimator(rwa);
    }

    @Override
    public void render(GL gl) {
        super.render(gl);
    }
}
