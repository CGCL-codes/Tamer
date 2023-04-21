package ch.sahits.codegen.internal.wizards;

/**
 * Interface representing a basic code generation wizard.
 * This interface is more a marker interface.
 * @author Andi Hotz, Sahits GmbH
 * @since 2.1.0
 */
public interface IBasicCodegenWizard {

    /**
	 * Unserialize the <code>fileName</code> and initialize the
	 * wizard with the data.
	 * @param fileName name of the XML file
	 * @since 0.9.4
	 */
    public void unserialize(String fileName);
}
