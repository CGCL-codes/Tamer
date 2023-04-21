package com.codename1;

import com.codename1.capture.Capture;
import com.codename1.ui.Button;
import com.codename1.ui.ComponentGroup;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import java.io.IOException;
import java.io.InputStream;

/**
 * This is a simple Demo that demonstrates how to the Camera API
 */
public class CameraDemo {

    public void init(Object context) {
        System.out.println("init");
        try {
            Resources theme = Resources.openLayered("/theme");
            UIManager.getInstance().setThemeProps(theme.getTheme(theme.getThemeResourceNames()[0]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        System.out.println("started");
        final Form f = new Form("Camera");
        f.setLayout(new BorderLayout());
        f.setScrollable(false);
        Button b = new Button("Take A Picture");
        final Label l = new Label();
        f.addComponent(BorderLayout.CENTER, l);
        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                Image i = l.getStyle().getBgImage();
                if (i != null) {
                    i.dispose();
                }
                l.getStyle().setBgImage(null);
                Capture.capturePhoto(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        InputStream is = null;
                        try {
                            String path = (String) evt.getSource();
                            System.out.println("path " + path);
                            is = com.codename1.io.FileSystemStorage.getInstance().openInputStream(path);
                            Image i = Image.createImage(is);
                            l.setIcon(i.scaledWidth(300));
                            f.revalidate();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        } finally {
                            try {
                                is.close();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
        ComponentGroup g = new ComponentGroup();
        g.addComponent(b);
        f.addComponent(BorderLayout.SOUTH, g);
        f.show();
    }

    public void stop() {
        System.out.println("stopped");
    }

    public void destroy() {
        System.out.println("destroyed");
    }
}
