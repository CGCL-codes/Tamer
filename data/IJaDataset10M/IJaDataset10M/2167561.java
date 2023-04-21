package org.apache.shindig.gadgets.variables;

import org.apache.shindig.gadgets.GadgetContext;
import org.apache.shindig.gadgets.GadgetException;
import org.apache.shindig.gadgets.MessageBundleFactory;
import org.apache.shindig.gadgets.spec.GadgetSpec;
import org.apache.shindig.gadgets.spec.MessageBundle;
import com.google.inject.Inject;

/**
 * Performs variable substitution on a gadget spec.
 */
public class VariableSubstituter {

    private final MessageBundleFactory messageBundleFactory;

    @Inject
    public VariableSubstituter(MessageBundleFactory messageBundleFactory) {
        this.messageBundleFactory = messageBundleFactory;
    }

    /**
   * Substitutes all hangman variables into the gadget spec.
   *
   * @return A new GadgetSpec, with all fields substituted as needed.
   */
    public GadgetSpec substitute(GadgetContext context, GadgetSpec spec) throws GadgetException {
        MessageBundle bundle = messageBundleFactory.getBundle(spec, context.getLocale(), context.getIgnoreCache());
        String dir = bundle.getLanguageDirection();
        Substitutions substituter = new Substitutions();
        substituter.addSubstitutions(Substitutions.Type.MESSAGE, bundle.getMessages());
        BidiSubstituter.addSubstitutions(substituter, dir);
        substituter.addSubstitution(Substitutions.Type.MODULE, "ID", Integer.toString(context.getModuleId()));
        UserPrefSubstituter.addSubstitutions(substituter, spec, context.getUserPrefs());
        return spec.substitute(substituter);
    }
}
