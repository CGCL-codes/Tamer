package gr.konstant.transonto.kb.prolog;

import gr.konstant.transonto.exception.BackendException;
import gr.konstant.transonto.exception.BadArgumentException;
import gr.konstant.transonto.exception.UnsupportedConstructException;
import gr.konstant.transonto.exception.UnsupportedFeatureException;
import gr.konstant.transonto.interfaces.Annotation;
import gr.konstant.transonto.interfaces.Axiom;
import gr.konstant.transonto.interfaces.AxiomEquiv;
import gr.konstant.transonto.interfaces.AxiomIncl;
import gr.konstant.transonto.interfaces.AxiomInstance;
import gr.konstant.transonto.interfaces.ExprConcept;
import gr.konstant.transonto.interfaces.ExprConceptNamed;
import gr.konstant.transonto.interfaces.ExprConceptNominals;
import gr.konstant.transonto.interfaces.ExprIndividual;
import gr.konstant.transonto.interfaces.ExprLiteral;
import gr.konstant.transonto.interfaces.ExprNamed;
import gr.konstant.transonto.interfaces.ExprPredicate;
import gr.konstant.transonto.interfaces.ExprPredicateNamed;
import gr.konstant.transonto.interfaces.ExprPropertyNamed;
import gr.konstant.transonto.interfaces.ExprUnion;
import gr.konstant.transonto.interfaces.ExprValue;
import gr.konstant.transonto.interfaces.Extension;
import gr.konstant.transonto.interfaces.ExtensionConcept;
import gr.konstant.transonto.interfaces.Extensions;
import gr.konstant.transonto.interfaces.Instantiation;
import gr.konstant.transonto.interfaces.Realization;
import gr.konstant.transonto.interfaces.Realizations;
import gr.konstant.transonto.interfaces.Valuation;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Prodlr extends KBFilePrologImpl {

    public Prodlr() {
    }

    public Prodlr(URI base) {
        super(base);
    }

    public Prodlr(File f) {
        super(f);
    }

    public Prodlr(URI base, File f) {
        super(base, f);
    }

    public Prodlr(File inFile, File outFile) {
        super(inFile, outFile);
    }

    public Prodlr(URI base, File inFile, File outFile) {
        super(base, inFile, outFile);
    }

    private Integer vcounter = 0;

    protected void init_repr() {
        vcounter = 0;
    }

    protected void inc_counter() {
        ++vcounter;
    }

    protected Integer get_counter() {
        return new Integer(vcounter);
    }

    @Override
    protected Argument repr(Argument arg) {
        if (arg instanceof SelectAny) {
            return reprSelect((SelectAny) arg);
        } else if (arg instanceof Term) {
            return arg;
        } else {
            throw new AssertionError();
        }
    }

    private SelectAny reprSelect(SelectAny c) {
        SelectAny cc = (SelectAny) c.clone();
        cc.args.add(0, new Variable(this, "X" + vcounter));
        ++vcounter;
        cc.args.add(new Variable(this, "X" + vcounter));
        cc.arity += 2;
        if (cc instanceof SelectExist) {
            Argument rel = (TermPlain) cc.args.get(1);
            try {
                Argument relPath = new TermList(this, Collections.singletonList(rel));
                cc.args.set(1, relPath);
            } catch (BadArgumentException ex) {
                throw new AssertionError(ex);
            }
        }
        return cc;
    }

    @Override
    protected void read(InputStreamReader reader) throws BackendException {
        throw new BackendException();
    }

    @Override
    protected void write(OutputStreamWriter writer) throws BackendException {
        for (Axiom c : getAllAxioms()) {
            String s;
            if (c instanceof ProdlrDecl) {
                s = ((ProdlrDecl) c).toString();
            } else if (c instanceof ProdlrInstance) {
                s = ((ProdlrInstance) c).toString();
            } else if (c instanceof ProdlrRule) {
                s = ((ProdlrRule) c).toProdlrString();
            } else {
                throw new AssertionError();
            }
            try {
                writer.write(s + "\n");
            } catch (IOException ex) {
                throw new BackendException(ex);
            }
        }
    }

    @Override
    public List<ExprValue> getAnnotation(URI key) throws UnsupportedFeatureException, BackendException {
        throw new UnsupportedFeatureException("Annotations not supported in this model");
    }

    @Override
    public List<Annotation> getAnnotation() throws UnsupportedFeatureException, BackendException {
        throw new UnsupportedFeatureException("Annotations not supported in this model");
    }

    @Override
    public List<Axiom> getAllAxioms() throws BackendException {
        List<Axiom> l = new ArrayList<Axiom>();
        List<Axiom> declC = new ArrayList<Axiom>();
        List<Axiom> declR = new ArrayList<Axiom>();
        List<Axiom> nullOther = new ArrayList<Axiom>();
        List<Axiom> other = new ArrayList<Axiom>();
        for (Axiom ax : super.getAllAxioms()) {
            if (ax instanceof ClauseHeadless) {
                ClauseHeadless c = (ClauseHeadless) ax;
                if (c.body.args.get(0).toString().startsWith("'declare_concept'")) {
                    declC.add(ax);
                } else if (c.body.args.get(0).toString().startsWith("'declare_")) {
                    declR.add(ax);
                } else {
                    nullOther.add(ax);
                }
            } else {
                other.add(ax);
            }
        }
        l.addAll(declC);
        l.addAll(declR);
        l.addAll(nullOther);
        l.addAll(other);
        return l;
    }

    @Override
    public Extensions getAllExtensions() throws BackendException {
        return null;
    }

    @Override
    public Realizations getAllRealizations() throws BackendException {
        return null;
    }

    @Override
    public Extension getExtension(ExprPredicate predicate) throws BackendException {
        return null;
    }

    @Override
    public Realization getRealization(Instantiation instance) throws BackendException {
        return null;
    }

    @Override
    public List<AxiomEquiv> findDefinitions(ExprPredicateNamed defined, ExprPredicate definition) throws BackendException {
        return Collections.emptyList();
    }

    @Override
    public List<AxiomIncl> findSubsumers(ExprPredicate subsumed, ExprPredicate subsumer) throws BackendException {
        return null;
    }

    @Override
    public Valuation instantiatesPredicate(Instantiation instance, ExprPredicate predicate) throws BadArgumentException {
        return null;
    }

    public ExprLiteral findLiteral(String data, String datatype) throws BadArgumentException, BackendException {
        return null;
    }

    public ExprLiteral findLiteral(URI xsdData) throws BadArgumentException, BackendException {
        return null;
    }

    public ExprIndividual findIndividual(URI name) throws BackendException {
        return null;
    }

    public ExprConceptNamed findConcept(URI name) throws BackendException {
        return null;
    }

    public ExprPropertyNamed findProperty(URI name) throws BackendException {
        return null;
    }

    public ExprPredicateNamed findPredicate(URI name) throws BackendException {
        return null;
    }

    @Override
    public AxiomInstance createInstantiation(ExprPredicate predicate, Instantiation instance, Valuation v) throws BadArgumentException, UnsupportedFeatureException {
        ProdlrInstance retv;
        if ((predicate.getArity() == 1) && (instance.getArity() == 1)) {
            URI pName, iName;
            if (predicate instanceof ExprPredicateNamed) {
                pName = ((ExprPredicateNamed) predicate).getName();
            } else {
                throw new UnsupportedFeatureException("Only named concepts are supported");
            }
            try {
                iName = instance.getAbstractTerms().get(0).getName();
            } catch (IndexOutOfBoundsException ex) {
                throw new AssertionError(ex);
            }
            retv = new ProdlrInstance(this, pName, iName, v);
        } else if ((predicate.getArity() == 2) && (instance.getArity() == 2)) {
            String pName, leftName, rightName;
            if (predicate instanceof ExprPredicateNamed) {
                pName = ((ExprPredicateNamed) predicate).getLocalName();
            } else {
                throw new UnsupportedFeatureException("Only named properties are supported");
            }
            try {
                leftName = instance.getAbstractTerms().get(0).getLocalName();
            } catch (IndexOutOfBoundsException ex) {
                throw new AssertionError(ex);
            }
            try {
                rightName = instance.getAbstractTerms().get(1).getLocalName();
            } catch (IndexOutOfBoundsException ex) {
                throw new AssertionError(ex);
            }
            retv = new ProdlrInstance(this, pName, leftName, rightName, v);
        } else if (predicate.getArity() == instance.getArity()) {
            throw new UnsupportedFeatureException();
        } else {
            throw new BadArgumentException();
        }
        return retv;
    }

    @Override
    public ProdlrPredicate createDefinition(ExprPredicateNamed concept, ExprPredicate definition, Valuation v) throws UnsupportedFeatureException, BadArgumentException {
        if (false) {
            throw new UnsupportedFeatureException();
        } else {
            ProdlrRule c = this.createSubsumption(definition, concept, v);
            return new ProdlrPredicate(this, Collections.singletonList((ClauseFull) c));
        }
    }

    @Override
    public ProdlrRule createSubsumption(ExprPredicate premises, ExprPredicate conclusion, Valuation v) throws BadArgumentException, UnsupportedFeatureException {
        TermList pr;
        if (premises instanceof SelectAny) {
            pr = new TermList(this, Collections.singletonList((Argument) premises));
        } else if (premises instanceof TermList) {
            pr = (TermList) premises;
        } else {
            throw new BadArgumentException();
        }
        SelectConceptPos c;
        if (conclusion instanceof SelectConceptPos) {
            c = (SelectConceptPos) conclusion;
        } else if (conclusion instanceof SelectAny) {
            throw new UnsupportedFeatureException();
        } else {
            throw new BadArgumentException();
        }
        return new ProdlrRule(this, c, pr, v);
    }

    @Override
    public TermPlain getIndividual(URI name) throws BadArgumentException {
        String functor = name2functor(name);
        return new TermPlain(this, functor, 0);
    }

    @Override
    public TermPlain getPredicate(URI name, int arity, boolean concrete) throws BadArgumentException, UnsupportedFeatureException {
        if (arity < 1) {
            throw new BadArgumentException();
        } else if (arity > 2) {
            throw new UnsupportedFeatureException();
        }
        List<HornClause> l = this.getClauses("declare_concept");
        boolean found = false;
        Iterator<HornClause> it = l.iterator();
        while (!found && it.hasNext()) {
            HornClause c = it.next();
            TermPlain b;
            try {
                b = (TermPlain) c.body.getArg(0);
            } catch (BadArgumentException ex) {
                throw new AssertionError(ex);
            }
            URI u;
            try {
                u = ((TermPlain) b.getArg(0)).getName();
            } catch (BadArgumentException ex) {
                throw new AssertionError(ex);
            } catch (ClassCastException ex) {
                throw new AssertionError(ex);
            }
            if (u.equals(name)) {
                found = true;
            }
        }
        if (found) {
            if (arity == 1) {
                return new SelectConceptPos(this, name);
            } else {
                throw new BadArgumentException();
            }
        }
        l = this.getClauses("declare_relation");
        found = false;
        it = l.iterator();
        while (!found && it.hasNext()) {
            HornClause c = it.next();
            TermPlain b;
            try {
                b = (TermPlain) c.body.getArg(0);
            } catch (BadArgumentException ex) {
                throw new AssertionError(ex);
            }
            URI u;
            try {
                u = ((TermPlain) b.getArg(0)).getName();
            } catch (BadArgumentException ex) {
                throw new AssertionError(ex);
            } catch (ClassCastException ex) {
                throw new AssertionError(ex);
            }
            if (u.equals(name)) {
                found = true;
            }
        }
        if (found) {
            if (arity == 2) {
                return new SelectConceptPos(this, name);
            } else {
                throw new BadArgumentException();
            }
        }
        ProdlrDecl ax = new ProdlrDecl(this, name, arity);
        ax.add();
        if (arity == 1) {
            return new SelectConceptPos(this, name);
        } else {
            return new TermPlain(this, name.toString(), 2);
        }
    }

    @Override
    public Literal getLiteral(URI datatype, String lexform) throws BadArgumentException, UnsupportedFeatureException {
        return new Literal(this, datatype, lexform);
    }

    @Override
    public SelectConceptNeg getComplement(ExprConcept concept) throws BadArgumentException {
        if (concept instanceof SelectConceptPos) {
            return new SelectConceptNeg(this, (SelectConceptPos) concept);
        } else if (concept instanceof SelectConceptNeg) {
            throw new BadArgumentException("double-complements not supported");
        } else if (concept instanceof SelectAny) {
            throw new BadArgumentException("only named concepts can be negated");
        } else {
            throw new BadArgumentException();
        }
    }

    @Override
    public ExprUnion getUnion(List<ExprConcept> concepts) throws UnsupportedFeatureException {
        throw new UnsupportedFeatureException("union not supported");
    }

    @Override
    public TermList getIntersection(List<ExprConcept> concepts) throws UnsupportedFeatureException, BadArgumentException {
        List<Argument> operands = new ArrayList<Argument>(concepts.size());
        boolean isGood = true;
        Iterator<ExprConcept> it = concepts.iterator();
        while (isGood && it.hasNext()) {
            ExprConcept c = it.next();
            SelectAny s;
            try {
                s = (SelectAny) c;
            } catch (ClassCastException ex) {
                s = null;
                isGood = false;
            }
            if (isGood) {
                operands.add(s);
            }
        }
        if (isGood) {
            return new TermList(this, operands);
        } else {
            throw new BadArgumentException();
        }
    }

    @Override
    public SelectExist getExists(ExprPredicate predicate, ExprPredicate range, Integer min, Integer max) throws UnsupportedFeatureException {
        if (predicate.getArity() != 2) {
            throw new UnsupportedFeatureException();
        }
        URI relName;
        if (predicate instanceof ExprNamed) {
            relName = ((ExprNamed) predicate).getName();
        } else {
            throw new UnsupportedFeatureException("Property descriptions in ");
        }
        URI rangeName;
        if (range instanceof ExprNamed) {
            rangeName = ((ExprNamed) range).getName();
        } else {
            throw new UnsupportedFeatureException();
        }
        if ((max == null) && (min != null)) {
            try {
                return new SelectExistMin(this, relName, rangeName, min);
            } catch (BadArgumentException ex) {
                throw new AssertionError();
            }
        } else if (((min == null) || (min == 0)) && (max != null)) {
            try {
                return new SelectExistMax(this, relName, rangeName, max);
            } catch (BadArgumentException ex) {
                throw new AssertionError();
            }
        } else {
            throw new UnsupportedConstructException(UnsupportedConstructException.MSG_EXISTS_BOTH);
        }
    }

    @Override
    public SelectForAll getForAll(ExprPredicate predicate, ExprPredicate range) throws UnsupportedFeatureException {
        throw new UnsupportedConstructException(UnsupportedConstructException.MSG_FORALL);
    }

    @Override
    public ExprConceptNominals getNominals(ExtensionConcept ext) throws UnsupportedFeatureException, BackendException {
        ProdlrDeclExt decl;
        try {
            decl = new ProdlrDeclExt(this, ext);
        } catch (BadArgumentException ex) {
            throw new AssertionError(ex);
        }
        decl.add();
        URI anonID = decl.getName();
        try {
            return new SelectConceptNominals(this, decl);
        } catch (BadArgumentException ex) {
            throw new AssertionError(ex);
        }
    }
}
