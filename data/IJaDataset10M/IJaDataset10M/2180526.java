package jacg;

import jacg.model.Model;
import jacg.model.ModelClass;
import jacg.validation.Validator;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Cartridge {

    private String name = "Cartridge";

    private List transformations = new ArrayList();

    private Map generatorMap = new HashMap();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addTransformation(ModelTransformation t) {
        transformations.add(t);
    }

    public List getModelTransformations() {
        return transformations;
    }

    public void addGenerator(String stereotype, Generator g) {
        List l = (List) generatorMap.get(stereotype);
        if (l == null) {
            l = new ArrayList();
            generatorMap.put(stereotype, l);
        }
        l.add(g);
    }

    public List getGenerators(String stereotype) {
        List l = (List) generatorMap.get(stereotype);
        if (l == null) {
            l = new ArrayList();
            generatorMap.put(stereotype, l);
        }
        return l;
    }

    public void doTransformations(Model m) {
        System.out.println("Cartridge[" + getName() + "]: ModelCheck and Transformations...");
        Validator modelChecker = new Validator();
        modelChecker.setModel(m);
        Iterator iModelTransformations = getModelTransformations().iterator();
        while (iModelTransformations.hasNext()) {
            ModelTransformation mt = (ModelTransformation) iModelTransformations.next();
            modelChecker.addTransformation((ModelTransformation) mt);
        }
        modelChecker.validateModel();
        System.out.println("Cartridge[" + getName() + "]: ModelCheck and Transformations finished.");
    }

    public void doGeneration(Model m) throws Exception {
        doTransformations(m);
        ModelClass mc;
        System.out.println("Cartridge[" + getName() + "]: Starting generation.");
        Enumeration eClasses = (Enumeration) m.getClasses();
        while (eClasses.hasMoreElements()) {
            mc = (ModelClass) eClasses.nextElement();
            if (getGenerators(mc.getStereotype()) == null) {
                continue;
            }
            Iterator iGenerators = getGenerators(mc.getStereotype()).iterator();
            while (iGenerators.hasNext()) {
                Generator g = (Generator) iGenerators.next();
                g.setModel(m);
                g.generate(mc);
            }
        }
        System.out.println("Cartridge[" + getName() + "]: Generation finished. ");
    }
}
