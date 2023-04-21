package com.mindquarry.jcr.jackrabbit;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import javax.jcr.NamespaceException;
import javax.jcr.NamespaceRegistry;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.apache.excalibur.source.SourceNotFoundException;
import org.apache.jackrabbit.core.nodetype.InvalidNodeTypeDefException;
import org.apache.jackrabbit.core.nodetype.NodeTypeDef;
import org.apache.jackrabbit.core.nodetype.NodeTypeManagerImpl;
import org.apache.jackrabbit.core.nodetype.NodeTypeRegistry;
import org.apache.jackrabbit.core.nodetype.compact.CompactNodeTypeDefReader;
import org.apache.jackrabbit.core.nodetype.compact.ParseException;

/**
 * Helper class for the JackrabbitInitializer.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class JackrabbitInitializerHelper {

    public static final String MQ_JCR_XML_NAMESPACE_PREFIX = "xt";

    public static final String MQ_JCR_XML_NAMESPACE_URI = "http://mindquarry.com/ns/cnd/xt";

    public static final String MQ_JCR_ID_NAMESPACE_PREFIX = "id";

    public static final String MQ_JCR_ID_NAMESPACE_URI = "http://mindquarry.com/ns/cnd/id";

    public static void setupRepository(Session session, InputStreamReader nDefs, String uri) throws ParseException, RepositoryException, InvalidNodeTypeDefException, SourceNotFoundException, IOException {
        NamespaceRegistry nsRegistry = session.getWorkspace().getNamespaceRegistry();
        try {
            nsRegistry.getURI(MQ_JCR_XML_NAMESPACE_PREFIX);
        } catch (NamespaceException ne) {
            nsRegistry.registerNamespace(MQ_JCR_XML_NAMESPACE_PREFIX, MQ_JCR_XML_NAMESPACE_URI);
        }
        try {
            nsRegistry.getURI(MQ_JCR_ID_NAMESPACE_PREFIX);
        } catch (NamespaceException ne) {
            nsRegistry.registerNamespace(MQ_JCR_ID_NAMESPACE_PREFIX, MQ_JCR_ID_NAMESPACE_URI);
        }
        NodeTypeManagerImpl ntmgr = (NodeTypeManagerImpl) session.getWorkspace().getNodeTypeManager();
        NodeTypeRegistry ntreg = ntmgr.getNodeTypeRegistry();
        registerNodeTypesFromTextFile(nDefs, ntreg, uri);
        setupInitialRepositoryStructure(session);
        session.save();
    }

    private static void registerNodeTypesFromTextFile(InputStreamReader reader, NodeTypeRegistry ntreg, String uri) throws IOException, SourceNotFoundException, ParseException, InvalidNodeTypeDefException, RepositoryException {
        CompactNodeTypeDefReader cndReader = new CompactNodeTypeDefReader(reader, uri);
        List ntdList = cndReader.getNodeTypeDefs();
        registerNodeTypes(ntdList, ntreg);
    }

    private static void registerNodeTypes(List ntdList, NodeTypeRegistry ntreg) throws RepositoryException {
        for (Iterator i = ntdList.iterator(); i.hasNext(); ) {
            NodeTypeDef ntd = (NodeTypeDef) i.next();
            try {
                ntreg.registerNodeType(ntd);
            } catch (InvalidNodeTypeDefException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
    }

    private static void setupInitialRepositoryStructure(Session session) throws RepositoryException {
        Node root = session.getRootNode();
        try {
            root.getNode("users");
        } catch (PathNotFoundException e) {
            root.addNode("users", "nt:folder");
        }
        try {
            root.getNode("teamspaces");
        } catch (PathNotFoundException e) {
            root.addNode("teamspaces", "nt:folder");
        }
        try {
            root.getNode("tags");
        } catch (PathNotFoundException e) {
            root.addNode("tags", "nt:folder");
        }
    }
}
