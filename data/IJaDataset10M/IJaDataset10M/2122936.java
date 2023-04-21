package oe53_exception;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SCJP
 */
public class D01_Exception {

    public static void main(String[] args) {
        Student s = new Student();
        try {
            s.getAge();
        } catch (Exception ex) {
            System.out.println("Exception");
        }
        try {
            s.getName();
        } catch (IOException ex) {
            Logger.getLogger(D01_Exception.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(D01_Exception.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
