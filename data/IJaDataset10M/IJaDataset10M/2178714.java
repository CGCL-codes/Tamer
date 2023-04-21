package nl.tue.id.creapro.admoveo;

import java.lang.reflect.Method;
import java.util.Iterator;
import nl.tue.id.creapro.arduino.Arduino;
import processing.core.PApplet;

public class AdMoVeo implements SensorListener, PinConstants {

    Arduino arduino;

    PApplet parent;

    LeftSoundSensor leftSoundSensor;

    RightSoundSensor rightSoundSensor;

    LeftLightSensor leftLightSensor;

    RightLightSensor rightLightSensor;

    LeftLineSensor leftLineSensor;

    RightLineSensor rightLineSensor;

    LeftDistanceSensor leftDistanceSensor;

    RightDistanceSensor rightDistanceSensor;

    FrontDistanceSensor frontDistanceSensor;

    LeftMotor leftMotor;

    RightMotor rightMotor;

    RedLed redLed;

    GreenLed greenLed;

    BlueLed blueLed;

    Buzzer buzzer;

    Class[] paramTypes3 = { Sensor.class, Integer.TYPE, Integer.TYPE };

    public AdMoVeo(PApplet parent, String name) {
        arduino = new Arduino(parent, name);
        this.parent = parent;
        leftSoundSensor = new LeftSoundSensor(arduino);
        rightSoundSensor = new RightSoundSensor(arduino);
        leftLightSensor = new LeftLightSensor(arduino);
        rightLightSensor = new RightLightSensor(arduino);
        leftLineSensor = new LeftLineSensor(arduino);
        rightLineSensor = new RightLineSensor(arduino);
        leftDistanceSensor = new LeftDistanceSensor(arduino);
        rightDistanceSensor = new RightDistanceSensor(arduino);
        frontDistanceSensor = new FrontDistanceSensor(arduino);
        Iterator it = Sensor.getSensors().iterator();
        while (it.hasNext()) {
            ((Sensor) it.next()).addSensorListener(this);
        }
        leftMotor = new LeftMotor(arduino);
        rightMotor = new RightMotor(arduino);
        redLed = new RedLed(arduino);
        greenLed = new GreenLed(arduino);
        blueLed = new BlueLed(arduino);
        buzzer = new Buzzer(arduino);
    }

    public BlueLed getBlueLed() {
        return blueLed;
    }

    public Buzzer getBuzzer() {
        return buzzer;
    }

    public FrontDistanceSensor getFrontDistanceSensor() {
        return frontDistanceSensor;
    }

    public GreenLed getGreenLed() {
        return greenLed;
    }

    public LeftDistanceSensor getLeftDistanceSensor() {
        return leftDistanceSensor;
    }

    public LeftLightSensor getLeftLightSensor() {
        return leftLightSensor;
    }

    public LeftLineSensor getLeftLineSensor() {
        return leftLineSensor;
    }

    public LeftMotor getLeftMotor() {
        return leftMotor;
    }

    public LeftSoundSensor getLeftSoundSensor() {
        return leftSoundSensor;
    }

    public RedLed getRedLed() {
        return redLed;
    }

    public RightDistanceSensor getRightDistanceSensor() {
        return rightDistanceSensor;
    }

    public RightLightSensor getRightLightSensor() {
        return rightLightSensor;
    }

    public RightLineSensor getRightLineSensor() {
        return rightLineSensor;
    }

    public RightMotor getRightMotor() {
        return rightMotor;
    }

    public RightSoundSensor getRightSoundSensor() {
        return rightSoundSensor;
    }

    public void inputAvailable(Sensor sensor, int oldValue, int newValue) {
        Class klass = parent.getClass();
        try {
            String methodName = "inputAvailable";
            Method callback = klass.getMethod(methodName, paramTypes3);
            Object[] parameters = { sensor, new Integer(oldValue), new Integer(newValue) };
            callback.invoke(parent, parameters);
        } catch (Exception e) {
        }
    }
}
