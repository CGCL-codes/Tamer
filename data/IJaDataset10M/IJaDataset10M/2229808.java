package tudresden.ocl20.pivot.interpreter.test.royalsandloyals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.Platform;
import tudresden.ocl20.pivot.facade.Ocl2ForEclipseFacade;
import tudresden.ocl20.pivot.interpreter.IInterpretationResult;
import tudresden.ocl20.pivot.model.IModel;
import tudresden.ocl20.pivot.model.ModelAccessException;
import tudresden.ocl20.pivot.modelinstance.IModelInstance;
import tudresden.ocl20.pivot.modelinstancetype.exception.OperationNotFoundException;
import tudresden.ocl20.pivot.modelinstancetype.exception.TypeNotFoundInModelException;
import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceElement;
import tudresden.ocl20.pivot.parser.ParseException;
import tudresden.ocl20.pivot.pivotmodel.Constraint;
import tudresden.ocl20.pivot.pivotmodel.ConstraintKind;
import tudresden.ocl20.pivot.pivotmodel.Operation;

/**
 * <p>
 * This class loads a given model file, a given model instance and given OCL
 * files. Then it can be used to perform some interpreter tests.
 * </p>
 * 
 * @author Claas Wilke
 */
public class TestPerformer {

    /** The package of the UML2 meta model. */
    private static final String META_MODEL = Ocl2ForEclipseFacade.UML2_MetaModel;

    /** The name of the bundle of the model file. */
    private static final String MODEL_BUNDLE = "tudresden.ocl20.pivot.examples.royalandloyal";

    /** The path of the UML model file. */
    private static final String MODEL_FILE = "model/royalsandloyals.uml";

    /**
	 * Contains the directory where the OCL files are stored which shall be
	 * parsed and imported.
	 */
    protected String fileDirectory = "";

    /** Contains the loaded UML2 model. */
    protected IModel myModel = null;

    /** Contains the loaded UML2 model instance. */
    protected IModelInstance myModelInstance = null;

    /**
	 * <p>
	 * Creates a new TestPerformer.
	 * </p>
	 */
    public TestPerformer() {
        super();
        this.init();
    }

    /**
	 * <p>
	 * Adapts a given {@link Object} as {@link IModelInstanceElement} and adds
	 * it to the {@link IModelInstance} under test.
	 * </p>
	 * 
	 * @param object
	 *            The {@link Object} that shall be adapted and added.
	 * @return The adapted {@link IModelInstanceElement}.
	 * @throws TypeNotFoundInModelException
	 */
    public IModelInstanceElement addModelObject(Object object) throws TypeNotFoundInModelException {
        IModelInstanceElement result;
        result = myModelInstance.addModelInstanceElement(object);
        return result;
    }

    /**
	 * <p>
	 * Searches for a {@link Operation} of a given {@link IModelInstanceElement}
	 * in its {@link IModel}.
	 * </p>
	 * 
	 * @param imiElement
	 *            The {@link IModelInstanceElement}.
	 * @param name
	 *            The name of the {@link Operation}.
	 * @return The found {@link Operation}.
	 * @throws OperationNotFoundException
	 *             Thrown, if the {@link Operation} has not been found.
	 */
    public Operation findOperation(IModelInstanceElement imiElement, String name) throws OperationNotFoundException {
        for (Operation ownedOperation : imiElement.getType().getOwnedOperation()) {
            if (ownedOperation.getName().equals(name)) {
                return ownedOperation;
            }
        }
        throw new OperationNotFoundException("Cannot find operation " + name + " on " + imiElement);
    }

    /**
	 * <p>
	 * Initializes the {@link TestPerformer} after all required parameters are
	 * set.
	 * </p>
	 * 
	 * @throws RuntimeException
	 *             Is thrown if any error occurred while loading the model or
	 *             the meta model.
	 */
    public void init() throws RuntimeException {
        try {
            fileDirectory = Platform.getBundle(MODEL_BUNDLE).getLocation();
            fileDirectory = fileDirectory.substring(15);
            this.loadModel();
            this.loadModelInstance();
        } catch (Exception e) {
            throw new RuntimeException("Unable to initialize the test. Reason: " + e.getMessage());
        }
    }

    /**
	 * <p>
	 * Interprets a given {@link Constraint} for a given {@link List} of
	 * {@link IModelInstanceElement}s.
	 * </p>
	 * 
	 * @param constrraint
	 *            The {@link Constraint} that shall be interpreted.
	 * @param modelObjects
	 *            The {@link IModelInstanceElement}s that shall be interpreted.
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 * @result A {@link List} containing the {@link IInterpretationResult}s of
	 *         the interpretation.
	 */
    public List<IInterpretationResult> interpretConstraint(Constraint constraint, List<IModelInstanceElement> modelObjects, boolean removeConstraints) throws IllegalArgumentException, ModelAccessException {
        List<IInterpretationResult> result;
        result = new ArrayList<IInterpretationResult>();
        for (IModelInstanceElement modelObject : modelObjects) {
            result.add(Ocl2ForEclipseFacade.interpretConstraint(constraint, this.myModelInstance, modelObject));
        }
        if (removeConstraints) {
            this.myModel.removeAllConstraints();
        }
        return result;
    }

    /**
	 * <p>
	 * Interprets a given {@link Constraint} of the
	 * {@link ConstraintKind#POSTCONDITION} for a given
	 * {@link IModelInstanceElement}.
	 * </p>
	 * 
	 * @param constraint
	 *            The {@link Constraint} that shall be interpreted.
	 * @param modelInstanceElements
	 *            The {@link IModelInstanceElement} that shall be interpreted.
	 * @param operation
	 *            The {@link Operation} whose postconditions shall be
	 *            interpreted.
	 * @param parameters
	 *            The parameter values of the {@link Operation} for which the
	 *            postconditions shall be interpreted.
	 * @param resultValue
	 *            The result of the {@link Operation}'s invocation for that the
	 *            postconditions shall be interpreted.
	 * @param removeConstraints
	 *            Indicates, whether or not all {@link Constraint} shall be
	 *            removed from the {@link IModel} after this interpretation.
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 * @result The {@link IInterpretationResult} of the interpretation.
	 */
    public List<IInterpretationResult> interpretPostCondition(Constraint constraint, List<IModelInstanceElement> modelInstanceElements, Operation operation, List<IModelInstanceElement> parameters, IModelInstanceElement resultValue, boolean removeConstraints) throws IllegalArgumentException, ModelAccessException {
        List<IInterpretationResult> result;
        result = new ArrayList<IInterpretationResult>();
        List<Constraint> constraints;
        constraints = new ArrayList<Constraint>();
        constraints.add(constraint);
        for (IModelInstanceElement modelInstanceElement : modelInstanceElements) {
            result.addAll(Ocl2ForEclipseFacade.interpretPostConditions(this.myModelInstance, modelInstanceElement, operation, parameters, resultValue, constraints));
        }
        if (removeConstraints) {
            this.myModel.removeAllConstraints();
        }
        return result;
    }

    /**
	 * <p>
	 * Interprets a given {@link Constraint} of the
	 * {@link ConstraintKind#PRECONDITION} for a given
	 * {@link IModelInstanceElement}.
	 * </p>
	 * 
	 * @param constraint
	 *            The {@link Constraint} that shall be interpreted.
	 * @param modelInstanceElements
	 *            The {@link IModelInstanceElement} that shall be interpreted.
	 * @param operation
	 *            The {@link Operation} whose preconditions shall be
	 *            interpreted.
	 * @param parameters
	 *            The parameter values of the {@link Operation} for which the
	 *            preconditions shall be interpreted.
	 * @param removeConstraints
	 *            Indicates, whether or not all {@link Constraint} shall be
	 *            removed from the {@link IModel} after this interpretation.
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 * @result The {@link IInterpretationResult} of the interpretation.
	 */
    public List<IInterpretationResult> interpretPreCondition(Constraint constraint, List<IModelInstanceElement> modelInstanceElements, Operation operation, List<IModelInstanceElement> parameters, boolean removeConstraints) throws IllegalArgumentException, ModelAccessException {
        List<IInterpretationResult> result;
        result = new ArrayList<IInterpretationResult>();
        List<Constraint> constraints;
        constraints = new ArrayList<Constraint>();
        constraints.add(constraint);
        for (IModelInstanceElement modelInstanceElement : modelInstanceElements) {
            result.addAll(Ocl2ForEclipseFacade.interpretPreConditions(this.myModelInstance, modelInstanceElement, operation, parameters, constraints));
        }
        if (removeConstraints) {
            this.myModel.removeAllConstraints();
        }
        return result;
    }

    /**
	 * <p>
	 * Parses the file <i>oclFileName</i> against the loaded UML model file. If
	 * an error occurred an CodeGenerationException will be thrown.
	 * </p>
	 * 
	 * @param oclFileName
	 *            The OCL file to be parsed.
	 * @return A {@link List} containing the parsed {@link Constraint}s.
	 * @throws ModelAccessException
	 * @throws ParseException
	 * @throws Ocl22JavaException
	 *             Is thrown if any error occurs
	 */
    public List<Constraint> loadOCLFile(String oclFileName) throws RuntimeException, ParseException, ModelAccessException {
        List<Constraint> result;
        File oclFile;
        oclFile = new File(fileDirectory + oclFileName);
        if (!oclFile.exists()) {
            String msg;
            msg = "The given OCL file does not exist. File name: ";
            msg += oclFileName + ".";
            throw new RuntimeException(msg);
        }
        result = Ocl2ForEclipseFacade.parseConstraints(oclFile, this.myModel, true);
        return result;
    }

    /**
	 * <p>
	 * Prepares a given {@link Constraint} of the
	 * {@link ConstraintKind#POSTCONDITION} for a given
	 * {@link IModelInstanceElement} (necessary to store <code>@pre</code>
	 * values).
	 * </p>
	 * 
	 * @param constraint
	 *            The {@link Constraint} that shall be interpreted.
	 * @param modelInstanceElements
	 *            The {@link IModelInstanceElement} that shall be interpreted.
	 * @param operation
	 *            The {@link Operation} whose postconditions shall be
	 *            interpreted.
	 * @param parameters
	 *            The parameter values of the {@link Operation} for which the
	 *            postconditions shall be interpreted.
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 * @result The {@link IInterpretationResult} of the interpretation.
	 */
    public List<IInterpretationResult> preparePostCondition(Constraint constraint, List<IModelInstanceElement> modelInstanceElements, Operation operation, List<IModelInstanceElement> parameters) throws IllegalArgumentException, ModelAccessException {
        List<IInterpretationResult> result;
        result = new ArrayList<IInterpretationResult>();
        List<Constraint> constraints;
        constraints = new ArrayList<Constraint>();
        constraints.add(constraint);
        for (IModelInstanceElement modelInstanceElement : modelInstanceElements) {
            Ocl2ForEclipseFacade.preparePostConditions(this.myModelInstance, modelInstanceElement, operation, parameters, constraints);
        }
        return result;
    }

    /**
	 * <p>
	 * Loads the {@link IModel} used for testing.
	 * </p>
	 * 
	 * @throws Ocl22JavaException
	 *             Is thrown if the {@link IModel} cannot be initialized or the
	 *             model file is not found.
	 */
    protected void loadModel() throws RuntimeException {
        if (!(this.myModel != null && this.myModel.getDisplayName().equals(MODEL_FILE))) {
            File modelFile;
            modelFile = new File(this.fileDirectory + MODEL_FILE);
            if (!modelFile.exists()) {
                String msg;
                msg = "The model file ";
                msg += this.fileDirectory + MODEL_FILE;
                msg += " doesn't exists.";
                throw new RuntimeException(msg);
            }
            try {
                this.myModel = Ocl2ForEclipseFacade.getModel(modelFile, META_MODEL);
            } catch (ModelAccessException e) {
                throw new RuntimeException("The model could not be loaded. " + e.getMessage());
            }
        }
    }

    /**
	 * <p>
	 * Loads a new empty {@link IModelInstance} of the actual selected
	 * {@link IModel}.
	 * </p>
	 * 
	 * @throws RuntimeException
	 *             Thrown, if an error during {@link IModelInstance}
	 *             initialization occurs.
	 */
    protected void loadModelInstance() throws RuntimeException {
        if (this.myModel != null) {
            this.myModelInstance = null;
            try {
                this.myModelInstance = Ocl2ForEclipseFacade.getEmptyModelInstance(this.myModel, Ocl2ForEclipseFacade.JAVA_MODEL_INSTANCE_TYPE);
            } catch (ModelAccessException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        } else {
            throw new RuntimeException("No model found to load a model instance.");
        }
    }
}
