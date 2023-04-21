package org.oslo.ocl20.bridge4emf;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.oslo.ocl20.OclProcessor;
import org.oslo.ocl20.semantics.SemanticsVisitor;
import org.oslo.ocl20.semantics.bridge.Environment;
import org.oslo.ocl20.semantics.bridge.ModelElement;
import org.oslo.ocl20.semantics.bridge.Namespace;

/**
 * @author dha
 *
 */
public class NamespaceImpl implements Namespace {

    protected OclProcessor processor = null;

    public NamespaceImpl(EPackage ePkg, OclProcessor proc) {
        this.processor = proc;
        _ePkg = ePkg;
    }

    EPackage _ePkg;

    Map _elements = new HashMap();

    public ModelElement lookupOwnedElement(String name) {
        ModelElement mel = (ModelElement) _elements.get(name);
        if (mel == null) {
            EClassifier ecl = _ePkg.getEClassifier(name);
            if (ecl == null) return null; else {
                mel = this.processor.getBridgeFactory().buildClassifier(ecl);
                _elements.put(name, mel);
            }
        }
        return mel;
    }

    Namespace _namespace = null;

    public Namespace getNamespace() {
        if (_namespace == null) _namespace = new NamespaceImpl(_ePkg.getESuperPackage(), this.processor);
        return _namespace;
    }

    public void setNamespace(Namespace n) {
        _namespace = n;
    }

    /**
	 * @see ocl20.bridge.Namespace#getEnvironmentWithoutParents()
	 */
    public Environment getEnvironmentWithoutParents() {
        Environment env = this.processor.getBridgeFactory().buildEnvironment();
        env.addNamespace(this);
        env.setParent(null);
        return env;
    }

    /**
	 * @see ocl20.bridge.Namespace#getEnvironmentWithParents()
	 */
    public Environment getEnvironmentWithParents() {
        if (this.getNamespace() == null) {
            return null;
        } else {
            Environment result = getEnvironmentWithoutParents();
            result.setParent(this.getNamespace().getEnvironmentWithParents());
            return result;
        }
    }

    /**
	 * @see ocl20.bridge.ModelElement#getName()
	 */
    public String getName() {
        return _ePkg.getName();
    }

    public String getFullName(String sep) {
        String name = "";
        EPackage pkg = _ePkg.getESuperPackage();
        while (pkg != null) {
            if (!name.equals("")) name = sep + name;
            name = pkg.getName() + name;
            pkg = pkg.getESuperPackage();
        }
        if (!name.equals("")) name += sep;
        return name + _ePkg.getName();
    }

    /**
	 * @see ocl20.bridge.ModelElement#setName(String)
	 */
    public void setName(String name) {
    }

    /**
	 * @see ocl20.ocl20Visitable#accept(ocl20Visitor, Object)
	 */
    public Object accept(SemanticsVisitor v, Object obj) {
        return v.visit(this, obj);
    }

    public Object clone() {
        return new NamespaceImpl(_ePkg, this.processor);
    }

    public Object getDelegate() {
        return _ePkg;
    }
}
