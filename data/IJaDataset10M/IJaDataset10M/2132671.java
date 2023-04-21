package fw4ex_client.exercise.retrieve;

import java.io.InputStream;
import java.util.ResourceBundle;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import fw4ex_client.Activator;
import fw4ex_client.data.ExercisePathItemRoot;
import fw4ex_client.data.interfaces.IExercisePathItem;

public class ExerciseListParser {

    public static String EXERCISE_PATH_ATTR_NAME = "location";

    public static String EXERCISE_ID_ATTR_NAME = "exerciseid";

    private IExercisePathItem list;

    public ExerciseListParser() {
        this.list = new ExercisePathItemRoot();
    }

    public IExercisePathItem parse(InputStream inputStream) {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(inputStream);
            Element e = doc.getDocumentElement();
            e.normalize();
            NodeList nlist = e.getChildNodes();
            for (int i = 0; i < nlist.getLength(); i++) {
                Node current = nlist.item(i);
                if (current.getNodeType() == Node.ELEMENT_NODE) {
                    Element el = (Element) current;
                    String name = current.getNodeName();
                    if (name.equals("exercisesPath")) {
                        list = ExercisePathItemRoot.parse(el);
                        if (list.size() == 1) {
                            return list.get(0);
                        }
                        return list;
                    }
                }
            }
        } catch (Exception err) {
            ResourceBundle bundle = Activator.getDefault().getResourceBundle();
            Activator.getDefault().showMessage(bundle.getString("Unhandled_Error") + " : " + err.getMessage());
        }
        return null;
    }

    public IExercisePathItem getList() {
        return list;
    }
}
