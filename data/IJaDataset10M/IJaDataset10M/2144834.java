package net.simplemodel.core.generator.internal;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import net.simplemodel.core.SimplemodelCore;
import net.simplemodel.core.generator.IGenerator;
import net.simplemodel.core.generator.IGeneratorFactory;
import net.simplemodel.core.generator.IGeneratorItem;
import net.simplemodel.core.generator.IGeneratorOptions;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.codegen.merge.java.JControlModel;
import org.eclipse.emf.codegen.merge.java.JMerger;
import org.eclipse.emf.codegen.merge.java.facade.ast.ASTFacadeHelper;

public class GeneratorFactory implements IGeneratorFactory {

    private static final JMergerFactory JMERGER_FACTORY = new JMergerFactory() {

        @Override
        public JMerger create() {
            String base = Platform.getBundle(SimplemodelCore.PLUGIN_ID).getEntry("/").toString() + "templates/";
            JControlModel model = new JControlModel();
            ASTFacadeHelper astFacadeHelper = new ASTFacadeHelper();
            model.initialize(astFacadeHelper, base + "Simplemodel-Merge.xml");
            return new JMerger(model);
        }
    };

    @Override
    public IGenerator createGenerator(IProject project, Map<String, String> options) {
        List<IGeneratorItem> generatorItems = new LinkedList<IGeneratorItem>();
        TemplateGeneratorItem baseClass = new TemplateGeneratorItem(project.getFolder(options.get(IGeneratorOptions.SRC_FOLDER)), new BaseClassTemplate(), "generatorContext.baseClass.packageName", "generatorContext.baseClass.simpleName", "subject==null");
        TemplateGeneratorItem baseInterface = new TemplateGeneratorItem(project.getFolder(options.get(IGeneratorOptions.SRC_FOLDER)), new BaseInterfaceTemplate(), "generatorContext.baseInterface.packageName", "generatorContext.baseInterface.simpleName", "subject==null");
        TemplateGeneratorItem factoryClass = new TemplateGeneratorItem(project.getFolder(options.get(IGeneratorOptions.SRC_FOLDER)), new FactoryClassTemplate(), "generatorContext.factoryClass.packageName", "generatorContext.factoryClass.simpleName", "subject==null");
        TemplateGeneratorItem factoryInterface = new TemplateGeneratorItem(project.getFolder(options.get(IGeneratorOptions.SRC_FOLDER)), new FactoryInterfaceTemplate(), "generatorContext.factoryInterface.packageName", "generatorContext.factoryInterface.simpleName", "subject==null");
        TemplateGeneratorItem interfaceObject = new TemplateGeneratorItem(project.getFolder(options.get(IGeneratorOptions.SRC_FOLDER)), new InterfaceTemplate(), "generatorContext.baseInterface.packageName", "subject.createDeclarationTypeReference().simpleName", "subject!=null && subject instanceof net.simplemodel.core.ast.SMTypeDeclaration && subject.createDeclarationTypeReference()!=null");
        TemplateGeneratorItem classObject = new TemplateGeneratorItem(project.getFolder(options.get(IGeneratorOptions.SRC_FOLDER)), new ClassTemplate(), "generatorContext.baseClass.packageName", "subject.createImplementationTypeReference().simpleName", "subject!=null && subject instanceof net.simplemodel.core.ast.SMTypeDeclaration && subject.createImplementationTypeReference()!=null");
        TemplateGeneratorItem testObject = new TemplateGeneratorItem(project.getFolder(options.get(IGeneratorOptions.TEST_FOLDER)), new TestTemplate(), "generatorContext.baseClass.packageName", "subject.createImplementationTypeReference().simpleName+\"Test\"", "subject!=null && subject instanceof net.simplemodel.core.ast.SMTypeDeclaration && subject.createImplementationTypeReference()!=null && !subject.isInterface() && !subject.isAbstract()");
        generatorItems.add(baseInterface);
        generatorItems.add(baseClass);
        generatorItems.add(factoryInterface);
        generatorItems.add(factoryClass);
        generatorItems.add(interfaceObject);
        generatorItems.add(classObject);
        if (Boolean.valueOf(options.get(IGeneratorOptions.GENERATE_TESTS))) {
            generatorItems.add(testObject);
        }
        for (ListIterator<IGeneratorItem> iterator = generatorItems.listIterator(); iterator.hasNext(); ) {
            IGeneratorItem generatorItem = iterator.next();
            if (Boolean.parseBoolean(options.get(IGeneratorOptions.USE_MERGER))) {
                generatorItem = new MergeGeneratorItem(JMERGER_FACTORY, generatorItem);
            }
            if (Boolean.parseBoolean(options.get(IGeneratorOptions.FORMAT))) {
                generatorItem = new FormatGeneratorItem(generatorItem);
            }
            generatorItem = new MakeDerivedGeneratorItem(generatorItem, Boolean.parseBoolean(options.get(IGeneratorOptions.MAKE_DERIVED)));
            iterator.set(generatorItem);
        }
        return new Generator(generatorItems);
    }
}
