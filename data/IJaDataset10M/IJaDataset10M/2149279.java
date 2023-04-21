package org.stlab.xd.analyzer.delegates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;
import org.stlab.xd.analyzer.IAnalyzerDelegate;
import org.stlab.xd.analyzer.IAnalyzerResult;

public class ArchImportNoticeDelegate extends AbstractAnalyzerDelegate implements IAnalyzerDelegate {

    public ArchImportNoticeDelegate() {
    }

    @Override
    public List<IAnalyzerResult> perform(final OWLOntology ontology) {
        List<IAnalyzerResult> results = new ArrayList<IAnalyzerResult>();
        if (ontology.getImportsClosure().size() == 1) return results;
        ReuseDiagnosisVisitor visitor = new ReuseDiagnosisVisitor(ontology);
        int linkeds = visitor.getLinked().size();
        final int numberOfRelated = linkeds;
        int unlinkeds = visitor.getUnlinked().size();
        final int numberOfUnrelated = unlinkeds;
        if (numberOfUnrelated > numberOfRelated) results.add(new ArchImportNoticeResult() {

            @Override
            public Set<? extends Object> getSuggestedOWLObjects() {
                return new HashSet<Object>();
            }

            @Override
            public String getMessage() {
                return "The ontology contains " + numberOfRelated + " entities structurally connected with imported entities. " + " " + numberOfUnrelated + " entities are locally declared";
            }

            @Override
            public Set<? extends Object> getInvolvedOWLObjects() {
                return Collections.singleton(ontology);
            }
        });
        return results;
    }

    public abstract class ArchImportNoticeResult implements IAnalyzerResult {
    }

    private class ReuseDiagnosisVisitor extends OWLAxiomVisitorAdapter {

        private OWLOntology ontology;

        private Set<OWLEntity> linked;

        private Set<OWLEntity> unlinked;

        private Set<OWLEntity> localEntities;

        private Set<OWLEntity> importedEntities;

        private Map<Object, Set<Object>> subSuperMap;

        public ReuseDiagnosisVisitor(OWLOntology ontology) {
            this.ontology = ontology;
            this.localEntities = getLocalEntities(ontology);
            this.importedEntities = getReferencedEntities(ontology, ontology.getImports());
            this.linked = new HashSet<OWLEntity>();
            this.unlinked = new HashSet<OWLEntity>();
            this.subSuperMap = new HashMap<Object, Set<Object>>();
            for (OWLEntity local : localEntities) {
                for (OWLAxiom a : ontology.getReferencingAxioms(local)) {
                    a.accept(this);
                }
            }
            for (OWLEntity local : localEntities) {
                boolean isLinked = cyclicCheck(local, subSuperMap.get(local));
                if (isLinked) {
                    linked.add(local);
                } else {
                    unlinked.add(local);
                }
            }
        }

        private boolean cyclicCheck(OWLEntity subject, Object superr) {
            if (superr instanceof Set) {
                for (Object o : ((Set) superr)) {
                    if (cyclicCheck(subject, o)) {
                        return true;
                    }
                }
                return false;
            }
            if (superr == null) return false;
            if (superr instanceof OWLEntity) {
                if (importedEntities.contains(superr)) {
                    return true;
                }
                return cyclicCheck(subject, subSuperMap.get(superr));
            }
            return false;
        }

        @Override
        public void visit(OWLSubAnnotationPropertyOfAxiom axiom) {
            add(axiom.getSuperProperty(), axiom.getSubProperty());
        }

        void add(Object sup, Object sub) {
            Set<Object> vs;
            vs = subSuperMap.get(sub);
            if (vs == null) {
                vs = new HashSet<Object>();
            }
            vs.add(sup);
            subSuperMap.put(sub, vs);
        }

        @Override
        public void visit(OWLSubClassOfAxiom axiom) {
            add(axiom.getSuperClass(), axiom.getSubClass());
        }

        @Override
        public void visit(OWLSubDataPropertyOfAxiom axiom) {
            add(axiom.getSuperProperty(), axiom.getSubProperty());
        }

        @Override
        public void visit(OWLSubObjectPropertyOfAxiom axiom) {
            add(axiom.getSuperProperty(), axiom.getSubProperty());
        }

        @Override
        public void visit(OWLSubPropertyChainOfAxiom axiom) {
            for (OWLObjectPropertyExpression pe : axiom.getPropertyChain()) {
                Set<OWLObjectProperty> ep = pe.getObjectPropertiesInSignature();
                for (OWLObjectProperty op : ep) add(axiom.getSuperProperty(), op);
            }
        }

        Set<OWLEntity> getLinked() {
            return linked;
        }

        Set<OWLEntity> getUnlinked() {
            return unlinked;
        }
    }
}
