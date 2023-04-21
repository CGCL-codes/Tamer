package org.nakedobjects.plugins.headless.viewer.internal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.nakedobjects.applib.events.CollectionAccessEvent;
import org.nakedobjects.applib.events.InteractionEvent;
import org.nakedobjects.applib.events.ObjectTitleEvent;
import org.nakedobjects.applib.events.PropertyAccessEvent;
import org.nakedobjects.applib.events.UsabilityEvent;
import org.nakedobjects.applib.events.ValidityEvent;
import org.nakedobjects.applib.events.VisibilityEvent;
import org.nakedobjects.commons.lang.StringUtils;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;
import org.nakedobjects.metamodel.consent.InteractionResult;
import org.nakedobjects.metamodel.interactions.ObjectTitleContext;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.spec.JavaSpecification;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;
import org.nakedobjects.metamodel.spec.feature.NakedObjectMember;
import org.nakedobjects.metamodel.spec.feature.OneToManyAssociation;
import org.nakedobjects.metamodel.spec.feature.OneToOneAssociation;
import org.nakedobjects.metamodel.util.NakedObjectUtils;
import org.nakedobjects.plugins.headless.applib.DisabledException;
import org.nakedobjects.plugins.headless.applib.HeadlessViewer;
import org.nakedobjects.plugins.headless.applib.HiddenException;
import org.nakedobjects.plugins.headless.applib.InteractionException;
import org.nakedobjects.plugins.headless.applib.InvalidException;
import org.nakedobjects.plugins.headless.applib.ViewObject;
import org.nakedobjects.plugins.headless.applib.HeadlessViewer.ExecutionMode;
import org.nakedobjects.plugins.headless.viewer.internal.util.Constants;
import org.nakedobjects.plugins.headless.viewer.internal.util.MethodPrefixFinder;

public class DomainObjectInvocationHandler<T> extends DelegatingInvocationHandlerDefault<T> {

    /**
     * The <tt>title()</tt> method; may be <tt>null</tt>.
     */
    protected Method titleMethod;

    /**
     * The <tt>save()</tt> method from {@link ViewObject#save()}.
     */
    protected Method saveMethod;

    /**
     * The <tt>underlying()</tt> method from {@link ViewObject#underlying()}.
     */
    protected Method underlyingMethod;

    private final Map<Method, Collection<?>> collectionViewObjectsByMethod = new HashMap<Method, Collection<?>>();

    private final Map<Method, Map<?, ?>> mapViewObjectsByMethod = new HashMap<Method, Map<?, ?>>();

    public DomainObjectInvocationHandler(final T delegate, final HeadlessViewer embeddedViewer, final ExecutionMode mode, final RuntimeContext runtimeContext) {
        super(delegate, embeddedViewer, mode, runtimeContext);
        try {
            titleMethod = delegate.getClass().getMethod("title", new Class[] {});
            saveMethod = ViewObject.class.getMethod("save", new Class[] {});
            underlyingMethod = ViewObject.class.getMethod("underlying", new Class[] {});
        } catch (final NoSuchMethodException e) {
        }
    }

    @Override
    public Object invoke(final Object proxyObject, final Method method, final Object[] args) throws Throwable {
        if (isObjectMethod(method)) {
            return delegate(method, args);
        }
        final NakedObject targetAdapter = getRuntimeContext().getAdapterFor(getDelegate());
        if (isTitleMethod(method)) {
            return handleTitleMethod(method, args, targetAdapter);
        }
        final NakedObjectSpecification targetNoSpec = targetAdapter.getSpecification();
        if (isSaveMethod(method)) {
            return handleSaveMethod(getAuthenticationSession(), targetAdapter, targetNoSpec);
        }
        if (isUnderlyingMethod(method)) {
            return getDelegate();
        }
        final NakedObjectMember nakedObjectMember = locateAndCheckMember(method);
        final String memberName = nakedObjectMember.getName();
        final String methodName = method.getName();
        final String prefix = checkPrefix(methodName);
        if (isDefaultMethod(prefix) || isChoicesMethod(prefix)) {
            return method.invoke(getDelegate(), args);
        }
        final boolean isGetterMethod = isGetterMethod(prefix);
        final boolean isSetterMethod = isSetterMethod(prefix);
        final boolean isAddToMethod = isAddToMethod(prefix);
        final boolean isRemoveFromMethod = isRemoveFromMethod(prefix);
        checkVisibility(getAuthenticationSession(), targetAdapter, nakedObjectMember);
        if (nakedObjectMember.isOneToOneAssociation()) {
            final OneToOneAssociation otoa = (OneToOneAssociation) nakedObjectMember;
            if (isGetterMethod) {
                return handleGetterMethodOnProperty(args, targetAdapter, otoa, methodName);
            }
            if (isSetterMethod) {
                checkUsability(getAuthenticationSession(), targetAdapter, nakedObjectMember);
                return handleSetterMethodOnProperty(args, getAuthenticationSession(), targetAdapter, otoa, methodName);
            }
        }
        if (nakedObjectMember.isOneToManyAssociation()) {
            final OneToManyAssociation otma = (OneToManyAssociation) nakedObjectMember;
            if (isGetterMethod) {
                return handleGetterMethodOnCollection(method, args, targetAdapter, otma, memberName);
            }
            if (isAddToMethod) {
                checkUsability(getAuthenticationSession(), targetAdapter, nakedObjectMember);
                return handleCollectionAddToMethod(args, targetAdapter, otma, methodName);
            }
            if (isRemoveFromMethod) {
                checkUsability(getAuthenticationSession(), targetAdapter, nakedObjectMember);
                return handleCollectionRemoveFromMethod(args, targetAdapter, otma, methodName);
            }
        }
        if (isGetterMethod) {
            throw new UnsupportedOperationException(String.format("Can only invoke 'get' on properties or collections; '%s' represents %s", methodName, decode(nakedObjectMember)));
        }
        if (isSetterMethod) {
            throw new UnsupportedOperationException(String.format("Can only invoke 'set' on properties; '%s' represents %s", methodName, decode(nakedObjectMember)));
        }
        if (isAddToMethod) {
            throw new UnsupportedOperationException(String.format("Can only invoke 'addTo' on collections; '%s' represents %s", methodName, decode(nakedObjectMember)));
        }
        if (isRemoveFromMethod) {
            throw new UnsupportedOperationException(String.format("Can only invoke 'removeFrom' on collections; '%s' represents %s", methodName, decode(nakedObjectMember)));
        }
        if (nakedObjectMember instanceof NakedObjectAction) {
            checkUsability(getAuthenticationSession(), targetAdapter, nakedObjectMember);
            final NakedObjectAction noa = (NakedObjectAction) nakedObjectMember;
            return handleActionMethod(args, getAuthenticationSession(), targetAdapter, noa, memberName);
        }
        throw new UnsupportedOperationException(String.format("Unknown member type '%s'", nakedObjectMember));
    }

    private Object handleTitleMethod(final Method method, final Object[] args, final NakedObject targetAdapter) throws IllegalAccessException, InvocationTargetException {
        resolveIfRequired(targetAdapter);
        final NakedObjectSpecification targetNoSpec = targetAdapter.getSpecification();
        final ObjectTitleContext titleContext = targetNoSpec.createTitleInteractionContext(getAuthenticationSession(), InteractionInvocationMethod.BY_USER, targetAdapter);
        final ObjectTitleEvent titleEvent = titleContext.createInteractionEvent();
        notifyListeners(titleEvent);
        return titleEvent.getTitle();
    }

    private Object handleSaveMethod(final AuthenticationSession session, final NakedObject targetAdapter, final NakedObjectSpecification targetNoSpec) {
        final InteractionResult interactionResult = targetNoSpec.isValidResult(targetAdapter);
        notifyListenersAndVetoIfRequired(interactionResult);
        if (getExecutionMode() == ExecutionMode.EXECUTE) {
            if (targetAdapter.isTransient()) {
                getRuntimeContext().makePersistent(targetAdapter);
            }
        }
        return null;
    }

    private Object handleGetterMethodOnProperty(final Object[] args, final NakedObject targetAdapter, final OneToOneAssociation otoa, final String methodName) {
        if (args.length != 0) {
            throw new IllegalArgumentException("Invoking a 'get' should have no arguments");
        }
        resolveIfRequired(targetAdapter);
        final NakedObject currentReferencedAdapter = otoa.get(targetAdapter);
        final Object currentReferencedObj = NakedObjectUtils.unwrap(currentReferencedAdapter);
        final PropertyAccessEvent ev = new PropertyAccessEvent(getDelegate(), otoa.getIdentifier(), currentReferencedObj);
        notifyListeners(ev);
        return currentReferencedObj;
    }

    private Object handleSetterMethodOnProperty(final Object[] args, final AuthenticationSession session, final NakedObject targetAdapter, final OneToOneAssociation otoa, final String methodName) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Invoking a setter should only have a single argument");
        }
        resolveIfRequired(targetAdapter);
        final Object argumentObj = underlying(args[0]);
        final NakedObject argumentNO = argumentObj != null ? getRuntimeContext().adapterFor(argumentObj) : null;
        final InteractionResult interactionResult = otoa.isAssociationValid(targetAdapter, argumentNO).getInteractionResult();
        notifyListenersAndVetoIfRequired(interactionResult);
        if (getExecutionMode() == ExecutionMode.EXECUTE) {
            if (argumentNO != null) {
                otoa.setAssociation(targetAdapter, argumentNO);
            } else {
                otoa.clearAssociation(targetAdapter);
            }
        }
        objectChangedIfRequired(targetAdapter);
        return null;
    }

    private Object handleGetterMethodOnCollection(final Method method, final Object[] args, final NakedObject targetAdapter, final OneToManyAssociation otma, final String memberName) {
        if (args.length != 0) {
            throw new IllegalArgumentException("Invoking a 'get' should have no arguments");
        }
        resolveIfRequired(targetAdapter);
        final NakedObject currentReferencedAdapter = otma.get(targetAdapter);
        final Object currentReferencedObj = NakedObjectUtils.unwrap(currentReferencedAdapter);
        final CollectionAccessEvent ev = new CollectionAccessEvent(getDelegate(), otma.getIdentifier());
        if (currentReferencedObj instanceof Collection) {
            final Collection<?> collectionViewObject = lookupViewObject(method, memberName, (Collection<?>) currentReferencedObj, otma);
            notifyListeners(ev);
            return collectionViewObject;
        } else if (currentReferencedObj instanceof Map) {
            final Map<?, ?> mapViewObject = lookupViewObject(method, memberName, (Map<?, ?>) currentReferencedObj, otma);
            notifyListeners(ev);
            return mapViewObject;
        }
        throw new IllegalArgumentException(String.format("Collection type '%s' not supported by framework", currentReferencedObj.getClass().getName()));
    }

    /**
     * Looks up (or creates) a proxy for this object.
     */
    private Collection<?> lookupViewObject(final Method method, final String memberName, final Collection<?> collectionToLookup, final OneToManyAssociation otma) {
        Collection<?> collectionViewObject = collectionViewObjectsByMethod.get(method);
        if (collectionViewObject == null) {
            if (collectionToLookup instanceof ViewObject) {
                collectionViewObject = collectionToLookup;
            } else {
                collectionViewObject = Proxy.proxy(collectionToLookup, memberName, this, getRuntimeContext(), otma);
            }
            collectionViewObjectsByMethod.put(method, collectionViewObject);
        }
        return collectionViewObject;
    }

    private Map<?, ?> lookupViewObject(final Method method, final String memberName, final Map<?, ?> mapToLookup, final OneToManyAssociation otma) {
        Map<?, ?> mapViewObject = mapViewObjectsByMethod.get(method);
        if (mapViewObject == null) {
            if (mapToLookup instanceof ViewObject) {
                mapViewObject = mapToLookup;
            } else {
                mapViewObject = Proxy.proxy(mapToLookup, memberName, this, getRuntimeContext(), otma);
            }
            mapViewObjectsByMethod.put(method, mapViewObject);
        }
        return mapViewObject;
    }

    private Object handleCollectionAddToMethod(final Object[] args, final NakedObject targetAdapter, final OneToManyAssociation otma, final String methodName) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Invoking a addTo should only have a single argument");
        }
        resolveIfRequired(targetAdapter);
        final Object argumentObj = underlying(args[0]);
        if (argumentObj == null) {
            throw new IllegalArgumentException("Must provide a non-null object to add");
        }
        final NakedObject argumentNO = getRuntimeContext().adapterFor(argumentObj);
        final InteractionResult interactionResult = otma.isValidToAdd(targetAdapter, argumentNO).getInteractionResult();
        notifyListenersAndVetoIfRequired(interactionResult);
        if (getExecutionMode() == ExecutionMode.EXECUTE) {
            otma.addElement(targetAdapter, argumentNO);
        }
        objectChangedIfRequired(targetAdapter);
        return null;
    }

    private Object handleCollectionRemoveFromMethod(final Object[] args, final NakedObject targetAdapter, final OneToManyAssociation otma, final String methodName) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Invoking a removeFrom should only have a single argument");
        }
        resolveIfRequired(targetAdapter);
        final Object argumentObj = underlying(args[0]);
        if (argumentObj == null) {
            throw new IllegalArgumentException("Must provide a non-null object to remove");
        }
        final NakedObject argumentAdapter = getRuntimeContext().adapterFor(argumentObj);
        final InteractionResult interactionResult = otma.isValidToRemove(targetAdapter, argumentAdapter).getInteractionResult();
        notifyListenersAndVetoIfRequired(interactionResult);
        if (getExecutionMode() == ExecutionMode.EXECUTE) {
            otma.removeElement(targetAdapter, argumentAdapter);
        }
        objectChangedIfRequired(targetAdapter);
        return null;
    }

    private Object handleActionMethod(final Object[] args, final AuthenticationSession session, final NakedObject targetAdapter, final NakedObjectAction noa, final String memberName) {
        final Object[] underlyingArgs = new Object[args.length];
        int i = 0;
        for (final Object arg : args) {
            underlyingArgs[i++] = underlying(arg);
        }
        final NakedObject[] argAdapters = new NakedObject[underlyingArgs.length];
        int j = 0;
        for (final Object underlyingArg : underlyingArgs) {
            argAdapters[j++] = underlyingArg != null ? getRuntimeContext().adapterFor(underlyingArg) : null;
        }
        final InteractionResult interactionResult = noa.isProposedArgumentSetValid(targetAdapter, argAdapters).getInteractionResult();
        notifyListenersAndVetoIfRequired(interactionResult);
        if (getExecutionMode() == ExecutionMode.EXECUTE) {
            final NakedObject actionReturnNO = noa.execute(targetAdapter, argAdapters);
            return NakedObjectUtils.unwrap(actionReturnNO);
        }
        objectChangedIfRequired(targetAdapter);
        return null;
    }

    private Object underlying(final Object arg) {
        if (arg instanceof ViewObject<?>) {
            final ViewObject<?> argViewObject = (ViewObject<?>) arg;
            return argViewObject.underlying();
        } else {
            return arg;
        }
    }

    private void checkVisibility(final AuthenticationSession session, final NakedObject targetNakedObject, final NakedObjectMember nakedObjectMember) {
        final InteractionResult interactionResult = nakedObjectMember.isVisible(getAuthenticationSession(), targetNakedObject).getInteractionResult();
        notifyListenersAndVetoIfRequired(interactionResult);
    }

    private void checkUsability(final AuthenticationSession session, final NakedObject targetNakedObject, final NakedObjectMember nakedObjectMember) {
        final InteractionResult interactionResult = nakedObjectMember.isUsable(getAuthenticationSession(), targetNakedObject).getInteractionResult();
        notifyListenersAndVetoIfRequired(interactionResult);
    }

    private void notifyListenersAndVetoIfRequired(final InteractionResult interactionResult) {
        final InteractionEvent interactionEvent = interactionResult.getInteractionEvent();
        notifyListeners(interactionEvent);
        if (interactionEvent.isVeto()) {
            throw toException(interactionEvent);
        }
    }

    private String decode(final NakedObjectMember nakedObjectMember) {
        if (nakedObjectMember instanceof OneToOneAssociation) {
            return "a property";
        }
        if (nakedObjectMember instanceof OneToManyAssociation) {
            return "a collection";
        }
        if (nakedObjectMember instanceof NakedObjectAction) {
            return "an action";
        }
        return "an UNKNOWN member type";
    }

    /**
     * Wraps a {@link InteractionEvent#isVeto() vetoing} {@link InteractionEvent} in a corresponding
     * {@link InteractionException}, and returns it.
     */
    private InteractionException toException(final InteractionEvent interactionEvent) {
        if (!interactionEvent.isVeto()) {
            throw new IllegalArgumentException("Provided interactionEvent must be a veto");
        }
        if (interactionEvent instanceof ValidityEvent) {
            final ValidityEvent validityEvent = (ValidityEvent) interactionEvent;
            return new InvalidException(validityEvent);
        }
        if (interactionEvent instanceof VisibilityEvent) {
            final VisibilityEvent visibilityEvent = (VisibilityEvent) interactionEvent;
            return new HiddenException(visibilityEvent);
        }
        if (interactionEvent instanceof UsabilityEvent) {
            final UsabilityEvent usabilityEvent = (UsabilityEvent) interactionEvent;
            return new DisabledException(usabilityEvent);
        }
        throw new IllegalArgumentException("Provided interactionEvent must be a VisibilityEvent, UsabilityEvent or a ValidityEvent");
    }

    private NakedObjectMember locateAndCheckMember(final Method method) {
        final JavaSpecification javaSpecification = getJavaSpecificationOfOwningClass(method);
        final NakedObjectMember member = javaSpecification.getMember(method);
        if (member == null) {
            final String methodName = method.getName();
            throw new UnsupportedOperationException("Method '" + methodName + "' being invoked does not correspond to any of the object's fields or actions.");
        }
        return member;
    }

    private String checkPrefix(final String methodName) {
        final String prefix = new MethodPrefixFinder().findPrefix(methodName);
        if (StringUtils.in(prefix, Constants.INVALID_PREFIXES)) {
            throw new UnsupportedOperationException(String.format("Cannot invoke methods with prefix '%s'; use only get/set/addTo/removeFrom/action", prefix));
        }
        return prefix;
    }

    protected boolean isTitleMethod(final Method method) {
        return method.equals(titleMethod);
    }

    protected boolean isSaveMethod(final Method method) {
        return method.equals(saveMethod);
    }

    protected boolean isUnderlyingMethod(final Method method) {
        return method.equals(underlyingMethod);
    }

    private boolean isGetterMethod(final String prefix) {
        return prefix.equals(Constants.PREFIX_GET);
    }

    private boolean isSetterMethod(final String prefix) {
        return prefix.equals(Constants.PREFIX_SET);
    }

    private boolean isAddToMethod(final String prefix) {
        return prefix.equals(Constants.PREFIX_ADD_TO);
    }

    private boolean isRemoveFromMethod(final String prefix) {
        return prefix.equals(Constants.PREFIX_REMOVE_FROM);
    }

    private boolean isChoicesMethod(final String prefix) {
        return prefix.equals(Constants.PREFIX_CHOICES);
    }

    private boolean isDefaultMethod(final String prefix) {
        return prefix.equals(Constants.PREFIX_DEFAULT);
    }

    private JavaSpecification getJavaSpecificationOfOwningClass(final Method method) {
        return getJavaSpecification(method.getDeclaringClass());
    }

    private JavaSpecification getJavaSpecification(final Class<?> clazz) {
        final NakedObjectSpecification nos = getSpecification(clazz);
        if (!(nos instanceof JavaSpecification)) {
            throw new UnsupportedOperationException("Only Java is supported (specification is '" + nos.getClass().getCanonicalName() + "')");
        }
        return (JavaSpecification) nos;
    }

    private NakedObjectSpecification getSpecification(final Class<?> type) {
        final NakedObjectSpecification nos = getSpecificationLoader().loadSpecification(type);
        return nos;
    }
}
