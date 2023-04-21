package ch.intertec.storybook.toolkit.swing.panel;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import org.apache.commons.lang3.text.WordUtils;
import ch.intertec.storybook.model.hbn.entity.AbstractEntity;
import ch.intertec.storybook.model.hbn.entity.Location;

/**
 * @author martin
 * 
 */
public class LocationCbPanelDecorator extends CbPanelDecorator {

    private String oldCity = "";

    private String oldCountry = "";

    public LocationCbPanelDecorator() {
    }

    @Override
    public void decorateBeforeEntity(AbstractEntity entity) {
        Location p = (Location) entity;
        String country = WordUtils.capitalize(p.getCountry());
        String city = WordUtils.capitalize(p.getCity());
        if (!oldCountry.equals(country) || !oldCity.equals(city)) {
            StringBuffer buf = new StringBuffer();
            buf.append("<html>");
            buf.append("<p style='margin-top:5px'>");
            buf.append("<b>" + city + "</b>");
            if (!country.isEmpty()) {
                if (!city.isEmpty()) {
                    buf.append(" (");
                } else {
                    buf.append("<b>");
                }
                buf.append(country);
                if (!city.isEmpty()) {
                    buf.append(")");
                } else {
                    buf.append("</b>");
                }
            }
            JLabel lb = new JLabel(buf.toString());
            panel.add(lb, "span");
            oldCountry = country;
            oldCity = city;
        }
    }

    @Override
    public void decorateEntity(JCheckBox cb, AbstractEntity entity) {
        if (!oldCountry.isEmpty() || !oldCity.isEmpty()) {
            panel.add(new JLabel("<html><p style='margin-left:5px'>&nbsp;"), "split 2");
        }
        panel.add(cb);
    }

    @Override
    public void decorateAfterEntity(AbstractEntity entity) {
    }
}
