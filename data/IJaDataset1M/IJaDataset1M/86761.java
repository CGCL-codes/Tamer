package com.bluebrim.xml.shared;

import java.util.*;
import org.w3c.dom.*;

public class CoCollectionModelBuilder extends CoSimpleModelBuilder {

    public CoCollectionModelBuilder(CoXmlParserIF parser) {
        super(parser);
    }

    /**
 * addSubModel method comment.
 */
    public void addSubModel(String name, Object model, CoXmlContext context) {
        ((Collection) m_model).add(model);
    }

    public void createModel(Object superModel, Node node, CoXmlContext context) {
        m_model = new ArrayList();
    }
}
