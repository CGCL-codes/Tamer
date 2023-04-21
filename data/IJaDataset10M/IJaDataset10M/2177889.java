package ch.sahits.codegen.java.generator.ast;

import java.io.IOException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import ch.sahits.codegen.core.util.ILogger;
import ch.sahits.codegen.core.util.LogFactory;
import ch.sahits.codegen.generator.IGenerator;
import ch.sahits.codegen.i18n.JavaMessages;
import ch.sahits.codegen.java.extensions.ReferenceImplementation;
import ch.sahits.codegen.java.generator.IReferenceImpleCaller;
import ch.sahits.codegen.java.generator.jettemplate.InitializableDbBeanGenerator;
import ch.sahits.codegen.model.IJetGatewayConfiguration;
import ch.sahits.model.java.IGeneratedJavaDBClass2;

/**
 * This class is a generator class that copies the delete method into the provided base class. 
 * This generator class needs a reference implementation, hence it implements the marker 
 * interface   {@link IReferenceImpleCaller} via {@link AbstractReferenceImplCaller}.
 * @author   Andi Hotz
 * @since 0.9.0
 */
public final class ASTBeanWithDelete extends AbstractReferenceImplCaller implements IGenerator, IASTGeneratorInitializer {

    private ILogger logger = LogFactory.getLogger();

    /** Model of the database-Table */
    private IGeneratedJavaDBClass2 model = null;

    /**
	 * class rump to start from
	 * @uml.property  name="baseClass"
	 */
    private String baseClass = "";

    /** Parser for the <code>unit</code> */
    private ASTParser parser = null;

    /** Document on which the modifications are made */
    private Document document = null;

    /** This class should inherit from both {@link AbstractReferenceImplCaller}
	 * and {@link ASTGenerator} therefore implement the missing methods for
	 * {@link ASTGenerator} through a fa�ade
	 */
    private ASTGenerator generator = null;

    /**
	 * Workload for the generation of JETemplate generation
	 */
    private int superWorkLoad = 0;

    /**
	 * Default Constructor
	 */
    public ASTBeanWithDelete() {
    }

    /**
	 * Generate the code
	 * @return Source code as string
	 */
    @SuppressWarnings("unchecked")
    public String generate() {
        if (!isInitialized()) {
            throw new IllegalStateException(JavaMessages.ASTBeanWithDelete_2);
        }
        initUnit();
        String refPath = model.getReferenceImplementationPath();
        try {
            Class c = ReferenceImplementation.getRefImpl(model.getReferencePluginID(), refPath).getClass();
            ReferenceAST ref = new ReferenceAST(c, model);
            addMethods(ref);
            addImports(ref);
            TextEdit edits = unit.rewrite(document, null);
            edits.apply(document);
        } catch (JavaModelException e) {
            logger.addException(e);
            logger.log(e);
        } catch (IOException e) {
            logger.addException(e);
            logger.log(e);
        } catch (MalformedTreeException e) {
            logger.addException(e);
            logger.log(e);
        } catch (BadLocationException e) {
            logger.addException(e);
            logger.log(e);
        }
        String result = document.get();
        return result;
    }

    /**
	 * @param _model  the model to set
	 * @uml.property  name="_model"
	 */
    public void setModel(IGeneratedJavaDBClass2 _model) {
        Exception e = new Exception(JavaMessages.ASTBeanWithDelete_3);
        logger.logInfo(e, JavaMessages.ASTBeanWithDelete_4);
        this.model = _model;
    }

    /**
	 * @param _baseClass  the baseClass to set
	 * @uml.property  name="baseClass"
	 */
    public void setBaseClass(String _baseClass) {
        Exception e = new Exception(JavaMessages.ASTBeanWithDelete_5);
        logger.logInfo(e, JavaMessages.ASTBeanWithDelete_6);
        this.baseClass = _baseClass;
        assert this.baseClass != null;
    }

    /**
	 * Check whether all members are initialized
	 * @return true if the model and the baseClass are initialized
	 */
    public boolean isInitialized() {
        return (!baseClass.trim().equals("")) && (model != null);
    }

    /**
	 * Initializes the CompilationUnit
	 */
    private void initUnit() {
        parser = ASTParser.newParser(AST.JLS3);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setSource(baseClass.toCharArray());
        parser.setResolveBindings(true);
        parser.setBindingsRecovery(true);
        unit = (CompilationUnit) parser.createAST(null);
        ast = unit.getAST();
        unit.recordModifications();
        document = new Document(baseClass);
    }

    /**
	 * Null implementation
	 * Do nothing since the initialization is done
	 * with {@link #init()}
	 * @param config Configuration
	 */
    public void init(IJetGatewayConfiguration config) {
    }

    /**
	 * @see ch.sahits.codegen.java.generator.ast.IASTGeneratorInitializer#setGenerator(ch.sahits.codegen.java.generator.ast.ASTGenerator)
	 */
    public void setGenerator(ASTGenerator _generator) {
        this.generator = _generator;
    }

    /**
	 * Clean up the generator
	 * @throws CoreException 
	 */
    public void cleanup() throws CoreException {
        generator.cleanup();
    }

    /**
	 * Retrieve the configuration object
	 * @return Configuration of this generator
	 */
    public IJetGatewayConfiguration getConfig() {
        return generator.getConfig();
    }

    /**
	 * Retrieve the workload for the generation
	 * @return workload
	 */
    public int getWorkload() {
        return generator.getWorkload() + superWorkLoad;
    }

    /**
	 * Initialize this instance
	 */
    public void init() {
        generator.init();
        InitializableDbBeanGenerator jetGenerator = new InitializableDbBeanGenerator();
        IJetGatewayConfiguration conf = getConfig();
        jetGenerator.init(conf);
        baseClass = jetGenerator.generate();
        assert baseClass != null;
        model = (IGeneratedJavaDBClass2) conf.getModel();
        superWorkLoad = jetGenerator.getWorkload();
    }

    /**
	 * Save to file
	 * @param monitor Progress-Monitor
	 * @param contents Byte array with the contents to be saved
	 * @return IFile instance of the saved contents
	 * @throws CoreException 
	 */
    public IFile save(IProgressMonitor monitor, byte[] contents) throws CoreException {
        return generator.save(monitor, contents);
    }

    /**
	 * Set the progress-Monitor
	 * @param monitor Progress Monitor
	 */
    public void setMonitor(IProgressMonitor monitor) {
        generator.setMonitor(monitor);
    }
}
