package cdox.util.conf;

import cdox.util.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;

/**
 * This class represents a language option. An appropriate section in the XML file might
 * look like this single line:
 *<pre>
 *  &lt;language package="cdox.CDox" key="language"/&gt;
 *</pre>
 * @author <a href="mailto:cdox@gmx.net">Rutger Bezema, Andreas Schmitz</a>
 * @version May 22nd 2002
 */
public class LanguageOption extends AbstractOption {

    private Localizer lang;

    private Properties props;

    private String iso3;

    private JComboBox box;

    /**
     * Constructs one.
     *@param n the DOM node where this option is described.
     *@param lang a localizer.
     */
    public LanguageOption(Node n, Localizer lang) {
        super(n.getAttributes().getNamedItem("package").getNodeValue(), n.getAttributes().getNamedItem("key").getNodeValue(), n);
        this.lang = lang;
        props = lang.getAvailableLanguages();
    }

    public JLabel getText() {
        return new JLabel(lang.get("tlanguage") + ":");
    }

    public JComponent getComponent() {
        box = new JComboBox(props.keySet().toArray());
        reset();
        box.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                iso3 = props.getProperty(box.getSelectedItem().toString());
            }
        });
        return box;
    }

    public boolean commitChanges() {
        prefs.put(key, iso3);
        return (!iso3.equals(lang.getLanguageCode()));
    }

    /**
     * This option type has no default value in the xml file, so it re-reads its value
     * from the preferences (doing a simple reset()). The default value of a language
     * option is always English, if it does not exist in the preferences.
     *@see AbstractOption#setToDefault
     *@see #reset
     */
    public void setToDefault() {
        reset();
    }

    public void reset() {
        Iterator i = props.keySet().iterator();
        iso3 = prefs.get(key, "eng");
        String current = null;
        while (i.hasNext()) {
            String h = i.next().toString();
            if (props.getProperty(h).equals(iso3)) current = h;
        }
        box.setSelectedItem(current);
    }
}
