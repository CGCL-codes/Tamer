package at.tuvienna.metamodeltoowl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.log4j.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import at.tuvienna.metamodel.MetaAssociation;
import at.tuvienna.metamodel.MetaAttribute;
import at.tuvienna.metamodel.MetaClass;
import at.tuvienna.metamodel.MetaElement;
import at.tuvienna.metamodel.MetaPackage;
import at.tuvienna.utils.SettingsReader;
import at.tuvienna.utils.Util;

/**
 * @author Andreas Gruenwald Convertion of meta model into OWL ontology using
 *         Protege's OWLApi.
 */
public class OWLMaker {

    private static final Logger logger = Logger.getLogger(OWLMaker.class);

    /**
	 * Convertion of meta model into OWL ontology. All identifiers (class names,
	 * attribute names, association names) must be unique.
	 * 
	 * @param owlPath
	 *            Absolute path to the resulting OWL file without name of OWL
	 *            file. Example: file:/home/user/factory_domain.owl
	 * @param iriPath
	 *            name of internal source identifier that is used inside the OWL
	 *            model. For example
	 *            "http://www.co-ode.org/ontologies/testont.owl". The URL should
	 *            be unique put it is not necessary that the URL exists or is
	 *            accessible.
	 * @param metaPackage
	 *            meta package (as part of the meta model),  containing classes and elements 
	 *            that should be transfered into a single file.
	 * @throws OWLException
	 *             if convertion process fails.
	 */
    public static void toOwl(String owlPath, String iriPath, MetaPackage metaPackage) throws OWLException {
        logger.trace(String.format("Called OWLMaker.toOWL(%s,%s,metaPackage).", owlPath, iriPath));
        owlPath = owlPath.startsWith("file:") ? owlPath : "file:" + owlPath;
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        IRI ontologyIRI = IRI.create(iriPath);
        IRI documentIRI = IRI.create(owlPath);
        SimpleIRIMapper mapper = new SimpleIRIMapper(ontologyIRI, documentIRI);
        manager.addIRIMapper(mapper);
        PrefixManager pm = new DefaultPrefixManager(ontologyIRI + "#");
        OWLOntology ontology = manager.createOntology(ontologyIRI);
        logger.debug("Created the ontology.");
        OWLDataFactory factory = manager.getOWLDataFactory();
        logger.debug("Created OWL data factory.");
        logger.trace("Add all classes to ontology.");
        Iterator<MetaClass> it = metaPackage.getClasses().iterator();
        while (it.hasNext()) {
            MetaClass metaClass = it.next();
            OWLClass owlClass = factory.getOWLClass(":" + metaClass.getName(), pm);
            OWLDeclarationAxiom declarationAxiom = factory.getOWLDeclarationAxiom(owlClass);
            manager.addAxiom(ontology, declarationAxiom);
            logger.debug(String.format("Added class %s.", metaClass.getName()));
            addComments(factory, ontology, manager, metaClass, owlClass.getIRI());
            if (metaClass.isAbstractClass()) {
                logger.debug(String.format("Class %s is abstract - created annotation.", metaClass.getName()));
                OWLAnnotation commentAnno = factory.getOWLAnnotation(factory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI()), factory.getOWLLiteral("This class has been marked as abstract in UML."));
                OWLAxiom axiom = factory.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), commentAnno);
                manager.applyChange(new AddAxiom(ontology, axiom));
            }
        }
        logger.trace(String.format("Create subclass axioms..."));
        it = metaPackage.getClasses().iterator();
        while (it.hasNext()) {
            MetaClass metaClass = it.next();
            Iterator<MetaClass> itSubclasses = metaClass.getSubclasses().iterator();
            OWLClass owlClazz = factory.getOWLClass(":" + metaClass.getName(), pm);
            while (itSubclasses.hasNext()) {
                MetaClass subClass = itSubclasses.next();
                OWLClass owlSubclass = factory.getOWLClass(":" + subClass.getName(), pm);
                OWLAxiom axiom = factory.getOWLSubClassOfAxiom(owlSubclass, owlClazz);
                AddAxiom addAxiom = new AddAxiom(ontology, axiom);
                manager.applyChange(addAxiom);
            }
        }
        addDataProperties(factory, ontology, manager, ontologyIRI, pm, metaPackage);
        addObjectProperties(factory, ontology, manager, ontologyIRI, pm, metaPackage);
        manager.saveOntology(ontology);
    }

    protected static void addComments(OWLDataFactory factory, OWLOntology ontology, OWLOntologyManager manager, MetaElement element, IRI elementIRI) {
        logger.trace(String.format("Look for comments for class %s.", element.getName()));
        for (String comment : element.getComment()) {
            OWLAnnotation commentAnno = factory.getOWLAnnotation(factory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI()), factory.getOWLLiteral(comment));
            OWLAxiom axiom = factory.getOWLAnnotationAssertionAxiom(elementIRI, commentAnno);
            manager.applyChange(new AddAxiom(ontology, axiom));
            logger.debug(String.format("       Added comment \"%s\".", comment));
        }
    }

    protected static void addDataProperties(OWLDataFactory factory, OWLOntology ontology, OWLOntologyManager manager, IRI ontologyIRI, PrefixManager pm, MetaPackage metaPackage) {
        logger.trace(String.format("Add attributes..."));
        Iterator<MetaClass> it = metaPackage.getClasses().iterator();
        while (it.hasNext()) {
            MetaClass metaClass = it.next();
            OWLClass owlClass = factory.getOWLClass(":" + metaClass.getName(), pm);
            for (MetaAttribute a : metaClass.getAttributes()) {
                String prefix = a.getRange().equalsIgnoreCase(MetaAttribute.BOOLEAN) ? SettingsReader.readProperty("data-property-prefix-boolean") : SettingsReader.readProperty("data-property-prefix");
                String dataPropertyIRI = prefix + a.getName().substring(0, 1).toUpperCase() + a.getName().substring(1);
                OWLDatatype dataType = null;
                String datatype = a.getRange();
                if (datatype.equalsIgnoreCase(MetaAttribute.VOID)) {
                    logger.debug(String.format("Data type %s is void.", datatype));
                } else {
                    String error = "";
                    String owl2DatatypeString = datatype.toUpperCase();
                    owl2DatatypeString = owl2DatatypeString.replace(" ", "_");
                    String[] owl2prefixes = new String[] { "XSD", "RDF", "OWL" };
                    logger.debug("Try to find native OWL2 datatype...");
                    for (String owl2Prefix : owl2prefixes) {
                        logger.trace(String.format("Attribute %s has data type %s. Looking for OWL2Datatype %s.", a, datatype, owl2Prefix + "_" + owl2DatatypeString));
                        try {
                            OWL2Datatype owl2Datatype = (OWL2Datatype) OWL2Datatype.class.getDeclaredField(owl2Prefix + "_" + owl2DatatypeString).get(null);
                            dataType = factory.getOWLDatatype(owl2Datatype.getIRI());
                            logger.debug(String.format("Found datatype %s for data property %s.", dataType, dataPropertyIRI));
                            break;
                        } catch (Exception e) {
                            error = (String.format("Could not convert to datatype that is supported by OWL2: %s. \nTake a look at XSD (XML Schema Definition) data types and rename your attribute type according to XSD supported types (e.g. string, token, ...).", owl2Prefix + datatype));
                        }
                    }
                    logger.debug(String.format("Try to identify attribute type %s...", datatype));
                    if (!error.isEmpty()) {
                        if (datatype.equalsIgnoreCase(MetaAttribute.DATETIME) || datatype.equalsIgnoreCase(MetaAttribute.TIME)) {
                            dataType = factory.getOWLDatatype(OWL2Datatype.XSD_DATE_TIME.getIRI());
                            error = "";
                        } else if (datatype.contains(MetaAttribute.TIMESTAMP)) {
                            dataType = factory.getOWLDatatype(OWL2Datatype.XSD_DATE_TIME_STAMP.getIRI());
                            error = "";
                        }
                    }
                    if (!error.isEmpty()) {
                        logger.error(error);
                        datatype = MetaAttribute.VOID;
                    }
                }
                OWLDataPropertyExpression dpe = factory.getOWLDataProperty(IRI.create(dataPropertyIRI));
                if (datatype.equalsIgnoreCase(MetaAttribute.VOID)) {
                    dataType = factory.getOWLDatatype("anyType", pm);
                    OWLDataExactCardinality svf = factory.getOWLDataExactCardinality(1, dpe, dataType);
                    OWLSubClassOfAxiom ax = factory.getOWLSubClassOfAxiom(owlClass, svf);
                    manager.addAxiom(ontology, ax);
                } else {
                    OWLDataExactCardinality svf = factory.getOWLDataExactCardinality(1, dpe, dataType);
                    OWLSubClassOfAxiom ax = factory.getOWLSubClassOfAxiom(owlClass, svf);
                    manager.addAxiom(ontology, ax);
                }
                OWLDataPropertyDomainAxiom dpda = factory.getOWLDataPropertyDomainAxiom(dpe, owlClass);
                manager.addAxiom(ontology, dpda);
                manager.addAxiom(ontology, factory.getOWLDataPropertyRangeAxiom(dpe, dataType));
            }
        }
    }

    protected static void addObjectProperties(OWLDataFactory factory, OWLOntology ontology, OWLOntologyManager manager, IRI ontologyIRI, PrefixManager pm, MetaPackage metaPackage) {
        logger.trace(String.format("Add relations..."));
        Iterator<MetaClass> it = metaPackage.getClasses().iterator();
        Map<OWLObjectProperty, List<List<OWLClass>>> rangeDomainMap = new HashMap<OWLObjectProperty, List<List<OWLClass>>>();
        while (it.hasNext()) {
            MetaClass metaClass = it.next();
            for (MetaAssociation a : metaClass.getAssociations()) {
                OWLClass classFrom = factory.getOWLClass(":" + a.getFrom().getMetaClass().getName(), pm);
                OWLClass classTo = factory.getOWLClass(":" + a.getTo().getMetaClass().getName(), pm);
                OWLObjectProperty hasRelation = factory.getOWLObjectProperty(":" + a.getName(), pm);
                OWLSubClassOfAxiom ax = null;
                List<String> range = a.getTo().getRange();
                if (range.isEmpty() || range.get(0).contains("*") || (range.size() == 2 && range.get(0).isEmpty() && range.get(1).equals("*"))) {
                    OWLClassExpression hasSomeToRelations = factory.getOWLObjectSomeValuesFrom(hasRelation, classTo);
                    ax = factory.getOWLSubClassOfAxiom(classFrom, hasSomeToRelations);
                } else if (range.size() == 1) {
                    OWLClassExpression hasSomeToRelations = factory.getOWLObjectExactCardinality(Integer.parseInt(range.get(0)), hasRelation, classTo);
                    ax = factory.getOWLSubClassOfAxiom(classFrom, hasSomeToRelations);
                } else {
                    Set<OWLClassExpression> expressionSet = new HashSet<OWLClassExpression>();
                    if (Util.isMinLimit(range)) {
                        OWLClassExpression hasSomeToRelations = factory.getOWLObjectMinCardinality(Integer.parseInt(range.get(0)), hasRelation, classTo);
                        expressionSet.add(hasSomeToRelations);
                    }
                    if (Util.isMaxLimit(range)) {
                        OWLClassExpression hasSomeToRelations = factory.getOWLObjectMaxCardinality(Integer.parseInt(range.get(range.size() - 1)), hasRelation, classTo);
                        expressionSet.add(hasSomeToRelations);
                    }
                    if (!Util.isChain(range)) {
                        Set<OWLClassExpression> chainExpr = new HashSet<OWLClassExpression>();
                        for (int i = 0; i < range.size(); i++) {
                            String value = range.get(i);
                            try {
                                Integer.parseInt(value);
                            } catch (NumberFormatException e) {
                                continue;
                            }
                            if (i < range.size() - 1 && range.get(i + 1).equals("*")) {
                                OWLClassExpression hasSomeToRelations = factory.getOWLObjectMinCardinality(Integer.parseInt(value), hasRelation, classTo);
                                chainExpr.add(hasSomeToRelations);
                            } else {
                                OWLClassExpression hasSomeToRelations = factory.getOWLObjectExactCardinality(Integer.parseInt(value), hasRelation, classTo);
                                chainExpr.add(hasSomeToRelations);
                            }
                        }
                        expressionSet.add(factory.getOWLObjectUnionOf(chainExpr));
                    }
                    ax = factory.getOWLSubClassOfAxiom(classFrom, factory.getOWLObjectIntersectionOf(expressionSet));
                }
                AddAxiom addAx = new AddAxiom(ontology, ax);
                manager.applyChange(addAx);
                if (!rangeDomainMap.containsKey(hasRelation)) {
                    List<List<OWLClass>> rangeDomainList = new LinkedList<List<OWLClass>>();
                    List<OWLClass> rangeList = new LinkedList<OWLClass>();
                    List<OWLClass> domainList = new LinkedList<OWLClass>();
                    rangeDomainList.add(rangeList);
                    rangeDomainList.add(domainList);
                    rangeDomainMap.put(hasRelation, rangeDomainList);
                }
                rangeDomainMap.get(hasRelation).get(0).add(classTo);
                rangeDomainMap.get(hasRelation).get(1).add(classFrom);
                OWLDeclarationAxiom declarationAxiom = factory.getOWLDeclarationAxiom(hasRelation);
                manager.addAxiom(ontology, declarationAxiom);
                if (a.isAggregation()) {
                    a.getComment().add("Has been defined as an aggregate relation.");
                }
                if (a.isComposition()) {
                    a.getComment().add("Has been defined as a composition relation.");
                }
                addComments(factory, ontology, manager, a, hasRelation.getIRI());
                if (a.getInverseAssociation() != null) {
                    OWLAxiom inverseAxiom = factory.getOWLInverseObjectPropertiesAxiom(hasRelation, factory.getOWLObjectProperty(":" + a.getInverseAssociation().getName(), pm));
                    manager.addAxiom(ontology, inverseAxiom);
                }
            }
        }
        Iterator<Entry<OWLObjectProperty, List<List<OWLClass>>>> itR = rangeDomainMap.entrySet().iterator();
        while (itR.hasNext()) {
            Entry<OWLObjectProperty, List<List<OWLClass>>> entry = itR.next();
            Set<OWLClassExpression> existingRanges = factory.getOWLObjectProperty(entry.getKey().getIRI()).getRanges(ontology);
            Set<OWLClassExpression> existingDomains = factory.getOWLObjectProperty(entry.getKey().getIRI()).getDomains(ontology);
            for (OWLClass rangeClass : entry.getValue().get(0)) {
                existingRanges.add(rangeClass);
            }
            for (OWLClass domainClass : entry.getValue().get(1)) {
                existingDomains.add(domainClass);
            }
            OWLAxiom rangeAxiom = factory.getOWLObjectPropertyRangeAxiom(entry.getKey(), factory.getOWLObjectUnionOf(existingRanges));
            OWLAxiom domainAxiom = factory.getOWLObjectPropertyDomainAxiom(entry.getKey(), factory.getOWLObjectUnionOf(existingDomains));
            manager.addAxiom(ontology, rangeAxiom);
            manager.addAxiom(ontology, domainAxiom);
        }
    }
}
