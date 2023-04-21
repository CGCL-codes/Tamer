package net.sf.refactorit.query.usage;

import net.sf.refactorit.classmodel.BinConstructor;
import net.sf.refactorit.classmodel.BinLocalVariable;
import net.sf.refactorit.classmodel.BinMember;
import net.sf.refactorit.classmodel.BinMethod;
import net.sf.refactorit.classmodel.BinParameter;
import net.sf.refactorit.classmodel.BinSourceConstruct;
import net.sf.refactorit.loader.Comment;
import net.sf.refactorit.loader.JavadocComment;
import net.sf.refactorit.refactorings.javadoc.Javadoc;

/**
 * @author Anton Safonov
 */
public final class LocalVariableNameIndexer extends LocalVariableIndexer {

    public boolean skipDeclarations = false;

    public LocalVariableNameIndexer(final ManagingIndexer supervisor, final BinLocalVariable target) {
        super(supervisor, target);
        setSearchForNames(true);
    }

    /** False by default */
    public final void setSkipDeclarations(boolean b) {
        skipDeclarations = b;
    }

    public final void visit(final BinLocalVariable var) {
        if (skipDeclarations) {
            return;
        }
        BinSourceConstruct c;
        if (var.getClass() == BinParameter.class) {
            c = null;
        } else {
            c = (BinSourceConstruct) var.getParent();
        }
        checkVariable(var, var.getNameAstOrNull(), c);
    }

    public final void visit(final BinConstructor constructor) {
        checkMemberJavadoc(constructor);
    }

    public final void visit(final BinMethod method) {
        checkMemberJavadoc(method);
    }

    protected final void checkMemberJavadoc(final BinMember member) {
        final BinLocalVariable local = (BinLocalVariable) getTarget();
        if (local.getParentMember() == member) {
            final JavadocComment comment = Comment.findJavadocFor(member);
            if (comment != null) {
                final Javadoc javadoc = Javadoc.parseIntoFakeClassmodel(member, comment, comment.getStartColumn() - 1);
                if (javadoc != null) {
                    javadoc.callVisit(this);
                }
            }
        }
    }
}
