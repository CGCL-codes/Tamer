package fr.univartois.cril.xtext2.scoping;

import org.eclipse.xtext.naming.DefaultDeclarativeQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import fr.univartois.cril.xtext2.als.Assertion;
import fr.univartois.cril.xtext2.als.AssertionName;
import fr.univartois.cril.xtext2.als.Fact;
import fr.univartois.cril.xtext2.als.FactName;
import fr.univartois.cril.xtext2.als.Function;
import fr.univartois.cril.xtext2.als.FunctionName;
import fr.univartois.cril.xtext2.als.Let;
import fr.univartois.cril.xtext2.als.Predicate;
import fr.univartois.cril.xtext2.als.PredicateName;
import fr.univartois.cril.xtext2.als.PropertyName;
import fr.univartois.cril.xtext2.als.ReferencesName;
import fr.univartois.cril.xtext2.als.Specification;

public class AlsQualifiedNameProvider extends DefaultDeclarativeQualifiedNameProvider {

    public QualifiedName qualifiedName(Specification s) {
        return getConverter().toQualifiedName(s.getModule().getModuleName());
    }

    public QualifiedName qualifiedName(Let l) {
        return getConverter().toQualifiedName(l.getName().getName());
    }

    public QualifiedName qualifiedName(Predicate p) {
        return getConverter().toQualifiedName(p.getName().getName());
    }

    public QualifiedName qualifiedName(Function f) {
        return getConverter().toQualifiedName(f.getName().getName());
    }

    public QualifiedName qualifiedName(Assertion a) {
        return getConverter().toQualifiedName(a.getName().getName());
    }

    public QualifiedName qualifiedName(Fact f) {
        return getConverter().toQualifiedName(f.getName().getName());
    }

    public QualifiedName qualifiedName(PredicateName p) {
        return getConverter().toQualifiedName(p.getName());
    }

    public QualifiedName qualifiedName(FunctionName f) {
        return getConverter().toQualifiedName(f.getName());
    }

    public QualifiedName qualifiedName(AssertionName a) {
        return getConverter().toQualifiedName(a.getName());
    }

    public QualifiedName qualifiedName(FactName f) {
        return getConverter().toQualifiedName(f.getName());
    }

    public QualifiedName qualifiedName(ReferencesName r) {
        if (!(r instanceof PropertyName)) {
            return getConverter().toQualifiedName(r.getName());
        }
        return null;
    }
}
