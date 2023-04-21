package de.rockon.fuzzy.simulation.view.shapes;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.BasicJoint;
import net.phys2d.raw.Body;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.shapes.Circle;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

/**
 * Ein Mensch
 */
public class Ragdoll extends CustomShape {

    /** Gr��e */
    private float size;

    /**
	 * Konstruktor
	 * 
	 * @param gc
	 *            GameContainer
	 * @param x
	 *            x Position
	 * @param y
	 *            y Position
	 * @param size
	 *            Gr��e
	 */
    public Ragdoll(float size, float x, float y, GameContainer gc) {
        super(x, y, gc);
        this.size = size;
        initPhysics();
    }

    @Override
    public void initPhysics() {
        float torsoWidth = 1.0f;
        float torsoHeight = 2.0f;
        float uArmWidth = .3f;
        float uArmHeight = 1.0f;
        float lArmWidth = .25f;
        float lArmHeight = 1.0f;
        float uLegWidth = 0.45f;
        float uLegHeight = 1.2f;
        float lLegWidth = 0.38f;
        float lLegHeight = 1.0f;
        float headRadius = 0.9f;
        float neckLength = 1.0f;
        float density = 5.0f;
        float restitution = 0.5f;
        float friction = 0.5f;
        Body torso = new Body(new Box(torsoWidth * size, torsoHeight * size), density);
        torso.setRestitution(restitution);
        torso.setFriction(friction);
        torso.setPosition(x, y);
        entities.add(torso);
        Body head = new Body(new Circle(size * headRadius), density);
        head.setRestitution(restitution);
        head.setFriction(friction);
        head.setPosition(x + 0f, y + (torsoHeight + neckLength) * size);
        entities.add(head);
        Body ruArm = new Body(new Box(uArmWidth * size, uArmHeight * size), density);
        ruArm.setRestitution(restitution);
        ruArm.setFriction(friction);
        ruArm.setPosition(x + (torsoWidth + uArmWidth) * size, y + size * (torsoHeight - uArmHeight));
        entities.add(ruArm);
        Body luArm = new Body(new Box(uArmWidth * size, uArmHeight * size), density);
        luArm.setRestitution(restitution);
        luArm.setFriction(friction);
        luArm.setPosition(x + -(torsoWidth + uArmWidth) * size, y + size * (torsoHeight - uArmHeight));
        entities.add(luArm);
        Body rlArm = new Body(new Box(lArmWidth * size, lArmHeight * size), density);
        rlArm.setRestitution(restitution);
        rlArm.setFriction(friction);
        rlArm.setPosition(x + (torsoWidth + uArmWidth) * size, y + size * (torsoHeight - 2 * uArmHeight - lArmHeight));
        entities.add(rlArm);
        Body llArm = new Body(new Box(lArmWidth * size, lArmHeight * size), density);
        llArm.setRestitution(restitution);
        llArm.setFriction(friction);
        llArm.setPosition(x + -(torsoWidth + uArmWidth) * size, y + size * (torsoHeight - 2 * uArmHeight - lArmHeight));
        entities.add(llArm);
        Body ruLeg = new Body(new Box(uLegWidth * size, uLegHeight * size), density);
        ruLeg.setRestitution(restitution);
        ruLeg.setFriction(friction);
        ruLeg.setPosition(x + (torsoWidth - uLegWidth) * size, y + size * (-torsoHeight - uLegHeight));
        entities.add(ruLeg);
        Body luLeg = new Body(new Box(uLegWidth * size, uLegHeight * size), density);
        luLeg.setRestitution(restitution);
        luLeg.setFriction(friction);
        luLeg.setPosition(x + -(torsoWidth - uLegWidth) * size, y + size * (-torsoHeight - uLegHeight));
        entities.add(luLeg);
        Body rlLeg = new Body(new Box(lLegWidth * size, lLegHeight * size), density);
        rlLeg.setRestitution(restitution);
        rlLeg.setFriction(friction);
        rlLeg.setPosition(x + (torsoWidth - uLegWidth) * size, y + size * (-torsoHeight - 2 * uLegHeight - lLegHeight));
        entities.add(rlLeg);
        Body llLeg = new Body(new Box(lLegWidth * size, lLegHeight * size), density);
        llLeg.setRestitution(restitution);
        llLeg.setFriction(friction);
        llLeg.setPosition(x + -(torsoWidth - uLegWidth) * size, y + size * (-torsoHeight - 2 * uLegHeight - lLegHeight));
        entities.add(llLeg);
        BasicJoint head_torso = new BasicJoint(head, torso, new Vector2f(head.getPosition().getX() + 0f, head.getPosition().getY() + -neckLength * size));
        joints.add(head_torso);
        BasicJoint torso_arms_r = new BasicJoint(torso, ruArm, new Vector2f(torso.getPosition().getX() + size * (torsoWidth + uArmWidth), torso.getPosition().getY() + size * torsoHeight));
        joints.add(torso_arms_r);
        BasicJoint torso_arms_l = new BasicJoint(torso, luArm, new Vector2f(torso.getPosition().getX() + -size * (torsoWidth + uArmWidth), torso.getPosition().getY() + size * torsoHeight));
        joints.add(torso_arms_l);
        BasicJoint arms_lower_upper_r = new BasicJoint(ruArm, rlArm, new Vector2f(ruArm.getPosition().getX() + 0f, ruArm.getPosition().getY() + -uArmHeight * size));
        joints.add(arms_lower_upper_r);
        ruArm.addExcludedBody(rlArm);
        rlArm.addExcludedBody(ruArm);
        BasicJoint arms_lower_upper_l = new BasicJoint(luArm, llArm, new Vector2f(luArm.getPosition().getX() + 0f, luArm.getPosition().getY() + -uArmHeight * size));
        joints.add(arms_lower_upper_l);
        luArm.addExcludedBody(llArm);
        llArm.addExcludedBody(luArm);
        BasicJoint torso_legs_r = new BasicJoint(torso, ruLeg, new Vector2f(ruLeg.getPosition().getX() + 0f, ruLeg.getPosition().getY() + uLegHeight * size));
        joints.add(torso_legs_r);
        torso.addExcludedBody(ruLeg);
        ruLeg.addExcludedBody(torso);
        BasicJoint torso_legs_l = new BasicJoint(torso, luLeg, new Vector2f(luLeg.getPosition().getX() + 0f, luLeg.getPosition().getY() + uLegHeight * size));
        joints.add(torso_legs_l);
        torso.addExcludedBody(luLeg);
        luLeg.addExcludedBody(torso);
        BasicJoint legs_upper_lower_r = new BasicJoint(ruLeg, rlLeg, new Vector2f(ruLeg.getPosition().getX() + 0f, ruLeg.getPosition().getY() + -uLegHeight * size));
        joints.add(legs_upper_lower_r);
        ruLeg.addExcludedBody(rlLeg);
        rlLeg.addExcludedBody(ruLeg);
        BasicJoint legs_upper_lower_l = new BasicJoint(luLeg, llLeg, new Vector2f(luLeg.getPosition().getX() + 0f, luLeg.getPosition().getY() + -uLegHeight * size));
        joints.add(legs_upper_lower_l);
        luLeg.addExcludedBody(llLeg);
        llLeg.addExcludedBody(luLeg);
    }

    @Override
    public void paint(Graphics g) {
    }

    @Override
    public void update(int delta) {
    }
}
