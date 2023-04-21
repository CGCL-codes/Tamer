package lv.webkursi.web2007a.sem05.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.web.context.WebApplicationContext;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileUtils;

/**
 * Create a Jena model given a path to an RDF file
 * (either a file path, or a path relative to 
 * the Web application root). 
 * 
 * <p>
 * The descriptions may identify different kinds of Jena models, such as models
 * held in files etc using an appropriate RDF type property.
 * </p>
 * 
 * @author kap
 * 
 */
public class FileModelFactory implements FactoryBean {

    /**
	 * Either "N3" or "RDF/XML" (case is significant). The default is null - if
	 * <code>fileEncoding</code> is not set, then encoding is guessed by some
	 * Jena utility.
	 */
    private String fileEncoding;

    private String filePath;

    protected WebApplicationContext applicationContext;

    /**
	 * Create a Jena model froma file model description.
	 * 
	 * <p>
	 * <b>Contract</b>
	 * </p>
	 * 
	 * <ol>
	 * <li>The <code>portal:File</code> identifies the file from which the
	 * model's graph is loaded.</li>
	 * <li>If the <code>portal:File</code> property value is a resource with
	 * a file: url, then that URL defines the file to be loaded.</li>
	 * <li>If the <code>portal:File<code> property value is a resource with
	 *   portal: url, then the path of the URL is a path, beginning with a "/"
	 *   character, relative to the base directory of the web application in 
	 *   which the portal is running.</li>
	 *   <li>If any other kind of url is used then a PortalException is thrown.</li>
	 *   <li>If the resource has a <code>portal:contentEncoding</code> property
	 *   the value of this property is assumed to define the content format for
	 *   the file.  Recognised values are "N3" and "RDF/XML".</li>
	 *   <li>If there is no <code>portal:contentEncoding</code> property then
	 *    an appropriate guess as to content will be made.  For example
	 *    a file whose URL ends in ".n3" will be assumed to be in N3 format.</li>
	 *   <li>If the ending of the URL is not recognised, then the content is
	 *   assumed to be in RDF/XML.</li>
	 * </ol>
	 * 
	 * @param resource
	 * @return
	 */
    public Object getObject() throws Exception {
        File file;
        try {
            file = resolveFileURL(filePath);
        } catch (MalformedURLException e) {
            throw new RuntimeException("bad URL for Jena model: " + filePath);
        }
        if (fileEncoding == null) {
            fileEncoding = FileUtils.guessLang(file.getPath());
        }
        Model m = ModelFactory.createDefaultModel();
        try {
            m.read(new FileInputStream(file), "", fileEncoding);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Cannot read RDF file model: " + file.getAbsolutePath(), e);
        }
        return m;
    }

    public Class getObjectType() {
        return Model.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public String getFileEncoding() {
        return fileEncoding;
    }

    public void setFileEncoding(String fileEncoding) {
        this.fileEncoding = fileEncoding;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public WebApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(WebApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public File resolveFileURL(String stringUrl) throws MalformedURLException {
        if (stringUrl.startsWith("file:")) {
            return fileURL(stringUrl);
        } else if (stringUrl.startsWith("portal:")) {
            return portalURL(stringUrl);
        } else {
            throw new RuntimeException("bad prefix on URL: " + stringUrl);
        }
    }

    protected File fileURL(String stringUrl) throws MalformedURLException {
        URL url = new URL(stringUrl);
        return new File(url.getFile());
    }

    protected File portalURL(String stringUrl) {
        String path = stringUrl.substring(7);
        String contextPath = applicationContext.getServletContext().getRealPath("");
        File file = new File(contextPath + path);
        return file;
    }
}
