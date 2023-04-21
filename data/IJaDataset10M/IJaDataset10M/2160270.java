package org.processing.examples.smoke;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class ParticleSystem {

    List<Particle> particles;

    PVector origin;

    PImage img;

    PApplet pApplet;

    private static int NUM_AREAS = 5;

    ParticleSystem(int num, PVector v, PImage img_, Random random, PApplet pApplet) {
        particles = new ArrayList<Particle>();
        origin = v.get();
        img = img_;
        this.pApplet = pApplet;
        for (int i = 0; i < num; i++) {
            particles.add(new Particle(origin, img, random, pApplet));
        }
    }

    public void setOrigin(PVector origin) {
        this.origin = origin;
    }

    private String getAreaPosition(Particle particle) {
        int areaLength = pApplet.width / NUM_AREAS;
        int numAreaX = (int) particle.loc.x / areaLength;
        int areaHeight = pApplet.height / NUM_AREAS;
        int numAreaY = (int) particle.loc.y / areaHeight;
        return "" + numAreaX + "" + numAreaY;
    }

    void run() {
        Map<String, Integer> zonas = new HashMap<String, Integer>();
        for (int i = particles.size() - 1; i >= 0; i--) {
            Particle p = particles.get(i);
            p.run(pApplet);
            if (p.dead()) {
                particles.remove(i);
            }
        }
    }

    void add_force(PVector dir) {
        for (int i = particles.size() - 1; i >= 0; i--) {
            Particle p = (Particle) particles.get(i);
            p.add_force(dir);
        }
    }

    void addParticle(Random random) {
        particles.add(new Particle(origin, img, random, pApplet));
    }

    void addParticle(Particle p) {
        particles.add(p);
    }

    boolean dead() {
        if (particles.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}
