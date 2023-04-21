package client.game.state.creation.xmlParser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Element;
import client.game.Game;
import com.jme.bounding.BoundingBox;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.util.export.binary.BinaryImporter;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;
import com.jmex.model.converters.AseToJme;
import com.jmex.model.converters.FormatConverter;
import com.jmex.model.converters.MaxToJme;
import com.jmex.model.converters.Md2ToJme;
import com.jmex.model.converters.Md3ToJme;
import com.jmex.model.converters.MilkToJme;
import com.jmex.model.converters.ObjToJme;
import com.jmex.model.util.ModelLoader;

public class MultiNodeXMLTagParser extends XMLTagParser {

    private static final String id = "multiNode";

    public MultiNodeXMLTagParser() {
        super(id);
    }

    @Override
    public Object processElement(Element tag, Object parent) {
        Attribute modelPath = tag.getAttribute("modelsPath");
        Attribute modelName = tag.getAttribute("modelsName");
        Attribute nodeName = tag.getAttribute("nodeNames");
        Attribute cantParts = tag.getAttribute("cant");
        Attribute extension = tag.getAttribute("extension");
        if ((modelName != null) && (modelPath != null)) {
            try {
                SimpleResourceLocator srl = new SimpleResourceLocator(Game.class.getClassLoader().getResource(modelPath.getValue()));
                ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, srl);
                XMLTagParserManager.getInstance().getResourceslocators().add(srl);
                int cant = 0;
                if (cantParts != null) {
                    cant = cantParts.getIntValue();
                }
                for (int k = 1; k <= cant; k++) {
                    Node hijo;
                    String ext = "3ds";
                    if (extension != null) {
                        ext = extension.getValue();
                    }
                    hijo = cargarModelo(modelPath.getValue() + modelName.getValue() + k + "." + ext);
                    if (nodeName != null) hijo.setName(nodeName.getValue() + k); else hijo.setName("Hijo" + k);
                    Quaternion q = hijo.getLocalRotation();
                    q = q.fromAngleAxis((float) -Math.PI / 2, new Vector3f(1, 0, 0));
                    hijo.setLocalRotation(q);
                    ((Node) parent).attachChild(hijo);
                }
            } catch (DataConversionException e) {
                log.log(Level.SEVERE, "El Tag \"" + id + "\" fallo porque cant no tiene un valor entero");
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
                log.log(Level.SEVERE, "El Tag \"" + id + "\" fallo porque no encuntra las texturas");
            }
        } else log.log(Level.SEVERE, "El Tag \"" + id + "\" no tiene alguno de los atributos modelsPath o modelName");
        return null;
    }

    public Node cargarModelo(String modelFile) {
        Node loadedModel = null;
        FormatConverter formatConverter = null;
        ByteArrayOutputStream BO = new ByteArrayOutputStream();
        String modelFormat = modelFile.substring(modelFile.lastIndexOf(".") + 1, modelFile.length());
        String modelBinary = modelFile.substring(0, modelFile.lastIndexOf(".") + 1) + "jbin";
        URL modelURL = ModelLoader.class.getClassLoader().getResource(modelBinary);
        if (modelURL == null) {
            modelURL = ModelLoader.class.getClassLoader().getResource(modelFile);
            if (modelFormat.equals("3ds")) {
                formatConverter = new MaxToJme();
            } else if (modelFormat.equals("md2")) {
                formatConverter = new Md2ToJme();
            } else if (modelFormat.equals("md3")) {
                formatConverter = new Md3ToJme();
            } else if (modelFormat.equals("ms3d")) {
                formatConverter = new MilkToJme();
            } else if (modelFormat.equals("ase")) {
                formatConverter = new AseToJme();
            } else if (modelFormat.equals("obj")) {
                formatConverter = new ObjToJme();
            }
            formatConverter.setProperty("mtllib", modelURL);
            try {
                formatConverter.convert(modelURL.openStream(), BO);
                loadedModel = (Node) BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            try {
                loadedModel = (Node) BinaryImporter.getInstance().load(modelURL.openStream());
            } catch (IOException e) {
                return null;
            }
        }
        loadedModel.setModelBound(new BoundingBox());
        loadedModel.updateModelBound();
        return loadedModel;
    }
}
