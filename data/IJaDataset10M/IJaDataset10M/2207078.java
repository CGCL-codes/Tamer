package lamao.soh.utils.deled;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import lamao.soh.utils.xmlparser.SHDocXMLParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Abstract class for data loading from DeleD *.dps files.
 * @author lamao
 *
 */
public abstract class SHDpsLoader {

    private static Logger logger = Logger.getLogger(SHDpsToJme.class.getName());

    /** Storage for materials, loaded from file */
    private Map<String, SHMaterialGroup> _materials = new HashMap<String, SHMaterialGroup>();

    /** Name of file from which scene is loaded. Used for accessing to 
	 * textures
	 */
    private String _fileName = null;

    protected String getFileName() {
        return _fileName;
    }

    protected Map<String, SHMaterialGroup> getMaterials() {
        return _materials;
    }

    /**
	 * Loads scene from *.dps file
	 * @param file
	 * @return Loaded scene. Null if loading failed.
	 */
    public void load(File file) {
        try {
            _fileName = file.getAbsolutePath();
            ZipFile zip = new ZipFile(file);
            ZipEntry entry = zip.getEntry("Map Files/scene.dxs");
            loadScene(zip.getInputStream(entry));
        } catch (IOException e) {
            logger.severe("Can't open file: " + file.getName());
            resetLoader();
        }
    }

    /**
	 * Load scene from input stream (unzipped *.dxs file)
	 * @param is
	 * @return Loaded scene. Null if loading failed.
	 */
    private void loadScene(InputStream is) {
        resetLoader();
        SHDocXMLParser parser = buildSceneParser();
        Node docRoot = getDocumentRoot(is);
        parser.removeWhitespaces(docRoot);
        parser.parse(docRoot);
    }

    /**
	 * Resets all internal variables to default
	 */
    protected void resetLoader() {
        _materials.clear();
    }

    /**
	 * Builds XML parser for scene.
	 * @return
	 */
    protected abstract SHDocXMLParser buildSceneParser();

    /**
	 * Parses XML and returns root element of the document.
	 * @param is
	 * @return
	 */
    private org.w3c.dom.Node getDocumentRoot(InputStream is) {
        org.w3c.dom.Node result = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(true);
            Document doc = factory.newDocumentBuilder().parse(is);
            result = doc.getDocumentElement();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return result;
    }
}
