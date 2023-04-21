package de.jassda.jabyba.backend.bytecode;

import org.objectweb.asm.ClassVisitor;

/**
 * A interface which has to be implemented by all patchers. * 
 * @author <a href="mailto:johannes.rieken@informatik.uni-oldenburg.de">riejo</a> */
public interface JaBybaPatcher extends ClassVisitor {

    /**
	 * Prior to the code manipulation this method has to be called.
	 * It sets a class visitor to which call method calls are delegated.
	 * @param cv The class visitor which is called by a JaBybaPatcher. 
	 *		Normally this is ought to be a ClassWriter.	 */
    public void setClassVisitor(ClassVisitor cv);
}
