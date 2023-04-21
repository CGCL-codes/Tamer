package org.nakedobjects.nof.reflect.java.facets.collections;

import org.nakedobjects.applib.security.UserMemento;
import org.nakedobjects.noa.facets.FacetHolder;
import org.nakedobjects.noa.facets.FacetUtil;
import org.nakedobjects.noa.facets.MethodRemover;
import org.nakedobjects.noa.facets.MethodScope;
import org.nakedobjects.noa.reflect.NakedObjectFeatureType;
import org.nakedobjects.noa.util.StringUtils;
import org.nakedobjects.nof.reflect.java.facets.PropertyOrCollectionIdentifyingFacetFactoryAbstract;
import org.nakedobjects.nof.reflect.java.facets.actcoll.typeof.TypeOfFacetInferredFromSupportingMethods;
import org.nakedobjects.nof.reflect.java.facets.collections.modify.CollectionAddToFacetViaAccessor;
import org.nakedobjects.nof.reflect.java.facets.collections.modify.CollectionAddToFacetViaMethod;
import org.nakedobjects.nof.reflect.java.facets.collections.modify.CollectionClearFacetViaAccessor;
import org.nakedobjects.nof.reflect.java.facets.collections.modify.CollectionClearFacetViaMethod;
import org.nakedobjects.nof.reflect.java.facets.collections.modify.CollectionRemoveFromFacetViaAccessor;
import org.nakedobjects.nof.reflect.java.facets.collections.modify.CollectionRemoveFromFacetViaMethod;
import org.nakedobjects.nof.reflect.java.facets.collections.validate.CollectionValidateAddToFacetViaMethod;
import org.nakedobjects.nof.reflect.java.facets.collections.validate.CollectionValidateRemoveFromFacetViaMethod;
import org.nakedobjects.nof.reflect.java.facets.propcoll.access.PropertyAccessorFacetViaAccessor;
import org.nakedobjects.nof.reflect.java.facets.properties.choices.PropertyChoicesFacetViaMethod;
import org.nakedobjects.nof.reflect.java.reflect.collections.JavaOneToManyAssociationPeer;
import org.nakedobjects.nof.reflect.java.reflect.util.NameUtils;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class CollectionFieldMethodsFacetFactory extends PropertyOrCollectionIdentifyingFacetFactoryAbstract {

    private static final Logger LOG = Logger.getLogger(CollectionFieldMethodsFacetFactory.class);

    private static final String REMOVE_FROM_PREFIX = "removeFrom";

    private static final String ADD_TO_PREFIX = "addTo";

    protected static final String CLEAR_PREFIX = "clear";

    protected static final String GET_PREFIX = "get";

    protected static final String MODIFY_PREFIX = "modify";

    protected static final String SET_PREFIX = "set";

    private static final String OPTIONAL_PREFIX = "optional";

    private static final String VALIDATE_ADD_TO_PREFIX = VALIDATE_PREFIX + ADD_TO_PREFIX;

    private static final String VALIDATE_ADD_TO_PREFIX_2 = VALIDATE_PREFIX + StringUtils.capitalize(ADD_TO_PREFIX);

    private static final String VALIDATE_REMOVE_FROM_PREFIX = VALIDATE_PREFIX + REMOVE_FROM_PREFIX;

    private static final String VALIDATE_REMOVE_FROM_PREFIX_2 = VALIDATE_PREFIX + StringUtils.capitalize(REMOVE_FROM_PREFIX);

    private static final String[] PREFIXES = { REMOVE_FROM_PREFIX, ADD_TO_PREFIX, CLEAR_PREFIX, GET_PREFIX, MODIFY_PREFIX, SET_PREFIX, OPTIONAL_PREFIX, VALIDATE_ADD_TO_PREFIX, VALIDATE_REMOVE_FROM_PREFIX };

    public CollectionFieldMethodsFacetFactory() {
        super(PREFIXES, NakedObjectFeatureType.COLLECTIONS_ONLY);
    }

    @Override
    public boolean process(final Method collectionAccessor, final MethodRemover methodRemover, final FacetHolder collection) {
        final String capitalizedName = NameUtils.javaBaseName(collectionAccessor.getName());
        final Class returnType = collectionAccessor.getReturnType();
        final Class cls = collectionAccessor.getDeclaringClass();
        final List facets = new ArrayList();
        removeMethod(methodRemover, collectionAccessor);
        facets.add(new PropertyAccessorFacetViaAccessor(collectionAccessor, collection));
        final Class addToType = findAndRemoveAddToMethod(facets, methodRemover, cls, collectionAccessor, capitalizedName, collection);
        final Class removeFromType = findAndRemoveRemoveFromMethod(facets, methodRemover, cls, collectionAccessor, capitalizedName, collection);
        final Class collectionType = inferTypeOfIfPossible(facets, collectionAccessor, addToType, removeFromType, collection);
        findAndRemoveClearMethod(facets, methodRemover, cls, collectionAccessor, capitalizedName, collection);
        findAndRemoveValidateAddToMethod(facets, methodRemover, cls, collectionType, capitalizedName, returnType, collection);
        findAndRemoveValidateRemoveFromMethod(facets, methodRemover, cls, collectionType, capitalizedName, returnType, collection);
        findAndRemoveNameMethod(facets, methodRemover, cls, capitalizedName, collection);
        findAndRemoveDescriptionMethod(facets, methodRemover, cls, capitalizedName, collection);
        findAndRemoveAlwaysHideMethod(facets, methodRemover, cls, capitalizedName, collection);
        findAndRemoveProtectMethod(facets, methodRemover, cls, capitalizedName, collection);
        findAndRemoveHideForSessionMethod(facets, methodRemover, cls, capitalizedName, UserMemento.class, collection);
        findAndRemoveDisableForSessionMethod(facets, methodRemover, cls, capitalizedName, UserMemento.class, collection);
        findAndRemoveHideMethod(facets, methodRemover, cls, OBJECT, capitalizedName, collection);
        findAndRemoveDisableMethod(facets, methodRemover, cls, OBJECT, capitalizedName, collection);
        return FacetUtil.addFacets(facets);
    }

    private Class findAndRemoveAddToMethod(final List collectionFacets, final MethodRemover methodRemover, final Class cls, final Method getMethod, final String capitalizedName, final FacetHolder collection) {
        final Method method = findMethod(cls, OBJECT, ADD_TO_PREFIX + capitalizedName, void.class);
        removeMethod(methodRemover, method);
        final Class addType = (method == null || method.getParameterTypes().length != 1) ? null : method.getParameterTypes()[0];
        if (method != null) {
            collectionFacets.add(new CollectionAddToFacetViaMethod(method, collection));
        } else {
            collectionFacets.add(new CollectionAddToFacetViaAccessor(getMethod, collection));
        }
        return addType;
    }

    private Class findAndRemoveRemoveFromMethod(final List collectionFacets, final MethodRemover methodRemover, final Class cls, final Method getMethod, final String capitalizedName, final FacetHolder collection) {
        final Method method = findMethod(cls, OBJECT, REMOVE_FROM_PREFIX + capitalizedName, void.class);
        removeMethod(methodRemover, method);
        final Class removeType = (method == null || method.getParameterTypes().length != 1) ? null : method.getParameterTypes()[0];
        if (method != null) {
            collectionFacets.add(new CollectionRemoveFromFacetViaMethod(method, collection));
        } else {
            collectionFacets.add(new CollectionRemoveFromFacetViaAccessor(getMethod, collection));
        }
        return removeType;
    }

    private void findAndRemoveClearMethod(final List collectionFacets, final MethodRemover methodRemover, final Class cls, final Method getMethod, final String capitalizedName, final FacetHolder collection) {
        final Method method = findMethod(cls, OBJECT, CLEAR_PREFIX + capitalizedName, void.class, null);
        removeMethod(methodRemover, method);
        if (method != null) {
            collectionFacets.add(new CollectionClearFacetViaMethod(method, collection));
        } else {
            collectionFacets.add(new CollectionClearFacetViaAccessor(getMethod, collection));
        }
    }

    private Class inferTypeOfIfPossible(final List collectionFacets, final Method getMethod, final Class addType, final Class removeType, final FacetHolder collection) {
        final Class returnType = getMethod.getReturnType();
        Class type = null;
        if (addType != null && removeType != null && addType != removeType) {
            LOG.warn("The addTo/removeFrom methods " + getMethod.getDeclaringClass() + " must " + "both deal with same type of object: " + addType + "; " + removeType);
            return null;
        }
        type = addType != null ? addType : removeType;
        if (type != null) {
            collectionFacets.add(new TypeOfFacetInferredFromSupportingMethods(type, collection, getReflector()));
        }
        return type;
    }

    private void findAndRemoveValidateAddToMethod(final List collectionFacets, final MethodRemover methodRemover, final Class cls, final Class collectionType, final String capitalizedName, final Class returnType, final FacetHolder collection) {
        Method method = findMethod(cls, OBJECT, VALIDATE_ADD_TO_PREFIX + capitalizedName, String.class, paramTypesOrNull(collectionType));
        if (method == null) {
            method = findMethod(cls, OBJECT, VALIDATE_ADD_TO_PREFIX_2 + capitalizedName, String.class, paramTypesOrNull(collectionType));
        }
        if (method == null) {
            return;
        }
        removeMethod(methodRemover, method);
        collectionFacets.add(new CollectionValidateAddToFacetViaMethod(method, collection));
    }

    private void findAndRemoveValidateRemoveFromMethod(final List collectionFacets, final MethodRemover methodRemover, final Class cls, final Class collectionType, final String capitalizedName, final Class returnType, final FacetHolder collection) {
        Method method = findMethod(cls, OBJECT, VALIDATE_REMOVE_FROM_PREFIX + capitalizedName, String.class, paramTypesOrNull(collectionType));
        if (method == null) {
            method = findMethod(cls, OBJECT, VALIDATE_REMOVE_FROM_PREFIX_2 + capitalizedName, String.class, paramTypesOrNull(collectionType));
        }
        if (method == null) {
            return;
        }
        removeMethod(methodRemover, method);
        collectionFacets.add(new CollectionValidateRemoveFromFacetViaMethod(method, collection));
    }

    private void findAndRemoveChoicesMethod(final List collectionFacets, final MethodRemover methodRemover, final Class cls, final String capitalizedName, final Class returnType, final JavaOneToManyAssociationPeer collection) {
        final Method method = findMethod(cls, OBJECT, CHOICES_PREFIX + capitalizedName, Object[].class, NO_PARAMETERS_TYPES);
        removeMethod(methodRemover, method);
        if (method == null) {
            return;
        }
        collectionFacets.add(new PropertyChoicesFacetViaMethod(method, returnType, collection));
    }

    public boolean isPropertyOrCollectionAccessorCandidate(final Method method) {
        return method.getName().startsWith(GET_PREFIX);
    }

    public boolean isCollectionAccessor(final Method method) {
        if (!isPropertyOrCollectionAccessorCandidate(method)) {
            return false;
        }
        final Class<?> methodReturnType = method.getReturnType();
        return isCollectionOrArray(methodReturnType);
    }

    /**
     * The method way well represent a reference property, but this facet factory does not have any opinion on
     * the matter.
     */
    public boolean isPropertyAccessor(final Method method) {
        return false;
    }

    /**
     * The method way well represent a value property, but this facet factory does not have any opinion on the
     * matter.
     */
    public boolean isValuePropertyAccessor(final Method method) {
        return false;
    }

    public void findAndRemoveCollectionAccessors(final MethodRemover methodRemover, final List methodListToAppendTo) {
        final Class[] collectionClasses = getCollectionTypeRepository().getCollectionType();
        for (int i = 0; i < collectionClasses.length; i++) {
            final Class returnType = collectionClasses[i];
            final List list = methodRemover.removeMethods(MethodScope.OBJECT, GET_PREFIX, returnType, false, 0);
            methodListToAppendTo.addAll(list);
        }
    }

    public void findAndRemovePropertyAccessors(final MethodRemover methodRemover, final List methodListToAppendTo) {
    }
}
