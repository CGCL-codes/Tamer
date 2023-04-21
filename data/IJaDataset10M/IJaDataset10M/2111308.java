package de.imtek.pressurecontrol;

import org.eclipse.swt.widgets.Label;
import net.sf.jgpib.GPIBException;
import net.sf.jgpib.IGPIBBoard;
import net.sf.jgpib.IGPIBDevice;
import net.sf.jgpib.IGPIBBoard.DeviceDescriptor;
import net.sf.jgpib.ni.GPIBBoard;

/**
 * This library is used to control the pressure controller "GE Druck DPI 520".
 * A simple frontend is PressureControlGUI.java
 * Written for Laboratory for Micro-Optics in the Department of Microsystems Engineering 
 * at the University of Freiburg/Germany
 *  
 * @author mada
 */
public class PressureControl {

    protected IGPIBBoard board;

    protected IGPIBDevice pressureController;

    public int i = 0;

    public int j = 0;

    public static int steps = 3;

    public static double pressureStep = 50.0;

    public static double pressure = 0.0;

    public static String queryString;

    /**
	 * Create and intitializes a new PressureControl object.
	 * @param boardNo
	 * @param address
	 * @throws GPIBException 
	 */
    public PressureControl(int boardNo, int address) throws GPIBException {
        board = new GPIBBoard(boardNo);
        long time = System.currentTimeMillis();
        board.clearBoard();
        board.becomeActiveController();
        System.out.println("BecomeActive took " + ((System.currentTimeMillis() - time) / 1000) + " s");
        DeviceDescriptor descriptor = new IGPIBBoard.DeviceDescriptor(board, address);
        pressureController = board.getGPIBDevice(descriptor);
    }

    /**
	 * Initializes the new PressureControl object with proper settings.
	 * @param 
	 * @throws GPIBException 
	 */
    public void init() throws GPIBException {
        pressureController.init(IGPIBDevice.Timeout.T300ms);
        sleep(500);
        pressureController.clear();
        sleep(1000);
        PressureControl.queryString = "R1,W5,S3,U4,D0,P0,C1,J0";
        pressureController.write(PressureControl.queryString);
        sleep(500);
        pressureController.write(PressureControl.queryString);
        PressureControl.queryString = "IDN?";
    }

    /**
	 * Returns LOC or REM operating mode of the pressure controller
	 * @param
	 * @throws GPIBException
	 */
    public String getOperatingMode() {
        String answer = null;
        try {
            pressureController.write(PressureControl.queryString);
            answer = pressureController.readAnswer();
            sleep(1000);
        } catch (GPIBException e) {
            e.printStackTrace();
        }
        return answer.substring(8, 11);
    }

    /**
	 * Set operating mode: REM with i=1 / LOC with i=0.
	 * @param i
	 * @throws GPIBException 
	 */
    public void setOperatingMode(int i) throws GPIBException {
        PressureControl.queryString = "R" + i;
        pressureController.write(PressureControl.queryString);
        sleep(500);
    }

    /**
	 * Set controller mode: on with i=1 / off with i=0.
	 * @param i
	 * @throws GPIBException 
	 */
    public void setControllerMode(int i) throws GPIBException {
        PressureControl.queryString = "C" + i;
        pressureController.write(PressureControl.queryString);
        sleep(500);
    }

    /**
	 * Set control pressure in current units.
	 * @param pressure
	 * @throws GPIBException
	 */
    public void setPressure(double pressure) throws GPIBException {
        PressureControl.queryString = "P" + pressure;
        pressureController.write(PressureControl.queryString);
        sleep(1000);
        PressureControl.pressure = pressure;
        PressureControl.queryString = "IDN?";
    }

    /**
	 * Step to a new pressure with number of steps and certain pressureStep increments. No stable pressure control but wait time between steps.
	 * @param steps
	 * @param pressureStep
	 * @param wait
	 * @throws GPIBException
	 */
    public void stepToPressure(int steps, double pressureStep, int wait) throws GPIBException {
        PressureControl.steps = steps;
        PressureControl.pressureStep = pressureStep;
        double setpoint = PressureControl.pressure;
        for (i = 0; i < PressureControl.steps; i++) {
            setpoint = setpoint + pressureStep;
            PressureControl.queryString = "P" + setpoint;
            pressureController.write(PressureControl.queryString);
            System.out.println("Current step count:" + i + "  " + "Current setpoint:" + setpoint);
            sleep(wait);
        }
        PressureControl.pressure = setpoint;
        PressureControl.queryString = "IDN?";
    }

    /**
	 * Returns full controller feedback.
	 * @param
	 * @throws GPIBException
	 */
    public String getState() {
        String answer = null;
        try {
            pressureController.write(PressureControl.queryString);
            answer = pressureController.readAnswer();
        } catch (GPIBException e) {
            e.printStackTrace();
        }
        return answer;
    }

    /**
	 * Return measured pressure in current units.
	 * @param
	 * @throws GPIBException
	 */
    public String getPressure() {
        String answer = null;
        try {
            pressureController.write(PressureControl.queryString);
            answer = pressureController.readAnswer();
            sleep(1000);
        } catch (GPIBException e) {
            e.printStackTrace();
        }
        return answer.substring(0, 8);
    }

    /**
	 * Resets the pressure controller device (controller off, local mode, zero pressure).
	 * @param
	 * @throws GPIBException
	 */
    public void reset() throws GPIBException {
        pressureController.write("P0");
        sleep(1000);
        pressureController.write("C0,R0");
        sleep(1000);
        pressureController.clear();
    }

    /**
	 * Wait time in milliseconds.
	 * @param milliseconds
	 */
    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws GPIBException {
        PressureControl pc1 = new PressureControl(0, 16);
        try {
            pc1.init();
            sleep(3000);
            Label displayPressureLabel = null;
            ShowPressureThread showPressure = new ShowPressureThread(displayPressureLabel, pc1);
            showPressure.start();
            for (int i = 0; i < 5; i++) {
                sleep(500);
                System.out.println("" + pc1.getPressure());
                System.out.println("" + pc1.getState());
            }
            pc1.setPressure(5);
            for (int i = 0; i < 10; i++) {
                sleep(500);
                System.out.println("" + pc1.getPressure());
                System.out.println("" + pc1.getState());
            }
            pc1.setPressure(100);
            for (int i = 0; i < 5; i++) {
                sleep(500);
                System.out.println("" + pc1.getPressure());
                System.out.println("" + pc1.getState());
            }
            pc1.stepToPressure(2, 50, 1000);
            for (int i = 0; i < 5; i++) {
                sleep(500);
                System.out.println("" + pc1.getPressure());
                System.out.println("" + pc1.getState());
            }
            showPressure.pleaseStop();
            pc1.reset();
        } catch (GPIBException e) {
            e.printStackTrace();
        }
        System.out.println("End of Program");
    }
}
