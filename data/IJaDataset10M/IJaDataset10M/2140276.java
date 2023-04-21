package com.codemonster.surinam.controller;

import com.codemonster.surinam.core.framework.ServiceToFieldInjectionPair;
import com.codemonster.surinam.export.framework.ServiceBlock;
import com.codemonster.surinam.export.throwable.BindingDeclarationError;
import com.codemonster.surinam.export.throwable.DependencyInjectionException;
import com.codemonster.surinam.export.throwable.MissingAnnotationException;
import com.codemonster.surinam.export.throwable.MissingInterfaceException;
import com.codemonster.surinam.export.throwable.UnknownServiceException;
import java.net.MalformedURLException;
import java.util.Iterator;

/**
 * This is the common interface for all supported 'Actions.' Actions are object representations of
 * operations that make up an Action Document. These operations will fall into different categories as
 * subclasses, refer to them for more details.
 */
public interface Action {

    /**
     * We want to be able to discern which operation is being performed by subclasses of this object.
     *
     * @return Returns the name of the action being performed.
     */
    String getName();

    /**
     * Get a list of fully qualified Contracts that this action requires. Contract actions
     * will normally be empty but implementations could have a quite a few.
     *
     * @return Returns a list of services, upon which, this service depends.
     */
    Iterator<ServiceToFieldInjectionPair> getRequiredServices();

    /**
     * This is the primary execution point for all ServiceBlock actions. Not all operations will throw
     * all of these exceptions but we wanted a unified interface for these actions to allow the code
     * to handle a broad ranged of actions and still be able to respond in a fine-grained manner.<br>
     * <br>
     * Note that if you aren't interested in fine-grained exceptions, all Surinam exceptions extend
     * SurinamException, so you could just trap that along with the Java exceptions separately. Or
     * catching Exception should do the trick.
     *
     * @param serviceBlock The ServiceBlock that the actions will be given access to.
     * @throws MalformedURLException        Thrown if one of the classpath segments are malformed.
     * @throws ClassNotFoundException       Thrown if the FQ class cannot be found on the classpath.
     * @throws IllegalAccessException       If you do not have access or permission to execute this method.
     * @throws MissingAnnotationException   The class is missing a required annotation.
     * @throws UnknownServiceException      The Service Contract is unknown to the system.
     * @throws MissingInterfaceException    A required interface has not been extended.
     * @throws DependencyInjectionException Attempts to inject resources as requested has failed.
     * @throws InstantiationException       The Service Block is not able to instantiate an instance of this class.
     * @throws com.codemonster.surinam.export.throwable.BindingDeclarationError
     *                                      Thrown if there is a binding failure.
     */
    void apply(ServiceBlock serviceBlock) throws MalformedURLException, ClassNotFoundException, IllegalAccessException, MissingAnnotationException, UnknownServiceException, MissingInterfaceException, DependencyInjectionException, InstantiationException, BindingDeclarationError;

    /**
     * This callback method is used to inject the ServiceBlock that the action will have access to in order
     * to perform its work. Needless to say, Actions can be supremely powerful and therefore need to be trusted
     * entities. This callback will probably be a NOP for all actions except implementations.
     *
     * @param serviceBlock The ServiceBlock that the actions will be given access to.
     * @throws com.codemonster.surinam.export.throwable.DependencyInjectionException
     *                                Thrown if injection fails.
     * @throws ClassNotFoundException Thrown if the class cannot be found; usually a classpath or packaging issue.
     * @throws IllegalAccessException Thrown if there is a loader or hierarchy failure.
     */
    void injectServices(ServiceBlock serviceBlock) throws ClassNotFoundException, DependencyInjectionException, IllegalAccessException;
}
