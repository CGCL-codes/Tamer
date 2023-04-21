package org.spbu.pldoctoolkit.registry;

import org.eclipse.core.resources.IFile;

public class RegisteredLocation {

    public static final String CORE_CONTEXT = "Core";

    public static final String PRODUCT_CONTEXT = "Product";

    public static final String INF_ELEMENT = "InfElement";

    public static final String INF_PRODUCT = "InfProduct";

    public static final String DICTIONARY = "Dictionary";

    public static final String PRODUCT = "Product";

    public static final String FINAL_INF_PRODUCT = "FinalInfProduct";

    public static final String DIRECTORY = "Directory";

    public static final String DIRTEPLATE = "DirTemplate";

    public static final String INF_ELEM_REF = "InfElemRef";

    private final String context;

    private final String type;

    private final String id;

    private final String name;

    private final IFile file;

    private final int lineNumber;

    public RegisteredLocation(String context, String type, String id, String name, IFile file, int lineNumber) {
        this.context = context;
        this.type = type;
        this.id = id;
        this.name = name;
        this.file = file;
        this.lineNumber = lineNumber;
    }

    public String getContext() {
        return context;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public IFile getFile() {
        return file;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String toString() {
        return context + "/" + type + "/" + id + " @ " + file + ":" + lineNumber;
    }
}
