package gpsmate.io.kml;

import gpsmate.geodata.Placemark;
import gpsmate.utils.SymbolInfo;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * KmlPlacemark
 * 
 * @author raphael.spreitzer, longdistancewalker
 */
public class KmlPlacemark implements KmlRepresentable {

    public static final String TAG_PLACEMARK = "Placemark";

    public static final String TAG_NAME = "name";

    public static final String TAG_DESCRIPTION = "description";

    private Placemark p = null;

    KmlPlacemark(Placemark p) {
        this.p = p;
    }

    @Override
    public Element toKmlElement(Document doc, KmlExporter exporter) {
        Element el = doc.createElement(TAG_PLACEMARK);
        Element name = doc.createElement(TAG_NAME);
        name.setTextContent(p.getName());
        el.appendChild(name);
        Element desc = doc.createElement(TAG_DESCRIPTION);
        desc.setTextContent(p.getDescription());
        el.appendChild(desc);
        KmlPoint at = new KmlPoint(p.getLocation());
        el.appendChild(at.toKmlElement(doc, exporter));
        KmlIconStyle icon = new KmlIconStyle(SymbolInfo.getSymbol(p));
        List<KmlStyleElement> styles = new ArrayList<KmlStyleElement>();
        styles.add(icon);
        KmlStyle style = new KmlStyle(styles);
        el.appendChild(style.toKmlElement(doc, exporter));
        return el;
    }
}
