package org.cantaloop.jiomask.testing.factory.javacode.classtemplate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import org.cantaloop.cgimlet.Options;
import org.cantaloop.cgimlet.PropertyParser;
import org.cantaloop.cgimlet.XMLUtils;
import org.cantaloop.cgimlet.lang.ClassTemplate;
import org.cantaloop.cgimlet.lang.CodeWriter;
import org.cantaloop.cgimlet.lang.java.JCompiler;
import org.cantaloop.cgimlet.lang.java.JLanguageFactory;
import org.cantaloop.jiomask.NamespaceConstants;
import org.cantaloop.jiomask.factory.javacode.Context;
import org.cantaloop.jiomask.factory.javacode.Generator;
import org.cantaloop.jiomask.factory.javacode.JavaBeanDescriptorFactory;
import org.cantaloop.jiomask.factory.javacode.JavaMetaNodeDescriptor;
import org.cantaloop.jiomask.factory.javacode.JavaMetaNodeDescriptorFactory;
import org.cantaloop.jiomask.factory.javacode.JavaMetaNodeDescriptorFactoryImpl;
import org.cantaloop.jiomask.factory.javacode.JavaXFormsNode;
import org.cantaloop.jiomask.factory.javacode.JavaXFormsNodeFactory;
import org.cantaloop.jiomask.factory.javacode.NameGenerator;
import org.cantaloop.jiomask.factory.javacode.NodeTypeDetector;
import org.cantaloop.jiomask.factory.javacode.OptionNames;
import org.cantaloop.jiomask.factory.javacode.Transformer;
import org.cantaloop.jiomask.factory.schema.SchemaContext;
import org.cantaloop.jiomask.factory.xforms.BindingContext;
import org.cantaloop.jiomask.factory.xforms.XFormsModelBuilder;
import org.cantaloop.jiomask.factory.xforms.XFormsNode;
import org.cantaloop.jiomask.testing.factory.javacode.NameGeneratorUTest;
import org.cantaloop.tools.logging.Logger;
import org.cantaloop.tools.logging.LoggingManager;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.QName;

public class MetaNodeClassTemplateGenerationUTest extends AbstractClassTemplateTest {

    public MetaNodeClassTemplateGenerationUTest(String name) throws IOException {
        super(name);
    }

    public void testMetaNodeClassesExist() {
        buildClassTemplates();
        List tmpl = m_metaNodeClassTemplates;
        for (Iterator it = tmpl.iterator(); it.hasNext(); ) {
            ClassTemplate ct = (ClassTemplate) it.next();
            System.out.println(" - " + ct.getName());
        }
        assertClassExists(tmpl, "ArticleSerialIdsIdMetaNode");
        assertClassExists(tmpl, "ArticleSerialIdsMetaNode");
        assertClassExists(tmpl, "BillingAddressCityMetaNode");
        assertClassExists(tmpl, "BillingAddressMetaNode");
        assertClassExists(tmpl, "BillingAddressStreetMetaNode");
        assertClassExists(tmpl, "CustomerIdMetaNode");
        assertClassExists(tmpl, "CustomerMetaNode");
        assertClassExists(tmpl, "DeliveredItemsItemIdMetaNode");
        assertClassExists(tmpl, "DeliveredItemsItemMetaNode");
        assertClassExists(tmpl, "DeliveredItemsItemPriceMetaNode");
        assertClassExists(tmpl, "DeliveredItemsMetaNode");
        assertClassExists(tmpl, "ExpectedDeliveryDateMetaNode");
        assertClassExists(tmpl, "InvoiceIdMetaNode");
        assertClassExists(tmpl, "InvoiceMetaNode");
        assertClassExists(tmpl, "MissingItemsItemIdMetaNode");
        assertClassExists(tmpl, "MissingItemsItemMetaNode");
        assertClassExists(tmpl, "MissingItemsItemPriceMetaNode");
        assertClassExists(tmpl, "MissingItemsMetaNode");
        assertClassExists(tmpl, "NameMetaNode");
        assertClassExists(tmpl, "SelectedItemMetaNode");
        assertClassExists(tmpl, "ShipmentAddressCityMetaNode");
        assertClassExists(tmpl, "ShipmentAddressMetaNode");
        assertClassExists(tmpl, "ShipmentAddressStreetMetaNode");
        assertClassExists(tmpl, "SumMetaNode");
        assertClassExists(tmpl, "UseShipmentAddressForBillMetaNode");
        assertClassExists(tmpl, "ZipcodeMetaNode");
        assertEquals(26, tmpl.size());
    }

    public void testMetaNodeCodeGeneration() throws java.io.IOException {
        buildClassTemplates();
        writeCode(m_metaNodeClassTemplates);
    }

    public void testCompileMetaNodeCode() throws IOException {
        buildClassTemplates();
        List metaNodeFiles = writeCode(m_metaNodeClassTemplates);
        List javaBeanFiles = writeCode(m_javaBeanClassTemplates);
        List allFiles = new ArrayList();
        allFiles.addAll(metaNodeFiles);
        allFiles.addAll(javaBeanFiles);
        compile(allFiles);
    }
}
