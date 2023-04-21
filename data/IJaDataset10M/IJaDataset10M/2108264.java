package net.laubenberger.bogatyr.misc.xml.adapter;

import java.awt.Dimension;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import net.laubenberger.bogatyr.misc.xml.XmlEntry;
import net.laubenberger.bogatyr.misc.xml.XmlMap;

/**
 * Map adapter for {@link Dimension}.
 *
 * @author Stefan Laubenberger
 * @version 0.9.3 (20100909)
 * @since 0.9.3
 */
public class AdapterDimension extends XmlAdapter<XmlMap, Dimension> {

    @Override
    public XmlMap marshal(final Dimension dim) throws Exception {
        if (null != dim) {
            final XmlMap xmlMap = new XmlMap();
            xmlMap.getEntries().add(new XmlEntry("x", String.valueOf(dim.width)));
            xmlMap.getEntries().add(new XmlEntry("y", String.valueOf(dim.height)));
            return xmlMap;
        }
        return null;
    }

    @Override
    public Dimension unmarshal(final XmlMap xmlMap) throws Exception {
        if (null != xmlMap && 2 == xmlMap.getEntries().size()) {
            return new Dimension(Integer.valueOf(xmlMap.getEntries().get(0).getValue()), Integer.valueOf(xmlMap.getEntries().get(1).getValue()));
        }
        return null;
    }
}
