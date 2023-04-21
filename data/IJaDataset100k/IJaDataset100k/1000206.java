package co.edu.unal.ungrid.filtering;

import java.util.HashMap;
import java.util.Map;
import org.jdom.Element;
import co.edu.unal.ungrid.xml.XmlUtil;

public abstract class FilterParametersFactory {

    protected abstract FilterParameters create();

    public static void register(final String cls, final FilterParametersFactory factory) {
        m_factories.put(cls, factory);
    }

    public static final FilterParameters getInstance(final Element root) {
        return getInstance(XmlUtil.getParameter(root, "filter-parameters-class"));
    }

    public static final FilterParameters getInstance(final String cls) {
        assert cls != null;
        if (!m_factories.containsKey(cls)) {
            try {
                Class.forName(cls);
            } catch (ClassNotFoundException exc) {
                return null;
            }
            if (!m_factories.containsKey(cls)) {
                return null;
            }
        }
        FilterParametersFactory factory = m_factories.get(cls);
        return factory.create();
    }

    /**
	 * @uml.property name="m_factories"
	 * @uml.associationEnd qualifier="key:java.lang.Object
	 *                     co.edu.unal.ungrid.transformation.FilterParametersFactory"
	 */
    private static Map<String, FilterParametersFactory> m_factories = new HashMap<String, FilterParametersFactory>();
}
