package net.sf.jajag.test;

import net.sf.jajag.ejb3.Interface;
import net.sf.jajag.ejb3.InterfaceType;

/**
 * This is a class-level comment that has annotations.
 * 
 * @author Niclas Olofsson
 */
@Interface(type = InterfaceType.Both)
public class TestJavaEjb implements TestJava, TestJavaRemote {

    /**
	 * Interface should be generated for BOTH
	 */
    @Interface(type = InterfaceType.Both)
    public void generateOnBoth() {
    }

    /**
	 * Interface should be generated for BOTH
	 * 
	 * @return a string
	 */
    @Interface(type = InterfaceType.Both)
    public String generateOnBothWithReturnValue() {
        return null;
    }

    /**
	 * Interface should be generated for BOTH
	 */
    @Interface(type = InterfaceType.Both)
    public void generateOnBothWithParams(String field) {
    }

    /**
	 * Interface should be generated for LOCAL
	 */
    @Interface(type = InterfaceType.Local)
    public void generateOnLocal(String field) {
    }

    /**
	 * Interface should be generated for REMOTE
	 */
    @Interface(type = InterfaceType.Remote)
    public void generateOnRemote(String field) {
    }

    /**
	 * Should not be generated
	 */
    public int doNotGenerate() {
        return 0;
    }
}
