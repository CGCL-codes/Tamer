package org.docflower.serializer.context;

import java.io.Writer;
import java.util.*;
import javax.jdo.PersistenceManager;
import org.docflower.serializer.metanodes.IMetanodeParameter;
import org.docflower.xml.SimpleNamespaceContext;

public class SimpleSerializationContext implements ISerializationContext {

    public static final String SCHEMA_NS_URI = "http://www.w3.org/2001/XMLSchema";

    public static final String SCHEMA_NS_PREFIX = "xs";

    public static final String SCHEMA_INSTANCE_NS_URI = "http://www.w3.org/2001/XMLSchema-instance";

    public static final String SCHEMA_INSTANCE_NS_PREFIX = "xsi";

    public static final String PAGEDTABLE_NS_PREFIX = "pt";

    public static final String PAGEDTABLE_NS_URI = "net://org.docflower.server/controls/pagedtable";

    private Integer currentNSIndex = 0;

    private SimpleNamespaceContext nsContext;

    private IOutputGenerator outputGenerator;

    private Writer writer;

    private PersistenceManager persistanceManager;

    private Map<Object, List<IMetanodeParameter>> parameters = new IdentityHashMap<Object, List<IMetanodeParameter>>();

    public SimpleSerializationContext(SimpleNamespaceContext nsContext, IOutputGenerator outputGenerator, Writer writer, PersistenceManager pm) {
        this.nsContext = nsContext;
        this.nsContext.addPrefix(SCHEMA_NS_PREFIX, SCHEMA_NS_URI);
        this.nsContext.addPrefix(SCHEMA_INSTANCE_NS_PREFIX, SCHEMA_INSTANCE_NS_URI);
        this.nsContext.addPrefix(PAGEDTABLE_NS_PREFIX, PAGEDTABLE_NS_URI);
        this.outputGenerator = outputGenerator;
        this.writer = writer;
        this.persistanceManager = pm;
    }

    @Override
    public SimpleNamespaceContext getNamespaceContext() {
        return this.nsContext;
    }

    @Override
    public String getPrefixByClassName(String className) {
        String result = "";
        if (this.nsContext != null) {
            result = this.nsContext.getPrefix(className);
            if (result == null) {
                result = getNextPrefix();
                this.nsContext.addPrefix(result, className);
            }
        }
        return result;
    }

    @Override
    public IOutputGenerator getOutputGenerator() {
        return this.outputGenerator;
    }

    @Override
    public Writer getWriter() {
        return writer;
    }

    private String getNextPrefix() {
        String result = "c" + currentNSIndex.toString();
        currentNSIndex++;
        return result;
    }

    @Override
    public String generateNameSpaces() {
        StringBuilder sb = new StringBuilder();
        for (String prefix : getNamespaceContext().getPrefixesList()) {
            sb.append(" xmlns:");
            sb.append(prefix);
            sb.append("=");
            sb.append("\"");
            sb.append(getNamespaceContext().getNamespaceURI(prefix));
            sb.append("\"");
            sb.append(" ");
        }
        return sb.toString();
    }

    @Override
    public Map<Object, List<IMetanodeParameter>> getParameters() {
        return this.parameters;
    }

    @Override
    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    @Override
    public PersistenceManager getPersistenceManager() {
        return this.persistanceManager;
    }
}
