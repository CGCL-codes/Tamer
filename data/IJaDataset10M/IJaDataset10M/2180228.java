package neon.tools.resources;

import java.util.ArrayList;
import neon.objects.ResourceManager;
import neon.objects.resources.RData;
import neon.objects.resources.RItem;
import org.jdom2.Element;

public class IContainer extends IObject {

    public ArrayList<IObject> contents = new ArrayList<IObject>();

    public IContainer(RData resource, int x, int y, int z, int uid) {
        super(resource, x, y, z, uid);
    }

    public IContainer(Element properties) {
        super(properties);
        for (Element e : properties.getChildren("item")) {
            RItem ri = (RItem) ResourceManager.getResource(e.getAttributeValue("id"));
            contents.add(new IObject(ri, 0, 0, 0, Integer.parseInt(e.getAttributeValue("uid"))));
        }
    }

    public Element toElement() {
        Element container = super.toElement();
        container.setName("container");
        for (IObject io : contents) {
            Element item = new Element("item");
            item.setAttribute("id", io.resource.id);
            item.setAttribute("uid", Integer.toString(io.uid));
            container.addContent(item);
        }
        return container;
    }
}
