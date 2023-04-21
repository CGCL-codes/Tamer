package sandbox;

import Common.Car;
import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.addon.RCXLightSensor;

public class testcode {

    static int m_outerTachoValue = 750;

    static int m_OuterPower = 100;

    static int m_InnerPower = 58;

    static RCXLightSensor m_leftLS = new RCXLightSensor(SensorPort.S3);

    static RCXLightSensor m_midLS = new RCXLightSensor(SensorPort.S2);

    static RCXLightSensor m_rightLS = new RCXLightSensor(SensorPort.S1);

    static Motor m_leftMotor = new Motor(MotorPort.B);

    static Motor m_rightMotor = new Motor(MotorPort.C);

    static CalibrateMode m_calibrateMode = CalibrateMode.INNER_POWER;

    static Direction m_directionMode = Direction.LEFT;

    public static void main(String[] args) throws InterruptedException {
        m_leftLS.setFloodlight(true);
        m_midLS.setFloodlight(true);
        m_rightLS.setFloodlight(true);
        LCD.drawString("Left:", 0, 4);
        LCD.drawString("Mid:", 0, 5);
        LCD.drawString("Right:", 0, 6);
        LCD.drawString("TACHO:", 0, 0, (m_calibrateMode == CalibrateMode.TACHO ? true : false));
        LCD.drawInt(m_outerTachoValue, 5, 10, 0);
        LCD.drawString("INNER:", 0, 10, (m_calibrateMode == CalibrateMode.INNER_POWER ? true : false));
        LCD.drawInt(m_InnerPower, 5, 10, 1);
        LCD.drawString("MODE:", 0, 20, (m_calibrateMode == CalibrateMode.DIRECTION ? true : false));
        LCD.drawInt(m_directionMode.ordinal(), 5, 10, 2);
        Button.LEFT.addButtonListener(new ButtonListener() {

            @Override
            public void buttonReleased(Button b) {
                if (m_calibrateMode == CalibrateMode.TACHO) m_outerTachoValue += 50; else if (m_calibrateMode == CalibrateMode.INNER_POWER) m_InnerPower++; else if (m_directionMode == Direction.LEFT) m_directionMode = Direction.TURN180; else if (m_directionMode == Direction.TURN180) m_directionMode = Direction.RIGHT; else m_directionMode = Direction.LEFT;
                LCD.drawInt(m_outerTachoValue, 5, 10, 0);
                LCD.drawInt(m_InnerPower, 5, 10, 1);
                LCD.drawInt(m_directionMode.ordinal(), 5, 10, 2);
            }

            @Override
            public void buttonPressed(Button b) {
            }
        });
        Button.RIGHT.addButtonListener(new ButtonListener() {

            @Override
            public void buttonReleased(Button b) {
                if (m_calibrateMode == CalibrateMode.TACHO) m_outerTachoValue -= 50; else if (m_calibrateMode == CalibrateMode.INNER_POWER) m_InnerPower--; else if (m_directionMode == Direction.LEFT) m_directionMode = Direction.RIGHT; else if (m_directionMode == Direction.RIGHT) m_directionMode = Direction.TURN180; else m_directionMode = Direction.LEFT;
                LCD.drawInt(m_outerTachoValue, 5, 10, 0);
                LCD.drawInt(m_InnerPower, 5, 10, 1);
                LCD.drawInt(m_directionMode.ordinal(), 5, 10, 2);
            }

            @Override
            public void buttonPressed(Button b) {
            }
        });
        Button.ENTER.addButtonListener(new ButtonListener() {

            @Override
            public void buttonPressed(Button b) {
                if (m_directionMode == Direction.TURN180) {
                    Turn180(850, 800, 800);
                } else Turn(m_directionMode, 850, 800, 800);
            }

            @Override
            public void buttonReleased(Button b) {
            }
        });
        Button.ESCAPE.addButtonListener(new ButtonListener() {

            @Override
            public void buttonPressed(Button b) {
                if (m_calibrateMode == CalibrateMode.INNER_POWER) {
                    m_calibrateMode = CalibrateMode.TACHO;
                } else if (m_calibrateMode == CalibrateMode.TACHO) {
                    m_calibrateMode = CalibrateMode.DIRECTION;
                } else m_calibrateMode = CalibrateMode.INNER_POWER;
                LCD.drawString("TACHO:", 0, 0, (m_calibrateMode == CalibrateMode.TACHO ? true : false));
                LCD.drawString("INNER:", 0, 10, (m_calibrateMode == CalibrateMode.INNER_POWER ? true : false));
                LCD.drawString("MOVE:", 0, 20, (m_calibrateMode == CalibrateMode.DIRECTION ? true : false));
            }

            @Override
            public void buttonReleased(Button b) {
            }
        });
        while (true) {
            LCD.drawInt(SensorPort.S3.readRawValue(), 5, 10, 4);
            LCD.drawInt(SensorPort.S2.readRawValue(), 5, 10, 5);
            LCD.drawInt(SensorPort.S1.readRawValue(), 5, 10, 6);
        }
    }

    public static void Turn180(int leftSensorLineThreshold, int midSensorLineThreshold, int rightSensorLineThreshold) {
        Sound.beepSequenceUp();
        m_rightMotor.resetTachoCount();
        MotorPort.B.controlMotor(50, 2);
        MotorPort.C.controlMotor(50, 1);
        while (m_rightMotor.getTachoCount() < 20) {
        }
        Sound.buzz();
        while (SensorPort.S3.readRawValue() < leftSensorLineThreshold && SensorPort.S2.readRawValue() < midSensorLineThreshold && SensorPort.S1.readRawValue() < rightSensorLineThreshold) {
        }
        MotorPort.B.controlMotor(m_OuterPower, 3);
        MotorPort.C.controlMotor(m_OuterPower, 3);
        Sound.beepSequence();
    }

    public static void Turn(Direction direction, int leftSensorLineThreshold, int midSensorLineThreshold, int rightSensorLineThreshold) {
        Sound.beepSequenceUp();
        if (direction == Direction.RIGHT) {
            m_leftMotor.resetTachoCount();
            m_rightMotor.resetTachoCount();
            MotorPort.B.controlMotor(m_OuterPower, 1);
            MotorPort.C.controlMotor(m_InnerPower, 1);
            while (m_leftMotor.getTachoCount() < m_outerTachoValue) {
            }
            Sound.buzz();
            while (SensorPort.S3.readRawValue() < leftSensorLineThreshold && SensorPort.S2.readRawValue() < midSensorLineThreshold && SensorPort.S1.readRawValue() < rightSensorLineThreshold) {
            }
            MotorPort.B.controlMotor(m_OuterPower, 3);
            MotorPort.C.controlMotor(m_InnerPower, 3);
        } else if (direction == Direction.LEFT) {
            m_leftMotor.resetTachoCount();
            m_rightMotor.resetTachoCount();
            MotorPort.B.controlMotor(m_InnerPower, 1);
            MotorPort.C.controlMotor(m_OuterPower, 1);
            while (m_rightMotor.getTachoCount() < m_outerTachoValue) {
            }
            Sound.buzz();
            while (SensorPort.S3.readRawValue() < leftSensorLineThreshold && SensorPort.S2.readRawValue() < midSensorLineThreshold && SensorPort.S1.readRawValue() < rightSensorLineThreshold) {
            }
            MotorPort.B.controlMotor(m_InnerPower, 3);
            MotorPort.C.controlMotor(m_OuterPower, 3);
        }
        Sound.beepSequence();
    }

    public enum Direction {

        LEFT, RIGHT, TURN180
    }

    public enum CalibrateMode {

        TACHO, INNER_POWER, DIRECTION
    }
}
