package org.oslo.ocl20.bridge4emf;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EPackage;
import org.oslo.ocl20.OclProcessor;
import org.oslo.ocl20.semantics.SemanticsVisitor;
import org.oslo.ocl20.semantics.bridge.Classifier;
import org.oslo.ocl20.semantics.bridge.EnumLiteral;
import org.oslo.ocl20.semantics.bridge.EnumerationType;
import org.oslo.ocl20.semantics.bridge.ModelElement;
import org.oslo.ocl20.semantics.bridge.Property;
import org.oslo.ocl20.standard.types.OclAnyTypeImpl;

/**
 * @author dha
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class EnumerationImpl extends OclAnyTypeImpl implements EnumerationType {

    protected EEnum _eenum;

    public EnumerationImpl(EEnum eenum, OclProcessor proc) {
        super(proc);
        _eenum = eenum;
        super.createOperations(processor.getTypeFactory());
        super.getOperations().add(proc.getBridgeFactory().buildOperation(super.processor.getTypeFactory().buildBooleanType(), "=", new Classifier[] { this }));
        super.getOperations().add(proc.getBridgeFactory().buildOperation(super.processor.getTypeFactory().buildBooleanType(), "<>", new Classifier[] { this }));
    }

    /** Get name */
    String name = null;

    public String getName() {
        if (_eenum != null) {
            name = "";
            EPackage pkg = _eenum.getEPackage();
            while (pkg != null) {
                if (!name.equals("")) name = "." + name;
                name = pkg.getName() + name;
                pkg = pkg.getESuperPackage();
            }
            if (!name.equals("")) name += ".";
            name += _eenum.getName();
        }
        return name;
    }

    /** Check if this (an Enumeration) conforms with t2 */
    public Boolean conformsTo(Classifier t2) {
        if (t2 instanceof EnumerationType) {
            return (getName().equals(t2.getName())) ? Boolean.TRUE : Boolean.FALSE;
        }
        return Boolean.FALSE;
    }

    /** Search for an Enumeration Literal with a given name */
    public EnumLiteral lookupEnumLiteral(String name) {
        return ((EmfBridgeFactory) super.processor.getBridgeFactory()).buildEnumLiteral(_eenum.getEEnumLiteral(name), this);
    }

    /** Search for an Enumeration Literal with a given name */
    public Property lookupProperty(String name) {
        return lookupEnumLiteral(name);
    }

    /** Search for an owned element */
    public ModelElement lookupOwnedElement(String name) {
        return lookupEnumLiteral(name);
    }

    /** Get Enumeration Literals */
    List literals = null;

    public List getLiteral() {
        if (_eenum != null) {
            literals = new Vector();
            Iterator i = _eenum.getELiterals().iterator();
            while (i.hasNext()) {
                EEnumLiteral lit = (EEnumLiteral) i.next();
                literals.add(((EmfBridgeFactory) super.processor.getBridgeFactory()).buildEnumLiteral(lit, this));
            }
        }
        return literals;
    }

    /** Set Enumeration Literals */
    public void setLiteral(List literals) {
    }

    /** Accept a Sematics Visitor */
    public Object accept(SemanticsVisitor v, Object obj) {
        return v.visit(this, obj);
    }

    public String toString() {
        return _eenum.getName();
    }

    /** Equals */
    public boolean equals(Object o) {
        if (o instanceof EnumerationType) {
            EnumerationType enumerationType = (EnumerationType) o;
            return getName().equals(enumerationType.getName());
        }
        return false;
    }

    public Object getDelegate() {
        return _eenum.getInstanceClass();
    }
}
