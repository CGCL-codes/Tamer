package agorum.blender.yadra.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

public abstract class BaseServlet extends HttpServlet {

    /**
	 * replace all occurences of srch and replace it with repl
	 */
    protected String replace(String s, String srch, String repl) throws Exception {
        return s.replaceAll("\\$\\{" + srch + "\\}", repl);
    }

    /**
	 * get the blend file in the actual folder
	 */
    public File getBlendFile(File path) throws Exception {
        File[] files = path.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().toLowerCase().endsWith(".blend")) {
                    return files[i];
                }
            }
        }
        return null;
    }

    /**
	 * get a displayname of the given blend file (just cut away the number at the
	 * beginnig)
	 * 
	 * @param fName
	 *          the filename
	 * @return the displayable file name
	 */
    public String getDisplayName(String fName) {
        String s = fName;
        int pos = fName.indexOf("_");
        if (pos != -1) {
            s = fName.substring(pos + 1);
        }
        return s;
    }

    /**
	 * get the blend properties file in the actual folder
	 */
    public Properties getBlendPropFile(File path) throws Exception {
        File[] files = path.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().toLowerCase().endsWith(".blend.properties")) {
                    Properties properties = new Properties();
                    FileInputStream propFIS = new FileInputStream(files[i]);
                    properties.load(propFIS);
                    propFIS.close();
                    return properties;
                }
            }
        }
        return null;
    }

    /**
	 * read the given template as string
	 */
    protected String readTemplate(String path) throws Exception {
        StringBuffer sb = new StringBuffer();
        FileInputStream fis = new FileInputStream(path);
        BufferedReader bReader = new BufferedReader(new InputStreamReader(fis));
        String line = null;
        while ((line = bReader.readLine()) != null) {
            sb.append(line + "\n");
        }
        fis.close();
        return sb.toString();
    }

    /**
	 * gets the subdirectory out of the request url
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
    protected String getUrlPath(HttpServletRequest request) throws Exception {
        String contextPath = request.getContextPath();
        String url = request.getRequestURL().toString();
        url = url.substring(url.indexOf(contextPath));
        url = url.substring(url.indexOf("/", 1), url.lastIndexOf("/"));
        return url;
    }
}
