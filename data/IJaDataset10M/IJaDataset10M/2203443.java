package org.jplate.tmplate.directive.named;

import org.jplate.JPlateException;
import org.jplate.tmplate.directive.DirectiveContainerIfc;
import org.jplate.tmplate.directive.NamedDirectiveIfc;
import org.jplate.tmplate.directive.util.Translator;
import org.jplate.util.environment.EnvironmentIfc;

/**
 *
 * This directive will compute the value of a name using child directives.
 * Specifically it will request all child directives to translate and take
 * the concatenation of the translations to set the value of name.
 *
 * <p>
 * Assume self is named <code>ComputeValue</code>:
 *
 * <pre>
 * ${ComputeValue name="Foo"}
 *    This is cool
 * ${/ComputeValue}
 * </pre>
 *
 * Any reference to <code>Foo</code> will now be:
 *
 * <pre>
 *     This is cool
 * </pre>
 *
 * @see #NAME_PARAM
 * @see org.jplate.tmplate.directive.DirectiveContainerIfc
 *
 */
public final class ComputeValueDirective implements NamedDirectiveIfc {

    /**
     *
     * This is the name parameter of the value we will be setting.  This is a
     * required parameter.
     *
     */
    public static String NAME_PARAM = "name";

    /**
     *
     * This is the default constructor.
     *
     */
    public ComputeValueDirective() {
    }

    /**
     *
     * This method requests that the data container in container be translated.
     *
     * @param container represents the directive container where translation
     *        will be done.
     *
     * @return the contents of container translated.
     *
     * @throws JPlateException if any problems arise performing translation for
     *         container.
     *
     */
    public String translate(final DirectiveContainerIfc container) throws JPlateException {
        final String name = container.getEnvironment().getRequiredValue(NAME_PARAM);
        container.getParent().getEnvironment().setValue(name, Translator.getSingleton().translateChildren(container));
        return "";
    }

    /**
     *
     * This method will create a copy of self.
     *
     * @return a copy of self.
     *
     */
    public NamedDirectiveIfc createCopy() {
        return new ComputeValueDirective();
    }
}
