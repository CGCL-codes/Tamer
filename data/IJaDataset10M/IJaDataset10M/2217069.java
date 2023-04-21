package org.onemind.swingweb.demo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import org.onemind.swingweb.component.shareapp.ViewComponent;
import org.onemind.swingweb.session.URLLocal;
import org.onemind.swingweb.util.SwingWebUtils;

public class URLDemo extends JFrame implements ActionListener {

    private final String KEY_SECTION1 = "App1";

    private final String KEY_SECTION2 = "App2";

    private final String KEY_SECTION3 = "App3";

    private Map mappedSection = new HashMap();

    URLLocal viewValue = new URLLocal("section", KEY_SECTION1, 0, true);

    public URLDemo() {
        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());
        SwingWebUtils.registerURLLocal(viewValue);
        ViewComponent com = new ViewComponent(viewValue);
        com.addView(KEY_SECTION1, new JLabel("This is section 1"));
        com.addView(KEY_SECTION2, new JLabel("This is section 2"));
        com.addView(KEY_SECTION3, new JLabel("This is section 3"));
        JPanel buttons = new JPanel();
        buttons.add(createButton("Section 1", KEY_SECTION1));
        buttons.add(createButton("Section 2", KEY_SECTION2));
        buttons.add(createButton("Section 3", KEY_SECTION3));
        main.add(buttons, BorderLayout.NORTH);
        main.add(com, BorderLayout.CENTER);
        getContentPane().add(main);
    }

    private JButton createButton(String name, String section) {
        JButton button = new JButton(name);
        button.addActionListener(this);
        mappedSection.put(button, section);
        return button;
    }

    public void actionPerformed(ActionEvent e) {
        String section = (String) mappedSection.get(e.getSource());
        if (section != null) {
            viewValue.setValue(section);
        }
    }
}
