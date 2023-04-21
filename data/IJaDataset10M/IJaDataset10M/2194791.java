package net.sourceforge.bprocessor.packages.physics;

import net.sourceforge.bprocessor.model.Vertex;

public class PhysicsSpring {

    boolean displayBendingStiffness = false;

    PhysicsParticle pA;

    PhysicsParticle pB;

    double startL;

    double eql;

    double l;

    double lLast;

    double c;

    double sf;

    double sf_damped;

    double restLengthFactor;

    double restLengthScale;

    Vertex fA;

    Vertex fB;

    PhysicsSpring(PhysicsParticle pA, PhysicsParticle pB, double eql, double c) {
        this.pA = pA;
        this.pB = pB;
        this.eql = eql;
        this.c = c;
        this.l = getLength();
        this.startL = this.l;
        lLast = l;
        sf = 0;
        sf_damped = 0;
        fA = new Vertex(0, 0, 0);
        fB = new Vertex(0, 0, 0);
        pA.connectSpring(this);
        pB.connectSpring(this);
        sf = 0;
        sf_damped = 0;
        restLengthScale = 1.0;
        restLengthFactor = 1.0;
    }

    double getLength() {
        double sl;
        sl = pA.pos.distance(pB.pos);
        return sl;
    }

    double calcDamping(double time, double spring_damping) {
        double damping;
        damping = (l - lLast) * spring_damping / time;
        lLast = l;
        return damping;
    }

    void calcForce(double time, double spring_damping) {
        sf = (l - (eql * restLengthFactor * restLengthScale)) * c;
        sf_damped = sf + calcDamping(time, spring_damping);
        fA = pB.pos.minus(pA.pos);
        fB = pA.pos.minus(pB.pos);
        fA.normalize();
        fB.normalize();
        fA.scaleIt(sf_damped);
        fB.scaleIt(sf_damped);
    }

    void update(double time, double spring_damping) {
        l = getLength();
        calcForce(time, spring_damping);
    }
}
