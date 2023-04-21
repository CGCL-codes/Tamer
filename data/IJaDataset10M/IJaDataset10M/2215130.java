package koppa.widget.box;

import java.util.List;
import koppa.builder.Builder;
import koppa.builder.internal.BuilderFactory;
import koppa.builder.internal.BuilderMapppingException;
import koppa.internal.util.XulHelper;
import koppa.widget.MetaData;
import koppa.widget.Widget;
import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Element;
import com.google.inject.Inject;

public class BoxBuilder implements Builder {

    private static Logger log = Logger.getLogger(BoxBuilder.class);

    private Box box;

    private BuilderFactory builderFactory;

    @Inject
    public final void setBox(final Box box) {
        this.box = box;
    }

    @Inject
    public final void setBuilderFactory(final BuilderFactory builderFactory) {
        this.builderFactory = builderFactory;
    }

    @SuppressWarnings("unchecked")
    public final Widget build(final Element xulNode) {
        List<Attribute> attributes = xulNode.attributes();
        populateMetaData(attributes);
        String parentId = box.getMetaData().getId();
        List<Element> children = xulNode.elements();
        for (Element child : children) {
            String widgetName = child.getName();
            try {
                Builder childBuilder = builderFactory.getBuilder(widgetName);
                Widget childWidget = childBuilder.build(child);
                childWidget.getMetaData().setParent(parentId);
                box.addChild(childWidget);
            } catch (BuilderMapppingException e) {
                log.warn(e.getMessage(), e);
                continue;
            }
        }
        return box;
    }

    private void populateMetaData(final List<Attribute> attributes) {
        MetaData metaData = box.getMetaData();
        XulHelper.populate(metaData, attributes);
    }
}
