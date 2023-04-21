package org.norecess.nolatte.drivers;

import org.antlr.runtime.RecognitionException;
import org.norecess.nolatte.IncludeFileException;
import org.norecess.nolatte.NoLatteTypeException;
import org.norecess.nolatte.ast.Datum;
import org.norecess.nolatte.ast.IGroupOfData;
import org.norecess.nolatte.ast.support.IDatumFactory;
import org.norecess.nolatte.environments.InvalidVariableException;
import org.norecess.nolatte.interpreters.IInterpreter;
import org.norecess.nolatte.interpreters.IInterpreterFactory;
import org.norecess.nolatte.interpreters.InterpreterException;
import org.norecess.nolatte.primitives.system.ErrorPrimitiveException;
import org.norecess.nolatte.primitives.system.ProcessOutputException;
import org.norecess.nolatte.system.ISystem;
import org.norecess.nolatte.types.IDataTypeFilter;

public class LatteDriver implements ILatteDriver {

    private final ISystem mySystem;

    private final IDataTypeFilter myDataTypeFilter;

    private final IDatumFactory myDatumFactory;

    private final IInterpreterFactory myInterpreterFactory;

    private final IEnvironmentBuilder myEnvironmentBuilder;

    private final ILatteDriverHelper myHelper;

    private final IProgramBuilder myProgramBuilder;

    private final Appendable myAppendable;

    private final IProblemHandler myProblemHandler;

    private final ILatteDriver myRecursion;

    public LatteDriver(ISystem system, IDataTypeFilter dataTypeFilter, IDatumFactory datumFactory, IInterpreterFactory interpreterFactory, IEnvironmentBuilder environmentBuilder, ILatteDriverHelper helper, IProgramBuilder programBuilder, Appendable appendable, IProblemHandler problemHandler) {
        mySystem = system;
        myDataTypeFilter = dataTypeFilter;
        myDatumFactory = datumFactory;
        myInterpreterFactory = interpreterFactory;
        myEnvironmentBuilder = environmentBuilder;
        myHelper = helper;
        myProgramBuilder = programBuilder;
        myAppendable = appendable;
        myProblemHandler = problemHandler;
        myRecursion = this;
    }

    public LatteDriver(ISystem system, IDataTypeFilter dataTypeFilter, IDatumFactory datumFactory, IInterpreterFactory interpreterFactory, IEnvironmentBuilder environmentBuilder, ILatteDriverHelper helper, IProgramBuilder programBuilder, Appendable appendable, IProblemHandler problemHandler, ILatteDriver recursion) {
        mySystem = system;
        myDataTypeFilter = dataTypeFilter;
        myDatumFactory = datumFactory;
        myInterpreterFactory = interpreterFactory;
        myEnvironmentBuilder = environmentBuilder;
        myHelper = helper;
        myProgramBuilder = programBuilder;
        myAppendable = appendable;
        myProblemHandler = problemHandler;
        myRecursion = recursion;
    }

    public int evaluateAndAppendSafely() {
        try {
            return myRecursion.evaluateAndAppend();
        } catch (ErrorPrimitiveException e) {
            return myProblemHandler.handle(e);
        } catch (ProcessOutputException e) {
            return myProblemHandler.handle(e);
        } catch (InvalidVariableException e) {
            return myProblemHandler.handle(e);
        } catch (NoLatteTypeException e) {
            return myProblemHandler.handle(e);
        } catch (IncludeFileException e) {
            return myProblemHandler.handle(e);
        } catch (InterpreterException e) {
            return myProblemHandler.handle(e);
        } catch (RuntimeException e) {
            return myProblemHandler.handle(e);
        } catch (RecognitionException e) {
            return myProblemHandler.handle(e);
        }
    }

    public int evaluateAndAppend() throws RecognitionException {
        myDataTypeFilter.convertToTextAndAppend(postProcess(interpretProgram()), myAppendable);
        return ProblemHandler.EXIT_NORMAL;
    }

    private Datum postProcess(IGroupOfData evaluation) {
        Datum datum = evaluation.accept(myHelper.createPostProcessor());
        return datum;
    }

    private IGroupOfData interpretProgram() throws RecognitionException {
        IGroupOfData group = createInterpreter().interpretGroup(myProgramBuilder.createProgram());
        return group;
    }

    private IInterpreter createInterpreter() {
        return myInterpreterFactory.createInterpreter(mySystem, myDataTypeFilter, myDatumFactory, myEnvironmentBuilder.createEnvironment());
    }
}
