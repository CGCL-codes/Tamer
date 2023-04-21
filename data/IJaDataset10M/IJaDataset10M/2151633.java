package de.fzi.injectj.model.impl.recoder;

import java.util.List;
import recoder.java.Expression;
import de.fzi.injectj.model.ActualParameterWeavepoint;
import de.fzi.injectj.model.Annotation;
import de.fzi.injectj.model.ExpressionFragment;
import de.fzi.injectj.model.FragmentType;
import de.fzi.injectj.model.MethodType;
import de.fzi.injectj.model.SourceElement;
import de.fzi.injectj.model.TypeWeavepoint;
import de.fzi.injectj.model.exception.AnnotationException;
import de.fzi.injectj.model.PackageWeavepoint;
import de.fzi.injectj.model.exception.ModelException;
import de.fzi.injectj.model.exception.NoSourceException;

/**
 * @author Volker Kuttruff*/
public class RecoderActualParameter extends ActualParameterWeavepoint {

    private Expression parameterExpr = null;

    private int index = -1;

    public RecoderActualParameter(Expression parameterExpr, int index) {
        super();
        if (parameterExpr == null) throw new NullPointerException();
        this.parameterExpr = parameterExpr;
        this.index = index;
    }

    public String getAskIdentifier() {
        return parameterExpr.toSource().trim();
    }

    public ExpressionFragment getExpression() {
        if (parameterExpr != null) {
            return new RecoderExpressionFragment(this, parameterExpr);
        } else {
            System.err.println("WARNING: 'expression' only allowed for method access parameters. Returning empty string.");
            return null;
        }
    }

    public String getPosition() {
        if (!hasSource()) {
            throw new NoSourceException("No source for actual parameter");
        }
        return RecoderUtil.getPosition(parameterExpr);
    }

    public FragmentType getSource() {
        if (!hasSource()) {
            throw new NoSourceException("No source for actual parameter");
        }
        return new RecoderExpressionFragment(this, parameterExpr);
    }

    public int getLine() {
        if (!hasSource()) {
            throw new NoSourceException("No source for actual parameter");
        }
        return RecoderUtil.getLine(parameterExpr);
    }

    public TypeWeavepoint getStaticType() {
        String typeName = null;
        RecoderRoot root = (RecoderRoot) getModel().getRoot();
        if (parameterExpr != null) {
            recoder.abstraction.Type type = root.getSourceInfo().getType(parameterExpr);
            if (type == null) return null;
            typeName = type.getFullName();
        }
        TypeWeavepoint result = getModel().getRoot().getTypeWeavepoint(typeName);
        if (result == null) {
            if (isChecked()) throw new RuntimeException("Cannot resolve return type in checked area");
            return null;
        }
        return result;
    }

    public Object getMopObject() {
        return parameterExpr;
    }

    public TypeWeavepoint getSurroundingType() {
        return RecoderUtil.findParentTypeWeavepoint((RecoderRoot) getModel().getRoot(), parameterExpr);
    }

    public MethodType getSurroundingMethod() {
        return RecoderUtil.findParentMethod((RecoderRoot) getModel().getRoot(), parameterExpr);
    }

    public List getLeadingComments() {
        return RecoderHelper.getLeadingComments(this);
    }

    public void setLeadingComments(List comments) {
        RecoderHelper.setLeadingComments(this, comments);
    }

    public List getTailingComments() {
        return RecoderHelper.getTailingComments(this);
    }

    public void setTailingComments(List comments) {
        RecoderHelper.setTailingComments(this, comments);
    }

    public boolean isConsistent() {
        if (super.isConsistent() == false) return false;
        if (parameterExpr == null) return false;
        return true;
    }

    public Annotation getAnnotation() throws AnnotationException {
        if (!hasSource()) {
            throw new NoSourceException("No source for actual parameter");
        }
        return RecoderCommentsHelper.getAnnotation(this);
    }

    public void setAnnotation(List listOfList) throws AnnotationException {
        if (!hasSource()) {
            throw new NoSourceException("No source for actual parameter");
        }
        RecoderAnnotation annotation = new RecoderAnnotation(this, listOfList);
    }

    public int getIndex() {
        return index;
    }

    public boolean canDelete() {
        return false;
    }

    public boolean canReplace(FragmentType source) {
        return false;
    }

    public SourceElement delete() throws ModelException {
        throw new RuntimeException("Not implemented yet");
    }

    public int getColumn() {
        if (!hasSource()) {
            throw new NoSourceException("No source for actual parameter");
        }
        return RecoderUtil.getLine(parameterExpr);
    }

    public PackageWeavepoint getSurroundingPackage() {
        return RecoderUtil.findParentPackageWeavepoint((RecoderRoot) getModel().getRoot(), parameterExpr);
    }

    public SourceElement replace(FragmentType source) throws ModelException {
        throw new RuntimeException("Not implemented yet");
    }
}
