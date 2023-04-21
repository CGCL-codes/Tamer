package edu.caltech.trigjclient;

import javax.swing.*;
import edu.caltech.sbw.*;

/**
 * @author fbergman
 * 
 */
public class TrigClientApplication {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        String angleString = JOptionPane.showInputDialog(null, "Enter angle:");
        double angle = Double.valueOf(angleString).doubleValue();
        double result = trigSin(angle);
        JOptionPane.showMessageDialog(null, "Result: " + Double.toString(result));
        System.exit(0);
    }

    public static double trigSin(double the_angle) {
        try {
            SBW.connect();
            Module module = SBW.getModuleInstance("edu.caltech.trigj");
            Service service = module.findServiceByName("Trig");
            Trigonometry trig = (Trigonometry) service.getServiceObject(Trigonometry.class);
            double result = trig.sin(the_angle);
            module.shutdown();
            SBW.disconnect();
            return result;
        } catch (SBWException e) {
            e.handleWithDialog();
        }
        return 0;
    }
}
