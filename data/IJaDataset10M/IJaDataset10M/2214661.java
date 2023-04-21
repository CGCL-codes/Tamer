package jxmlswing;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.TransformerException;

/**
 * @author Giulio Rossetti <Giulio.Rossetti@gmail.com>
 * @version 0.2alpha
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        XMLtoJava X2J = new XMLtoJava();
        try {
            X2J.generateCode("src/Template/HelloWorldFrame.xml");
        } catch (TransformerException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
