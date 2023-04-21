package it.battlehorse.ioc;

/**
 * Constants used in the inversion-of-control java bytecode transformer.
 * 
 * @author battlehorse
 * @since May 9, 2006
 */
public interface IOCConstants {

    /**
	 * The fully qualified internal class name for the annotation which identifies serviceable objects.
	 * Its value is {@value }.
	 */
    public static final String SERVICEABLE_ANNOTATION = "Lit/battlehorse/rcp/sl/annot/Serviceable;";

    /**
	 * The fully qualified class name for the service locator class, whose value is {@value }.
	 */
    public static final String SERVICE_LOCATOR_CLASS = "it/battlehorse/rcp/sl/ServiceLocator";

    /**
	 * The name of the service locator method which configures objects. Its value is {@value }.
	 */
    public static final String SERVICE_LOCATOR_METHOD = "service";

    /**
	 * The parameters for the service locator  method which configures objects. Its value is {@value }.
	 */
    public static final String SERVICE_LOCATOR_PARAMETERS = "(Ljava/lang/Object;)V";
}
