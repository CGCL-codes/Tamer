package net.sf.appfw.common.hivemind;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.AbstractMessages;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.hivemind.util.LocalizedNameGenerator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import net.sf.appfw.common.util.XMLProperties;

/**
 * A kind of <code>AbstractMessages</code>, uses <code>XMLProperties</code>
 * to load property files of XML format.
 * <p>
 * <b>In Chinese:</b> ��HiveMind��<code>AbstractMessages</code>�����࣬ ������<code>XMLProperties</code>����XML�ļ���װ����Ϣ���塣
 * 
 * @see net.sf.appfw.common.util.XMLProperties
 * 
 * @author Zhengmao Hu
 */
public class XmlMessagesImpl extends AbstractMessages {

    private static final Log _log = LogFactory.getLog(XmlMessagesImpl.class);

    private Properties _properties;

    private Locale _locale;

    /**
	 * Construct a corresponding <code>Messages</code> of the specified class.
	 * "Msgs.xml" is appended to the class name when looking for the property
	 * file. For example, the base name of property file for class
	 * <code>com.test.Test</code> is <code>com/test/TestMsgs.xml</code>.
	 * <code>Locale.getDefault()</code> is referenced to decide the property
	 * file name.
	 * <p>
	 * <b>In Chinese:</b> ����һ����ָ������ͬ������Msgs.xmlΪ��׺����Դ���Ӧ��Messages��
	 * ����com.test.Test��Ӧ�ľ���com/test/TestMsgs.xml�� ʹ��Locale.getDefault()��
	 */
    public XmlMessagesImpl(Class claz) {
        this(claz, Locale.getDefault());
    }

    /**
	 * Construct a corresponding <code>Messages</code> of the specified class.
	 * "Msgs.xml" or ".xml" (depending on parameter <code>addMsgs</code>) is
	 * appended to the class name when looking for the property file. For
	 * example, the base name of property file for class
	 * <code>com.test.Test</code> may be <code>com/test/TestMsgs.xml</code>
	 * if <code>addMsgs</code> is true, or <code>com/test/Test.xml</code> if
	 * <code>addMsgs</code> is false. <code>Locale.getDefault()</code> is
	 * referenced to decide the property file name.
	 * <p>
	 * <b>In Chinese:</b> ����һ����ָ������ͬ������Msgs.xml��.xmlΪ��׺����Դ���Ӧ��Messages��
	 * ����com.test.Test��Ӧ�ľ���com/test/TestMsgs.xml������addMsgs����Ϊtrue��
	 * ��com/test/Test.xml������addMsgs����Ϊfalse���� ʹ��Locale.getDefault()��
	 * 
	 * @param claz
	 * @param addMsgs
	 *            if true, append "Msgs.xml"; if false append just ".xml"
	 */
    public XmlMessagesImpl(Class claz, boolean addMsgs) {
        this(claz, Locale.getDefault(), addMsgs);
    }

    /**
	 * Construct a corresponding <code>Messages</code> of the specified class.
	 * "Msgs.xml" is appended to the class name when looking for the property
	 * file. For example, the base name of property file for class
	 * <code>com.test.Test</code> is <code>com/test/TestMsgs.xml</code>.
	 * Specified Locale is referenced to decide the property file name.
	 * <p>
	 * <b>In Chinese:</b> ����һ����ָ������ͬ������Msgs.xmlΪ��׺����Դ���Ӧ��Messages��
	 * ����com.test.Test��Ӧ�ľ���com/test/TestMsgs.xml�� ʹ��ָ����Locale��
	 */
    public XmlMessagesImpl(Class claz, Locale locale) {
        this(claz, locale, true);
    }

    /**
	 * Construct a <code>Messages</code> from specified class path resource
	 * file. <code>Locale.getDefault()</code> is referenced to decide the file
	 * name.
	 * <p>
	 * <b>In Chinese:</b>����һ����ָ����classpath��Դ���Ӧ��Messages��
	 * ʹ��Locale.getDefault()��
	 * 
	 */
    public XmlMessagesImpl(String path) {
        this(path, Locale.getDefault());
    }

    /**
	 * Construct a corresponding <code>Messages</code> of the specified class.
	 * "Msgs.xml" of ".xml" (depending on parameter <code>addMsgs</code>) is
	 * appended to the class name when looking for the property file. For
	 * example, the base name of property file for class
	 * <code>com.test.Test</code> may be <code>com/test/TestMsgs.xml</code>
	 * if <code>addMsgs</code> is true, or <code>com/test/Test.xml</code> if
	 * <code>addMsgs</code> is false. Specified Locale is referenced to decide
	 * the property file name.
	 * <p>
	 * <b>In Chinese:</b> ����һ����ָ������ͬ������Msgs.xml��.xmlΪ��׺����Դ���Ӧ��Messages��
	 * ����com.test.Test��Ӧ�ľ���com/test/TestMsgs.xml������addMsgs����Ϊtrue��
	 * ��com/test/Test.xml������addMsgs����Ϊfalse���� ʹ��ָ����Locale��
	 * 
	 * @param claz
	 * @param locale
	 * @param addMsgs
	 *            if true, append "Msgs.xml"; if false append just ".xml"
	 */
    public XmlMessagesImpl(Class claz, Locale locale, boolean addMsgs) {
        ClassResolver resolver = new DefaultClassResolver();
        Resource location = new ClasspathResource(resolver, claz.getName().replace('.', '/') + (addMsgs ? "Msgs.xml" : ".xml"), locale);
        _locale = locale;
        initialize(location);
    }

    /**
	 * Construct a <code>Messages</code> from specified class path resource
	 * file. Specified Locale is referenced to decide the file name.
	 * <p>
	 * <b>In Chinese:</b>����һ����ָ����classpath��Դ���Ӧ��Messages�� ʹ��ָ����Locale��
	 */
    public XmlMessagesImpl(String path, Locale locale) {
        ClassResolver resolver = new DefaultClassResolver();
        Resource location = new ClasspathResource(resolver, path, locale);
        _locale = locale;
        initialize(location);
    }

    /**
	 * Construct a <code>Messages</code> from specified <code>Resource</code>
	 * Specified Locale is referenced to decide the file name.
	 * <p>
	 * <b>In Chinese:</b>����һ����ָ����<code>Resource</code>���Ӧ��Messages��
	 * ʹ��ָ����Locale��
	 */
    public XmlMessagesImpl(Resource location, Locale locale) {
        _locale = locale;
        initialize(location);
    }

    /**
	 * Load message key-value pair from each corresponding .properties/.xml
	 * files.
	 * <p>
	 * <b>In Chinese:</b>�����ÿһ��.properties/.xml�ļ���������Ϣ�ַ�
	 * 
	 * @param location
	 */
    private void initialize(Resource location) {
        if (_log.isDebugEnabled()) _log.debug("loading messages from location: " + location);
        String descriptorName = location.getName();
        int dotx = descriptorName.lastIndexOf('.');
        String baseName = descriptorName.substring(0, dotx);
        String suffix = descriptorName.substring(dotx + 1);
        LocalizedNameGenerator g = new LocalizedNameGenerator(baseName, _locale, "." + suffix);
        List urls = new ArrayList();
        while (g.more()) {
            String name = g.next();
            Resource l = location.getRelativeResource(name);
            URL url = l.getResourceURL();
            if (url != null) urls.add(url);
        }
        _properties = new XMLProperties();
        int count = urls.size();
        boolean loaded = false;
        for (int i = count - 1; i >= 0 && !loaded; i--) {
            URL url = (URL) urls.get(i);
            InputStream stream = null;
            try {
                stream = url.openStream();
                _properties.load(stream);
                loaded = true;
                if (_log.isDebugEnabled()) _log.debug("Messages loaded from URL: " + url);
            } catch (IOException ex) {
                if (_log.isDebugEnabled()) _log.debug("Unable to load messages from URL: " + url, ex);
            } finally {
                if (stream != null) try {
                    stream.close();
                } catch (IOException ioe) {
                }
            }
        }
        if (!loaded) {
            _log.error("Messages can not be loaded from location: " + location);
        }
    }

    /**
	 * Implemented abstract method of super class to find message string for
	 * specified key.
	 */
    protected String findMessage(String key) {
        return _properties.getProperty(key);
    }

    /**
	 * Implemented abstract method of super class to returen specified locale.
	 */
    protected Locale getLocale() {
        return _locale;
    }
}
