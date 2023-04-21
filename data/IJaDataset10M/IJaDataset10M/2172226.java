package org.one.stone.soup.grfxML.plugin.swing;

import javax.swing.JOptionPane;
import org.one.stone.soup.grfxML.DataState;
import org.one.stone.soup.grfxML.DataString;
import org.one.stone.soup.grfxML.DataText;
import org.one.stone.soup.grfxML.GrfxMLEngine;
import org.one.stone.soup.grfxML.plugin.SimplePlugin;
import org.one.stone.soup.grfxML.plugin.grfxMLCaster;
import org.one.stone.soup.mjdb.data.field.Field;
import org.one.stone.soup.mjdb.data.field.LockException;

public class JDropDownPlugin extends SimplePlugin implements Runnable {

    private static final int MESSAGE_ARG = 0;

    private static final int OPTIONS_ARG = 1;

    private static final int TARGET_ARG = 2;

    private static final int ENTER_ARG = 3;

    private DataString message = new DataString();

    private DataText options = new DataText();

    private DataString target = new DataString();

    private DataState enter = new DataState();

    /**
 * FileToText constructor comment.
 */
    public JDropDownPlugin(GrfxMLEngine engine) {
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

    public void run() {
        try {
            String[] optionList = new String[options.size()];
            for (int loop = 0; loop < options.size(); loop++) {
                optionList[loop] = options.get(loop).getValue();
            }
            String newValue = (String) JOptionPane.showInputDialog(null, message.getValue(), "", JOptionPane.PLAIN_MESSAGE, null, optionList, target.getValue());
            if (newValue != null) {
                target.setValue(newValue, this);
            }
        } catch (LockException le) {
        }
    }

    /**
 * register method comment.
 */
    public void register(org.one.stone.soup.mjdb.data.field.DataLinker store) {
        message = grfxMLCaster.cast(message, getArg(message, MESSAGE_ARG, store));
        options = grfxMLCaster.cast(options, getArg(options, OPTIONS_ARG, store));
        target = grfxMLCaster.cast(target, getArg(target, TARGET_ARG, store));
        enter = grfxMLCaster.cast(enter, getArg(enter, ENTER_ARG, store));
    }

    public void replace(Field oldObj, Field newObj) {
        message = grfxMLCaster.replace(message, oldObj, newObj);
        options = grfxMLCaster.replace(options, oldObj, newObj);
        target = grfxMLCaster.replace(target, oldObj, newObj);
        enter = grfxMLCaster.replace(enter, oldObj, newObj);
    }

    /**
 * stop method comment.
 */
    public void stop() {
    }
}
