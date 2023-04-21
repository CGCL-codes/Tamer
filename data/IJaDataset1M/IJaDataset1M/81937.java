package tests;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import wiiusej.Point2DInteger;
import wiiusej.WiiMoteEvent;
import wiiusej.WiiUseApiListener;
import wiiusej.WiiUseApiManager;
import wiiusej.Wiimote;
import wiiusej.WiimoteListener;

/**
 * This class used to test this API.
 * 
 * @author gduche
 * 
 */
public class Tests implements WiimoteListener {

    Robot robot;

    private static int DISPLAY_EACH_VALUE = 1;

    private static int DUMP = 2;

    private static int MOVE_MOUSE = 3;

    private static int ORIENT_THRESH_CONT = 4;

    private static int TEST_LEDS = 5;

    private Wiimote wiimote;

    int dump = DISPLAY_EACH_VALUE;

    public Tests(Wiimote wim) {
        wiimote = wim;
        wiimote.addWiiMoteEventListeners(this);
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void wiimoteEvent(WiiMoteEvent e) {
        if (dump == DISPLAY_EACH_VALUE) {
            if (e.isConnected()) {
                if (e.isButtonOneJustPressed()) {
                    System.out.println("button one pressed");
                }
                if (e.isButtonOneHeld()) {
                    System.out.println("button one held");
                }
                if (e.isButtonOneJustReleased()) {
                    System.out.println("button one released");
                }
                if (e.isButtonTwoJustPressed()) {
                    System.out.println("button two pressed");
                }
                if (e.isButtonTwoHeld()) {
                    System.out.println("button two held");
                }
                if (e.isButtonTwoJustReleased()) {
                    System.out.println("button two released");
                }
                if (e.isButtonAJustPressed()) {
                    System.out.println("button A pressed");
                }
                if (e.isButtonAHeld()) {
                    System.out.println("button A held");
                }
                if (e.isButtonAJustReleased()) {
                    System.out.println("button A released");
                }
                if (e.isButtonBJustPressed()) {
                    System.out.println("button B pressed");
                }
                if (e.isButtonBHeld()) {
                    System.out.println("button B held");
                }
                if (e.isButtonBJustReleased()) {
                    System.out.println("button B released");
                }
                if (e.isButtonLeftJustPressed()) {
                    System.out.println("button Left pressed");
                }
                if (e.isButtonLeftHeld()) {
                    System.out.println("button Left held");
                }
                if (e.isButtonLeftJustReleased()) {
                    System.out.println("button Left released");
                }
                if (e.isButtonRightJustPressed()) {
                    System.out.println("button Right pressed");
                }
                if (e.isButtonRightHeld()) {
                    System.out.println("button Right held");
                }
                if (e.isButtonRightJustReleased()) {
                    System.out.println("button Right released");
                }
                if (e.isButtonUpJustPressed()) {
                    System.out.println("button UP pressed");
                }
                if (e.isButtonUpHeld()) {
                    System.out.println("button UP held");
                }
                if (e.isButtonUpJustReleased()) {
                    System.out.println("button UP released");
                }
                if (e.isButtonDownJustPressed()) {
                    System.out.println("button DOWN pressed");
                }
                if (e.isButtonDownHeld()) {
                    System.out.println("button DOWN held");
                }
                if (e.isButtonDownJustReleased()) {
                    System.out.println("button DOWN released");
                }
                if (e.isButtonMinusJustPressed()) {
                    System.out.println("button MINUS pressed");
                }
                if (e.isButtonMinusHeld()) {
                    System.out.println("button MINUS held");
                }
                if (e.isButtonMinusJustReleased()) {
                    System.out.println("button MINUS released");
                }
                if (e.isButtonPlusJustPressed()) {
                    System.out.println("button PLUS pressed");
                }
                if (e.isButtonPlusHeld()) {
                    System.out.println("button PLUS held");
                }
                if (e.isButtonPlusJustReleased()) {
                    System.out.println("button PLUS released");
                }
                if (e.isButtonHomeJustPressed()) {
                    System.out.println("button HOME pressed");
                }
                if (e.isButtonHomeHeld()) {
                    System.out.println("button HOME held");
                }
                if (e.isButtonHomeJustReleased()) {
                    System.out.println("button HOME released");
                }
                if (e.isButtonMinusJustPressed() && e.isButtonPlusJustPressed()) {
                    wiimote.getStatus();
                }
                if (e.isButtonOneJustPressed()) {
                    System.out.println("Rumble Activated");
                    wiimote.activateRumble();
                }
                if (e.isButtonTwoJustPressed()) {
                    System.out.println("Rumble Deactivated");
                    wiimote.deactivateRumble();
                }
                if (e.isButtonAJustPressed()) {
                    System.out.println("IR Activated");
                    wiimote.activateIRTRacking();
                }
                if (e.isButtonBJustPressed()) {
                    System.out.println("IR Deactivated");
                    wiimote.deactivateIRTRacking();
                }
                if (e.isButtonPlusJustPressed()) {
                    System.out.println("Motion sensing Activated");
                    wiimote.activateMotionSensing();
                }
                if (e.isButtonMinusJustPressed()) {
                    System.out.println("Motion sensing Deactivated");
                    wiimote.deactivateMotionSensing();
                }
                if (e.getBatteryLevel() != -1) {
                    System.out.println("battery level : " + e.getBatteryLevel());
                    System.out.println("= --- Leds : " + e.getLeds() + "\n");
                    System.out.println("= --- Rumble : " + e.isRumbleActive() + "\n");
                    System.out.println("= --- Continous : " + e.isContinuousActive() + "\n");
                    System.out.println("= --- Smoothing : " + e.isSmoothingActive() + "\n");
                    System.out.println("= --- Speaker : " + e.isSpeakerEnabled() + "\n");
                    System.out.println("= --- Attachment : " + e.isThereAttachment() + "\n");
                }
                if (e.isIrActive()) {
                    Point2DInteger[] list = e.getIRPoints();
                    for (int i = 0; i < list.length; i++) {
                        if (list[i] != null) System.out.print("Point :(" + list[i].getX() + "," + list[i].getY() + ") ");
                    }
                    System.out.println("");
                }
                if (e.isMotionSensingActive()) {
                    System.out.println("Motion Sensing :" + e.getOrientation() + " , " + e.getGforce());
                }
                if (e.isButtonHomeJustPressed()) {
                    System.out.println("LEAVING TEST");
                    wiimote.disconnect();
                }
            } else {
                System.out.println(" WIIMOTE ID   : " + e.getWiimoteId() + "  DISCONNECTED !!!!!");
                wiimote.disconnect();
            }
        } else if (dump == DUMP) {
            System.out.println(e);
            if (e.isButtonAJustPressed()) {
                System.out.println("IR Activated");
                wiimote.activateIRTRacking();
                wiimote.activateMotionSensing();
                wiimote.activateRumble();
            }
            if (e.isButtonBJustPressed()) {
                System.out.println("IR Deactivated");
                wiimote.deactivateIRTRacking();
                wiimote.deactivateMotionSensing();
                wiimote.deactivateRumble();
            }
            if (e.isButtonHomeJustPressed()) {
                System.out.println("LEAVING TEST");
                wiimote.disconnect();
            }
        } else if (dump == MOVE_MOUSE) {
            if (e.isButtonOneJustPressed()) {
                System.out.println("IR Activated");
                wiimote.activateIRTRacking();
            }
            if (e.isButtonTwoJustPressed()) {
                System.out.println("IR Deactivated");
                wiimote.deactivateIRTRacking();
            }
            if (e.isButtonAJustPressed()) {
                robot.mousePress(InputEvent.BUTTON1_MASK);
            }
            if (e.isButtonAJustReleased()) {
                robot.mouseRelease(InputEvent.BUTTON1_MASK);
            }
            if (e.isButtonBJustPressed()) {
                robot.mousePress(InputEvent.BUTTON2_MASK);
            }
            if (e.isButtonBJustReleased()) {
                robot.mouseRelease(InputEvent.BUTTON2_MASK);
            }
            Point2DInteger[] list = e.getIRPoints();
            if (e.isIrActive() && list[0] != null) {
                int x1 = (int) list[0].getX();
                int y1 = (int) list[0].getY();
                int mousex = (int) Math.round(((double) x1 / 1024.0) * 1280.0);
                int mousey = (int) Math.round(((double) y1 / 768.0) * 1024.0);
                robot.mouseMove(mousex, mousey);
            }
            if (e.isButtonHomeJustPressed()) {
                System.out.println("LEAVING TEST");
                wiimote.disconnect();
            }
        } else if (dump == ORIENT_THRESH_CONT) {
            wiimote.activateMotionSensing();
            if (e.isButtonOneJustPressed()) {
                System.out.println("Continous activated");
                wiimote.activateContinuous();
            }
            if (e.isButtonTwoJustPressed()) {
                System.out.println("Continous deactivated");
                wiimote.deactivateContinuous();
            }
            if (e.isButtonAJustPressed()) {
                System.out.println("Smoothing activated");
                wiimote.activateSmoothing();
            }
            if (e.isButtonBJustPressed()) {
                System.out.println("Smoothing deactivated");
                wiimote.deactivateSmoothing();
            }
            if (e.isButtonPlusJustPressed()) {
                System.out.println("Threshold orientation 10 degrees");
                wiimote.setOrientationThreshold(10);
            }
            if (e.isButtonMinusJustPressed()) {
                System.out.println("Threshold orientation 0.5 degrees");
                wiimote.setOrientationThreshold((float) 0.5);
            }
            System.out.println(e);
            if (e.isButtonHomeJustPressed()) {
                System.out.println("LEAVING TEST");
                wiimote.disconnect();
            }
        } else if (dump == TEST_LEDS) {
            wiimote.activateMotionSensing();
            if (e.isButtonUpJustPressed()) {
                wiimote.setLeds(true, false, false, false);
            }
            if (e.isButtonDownJustPressed()) {
                wiimote.setLeds(false, true, false, false);
            }
            if (e.isButtonLeftJustPressed()) {
                wiimote.setLeds(false, false, true, false);
            }
            if (e.isButtonRightJustPressed()) {
                wiimote.setLeds(false, false, false, true);
            }
            if (e.isButtonHomeJustPressed()) {
                System.out.println("LEAVING TEST");
                wiimote.disconnect();
            }
        }
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Wiimote[] wiimotes = WiiUseApiManager.getWiimotes();
        if (wiimotes.length > 0) {
            System.out.println(wiimotes[0]);
            Tests tests = new Tests(wiimotes[0]);
        } else {
            System.out.println("No wiimotes found !!!");
        }
    }
}
