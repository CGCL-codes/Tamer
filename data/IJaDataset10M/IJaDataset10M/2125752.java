package org.mediavirus.graphl.vocabulary;

/**
 * @author Flo Ledermann <ledermann@ims.tuwien.ac.at>
 * created: 18.05.2004 00:47:56
 */
public class Graphl extends AbstractVocabulary {

    static Vocabulary INSTANCE = new Graphl();

    public static Vocabulary getVocabulary() {
        return INSTANCE;
    }

    private Graphl() {
        namespace = NS.graphl;
        name = "Graphl";
        defaultNamespacePrefix = "graphl";
        description = "Graphl internal vocabulary";
        addPropertyGroup(new ResourceGroup("Linking", new ResourceDescriptor[] { new ResourceDescriptor(namespace, "connectedTo", "connected to"), new ResourceDescriptor(namespace, "parentOf", "parent of"), new ResourceDescriptor(namespace, "frontLink", "front link") }));
        addPropertyGroup(new ResourceGroup("Internal Properties", new ResourceDescriptor[] { new ResourceDescriptor(namespace, "hasLabel", "label"), new ResourceDescriptor(namespace, "hasComment", "comment"), new ResourceDescriptor(namespace, "canvasPosition", "position") }));
    }
}
