package com.codename1.demos.kitchen;

import com.codename1.facebook.ui.LikeButton;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.ComponentGroup;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import java.io.IOException;

/**
 *
 * @author Shai Almog
 */
public class Push extends Demo {

    public String getDisplayName() {
        return "Push";
    }

    public Image getDemoIcon() {
        return getResources().getImage("share-icon.png");
    }

    public Container createDemo() {
        return null;
    }

    public Container createDemo(final Form parentForm) {
        ComponentGroup grp = new ComponentGroup();
        final TextField tf = new TextField("");
        tf.setHint("ID/Email");
        grp.addComponent(tf);
        Button register = new Button("Register Push");
        Button deregister = new Button("Deregister");
        grp.addComponent(register);
        grp.addComponent(deregister);
        register.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                Display.getInstance().registerPush(tf.getText(), true);
            }
        });
        deregister.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                Display.getInstance().deregisterPush();
            }
        });
        return grp;
    }
}
