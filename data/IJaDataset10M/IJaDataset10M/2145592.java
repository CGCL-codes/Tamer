package org.nakedobjects.xat2;

import org.nakedobjects.nof.reflect.javax.JavaxDomainObjectContainer;
import org.nakedobjects.nof.reflect.javax.Utils;
import org.nakedobjects.nof.reflect.javax.exceptions.AbstractSemanticException;
import org.nakedobjects.nof.reflect.javax.exceptions.BoundedException;
import org.nakedobjects.nof.reflect.javax.exceptions.Declarative;
import org.nakedobjects.nof.reflect.javax.exceptions.DisabledException;
import org.nakedobjects.nof.reflect.javax.exceptions.HiddenException;
import org.nakedobjects.nof.reflect.javax.exceptions.ImmutableException;
import org.nakedobjects.nof.reflect.javax.exceptions.ImperativeAuthorization;
import org.nakedobjects.nof.reflect.javax.exceptions.InvalidDeclarativelyException;
import org.nakedobjects.nof.reflect.javax.exceptions.InvalidException;
import org.nakedobjects.nof.reflect.javax.exceptions.InvalidImperativelyException;
import org.nakedobjects.nof.reflect.javax.exceptions.InvalidMandatoryException;
import org.nakedobjects.nof.reflect.javax.exceptions.InvalidMaxLengthException;
import org.nakedobjects.nof.reflect.javax.exceptions.InvalidRegExException;
import org.nakedobjects.nof.reflect.javax.listeners.ActionEvent;
import org.nakedobjects.nof.reflect.javax.listeners.CollectionInteractionEvent;
import org.nakedobjects.nof.reflect.javax.listeners.CollectionModifiedEvent;
import org.nakedobjects.nof.reflect.javax.listeners.CollectionReadEvent;
import org.nakedobjects.nof.reflect.javax.listeners.InteractionEvent;
import org.nakedobjects.nof.reflect.javax.listeners.InteractionListener;
import org.nakedobjects.nof.reflect.javax.listeners.PropertyModifiedEvent;
import org.nakedobjects.nof.reflect.javax.listeners.PropertyReadEvent;
import org.nakedobjects.nof.reflect.javax.listeners.TitleEvent;
import org.nakedobjects.nos.client.xat.Documentor;

/**
 * Acts as the go-between between interactions with domain objects (via the
 * proxy infrastructure provided by {@link JavaxDomainObjectContainer}) and
 * the XAT {@link Documentor}.
 */
public class DocumentProxyInteractionsListener implements InteractionListener {

    public DocumentProxyInteractionsListener(Documentor documentor) {
        setDocumentor(documentor);
    }

    private Documentor documentor;

    public void setDocumentor(Documentor documentor) {
        this.documentor = documentor;
    }

    /**
     * Documents an action being invoked.
     * 
     * <p>
     * <ul>
     * <li>Invoke the 'Place Order' action with arguments 'xx', 'yy' and 'zz'; it should succeed.
     * <li>As per {@link #interpretException(AbstractSemanticException, String, String)} if there was an exception.
     * </ul>
     */
    public void actionInvoked(ActionEvent ev) {
        StringBuffer buf = new StringBuffer();
        String memberPhrase = memberPhrase("action", ev);
        AbstractSemanticException exception = ev.getException();
        if (exception == null) {
            buf.append("Invoke " + memberPhrase + " with " + argsPhrase(ev.getArgs()) + "; it should succeed.");
        } else {
            interpretException(buf, exception, memberPhrase, verbArgsPhrase(ev.getArgs()));
            if (!(exception instanceof InvalidException)) {
                buf.append("invoked.");
            }
        }
        documentor.docln(buf.toString());
    }

    /**
     * Documents an collection being added to or removed from.
     * 
     * <p>
     * <ul>
     * <li>Add the argument 'xx' to the 'Orders' collection; it should succeed.</li>
     * <li>Remove the argument 'xx' to the 'Orders' collection; it should succeed.</li>
     * <li>As per {@link #interpretException(AbstractSemanticException, String, String)} if there was an exception.
     * </ul>
     */
    public void collectionModified(CollectionModifiedEvent ev) {
        StringBuffer buf = new StringBuffer();
        String memberPhrase = memberPhrase("collection", ev);
        String verbArgPhrase = verbArgPhrase(isAdding(ev) ? "Add" : "Remove", ev.getArg(), isAdding(ev) ? "to" : "from");
        AbstractSemanticException exception = ev.getException();
        if (exception == null) {
            buf.append(verbArgPhrase + memberPhrase + "; it should succeed.");
        } else {
            interpretException(buf, exception, memberPhrase, verbArgPhrase);
            if (!(exception instanceof InvalidException)) {
                buf.append(isAdding(ev) ? "added to." : "removed from.");
            }
        }
        documentor.docln(buf.toString());
    }

    private boolean isAdding(CollectionModifiedEvent ev) {
        return ev.getType() == CollectionModifiedEvent.Type.addTo;
    }

    /**
     * Documents a collection being read.
     * 
     * <p>
     * <ul>
     * <li>Inspect the 'Orders' collection; </li>
     * <li>As per {@link #interpretException(AbstractSemanticException, String, String)} if there was an exception.
     * </ul>
     */
    public void collectionRead(CollectionReadEvent ev) {
        StringBuffer buf = new StringBuffer();
        String memberPhrase = memberPhrase("collection", ev);
        AbstractSemanticException exception = ev.getException();
        if (exception == null) {
            buf.append("Inspect " + memberPhrase + ". ");
            documentor.doc(buf.toString());
        } else {
            interpretException(buf, exception, memberPhrase, null);
            buf.append("read.");
            documentor.docln(buf.toString());
        }
    }

    public void collectionInteracted(CollectionInteractionEvent ev) {
        StringBuffer buf = new StringBuffer();
        buf.append(ev.getInteractionPhrase());
        documentor.docln(buf.toString());
    }

    /**
     * Documents a property being modified.
     * 
     * <p>
     * <ul>
     * <li>Set the argument 'xx' for the 'First Name' property; it should succeed.</li>
     * <li>Clear the 'First Name' property; it should succeed.</li>
     * <li>As per {@link #interpretException(AbstractSemanticException, String, String)} if there was an exception.
     * </ul>
     */
    public void propertyModified(PropertyModifiedEvent ev) {
        StringBuffer buf = new StringBuffer();
        String memberPhrase = memberPhrase("property", ev);
        String verbArgPhrase = verbArgPhrase(isSetting(ev) ? "Set" : "Clear", Utils.format2(ev.getArg()), "for");
        AbstractSemanticException exception = ev.getException();
        if (exception == null) {
            buf.append(verbArgPhrase + memberPhrase + "; it should succeed.");
        } else {
            interpretException(buf, exception, memberPhrase, verbArgPhrase);
            if (!(exception instanceof InvalidException)) {
                buf.append(isSetting(ev) ? "modified." : "cleared.");
            }
        }
        documentor.docln(buf.toString());
    }

    private boolean isSetting(PropertyModifiedEvent ev) {
        return ev.getArg() != null;
    }

    /**
     * Documents a property being read.
     * 
     * <p>
     * <ul>
     * <li>Inspect the 'First Name' property; it should contain the value 'Richard'.</li>
     * <li>As per {@link #interpretException(AbstractSemanticException, String, String)} if there was an exception.
     * </ul>
     */
    public void propertyRead(PropertyReadEvent ev) {
        StringBuffer buf = new StringBuffer();
        String memberPhrase = memberPhrase("property", ev);
        Object value = ev.getValue();
        String argPhrase = argsPhrase(value);
        AbstractSemanticException exception = ev.getException();
        if (exception == null) {
            buf.append("Inspect " + memberPhrase + "; it should ");
            if (!isEmpty(value)) {
                buf.append("contain the value " + argPhrase + ".");
            } else {
                buf.append("be empty.");
            }
        } else {
            interpretException(buf, exception, memberPhrase, null);
            buf.append("read.");
        }
        documentor.docln(buf.toString());
    }

    private boolean isEmpty(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof String && ((String) value).length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * Documents the title being read.
     * 
     * <p>
     * <ul>
     * <li>The title should read 'Fred Bloggs'.
     * </ul>
     */
    public void titleRead(TitleEvent ev) {
        documentor.docln("The title should read '" + ev.getTitle() + "'.");
    }

    /**
     * For example:
     * <ul>
     * <li> <tt>'First Name' property</tt> </li>
     * <li> <tt>'Place Order' action</tt> </li>
     * </ul>
     * 
     * <p>
     * Note there is no trailing space.
     * 
     * @param memberType
     * @param ev
     * @return
     */
    private String memberPhrase(String memberType, InteractionEvent ev) {
        return "the '" + ev.getMemberName() + "' " + memberType;
    }

    private String verbArgPhrase(String verb, Object arg, String preposition) {
        if (arg != null) {
            return verb + " the argument '" + Utils.format2(arg) + "' " + preposition + " ";
        } else {
            return verb + " ";
        }
    }

    private String argsPhrase(Object value) {
        return "'" + Utils.format2(value) + "'";
    }

    private String verbArgsPhrase(Object[] args) {
        if (args.length == 0) {
            return "invoke ";
        }
        StringBuilder builder = new StringBuilder("specify the argument");
        if (args.length > 1) {
            builder.append("s");
        }
        builder.append(" ");
        builder.append(argsPhrase(args));
        builder.append(" for ");
        return builder.toString();
    }

    private String argsPhrase(Object[] args) {
        if (args.length == 0) {
            return "no arguments ";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            String formattedArg = Utils.format2(args[i]);
            if (formattedArg != null) {
                builder.append("'").append(formattedArg).append("'");
            } else {
                builder.append("<null>");
            }
            if (args.length > 1) {
                if (i < args.length - 2) {
                    builder.append(", ");
                }
                if (i == args.length - 2) {
                    builder.append(" and ");
                }
            }
        }
        return builder.toString();
    }

    /**
     * Interprets the exception, documenting to the {@link #documentor}.
     * @param buf 
     * 
     * @param ex
     * @param memberPhrase
     * @param argsPhrase - used only for {@link InvalidException}s. 
     */
    private void interpretException(StringBuffer buf, AbstractSemanticException ex, String memberPhrase, String argsPhrase) {
        if (ex instanceof BoundedException) {
            BoundedException boundedException = (BoundedException) ex;
            interpretException(buf, boundedException, memberPhrase);
            return;
        }
        if (ex instanceof ImmutableException) {
            ImmutableException immutableException = (ImmutableException) ex;
            interpretException(buf, immutableException, memberPhrase);
            return;
        }
        if (ex instanceof HiddenException) {
            HiddenException hiddenException = (HiddenException) ex;
            interpretException(buf, hiddenException, memberPhrase);
            return;
        }
        if (ex instanceof DisabledException) {
            DisabledException disabledException = (DisabledException) ex;
            interpretException(buf, disabledException, memberPhrase);
            return;
        }
        if (ex instanceof InvalidException) {
            InvalidException invalidException = (InvalidException) ex;
            interpretException(buf, invalidException, memberPhrase, argsPhrase);
            return;
        }
    }

    /**
     * <ul>
     * <li>Because the object is bounded, the 'Place Order' action cannot be ...</li>
     * <li>Because the object is declared as being bounded, the 'Place Order' action 
     *     cannot be ...</li>
     * </ul>
     * 
     * @param ex
     * @param memberPhrase
     */
    private void interpretException(StringBuffer buf, BoundedException ex, String memberPhrase) {
        buf.append("Because the object is ");
        if (ex instanceof Declarative) {
            buf.append("declared as being ");
        }
        buf.append("bounded, " + memberPhrase + " cannot be ");
    }

    /**
     * <ul>
     * <li>Because the object is immutable, the 'Place Order' action cannot be ...</li>
     * <li>Because the object is declared as being immutable, the 'Place Order' action 
     *     cannot be ...</li>
     * </ul>
     * 
     * @param ex
     * @param memberPhrase
     */
    private void interpretException(StringBuffer buf, ImmutableException ex, String memberPhrase) {
        buf.append("Because the object is ");
        if (ex instanceof Declarative) {
            buf.append("declared as being ");
        }
        buf.append("immutable, " + memberPhrase + " cannot be ");
    }

    /**
     * <ul>
     * <li>Because the 'Place Order' action is declared as being hidden, it cannot be...</li>  
     * <li>Because the 'Place Order' action is currently hidden, it cannot be...</li>  
     * <li>Because the 'Place Order' action is hidden for this user, it cannot be...</li>  
     * </ul>
     * 
     * @param ex
     * @param memberPhrase
     */
    private void interpretException(StringBuffer buf, HiddenException ex, String memberPhrase) {
        buf.append("Because " + memberPhrase + " is ");
        if (ex instanceof Declarative) {
            buf.append("declared as being hidden");
        } else if (ex instanceof ImperativeAuthorization) {
            buf.append("hidden for this user");
        } else {
            buf.append("currently hidden");
        }
        buf.append(", it cannot be ");
    }

    /**
     * <ul>
     * <li>Because the 'Place Order' action is declared as being disabled, it cannot be ...</li>
     * <li>Because the 'Place Order' action is currently disabled (customer blacklisted), it cannot be ...</li>
     * <li>Because the 'Place Order' action is disabled for this user, it cannot be ...</li>
     * </ul>
     * 
     * @param ex
     * @param memberPhrase
     */
    private void interpretException(StringBuffer buf, DisabledException ex, String memberPhrase) {
        buf.append("Because " + memberPhrase + " is ");
        if (ex instanceof Declarative) {
            buf.append("declared as being disabled");
        } else if (ex instanceof ImperativeAuthorization) {
            buf.append("disabled for this user");
        } else {
            buf.append("currently disabled (" + ex.getMessage() + ")");
        }
        buf.append(", it cannot be ");
    }

    /**
     * <ul>
     * <li>Imperative:
     *     <ul>
     *     <li>Attempt to specify the arguments 'xx', 'yy' and 'zz' for the 'Place Order' action;
     *         this will be rejected (quantity must be positive).</li>
     *     <li>Attempt to set the argument 'xx' for the 'Amount' property;
     *         this will be rejected (amount must be positive).</li>
     *     <li>Attempt to add the argument 'xx' to the 'Orders' collection;
     *         this will be rejected (amount must be positive).</li>
     *     <li>Attempt to remove the argument 'xx' from the 'Preferences' collection;
     *         this will be rejected (cannot remove last preference).</li>
     *     </ul>
     * <li>Imperative (authorization):
     *     <ul>
     *     <li>Attempt to specify the arguments 'xx', 'yy' and 'zz' for the 'Place Order' action;
     *         this will be rejected (user not authorized).</li>
     *     <li>Attempt to set the argument 'xx' for the 'Amount' property;
     *         this will be rejected (user not authorized).</li>
     *     <li>Attempt to add the argument 'xx' to the 'Orders' collection;
     *         this will be rejected (user not authorized).</li>
     *     <li>Attempt to remove the argument 'xx' from the 'Preferences' collection;
     *         this will be rejected (user not authorized).</li>
     *     </ul>
     * <li>Declarative (properties only):
     *     <ul>
     *     <li>Attempt to set the argument 'xx' for the 'Amount' property;
     *         because the property is mandatory, this will be rejected.</li>
     *     <li>Attempt to set the argument 'xx' for the 'Amount' property;
     *         because this exceeds the declared maximum length of NN, 
     *         this will be rejected.</li>
     *     <li>Attempt to set the argument 'xx' for the 'Amount' property;
     *         because this does not meet the regular expression constraint [ZZZ], 
     *         this will be rejected.</li>
     *     </ul>
     * </ul>
     * 
     * @param ex
     * @param memberPhrase
     */
    private void interpretException(StringBuffer buf, InvalidException ex, String memberPhrase, String verbArgPhrase) {
        verbArgPhrase = lowerFirst(verbArgPhrase);
        buf.append("Attempt to " + verbArgPhrase + memberPhrase + "; ");
        if (ex instanceof InvalidDeclarativelyException) {
            buf.append("because ");
            if (ex instanceof InvalidMandatoryException) {
                buf.append("the property is mandatory, ");
            } else if (ex instanceof InvalidMaxLengthException) {
                InvalidMaxLengthException invalidMaxLengthException = (InvalidMaxLengthException) ex;
                buf.append("this exceeds the declared maximum length of " + invalidMaxLengthException.getMaximumLength() + ", ");
            } else if (ex instanceof InvalidRegExException) {
                InvalidRegExException invalidRegExException = (InvalidRegExException) ex;
                buf.append("this does not meet the regular expression constraint, [" + invalidRegExException.getValidation() + "], ");
            }
            buf.append("it will be rejected.");
        } else if (ex instanceof InvalidImperativelyException) {
            InvalidImperativelyException invalidImperativelyException = (InvalidImperativelyException) ex;
            buf.append("this will be rejected (");
            if (ex instanceof ImperativeAuthorization) {
                buf.append("user not authorized");
            } else {
                buf.append(invalidImperativelyException.getMessage());
            }
            buf.append(").");
        }
    }

    private String lowerFirst(String argsPhrase) {
        if (argsPhrase == null || argsPhrase.length() == 0) {
            return argsPhrase;
        }
        if (argsPhrase.length() == 1) {
            return argsPhrase.toLowerCase();
        }
        return argsPhrase.substring(0, 1).toLowerCase() + argsPhrase.substring(1);
    }
}
