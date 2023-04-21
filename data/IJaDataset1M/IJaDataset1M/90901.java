package com.hp.hpl.jena.rdf.model.impl;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.*;
import com.hp.hpl.jena.vocabulary.*;
import com.hp.hpl.jena.shared.*;
import java.util.*;

/**
    An abstract base class for implementations of ModelSpec. It provides the base 
    functionality of providing a ModelMaker (different sub-classes use this for different
    purposes) and utility methods for reading and creating RDF descriptions. It also
    provides a value table associating freshly-constructed bnodes with arbitrary Java
    values, so program-constructed specifications can pass on database connections,
    actual document managers, and so forth.
    
 	@author kers
*/
public abstract class ModelSpecImpl implements ModelSpec {

    /**
        The ModelMaker that may be used by sub-classes.
    */
    protected ModelMaker maker;

    /**
        The map which associates bnodes with Java values.
    */
    private static Map values = new HashMap();

    /**
        Initialise this ModelSpec with the supplied non-nullModeMaker.
        
        @param maker the ModelMaker to use, or null to create a fresh one
    */
    public ModelSpecImpl(ModelMaker maker) {
        if (maker == null) throw new RuntimeException("null maker not allowed");
        this.maker = maker;
    }

    public ModelSpecImpl(Resource root, Model description) {
        this(createMaker(getMaker(root, description), description));
        this.root = root;
        this.description = description;
    }

    public static final Model emptyModel = ModelFactory.createDefaultModel();

    protected Model defaultModel = null;

    public static final Resource emptyResource = emptyModel.createResource();

    protected Model description = emptyModel;

    protected Resource root = ResourceFactory.createResource("");

    /**
        Answer a Model created according to this ModelSpec, with any required
        files loaded into it.
    */
    public final Model createFreshModel() {
        return loadFiles(doCreateModel());
    }

    /**
        Answer a Model created according to this ModelSpec; subclasses must 
        implement. The resulting model is returned by <code>createModel</code>
        after loading any files specified by jms:loadFile properties.
    */
    protected abstract Model doCreateModel();

    public Model createDefaultModel() {
        if (defaultModel == null) defaultModel = makeDefaultModel();
        return defaultModel;
    }

    protected Model makeDefaultModel() {
        Statement s = root.getProperty(JenaModelSpec.modelName);
        return loadFiles(s == null ? maker.createDefaultModel() : maker.createModel(s.getString()));
    }

    /**
        Answer a Model created according to this ModelSpec and based on an underlying
        Model with the given name.
         
     	@see com.hp.hpl.jena.rdf.model.ModelSpec#createModelOver(java.lang.String)
     */
    public Model createModelOver(String name) {
        return loadFiles(implementCreateModelOver(name));
    }

    public abstract Model implementCreateModelOver(String name);

    /**
        Answer the JenaModelSpec subproperty of JenaModelSpec.maker that describes the relationship 
        between this specification and its ModelMaker.
        
        @return a sub-property of JenaModelSpec.maker
    */
    public abstract Property getMakerProperty();

    /**
     	Answer a Model, as per the specification of ModelSpec; appeal to 
        the sibling Maker.
    */
    public Model openModel(String name) {
        return loadFiles(maker.openModel(name));
    }

    public Model openModel() {
        Statement s = root.getProperty(JenaModelSpec.modelName);
        return loadFiles(s == null ? maker.openModel() : maker.openModel(s.getString(), true));
    }

    /**
        Answer the model hidden in the sibling maker, if it has one, and
        null otherwise.
    */
    public Model openModelIfPresent(String name) {
        return maker.hasModel(name) ? loadFiles(maker.openModel(name)) : null;
    }

    public static Resource getMaker(Resource root, Model desc) {
        StmtIterator it = desc.listStatements(root, JenaModelSpec.maker, (RDFNode) null);
        if (it.hasNext()) return it.nextStatement().getResource(); else {
            Resource r = desc.createResource();
            desc.add(root, JenaModelSpec.maker, r);
            return r;
        }
    }

    /**
        Answer the ModelMaker that this ModelSpec uses.
        @return the embedded ModelMaker
    */
    public ModelMaker getModelMaker() {
        return maker;
    }

    public Model getDescription() {
        return getDescription(ResourceFactory.createResource());
    }

    public Model getDescription(Resource root) {
        return addDescription(ModelFactory.createDefaultModel(), root);
    }

    public Model addDescription(Model desc, Resource root) {
        Resource makerRoot = desc.createResource();
        desc.add(root, JenaModelSpec.maker, makerRoot);
        maker.addDescription(desc, makerRoot);
        return desc;
    }

    /**
        Answer a new bnode Resource associated with the given value. The mapping from
        bnode to value is held in a single static table, and is not intended to hold many
        objects; there is no provision for garbage-collecting them [this might eventually be
        regarded as a bug].
        
        @param value a Java value to be remembered 
        @return a fresh bnode bound to <code>value</code>
    */
    public static Resource createValue(Object value) {
        Resource it = ResourceFactory.createResource();
        values.put(it, value);
        return it;
    }

    /**
        Answer the value bound to the supplied bnode, or null if there isn't one or the
        argument isn't a bnode.
        
        @param it the RDF node to be looked up in the <code>createValue</code> table.
        @return the associated value, or null if there isn't one.
    */
    public static Object getValue(RDFNode it) {
        return values.get(it);
    }

    /**
        Answer the unique subject with the given rdf:type.
        
        @param m the model in which the typed subject is sought
        @param type the RDF type the subject must have
        @return the unique S such that (S rdf:type type)
        @exception BadDescriptionException if there's not exactly one subject
    */
    public static Resource findRootByType(Model description, Resource type) {
        return ModelSpecFactory.findRootByType(ModelSpecFactory.withSchema(description), type);
    }

    /**
        Answer a ModelMaker that conforms to the supplied description. The Maker
        is found from the ModelMakerCreatorRegistry by looking up the most 
        specific type of the unique object with type JenaModelSpec.MakerSpec.
        
        @param d the model containing the description
        @return a ModelMaker fitting that description
    */
    public static ModelMaker createMaker(Model description) {
        Model d = ModelSpecFactory.withSchema(description);
        return createMakerByRoot(ModelSpecFactory.findRootByType(d, JenaModelSpec.MakerSpec), d);
    }

    public static ModelMaker createMaker(Resource root, Model d) {
        return createMakerByRoot(root, ModelSpecFactory.withSchema(d));
    }

    public static ModelMaker createMakerByRoot(Resource root, Model fullDesc) {
        Resource type = ModelSpecFactory.findSpecificType((Resource) root.inModel(fullDesc), JenaModelSpec.MakerSpec);
        ModelMakerCreator mmc = ModelMakerCreatorRegistry.findCreator(type);
        if (mmc == null) throw new RuntimeException("no maker type");
        return mmc.create(fullDesc, root);
    }

    /**
        Read a model from a given URI.
     	@param source the resource who's URI specifies what to laod
     	@return the model as loaded from the resource URI
     */
    public static Model readModel(Resource source) {
        String uri = source.getURI();
        return FileManager.get().loadModel(uri);
    }

    protected Model loadFiles(Model m) {
        StmtIterator it = description.listStatements(root, JenaModelSpec.loadWith, (RDFNode) null);
        while (it.hasNext()) loadFile(m, it.nextStatement().getResource());
        return m;
    }

    protected Model loadFile(Model m, Resource file) {
        FileManager.get().readModel(m, file.getURI());
        return m;
    }

    /**
        @deprecated 
        @see com.hp.hpl.jena.rdf.model.ModelSource#getModel()
    */
    public Model getModel() {
        return createDefaultModel();
    }

    /**
        @deprecated 
        @see com.hp.hpl.jena.rdf.model.ModelSource#createModel()
     */
    public Model createModel() {
        return createFreshModel();
    }

    public Model getModel(String URL) {
        return null;
    }

    public Model getModel(String URL, ModelReader loadIfAbsent) {
        throw new CannotCreateException(URL);
    }
}
