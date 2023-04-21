package hub.sam.lang.robot.provider;

import hub.metrik.lang.eprovide.debuggingstate.provider.DebuggingstateEditPlugin;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.ResourceLocator;

/**
 * This is the central singleton for the Robot edit plugin.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public final class RobotEditPlugin extends EMFPlugin {

    /**
     * Keep track of the singleton.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
    public static final RobotEditPlugin INSTANCE = new RobotEditPlugin();

    /**
     * Keep track of the singleton.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
    private static Implementation plugin;

    /**
     * Create the instance.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
    public RobotEditPlugin() {
        super(new ResourceLocator[] { DebuggingstateEditPlugin.INSTANCE });
    }

    /**
     * Returns the singleton instance of the Eclipse plugin.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the singleton instance.
     * @generated
     */
    @Override
    public ResourceLocator getPluginResourceLocator() {
        return plugin;
    }

    /**
     * Returns the singleton instance of the Eclipse plugin.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the singleton instance.
     * @generated
     */
    public static Implementation getPlugin() {
        return plugin;
    }

    /**
     * The actual implementation of the Eclipse <b>Plugin</b>.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
    public static class Implementation extends EclipsePlugin {

        /**
         * Creates an instance.
         * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
         * @generated
         */
        public Implementation() {
            super();
            plugin = this;
        }
    }
}
