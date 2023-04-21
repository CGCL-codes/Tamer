package genj.edit.beans;

import genj.gedcom.Indi;
import genj.gedcom.Property;
import genj.gedcom.PropertyDate;
import genj.gedcom.PropertyEvent;
import genj.gedcom.time.Delta;
import genj.gedcom.time.PointInTime;
import genj.util.swing.NestedBlockLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * A bean for editing Events
 * @author Nils Meier
 * @author Daniel Kionka
 */
public class EventBean extends PropertyBean {

    private static final NestedBlockLayout LAYOUT = new NestedBlockLayout("<col><row><at/><age wx=\"1\"/></row><row><known/></row></col>");

    /** known to have happened */
    private JCheckBox cKnown;

    private JLabel lAgeAt;

    private JTextField tAge;

    public EventBean() {
        setLayout(LAYOUT.copy());
        lAgeAt = new JLabel();
        tAge = new JTextField("", 16);
        tAge.setEditable(false);
        tAge.setFocusable(false);
        cKnown = new JCheckBox(RESOURCES.getString("even.known"));
        cKnown.addActionListener(changeSupport);
        add(lAgeAt);
        add(tAge);
        add(cKnown);
    }

    /**
   * Finish proxying edit for property Birth
   */
    protected void commitImpl(Property property) {
        if (cKnown.isVisible()) {
            ((PropertyEvent) property).setKnownToHaveHappened(cKnown.isSelected());
        }
    }

    /**
   * Nothing to edit
   */
    public boolean isEditable() {
        return cKnown.isVisible();
    }

    /**
   * Set context to edit
   */
    public void setPropertyImpl(Property prop) {
        PropertyEvent event = (PropertyEvent) prop;
        Boolean known = null;
        if (event != null && event.getEntity() instanceof Indi) {
            Indi indi = (Indi) event.getEntity();
            PropertyDate date = event.getDate(true);
            String ageat = "even.age";
            String age = "";
            if ("BIRT".equals(event.getTag())) {
                ageat = "even.age.today";
                if (date != null) {
                    Delta delta = Delta.get(date.getStart(), PointInTime.getNow());
                    if (delta != null) age = delta.toString();
                }
            } else {
                age = date != null ? indi.getAgeString(date.getStart()) : RESOURCES.getString("even.age.?");
            }
            lAgeAt.setText(RESOURCES.getString(ageat));
            tAge.setText(age);
            lAgeAt.setVisible(true);
            tAge.setVisible(true);
            if (!"EVEN".equals(event.getTag())) known = event.isKnownToHaveHappened();
        } else {
            lAgeAt.setVisible(false);
            tAge.setVisible(false);
        }
        if (known != null) {
            cKnown.setSelected(known.booleanValue());
            cKnown.setVisible(true);
            defaultFocus = cKnown;
        } else {
            cKnown.setVisible(false);
            defaultFocus = null;
        }
    }
}
