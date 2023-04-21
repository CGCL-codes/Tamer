package com.neurogrid.md5;

import com.neurogrid.md5.MD5;
import java.applet.Applet;
import java.awt.TextField;
import java.awt.Button;
import java.awt.Event;

/** 
 * Hasher
 *
 * @version	$Revision: 1.1 $
 * @author	Santeri Paavolainen <santtu@iki.fi>
 */
public class Hasher extends Applet {

    TextField input, result;

    Button update;

    public void init() {
        input = new TextField(20);
        result = new TextField(20);
        update = new Button();
        input.setEditable(true);
        result.setEditable(false);
        update.setLabel("Calculate hash value");
        setLayout(new java.awt.GridLayout(0, 1));
        add(input);
        add(update);
        add(result);
        validate();
    }

    public boolean action(Event e, Object arg) {
        Object ob = e.target;
        if (ob == update) {
            perform_update();
            return true;
        }
        return false;
    }

    void perform_update() {
        MD5 hash;
        String hex;
        hash = new MD5(input.getText());
        result.setText(hash.asHex());
    }
}
