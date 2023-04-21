package net.sf.jftp.gui.base;

import net.sf.jftp.*;
import net.sf.jftp.gui.framework.*;
import net.sf.jftp.system.logging.Log;
import net.sf.jftp.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Properties extends HFrame implements ActionListener {

    private Label fileL = new Label("File:                      ");

    private Label sizeL = new Label("Size: ? bytes              ");

    private HButton ok = new HButton("Dismiss");

    private HPanel okP = new HPanel();

    private String type = "";

    private String file = "";

    public Properties(String file, String type) {
        this.file = file;
        this.type = type;
        setSize(300, 110);
        setTitle("File properties...");
        setLocation(150, 150);
        setLayout(new GridLayout(3, 1));
        okP.add(ok);
        add(sizeL);
        add(fileL);
        add(okP);
        ok.addActionListener(this);
        process();
        setVisible(true);
    }

    private void process() {
        if (type.equals("local")) {
            File f = new File(JFtp.localDir.getPath() + file);
            sizeL.setText("Size: " + Long.toString(f.length()) + " bytes");
            try {
                fileL.setText("File: " + f.getCanonicalPath());
            } catch (Exception ex) {
                Log.debug(ex.toString());
            }
            sizeL.setText("Size: " + Long.toString(f.length()) + " bytes");
        }
        if (type.equals("remote")) {
            fileL.setText("File: " + JFtp.remoteDir.getPath() + file);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ok) {
            setVisible(false);
        }
    }
}
