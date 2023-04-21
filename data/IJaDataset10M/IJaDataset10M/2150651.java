package org.regola.codeassistence;

import org.regola.codeassistence.generator.ApplicationPropertiesGenerator;
import org.regola.codeassistence.generator.CustomDaoGenerator;
import org.regola.codeassistence.generator.CustomManagerGenerator;
import org.regola.codeassistence.generator.FilterGenerator;
import org.regola.codeassistence.generator.FlexClientClassesGenerator;
import org.regola.codeassistence.generator.FlowMasterDetailsGenerator;
import org.regola.codeassistence.generator.FlowMvcMasterDetailsGenerator;
import org.regola.codeassistence.generator.FormManagedBeanGenerator;
import org.regola.codeassistence.generator.FormPageGenerator;
import org.regola.codeassistence.generator.Generator;
import org.regola.codeassistence.generator.ListManagedBeanGenerator;
import org.regola.codeassistence.generator.ListPageGenerator;
import org.regola.codeassistence.generator.MockGenerator;
import org.regola.codeassistence.generator.PortletGenerator;
import org.regola.codeassistence.generator.ServiceManagerGenerator;
import org.regola.codeassistence.generator.SoapServiceGenerator;
import org.regola.codeassistence.generator.VariablesListGenerator;
import org.regola.util.Ognl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Raccoglie le opzioni impostate da riga di comando quando si lancia l'assistente alla generazione del codice.
 * @author  nicola
 */
public class Options {

    /**
	 * @uml.property  name="allGenerators"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
    static Generator[] allGenerators = { new ApplicationPropertiesGenerator(), new CustomDaoGenerator(), new FilterGenerator(), new FormPageGenerator(), new ListManagedBeanGenerator(), new FormManagedBeanGenerator(), new CustomManagerGenerator(), new ListPageGenerator(), new MockGenerator(), new VariablesListGenerator(), new FlowMasterDetailsGenerator(), new FlowMvcMasterDetailsGenerator(), new SoapServiceGenerator(), new PortletGenerator(), new FlexClientClassesGenerator() };

    String modelClass;

    private static Map<String, Generator> generatorsMap = new HashMap<String, Generator>();

    static {
        for (Generator gen : allGenerators) {
            getGeneratorsMap().put(gen.getName(), gen);
        }
    }

    public static Generator getGenerator(String name) {
        return getGeneratorsMap().get(name);
    }

    public static Generator[] getAllGenerators() {
        return allGenerators;
    }

    public static Generator[] getDAOGenerators() {
        return new Generator[] { new CustomDaoGenerator(), new FilterGenerator(), new MockGenerator(), new VariablesListGenerator() };
    }

    public static Generator[] getServiceGenerators() {
        return new Generator[] { new CustomManagerGenerator(), new SoapServiceGenerator() };
    }

    public static Generator[] getPresentationGenerators() {
        return new Generator[] { new ApplicationPropertiesGenerator(), new ListPageGenerator(), new FormPageGenerator(), new ListManagedBeanGenerator(), new FormManagedBeanGenerator(), new FlowMasterDetailsGenerator(), new FlowMvcMasterDetailsGenerator(), new PortletGenerator(), new FlexClientClassesGenerator() };
    }

    /**
	 * @return  the modelClass
	 * @uml.property  name="modelClass"
	 */
    public String getModelClass() {
        return modelClass;
    }

    /**
	 * @param modelClass  the modelClass to set
	 * @uml.property  name="modelClass"
	 */
    public void setModelClass(String modelClass) {
        this.modelClass = modelClass;
    }

    public Options(String className) {
        modelClass = className;
    }

    @SuppressWarnings("unchecked")
    public static Generator[] getGeneratorListByNames(String[] names) {
        List<Generator> list = new ArrayList<Generator>();
        for (String name : names) {
            List<Generator> gens = (List<Generator>) Ognl.getValue("#root.{^ #this.name == '" + name + "'}", allGenerators);
            if (gens.size() == 1 && gens.get(0) != null) list.add(gens.get(0));
        }
        Generator[] gn = {};
        return list.toArray(gn);
    }

    public static Map<String, Generator> getGeneratorsMap() {
        return generatorsMap;
    }
}
