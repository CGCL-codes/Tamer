package net.sf.linns.model;

public interface IActivationFunction {

    double getA(double net);

    /**
	 * Returns a java command that returns the result f(x). x can be used as
	 * variable. This is used e.g. for Java class file generation.
	 * 
	 * @return
	 */
    String getJavaString();
}
