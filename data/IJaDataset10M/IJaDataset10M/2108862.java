package org.argouml.application.api;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import javax.swing.Icon;
import org.apache.log4j.Category;
import org.argouml.application.events.ArgoEvent;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoNotationEvent;
import org.argouml.application.notation.NotationNameImpl;
import org.argouml.application.notation.NotationProviderFactory;
import ru.novosoft.uml.behavior.collaborations.MAssociationRole;
import ru.novosoft.uml.behavior.collaborations.MMessage;
import ru.novosoft.uml.behavior.common_behavior.MAction;
import ru.novosoft.uml.behavior.common_behavior.MCallAction;
import ru.novosoft.uml.behavior.state_machines.MGuard;
import ru.novosoft.uml.behavior.state_machines.MState;
import ru.novosoft.uml.behavior.state_machines.MTransition;
import ru.novosoft.uml.behavior.use_cases.MExtensionPoint;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationEnd;
import ru.novosoft.uml.foundation.core.MAttribute;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MOperation;
import ru.novosoft.uml.foundation.core.MParameter;
import ru.novosoft.uml.foundation.data_types.MExpression;
import ru.novosoft.uml.foundation.data_types.MMultiplicity;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;
import ru.novosoft.uml.foundation.extension_mechanisms.MTaggedValue;
import ru.novosoft.uml.model_management.MPackage;

/** Provides centralized methods dealing with notation.
 *
 *  @stereotype singleton
 *  @author Thierry Lach
 *  @since 0.9.4
 */
public final class Notation implements PropertyChangeListener {

    /** Define a static log4j category variable for ArgoUML notation.
   */
    public static final Category cat = Category.getInstance("org.argouml.application.notation");

    /** The name of the default Argo notation.  This notation is
   *  part of Argo core distribution.
   */
    private static NotationName NOTATION_ARGO = null;

    /** The configuration key for the preferred notation
   */
    public static final ConfigurationKey KEY_DEFAULT_NOTATION = Configuration.makeKey("notation", "default");

    /** The configuration key that indicates whether to show stereotypes
   *  in the navigation panel
   */
    public static final ConfigurationKey KEY_SHOW_STEREOTYPES = Configuration.makeKey("notation", "navigation", "show", "stereotypes");

    /** The configuration key that indicates whether to use guillemots
   *  or greater/lessthan characters in stereotypes.
   */
    public static final ConfigurationKey KEY_USE_GUILLEMOTS = Configuration.makeKey("notation", "guillemots");

    /** 
   * Indicates if the user only wants to see UML notation.
   */
    public static final ConfigurationKey KEY_UML_NOTATION_ONLY = Configuration.makeKey("notation", "only", "uml");

    /** 
   * Indicates if the user wants to see visibility signs (public, private, protected or # + -)
   */
    public static final ConfigurationKey KEY_SHOW_VISIBILITY = Configuration.makeKey("notation", "show", "visibility");

    /** 
   * Indicates if the user wants to see multiplicity in attributes and classes
   */
    public static final ConfigurationKey KEY_SHOW_MULTIPLICITY = Configuration.makeKey("notation", "show", "multiplicity");

    /** 
   * Indicates if the user wants to see the initial value
   */
    public static final ConfigurationKey KEY_SHOW_INITIAL_VALUE = Configuration.makeKey("notation", "show", "initialvalue");

    /** 
   * Indicates if the user wants to see the properties (everything between braces), that is for example the concurrency
   */
    public static final ConfigurationKey KEY_SHOW_PROPERTIES = Configuration.makeKey("notation", "show", "properties");

    private static Notation SINGLETON = new Notation();

    private Notation() {
        Configuration.addListener(KEY_USE_GUILLEMOTS, this);
        Configuration.addListener(KEY_DEFAULT_NOTATION, this);
        Configuration.addListener(KEY_UML_NOTATION_ONLY, this);
    }

    /** Remove the notation change listener.
   *  <code>finalize</code> should never happen, but play it safe.
   */
    public void finalize() {
        Configuration.removeListener(KEY_DEFAULT_NOTATION, this);
        Configuration.removeListener(KEY_USE_GUILLEMOTS, this);
        Configuration.removeListener(KEY_UML_NOTATION_ONLY, this);
    }

    private NotationProvider getProvider(NotationName notation) {
        NotationProvider np = null;
        np = NotationProviderFactory.getInstance().getProvider(notation);
        cat.debug("getProvider(" + notation + ") returns " + np);
        return np;
    }

    public static void setDefaultNotation(NotationName n) {
        cat.info("default notation set to " + n.getConfigurationValue());
        Configuration.setString(KEY_DEFAULT_NOTATION, n.getConfigurationValue());
    }

    public static NotationName findNotation(String s) {
        return NotationNameImpl.findNotation(s);
    }

    private static boolean reportedNotationProblem = false;

    public static NotationName getDefaultNotation() {
        NotationName n = NotationNameImpl.findNotation(Configuration.getString(KEY_DEFAULT_NOTATION, NOTATION_ARGO.getConfigurationValue()));
        if (n == null) n = NotationNameImpl.findNotation("Uml.1.3");
        cat.debug("default notation is " + n.getConfigurationValue());
        return n;
    }

    /**
     * <p>General accessor for an extension point.</p>
     *
     * @param notation    Name of the notation to be used.
     *
     * @param ep          The extension point to generate for.
     *
     * @return            The generated text.
     */
    protected String generateExtensionPoint(NotationName notation, MExtensionPoint ep) {
        return getProvider(notation).generateExtensionPoint(ep);
    }

    protected String generateOperation(NotationName notation, MOperation op, boolean documented) {
        return getProvider(notation).generateOperation(op, documented);
    }

    protected String generateAttribute(NotationName notation, MAttribute attr, boolean documented) {
        return getProvider(notation).generateAttribute(attr, documented);
    }

    protected String generateParameter(NotationName notation, MParameter param) {
        return getProvider(notation).generateParameter(param);
    }

    protected String generateName(NotationName notation, String name) {
        return getProvider(notation).generateName(name);
    }

    protected String generatePackage(NotationName notation, MPackage pkg) {
        return getProvider(notation).generatePackage(pkg);
    }

    protected String generateExpression(NotationName notation, MExpression expr) {
        return getProvider(notation).generateExpression(expr);
    }

    protected String generateClassifier(NotationName notation, MClassifier cls) {
        return getProvider(notation).generateClassifier(cls);
    }

    protected String generateStereotype(NotationName notation, MStereotype s) {
        return getProvider(notation).generateStereotype(s);
    }

    protected String generateTaggedValue(NotationName notation, MTaggedValue s) {
        return getProvider(notation).generateTaggedValue(s);
    }

    protected String generateAssociation(NotationName notation, MAssociation a) {
        return getProvider(notation).generateAssociation(a);
    }

    protected String generateAssociationEnd(NotationName notation, MAssociationEnd ae) {
        return getProvider(notation).generateAssociationEnd(ae);
    }

    protected String generateMultiplicity(NotationName notation, MMultiplicity m) {
        return getProvider(notation).generateMultiplicity(m);
    }

    protected String generateState(NotationName notation, MState m) {
        return getProvider(notation).generateState(m);
    }

    protected String generateStateBody(NotationName notation, MState stt) {
        return getProvider(notation).generateStateBody(stt);
    }

    protected String generateTransition(NotationName notation, MTransition m) {
        return getProvider(notation).generateTransition(m);
    }

    protected String generateAction(NotationName notation, MAction m) {
        return getProvider(notation).generateAction(m);
    }

    protected String generateGuard(NotationName notation, MGuard m) {
        return getProvider(notation).generateGuard(m);
    }

    protected String generateMessage(NotationName notation, MMessage m) {
        return getProvider(notation).generateMessage(m);
    }

    protected String generateClassifierRef(NotationName notation, MClassifier m) {
        return getProvider(notation).generateClassifierRef(m);
    }

    protected String generateAssociationRole(NotationName notation, MAssociationRole m) {
        return getProvider(notation).generateAssociationRole(m);
    }

    public static Notation getInstance() {
        return SINGLETON;
    }

    /**
     * <p>Static accessor for extension point generation. Invokes our protected
     *   accessor from the singleton instance with the "documented" flag set
     *   false.</p>
     *
     * @param ctx  Context used to identify the notation
     *
     * @param ep   The extension point to generate for.
     *
     * @return     The generated text.
     */
    public static String generateExtensionPoint(NotationContext ctx, MExtensionPoint ep) {
        return SINGLETON.generateExtensionPoint(Notation.getNotation(ctx), ep);
    }

    public static String generateOperation(NotationContext ctx, MOperation op) {
        return SINGLETON.generateOperation(Notation.getNotation(ctx), op, false);
    }

    public static String generateOperation(NotationContext ctx, MOperation op, boolean documented) {
        return SINGLETON.generateOperation(Notation.getNotation(ctx), op, documented);
    }

    public static String generateAttribute(NotationContext ctx, MAttribute attr) {
        return SINGLETON.generateAttribute(Notation.getNotation(ctx), attr, false);
    }

    public static String generateAttribute(NotationContext ctx, MAttribute attr, boolean documented) {
        return SINGLETON.generateAttribute(Notation.getNotation(ctx), attr, documented);
    }

    public static String generateParameter(NotationContext ctx, MParameter param) {
        return SINGLETON.generateParameter(Notation.getNotation(ctx), param);
    }

    public static String generatePackage(NotationContext ctx, MPackage p) {
        return SINGLETON.generatePackage(Notation.getNotation(ctx), p);
    }

    public static String generateClassifier(NotationContext ctx, MClassifier cls) {
        return SINGLETON.generateClassifier(Notation.getNotation(ctx), cls);
    }

    public static String generateStereotype(NotationContext ctx, MStereotype s) {
        return SINGLETON.generateStereotype(Notation.getNotation(ctx), s);
    }

    public static String generateTaggedValue(NotationContext ctx, MTaggedValue s) {
        return SINGLETON.generateTaggedValue(Notation.getNotation(ctx), s);
    }

    public static String generateAssociation(NotationContext ctx, MAssociation a) {
        return SINGLETON.generateAssociation(Notation.getNotation(ctx), a);
    }

    public static String generateAssociationEnd(NotationContext ctx, MAssociationEnd ae) {
        return SINGLETON.generateAssociationEnd(Notation.getNotation(ctx), ae);
    }

    public static String generateMultiplicity(NotationContext ctx, MMultiplicity m) {
        return SINGLETON.generateMultiplicity(Notation.getNotation(ctx), m);
    }

    public static String generateState(NotationContext ctx, MState m) {
        return SINGLETON.generateState(Notation.getNotation(ctx), m);
    }

    public static String generateStateBody(NotationContext ctx, MState m) {
        return SINGLETON.generateStateBody(Notation.getNotation(ctx), m);
    }

    public static String generateTransition(NotationContext ctx, MTransition m) {
        return SINGLETON.generateTransition(Notation.getNotation(ctx), m);
    }

    public static String generateAction(NotationContext ctx, MAction m) {
        return SINGLETON.generateAction(Notation.getNotation(ctx), m);
    }

    public static String generateGuard(NotationContext ctx, MGuard m) {
        return SINGLETON.generateGuard(Notation.getNotation(ctx), m);
    }

    public static String generateMessage(NotationContext ctx, MMessage m) {
        return SINGLETON.generateMessage(Notation.getNotation(ctx), m);
    }

    public static String generateClassifierRef(NotationContext ctx, MClassifier cls) {
        return SINGLETON.generateClassifierRef(Notation.getNotation(ctx), cls);
    }

    public static String generateAssociationRole(NotationContext ctx, MAssociationRole m) {
        return SINGLETON.generateAssociationRole(Notation.getNotation(ctx), m);
    }

    /**
     * <p>General purpose static generator for any object that wishes to set
     *   the documented flag.</p>
     *
     * <p>Uses the class of the object to determine which method to
     *   invoke. Only actually looks for MOperation and MAttribute. All others
     *   invoke the simpler version with no documented flag, so taking the
     *   default version.</p>
     *
     * @param ctx        The context to look up the notation generator.
     *
     * @param o          The object to generate.
     *
     * @param documented  A flag of unknown meaning. Only has any effect for
     *                    {@link MOperation} and {@link MAttribute}.
     *
     * @return            The generated string.
     */
    public static String generate(NotationContext ctx, Object o, boolean documented) {
        if (o == null) return "";
        if (o instanceof MOperation) return SINGLETON.generateOperation(Notation.getNotation(ctx), (MOperation) o, documented);
        if (o instanceof MAttribute) return SINGLETON.generateAttribute(Notation.getNotation(ctx), (MAttribute) o, documented);
        return generate(ctx, o);
    }

    public static String generate(NotationContext ctx, Object o) {
        if (o == null) return "";
        if (o instanceof MAssociationRole) {
            return SINGLETON.generateAssociationRole(Notation.getNotation(ctx), (MAssociationRole) o);
        }
        if (o instanceof MExtensionPoint) {
            return SINGLETON.generateExtensionPoint(Notation.getNotation(ctx), (MExtensionPoint) o);
        }
        if (o instanceof MOperation) return SINGLETON.generateOperation(Notation.getNotation(ctx), (MOperation) o, false);
        if (o instanceof MAttribute) return SINGLETON.generateAttribute(Notation.getNotation(ctx), (MAttribute) o, false);
        if (o instanceof MParameter) return SINGLETON.generateParameter(Notation.getNotation(ctx), (MParameter) o);
        if (o instanceof MPackage) return SINGLETON.generatePackage(Notation.getNotation(ctx), (MPackage) o);
        if (o instanceof MClassifier) return SINGLETON.generateClassifier(Notation.getNotation(ctx), (MClassifier) o);
        if (o instanceof MExpression) return SINGLETON.generateExpression(Notation.getNotation(ctx), (MExpression) o);
        if (o instanceof String) return SINGLETON.generateName(Notation.getNotation(ctx), (String) o);
        if (o instanceof MStereotype) return SINGLETON.generateStereotype(Notation.getNotation(ctx), (MStereotype) o);
        if (o instanceof MTaggedValue) return SINGLETON.generateTaggedValue(Notation.getNotation(ctx), (MTaggedValue) o);
        if (o instanceof MAssociation) return SINGLETON.generateAssociation(Notation.getNotation(ctx), (MAssociation) o);
        if (o instanceof MAssociationEnd) return SINGLETON.generateAssociationEnd(Notation.getNotation(ctx), (MAssociationEnd) o);
        if (o instanceof MMultiplicity) return SINGLETON.generateMultiplicity(Notation.getNotation(ctx), (MMultiplicity) o);
        if (o instanceof MState) return SINGLETON.generateState(Notation.getNotation(ctx), (MState) o);
        if (o instanceof MTransition) return SINGLETON.generateTransition(Notation.getNotation(ctx), (MTransition) o);
        if (o instanceof MAction) return SINGLETON.generateAction(Notation.getNotation(ctx), (MAction) o);
        if (o instanceof MCallAction) return SINGLETON.generateAction(Notation.getNotation(ctx), (MAction) o);
        if (o instanceof MGuard) return SINGLETON.generateGuard(Notation.getNotation(ctx), (MGuard) o);
        if (o instanceof MMessage) return SINGLETON.generateMessage(Notation.getNotation(ctx), (MMessage) o);
        if (o instanceof MModelElement) return SINGLETON.generateName(Notation.getNotation(ctx), ((MModelElement) o).getName());
        if (o == null) return "";
        return o.toString();
    }

    public static NotationName getNotation(NotationContext context) {
        if (Configuration.getBoolean(Notation.KEY_UML_NOTATION_ONLY, false)) {
            return NOTATION_ARGO;
        }
        return context.getContextNotation();
    }

    /** Called after the notation default property gets changed.
     */
    public void propertyChange(PropertyChangeEvent pce) {
        cat.info("Notation change:" + pce.getOldValue() + " to " + pce.getNewValue());
        ArgoEventPump.fireEvent(new ArgoNotationEvent(ArgoEvent.NOTATION_CHANGED, pce));
    }

    public NotationProvider getDefaultProvider() {
        return NotationProviderFactory.getInstance().getDefaultProvider();
    }

    /** List of available notations.
     */
    public static ArrayList getAvailableNotations() {
        return NotationNameImpl.getAvailableNotations();
    }

    /** Create an unversioned notation name.
   */
    public static NotationName makeNotation(String k1) {
        return makeNotation(k1, null, null);
    }

    /** Create a versioned notation name.
   */
    public static NotationName makeNotation(String k1, String k2) {
        return makeNotation(k1, k2, null);
    }

    /** Create an unversioned notation name with an icon.
   */
    public static NotationName makeNotation(String k1, Icon icon) {
        return makeNotation(k1, null, icon);
    }

    /** Create a versioned notation name with an icon.
   */
    public static NotationName makeNotation(String k1, String k2, Icon icon) {
        NotationName nn = NotationNameImpl.makeNotation(k1, k2, icon);
        if (NOTATION_ARGO == null) {
            NOTATION_ARGO = nn;
        }
        return nn;
    }

    public static boolean getUseGuillemots() {
        return Configuration.getBoolean(KEY_USE_GUILLEMOTS, false);
    }

    public static void setUseGuillemots(boolean useGuillemots) {
        Configuration.setBoolean(KEY_USE_GUILLEMOTS, useGuillemots);
    }
}
