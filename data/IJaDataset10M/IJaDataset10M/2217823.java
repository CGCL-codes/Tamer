package de.fzi.injectj.model;

import java.io.PrintStream;
import java.util.List;
import de.fzi.injectj.model.engine.Transaction;
import de.fzi.injectj.model.exception.ExitException;
import de.fzi.injectj.model.exception.ExplicitWeavepointException;
import de.fzi.injectj.script.engine.Environment;
import de.fzi.injectj.script.exception.FileIOException;
import de.fzi.injectj.script.model.BlockType;
import de.fzi.injectj.script.model.FileType;
import de.fzi.injectj.script.model.NamePattern;
import de.fzi.injectj.script.model.Type;

/**
 * @author gutzmann
 * 
 * @inject export name=MOPGlobals package=lang.model modifier=query,global 
 */
public abstract class GlobalFunctions {

    public abstract List _evaluateNamePattern(NamePattern _pattern, Environment environment);

    /**
	 * @inject name=class  params=1
	 */
    public abstract List _class(NamePattern _pattern, Environment environment);

    /**
     * 
     * @param _pattern
     * @param environment
     * @return
     * @inject name=interface params=1
     */
    public abstract List _interface(NamePattern _pattern, Environment environment);

    /**
     * 
     * @param _pattern
     * @param environment
     * @return
     * @inject name=type params=1
     */
    public List _type(NamePattern _pattern, Environment environment) {
        List res = _interface(_pattern, environment);
        res.addAll(_class(_pattern, environment));
        return res;
    }

    /**
	 * @inject name=explicit  params=1
	 * @throws ExplicitWeavepointException
	 */
    public abstract List _explicit(NamePattern _pattern, Environment environment) throws ExplicitWeavepointException;

    /**
	 * @inject name=package params=1
	 */
    public abstract List _package(NamePattern _pattern, Environment environment);

    /**
	 * @inject name=method params=1
	 */
    public abstract List _method(NamePattern _pattern, Environment environment);

    /**
	 * @inject name=attribute params=1
	 */
    public abstract List _attribute(NamePattern _pattern, Environment environment);

    /**
	 * @inject name=property params=1
	 */
    public abstract List _property(NamePattern _pattern, Environment environment);

    private static PrintStream consoleOutStream = System.out;

    /**
	 * @inject name=console params=1
	 */
    public void _console(String message, Environment environment) {
        consoleOutStream.println(message);
    }

    public static void setConsoleOutStream(PrintStream stream) {
        consoleOutStream = stream;
    }

    public static PrintStream getConsoleOutStream() {
        return consoleOutStream;
    }

    /**
	 * @inject name=ask params=3
	 */
    public Type _ask(String askString, int argTypeID, Type selectFrom[], Environment environment) {
        return null;
    }

    /**
	 * throws an ExitException with the given exitCode
	 * @inject name=exit params=1
	 */
    public void _exit(int exitCode, Environment environemnt) throws ExitException {
        throw new ExitException(exitCode);
    }

    /**
	 * @inject name=classes params=0
	 */
    public List _classes(Environment environment) {
        return environment.getModel().getRoot().getAllClasses();
    }

    /**
     * 
     * @param environment
     * @return
     * @inject name=namespace params=0
     */
    public List _namespace(Environment environment) {
        return environment.getModel().getRoot().getNamespace();
    }

    /**
     * @param environment
     * @return
     * @inject name=interfaces params=0
     */
    public List _interfaces(Environment environment) {
        return environment.getModel().getRoot().getAllInterfaces();
    }

    /**
	 * @inject name=packages params=0
	 */
    public List _packages(Environment environment) {
        return environment.getModel().getRoot().getPackages();
    }

    /**
	 * Controls whether the word assert is recognized as keyword or as identifier  
	 * @param v if true, assert is recognized as keyword, if false, assert is handled as identifier
	 * @inject name=assertIsKeyword params=1
	 */
    public abstract void _assertIsKeyword(boolean v);

    /**
	 * 
	 * @inject name=root params=0
	 */
    public Type _root(Environment env) {
        return env.getModel().getRoot().getPackage("");
    }

    /**
	 * 
	 * @param className
	 * @param environment
	 * @inject name=existsClass params=1
	 * @return
	 */
    public boolean _existsClass(String className, Environment environment) {
        return environment.getModel().getRoot().existsClass(className);
    }

    /**
	 * 
	 * @param interfaceName
	 * @param environment
	 * @return
	 * @inject name=existsInterface params=1
	 */
    public boolean _existsInterface(String interfaceName, Environment environment) {
        return environment.getModel().getRoot().existsInterface(interfaceName);
    }

    /**
	 * 
	 * @param packageName
	 * @param environment
	 * @return
	 * @inject name=existsPackage params=1
	 */
    public boolean _existsPackage(String packageName, Environment environment) {
        return environment.getModel().getRoot().existsPackage(packageName);
    }

    /**
	 * 
	 * @param typeName
	 * @return
	 * @inject name=getNonnamespaceType params=1
	 */
    public abstract TypeWeavepoint _getNonnamespaceType(String typeName, Environment environment);

    /**
	 * 
	 * @param statements
	 * @param environment
	 * @return
	 * @inject name=tx params=1
	 */
    public Transaction _tx(BlockType statements, Environment environment) {
        return environment.getModel().getTransactionManager().createScriptTransaction(statements);
    }

    /**
	 * 
	 * @param filename
	 * @param environment
	 * @return
	 * @inject name=openFileRead params=1
	 */
    public FileType _openFileRead(String filename, Environment environment) throws FileIOException {
        FileType result = new FileType(FileType.READ);
        result.open(filename);
        return result;
    }

    /**
	 * 
	 * @param filename
	 * @param environment
	 * @return
	 * @inject name=openFileWrite params=1
	 */
    public FileType _openFileWrite(String filename, Environment environment) throws FileIOException {
        FileType result = new FileType(FileType.WRITE);
        result.open(filename);
        return result;
    }
}
