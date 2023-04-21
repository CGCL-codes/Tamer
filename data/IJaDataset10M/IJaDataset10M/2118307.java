package org.rascalli.mbe;

import java.io.IOException;
import org.openrdf.OpenRDFException;
import org.openrdf.model.URI;
import org.openrdf.model.vocabulary.RDF;

/**
 * <p>
 * 
 * </p>
 * 
 * <p>
 * <b>Company:&nbsp;</b> SAT, Research Studios Austria
 * </p>
 * 
 * <p>
 * <b>Copyright:&nbsp;</b> (c) 2007
 * </p>
 * 
 * <p>
 * <b>last modified:</b><br/> $Author: $<br/> $Date: $<br/> $Revision: $
 * </p>
 * 
 * @author Christian Schollum
 */
public class ToolData extends RdfData {

    private static UUIDGenerator uuidGenerator = RandomUUIDGenerator.getInstance();

    /**
     * This method is used for testing purposes, in order to have control over
     * the generated UUIDs.
     * 
     * @param uuidGenerator
     */
    public static void setUUIDGenerator(UUIDGenerator uuidGenerator) {
        ToolData.uuidGenerator = uuidGenerator;
    }

    protected final URI tool;

    public ToolData(ToolID toolId, ToolDataType toolDataType) throws RdfException {
        super(toolId, toolDataType);
        tool = rascalli(toolId.toString());
        add(tool, RDF.TYPE, rascalli(toolId.getToolType()));
    }

    /**
     * @param data
     * @throws IOException
     * @throws RdfException
     */
    public ToolData(String data) throws RdfException, IOException {
        super(data);
        tool = rascalli(getToolId().toString());
    }

    /**
     * @param action
     * @return
     */
    protected String uniqueInstance(String type) {
        return type + "-" + generateUUID();
    }

    /**
     * @return
     */
    private String generateUUID() {
        return uuidGenerator.generateUUID().toString();
    }

    /**
     * @param subject
     * @param textValue
     * @throws OpenRDFException
     */
    public void hasTextValue(final URI subject, String textValue) throws RdfException {
        final URI stringLiteral = has(subject, "literalStringValue", "StringLiteral");
        hasLiteralValue(stringLiteral, "textValue", textValue);
    }

    /**
     * @param stringLiteral
     * @param string
     * @param text
     * @throws OpenRDFException
     */
    protected void hasLiteralValue(URI subject, String predicate, String value) throws RdfException {
        add(subject, rascalli(predicate), literal(value));
    }

    /**
     * @param string
     * @param string2
     * @throws RdfException
     */
    public void toolHasLiteral(String predicate, String value) throws RdfException {
        hasLiteralValue(tool, predicate, value);
    }

    /**
     * @param subject
     * @param predicate
     *            TODO
     * @param objectType
     *            TODO
     * @throws OpenRDFException
     */
    public URI has(final URI subject, String predicate, String objectType, String objectId) throws RdfException {
        final URI object = rascalli(objectType + "-" + objectId);
        add(subject, rascalli(predicate), object);
        add(object, RDF.TYPE, rascalli(objectType));
        return object;
    }

    public URI has(final URI subject, String predicate, String objectType) throws RdfException {
        return has(subject, predicate, objectType, generateUUID());
    }

    public URI toolHas(String predicate, String objectType) throws RdfException {
        return has(tool, predicate, objectType, generateUUID());
    }
}
