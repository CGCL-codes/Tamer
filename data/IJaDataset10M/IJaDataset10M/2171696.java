package org.stlab.xd.specialization;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.semanticweb.owlapi.model.OWLAnonymousClassExpression;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLPropertyExpression;
import org.semanticweb.owlapi.model.OWLPropertyRange;
import org.semanticweb.owlapi.model.OWLQuantifiedRestriction;
import org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;

/**
 * <p>
 * To generate candidate axioms related to a statement to be done in an
 * Specialization. For example, in a specialization operation, some new entities
 * are subclasses or subproperties of a super entity. Which possible additional
 * restrictions could be proposed?
 * 
 * </p>
 * <p>
 * These are the rules followed by to generate <i>candidate</i> axioms for
 * Classes:
 * <ul>
 * <li><i>IF</i> SubClassOf(SuperClass,SomeValuesFrom(X,Y) <i>MAYBE</i>
 * SubClassOf(SubClass,SomeValuesFrom(bottomX,bottomY)</li>
 * <li><i>IF</i> SubClassOf(SuperClass,AllValuesFrom(X,Y) <i>MAYBE</i>
 * SubClassOf(SubClass,AllValuesFrom(bottomX,bottomY)</li>
 * <li><i>IF</i> SubClass(SuperClass,X) AND SubClass(SuperClass,Y) <i>MAYBE</i>
 * Disjoint(X,Y)</li>
 * </ul>
 * 
 * These are the rules followed by to generate <i>candidate</i> axioms for
 * ObjectProperty:
 * <ul>
 * <li><i>IF</i> Domain(superProperty,X) <i>MAYBE</i> Domain(subProperty,subX)</li>
 * <li><i>IF</i> Range(superProperty,X) <i>MAYBE</i> Range(subProperty,subX)</li>
 * <li><i>IF</i> SubProperty(superProperty,X) AND SubProperty(superProperty,Y)
 * <i>MAYBE</i> Disjoint(X,Y)</li>
 * </ul>
 * 
 * These are the rules followed by to generate <i>candidate</i> axioms for
 * DataProperty:
 * <ul>
 * <li><i>IF</i> SubProperty(superProperty,X) AND SubProperty(superProperty,Y)
 * <i>MAYBE</i> Disjoint(X,Y)</li>
 * </ul>
 * 
 * These are the rules followed by to generate <i>candidate</i> axioms for
 * AnnotationProperty:
 * <ul>
 * <li><i>IF</i> SubProperty(superProperty,X) AND SubProperty(superProperty,Y)
 * <i>MAYBE</i> Disjoint(X,Y)</li>
 * </ul>
 *</p>
 */
class CandidateAxiomsVisitor extends OWLAxiomVisitorAdapter {

    private Set<OWLAxiom> candidates;

    private Map<OWLEntity, Set<OWLEntity>> superSubofMap;

    private Map<OWLEntity, Set<OWLEntity>> subofSuperMap;

    private Map<OWLPropertyExpression<?, ?>, Set<OWLPropertyRange>> propertyDomain;

    private Map<OWLPropertyExpression<?, ?>, Set<OWLPropertyRange>> propertyRange;

    private OWLDataFactory factory;

    private void setup(Set<OWLAxiom> axioms, OWLDataFactory factory) {
        subofSuperMap = new HashMap<OWLEntity, Set<OWLEntity>>();
        superSubofMap = new HashMap<OWLEntity, Set<OWLEntity>>();
        propertyDomain = new HashMap<OWLPropertyExpression<?, ?>, Set<OWLPropertyRange>>();
        propertyRange = new HashMap<OWLPropertyExpression<?, ?>, Set<OWLPropertyRange>>();
        candidates = new HashSet<OWLAxiom>();
        this.factory = factory;
        for (OWLAxiom a : axioms) {
            if (a instanceof OWLObjectPropertyRangeAxiom) {
                OWLObjectPropertyRangeAxiom pra = (OWLObjectPropertyRangeAxiom) a;
                OWLPropertyExpression<?, ?> p = pra.getProperty();
                OWLPropertyRange r = pra.getRange();
                if (propertyRange.containsKey(p)) {
                    propertyRange.get(p).add(r);
                } else {
                    Set<OWLPropertyRange> v = new HashSet<OWLPropertyRange>();
                    v.add(r);
                    propertyRange.put(p, v);
                }
            }
            if (a instanceof OWLDataPropertyRangeAxiom) {
                OWLDataPropertyRangeAxiom pra = (OWLDataPropertyRangeAxiom) a;
                OWLPropertyExpression<?, ?> p = pra.getProperty();
                OWLPropertyRange r = pra.getRange();
                if (propertyRange.containsKey(p)) {
                    propertyRange.get(p).add(r);
                } else {
                    Set<OWLPropertyRange> v = new HashSet<OWLPropertyRange>();
                    v.add(r);
                    propertyRange.put(p, v);
                }
            }
            if (a instanceof OWLObjectPropertyDomainAxiom) {
                OWLObjectPropertyDomainAxiom pra = (OWLObjectPropertyDomainAxiom) a;
                OWLPropertyExpression<?, ?> p = pra.getProperty();
                OWLPropertyRange r = pra.getDomain();
                if (propertyDomain.containsKey(p)) {
                    Set<OWLPropertyRange> s = propertyDomain.get(p);
                    s.add(r);
                } else {
                    Set<OWLPropertyRange> v = new HashSet<OWLPropertyRange>();
                    v.add(r);
                    propertyDomain.put(p, v);
                }
            }
            if (a instanceof OWLDataPropertyDomainAxiom) {
                OWLDataPropertyDomainAxiom pra = (OWLDataPropertyDomainAxiom) a;
                OWLPropertyExpression<?, ?> p = pra.getProperty();
                OWLPropertyRange r = pra.getDomain();
                if (propertyDomain.containsKey(p)) {
                    propertyDomain.get(p).add(r);
                } else {
                    Set<OWLPropertyRange> v = new HashSet<OWLPropertyRange>();
                    v.add(r);
                    propertyDomain.put(p, v);
                }
            }
            if (a instanceof OWLSubClassOfAxiom) {
                OWLSubClassOfAxiom axiom = (OWLSubClassOfAxiom) a;
                if (axiom.getSubClass() instanceof OWLEntity && axiom.getSuperClass() instanceof OWLEntity) {
                    if (superSubofMap.containsKey((OWLEntity) axiom.getSuperClass())) {
                        superSubofMap.get((OWLEntity) axiom.getSuperClass()).add((OWLEntity) axiom.getSubClass());
                    } else {
                        HashSet<OWLEntity> vl = new HashSet<OWLEntity>();
                        vl.add((OWLEntity) axiom.getSubClass());
                        CandidateAxiomsVisitor.this.superSubofMap.put((OWLEntity) axiom.getSuperClass(), vl);
                    }
                }
            } else if (a instanceof OWLSubObjectPropertyOfAxiom) {
                OWLSubObjectPropertyOfAxiom axiom = (OWLSubObjectPropertyOfAxiom) a;
                if (axiom.getSubProperty() instanceof OWLEntity && axiom.getSuperProperty() instanceof OWLEntity) {
                    if (superSubofMap.containsKey((OWLEntity) axiom.getSuperProperty())) {
                        superSubofMap.get((OWLEntity) axiom.getSuperProperty()).add((OWLEntity) axiom.getSubProperty());
                    } else {
                        HashSet<OWLEntity> vl = new HashSet<OWLEntity>();
                        vl.add((OWLEntity) axiom.getSubProperty());
                        CandidateAxiomsVisitor.this.superSubofMap.put((OWLEntity) axiom.getSuperProperty(), vl);
                    }
                }
            } else if (a instanceof OWLSubDataPropertyOfAxiom) {
                OWLSubDataPropertyOfAxiom axiom = (OWLSubDataPropertyOfAxiom) a;
                if (axiom.getSubProperty() instanceof OWLEntity && axiom.getSuperProperty() instanceof OWLEntity) {
                    if (superSubofMap.containsKey((OWLEntity) axiom.getSuperProperty())) {
                        superSubofMap.get((OWLEntity) axiom.getSuperProperty()).add((OWLEntity) axiom.getSubProperty());
                    } else {
                        HashSet<OWLEntity> vl = new HashSet<OWLEntity>();
                        vl.add((OWLEntity) axiom.getSubProperty());
                        CandidateAxiomsVisitor.this.superSubofMap.put((OWLEntity) axiom.getSuperProperty(), vl);
                    }
                }
            } else if (a instanceof OWLSubAnnotationPropertyOfAxiom) {
                OWLSubAnnotationPropertyOfAxiom axiom = (OWLSubAnnotationPropertyOfAxiom) a;
                if (axiom.getSubProperty() instanceof OWLEntity && axiom.getSuperProperty() instanceof OWLEntity) {
                    if (superSubofMap.containsKey((OWLEntity) axiom.getSuperProperty())) {
                        superSubofMap.get((OWLEntity) axiom.getSuperProperty()).add((OWLEntity) axiom.getSubProperty());
                    } else {
                        HashSet<OWLEntity> vl = new HashSet<OWLEntity>();
                        vl.add((OWLEntity) axiom.getSubProperty());
                        CandidateAxiomsVisitor.this.superSubofMap.put((OWLEntity) axiom.getSuperProperty(), vl);
                    }
                }
            }
        }
        for (Entry<OWLEntity, Set<OWLEntity>> entry : superSubofMap.entrySet()) {
            Set<OWLEntity> subof = entry.getValue();
            OWLEntity superof = entry.getKey();
            for (OWLEntity sub : subof) {
                if (subofSuperMap.containsKey(sub)) {
                    subofSuperMap.get(sub).add(superof);
                } else {
                    Set<OWLEntity> s = new HashSet<OWLEntity>();
                    s.add(superof);
                    subofSuperMap.put(sub, s);
                }
            }
        }
    }

    /**
	 * Only OWLSubXOf are token into account as input
	 * 
	 * @param subsumptions
	 */
    public CandidateAxiomsVisitor(Set<OWLAxiom> subsumptions, OWLDataFactory factory) {
        this.setup(subsumptions, factory);
    }

    Set<OWLEntity> getSub(Object obj) {
        if (obj instanceof OWLEntity) return getSub((OWLEntity) obj); else return new HashSet<OWLEntity>();
    }

    Set<OWLEntity> getSub(OWLEntity entity) {
        Set<OWLEntity> toRet = new HashSet<OWLEntity>();
        if (superSubofMap.get(entity) instanceof Set<?>) for (OWLEntity en : superSubofMap.get(entity)) {
            toRet.add(en);
            if (getSub(en) instanceof Set<?>) toRet.addAll(getSub(en));
        }
        return toRet;
    }

    /**
	 * Clear the set from super entities leaving only bottom level entities
	 * 
	 * @param entities
	 * @return
	 */
    Set<OWLEntity> onlyBottomEntities(Set<OWLEntity> entities) {
        Set<OWLEntity> removable = new HashSet<OWLEntity>();
        for (OWLEntity e : entities) {
            if (subofSuperMap.containsValue(e)) {
                removable.add(e);
            }
        }
        entities.removeAll(removable);
        return entities;
    }

    Set<OWLEntity> bottomLevelSpecializations(Object entity) {
        if (entity instanceof OWLEntity) return bottomLevelSpecializations((OWLEntity) entity); else return new HashSet<OWLEntity>();
    }

    Set<OWLEntity> bottomLevelSpecializations(OWLEntity entity) {
        Set<OWLEntity> allSpecializations = getSub(entity);
        return onlyBottomEntities(allSpecializations);
    }

    /**
	 */
    @Override
    public void visit(OWLSubClassOfAxiom axiom) {
        if (this.superSubofMap.containsKey(axiom.getSubClass())) {
            Set<OWLEntity> specializedClasses = bottomLevelSpecializations((OWLEntity) axiom.getSubClass());
            for (OWLEntity subclass : specializedClasses) {
                try {
                    OWLClassExpression newSubClass = (OWLClassExpression) subclass;
                    OWLClassExpression expression = axiom.getSuperClass();
                    if (expression instanceof OWLAnonymousClassExpression) {
                        if (expression instanceof OWLQuantifiedRestriction<?, ?, ?>) {
                            Object property_o = ((OWLQuantifiedRestriction<?, ?, ?>) expression).getProperty();
                            Object filler_o = ((OWLQuantifiedRestriction<?, ?, ?>) expression).getFiller();
                            Set<OWLEntity> property_s = null;
                            if (superSubofMap.containsKey(property_o)) {
                                property_s = bottomLevelSpecializations(property_o);
                            } else property_s = new HashSet<OWLEntity>();
                            Set<OWLPropertyRange> filler_s = new HashSet<OWLPropertyRange>();
                            if (superSubofMap.containsKey(filler_o)) {
                                Set<OWLEntity> filler_e_s = bottomLevelSpecializations(filler_o);
                                for (OWLEntity entity : filler_e_s) {
                                    if (entity instanceof OWLPropertyRange) {
                                        filler_s.add((OWLPropertyRange) entity);
                                    }
                                }
                            }
                            if (property_s.isEmpty() && filler_s.isEmpty()) return;
                            if (property_s.isEmpty()) {
                                if (property_o instanceof OWLEntity) {
                                    property_s.add((OWLEntity) property_o);
                                } else {
                                    return;
                                }
                            }
                            OWLPropertyRange base_filler = null;
                            if (filler_s.isEmpty()) {
                                base_filler = (OWLPropertyRange) filler_o;
                            }
                            Set<OWLQuantifiedRestriction<?, ?, ?>> restrictions = new HashSet<OWLQuantifiedRestriction<?, ?, ?>>();
                            for (OWLEntity property : property_s) {
                                if (filler_s.isEmpty()) {
                                    OWLQuantifiedRestriction<?, ?, ?> res = refactorRestriction((OWLQuantifiedRestriction<?, ?, ?>) expression, (OWLPropertyExpression<?, ?>) property, base_filler);
                                    if (res != null) restrictions.add(res);
                                } else for (OWLPropertyRange filler : filler_s) {
                                    OWLQuantifiedRestriction<?, ?, ?> res = refactorRestriction((OWLQuantifiedRestriction<?, ?, ?>) expression, (OWLPropertyExpression<?, ?>) property, filler);
                                    if (res != null) restrictions.add(res);
                                }
                            }
                            for (OWLQuantifiedRestriction<?, ?, ?> restriction : restrictions) {
                                try {
                                    OWLSubClassOfAxiom scoa = factory.getOWLSubClassOfAxiom(newSubClass, restriction);
                                    candidates.add(scoa);
                                } catch (Exception e) {
                                    String outString = "Sub: " + subclass.toString();
                                    outString += " Super: " + expression.toString();
                                    outString += " Quantified restriction: " + restriction;
                                    System.err.println("Cannot build subClassOf axiom. " + outString);
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private OWLQuantifiedRestriction<?, ?, ?> refactorRestriction(OWLQuantifiedRestriction<?, ?, ?> prototype, OWLPropertyExpression<?, ?> property, OWLPropertyRange filler) {
        if (prototype instanceof OWLObjectAllValuesFrom && property instanceof OWLObjectPropertyExpression && filler instanceof OWLClassExpression) {
            return factory.getOWLObjectAllValuesFrom((OWLObjectPropertyExpression) property, (OWLClassExpression) filler);
        } else if (prototype instanceof OWLDataAllValuesFrom && property instanceof OWLDataPropertyExpression && filler instanceof OWLDataRange) {
            return factory.getOWLDataAllValuesFrom((OWLDataPropertyExpression) property, (OWLDataRange) filler);
        } else if (prototype instanceof OWLObjectSomeValuesFrom && property instanceof OWLObjectPropertyExpression && filler instanceof OWLClassExpression) {
            return factory.getOWLObjectSomeValuesFrom((OWLObjectPropertyExpression) property, (OWLClassExpression) filler);
        } else if (prototype instanceof OWLDataSomeValuesFrom && property instanceof OWLDataPropertyExpression && filler instanceof OWLDataRange) {
            return factory.getOWLDataSomeValuesFrom((OWLDataPropertyExpression) property, (OWLDataRange) filler);
        }
        return null;
    }

    @Override
    public void visit(OWLDataPropertyDomainAxiom axiom) {
        OWLClassExpression domain = axiom.getDomain();
        OWLDataPropertyExpression property = axiom.getProperty();
        for (OWLEntity subp : getSub(property)) {
            if (propertyDomain.containsKey(subp)) return;
        }
        Set<OWLEntity> bottomDomains = bottomLevelSpecializations(domain);
        Set<OWLEntity> bottomProperties = bottomLevelSpecializations(property);
        if (bottomDomains.isEmpty() && bottomProperties.isEmpty()) return;
        if (bottomDomains.isEmpty()) {
            if (domain instanceof OWLEntity) bottomDomains.add((OWLEntity) domain); else return;
        }
        if (bottomProperties.isEmpty()) {
            if (property instanceof OWLEntity) bottomProperties.add((OWLEntity) property);
        }
        for (OWLEntity d : bottomDomains) {
            for (OWLEntity p : bottomProperties) {
                if (d instanceof OWLClassExpression && p instanceof OWLDataPropertyExpression) candidates.add(factory.getOWLDataPropertyDomainAxiom((OWLDataPropertyExpression) p, (OWLClassExpression) d));
            }
        }
    }

    @Override
    public void visit(OWLDataPropertyRangeAxiom axiom) {
        OWLDataRange range = axiom.getRange();
        OWLDataPropertyExpression property = axiom.getProperty();
        Set<OWLEntity> bottomRanges = bottomLevelSpecializations(range);
        Set<OWLEntity> bottomProperties = bottomLevelSpecializations(property);
        for (OWLEntity subp : getSub(property)) {
            if (propertyRange.containsKey(subp)) return;
        }
        if (bottomRanges.isEmpty() && bottomProperties.isEmpty()) return;
        if (bottomRanges.isEmpty()) {
            if (range instanceof OWLEntity) bottomRanges.add((OWLEntity) range); else return;
        }
        if (bottomProperties.isEmpty()) {
            if (property instanceof OWLEntity) bottomProperties.add((OWLEntity) property);
        }
        for (OWLEntity r : bottomRanges) {
            for (OWLEntity p : bottomProperties) {
                if (r instanceof OWLDataRange && p instanceof OWLDataPropertyExpression) candidates.add(factory.getOWLDataPropertyRangeAxiom((OWLDataPropertyExpression) p, (OWLDataRange) r));
            }
        }
    }

    @Override
    public void visit(OWLObjectPropertyDomainAxiom axiom) {
        OWLClassExpression domain = axiom.getDomain();
        OWLObjectPropertyExpression property = axiom.getProperty();
        Set<OWLEntity> bottomDomains = bottomLevelSpecializations(domain);
        Set<OWLEntity> bottomProperties = bottomLevelSpecializations(property);
        for (OWLEntity subp : getSub(property)) {
            if (propertyDomain.containsKey(subp)) return;
        }
        if (bottomDomains.isEmpty() && bottomProperties.isEmpty()) return;
        if (bottomDomains.isEmpty()) {
            if (domain instanceof OWLEntity) bottomDomains.add((OWLEntity) domain); else return;
        }
        if (bottomProperties.isEmpty()) {
            if (property instanceof OWLEntity) bottomProperties.add((OWLEntity) property);
        }
        for (OWLEntity d : bottomDomains) {
            for (OWLEntity p : bottomProperties) {
                if (d instanceof OWLClassExpression && p instanceof OWLObjectPropertyExpression) candidates.add(factory.getOWLObjectPropertyDomainAxiom((OWLObjectPropertyExpression) p, (OWLClassExpression) d));
            }
        }
    }

    @Override
    public void visit(OWLObjectPropertyRangeAxiom axiom) {
        OWLClassExpression range = axiom.getRange();
        OWLObjectPropertyExpression property = axiom.getProperty();
        Set<OWLEntity> bottomRanges = bottomLevelSpecializations(range);
        Set<OWLEntity> bottomProperties = bottomLevelSpecializations(property);
        for (OWLEntity subp : getSub(property)) {
            if (propertyRange.containsKey(subp)) return;
        }
        if (bottomRanges.isEmpty() && bottomProperties.isEmpty()) return;
        if (bottomRanges.isEmpty()) {
            if (range instanceof OWLEntity) bottomRanges.add((OWLEntity) range); else return;
        }
        if (bottomProperties.isEmpty()) {
            if (property instanceof OWLEntity) bottomProperties.add((OWLEntity) property);
        }
        for (OWLEntity r : bottomRanges) {
            for (OWLEntity p : bottomProperties) {
                if (r instanceof OWLClassExpression && p instanceof OWLObjectPropertyExpression) candidates.add(factory.getOWLObjectPropertyRangeAxiom((OWLObjectPropertyExpression) p, (OWLClassExpression) r));
            }
        }
    }

    public Set<OWLAxiom> getCandidates() {
        Set<OWLAxiom> disjointness = new HashSet<OWLAxiom>();
        for (Entry<OWLEntity, Set<OWLEntity>> subs : superSubofMap.entrySet()) {
            if (subs.getKey().equals(factory.getOWLThing())) continue;
            if (!(subs.getValue().size() > 1)) continue;
            if (subs.getKey() instanceof OWLClass) {
                Set<OWLClass> casted = new HashSet<OWLClass>();
                for (OWLEntity e : subs.getValue()) {
                    if (e instanceof OWLClass) casted.add((OWLClass) e);
                }
                disjointness.add(factory.getOWLDisjointClassesAxiom(casted));
                for (OWLClass e : casted) {
                    for (OWLClass e1 : casted) {
                        if (!e.equals(e1)) disjointness.add(factory.getOWLDisjointClassesAxiom(e, e1));
                    }
                }
            } else if (subs.getKey() instanceof OWLObjectProperty) {
                Set<OWLObjectProperty> casted = new HashSet<OWLObjectProperty>();
                for (OWLEntity e : subs.getValue()) {
                    if (e instanceof OWLObjectProperty) casted.add((OWLObjectProperty) e);
                }
                disjointness.add(factory.getOWLDisjointObjectPropertiesAxiom(casted));
                for (OWLObjectProperty e : casted) {
                    for (OWLObjectProperty e1 : casted) {
                        if (!e.equals(e1)) disjointness.add(factory.getOWLDisjointObjectPropertiesAxiom(e, e1));
                    }
                }
            } else if (subs.getKey() instanceof OWLDataProperty) {
                Set<OWLDataProperty> casted = new HashSet<OWLDataProperty>();
                for (OWLEntity e : subs.getValue()) {
                    if (e instanceof OWLDataProperty) casted.add((OWLDataProperty) e);
                }
                disjointness.add(factory.getOWLDisjointDataPropertiesAxiom(casted));
                for (OWLDataProperty e : casted) {
                    for (OWLDataProperty e1 : casted) {
                        if (!e.equals(e1)) disjointness.add(factory.getOWLDisjointDataPropertiesAxiom(e, e1));
                    }
                }
            }
        }
        candidates.addAll(disjointness);
        return this.candidates;
    }

    /**
	 * 
	 * @param object
	 * @return
	 */
    boolean existsSpecialization(OWLObject object) {
        return false;
    }
}
