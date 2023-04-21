package org.apache.tapestry5.ioc;

/**
 * The source for the module instance needed by any service builders, service contributors and service decorators that
 * are mapped to instance methods.
 * <p/>
 * Allows the creation of the module instance to be deferred until actually needed; in practical terms, when the
 * builder/decorator/contributor is a <em>static</em> method on the module builder class, then a module instance is not
 * needed. This allows Tapestry IOC to work around a tricky chicken-and-the-egg problem, whereby the constructor of a
 * module instance requires contributions that originate in the same module.
 * <p/>
 * The term "module builder" has been deprecated; the current term is "module class", but this interface is left as-is
 * for backwards compatibility.
 */
public interface ModuleBuilderSource {

    /**
     * Returns the instantiated version of the Tapestry IoC module class.
     */
    Object getModuleBuilder();
}
