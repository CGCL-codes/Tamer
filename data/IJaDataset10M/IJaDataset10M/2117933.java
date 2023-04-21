package shag.demo;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import shag.App;

/**
 * HelloWorld is perhaps the most rudimentary concrete Shag application
 * that can be written.
 * 
 * @author   dondi
 * @version  $Revision: 1.2 $ $Date: 2005/03/31 02:28:35 $
 */
public class HelloWorld extends App {

    /**
     * Starts the Hello World application.
     */
    public static void main(String[] args) {
        (new HelloWorld()).run();
    }

    /**
     * @see shag.App#getAppName()
     */
    public String getAppName() {
        return ("Hello");
    }

    /**
     * @see shag.App#getStartupFrame()
     */
    protected JFrame getStartupFrame() {
        JFrame result = new JFrame("Hello");
        JPanel hwPanel = new JPanel(new BorderLayout());
        hwPanel.add(new JLabel("Hello world!", JLabel.CENTER), BorderLayout.CENTER);
        result.setContentPane(hwPanel);
        result.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        result.setSize(320, 240);
        return (result);
    }
}
