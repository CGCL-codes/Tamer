package tudresden.ocl20.core.jmi.uml15.impl.modelmanagement;

import javax.jmi.reflect.*;
import java.util.*;
import tudresden.ocl20.core.jmi.uml15.core.*;
import tudresden.ocl20.core.jmi.uml15.modelmanagement.*;
import tudresden.ocl20.core.jmi.uml15.uml15.Uml15Package;

/**
 *
 * @author  Administrator
 */
public class ModelHelper {

    Uml15Package umlPackage;

    Model model;

    public static Map instances = new HashMap();

    private ModelHelper(RefPackage umlPackage) {
        this(umlPackage, null);
    }

    /** Creates a new instance of ModelHelper */
    private ModelHelper(RefPackage umlPackage, Collection topElements) {
        this.umlPackage = (Uml15Package) umlPackage;
        if (topElements == null) {
            topElements = getTopElements();
        }
        Iterator it;
        int modelCount = 0;
        Model modelCandidate = null;
        it = topElements.iterator();
        while (it.hasNext()) {
            ModelElement me = (ModelElement) it.next();
            if (me instanceof Model) {
                modelCount++;
                modelCandidate = (Model) me;
            }
        }
        if (modelCount == 1) {
            model = modelCandidate;
        } else {
            model = this.umlPackage.getModelManagement().getModel().createModel();
            model.setNameA("generatedTopLevelModel");
            it = topElements.iterator();
            while (it.hasNext()) {
                ModelElement me = (ModelElement) it.next();
                System.out.println("TopElement " + me.getNameA() + " " + me);
                me.setNamespace(model);
            }
        }
    }

    public static ModelHelper getInstance(Uml15Package umlPackage) {
        ModelHelper result = (ModelHelper) instances.get(umlPackage);
        if (result == null) {
            result = new ModelHelper(umlPackage);
            instances.put(umlPackage, result);
        }
        return result;
    }

    private Collection getTopElements() {
        Collection result = new HashSet();
        Iterator it = this.umlPackage.getCore().getModelElement().refAllOfType().iterator();
        while (it.hasNext()) {
            ModelElement me = (ModelElement) it.next();
            if (me.refImmediateComposite() == null) {
                result.add(me);
            }
        }
        return result;
    }

    /**
     * @return  the top-level model (may be a "sythesized" one)
     */
    public Model getTopPackage() {
        return model;
    }
}
