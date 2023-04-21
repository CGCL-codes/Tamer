package org.dbe.composer.wfengine.util;

import java.io.CharArrayWriter;
import java.io.StringReader;
import java.util.Enumeration;
import org.apache.log4j.Logger;
import org.dbe.composer.wfengine.xml.XMLParserBase;
import org.exolab.castor.xml.Namespaces;
import org.exolab.castor.xml.schema.AttributeGroupDecl;
import org.exolab.castor.xml.schema.ComplexType;
import org.exolab.castor.xml.schema.ElementDecl;
import org.exolab.castor.xml.schema.ModelGroup;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.SchemaException;
import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.castor.xml.schema.writer.SchemaWriter;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * A utility class that contains static methods used to manipulate XML schemas.
 */
public class SdlSchemaUtil {

    private static final Logger logger = Logger.getLogger(SdlSchemaUtil.class.getName());

    /**
     * This method merges all of the schema types, elements, namespaces, attribute
     * groups, from the two passed in schemas into a single schema.
     *
     * todo Alternatively, change the wsdl def object to hold a list of schemas rather than one schema per target namespace.
     * @param aSchema1
     * @param aSchema2
     */
    public static Schema mergeSchemas(Schema aSchema1, Schema aSchema2) throws SchemaException {
        logger.debug("mergeSchemas()");
        if (!SdlUtil.compareObjects(aSchema1.getTargetNamespace(), aSchema2.getTargetNamespace())) {
            logger.error("error while merging schemas");
            throw new SchemaException("Error merging schemas - target namespaces are different.");
        }
        String targetNS = aSchema2.getTargetNamespace();
        Enumeration enums = aSchema2.getComplexTypes();
        while (enums.hasMoreElements()) {
            ComplexType ct = (ComplexType) enums.nextElement();
            ct.setSchema(aSchema1);
            if (aSchema1.getComplexType(ct.getName()) == null) {
                aSchema1.addComplexType(ct);
            }
        }
        enums = aSchema2.getElementDecls();
        while (enums.hasMoreElements()) {
            ElementDecl ed = (ElementDecl) enums.nextElement();
            if (aSchema1.getElementDecl(ed.getName()) == null) {
                aSchema1.addElementDecl(ed);
            }
        }
        enums = aSchema2.getSimpleTypes();
        while (enums.hasMoreElements()) {
            SimpleType st = (SimpleType) enums.nextElement();
            st.setSchema(aSchema1);
            if (aSchema1.getSimpleType(st.getName(), targetNS) == null) {
                aSchema1.addSimpleType(st);
            }
        }
        enums = aSchema2.getImportedSchema();
        while (enums.hasMoreElements()) {
            Schema importedSchema = (Schema) enums.nextElement();
            if (aSchema1.getImportedSchema(importedSchema.getTargetNamespace()) == null) {
                aSchema1.addImportedSchema(importedSchema);
            }
        }
        enums = aSchema2.getAttributeGroups();
        while (enums.hasMoreElements()) {
            AttributeGroupDecl ag = (AttributeGroupDecl) enums.nextElement();
            if (aSchema1.getAttributeGroup(ag.getName()) == null) {
                aSchema1.addAttributeGroup(ag);
            }
        }
        Namespaces namespaces = aSchema2.getNamespaces();
        enums = namespaces.getLocalNamespacePrefixes();
        while (enums.hasMoreElements()) {
            String prefix = (String) enums.nextElement();
            String ns = namespaces.getNamespaceURI(prefix);
            if (aSchema1.getNamespace(prefix) == null) {
                aSchema1.addNamespace(prefix, ns);
            }
        }
        enums = aSchema2.getModelGroups();
        while (enums.hasMoreElements()) {
            ModelGroup mg = (ModelGroup) enums.nextElement();
            if (aSchema1.getModelGroup(mg.getName()) == null) {
                aSchema1.addModelGroup(mg);
            }
        }
        return aSchema1;
    }

    /**
     * Given a schema, returns a String which is the schema serialized as a string.  This
     * method is primarily a debugging tool - it should be used as the detail formatter
     * for a Schema object in Eclipse.  That is the reason for the extra formatting flag:
     * the resulting text from the SchemaWriter is reparsed in order to re-serialize with
     * indentation turned on.
     *
     * @param aSchema The schema to serialize
     * @param aUseIndentation True if the result should be indented
     */
    public static String serializeSchema(Schema aSchema, boolean aUseIndentation) {
        logger.debug("serializeSchema()");
        try {
            CharArrayWriter charWriter = new CharArrayWriter();
            SchemaWriter schemaWriter = new SchemaWriter(charWriter);
            schemaWriter.write(aSchema);
            String schemaStr = new String(charWriter.toCharArray());
            if (aUseIndentation) {
                XMLParserBase parser = new XMLParserBase();
                parser.setNamespaceAware(false);
                parser.setValidating(false);
                Document doc = parser.loadWsdlDocument(new InputSource(new StringReader(schemaStr)), null);
                schemaStr = XMLParserBase.documentToString(doc, true);
            }
            return schemaStr;
        } catch (Exception e) {
            logger.error("Error: " + e);
            return e.getLocalizedMessage();
        }
    }
}
