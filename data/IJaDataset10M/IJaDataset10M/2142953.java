package com.evaserver.rof.xml.parser;

/**
 *
 *
 * @author Max Antoni
 * @version $Revision: 2 $
 */
public class Fragment extends ParentTag {

    private String name;

    public void accept(Visitor inVisitor) {
        inVisitor.visitFragment(this);
    }

    /**
	 * @return the name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param inName the name.
	 */
    public void setName(String inName) {
        name = inName;
    }
}
