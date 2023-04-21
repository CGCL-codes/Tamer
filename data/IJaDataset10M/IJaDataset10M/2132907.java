package net.sourceforge.hlm.semantics.type;

import java.util.*;
import net.sourceforge.hlm.generic.*;
import net.sourceforge.hlm.library.contexts.*;
import net.sourceforge.hlm.library.objects.*;
import net.sourceforge.hlm.library.parameters.*;
import net.sourceforge.hlm.library.parameters.arguments.*;
import net.sourceforge.hlm.library.terms.set.*;
import net.sourceforge.hlm.semantics.clone.*;
import net.sourceforge.hlm.semantics.traverse.*;

public class CompletingTypeChecker extends AutoTypeChecker {

    @Override
    public void traverseObject(MathObject object) throws Exception {
        super.traverseObject(object);
        for (Map.Entry<ArgumentList, ArgumentList> entry : this.filledArgumentLists.entrySet()) {
            this.autoComplete(entry.getKey(), entry.getValue());
        }
    }

    @Override
    protected void checkExactSetTerm(ContextPlaceholder<SetTerm> placeholder, SetTerm term) throws Exception {
        while (term instanceof WildcardSetTerm) {
            term = ((WildcardSetTerm) term).getTerm(true);
        }
        if (!ObjectComparer.getInstance().areSetTermsEqual(term, placeholder.get())) {
            placeholder.clear();
            ObjectCloner.getInstance().cloneSetTerm(term, placeholder);
        }
    }

    @Override
    protected TypeAnalyzer createTypeAnalyzer() {
        return new CompletingTypeAnalyzer(this.filledArgumentLists);
    }

    class CompletingTypeAnalyzer extends TypeAnalyzer {

        CompletingTypeAnalyzer(Map<ArgumentList, ArgumentList> argumentListReplacements) {
            super(argumentListReplacements);
        }

        @Override
        protected SubstitutingCloner createSubstitutingCloner(ArgumentList arguments) {
            return new CompletingSubstitutingCloner(arguments);
        }

        class CompletingSubstitutingCloner extends SubstitutingCloner {

            CompletingSubstitutingCloner(ArgumentList arguments) {
                super(arguments);
            }

            @Override
            protected <A> boolean parameterHasShortCut(SubstitutionEnvironment<A> environment, ElementParameter.ShortCut shortCut, ElementParameter parameter) throws Exception {
                FixedPlaceholder<ElementParameter.ShortCut> placeholder = parameter.getShortCut();
                if (placeholder.isEmpty()) {
                    this.cloneShortCut(shortCut, placeholder, environment);
                }
                return true;
            }
        }
    }
}
