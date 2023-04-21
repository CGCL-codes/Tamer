package ModelTrate;

import java.util.ArrayList;
import java.util.Calendar;
import champ2010client.*;

public class SimpleDriver extends Controller {

    public ArrayList<Double> leftsideX = new ArrayList<Double>();

    public ArrayList<Double> leftsideY = new ArrayList<Double>();

    public Double curX = (double) 0;

    public Double curY = (double) 0;

    public Double curdX = (double) 0;

    public Double curdY = (double) 1;

    long timeBef = 0;

    long timeNow = 0;

    long timer = 0;

    double cursteer = 0;

    final int[] gearUp = { 5000, 6000, 6000, 6500, 7000, 0 };

    final int[] gearDown = { 0, 2500, 3000, 3000, 3500, 3500 };

    final int stuckTime = 25;

    final float stuckAngle = (float) 0.523598775;

    final float maxSpeedDist = 70;

    final float maxSpeed = 150;

    final float sin5 = (float) 0.08716;

    final float cos5 = (float) 0.99619;

    final float steerLock = (float) 0.785398;

    final float steerSensitivityOffset = (float) 80.0;

    final float wheelSensitivityCoeff = 1;

    final float wheelRadius[] = { (float) 0.3179, (float) 0.3179, (float) 0.3276, (float) 0.3276 };

    final float absSlip = (float) 2.0;

    final float absRange = (float) 3.0;

    final float absMinSpeed = (float) 3.0;

    final float clutchMax = (float) 0.5;

    final float clutchDelta = (float) 0.05;

    final float clutchRange = (float) 0.82;

    final float clutchDeltaTime = (float) 0.02;

    final float clutchDeltaRaced = 10;

    final float clutchDec = (float) 0.01;

    final float clutchMaxModifier = (float) 1.3;

    final float clutchMaxTime = (float) 1.5;

    private int stuck = 0;

    private float clutch = 0;

    public void reset() {
        System.out.println("Restarting the race!");
    }

    public void shutdown() {
        System.out.println("Bye bye!");
    }

    private int getGear(SensorModel sensors) {
        int gear = sensors.getGear();
        double rpm = sensors.getRPM();
        if (gear < 1) return 1;
        if (gear < 6 && rpm >= gearUp[gear - 1]) return gear + 1; else if (gear > 1 && rpm <= gearDown[gear - 1]) return gear - 1; else return gear;
    }

    private float getSteer(SensorModel sensors) {
        float targetAngle = (float) (sensors.getAngleToTrackAxis() - sensors.getTrackPosition() * 0.5);
        if (sensors.getSpeed() > steerSensitivityOffset) return (float) (targetAngle / (steerLock * (sensors.getSpeed() - steerSensitivityOffset) * wheelSensitivityCoeff)); else return (targetAngle) / steerLock;
    }

    private float getAccel(SensorModel sensors) {
        if (sensors.getTrackPosition() < 1 && sensors.getTrackPosition() > -1) {
            float rxSensor = (float) sensors.getTrackEdgeSensors()[10];
            float sensorsensor = (float) sensors.getTrackEdgeSensors()[9];
            float sxSensor = (float) sensors.getTrackEdgeSensors()[8];
            float targetSpeed;
            if (sensorsensor > maxSpeedDist || (sensorsensor >= rxSensor && sensorsensor >= sxSensor)) targetSpeed = maxSpeed; else {
                if (rxSensor > sxSensor) {
                    float h = sensorsensor * sin5;
                    float b = rxSensor - sensorsensor * cos5;
                    float sinAngle = b * b / (h * h + b * b);
                    targetSpeed = maxSpeed * (sensorsensor * sinAngle / maxSpeedDist);
                } else {
                    float h = sensorsensor * sin5;
                    float b = sxSensor - sensorsensor * cos5;
                    float sinAngle = b * b / (h * h + b * b);
                    targetSpeed = maxSpeed * (sensorsensor * sinAngle / maxSpeedDist);
                }
            }
            return (float) (2 / (1 + Math.exp(sensors.getSpeed() - targetSpeed)) - 1);
        } else return (float) 0.3;
    }

    public Action control(SensorModel sensors) {
        if (Math.abs(sensors.getAngleToTrackAxis()) > stuckAngle) {
            stuck++;
        } else {
            stuck = 0;
        }
        if (stuck > stuckTime) {
            float steer = (float) (-sensors.getAngleToTrackAxis() / steerLock);
            int gear = -1;
            if (sensors.getAngleToTrackAxis() * sensors.getTrackPosition() > 0) {
                gear = 1;
                steer = -steer;
            }
            clutch = clutching(sensors, clutch);
            Action action = new Action();
            action.gear = gear;
            action.steering = steer;
            action.accelerate = 1.0;
            action.brake = 0;
            action.clutch = clutch;
            return action;
        } else {
            float accel_and_brake = getAccel(sensors);
            int gear = getGear(sensors);
            float steer = getSteer(sensors);
            if (steer < -1) steer = -1;
            if (steer > 1) steer = 1;
            float accel, brake;
            if (accel_and_brake > 0) {
                accel = accel_and_brake;
                brake = 0;
            } else {
                accel = 0;
                brake = filterABS(sensors, -accel_and_brake);
            }
            clutch = clutching(sensors, clutch);
            Action action = new Action();
            action.gear = gear;
            action.steering = steer;
            action.accelerate = accel;
            action.brake = brake;
            action.clutch = clutch;
            if (sensors.getSpeed() < 3) {
                timeBef = Calendar.getInstance().getTimeInMillis();
            } else {
                {
                    timeNow = Calendar.getInstance().getTimeInMillis();
                    curX = curX + (((timeNow - timeBef) * sensors.getSpeed() / 36) * curdX);
                    curY = curY + (((timeNow - timeBef) * sensors.getSpeed() / 36) * curdY);
                    timeBef = Calendar.getInstance().getTimeInMillis();
                    System.out.print("plot(" + curX + "," + curY + "); ");
                    timer++;
                    if (timer == 25) {
                        timer = 0;
                        System.out.println();
                    }
                    if ((action.steering - cursteer) < 0) curdY = curdY - Math.cos((action.steering - cursteer) * 0.785398); else curdY = curdY + Math.cos((action.steering - cursteer) * 0.785398);
                    curdX = curdX + Math.sin((action.steering - cursteer) * 0.785398);
                    if (curdX < -1) curdX = 2 + curdX;
                    if (curdY < -1) curdY = 2 + curdY;
                    if (curdX > 1) curdX = -2 + curdX;
                    if (curdY > 1) curdY = -2 + curdY;
                    cursteer = action.steering;
                }
            }
            return action;
        }
    }

    private float filterABS(SensorModel sensors, float brake) {
        float speed = (float) (sensors.getSpeed() / 3.6);
        if (speed < absMinSpeed) return brake;
        float slip = 0.0f;
        for (int i = 0; i < 4; i++) {
            slip += sensors.getWheelSpinVelocity()[i] * wheelRadius[i];
        }
        slip = speed - slip / 4.0f;
        if (slip > absSlip) {
            brake = brake - (slip - absSlip) / absRange;
        }
        if (brake < 0) return 0; else return brake;
    }

    float clutching(SensorModel sensors, float clutch) {
        float maxClutch = clutchMax;
        if (sensors.getCurrentLapTime() < clutchDeltaTime && getStage() == Stage.RACE && sensors.getDistanceRaced() < clutchDeltaRaced) clutch = maxClutch;
        if (clutch > 0) {
            double delta = clutchDelta;
            if (sensors.getGear() < 2) {
                delta /= 2;
                maxClutch *= clutchMaxModifier;
                if (sensors.getCurrentLapTime() < clutchMaxTime) clutch = maxClutch;
            }
            clutch = Math.min(maxClutch, clutch);
            if (clutch != maxClutch) {
                clutch -= delta;
                clutch = Math.max((float) 0.0, clutch);
            } else clutch -= clutchDec;
        }
        return clutch;
    }

    public float[] initAngles() {
        float[] angles = new float[19];
        for (int i = 0; i < 5; i++) {
            angles[i] = -90 + i * 15;
            angles[18 - i] = 90 - i * 15;
        }
        for (int i = 5; i < 9; i++) {
            angles[i] = -20 + (i - 5) * 5;
            angles[18 - i] = 20 - (i - 5) * 5;
        }
        angles[9] = 0;
        return angles;
    }
}
