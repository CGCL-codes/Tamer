package de.fzi.injectj.frontendnew.swing.controls;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JPanel;
import de.fzi.injectj.util.settings.MetaProperty;
import de.fzi.injectj.util.settings.Settings;
import de.fzi.injectj.util.xml.XmlElement;

/**
 * Action panel
 * 
 * @author <a href="mailto:mies@fzi.de">Sebastian Mies</a>
 */
public class ActionControl extends SettingControl implements ActionListener {

    private HashMap actionMap;

    /**
	 * @param property
	 */
    public ActionControl(MetaProperty property) {
        super(property);
    }

    protected void init() {
        panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionMap = new HashMap();
        XmlElement[] e = property.getMeta().getChildrenArray("action");
        for (int i = 0; i < e.length; i++) {
            String desc = e[i].getProperty("desc");
            String edit = e[i].getProperty("edit");
            MetaProperty p = property.getOwner().get(edit);
            JButton button = new JButton(desc);
            panel.add(button);
            actionMap.put(button, p);
            button.addActionListener(this);
        }
    }

    public void refresh() {
    }

    public void apply() {
    }

    public void actionPerformed(ActionEvent e) {
        MetaProperty p = (MetaProperty) actionMap.get(e.getSource());
        Settings s = p.getSettings();
        if (s != null) editSettings(s);
    }
}
