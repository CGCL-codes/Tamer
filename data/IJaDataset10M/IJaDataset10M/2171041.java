package itb.ai;

public class ShipArrive {

    Kinematic EnemyShip = new Kinematic();

    Kinematic target = new Kinematic();

    SteerOutput steering;

    double maxspeed = 5;

    double radius = 1;

    double timeToTarget = 0.25;

    ShipArrive(Kinematic ship, Kinematic targ) {
        EnemyShip = ship;
        target = targ;
    }

    public SteerOutput getSteering() {
        steering = new SteerOutput();
        steering.velocity.x = target.position.x - EnemyShip.position.x;
        steering.velocity.y = target.position.y - EnemyShip.position.y;
        if (steering.velocity.length() < radius) {
            return null;
        }
        steering.velocity.x /= timeToTarget;
        steering.velocity.y /= timeToTarget;
        if (steering.velocity.length() > maxspeed) {
            steering.velocity.normalise();
            steering.velocity.x *= maxspeed;
            steering.velocity.y *= maxspeed;
        }
        steering.rotation = 0;
        return steering;
    }
}
