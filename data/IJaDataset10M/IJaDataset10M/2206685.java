package org.python.newcompiler;

import java.util.Set;
import org.python.bytecode.Label;

public interface Environment {

    void markHasExec();

    void addEntryPoint(Label entry);

    void markAsGlobal(String name);

    void markAsNonlocal(String name);

    void addAssignment(String name);

    void addReference(String name);

    void addParameter(String name);

    void addFuture(String feature) throws Exception;

    GlobalEnvironment getGlobalEnvironment();

    Set<CompilerFlag> getCompilerFlags();
}
