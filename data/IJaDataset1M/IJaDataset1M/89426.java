package ca.uhn.hl7v2.conf.classes.generator.builders;

import java.io.*;
import ca.uhn.hl7v2.conf.classes.exceptions.*;

/** This Class is used to Generate a Class
 * @author <table><tr>James Agnew</tr>
 *                <tr>Paul Brohman</tr>
 *                <tr>Mitch Delachevrotiere</tr>
 *                <tr>Shawn Dyck</tr>
 * 				  <tr>Cory Metcalf</tr></table>
 */
public class ConfGen {

    /** this is the main method to start the Conformance Generator 
    * @param args the command line argument
    */
    public static void main(String[] args) {
        ConfGen gc = new ConfGen();
        CommandParser cp = new CommandParser();
        cp.parse(args);
        if (cp.getHelpFlag()) {
            System.out.println("Usage: ConfGen [-vht] SOURCE DESTINATION PACKAGENAME");
            return;
        }
        if (cp.getErrFlag()) {
            System.out.println("ConfGen: command line parse error");
            System.out.println("ConfGen: " + cp.getError());
            return;
        }
        DeploymentManager dm = new DeploymentManager(cp.getDest(), cp.getPackage());
        if (cp.getTestFlag()) {
            System.out.println("ConfGen: system test enabled");
            gc.test();
            return;
        }
        if (cp.getVerbFlag()) {
            System.out.println("ConfGen: verbose display enabled");
            dm.setVerbose(true);
        }
        System.out.println("Generating Source...");
        gc.generateConf(dm, cp);
        System.out.println("Done.");
    }

    /** this method generates conformance
    * @param dm the DeploymentManager
    * @param cp the CommandParser which parses the command line argument of ConfGen 
    */
    public void generateConf(DeploymentManager dm, CommandParser cp) {
        try {
            File f = new File(cp.getSource());
            BufferedReader in = new BufferedReader(new FileReader(f));
            char[] cbuf = new char[(int) f.length()];
            in.read(cbuf, 0, (int) f.length());
            dm.generate(String.valueOf(cbuf));
        } catch (FileNotFoundException e) {
            System.out.println("Filenotfoundexception: " + e.toString());
        } catch (IOException e) {
            System.out.println("IOexception:\n" + e.toString() + "\n");
        } catch (ConformanceError e) {
            System.out.println("ConformanceError:\n" + e.toString() + "\n");
        } catch (ConformanceException e) {
            System.out.println("ConformanceException:\n" + e.toString() + "\n");
        }
    }

    /** this method tests the conformance generator to see if it is working
    */
    public void test() {
        try {
            System.out.print("Checking for XML Parser:");
            Class.forName("org.w3c.dom.Element");
            System.out.println("           PASS");
        } catch (ClassNotFoundException e) {
            System.out.println("           FAIL");
        }
        try {
            System.out.print("Checking for Xerces:");
            Class.forName("org.apache.xerces.parsers.DOMParser");
            System.out.println("               PASS");
        } catch (ClassNotFoundException e) {
            System.out.println("               FAIL");
        }
        try {
            System.out.print("Checking for Conformance Classes:");
            Class.forName("ca.uhn.hl7v2.conf.classes.generator.builders.ConformanceMessageBuilder");
            System.out.println("  PASS");
        } catch (ClassNotFoundException e) {
            System.out.println("  FAIL");
        }
        try {
            System.out.print("Checking for Apache Ant:");
            Class.forName("org.apache.tools.ant.Main");
            System.out.println("           PASS");
        } catch (ClassNotFoundException e) {
            System.out.println("           FAIL");
        }
    }
}
