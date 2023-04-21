package de.tu.depth.fragments;

import java.util.UUID;

public class Category extends HierarchyFragment {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3751618735846176568L;

    /**
     * Necessary when loading from XML 
     * @param subTypeString
     * @return
	 */
    public static SubType getSubType(String subTypeString) {
        if (subTypeString.equals("images")) {
            return SubType.IMAGES;
        } else if (subTypeString.equals("tutorials")) {
            return SubType.TUTORIALS;
        } else if (subTypeString.equals("chapters")) {
            return SubType.CHAPTERS;
        } else if (subTypeString.equals("codefiles")) {
            return SubType.CODE_FILES;
        } else if (subTypeString.equals("codesnippets")) {
            return SubType.CODE_SNIPPETS;
        }
        return null;
    }

    public enum SubType {

        IMAGES("Images", "images"), TUTORIALS("Tutorials", "tutorials"), CHAPTERS("Chapters", "chapters"), CODE_FILES("Code files", "codefiles"), CODE_SNIPPETS("Code snippets", "codesnippets");

        private String name, progname;

        private SubType(String name, String progname) {
            this.name = name;
            this.progname = progname;
        }

        public String getName() {
            return this.name;
        }

        public String getProgName() {
            return progname;
        }
    }

    public Category(Project parent, SubType subType) {
        super(parent, subType.getName());
        setSubType(subType);
    }

    public Category(Project parent, SubType subType, UUID uuid) {
        super(parent, subType.getName(), uuid);
        setSubType(subType);
    }

    public String getTypeString() {
        return "category";
    }
}
