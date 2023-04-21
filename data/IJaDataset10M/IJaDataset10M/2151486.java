package org.primordion.xholon.service.netlogo;

import org.nlogo.app.App;

public class WithGuiController extends NetLogoController {

    public Object setup(String setupStr) {
        final String[] args = setupStr.split(",");
        setRoleName(args[0]);
        String[] argv = new String[0];
        App.main(argv);
        try {
            java.awt.EventQueue.invokeAndWait(new Runnable() {

                public void run() {
                    App.app.openFromSource(args[0], getSourceFromFile(args[3], args[4]));
                }
            });
            if (args.length >= 6) {
                App.app.command(args[5]);
            }
            if (args.length >= 7) {
                return (App.app.report(args[6]));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Object goOnce(String reporter) {
        try {
            App.app.command("go");
            if (reporter != null) {
                return (App.app.report(reporter));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Object command(String command) {
        try {
            App.app.command(command);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Object reporter(String reporter) {
        try {
            return (App.app.report(reporter));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
