package emil.poker.internetPlay;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import org.jruby.IRuby;
import org.jruby.Ruby;
import org.jruby.RubyArray;
import org.jruby.RubyBinding;
import org.jruby.RubyBoolean;
import org.jruby.RubyClass;
import org.jruby.RubyException;
import org.jruby.RubyFileStat;
import org.jruby.RubyFixnum;
import org.jruby.RubyFloat;
import org.jruby.RubyModule;
import org.jruby.RubyNumeric;
import org.jruby.RubyProc;
import org.jruby.RubyString;
import org.jruby.RubySymbol;
import org.jruby.RubyTime;
import org.jruby.RubySymbol.SymbolTable;
import org.jruby.ast.Node;
import org.jruby.common.RubyWarnings;
import org.jruby.exceptions.RaiseException;
import org.jruby.internal.runtime.GlobalVariables;
import org.jruby.internal.runtime.ThreadService;
import org.jruby.javasupport.JavaSupport;
import org.jruby.lexer.yacc.ISourcePosition;
import org.jruby.runtime.Block;
import org.jruby.runtime.CacheMap;
import org.jruby.runtime.CallbackFactory;
import org.jruby.runtime.DynamicScope;
import org.jruby.runtime.GlobalVariable;
import org.jruby.runtime.ObjectSpace;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.runtime.load.LoadService;
import org.jruby.util.collections.SinglyLinkedList;

public class RubyEngine implements IRuby {

    public static synchronized IRuby getInstance() {
        if (_instance == null) _instance = Ruby.getDefaultInstance();
        return _instance;
    }

    private static IRuby _instance;

    private RubyEngine() {
        IRuby defaultInstance = Ruby.getDefaultInstance();
    }

    public void callTraceFunction(ThreadContext arg0, String arg1, ISourcePosition arg2, IRubyObject arg3, String arg4, IRubyObject arg5) {
    }

    public CallbackFactory callbackFactory(Class arg0) {
        return null;
    }

    public IRubyObject compileAndRun(Node arg0) {
        return null;
    }

    public RubyClass defineClass(String arg0, RubyClass arg1) {
        return null;
    }

    public RubyClass defineClassUnder(String arg0, RubyClass arg1, SinglyLinkedList arg2) {
        return null;
    }

    public void defineGlobalConstant(String arg0, IRubyObject arg1) {
    }

    public RubyModule defineModule(String arg0) {
        return null;
    }

    public RubyModule defineModuleUnder(String arg0, SinglyLinkedList arg1) {
        return null;
    }

    public void defineReadonlyVariable(String arg0, IRubyObject arg1) {
    }

    public void defineVariable(GlobalVariable arg0) {
    }

    public IRubyObject eval(Node arg0) {
        return null;
    }

    public IRubyObject evalScript(String arg0) {
        return null;
    }

    public CacheMap getCacheMap() {
        return null;
    }

    public RubyClass getClass(String arg0) {
        return null;
    }

    public RubyModule getClassFromPath(String arg0) {
        return null;
    }

    public ThreadContext getCurrentContext() {
        return null;
    }

    public String getCurrentDirectory() {
        return null;
    }

    public IRubyObject getDebug() {
        return null;
    }

    public String getEncoding() {
        return null;
    }

    public PrintStream getErr() {
        return null;
    }

    public PrintStream getErrorStream() {
        return null;
    }

    public RubyBoolean getFalse() {
        return null;
    }

    public RubyClass getFixnum() {
        return null;
    }

    public RubyFixnum[] getFixnumCache() {
        return null;
    }

    public GlobalVariables getGlobalVariables() {
        return null;
    }

    public InputStream getIn() {
        return null;
    }

    public InputStream getInputStream() {
        return null;
    }

    public Hashtable getIoHandlers() {
        return null;
    }

    public JavaSupport getJavaSupport() {
        return null;
    }

    public RubyModule getKernel() {
        return null;
    }

    public LoadService getLoadService() {
        return null;
    }

    public RubyModule getModule(String arg0) {
        return null;
    }

    public IRubyObject getNil() {
        return null;
    }

    public RubyClass getNilClass() {
        return null;
    }

    public RubyClass getObject() {
        return null;
    }

    public ObjectSpace getObjectSpace() {
        return null;
    }

    public RubyModule getOrCreateModule(String arg0) {
        return null;
    }

    public PrintStream getOut() {
        return null;
    }

    public PrintStream getOutputStream() {
        return null;
    }

    public Random getRandom() {
        return null;
    }

    public long getRandomSeed() {
        return 0;
    }

    public int getSafeLevel() {
        return 0;
    }

    public int getStackTraces() {
        return 0;
    }

    public long getStartTime() {
        return 0;
    }

    public RubyClass getString() {
        return null;
    }

    public SymbolTable getSymbolTable() {
        return null;
    }

    public ThreadService getThreadService() {
        return null;
    }

    public IRubyObject getTmsStruct() {
        return null;
    }

    public IRubyObject getTopConstant(String arg0) {
        return null;
    }

    public IRubyObject getTopSelf() {
        return null;
    }

    public RubyProc getTraceFunction() {
        return null;
    }

    public RubyBoolean getTrue() {
        return null;
    }

    public IRubyObject getVerbose() {
        return null;
    }

    public RubyWarnings getWarnings() {
        return null;
    }

    public long incrementRandomSeedSequence() {
        return 0;
    }

    public boolean isClassDefined(String arg0) {
        return false;
    }

    public boolean isDoNotReverseLookupEnabled() {
        return false;
    }

    public boolean isGlobalAbortOnExceptionEnabled() {
        return false;
    }

    public boolean isObjectSpaceEnabled() {
        return false;
    }

    public void loadFile(File arg0, boolean arg1) {
    }

    public void loadNode(String arg0, Node arg1, boolean arg2) {
    }

    public void loadScript(RubyString arg0, RubyString arg1, boolean arg2) {
    }

    public void loadScript(String arg0, Reader arg1, boolean arg2) {
    }

    public RaiseException newArgumentError(String arg0) {
        return null;
    }

    public RaiseException newArgumentError(int arg0, int arg1) {
        return null;
    }

    public RubyArray newArray() {
        return null;
    }

    public RubyArray newArray(IRubyObject arg0) {
        return null;
    }

    public RubyArray newArray(IRubyObject[] arg0) {
        return null;
    }

    public RubyArray newArray(List arg0) {
        return null;
    }

    public RubyArray newArray(int arg0) {
        return null;
    }

    public RubyArray newArray(IRubyObject arg0, IRubyObject arg1) {
        return null;
    }

    public RubyBinding newBinding() {
        return null;
    }

    public RubyBinding newBinding(Block arg0) {
        return null;
    }

    public RubyBoolean newBoolean(boolean arg0) {
        return null;
    }

    public RaiseException newEOFError() {
        return null;
    }

    public RaiseException newErrnoEBADFError() {
        return null;
    }

    public RaiseException newErrnoEBADFError(String arg0) {
        return null;
    }

    public RaiseException newErrnoEEXISTError(String arg0) {
        return null;
    }

    public RaiseException newErrnoEINVALError() {
        return null;
    }

    public RaiseException newErrnoEINVALError(String arg0) {
        return null;
    }

    public RaiseException newErrnoENOENTError() {
        return null;
    }

    public RaiseException newErrnoENOENTError(String arg0) {
        return null;
    }

    public RaiseException newErrnoESPIPEError() {
        return null;
    }

    public RaiseException newErrnoESPIPEError(String arg0) {
        return null;
    }

    public RubyFixnum newFixnum(long arg0) {
        return null;
    }

    public RubyFloat newFloat(double arg0) {
        return null;
    }

    public RaiseException newFrozenError(String arg0) {
        return null;
    }

    public RaiseException newIOError(String arg0) {
        return null;
    }

    public RaiseException newIOErrorFromException(IOException arg0) {
        return null;
    }

    public RaiseException newIndexError(String arg0) {
        return null;
    }

    public RaiseException newLoadError(String arg0) {
        return null;
    }

    public RaiseException newLocalJumpError(String arg0) {
        return null;
    }

    public RaiseException newNameError(String arg0, String arg1) {
        return null;
    }

    public RaiseException newNoMethodError(String arg0, String arg1) {
        return null;
    }

    public RaiseException newNotImplementedError(String arg0) {
        return null;
    }

    public RubyNumeric newNumeric() {
        return null;
    }

    public RubyProc newProc() {
        return null;
    }

    public RaiseException newRangeError(String arg0) {
        return null;
    }

    public RubyFileStat newRubyFileStat(File arg0) {
        return null;
    }

    public RaiseException newSecurityError(String arg0) {
        return null;
    }

    public RubyString newString(String arg0) {
        return null;
    }

    public RubySymbol newSymbol(String arg0) {
        return null;
    }

    public RaiseException newSyntaxError(String arg0) {
        return null;
    }

    public RaiseException newSystemCallError(String arg0) {
        return null;
    }

    public RaiseException newSystemExit(int arg0) {
        return null;
    }

    public RaiseException newSystemStackError(String arg0) {
        return null;
    }

    public RaiseException newThreadError(String arg0) {
        return null;
    }

    public RubyTime newTime(long arg0) {
        return null;
    }

    public RaiseException newTypeError(String arg0) {
        return null;
    }

    public RaiseException newTypeError(IRubyObject arg0, RubyClass arg1) {
        return null;
    }

    public RaiseException newZeroDivisionError() {
        return null;
    }

    public Node parse(Reader arg0, String arg1, DynamicScope arg2) {
        return null;
    }

    public Node parse(String arg0, String arg1, DynamicScope arg2) {
        return null;
    }

    public void printError(RubyException arg0) {
    }

    public IRubyObject pushExitBlock(RubyProc arg0) {
        return null;
    }

    public boolean registerInspecting(Object arg0) {
        return false;
    }

    public void secure(int arg0) {
    }

    public void setCurrentDirectory(String arg0) {
    }

    public void setDebug(IRubyObject arg0) {
    }

    public void setDoNotReverseLookupEnabled(boolean arg0) {
    }

    public void setEncoding(String arg0) {
    }

    public void setGlobalAbortOnExceptionEnabled(boolean arg0) {
    }

    public void setGlobalVariables(GlobalVariables arg0) {
    }

    public void setRandomSeed(long arg0) {
    }

    public void setSafeLevel(int arg0) {
    }

    public void setStackTraces(int arg0) {
    }

    public void setTraceFunction(RubyProc arg0) {
    }

    public void setVerbose(IRubyObject arg0) {
    }

    public void tearDown() {
    }

    public void unregisterInspecting(Object arg0) {
    }
}
