package rotorsim.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import rotorsim.main.Connector;
import util.GLUtils;
import util.Matrix4f;
import util.Vector3f;
import util.Vector4f;

/**
 * @author Shannon Smith
 */
public class ArticulatedModel {

    private final Map<String, ModelSection> sections;

    private Model model;

    private ModelSection rootSection;

    public ArticulatedModel(Model model) {
        this.model = model;
        sections = new HashMap<String, ModelSection>();
    }

    public Model getModel() {
        return model;
    }

    private void transformToSection(String name, ModelState state) {
        LinkedList selectionPath = getSelectionPath(name);
        while (!selectionPath.isEmpty()) {
            ModelSection curSection = (ModelSection) selectionPath.removeLast();
            curSection.getConnector().transformGL((float) (state.getAngle(curSection.getName()) * 180.0f / Math.PI));
        }
    }

    public Connector getTransformedConnector(String name, ModelState state) {
        Connector con = getSection(name).getConnector();
        transformToSection(name, state);
        Matrix4f m = GLUtils.getModelViewMatrix();
        Vector4f pos = new Vector4f(con.getPosition().x, con.getPosition().y, con.getPosition().z, 1);
        Vector4f axis = new Vector4f(con.getAxis().x + pos.x, con.getAxis().y + pos.y, con.getAxis().z + pos.z, 1);
        m.transform(pos);
        m.transform(axis);
        Vector3f scaledAxis = new Vector3f(axis.x - pos.x, axis.y - pos.y, axis.z - pos.z);
        scaledAxis.normalise();
        Connector c = new Connector(new Vector3f(pos.x, pos.y, pos.z), scaledAxis);
        return c;
    }

    public LinkedList getSelectionPath(String name) {
        LinkedList<ModelSection> sectionPath = new LinkedList<ModelSection>();
        ModelSection section = getSection(name);
        while (section.getParent() != null) {
            sectionPath.addLast(section);
            section = section.getParent();
        }
        sectionPath.addLast(section);
        return sectionPath;
    }

    public Collection<ModelSection> getSections() {
        return sections.values();
    }

    public void addSection(ModelSection section, ModelSection parent) {
        if (parent == null) {
            rootSection = section;
        } else {
            parent.addChild(section);
        }
        sections.put(section.getName(), section);
    }

    public ModelSection getSection(String name) {
        if (!sections.containsKey(name)) {
            System.out.println("No such model section: " + name);
            return null;
        } else {
            return sections.get(name);
        }
    }

    public ModelSection getRootSection() {
        return rootSection;
    }

    public void draw(ModelState state) {
        if (rootSection != null) {
            rootSection.draw(state);
        }
    }
}
