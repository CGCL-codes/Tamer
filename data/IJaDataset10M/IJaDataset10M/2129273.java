package de.tu.depth.fragments;

import java.util.UUID;

public class CodeFile extends Fragment {

    public static final String CODEFILE_TYPE_STRING = "codefile";

    private static final long serialVersionUID = 1L;

    private String parserID;

    private String editorID;

    public CodeFile(HierarchyFragment parent, String name, String parserID) {
        super(parent, name);
        this.parserID = parserID;
    }

    public CodeFile(HierarchyFragment parent, String name, String parserID, UUID uuid) {
        super(parent, name, uuid);
        this.parserID = parserID;
    }

    public String getParserID() {
        return parserID;
    }

    public void setEditorID(String editorID) {
        this.editorID = editorID;
    }

    public String getEditorID() {
        return this.editorID;
    }

    public String getTypeString() {
        return CODEFILE_TYPE_STRING;
    }
}
