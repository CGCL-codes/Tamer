package voji.vdialog;

import java.lang.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import voji.log.*;

public class VDialog {

    private static Class internClass;

    public static Class setInternClass(Class initInternClass) {
        if (VDialogInterface.class.isAssignableFrom(initInternClass)) internClass = initInternClass;
        return internClass;
    }

    public static Class setInternClass(String initInternClass) throws ClassNotFoundException {
        return setInternClass(Class.forName(initInternClass));
    }

    protected static VDialogInterface newInternClassInstance() {
        try {
            return (VDialogInterface) internClass.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    private final VDialogInterface internDialog = newInternClassInstance();

    protected final Container contentPane = internDialog.getContentPane();

    public VDialog(String title, int sizeX, int sizeY, LayoutManager layout) {
        internDialog.setTitle(title);
        internDialog.setLocation(new Point(0, 50));
        internDialog.setSize(new Dimension(sizeX, sizeY));
        contentPane.setLayout(layout);
        progressBar = new JProgressBar();
        progressBar.setString("");
        progressBar.setStringPainted(true);
    }

    public void show() {
        internDialog.show();
        doInit();
    }

    public void close() {
        internDialog.close();
    }

    private int initLock = 0;

    protected void lockInit() {
        initLock++;
    }

    protected void releaseInit() {
        initLock--;
    }

    public void doInit() {
        if (initLock <= 0) init();
    }

    protected void init() {
    }

    private Vector enabling = new Vector();

    protected void addEnabling(Component component) {
        enabling.add(component);
    }

    private Component lastFocus = null;

    public void setEnabled(boolean enabled) {
        if (!enabled) {
            lockInit();
            lastFocus = internDialog.getFocusOwner();
        }
        for (Iterator i = enabling.iterator(); i.hasNext(); ) ((Component) i.next()).setEnabled(enabled);
        if (enabled) {
            if (lastFocus != null) lastFocus.requestFocus();
            releaseInit();
        }
    }

    protected final JProgressBar progressBar;

    public static final String ERROR = "ERROR";

    protected abstract class Job extends Thread {

        private int maximum;

        public Job(int initMaximum) {
            maximum = initMaximum;
        }

        public void run() {
            setEnabled(false);
            progressBar.setMaximum(maximum);
            progressBar.setValue(0);
            try {
                doJob();
                progressBar.setString("");
                progressBar.setValue(0);
            } catch (Exception e) {
                Log.log(e);
                progressBar.setString(ERROR);
            }
            setEnabled(true);
            afterJob();
        }

        public void nextStep() {
            progressBar.setValue(progressBar.getValue() + 1);
        }

        public void nextStep(String description) {
            progressBar.setString(description);
            nextStep();
        }

        public void afterJob() {
        }

        public abstract void doJob() throws Exception;
    }
}
