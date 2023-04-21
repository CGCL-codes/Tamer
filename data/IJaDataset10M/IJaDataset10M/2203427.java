package fr.ana.anaballistics.core;

public class Velocity extends Trajectory {

    public Velocity(Trajectory trajectory) {
        super();
        for (int i = 1; i < trajectory.getPoints().size(); ++i) {
            SpeedPoint sp = new SpeedPoint(trajectory.getPoints().get(i - 1), trajectory.getPoints().get(i));
            this.add(sp.toPoint());
        }
    }
}
