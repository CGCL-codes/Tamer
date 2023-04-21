package werkzeugkasten.weblauncher.tomcat.startup;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.catalina.core.StandardContext;

public class WebLauncherContext extends StandardContext {

    private static final long serialVersionUID = 1L;

    @Override
    public void setDocBase(String docBase) {
        super.setDocBase(resolveString(docBase, System.getProperties()));
    }

    public static String resolveString(String string, Properties context) {
        String result = "";
        if (WebLauncherHostConfig.isEmpty(string) == false) {
            Pattern p = Pattern.compile("\\$\\{[^\\$\\{\\}]*\\}");
            StringBuffer stb = new StringBuffer(string);
            Matcher m = p.matcher(stb);
            int index = 0;
            while (index < stb.length() && m.find(index)) {
                String s = m.group();
                String v = context.getProperty(s.substring(2, s.length() - 1));
                index = m.start() + v.length();
                stb.replace(m.start(), m.end(), v);
                m = p.matcher(stb);
            }
            result = stb.toString();
        }
        return result;
    }
}
