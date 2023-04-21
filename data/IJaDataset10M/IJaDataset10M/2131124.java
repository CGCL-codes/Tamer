package org.sodeja.generator.impl;

import java.io.PrintWriter;
import java.util.List;
import org.sodeja.generator.GeneratorContext;
import org.sodeja.generator.java.JavaClass;
import org.sodeja.generator.uml.UmlClass;
import org.sodeja.generator.uml.UmlModel;
import org.sodeja.generator.uml.UmlNamespace;
import org.sodeja.generator.uml.UmlOperation;
import org.sodeja.generator.uml.UmlPackage;
import org.sodeja.generator.xml.spring.SpringBean;
import org.sodeja.generator.xml.spring.SpringConfiguration;

public class SpringDaoConfigurationGenerator extends AbstractConfigurationGenerator {

    private static final SpringBean HIBERNATE_SESSION_FACTORY = new SpringBean("hibernateSessionFactory", null);

    private static final String BASE_DAO_NAME = "baseDao";

    private boolean daoOnly = false;

    @Override
    public void generate(GeneratorContext ctx, UmlModel model) {
        try {
            PrintWriter writer = new PrintWriter(getFileWriter());
            SpringConfiguration config = generateConfiguration(ctx, model);
            config.write(writer);
            writer.close();
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }

    private SpringConfiguration generateConfiguration(GeneratorContext ctx, UmlModel model) {
        SpringConfiguration config = new SpringConfiguration();
        generateDaoParent(config);
        List<UmlClass> modelClasses = model.findClassesByStereotype(getStereotype());
        for (UmlClass modelClass : modelClasses) {
            if (!GeneratorUtils.isCrud(modelClass)) {
                continue;
            }
            generateDao(config, modelClass);
        }
        return config;
    }

    private void generateDaoParent(SpringConfiguration config) {
        config.add(new SpringBean(BASE_DAO_NAME, getDaoInterface().getFullName()));
        config.current().setAbstract(true);
        config.current().addReference("sessionFactory", HIBERNATE_SESSION_FACTORY);
    }

    private void generateDao(SpringConfiguration config, UmlClass modelClass) {
        config.add(new SpringBean(getBeanName(modelClass), getDaoName(modelClass)));
        config.current().setParent(config.findByName(BASE_DAO_NAME));
        config.current().addValue("persistentClass", modelClass.getFullName());
    }

    private String getBeanName(UmlClass modelClass) {
        return NamingUtils.firstLower(modelClass.getName()) + "Dao";
    }

    private String getDaoName(UmlClass modelClass) {
        if (!isCustomDao(modelClass)) {
            return getDaoImplementation().getFullName();
        }
        UmlNamespace modelRootPackage = modelClass.getParentNamespace().getParent();
        UmlPackage modelDaoPackage = new UmlPackage(modelRootPackage);
        modelDaoPackage.setName("dao");
        return String.format("%s.%s", modelDaoPackage.getFullName(), modelClass.getName() + "DaoImpl");
    }

    private boolean isCustomDao(UmlClass modelClass) {
        if (!daoOnly) {
            return true;
        }
        for (UmlOperation modelOperation : modelClass.getOperations()) {
            if (GeneratorUtils.isDao(modelOperation)) {
                return true;
            }
        }
        return false;
    }

    protected JavaClass getDaoInterface() {
        return HibernateDaoGenerator.PARENT_DAO;
    }

    protected JavaClass getDaoImplementation() {
        return HibernateDaoGenerator.PARENT_DAO_IMPL;
    }
}
