package nl.beesting.beangenerator.generator;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import nl.beesting.beangenerator.TypeNotSupportedException;
import nl.beesting.beangenerator.util.ParameterDescriptor;
import nl.beesting.beangenerator.util.ParameterPair;
import nl.beesting.beangenerator.util.ParameterPairUtil;
import nl.beesting.beangenerator.util.RandomUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ListInstanceGenerator extends AbstractInstanceGenerator {

    /** The <code>logger</code> for this class. */
    private static final Log logger = LogFactory.getLog(ListInstanceGenerator.class);

    private static final String LIST_PARAMETER = "list";

    private List<?> list;

    public ListInstanceGenerator() {
        addSupportedTypes(new Class[] { java.lang.String.class, java.lang.StringBuffer.class });
        setSupportedParameterDescriptors(new ParameterDescriptor[] { new ParameterDescriptor(LIST_PARAMETER, String.class, "The contents of the list.") });
    }

    public void init(ParameterPair[] initParams) {
        if (initParams != null) {
            list = (List<?>) ParameterPairUtil.getFirstParameterValue(initParams, LIST_PARAMETER, false, ArrayList.class);
        }
    }

    public Object generateInstance(Class<?> type) throws TypeNotSupportedException {
        if (type == null) {
            throw new IllegalArgumentException("Input parameter type cannot be null.");
        }
        if (!checkSupport(type)) {
            throw new TypeNotSupportedException(type.getName(), "generateInstance");
        }
        String result = null;
        if (list != null) {
            result = (String) list.get(RandomUtil.initRandomIntBetween(0, list.size()));
        } else {
            return new StringInstanceGenerator().generateInstance(type);
        }
        try {
            Constructor<?> c = type.getConstructor(String.class);
            return c.newInstance(result);
        } catch (Exception e) {
            logger.warn("Could not construct Object of type " + type + " . Returning null.", e);
        }
        return null;
    }

    public Object generateInstance(Class<?> type, int modus) throws TypeNotSupportedException {
        return generateInstance(type);
    }

    protected Object getDefaultFromValue() {
        return null;
    }

    protected Object getDefaultUntilValue() {
        return null;
    }

    protected Object getFromValue() {
        return null;
    }

    protected Object getUntilValue() {
        return null;
    }

    protected List<?> getList() {
        return list;
    }
}
