package net.sourceforge.javagg.gsc.driver;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import net.sourceforge.javagg.gsc.GraphicsWriter;
import net.sourceforge.javagg.gsc.components.ReportScreenComponent;
import net.sourceforge.javaref.log.Log;

/**
 * A Mannual Driver Test for the ReportScreen features.
 * 
 * @author Larry Gray
 * @version 1.2
 *
 */
public class ReportScreenDriver extends JFrame {

    /** ReportScreen */
    ReportScreenComponent report = new ReportScreenComponent(500, 500, 1);

    /** Setup work */
    public ReportScreenDriver() {
        this.addWindowListener(new WindowAdapter() {

            public void windowClosed(WindowEvent we) {
                Log.close();
                System.exit(0);
            }
        });
        testReportScreen();
        this.getContentPane().add(report);
        this.setSize(500, 500);
        this.setVisible(true);
    }

    /** Displays some information using various print methods, and tab stops, changes the
     * font and color a few times */
    public void testReportScreen() {
        report.clearScreen();
        report.setScreenFont(GraphicsWriter.COURIER, Font.PLAIN, 12);
        report.setCursorPosition(40, 40);
        report.setMargin();
        report.println("Hello World!");
        report.setColor(Color.blue);
        report.println("Hello World!");
        report.fontUp();
        report.println("Hellow World!");
        report.fontDown();
        report.print("Whats up!");
        report.fontType(Font.BOLD);
        report.print("Howdy!");
        report.fontType(Font.ITALIC);
        report.print("Lets go!");
        report.setTabStop(40);
        report.setTabStop(120);
        report.setTabStop(200);
        report.setTabStop(280);
        report.setTabStop(360);
        report.setTabStop(440);
        report.setColor(Color.red);
        report.println();
        report.println();
        report.fontType(Font.BOLD);
        report.print("Character");
        report.tab();
        report.print("Vigor");
        report.tab();
        report.print("Agility");
        report.tab();
        report.print("Stamina");
        report.tab();
        report.print("Intelect");
        report.tab();
        report.print("Luck");
        report.println();
        report.fontType(Font.PLAIN);
        report.print("Fighter");
        report.tab();
        report.print("2");
        report.tab();
        report.print("3");
        report.tab();
        report.print("1");
        report.tab();
        report.print("2");
        report.tab();
        report.print("4");
        report.println();
        report.print("Wizard");
        report.tab();
        report.print("5");
        report.tab();
        report.print("6");
        report.tab();
        report.print("3");
        report.tab();
        report.print("2");
        report.tab();
        report.print("1");
        report.println();
        report.print("Image=");
        report.setGraphicsColor(Color.gray);
        report.drawString(150, 400, "Hello Again!");
        report.setGraphicsColor(Color.CYAN);
        report.drawCircle(40, 100, 30);
        report.setGraphicsColor(Color.darkGray);
        report.drawCircleDiameter(40, 100, 100);
        report.setGraphicsColor(Color.green);
        report.drawOval(4, 100, 30, 50);
        report.setGraphicsColor(Color.magenta);
        report.drawLine(0, 0, 80, 180);
        report.setGraphicsColor(Color.orange);
        report.fillRect(1, 10, 20, 30);
        report.repaintScreen();
    }

    public static void main(String[] args) {
        new ReportScreenDriver();
    }
}
