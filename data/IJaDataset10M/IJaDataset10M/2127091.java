package com.sts.webmeet.client;

import java.applet.*;
import java.awt.*;
import java.net.URL;

public class BootStrapper extends Applet implements AppletStub {

    public void init() {
        setLayout(new BorderLayout());
        context = getAppletContext();
        boolean bIsMSJVM = false;
        boolean bPrivilegesDenied = true;
        boolean bTest = false;
        if (System.getProperty("java.vendor").indexOf("Microsoft") > -1) {
            bIsMSJVM = true;
            try {
                if (Class.forName("com.ms.security.PolicyEngine") != null) {
                    com.ms.security.PolicyEngine.assertPermission(com.ms.security.PermissionID.SYSTEM);
                    System.getProperty("com.ms.sysdir");
                    bPrivilegesDenied = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        boolean bSandbox = false;
        if (null != getParameter("sandboxClient")) {
            bSandbox = getParameter("sandboxClient").equalsIgnoreCase("true");
        }
        boolean bCabIsSigned = false;
        if (null != getParameter("CABINETS")) {
            bCabIsSigned = getParameter("CABINETS").indexOf("_signed.cab") > -1;
        }
        if (((bIsMSJVM && bPrivilegesDenied) || bTest) && !bSandbox && bCabIsSigned) {
            try {
                URL url = new URL(getCodeBase(), getParameter("privileges_denied_page") + "&tokenParameter=wow");
                System.out.println("navigating to: " + url);
                getAppletContext().showDocument(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                Class clazz = Class.forName(getParameter("targetClass"));
                applet = (Applet) (clazz.newInstance());
                applet.setStub(this);
                add(applet, BorderLayout.CENTER);
                applet.init();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        if (null != applet) {
            applet.start();
        }
    }

    public void stop() {
        if (null != applet) {
            applet.stop();
        }
    }

    public void destroy() {
        if (null != applet) {
            applet.destroy();
        }
    }

    public void appletResize(int iWidth, int iHeight) {
        resize(iWidth, iHeight);
    }

    public boolean isMeetingOver() {
        boolean bRet = (null == this.applet || ((AWTClient) this.applet).isMeetingOver());
        return bRet;
    }

    public static void main(String[] args) throws Exception {
        BootStrapper bs = new BootStrapper();
        Applet applet = (Applet) (Class.forName("com.sts.webmeet.client.AWTClient").newInstance());
    }

    private Applet applet;

    private AppletContext context;
}
