package demo.com.handcoded.fpml;

import java.io.File;
import java.io.FileFilter;
import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Logger;
import org.xml.sax.SAXException;
import com.handcoded.fpml.Releases;
import com.handcoded.framework.Option;
import com.handcoded.meta.Release;
import com.handcoded.meta.SchemaRelease;
import com.handcoded.xml.XmlUtility;
import com.handcoded.xml.resolver.Catalog;
import com.handcoded.xml.resolver.CatalogManager;

/**
 * The <CODE>Application</CODE> contains some standard option handling
 * that is common to all FpML demonstration applications.
 *
 * @author	BitWise
 * @version	$Id: Application.java 624 2012-04-11 10:57:52Z andrew_jacobs $
 * @since	TFP 1.0
 */
public abstract class Application extends com.handcoded.framework.Application {

    /**
	 * Constructs a <CODE>Application</CODE> instance.
	 * @since	TFP 1.0
	 */
    protected Application() {
    }

    /**
	 * {@inheritDoc}
	 * @since	TFP 1.0
	 */
    @Override
    protected void startUp() {
        super.startUp();
        String catalogPath = "files-fpml/catalog-fpml-5-3.xml";
        if (catalogOption.isPresent()) {
            if (catalogOption.getValue() != null) catalogPath = catalogOption.getValue(); else logger.severe("Missing argument for -catalog option");
        }
        try {
            XmlUtility.setDefaultCatalog(CatalogManager.find(catalogPath));
        } catch (SAXException error) {
            logger.severe("Failed to parse XML catalog");
            System.exit(1);
        }
        Enumeration<Release> releases = Releases.FPML.releases();
        while (releases.hasMoreElements()) {
            Release release = releases.nextElement();
            if (release instanceof SchemaRelease) XmlUtility.getDefaultSchemaSet().add((SchemaRelease) release);
        }
    }

    /**
	 * Provides access to the <CODE>Catalog</CODE> instance to be used for
	 * entity resolution. If the <CODE>-catalog</CODE> option was not specified
	 * then the result will be <CODE>null</CODE>
	 *
	 * @return	The <CODE>Catalog</CODE> instance or <CODE>null</CODE>.
	 * @since	TFP 1.0
	 */
    protected final Catalog getCatalog() {
        return (catalog);
    }

    /**
	 * Process the paths given on the command line trying to expand out
	 * any that refer to directories.
	 * 
	 * @param 	arguments			File paths from the command line.
	 * @return	An array of XML file paths.
	 * @since	TFP 1.2
	 */
    protected String[] findFiles(String[] arguments) {
        Vector<String> result = new Vector<String>();
        for (int index = 0; index < arguments.length; ++index) addFile(result, new File(arguments[index]));
        String[] paths = new String[result.size()];
        result.copyInto(paths);
        return (paths);
    }

    /**
	 * Logging instance used for error reporting.
	 * @since	TFP 1.0
	 */
    private static Logger logger = Logger.getLogger("demo.com.handcoded.fpml");

    /**
	 * A command line option that allows the default catalog to be overridden.
	 * @since	TFP 1.0
	 */
    private Option catalogOption = new Option("-catalog", "Use url to create an XML catalog for parsing", "url");

    /**
	 * The <CODE>Catalog</CODE> instance if specified by the options.
	 * @since	TFP 1.0
	 */
    private Catalog catalog = null;

    /**
	 * If the <CODE>File</CODE> argument is a file then just add it to the list
	 * of paths to be processed. If its a directory recursive look inside for
	 * more XML files and directories.
	 * 
	 * @param 	paths			A <CODE>Vector</CODE> of paths found so far.
	 * @param 	file			The <CODE>File</CODE> under consideration.
	 * @since	TFP 1.2
	 */
    private void addFile(Vector<String> paths, File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles(new FileFilter() {

                public boolean accept(File file) {
                    return (file.isDirectory() || (file.isFile() && file.getName().endsWith(".xml")));
                }
            });
            for (int index = 0; index < files.length; ++index) addFile(paths, files[index]);
        } else paths.add(file.getPath());
    }
}
