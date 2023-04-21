package champ2010client;

public class OdmerajSmyk extends Controller {

    static double PI = 3.141592654;

    private final int[] gearUp = { 9500, 9400, 9500, 9500, 9500, 0 };

    private final int[] gearDown = { 0, 3300, 6200, 7000, 7300, 7700 };

    int gear = 1;

    int waitForGearSwitch = 25;

    boolean brzdi = false, vypisMetre = false;

    double brakeS = 0;

    private final double breakSpeed = 180;

    @Override
    public Action control(SensorModel sensors) {
        Action action = new Action();
        if (sensors.getSpeed() <= breakSpeed && !brzdi) {
            action.accelerate = 1;
            action.brake = 0;
            brakeS = sensors.getDistanceRaced();
        } else {
            brzdi = true;
            action.accelerate = 0;
            action.brake = 1;
            action.clutch = 1;
            if (sensors.getSpeed() < 0.1 && !vypisMetre) {
                System.out.println(brakeS);
                System.out.println("brydna draha " + (sensors.getDistanceRaced() - brakeS) + "");
                vypisMetre = true;
            }
        }
        action.focus = 0;
        int[] pom;
        int j = 0;
        double smer = priemrDvestoSenzors(sensors.getTrackEdgeSensors());
        action.steering = stupneToSmer(smer);
        if (sensors.getTrackEdgeSensors()[0] < 3.5) action.steering -= 0.03;
        if (sensors.getTrackEdgeSensors()[18] < 3.5) action.steering += 0.03;
        double rpm = sensors.getRPM();
        if (waitForGearSwitch > 0) {
            waitForGearSwitch--;
        }
        if (gear < 1) {
            gear = 1;
            waitForGearSwitch = 25;
        } else if (gear < 6 && rpm >= gearUp[gear - 1]) {
            gear++;
            waitForGearSwitch = 25;
        } else if (gear > 1 && rpm <= gearDown[gear - 1]) {
            gear--;
            waitForGearSwitch = 25;
        }
        action.gear = gear;
        return action;
    }

    private double stupneToSmer(double stup) {
        return -((((PI * stup) / 180) * 100) / 0.785398) / 100;
    }

    private double getMax(double[] sens) {
        double pom = 0.0;
        int max = 0;
        for (int i = 0; i < sens.length; i++) {
            if (pom < sens[i]) {
                pom = sens[i];
                max = i;
            }
        }
        return pom;
    }

    public double priemrDvestoSenzors(double[] sens) {
        double pomLeft = 0.0;
        int pom = 0;
        boolean flag = false;
        double max = getMax(sens);
        for (int i = 5; i < 13; i++) {
            if (sens[i] == max) {
                if (flag) {
                    pomLeft = 0.0;
                    flag = false;
                }
                pomLeft += i;
                pom++;
            } else {
                flag = true;
            }
        }
        double smer = (pomLeft / (pom)) * 10;
        smer -= 90;
        return smer;
    }

    @Override
    public void reset() {
    }

    @Override
    public void shutdown() {
    }
}
