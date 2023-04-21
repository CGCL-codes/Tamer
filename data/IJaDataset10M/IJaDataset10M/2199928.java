package org.sourceviewer.ctags.tags;

import java.util.List;

public class SimpleTag {

    public SimpleTag(String name, List<KindOption> makeOption, int kind) {
        System.out.println(name);
    }

    public boolean lineNumberEntry;

    public long lineNumber;

    private long filePosition;

    public String language;

    public boolean isFileScope;

    public boolean isFileEntry;

    public boolean truncateLine;

    public String sourceFileName;

    public String name;

    public String kindName;

    public char kind;

    public ExtensionFields extennsionFields;

    public class ExtensionFields {

        public String access;

        public String fileScope;

        public String implementation;

        public String inheritance;

        public String scope;

        public String signature;

        public String typeRef;
    }
}
