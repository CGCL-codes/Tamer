package com.davydov.suabl.model;

import java.awt.Image;
import java.util.ArrayList;
import com.davydov.suabl.Force;
import com.davydov.suabl.Position;

public class PelletMonster extends Character {

    protected PelletMonster(int health, int mass, Position pos, Image avatar) {
        super(health, mass, pos, avatar);
    }

    public String getName() {
        return "PelletMonster";
    }

    @Override
    void die(Environment env) {
        env.remove(this);
    }

    @Override
    void fire(Environment env) {
        if (this.getVelocity().getDirection() > Math.PI / 2 && this.getVelocity().getDirection() < 3 * Math.PI / 2) {
            Pellet bullet = new Pellet(1, 0, this.getPosition());
            try {
                env.add(bullet);
            } catch (OutsideEnvironmentException e) {
                System.out.print("OutsideEnvironmentException");
            }
            bullet.setNetForce(new Force(Math.PI, 100));
        } else if (this.getVelocity().getDirection() > Math.PI / 2 && this.getVelocity().getDirection() < 3 * Math.PI / 2) {
            Pellet bullet = new Pellet(1, 0, this.getPosition());
            try {
                env.add(bullet);
            } catch (OutsideEnvironmentException e) {
                System.out.print("OutsideEnvironmentException");
            }
            bullet.setNetForce(new Force(0, 100));
        }
    }

    @Override
    public void tick(Environment env) {
        ArrayList<PhysicsObject> creatures = env.getAllObjects();
        HeroCharacter hero = null;
        for (int i = 0; i < creatures.size(); i++) {
            if (env.getAllObjects().get(i) instanceof HeroCharacter) {
                hero = (HeroCharacter) env.getAllObjects().get(i);
            }
        }
        if (hero == null) {
            return;
        }
        if (this.getPosition().getX() < hero.getPosition().getX()) {
            this.getVelocity().setDirection(0);
            this.fire(env);
        } else if (this.getPosition().getX() > hero.getPosition().getX()) {
            this.getVelocity().setDirection(Math.PI);
            this.fire(env);
        }
    }
}
