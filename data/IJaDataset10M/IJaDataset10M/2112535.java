package onepoint.xml;

import java.util.HashMap;

public interface XNodeHandler {

    public Object newNode(XContext context, String name, HashMap attributes);

    public void addNodeContent(XContext context, Object node, String content);

    public void addChildNode(XContext context, Object node, String child_name, Object child);

    public void nodeFinished(XContext context, String name, Object node, Object parent);
}
