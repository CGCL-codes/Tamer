package org.obolibrary.obo2owl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.obolibrary.obo2owl.Obo2OWLConstants.Obo2OWLVocabulary;
import org.obolibrary.oboformat.model.Clause;
import org.obolibrary.oboformat.model.Frame;
import org.obolibrary.oboformat.model.Frame.FrameType;
import org.obolibrary.oboformat.model.OBODoc;
import org.obolibrary.oboformat.model.QualifierValue;
import org.obolibrary.oboformat.model.Xref;
import org.obolibrary.oboformat.parser.OBOFormatConstants;
import org.obolibrary.oboformat.parser.OBOFormatConstants.OboFormatTag;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedObject;
import org.semanticweb.owlapi.model.OWLNaryPropertyAxiom;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectCardinalityRestriction;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLQuantifiedObjectRestriction;
import org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owlapi.vocab.Namespaces;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

public class Owl2Obo {

    private static Logger LOG = Logger.getLogger(Owl2Obo.class);

    OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

    OWLOntology owlOntology;

    OWLDataFactory fac;

    OBODoc obodoc;

    Set<OWLAxiom> untranslatableAxioms;

    Map<String, String> idSpaceMap;

    public static Map<String, String> annotationPropertyMap = initAnnotationPropertyMap();

    Set<OWLAnnotationProperty> apToDeclare;

    private String ontologyId;

    private boolean strictConversion;

    private void init() {
        idSpaceMap = new HashMap<String, String>();
        idSpaceMap.put("http://www.obofoundry.org/ro/ro.owl#", "OBO_REL");
        untranslatableAxioms = new HashSet<OWLAxiom>();
        manager = OWLManager.createOWLOntologyManager();
        fac = manager.getOWLDataFactory();
        apToDeclare = new HashSet<OWLAnnotationProperty>();
    }

    public Owl2Obo() {
        init();
    }

    private static HashMap<String, String> initAnnotationPropertyMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        for (String key : Obo2Owl.annotationPropertyMap.keySet()) {
            IRI propIRI = Obo2Owl.annotationPropertyMap.get(key);
            map.put(propIRI.toString(), key);
        }
        return map;
    }

    public void setStrictConversion(boolean b) {
        this.strictConversion = b;
    }

    public boolean getStrictConversion() {
        return this.strictConversion;
    }

    public OWLOntologyManager getManager() {
        return manager;
    }

    public void setManager(OWLOntologyManager manager) {
        this.manager = manager;
    }

    public OBODoc getObodoc() {
        return obodoc;
    }

    public void setObodoc(OBODoc obodoc) {
        this.obodoc = obodoc;
    }

    public OBODoc convert(OWLOntology ont) throws OWLOntologyCreationException {
        this.owlOntology = ont;
        if (ont != null) this.ontologyId = getOntologyId(ont); else this.ontologyId = "TODO";
        init();
        return tr();
    }

    /**
	 * @return the untranslatableAxioms
	 */
    public Collection<OWLAxiom> getUntranslatableAxioms() {
        return untranslatableAxioms;
    }

    private OBODoc tr() throws OWLOntologyCreationException {
        obodoc = new OBODoc();
        preProcess(owlOntology);
        tr(owlOntology);
        for (OWLAxiom ax : owlOntology.getAxioms()) {
            if (ax instanceof OWLDeclarationAxiom) {
                tr((OWLDeclarationAxiom) ax);
            } else if (ax instanceof OWLSubClassOfAxiom) {
                tr((OWLSubClassOfAxiom) ax);
            } else if (ax instanceof OWLDisjointClassesAxiom) {
                tr((OWLDisjointClassesAxiom) ax);
            } else if (ax instanceof OWLEquivalentClassesAxiom) {
                tr((OWLEquivalentClassesAxiom) ax);
            } else if (ax instanceof OWLClassAssertionAxiom) {
                tr((OWLClassAssertionAxiom) ax);
            } else if (ax instanceof OWLEquivalentObjectPropertiesAxiom) {
                tr((OWLEquivalentObjectPropertiesAxiom) ax);
            } else if (ax instanceof OWLSubAnnotationPropertyOfAxiom) {
                tr((OWLSubAnnotationPropertyOfAxiom) ax);
            } else if (ax instanceof OWLSubObjectPropertyOfAxiom) {
                tr((OWLSubObjectPropertyOfAxiom) ax);
            } else if (ax instanceof OWLObjectPropertyRangeAxiom) {
                tr((OWLObjectPropertyRangeAxiom) ax);
            } else if (ax instanceof OWLFunctionalObjectPropertyAxiom) {
                tr((OWLFunctionalObjectPropertyAxiom) ax);
            } else if (ax instanceof OWLSymmetricObjectPropertyAxiom) {
                tr((OWLSymmetricObjectPropertyAxiom) ax);
            } else if (ax instanceof OWLAsymmetricObjectPropertyAxiom) {
                tr((OWLAsymmetricObjectPropertyAxiom) ax);
            } else if (ax instanceof OWLObjectPropertyDomainAxiom) {
                tr((OWLObjectPropertyDomainAxiom) ax);
            } else if (ax instanceof OWLInverseFunctionalObjectPropertyAxiom) {
                tr((OWLInverseFunctionalObjectPropertyAxiom) ax);
            } else if (ax instanceof OWLInverseObjectPropertiesAxiom) {
                tr((OWLInverseObjectPropertiesAxiom) ax);
            } else if (ax instanceof OWLDisjointObjectPropertiesAxiom) {
                tr((OWLDisjointObjectPropertiesAxiom) ax);
            } else if (ax instanceof OWLReflexiveObjectPropertyAxiom) {
                tr((OWLReflexiveObjectPropertyAxiom) ax);
            } else if (ax instanceof OWLTransitiveObjectPropertyAxiom) {
                tr((OWLTransitiveObjectPropertyAxiom) ax);
            } else if (ax instanceof OWLSubPropertyChainOfAxiom) {
                tr((OWLSubPropertyChainOfAxiom) ax);
            } else {
                if (!(ax instanceof OWLAnnotationAssertionAxiom)) {
                    error(ax);
                } else {
                }
            }
        }
        return obodoc;
    }

    private void preProcess(OWLOntology owlOntology2) {
        String viewRel = null;
        for (OWLAnnotation ann : owlOntology.getAnnotations()) {
            if (ann.getProperty().getIRI().equals(Obo2OWLVocabulary.IRI_OIO_LogicalDefinitionViewRelation.getIRI())) {
                OWLAnnotationValue v = ann.getValue();
                if (v instanceof OWLLiteral) {
                    viewRel = ((OWLLiteral) v).getLiteral();
                } else {
                    viewRel = this.getIdentifier((IRI) v);
                }
                break;
            }
        }
        if (viewRel != null) {
            Set<OWLAxiom> rmAxioms = new HashSet<OWLAxiom>();
            Set<OWLAxiom> newAxioms = new HashSet<OWLAxiom>();
            for (OWLEquivalentClassesAxiom eca : owlOntology.getAxioms(AxiomType.EQUIVALENT_CLASSES)) {
                int numNamed = 0;
                Set<OWLClassExpression> xs = new HashSet<OWLClassExpression>();
                for (OWLClassExpression x : eca.getClassExpressions()) {
                    if (x instanceof OWLClass) {
                        xs.add(x);
                        numNamed++;
                        continue;
                    } else if (x instanceof OWLObjectSomeValuesFrom) {
                        OWLObjectProperty p = (OWLObjectProperty) ((OWLObjectSomeValuesFrom) x).getProperty();
                        if (this.getIdentifier(p).equals(viewRel) == false) {
                            LOG.error("Expected: " + viewRel + " got: " + p + " in " + eca);
                        }
                        xs.add(((OWLObjectSomeValuesFrom) x).getFiller());
                    } else {
                        LOG.error("Unexpected: " + eca);
                    }
                }
                if (numNamed == 1) {
                    rmAxioms.add(eca);
                    newAxioms.add(fac.getOWLEquivalentClassesAxiom(xs));
                } else {
                    LOG.error("ECA did not fit expected pattern: " + eca);
                }
            }
            manager.removeAxioms(owlOntology, rmAxioms);
            manager.addAxioms(owlOntology, newAxioms);
        }
    }

    private void add(Frame f) {
        if (f != null) {
            try {
                this.obodoc.addFrame(f);
            } catch (Exception ex) {
                LOG.error(ex.getMessage(), ex);
            }
        }
    }

    private boolean trObjectProperty(OWLObjectProperty prop, String tag, String value, Set<OWLAnnotation> annotations) {
        if (prop == null || value == null) return false;
        Frame f = getTypedefFrame(prop);
        Clause clause;
        if (OboFormatTag.TAG_ID.getTag().equals(tag)) {
            clause = f.getClause(tag);
            if (tag != null) {
                clause.setValue(value);
            } else {
                clause = new Clause(tag, value);
                f.addClause(clause);
            }
        } else {
            clause = new Clause(tag, value);
            f.addClause(clause);
        }
        addQualifiers(clause, annotations);
        return true;
    }

    private boolean trObjectProperty(OWLObjectProperty prop, String tag, Boolean value, Set<OWLAnnotation> annotations) {
        if (prop == null || value == null) return false;
        Frame f = getTypedefFrame(prop);
        Clause clause = new Clause(tag);
        clause.addValue(value);
        f.addClause(clause);
        addQualifiers(clause, annotations);
        return true;
    }

    private void trNaryPropertyAxiom(OWLNaryPropertyAxiom<OWLObjectPropertyExpression> ax, String tag) {
        Set<OWLObjectPropertyExpression> set = ax.getProperties();
        if (set.size() > 1) {
            boolean first = true;
            OWLObjectProperty prop = null;
            String disjointFrom = null;
            for (OWLObjectPropertyExpression ex : set) {
                if (first) {
                    first = false;
                    if (ex instanceof OWLObjectProperty) prop = (OWLObjectProperty) ex;
                } else {
                    disjointFrom = this.getIdentifier(ex);
                }
            }
            if (trObjectProperty(prop, tag, disjointFrom, ax.getAnnotations())) {
                return;
            }
        }
        error(ax);
    }

    private void tr(OWLSubPropertyChainOfAxiom ax) {
        OWLObjectPropertyExpression p = ax.getSuperProperty();
        List<OWLObjectPropertyExpression> list = ax.getPropertyChain();
        if (list.size() != 2) {
            error(ax);
            return;
        }
        Frame f = getTypedefFrame((OWLObjectProperty) p);
        String rel1 = getIdentifier(list.get(0));
        String rel2 = getIdentifier(list.get(1));
        if (rel1 == null || rel2 == null) {
            error(ax);
            return;
        }
        Clause clause;
        if (rel1.equals(f.getId())) {
            clause = new Clause(OboFormatTag.TAG_TRANSITIVE_OVER, rel2);
        } else {
            OboFormatTag tag = OboFormatTag.TAG_HOLDS_OVER_CHAIN;
            for (OWLAnnotation ann : ax.getAnnotations()) {
                if (Obo2Owl.IRI_PROP_isReversiblePropertyChain.equals(ann.getProperty().getIRI().toString())) {
                    tag = OboFormatTag.TAG_EQUIVALENT_TO_CHAIN;
                    break;
                }
            }
            clause = new Clause(tag);
            clause.addValue(rel1);
            clause.addValue(rel2);
        }
        f.addClause(clause);
        addQualifiers(clause, ax.getAnnotations());
    }

    private void tr(OWLEquivalentObjectPropertiesAxiom ax) {
        trNaryPropertyAxiom(ax, OboFormatTag.TAG_EQUIVALENT_TO.getTag());
    }

    private void tr(OWLTransitiveObjectPropertyAxiom ax) {
        OWLObjectPropertyExpression prop = ax.getProperty();
        if (prop instanceof OWLObjectProperty) {
            if (trObjectProperty((OWLObjectProperty) prop, OboFormatTag.TAG_IS_TRANSITIVE.getTag(), Boolean.TRUE, ax.getAnnotations())) return;
        }
        error(ax);
    }

    private void tr(OWLDisjointObjectPropertiesAxiom ax) {
        trNaryPropertyAxiom(ax, OboFormatTag.TAG_DISJOINT_FROM.getTag());
    }

    private void tr(OWLReflexiveObjectPropertyAxiom ax) {
        OWLObjectPropertyExpression prop = ax.getProperty();
        if (prop instanceof OWLObjectProperty) {
            if (trObjectProperty((OWLObjectProperty) prop, OboFormatTag.TAG_IS_REFLEXIVE.getTag(), Boolean.TRUE, ax.getAnnotations())) return;
        }
        error(ax);
    }

    private void tr(OWLInverseFunctionalObjectPropertyAxiom ax) {
        OWLObjectPropertyExpression prop = ax.getProperty();
        if (prop instanceof OWLObjectProperty) {
            if (trObjectProperty((OWLObjectProperty) prop, OboFormatTag.TAG_IS_INVERSE_FUNCTIONAL.getTag(), Boolean.TRUE, ax.getAnnotations())) return;
        }
        error(ax);
    }

    private void tr(OWLInverseObjectPropertiesAxiom ax) {
        OWLObjectPropertyExpression prop1 = ax.getFirstProperty();
        OWLObjectPropertyExpression prop2 = ax.getSecondProperty();
        if (prop1 instanceof OWLObjectProperty && prop2 instanceof OWLObjectProperty) {
            if (trObjectProperty((OWLObjectProperty) prop1, OboFormatTag.TAG_INVERSE_OF.getTag(), this.getIdentifier(prop2), ax.getAnnotations())) return;
        }
        error(ax);
    }

    private void tr(OWLObjectPropertyDomainAxiom ax) {
        String range = this.getIdentifier(ax.getDomain());
        OWLObjectPropertyExpression prop = ax.getProperty();
        if (range != null && prop instanceof OWLObjectProperty) {
            if (trObjectProperty((OWLObjectProperty) prop, OboFormatTag.TAG_DOMAIN.getTag(), range, ax.getAnnotations())) {
                return;
            }
        }
        error(ax);
    }

    private void tr(OWLAsymmetricObjectPropertyAxiom ax) {
        OWLObjectPropertyExpression prop = ax.getProperty();
        if (prop instanceof OWLObjectProperty) {
            if (trObjectProperty((OWLObjectProperty) prop, OboFormatTag.TAG_IS_ASYMMETRIC.getTag(), Boolean.TRUE, ax.getAnnotations())) {
                return;
            }
        }
        error(ax);
    }

    private void tr(OWLSymmetricObjectPropertyAxiom ax) {
        OWLObjectPropertyExpression prop = ax.getProperty();
        if (prop instanceof OWLObjectProperty) {
            if (trObjectProperty((OWLObjectProperty) prop, OboFormatTag.TAG_IS_SYMMETRIC.getTag(), Boolean.TRUE, ax.getAnnotations())) return;
        }
        error(ax);
    }

    private void tr(OWLFunctionalObjectPropertyAxiom ax) {
        OWLObjectPropertyExpression prop = ax.getProperty();
        if (prop instanceof OWLObjectProperty) {
            if (trObjectProperty((OWLObjectProperty) prop, OboFormatTag.TAG_IS_FUNCTIONAL.getTag(), Boolean.TRUE, ax.getAnnotations())) return;
        }
        error(ax);
    }

    private void tr(OWLObjectPropertyRangeAxiom ax) {
        String range = this.getIdentifier(ax.getRange());
        OWLObjectPropertyExpression prop = ax.getProperty();
        if (range != null && prop instanceof OWLObjectProperty) {
            if (trObjectProperty((OWLObjectProperty) prop, OboFormatTag.TAG_RANGE.getTag(), range, ax.getAnnotations())) return;
        }
        error(ax);
    }

    private void tr(OWLSubObjectPropertyOfAxiom ax) {
        OWLObjectPropertyExpression sup = ax.getSuperProperty();
        OWLObjectPropertyExpression sub = ax.getSubProperty();
        if (sub instanceof OWLObjectProperty && sup instanceof OWLObjectProperty) {
            String supId = this.getIdentifier(sup);
            if (supId.startsWith("owl:")) {
                return;
            }
            Frame f = getTypedefFrame((OWLEntity) ax.getSubProperty());
            Clause clause = new Clause(OboFormatTag.TAG_IS_A, supId);
            f.addClause(clause);
            addQualifiers(clause, ax.getAnnotations());
        } else {
            error(ax);
        }
    }

    private void tr(OWLSubAnnotationPropertyOfAxiom ax) {
        OWLAnnotationProperty sup = ax.getSuperProperty();
        OWLAnnotationProperty sub = ax.getSubProperty();
        String _tag = owlObjectToTag(sup);
        if (OboFormatTag.TAG_SYNONYMTYPEDEF.getTag().equals(_tag)) {
            String name = "";
            String scope = "";
            for (OWLAnnotationAssertionAxiom axiom : sub.getAnnotationAssertionAxioms(owlOntology)) {
                String tg = owlObjectToTag(axiom.getProperty());
                if (OboFormatTag.TAG_NAME.getTag().equals(tg)) {
                    name = ((OWLLiteral) axiom.getValue()).getLiteral();
                } else if (OboFormatTag.TAG_SCOPE.getTag().equals(tg)) {
                    scope = owlObjectToTag(axiom.getValue());
                }
            }
            Frame hf = obodoc.getHeaderFrame();
            Clause clause = new Clause(OboFormatTag.TAG_SYNONYMTYPEDEF);
            clause.addValue(this.getIdentifier(sub));
            clause.addValue(name);
            clause.addValue(scope);
            hf.addClause(clause);
            addQualifiers(clause, ax.getAnnotations());
            return;
        } else if (OboFormatTag.TAG_SUBSETDEF.getTag().equals(_tag)) {
            String comment = "";
            for (OWLAnnotationAssertionAxiom axiom : sub.getAnnotationAssertionAxioms(owlOntology)) {
                String tg = owlObjectToTag(axiom.getProperty());
                if (OboFormatTag.TAG_COMMENT.getTag().equals(tg)) {
                    comment = ((OWLLiteral) axiom.getValue()).getLiteral();
                    if (comment != null) break;
                }
            }
            Frame hf = obodoc.getHeaderFrame();
            Clause clause = new Clause(OboFormatTag.TAG_SUBSETDEF);
            clause.addValue(this.getIdentifier(sub));
            clause.addValue(comment);
            hf.addClause(clause);
            addQualifiers(clause, ax.getAnnotations());
            return;
        }
        if (sub instanceof OWLObjectProperty && sup instanceof OWLObjectProperty) {
            String supId = this.getIdentifier(sup);
            if (supId.startsWith("owl:")) {
                return;
            }
            Frame f = getTypedefFrame((OWLEntity) ax.getSubProperty());
            Clause clause = new Clause(OboFormatTag.TAG_IS_A, supId);
            f.addClause(clause);
            addQualifiers(clause, ax.getAnnotations());
        } else {
            error(ax);
        }
    }

    private Pattern absoulteURLPattern = Pattern.compile("<\\s*http.*?>");

    private void tr(OWLAnnotationAssertionAxiom aanAx, Frame frame) {
        boolean success = tr(aanAx.getProperty(), aanAx.getValue(), aanAx.getAnnotations(), frame);
        if (!success) {
            untranslatableAxioms.add(aanAx);
        }
    }

    private boolean tr(OWLAnnotationProperty prop, OWLAnnotationValue annVal, Set<OWLAnnotation> qualifiers, Frame frame) {
        String tagString = owlObjectToTag(prop);
        if (tagString == null) {
            return trGenericPropertyValue(prop, annVal, qualifiers, frame);
        }
        String value = getValue(annVal, tagString);
        OboFormatTag tag = OBOFormatConstants.getTag(tagString);
        if (tag == null) {
            Clause clause = new Clause(OboFormatTag.TAG_PROPERTY_VALUE);
            String propId = this.getIdentifier(prop);
            if (propId.equals("shorthand")) {
                addQualifiers(clause, qualifiers);
            } else {
                clause.addValue(propId);
                clause.addValue(value);
                frame.addClause(clause);
            }
        } else if (value.trim().length() > 0) {
            if (tag == OboFormatTag.TAG_ID) {
                if (frame.getId().equals(value) == false) {
                    error("Conflicting id definitions: 1) " + frame.getId() + "  2)" + value);
                    return false;
                }
                return true;
            }
            Clause clause = new Clause(tag);
            if (tag == OboFormatTag.TAG_DATE) {
                try {
                    clause.addValue(OBOFormatConstants.headerDateFormat.get().parseObject(value));
                } catch (ParseException e) {
                    error("Could not parse date string: " + value);
                    return false;
                }
            } else {
                clause.addValue(value);
            }
            Set<OWLAnnotation> unprocessedQualifiers = new HashSet<OWLAnnotation>(qualifiers);
            if (tag == OboFormatTag.TAG_DEF) {
                for (OWLAnnotation aan : qualifiers) {
                    String propId = owlObjectToTag(aan.getProperty());
                    if ("xref".equals(propId)) {
                        String xrefValue = ((OWLLiteral) aan.getValue()).getLiteral();
                        Xref xref = new Xref(xrefValue);
                        clause.addXref(xref);
                        unprocessedQualifiers.remove(aan);
                    }
                }
            } else if (tag == OboFormatTag.TAG_XREF) {
                Xref xref = new Xref(value);
                for (OWLAnnotation annotation : qualifiers) {
                    if (fac.getRDFSLabel().equals(annotation.getProperty())) {
                        OWLAnnotationValue owlAnnotationValue = annotation.getValue();
                        if (owlAnnotationValue instanceof OWLLiteral) {
                            unprocessedQualifiers.remove(annotation);
                            String xrefAnnotation = ((OWLLiteral) owlAnnotationValue).getLiteral();
                            if (xrefAnnotation != null) {
                                xrefAnnotation = xrefAnnotation.trim();
                                if (xrefAnnotation.length() > 0) {
                                    xref.setAnnotation(xrefAnnotation);
                                }
                            }
                        }
                    }
                }
                clause.setValue(xref);
            } else if (tag == OboFormatTag.TAG_EXACT || tag == OboFormatTag.TAG_NARROW || tag == OboFormatTag.TAG_BROAD || tag == OboFormatTag.TAG_RELATED) {
                clause.setTag(OboFormatTag.TAG_SYNONYM.getTag());
                String scope = tag.getTag();
                String type = null;
                clause.setXrefs(new Vector<Xref>());
                for (OWLAnnotation aan : qualifiers) {
                    String propId = owlObjectToTag(aan.getProperty());
                    if (OboFormatTag.TAG_XREF.getTag().equals(propId)) {
                        String xrefValue = ((OWLLiteral) aan.getValue()).getLiteral();
                        Xref xref = new Xref(xrefValue);
                        clause.addXref(xref);
                        unprocessedQualifiers.remove(aan);
                    } else if (OboFormatTag.TAG_HAS_SYNONYM_TYPE.getTag().equals(propId)) {
                        type = getIdentifier(aan.getValue());
                        unprocessedQualifiers.remove(aan);
                    }
                }
                if (scope != null) {
                    clause.addValue(scope);
                    if (type != null) {
                        clause.addValue(type);
                    }
                }
            }
            addQualifiers(clause, unprocessedQualifiers);
            boolean redundant = false;
            for (Clause frameClause : frame.getClauses()) {
                if (clause.equals(frameClause)) {
                    redundant = handleDuplicateClause(frame, frameClause);
                }
            }
            if (!redundant) {
                frame.addClause(clause);
            }
        } else {
            return false;
        }
        return true;
    }

    /**
	 * Handle a duplicate clause in a frame during translation.
	 * 
	 * @param frame
	 * @param clause
	 * @return true if the clause is to be marked as redundant and will not be added to the
	 */
    protected boolean handleDuplicateClause(Frame frame, Clause clause) {
        LOG.warn("Duplicate clause '" + clause + "' generated in frame: " + frame.getId());
        return true;
    }

    private boolean trGenericPropertyValue(OWLAnnotationProperty prop, OWLAnnotationValue annVal, Set<OWLAnnotation> qualifiers, Frame frame) {
        Clause clause = new Clause();
        clause.setTag(OboFormatTag.TAG_PROPERTY_VALUE.getTag());
        String propId = this.getIdentifier(prop);
        if (propId.equals("shorthand")) {
            addQualifiers(clause, qualifiers);
        } else {
            clause.addValue(propId);
            if (annVal instanceof OWLLiteral) {
                OWLLiteral owlLiteral = (OWLLiteral) annVal;
                clause.addValue(owlLiteral.getLiteral());
                OWLDatatype datatype = owlLiteral.getDatatype();
                IRI dataTypeIri = datatype.getIRI();
                if (!OWL2Datatype.isBuiltIn(dataTypeIri)) {
                    error("Untranslatable axiom due to unknown data type: " + annVal);
                    return false;
                }
                if (dataTypeIri.getStart().equals(Namespaces.XSD.toString())) {
                    clause.addValue("xsd:" + dataTypeIri.getFragment());
                } else if (dataTypeIri.isPlainLiteral()) {
                    clause.addValue("xsd:string");
                } else {
                    clause.addValue(dataTypeIri.toString());
                }
            } else if (annVal instanceof IRI) {
                clause.addValue(getIdentifier((IRI) annVal));
            }
            frame.addClause(clause);
        }
        return true;
    }

    private String getValue(OWLAnnotationValue annVal, String tag) {
        String value = annVal.toString();
        if (annVal instanceof OWLLiteral) {
            value = ((OWLLiteral) annVal).getLiteral();
        } else if (annVal instanceof IRI) {
            value = getIdentifier((IRI) annVal);
        }
        if (OboFormatTag.TAG_EXPAND_EXPRESSION_TO.getTag().equals(tag)) {
            Matcher matcher = absoulteURLPattern.matcher(value);
            while (matcher.find()) {
                String m = matcher.group();
                m = m.replace("<", "");
                m = m.replace(">", "");
                int i = m.lastIndexOf("/");
                m = m.substring(i + 1);
                value = value.replace(matcher.group(), m);
            }
        }
        return value;
    }

    private void addQualifiers(Clause c, Set<OWLAnnotation> qualifiers) {
        for (OWLAnnotation ann : qualifiers) {
            String prop = owlObjectToTag(ann.getProperty());
            if (prop == null) {
                prop = ann.getProperty().getIRI().toString();
            }
            if (prop.equals("gci_relation") || prop.equals("gci_filler") || prop.equals("cardinality") || prop.equals("minCardinality") || prop.equals("maxCardinality")) {
                continue;
            }
            String value = ann.getValue().toString();
            if (ann.getValue() instanceof OWLLiteral) {
                value = ((OWLLiteral) ann.getValue()).getLiteral();
            } else if (ann.getValue() instanceof IRI) {
                value = getIdentifier((IRI) ann.getValue());
            }
            QualifierValue qv = new QualifierValue(prop, value);
            c.addQualifierValue(qv);
        }
    }

    /**
	 * E.g. http://purl.obolibrary.org/obo/go.owl --> "go"
	 * 
	 * if does not match this pattern, then retain original IRI
	 * 
	 * @param ontology
	 * @return The OBO ID of the ontology
	 */
    public static String getOntologyId(OWLOntology ontology) {
        return getOntologyId(ontology.getOntologyID().getOntologyIRI());
    }

    public static String getOntologyId(IRI iriObj) {
        String iri = iriObj.toString();
        String id;
        if (iri.startsWith("http://purl.obolibrary.org/obo/")) {
            id = iri.replace("http://purl.obolibrary.org/obo/", "");
            if (id.endsWith(".owl")) {
                id = id.replaceFirst(".owl$", "");
            }
        } else {
            id = iri;
        }
        return id;
    }

    private void tr(OWLOntology ontology) {
        Frame f = new Frame(FrameType.HEADER);
        this.obodoc.setHeaderFrame(f);
        for (IRI iri : ontology.getDirectImportsDocuments()) {
            Clause c = new Clause();
            c.setTag(OboFormatTag.TAG_IMPORT.getTag());
            c.setValue(iri.toString());
            f.addClause(c);
        }
        String id = getOntologyId(this.owlOntology);
        Clause c = new Clause();
        c.setTag(OboFormatTag.TAG_ONTOLOGY.getTag());
        c.setValue(id);
        f.addClause(c);
        for (OWLAnnotation ann : ontology.getAnnotations()) {
            tr(ann.getProperty(), ann.getValue(), new HashSet<OWLAnnotation>(), f);
        }
    }

    private void tr(OWLEquivalentClassesAxiom ax) {
        List<OWLClassExpression> list = ax.getClassExpressionsAsList();
        OWLClassExpression ce1 = list.get(0);
        OWLClassExpression ce2 = list.get(1);
        String cls2 = this.getIdentifier(ce2);
        Frame f = getTermFrame((OWLEntity) ce1);
        if (f == null) {
            error(ax);
            return;
        }
        boolean isUntranslateable = false;
        List<Clause> equivalenceAxiomClauses = new ArrayList<Clause>();
        if (cls2 != null) {
            Clause c = new Clause();
            c.setTag(OboFormatTag.TAG_EQUIVALENT_TO.getTag());
            c.setValue(cls2);
            f.addClause(c);
            addQualifiers(c, ax.getAnnotations());
        } else if (ce2 instanceof OWLObjectUnionOf) {
            List<OWLClassExpression> list2 = ((OWLObjectUnionOf) ce2).getOperandsAsList();
            for (OWLClassExpression oce : list2) {
                String id = this.getIdentifier(oce);
                Clause c = new Clause();
                c.setTag(OboFormatTag.TAG_UNION_OF.getTag());
                if (id == null) {
                    isUntranslateable = true;
                    error(ax);
                    return;
                } else {
                    c.setValue(id);
                    equivalenceAxiomClauses.add(c);
                }
            }
        } else if (ce2 instanceof OWLObjectIntersectionOf) {
            List<OWLClassExpression> list2 = ((OWLObjectIntersectionOf) ce2).getOperandsAsList();
            for (OWLClassExpression ce : list2) {
                String r = null;
                cls2 = this.getIdentifier(ce);
                if (ce instanceof OWLObjectSomeValuesFrom) {
                    OWLObjectSomeValuesFrom ristriction = (OWLObjectSomeValuesFrom) ce;
                    r = this.getIdentifier(ristriction.getProperty());
                    cls2 = this.getIdentifier(ristriction.getFiller());
                }
                if (cls2 != null) {
                    Clause c = new Clause();
                    c.setTag(OboFormatTag.TAG_INTERSECTION_OF.getTag());
                    if (r != null) c.addValue(r);
                    c.addValue(cls2);
                    equivalenceAxiomClauses.add(c);
                } else if (f.getClauses(OboFormatTag.TAG_INTERSECTION_OF).size() > 0) {
                    error("The axiom is not translated (maximimum one IntersectionOf EquivalenceAxiom)", ax);
                } else {
                    isUntranslateable = true;
                    error(ax);
                }
            }
        }
        if (!isUntranslateable) {
            for (Clause c : equivalenceAxiomClauses) {
                f.addClause(c);
            }
        }
    }

    private void tr(OWLDisjointClassesAxiom ax) {
        List<OWLClassExpression> list = ax.getClassExpressionsAsList();
        String cls2 = this.getIdentifier(list.get(1));
        if (cls2 == null) {
            error(ax);
            return;
        }
        Frame f = getTermFrame((OWLEntity) list.get(0));
        Clause c = new Clause();
        c.setTag(OboFormatTag.TAG_DISJOINT_FROM.getTag());
        c.setValue(cls2);
        f.addClause(c);
    }

    private void tr(OWLDeclarationAxiom axiom) {
        OWLEntity entity = axiom.getEntity();
        Set<OWLAnnotationAssertionAxiom> set = entity.getAnnotationAssertionAxioms(this.owlOntology);
        if (set.isEmpty()) return;
        Frame f = null;
        if (entity instanceof OWLClass) {
            f = getTermFrame(entity);
        } else if (entity instanceof OWLObjectProperty) {
            f = getTypedefFrame(entity);
        } else if (entity instanceof OWLAnnotationProperty) {
            for (OWLAnnotationAssertionAxiom ax : set) {
                OWLAnnotationProperty prop = ax.getProperty();
                String tag = owlObjectToTag(prop);
                if (OboFormatTag.TAG_IS_METADATA_TAG.getTag().equals(tag)) {
                    f = getTypedefFrame(entity);
                    break;
                }
            }
        }
        if (f != null) {
            for (OWLAnnotationAssertionAxiom aanAx : set) {
                tr(aanAx, f);
            }
            add(f);
            return;
        }
    }

    public String getIdentifier(OWLObject obj) {
        try {
            return getIdentifierFromObject(obj, this.owlOntology);
        } catch (UntranslatableAxiomException e) {
            error(e.getMessage());
        }
        return null;
    }

    public static class UntranslatableAxiomException extends Exception {

        private static final long serialVersionUID = 4674805484349471665L;

        public UntranslatableAxiomException(String message, Throwable cause) {
            super(message, cause);
        }

        public UntranslatableAxiomException(String message) {
            super(message);
        }
    }

    /**
	 * Retrieve the identifier for a given {@link OWLObject}. 
	 * This methods uses also shorthand hints to resolve the 
	 * identifier. 
	 * Should the translation process encounter a problem or 
	 * not find an identifier the defaultValue is returned.
	 * 
	 * @param obj the {@link OWLObject} to resolve
	 * @param ont the target ontology
	 * @param defaultValue the value to return in case of an error or no id
	 * @return identifier or the default value
	 */
    public static String getIdentifierFromObject(OWLObject obj, OWLOntology ont, String defaultValue) {
        String id = defaultValue;
        try {
            id = getIdentifierFromObject(obj, ont);
            if (id == null) {
                id = defaultValue;
            }
        } catch (UntranslatableAxiomException e) {
            LOG.warn(e.getMessage());
        }
        return id;
    }

    /**
	 * Retrieve the identifier for a given {@link OWLObject}. 
	 * This methods uses also shorthand hints to resolve the 
	 * identifier. 
	 * Should the translation process encounter an unexpected 
	 * axiom an {@link UntranslatableAxiomException} is thrown.
	 * 
	 * @param obj the {@link OWLObject} to resolve
	 * @param ont the target ontology
	 * @return identifier or null
	 * @throws UntranslatableAxiomException
	 */
    public static String getIdentifierFromObject(OWLObject obj, OWLOntology ont) throws UntranslatableAxiomException {
        if (obj instanceof OWLObjectProperty) {
            OWLObjectProperty prop = (OWLObjectProperty) obj;
            for (OWLAnnotationAssertionAxiom ax : prop.getAnnotationAssertionAxioms(ont)) {
                String propId = getIdentifierFromObject(ax.getProperty(), ont);
                if (propId.equals("shorthand")) {
                    final OWLAnnotationValue value = ax.getValue();
                    if (value != null && value instanceof OWLLiteral) {
                        return ((OWLLiteral) value).getLiteral();
                    }
                    throw new UntranslatableAxiomException("Untranslatable axiom, expected literal value, but was: " + value + " in axiom: " + ax);
                }
            }
        }
        if (obj instanceof OWLEntity) return getIdentifier(((OWLEntity) obj).getIRI());
        if (obj instanceof IRI) return getIdentifier((IRI) obj);
        return null;
    }

    /**
	 * See table 5.9.2. Translation of identifiers
	 * 
	 * @param iriId
	 * @return obo identifier or null
	 */
    public static String getIdentifier(IRI iriId) {
        String id = _getIdentifier(iriId);
        return id;
    }

    private static String _getIdentifier(IRI iriId) {
        if (iriId == null) return null;
        String iri = iriId.toString();
        int indexSlash = iri.lastIndexOf("/");
        String prefixURI = null;
        String id = null;
        if (indexSlash > -1) {
            prefixURI = iri.substring(0, indexSlash + 1);
            id = iri.substring(indexSlash + 1);
        } else id = iri;
        String s[] = id.split("#_");
        if (s.length > 1) {
            return s[0] + ":" + s[1];
        }
        s = id.split("#");
        if (s.length > 1) {
            String prefix = "";
            if ("owl".equals(s[0]) || "rdf".equals(s[0]) || "rdfs".equals(s[0])) {
                prefix = s[0] + ":";
            }
            return prefix + s[1];
        }
        s = id.split("_");
        if (s.length == 2 && !id.contains("#") && !s[1].contains("_")) {
            String localId = java.net.URLDecoder.decode(s[1]);
            return s[0] + ":" + localId;
        }
        if (s.length > 2 && !id.contains("#")) {
            if (s[s.length - 1].replaceAll("[0-9]", "").length() == 0) {
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < s.length; i++) {
                    if (i > 0) {
                        if (i == s.length - 1) {
                            sb.append(":");
                        } else {
                            sb.append("_");
                        }
                    }
                    sb.append(s[i]);
                }
                return sb.toString();
            }
        }
        return iri;
    }

    public static String owlObjectToTag(OWLObject obj) {
        IRI iriObj = null;
        if ((obj instanceof OWLNamedObject)) {
            iriObj = ((OWLNamedObject) obj).getIRI();
        } else if (obj instanceof IRI) {
            iriObj = (IRI) obj;
        }
        if (iriObj == null) return null;
        String iri = iriObj.toString();
        String tag = annotationPropertyMap.get(iri);
        if (tag == null) {
            if (iri.startsWith(Obo2OWLConstants.DEFAULT_IRI_PREFIX + "IAO_")) {
                String legacyId = iri.replace(Obo2OWLConstants.DEFAULT_IRI_PREFIX, "");
                if (legacyId.equals("IAO_xref")) {
                    return OboFormatTag.TAG_XREF.getTag();
                }
                if (legacyId.equals("IAO_id")) {
                    return OboFormatTag.TAG_ID.getTag();
                }
                if (legacyId.equals("IAO_namespace")) {
                    return OboFormatTag.TAG_NAMESPACE.getTag();
                }
            }
            String prefix = Obo2OWLConstants.OIOVOCAB_IRI_PREFIX;
            if (iri.startsWith(prefix)) {
                tag = iri.substring(prefix.length());
            }
        }
        return tag;
    }

    private Frame getTermFrame(OWLEntity entity) {
        String id = getIdentifier(entity.getIRI());
        Frame f = this.obodoc.getTermFrame(id);
        if (f == null) {
            f = new Frame(FrameType.TERM);
            f.setId(id);
            f.addClause(new Clause(OboFormatTag.TAG_ID, id));
            add(f);
        }
        return f;
    }

    private Frame getTypedefFrame(OWLEntity entity) {
        String id = this.getIdentifier(entity);
        Frame f = this.obodoc.getTypedefFrame(id);
        if (f == null) {
            f = new Frame(FrameType.TYPEDEF);
            f.setId(id);
            f.addClause(new Clause(OboFormatTag.TAG_ID, id));
            add(f);
        }
        return f;
    }

    private void tr(OWLClassAssertionAxiom ax) {
    }

    private void tr(OWLSubClassOfAxiom ax) {
        OWLClassExpression sub = ax.getSubClass();
        OWLClassExpression sup = ax.getSuperClass();
        Set<QualifierValue> qvs = new HashSet<QualifierValue>();
        boolean isRewrittenToGCI = false;
        if (sub instanceof OWLObjectIntersectionOf) {
            Set<OWLClassExpression> xs = ((OWLObjectIntersectionOf) sub).getOperands();
            if (xs.size() == 2) {
                OWLClass c = null;
                OWLObjectSomeValuesFrom r = null;
                OWLObjectProperty p = null;
                OWLClass filler = null;
                for (OWLClassExpression x : xs) {
                    if (x instanceof OWLClass) c = (OWLClass) x;
                    if (x instanceof OWLObjectSomeValuesFrom) {
                        r = (OWLObjectSomeValuesFrom) x;
                        if (r.getProperty() instanceof OWLObjectProperty) {
                            if (r.getFiller() instanceof OWLClass) {
                                p = (OWLObjectProperty) r.getProperty();
                                filler = (OWLClass) r.getFiller();
                            }
                        }
                    }
                }
                if (c != null && p != null && filler != null) {
                    isRewrittenToGCI = true;
                    sub = c;
                    qvs.add(new QualifierValue("gci_relation", getIdentifier(p)));
                    qvs.add(new QualifierValue("gci_filler", getIdentifier(filler)));
                }
            }
        }
        if (sub instanceof OWLClass) {
            Frame f = getTermFrame((OWLEntity) sub);
            if (sup instanceof OWLClass) {
                Clause c = new Clause();
                c.setTag(OboFormatTag.TAG_IS_A.getTag());
                c.setValue(this.getIdentifier(sup));
                c.setQualifierValues(qvs);
                f.addClause(c);
                addQualifiers(c, ax.getAnnotations());
            } else if (sup instanceof OWLQuantifiedObjectRestriction) {
                OWLQuantifiedObjectRestriction r = (OWLQuantifiedObjectRestriction) sup;
                String fillerId = this.getIdentifier(r.getFiller());
                if (fillerId == null) {
                    error(ax);
                    return;
                }
                f.addClause(createRelationshipClauseWithRestrictions(r, fillerId, qvs, ax));
            } else if (sup instanceof OWLObjectCardinalityRestriction) {
                OWLObjectCardinalityRestriction cardinality = (OWLObjectCardinalityRestriction) sup;
                String fillerId = this.getIdentifier(cardinality.getFiller());
                if (fillerId == null) {
                    error(ax);
                    return;
                }
                f.addClause(createRelationshipClauseWithCardinality(cardinality, fillerId, qvs, ax));
            } else if (sup instanceof OWLObjectIntersectionOf) {
                OWLObjectIntersectionOf i = (OWLObjectIntersectionOf) sup;
                List<Clause> clauses = new ArrayList<Clause>();
                for (OWLClassExpression operand : i.getOperands()) {
                    if (operand instanceof OWLQuantifiedObjectRestriction) {
                        OWLQuantifiedObjectRestriction restriction = (OWLQuantifiedObjectRestriction) operand;
                        String fillerId = this.getIdentifier(restriction.getFiller());
                        if (fillerId == null) {
                            error(ax);
                            return;
                        }
                        clauses.add(createRelationshipClauseWithRestrictions(restriction, fillerId, new HashSet<QualifierValue>(qvs), ax));
                    } else if (operand instanceof OWLObjectCardinalityRestriction) {
                        OWLObjectCardinalityRestriction restriction = (OWLObjectCardinalityRestriction) operand;
                        String fillerId = this.getIdentifier(restriction.getFiller());
                        if (fillerId == null) {
                            error(ax);
                            return;
                        }
                        clauses.add(createRelationshipClauseWithCardinality(restriction, fillerId, new HashSet<QualifierValue>(qvs), ax));
                    } else {
                        error(ax);
                        return;
                    }
                }
                if (clauses.isEmpty()) {
                    error(ax);
                    return;
                }
                clauses = normalizeRelationshipClauses(clauses);
                for (Clause clause : clauses) {
                    f.addClause(clause);
                }
            } else {
                error(ax);
                return;
            }
        } else {
            error(ax);
            return;
        }
    }

    private Clause createRelationshipClauseWithRestrictions(OWLQuantifiedObjectRestriction r, String fillerId, Set<QualifierValue> qvs, OWLSubClassOfAxiom ax) {
        Clause c = new Clause();
        c.setTag(OboFormatTag.TAG_RELATIONSHIP.getTag());
        c.addValue(this.getIdentifier(r.getProperty()));
        c.addValue(fillerId);
        c.setQualifierValues(qvs);
        addQualifiers(c, ax.getAnnotations());
        return c;
    }

    private Clause createRelationshipClauseWithCardinality(OWLObjectCardinalityRestriction restriction, String fillerId, Set<QualifierValue> qvs, OWLSubClassOfAxiom ax) {
        Clause c = new Clause();
        c.setTag(OboFormatTag.TAG_RELATIONSHIP.getTag());
        c.addValue(this.getIdentifier(restriction.getProperty()));
        c.addValue(fillerId);
        c.setQualifierValues(qvs);
        String q = "cardinality";
        if (restriction instanceof OWLObjectMinCardinality) {
            q = "minCardinality";
        } else if (restriction instanceof OWLObjectMaxCardinality) {
            q = "maxCardinality";
        }
        c.addQualifierValue(new QualifierValue(q, Integer.toString(restriction.getCardinality())));
        addQualifiers(c, ax.getAnnotations());
        return c;
    }

    /**
	 * Join clauses and its {@link QualifierValue} which have the same 
	 * relationship type and target. Try to resolve conflicts for multiple
	 * statements. E.g., min=2 and min=3 is resolved to min=2, or  max=2 and max=4 
	 * is resolved to max=4. It will not merge conflicting exact cardinality statements.
	 * 
	 * TODO How to merge "all_some", and "all_only"?
	 * 
	 * @param clauses
	 * @return normalized list of {@link Clause}
	 */
    private List<Clause> normalizeRelationshipClauses(List<Clause> clauses) {
        final List<Clause> normalized = new ArrayList<Clause>();
        while (!clauses.isEmpty()) {
            final Clause target = clauses.remove(0);
            List<Clause> similar = findSimilarClauses(clauses, target);
            normalized.add(target);
            mergeSimilarIntoTarget(target, similar);
        }
        return normalized;
    }

    private List<Clause> findSimilarClauses(List<Clause> clauses, final Clause target) {
        final String targetTag = target.getTag();
        final Object targetValue = target.getValue();
        final Object targetValue2 = target.getValue2();
        List<Clause> similar = new ArrayList<Clause>();
        Iterator<Clause> iterator = clauses.iterator();
        while (iterator.hasNext()) {
            final Clause current = iterator.next();
            final Object currentValue = current.getValue();
            final Object currentValue2 = current.getValue2();
            if (targetTag.equals(current.getTag()) && targetValue.equals(currentValue)) {
                if (targetValue2 == null) {
                    if (currentValue2 == null) {
                        similar.add(current);
                        iterator.remove();
                    }
                } else if (targetValue2.equals(currentValue2)) {
                    similar.add(current);
                    iterator.remove();
                }
            }
        }
        return similar;
    }

    private void mergeSimilarIntoTarget(final Clause target, List<Clause> similar) {
        if (similar.isEmpty()) {
            return;
        }
        final Collection<QualifierValue> targetQVs = target.getQualifierValues();
        for (Clause current : similar) {
            final Collection<QualifierValue> newQVs = current.getQualifierValues();
            for (QualifierValue newQV : newQVs) {
                final String newQualifier = newQV.getQualifier();
                if ("minCardinality".equals(newQualifier) || "maxCardinality".equals(newQualifier)) {
                    QualifierValue match = findMatchingQualifierValue(newQV, targetQVs);
                    if (match != null) {
                        mergeQualifierValues(match, newQV);
                    } else {
                        target.addQualifierValue(newQV);
                    }
                } else {
                    target.addQualifierValue(newQV);
                }
            }
        }
    }

    private QualifierValue findMatchingQualifierValue(QualifierValue query, Collection<QualifierValue> list) {
        String queryQualifier = query.getQualifier();
        for (QualifierValue qv : list) {
            if (queryQualifier.equals(qv.getQualifier())) {
                return qv;
            }
        }
        return null;
    }

    private void mergeQualifierValues(QualifierValue target, QualifierValue newQV) {
        if (!target.getValue().equals(newQV.getValue())) {
            if ("minCardinality".equals(target.getQualifier())) {
                int currentValue = Integer.parseInt(target.getValue().toString());
                int newValue = Integer.parseInt(newQV.getValue().toString());
                int mergedValue = Math.min(currentValue, newValue);
                target.setValue(Integer.toString(mergedValue));
            } else if ("maxCardinality".equals(target.getQualifier())) {
                int currentValue = Integer.parseInt(target.getValue().toString());
                int newValue = Integer.parseInt(newQV.getValue().toString());
                int mergedValue = Math.max(currentValue, newValue);
                target.setValue(Integer.toString(mergedValue));
            }
        }
    }

    private void error(String message, OWLAxiom ax) {
        untranslatableAxioms.add(ax);
        error(message + ax);
    }

    private void error(OWLAxiom ax) {
        untranslatableAxioms.add(ax);
        error("the axiom is not translated : " + ax);
    }

    private void error(String message) {
        LOG.warn(message);
        if (strictConversion) throw new RuntimeException("The conversion is halted: " + message);
    }
}
