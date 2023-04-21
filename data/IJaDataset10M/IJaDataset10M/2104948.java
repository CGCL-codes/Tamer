package molmaster.gui.editbar;

import molmaster.*;
import molmaster.gui.*;
import javax.swing.*;
import java.awt.event.*;

/**
 *
 * @author  rjpower
 */
public class AntialiasAction implements EditAction, CustomView {

    JCheckBox active = new JCheckBox("Antialiased Scene");

    class MyAction implements ActionListener {

        EditAction ee;

        MyAction(EditAction e) {
            ee = e;
        }

        public void actionPerformed(ActionEvent e) {
            javax.media.j3d.View v = MainFrame.uni.getViewer().getView();
            active.setSelected(!v.getSceneAntialiasingEnable());
            ActiveAction.setHandler(ee);
        }
    }

    /** Creates a new instance of AntialiasAction */
    public AntialiasAction() {
        active.addActionListener(new MyAction(this));
    }

    public javax.swing.AbstractButton getButton() {
        return active;
    }

    /** Called when a significant update has occured.  Significant is
     * defined as 'whatever Russell felt like implementing', but in general
     * all expected events ( mouse clicks, drags, etc. ) will be reported.
     */
    public void actionPerformed(Object source, molmaster.Event evt) {
        if (evt.getID() == handlerActivate) {
            javax.media.j3d.View v = MainFrame.uni.getViewer().getView();
            if (!v.getSceneAntialiasingEnable()) {
                v.setSceneAntialiasingEnable(true);
                new Thread(highRes).start();
            } else {
                v.setSceneAntialiasingEnable(false);
                new Thread(lowRes).start();
            }
        } else {
            ActiveAction.setDefaultHandler();
        }
    }

    protected static boolean lowRunning = false;

    protected static boolean highRunning = false;

    Runnable lowRes = new Runnable() {

        public void run() {
            if (lowRunning) {
                return;
            }
            lowRunning = true;
            try {
                Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
                AtomView.setResolution(AtomView.RESOLUTION_LOW);
                BondView.setResolution(BondView.RESOLUTION_LOW);
            } catch (Exception e) {
            } finally {
                lowRunning = false;
            }
        }
    };

    Runnable highRes = new Runnable() {

        public void run() {
            if (highRunning) {
                return;
            }
            highRunning = true;
            try {
                Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
                AtomView.setResolution(AtomView.RESOLUTION_HIGH);
                BondView.setResolution(BondView.RESOLUTION_HIGH);
            } catch (Exception e) {
            } finally {
                highRunning = false;
            }
        }
    };

    /** Returns the handler group this belongs to - selection handlers might compose
     * one group, while angle calculations would belong to another.  The groups
     * will be clustered on the screen to indicate their relation.
     */
    public String getHandlerGroup() {
        return "Rendering";
    }

    /** Optional.  Return null if not implemented.  Returns a graphical icon to
     * display along with the text for this handler.    */
    public java.awt.Image getHandlerImage() {
        return null;
    }

    /** This is displayed to the user on the selection button for this handler.  */
    public String getHandlerName() {
        return "Antialiasing";
    }
}
