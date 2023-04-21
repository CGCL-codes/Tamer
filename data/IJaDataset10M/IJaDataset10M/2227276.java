package eu.planets_project.pp.plato.services.characterisation.xcl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import eu.planets_project.pp.plato.model.characterisation.xcl.XCLObjectProperty;
import eu.planets_project.pp.plato.model.measurement.Metric;
import eu.planets_project.pp.plato.services.PlatoServiceException;
import eu.planets_project.pp.plato.services.characterisation.comparator.ComparatorCompareMultipleXcdlValues;
import eu.planets_project.pp.plato.util.PlatoLogger;
import eu.planets_project.services.datatypes.Parameter;

/**
 * this is an old wrapper for the XCL comparator tool
 *  
 * @author kraxner
 *
 */
@Deprecated
public class Comparator implements Serializable {

    private static final long serialVersionUID = 6847523712380140405L;

    private static final Log log = PlatoLogger.getLogger(Comparator.class);

    private ComparatorCompareMultipleXcdlValues comparatorPort;

    public CompareResult compare(String sourceXcdl, String targetXcdl, Set<XCLObjectProperty> mappedProperties, StringBuffer xmlResult) throws PlatoServiceException {
        return null;
    }

    /**
     * generates a list of parameters, one {@link Parameter} per mappedProperty
     * 
     * @param mappedProperties
     * @return
     */
    private List<Parameter> generateConfigAsParameters(Set<XCLObjectProperty> mappedProperties) {
        List<Parameter> result = new ArrayList<Parameter>();
        for (XCLObjectProperty objectProperty : mappedProperties) {
            String propertyName = objectProperty.getName();
            StringBuilder metrics = new StringBuilder();
            for (Metric metric : objectProperty.getMetrics()) {
                metrics.append("metric").append(" ").append(metric.getName()).append(" ").append(String.valueOf(metric.getMetricId())).append(",");
            }
            String mString = metrics.toString();
            if (mString.endsWith(",")) {
                mString = mString.substring(0, mString.length() - 1);
            }
            String propId = String.valueOf(objectProperty.getId());
            Parameter prop = new Parameter.Builder(propertyName, mString).type(propId).build();
            result.add(prop);
        }
        return result;
    }

    /**
     * Generates a PCR for all <param>mappedProperties</param> and their metrics.
     * Note: All possible metrics are included.
     * 
     * @param mappedProperties
     * @return
     */
    private String generateConfig(Set<XCLObjectProperty> mappedProperties) {
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("pcRequest");
        Namespace xsi = new Namespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        root.add(xsi);
        root.addAttribute(xsi.getPrefix() + ":schemaLocation", "http://www.planets-project.eu/xcl/schemas/xcl data/pp5/schemas/pcr/pcr.xsd");
        Namespace xcl = new Namespace("", "http://www.planets-project.eu/xcl/schemas/xcl");
        root.add(xcl);
        Element compSet = root.addElement("compSet", xcl.getURI());
        compSet.addElement("source").addAttribute("name", "samplerecord");
        compSet.addElement("target").addAttribute("name", "experimentresult");
        for (XCLObjectProperty objectProperty : mappedProperties) {
            Element prop = compSet.addElement("property").addAttribute("id", objectProperty.getPropertyId()).addAttribute("name", objectProperty.getName());
            for (Metric metric : objectProperty.getMetrics()) {
                prop.addElement("metric").addAttribute("id", metric.getMetricId()).addAttribute("name", metric.getName());
            }
        }
        return doc.asXML();
    }

    public static void main(String[] args) {
        Comparator comp = new Comparator();
        try {
            InputStream in = new FileInputStream("d:/workspace/plato/data/pp5/polarbear1.tiff.xcdl-vs-polarbear1.tiff.xcdl.cpr");
            ComparatorUtils compUtils = new ComparatorUtils();
            List<CompareResult> result = compUtils.parseResponse("d:/workspace/plato/data/pp5/polarbear1.tiff.xcdl-vs-polarbear1.tiff.xcdl.cpr");
            System.out.print("num of comps: " + result.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (PlatoServiceException e) {
            e.printStackTrace();
        }
    }
}
