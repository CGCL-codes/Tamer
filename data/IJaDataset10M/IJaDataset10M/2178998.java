package net.sf.xpontus.plugins.lexer.html;

import net.sf.xpontus.configuration.XPontusConfig;
import net.sf.xpontus.constants.XPontusConfigurationConstantsIF;
import net.sf.xpontus.utils.ColorUtils;
import net.sf.xpontus.utils.PropertiesConfigurationLoader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.java.plugin.Plugin;
import java.awt.Color;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Properties;

/**
 *
 * @author Yves Zoundi
 */
public class HTMLLexerPlugin extends Plugin {

    static File configfile = null;

    String packageName = getClass().getPackage().getName();

    File confdir = new File(XPontusConfigurationConstantsIF.XPONTUS_PREFERENCES_DIR, packageName);

    public HTMLLexerPlugin() {
    }

    protected void doStart() throws Exception {
        try {
            if (!confdir.exists()) {
                confdir.mkdirs();
            }
            configfile = new File(confdir, "settings.properties");
            Properties props = new Properties();
            if (!configfile.exists()) {
                OutputStream bos = FileUtils.openOutputStream(configfile);
                props.put(HMLLexerPreferencesConstantsIF.class.getName() + "$" + HMLLexerPreferencesConstantsIF.STRING_PROPERTY, ColorUtils.colorToString(Color.red));
                props.put(HMLLexerPreferencesConstantsIF.class.getName() + "$" + HMLLexerPreferencesConstantsIF.ATTRIBUTE_PROPERTY, ColorUtils.colorToString(Color.RED));
                props.put(HMLLexerPreferencesConstantsIF.class.getName() + "$" + HMLLexerPreferencesConstantsIF.COMMENT_PROPERTY, ColorUtils.colorToString(new Color(0, 139, 0)));
                props.put(HMLLexerPreferencesConstantsIF.class.getName() + "$" + HMLLexerPreferencesConstantsIF.TAGS_PROPERTY, ColorUtils.colorToString(Color.BLUE));
                props.put(HMLLexerPreferencesConstantsIF.class.getName() + "$" + HMLLexerPreferencesConstantsIF.DECLARATION_PROPERTY, ColorUtils.colorToString(Color.MAGENTA));
                props.put(HMLLexerPreferencesConstantsIF.class.getName() + "$" + HMLLexerPreferencesConstantsIF.ATTRIBUTES_PROPERTY, ColorUtils.colorToString(new Color(127, 0, 85)));
                PropertiesConfigurationLoader.save(configfile, props);
                Iterator it = props.keySet().iterator();
                while (it.hasNext()) {
                    Object m_key = it.next();
                    String m_value = (String) props.get(m_key);
                    Color c = ColorUtils.stringToColor(m_value);
                    XPontusConfig.put(m_key, c);
                }
            } else {
                InputStream fis = FileUtils.openInputStream(configfile);
                props.load(fis);
                IOUtils.closeQuietly(fis);
                Iterator it = props.keySet().iterator();
                while (it.hasNext()) {
                    Object m_key = it.next();
                    String m_value = (String) props.get(m_key);
                    Color c = ColorUtils.stringToColor(m_value);
                    XPontusConfig.put(m_key, c);
                }
            }
        } catch (Exception err) {
            log.error("Error loading HTML lexer settings");
            log.error(err.getMessage());
        }
    }

    protected void doStop() throws Exception {
    }
}
