package pl.n3fr0.n3talk.mirror;

import org.w3c.dom.Element;
import pl.n3fr0.n3talk.message.XML;
import pl.n3fr0.n3talk.mirror.entity.MirrorListModificator;

public class MirrorListRebuilder implements MirrorListModificator, XML {

    public MirrorListRebuilder(Element element) {
        this.element = element;
    }

    public Kind getKind() {
        for (Kind k : Kind.values()) {
            if (k.toString().equals(element.getAttribute(ATT_TYPE))) {
                return k;
            }
        }
        throw new UnsupportedOperationException("I don't know KIND!");
    }

    public String getObjectUID() {
        return element.getAttribute(ATT_OBJECT_UID);
    }

    public String getName() {
        return element.getAttribute(ATT_NAME);
    }

    public Object getObject() {
        Object o = null;
        if (container != null) {
            final Element obj = (Element) element.getFirstChild();
            o = container.objects.get(obj.getAttribute(ATT_OBJECT_UID));
        }
        return o;
    }

    public MirrorContainer getContainer() {
        return container;
    }

    public void setContainer(MirrorContainer container) {
        this.container = container;
    }

    final Element element;

    MirrorContainer container;
}
