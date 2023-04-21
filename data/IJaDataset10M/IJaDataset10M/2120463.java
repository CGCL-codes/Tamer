package ch.sahits.codegen.java.internal.wizards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import ch.sahits.model.IBuilder;
import ch.sahits.codegen.i18n.JavaMessages;
import ch.sahits.codegen.java.model.HeadlessJavaModelFactory;
import ch.sahits.codegen.java.model.JavaClassDefinition;
import ch.sahits.codegen.java.wizards.IJavaCodeGeneratorDelegate;
import ch.sahits.codegen.wizards.DynamicDBGenerationCode;
import ch.sahits.codegen.wizards.EGenerationType;
import ch.sahits.model.IBuildable;
import ch.sahits.model.db.IBasicDBDefinitionPage;
import ch.sahits.model.java.EVisibility;
import ch.sahits.model.java.IGeneratedJavaClassBuilder;
import ch.sahits.model.java.IGeneratedJavaDBClassBuilder;
import ch.sahits.model.java.db.IJavaDataBaseTableBuilder;

/**
 * Headless generater delegate for generated classes
 * @author Andi Hotz, Sahits GmbH
 * @since 2.1.0
 */
public class HeadlessGeneratedClassDelegateGenerator extends GeneratedClass implements IJavaCodeGeneratorDelegate {

    /**
	 * Constructor using a builder
	 * @param builder
	 */
    public HeadlessGeneratedClassDelegateGenerator(HeadlessGeneratedClassDelegateGeneratorBuilder builder) {
        super(builder);
    }

    /**
	 * Retrieve the computation type
	 * @return generation type
	 */
    public EGenerationType getComputationType() {
        return EGenerationType.valueOf(getGenerationType());
    }

    /**
	 * Compute the workload for the model generation
	 * @return workload for the computation.
	 */
    public int computeWorkload() {
        int modelGeneration = HeadlessJavaModelFactory.getModelGenerationWorkload(getModelBase(), getInputFilePath(), getDbProduct());
        return modelGeneration;
    }

    /**
	 * Convert the {@link #getGenerationBase()} to {@link DynamicDBGenerationCode}
	 * @return Code indicating which way the input model is generated (input file,DB,  ...)
	 */
    public int getModelBase() {
        return getGenerationBase();
    }

    /**
	 * Create and initialize a model for the class to be generated
	 * @param monitor Progress monitor
	 * @param dbDefintionPage current DB definition page
	 * @return class model
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
    public IBuilder<? extends IBuildable> initModel(IProgressMonitor monitor, IBasicDBDefinitionPage dbDefintionPage) throws FileNotFoundException, IOException, ClassNotFoundException {
        IGeneratedJavaClassBuilder modelB = null;
        int modelBase = getModelBase();
        if (modelBase == -1) {
            throw new RuntimeException(JavaMessages.JavaCodegenNewWizard_12);
        }
        JavaClassDefinition definition = new JavaClassDefinition(getClassName(), getPackageName(), getVisibility().equals(EVisibility.PUBLIC.name()), isAbstract(), isFinal());
        switch(modelBase) {
            case DynamicDBGenerationCode.WITH_DB_INPUT_FILE:
                modelB = HeadlessJavaModelFactory.createModelWithDBInputFile(definition, getDbProduct(), getInputFilePath(), null, monitor);
                break;
            case DynamicDBGenerationCode.WITHOUT_DB:
                modelB = HeadlessJavaModelFactory.createModelWithoutDB(definition, getInputFilePath(), monitor);
                break;
            default:
                ExtendablePseudoDBPage dbPage = new ExtendablePseudoDBPage(getDbProduct(), getDbHost(), getDbName(), getDbPort(), getDbUser());
                HeadlessJavaModelFactory.setCurrentDBDefinitionPage(dbPage);
                modelB = HeadlessJavaModelFactory.createModelWithDBConnection(definition, getDbProduct(), getDbHost(), getDbPort(), getDbName(), getDbUser(), getDbPassword(), getDbTableName(), monitor, getDbSchema());
                HeadlessJavaModelFactory.setCurrentDBDefinitionPage(null);
                break;
        }
        if (modelB == null) {
            throw new RuntimeException(JavaMessages.JavaCodegenNewWizard_13);
        }
        modelB.className(getClassName());
        if (!modelB.hasSuperClass()) {
            if (!getSuperClassName().trim().equals("")) {
                Class<? extends Object> superClass = getClass().getClassLoader().loadClass(getSuperClassName());
                modelB.addSuperClass(superClass);
            } else {
                modelB.addSuperClass(Object.class);
            }
        }
        initializeModifiers(modelB);
        if (modelB instanceof IJavaDataBaseTableBuilder) {
            ((IJavaDataBaseTableBuilder) modelB).host(getDbHost()).port(getDbPort()).password(getDbPassword());
            ((IJavaDataBaseTableBuilder) modelB).userName(getDbUser()).dbName(getDbName()).schema(getDbSchema());
        }
        if (!getPackageName().equals("")) {
            modelB.packageName(getPackageName());
        }
        String superClass = getSuperClassName();
        if (superClass.equals("") || superClass.equals(Object.class.getName())) {
        } else {
            Class<? extends Object> superClazz = getClass().getClassLoader().loadClass(superClass);
            modelB.addSuperClass(superClazz);
        }
        String src = getSrcFolderPath();
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        String projectName = src.substring(0, src.indexOf(File.separator, 1));
        IProject prj = (IProject) root.findMember(new Path(projectName));
        IJavaProject jProject = JavaCore.create(prj);
        modelB.javaProject(jProject);
        List<String> interfaces = getInterfaces();
        for (Iterator<String> iterator = interfaces.iterator(); iterator.hasNext(); ) {
            String inter = iterator.next();
            if (!inter.equals("")) {
                modelB.addInterface(getClass().getClassLoader().loadClass(inter));
            }
        }
        modelB.srcPath(getSrcFolderPath());
        modelB.inputFilePath(getInputFilePath());
        if (modelB instanceof IGeneratedJavaDBClassBuilder) {
            ((IGeneratedJavaDBClassBuilder) modelB).dbProductName(getDbProduct());
        }
        modelB.jetFileUse(isTemplate());
        modelB.generatorUse(isGenerationSelected());
        modelB.jeTemplateUse(isJetSelected());
        modelB.astTemplateUse(isASTSelected());
        if (isTemplate() || (isASTSelected() && isJetSelected())) {
            modelB.jeTemplatePath(getJetemplate());
            modelB.jetemplatePluginID(getJetemplatePluginID());
        }
        if (isGenerationSelected()) {
            modelB.generatorClassPath(getGenerator());
            modelB.generatorPluginID(getGeneratorPluginID());
        }
        if (isASTSelected()) {
            modelB.astTemplateUse(true);
            modelB.generatorClassPath(getGenerator());
            modelB.generatorPluginID(getGeneratorPluginID());
        }
        if (getReferenceImplementation() != null && !getReferenceImplementation().trim().equals("")) {
            modelB.referenceImplementationPath(getReferenceImplementation());
            modelB.referencePluginID(getReferenceClassPluginID());
        }
        return modelB;
    }

    /**
	 * Check whether AST is involved 
	 * @return true if AST is needed
	 */
    private boolean isASTSelected() {
        EGenerationType type = getComputationType();
        switch(type) {
            case AST_IMPLEMENTATION:
                return true;
            case JET_AST_GENERATION:
                return true;
            case GENERATE_JETEMPLATE:
                return false;
            case JETEMPATE:
                return false;
            case JETEMPLATE_AST_IMPROVEMENT:
                return true;
            default:
                return false;
        }
    }

    /**
	 * Check if a JETemplate is part of the generation process
	 * @return true if JET is involved
	 */
    private boolean isJetSelected() {
        EGenerationType type = getComputationType();
        switch(type) {
            case AST_IMPLEMENTATION:
                return false;
            case JET_AST_GENERATION:
                return true;
            case GENERATE_JETEMPLATE:
                return true;
            case JETEMPATE:
                return true;
            case JETEMPLATE_AST_IMPROVEMENT:
                return true;
            default:
                return false;
        }
    }

    /**
	 * Check if the code should be generated via generation
	 * @return true if all code is generated
	 */
    private boolean isGenerationSelected() {
        EGenerationType type = getComputationType();
        switch(type) {
            case AST_IMPLEMENTATION:
                return true;
            case JET_AST_GENERATION:
                return true;
            case GENERATE_JETEMPLATE:
                return true;
            case JETEMPATE:
                return false;
            case JETEMPLATE_AST_IMPROVEMENT:
                return false;
            default:
                return false;
        }
    }

    /**
	 * Check if the code should be generated with a template
	 * @return true if a JETemplete is involved
	 */
    private boolean isTemplate() {
        EGenerationType type = getComputationType();
        switch(type) {
            case AST_IMPLEMENTATION:
                return false;
            case JET_AST_GENERATION:
                return false;
            case GENERATE_JETEMPLATE:
                return false;
            case JETEMPATE:
                return true;
            case JETEMPLATE_AST_IMPROVEMENT:
                return true;
            default:
                return false;
        }
    }

    /**
	 * Initialize the modifiers
	 * @param modelB model builder
	 */
    private void initializeModifiers(IGeneratedJavaClassBuilder modelB) {
        if (isAbstract()) {
            modelB.isAbstract(true);
        } else {
            modelB.isAbstract(false);
        }
        if (isFinal()) {
            modelB.isFinal(true);
        } else {
            modelB.isFinal(false);
        }
        modelB.visibility(EVisibility.valueOf(getVisibility()));
    }
}
