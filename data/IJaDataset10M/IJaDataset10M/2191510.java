package com.silenistudios.silenus.java;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import com.silenistudios.silenus.ParseException;
import com.silenistudios.silenus.RawJavaRenderer;
import com.silenistudios.silenus.XFLDocument;

/**
 * This demo will take any XFL directory from the command line, and render it to screen.
 * @author Karel
 *
 */
public class Main {

    public static void main(String[] args) {
        String directoryName = "example/example.fla";
        if (args.length > 0) directoryName = args[0];
        XFLDocument xfl = new XFLDocument();
        try {
            System.out.println("Parsing document in directory '" + directoryName + "'");
            xfl.parseXFL(directoryName);
            System.out.println("Drawing document...");
            openInJFrame(new RawJavaRenderer(xfl), xfl.getWidth(), xfl.getHeight(), "Silenus demo");
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Done!");
    }

    public static JFrame openInJFrame(Container content, int width, int height, String title) {
        JFrame frame = new JFrame(title);
        frame.setBackground(Color.white);
        content.setBackground(Color.white);
        frame.setSize(width, height + 50);
        frame.setContentPane(content);
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent event) {
                System.exit(0);
            }
        });
        frame.setVisible(true);
        return (frame);
    }
}
