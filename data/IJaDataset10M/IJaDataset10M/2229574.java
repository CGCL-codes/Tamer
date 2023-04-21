package se.sics.cooja;

import javax.swing.*;

public class CoojaApplet extends JApplet {

    public static CoojaApplet applet = null;

    public void init() {
        applet = this;
    }

    public void start() {
        String contikiWebPath = getParameter("contiki_web");
        String contikiBuildPath = getParameter("contiki_build");
        String skyFirmware = getParameter("sky_firmware");
        String esbFirmware = getParameter("esb_firmware");
        if (skyFirmware == null) {
            skyFirmware = "";
        }
        if (esbFirmware == null) {
            esbFirmware = "";
        }
        GUI.main(new String[] { "-applet ", "-esb_firmware=" + esbFirmware, "-sky_firmware=" + skyFirmware, "-web=" + contikiWebPath, "-build=" + contikiBuildPath });
    }

    public void stop() {
    }
}
