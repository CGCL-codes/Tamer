package gr.gousios.ereceipt.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

public abstract class ModelObject {

    private static List<Class<?>> modelObjects;

    static {
        modelObjects = new ArrayList<Class<?>>();
    }

    public static void addModelObject(Class<?> clazz) {
        modelObjects.add(clazz);
    }

    public static Class<?>[] getModelObjects() {
        return modelObjects.toArray(new Class[modelObjects.size()]);
    }

    public abstract String toJSON(EntityManager em);

    public String toXML(EntityManager em) {
        JSONObject json = JSONObject.fromObject(this.toJSON(em));
        String xml = new XMLSerializer().write(json);
        return xml;
    }
}
