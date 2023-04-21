package org.exist.eclipse.xquery.ui.search;

import org.eclipse.dltk.ast.declarations.Argument;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.references.Reference;
import org.eclipse.dltk.ast.references.SimpleReference;
import org.eclipse.dltk.core.IMethod;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.search.SearchPattern;
import org.eclipse.dltk.internal.core.search.matching.FieldLocator;
import org.eclipse.dltk.internal.core.search.matching.FieldPattern;
import org.eclipse.dltk.internal.core.search.matching.MatchingNodeSet;
import org.exist.eclipse.xquery.core.XQueryVariableReference;

@SuppressWarnings("restriction")
public class MyFieldLocator extends FieldLocator {

    public MyFieldLocator(SearchPattern pattern) {
        super((FieldPattern) pattern);
    }

    @Override
    public int match(Reference node, MatchingNodeSet nodeSet) {
        if (!this.pattern.findReferences) {
            return IMPOSSIBLE_MATCH;
        }
        if (!(node instanceof SimpleReference)) {
            return IMPOSSIBLE_MATCH;
        }
        if (this.pattern.name == null) {
            return nodeSet.addMatch(node, POSSIBLE_MATCH);
        }
        if (matchesName(this.pattern.name, ((SimpleReference) node).getName().toCharArray())) {
            boolean doAdd = true;
            IModelElement focusParent = pattern.focus.getParent();
            if (focusParent instanceof IMethod && node instanceof XQueryVariableReference) {
                XQueryVariableReference varRef = (XQueryVariableReference) node;
                MethodDeclaration refParent = varRef.getParent();
                if (refParent == null) {
                    doAdd = false;
                } else {
                    try {
                        doAdd = refParent.getName().equals(focusParent.getElementName()) && refParent.getArguments().size() == ((IMethod) focusParent).getParameters().length;
                        if (doAdd) {
                            for (Object it : refParent.getArguments()) {
                                Argument arg = (Argument) it;
                                if (matchesName(this.pattern.name, arg.getName().toCharArray())) {
                                    break;
                                }
                            }
                        }
                    } catch (ModelException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            if (doAdd) {
                return nodeSet.addMatch(node, ACCURATE_MATCH);
            }
        }
        return IMPOSSIBLE_MATCH;
    }
}
