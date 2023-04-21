package com.c4j.workspace;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;
import com.c4j.linker.IResolver;

public interface IWorkspace extends IContainer {

    String FOLDER_NAME_EXPR = "[a-zA-Z0-9_ ]+";

    String JAR_FILE_NAME_EXPR = "[a-zA-Z0-9_]+";

    String VARIABLE_NAME_SUFFIX_EXPR = "[a-zA-Z0-9_]+";

    String TYPE_NAME_EXPR = "[a-zA-Z_][a-zA-Z0-9_]*";

    Pattern FOLDER_NAME_PATTERN = Pattern.compile(FOLDER_NAME_EXPR);

    Pattern JAR_FILE_NAME_PATTERN = Pattern.compile(JAR_FILE_NAME_EXPR);

    Pattern VARIABLE_NAME_SUFFIX_PATTERN = Pattern.compile(VARIABLE_NAME_SUFFIX_EXPR);

    Pattern TYPE_NAME_PATTERN = Pattern.compile(TYPE_NAME_EXPR);

    IResolver<IFragment> getFragmentResovler();

    List<? extends IFragment> getTopologicalFragmentOrder();

    void addListener(final IWorkspaceListener listener);

    void removeListener(final IWorkspaceListener listener);

    void fireChangedContainerElement(final IContainerElement element);

    void fireAddedContainerElement(final IContainerElement element);

    void fireRemovedContainerElement(final IContainerElement element);

    void fireRenamedContainerElement(final IContainerElement element, final String oldName);

    void fireStartGroup();

    void fireEndGroup();

    boolean hasOwnRuntime();

    File getFolder();
}
