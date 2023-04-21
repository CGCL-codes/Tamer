package org.kwantu.m2.model.ui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kwantu.m2.KwantuFaultException;
import org.kwantu.m2.model.KwantuModel;
import org.kwantu.m2.model.StringUtil;
import org.kwantu.m2.xpath.KwantuInvalidXPathException;
import org.kwantu.m2.xpath.compiler.TypeResolver;
import org.kwantu.m2.xpath.compiler.Typed;
import org.kwantu.m2.xpath.compiler.TypedParser;
import org.kwantu.persistence.AbstractPersistentObject;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

/**
 * KwantuComponent is the base class of all user interface components.
 */
@Entity
public abstract class KwantuComponent extends AbstractPersistentObject implements SequenceElement {

    private static final Log LOG = LogFactory.getLog(KwantuComponent.class);

    private Composite parent;

    protected int sequenceNo;

    private String path;

    private String helpKey;

    private String visibilityPath;

    private int columnSpan;

    private TypeResolver declaredTypeResolver;

    private int regressionTestingId;

    private KwantuUISecurityCheck securityCheck;

    public KwantuComponent() {
        super();
    }

    public int getRegressionTestingId() {
        if (regressionTestingId == 0) {
            regressionTestingId = getRootComponent().getLargestRegressionTestingId() + 1;
        }
        return regressionTestingId;
    }

    public void setRegressionTestingId(int regressionTestingId) {
        this.regressionTestingId = regressionTestingId;
    }

    @Transient
    public int getLargestRegressionTestingId() {
        int result = regressionTestingId;
        if (this instanceof Composite) {
            for (KwantuComponent child : ((Composite) this).getChildren()) {
                result = Math.max(result, child.getLargestRegressionTestingId());
            }
        }
        return result;
    }

    @ManyToOne
    public Composite getParent() {
        return parent;
    }

    public void setParent(Composite parent) {
        this.parent = parent;
    }

    @Override
    public int getSequenceNo() {
        return sequenceNo;
    }

    @Override
    public void setSequenceNo(int sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHelpKey() {
        return helpKey;
    }

    public void setHelpKey(String helpKey) {
        this.helpKey = helpKey;
    }

    public KwantuComponent helpKey(String helpKey) {
        setHelpKey(helpKey);
        return this;
    }

    public int getColumnSpan() {
        return columnSpan;
    }

    @Transient
    public int getValidColumnSpan() {
        if (columnSpan < 1) {
            return 1;
        }
        return columnSpan;
    }

    public void setColumnSpan(int columnSpan) {
        this.columnSpan = columnSpan;
    }

    public KwantuComponent columnSpan(int columnSpan) {
        setColumnSpan(columnSpan);
        return this;
    }

    @Transient
    public boolean isAbsolutePath() {
        return this.path != null && this.path.startsWith("/");
    }

    @Transient
    public int getNumberOfComponents() {
        return 1;
    }

    @Transient
    public final Object getType() throws KwantuInvalidXPathException {
        return getTypeDescriptor().getType();
    }

    @Transient
    public final TypeDescriptor getTypeDescriptor() throws KwantuInvalidXPathException {
        return determineTypeDescriptor();
    }

    @Transient
    public KwantuComponent getRootComponent() {
        if (getParent() == null) {
            return this;
        }
        return getParent().getRootComponent();
    }

    public void clearCashedTypeDescriptors() {
        throw new RuntimeException("not implemented; the idea is that getTypeDescriptor() " + "cashes the result. Then in those cases where anything being used as input " + "for determination of the type descriptor changes, this method can be " + "called.");
    }

    @Transient
    public boolean isCollection() throws KwantuInvalidXPathException {
        return getTypeDescriptor().isIsCollection();
    }

    @Override
    public KwantuComponent clone() {
        CloneVisitor visitor = new CloneVisitor();
        this.accept(visitor);
        KwantuComponent root = visitor.getClonedRoot();
        if (Composite.class.isAssignableFrom(root.getClass())) {
            ((Composite) root).sequenceChildren();
        }
        return root;
    }

    /** This method is part of the visitor pattern implementation,
     *  see {@link ComponentVisitor ComponentVisitor}.
     * 
     * @param visitor
     */
    public abstract void accept(ComponentVisitor visitor);

    @Transient
    public static String assembleRoute(KwantuComponent component) {
        if (component.getClass().equals(KwantuPage.class)) {
            return "Page: " + ((KwantuPage) component).getName();
        } else if (component.getClass().equals(KwantuPanel.class) && component.getParent() == null) {
            return "Named Panel: " + ((KwantuPanel) component).getName();
        } else {
            int i = 0;
            for (KwantuComponent c : component.getParent().getChildren()) {
                if (c.equals(component)) {
                    return assembleRoute(component.getParent()) + ".child[" + i + "]=" + component.getClass().getSimpleName();
                }
                i++;
            }
        }
        return "(invalid path to child)";
    }

    @Transient
    public static String assembleXPath(KwantuComponent component) {
        if (component.getParent() == null) {
            return "XPath: " + component.getPath();
        } else {
            return assembleXPath(component.getParent()) + "/" + component.getPath();
        }
    }

    @Transient
    public abstract String getDisplayName();

    @Transient
    public String getBreadCrumbString() {
        if (getParent() != null) {
            return (getParent().getBreadCrumbString() + " > " + getDisplayName());
        } else {
            return getDisplayName();
        }
    }

    public String getVisibilityPath() {
        return visibilityPath;
    }

    public void setVisibilityPath(String visibilityPath) {
        this.visibilityPath = visibilityPath;
    }

    public KwantuComponent visibility(String visibilityPath) {
        this.setVisibilityPath(visibilityPath);
        return this;
    }

    public KwantuComponent path(String path) {
        this.setPath(path);
        return this;
    }

    @Transient
    public KwantuModel getKwantuModel() {
        if (this instanceof KwantuPanel && getParent() == null) {
            return ((KwantuPanel) this).getOwningKwantuModel();
        } else if (getParent() != null) {
            return getParent().getKwantuModel();
        }
        throw new KwantuFaultException("unable to determine kwantu model");
    }

    /** determine the type context of this component for the evaluation of its
     * path. This method is overridden e.g. by a kwantu ui component which
     * is explicitly typed (e.g. a kwantu ui page).
     * @return the type context.
     */
    protected TypeDescriptor determineTypeContext() throws KwantuInvalidXPathException {
        return getParent().getTypeDescriptor();
    }

    @Transient
    protected TypeResolver getTypeResolver() {
        if (getDeclaredTypeResolver() != null) {
            return getDeclaredTypeResolver();
        }
        if (getParent() != null) {
            return getParent().getTypeResolver();
        }
        if (getKwantuModel() != null) {
            return getKwantuModel().getTypeResolver();
        }
        return null;
    }

    protected TypeDescriptor determineTypeDescriptor() throws KwantuInvalidXPathException {
        if (StringUtil.isEmpty(getPath())) {
            LOG.debug("path is empty");
            return TypeDescriptor.NULL;
        }
        TypeDescriptor typeContext = determineTypeContext();
        Typed typedExpression = TypedParser.parseExpression(getPath());
        return typedExpression.computeType(typeContext, getTypeResolver());
    }

    @Transient
    public TypeDescriptor computeTypeInContext(String path) throws KwantuInvalidXPathException {
        TypeDescriptor typeContext = getTypeDescriptor();
        Typed typedExpression = TypedParser.parseExpression(path);
        return typedExpression.computeType(typeContext, getTypeResolver());
    }

    @Transient
    public TypeResolver getDeclaredTypeResolver() {
        return declaredTypeResolver;
    }

    public void setDeclaredTypeResolver(TypeResolver declaredTypeResolver) {
        this.declaredTypeResolver = declaredTypeResolver;
    }

    @OneToOne(mappedBy = "owningComponent")
    public KwantuUISecurityCheck getSecurityCheck() {
        return securityCheck;
    }

    public void setSecurityCheck(KwantuUISecurityCheck securityCheck) {
        this.securityCheck = securityCheck;
    }

    public KwantuComponent securityCheck(KwantuUISecurityCheck check) {
        setSecurityCheck(check);
        check.setOwningComponent(this);
        return this;
    }
}
