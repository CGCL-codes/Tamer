package org.one.stone.soup.grfxML.plugin.swing;

import java.awt.Component;
import java.awt.Frame;
import javax.swing.JDialog;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.one.stone.soup.grfxML.DataSize;
import org.one.stone.soup.grfxML.DataState;
import org.one.stone.soup.grfxML.DataString;
import org.one.stone.soup.grfxML.GrfxMLEngine;
import org.one.stone.soup.grfxML.plugin.SimplePlugin;
import org.one.stone.soup.grfxML.plugin.grfxMLCaster;
import org.one.stone.soup.mjdb.data.field.Field;
import org.one.stone.soup.mjdb.data.field.LockException;

public class JSizeFieldPlugin extends SimplePlugin implements Runnable, ChangeListener {

    private static final int MESSAGE_ARG = 0;

    private static final int TARGET_ARG = 1;

    private static final int ENTER_ARG = 2;

    private DataString message = new DataString();

    private DataSize target = new DataSize();

    private DataState enter = new DataState();

    private JSpinner spinner;

    /**
 * FileToText constructor comment.
 */
    public JSizeFieldPlugin(GrfxMLEngine engine) {
        super(engine);
    }

    /**
 * initialize method comment.
 */
    public void initialize() {
    }

    /**
 * process method comment.
 */
    public void process() {
        try {
            if (enter.getValue() == true) {
                new Thread(this).start();
                enter.setValue(false, this);
            }
        } catch (LockException le) {
        }
    }

    public void stateChanged(ChangeEvent ce) {
        try {
            Integer newValue = (Integer) spinner.getValue();
            if (newValue != null) {
                target.setValue(newValue.intValue(), this);
            }
        } catch (Exception e) {
        }
    }

    public void run() {
        spinner = new JSpinner();
        spinner.setValue(new Integer(target.getValue()));
        spinner.addChangeListener(this);
        Component parent = getEngine().getController().getComponent();
        while (parent != null && !(parent instanceof Frame)) {
            parent = parent.getParent();
        }
        JDialog dialog = null;
        if (parent == null) {
            dialog = new JDialog();
        } else {
            dialog = new JDialog((Frame) parent);
        }
        dialog.setTitle(message.getValue());
        dialog.getContentPane().add(spinner);
        dialog.setModal(true);
        dialog.pack();
        dialog.setVisible(true);
    }

    /**
 * register method comment.
 */
    public void register(org.one.stone.soup.mjdb.data.field.DataLinker store) {
        message = grfxMLCaster.cast(message, getArg(message, MESSAGE_ARG, store));
        target = grfxMLCaster.cast(target, getArg(target, TARGET_ARG, store));
        enter = grfxMLCaster.cast(enter, getArg(enter, ENTER_ARG, store));
    }

    public void replace(Field oldObj, Field newObj) {
        message = grfxMLCaster.replace(message, oldObj, newObj);
        target = grfxMLCaster.replace(target, oldObj, newObj);
        enter = grfxMLCaster.replace(enter, oldObj, newObj);
    }

    /**
 * stop method comment.
 */
    public void stop() {
    }
}
