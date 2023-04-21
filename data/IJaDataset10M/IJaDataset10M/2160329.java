package progranet.omg.ocl.types.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import progranet.omg.core.types.MultiplicityElement;
import progranet.omg.core.types.Property;
import progranet.omg.core.types.Type;
import progranet.omg.core.types.impl.IntegerTypeImpl;
import progranet.omg.core.types.impl.RealTypeImpl;
import progranet.omg.core.types.impl.TypeImpl;
import progranet.omg.customlibrary.impl.Tuple;
import progranet.omg.ocl.Model;
import progranet.omg.ocl.customlibrary.Iterator;
import progranet.omg.ocl.customlibrary.IteratorOperation;
import progranet.omg.ocl.customlibrary.impl.IteratorOperationImpl;
import progranet.omg.ocl.customlibrary.impl.OperationImpl;
import progranet.omg.ocl.customlibrary.opers.CollectionCollectImpl;
import progranet.omg.ocl.types.CollectionType;

public class CollectionTypeImpl extends TypeImpl implements CollectionType, Serializable {

    private static final long serialVersionUID = -1406130319793325655L;

    private Type elementType;

    private Map<String, IteratorOperation> ownedIterator = new HashMap<String, IteratorOperation>();

    private static Map<Type, CollectionType> COLLECTION = new HashMap<Type, CollectionType>();

    public CollectionTypeImpl() {
    }

    private CollectionTypeImpl(Type elementType) {
        super(null, "Collection(" + elementType.getQualifiedName() + ")");
        this.elementType = elementType;
        this.general.add(Model.OCL_TYPE);
        COLLECTION.put(elementType, this);
        new OperationImpl(this, "size", Model.INTEGER) {

            private static final long serialVersionUID = 2611434650547185864L;

            public Object invoke(Object object, Object[] params) {
                return ((Collection) object).size();
            }
        };
        new OperationImpl(this, "includes", Model.BOOLEAN) {

            private static final long serialVersionUID = -5223778426204898146L;

            public Object invoke(Object object, Object[] params) {
                return ((Collection) object).contains(params[0]);
            }
        }.addParameter(this.getElementType());
        new OperationImpl(this, "excludes", Model.BOOLEAN) {

            private static final long serialVersionUID = 4180329303014923232L;

            public Object invoke(Object object, Object[] params) {
                return !((Collection) object).contains(params[0]);
            }
        }.addParameter(this.getElementType());
        new OperationImpl(this, "includesAll", Model.BOOLEAN) {

            private static final long serialVersionUID = -5221375291884950317L;

            public Object invoke(Object object, Object[] params) {
                return ((Collection) object).containsAll((Collection) params[0]);
            }
        }.addParameter(this);
        new OperationImpl(this, "excludesAll", Model.BOOLEAN) {

            private static final long serialVersionUID = -1180647899594543594L;

            public Object invoke(Object object, Object[] params) {
                return !((Collection) object).containsAll((Collection) params[0]);
            }
        }.addParameter(this);
        new OperationImpl(this, "isEmpty", Model.BOOLEAN) {

            private static final long serialVersionUID = -2619679982529639375L;

            public Object invoke(Object object, Object[] params) {
                return ((Collection) object).isEmpty();
            }
        };
        new OperationImpl(this, "notEmpty", Model.BOOLEAN) {

            private static final long serialVersionUID = -8615581074435326026L;

            public Object invoke(Object object, Object[] params) {
                return !((Collection) object).isEmpty();
            }
        };
        new OperationImpl(this, "sum", this.elementType) {

            private static final long serialVersionUID = -3135492516994315565L;

            public Object invoke(Object object, Object[] params) {
                OperationImpl add;
                Type elementType = ((CollectionType) this.getOwner()).getElementType();
                Object n;
                if (elementType.equals(Model.INTEGER)) {
                    add = (OperationImpl) IntegerTypeImpl.ADD;
                    n = 0;
                } else if (elementType.equals(Model.REAL)) {
                    add = (OperationImpl) RealTypeImpl.ADD;
                    n = new BigDecimal(0);
                } else return new NoSuchMethodException("No such operation: " + this.getOwner() + ".sum()");
                for (Object o : (Collection) object) n = add.invoke(n, new Object[] { o });
                return n;
            }
        };
        new IteratorOperationImpl(this, "collect") {

            public Type getOclType(Type bodyExpressionType) {
                return SequenceTypeImpl.getSequenceTypeInstance(bodyExpressionType);
            }

            public Iterator iterator(Collection collection) {
                return new CollectionCollectImpl(collection);
            }
        };
    }

    protected CollectionTypeImpl(String name, Type elementType) {
        super(null, name);
        this.elementType = elementType;
        CollectionType superType = COLLECTION.get(elementType);
        if (superType == null) superType = new CollectionTypeImpl(elementType);
        this.general.add(superType);
        this.ownedIterator.putAll(((CollectionTypeImpl) superType).ownedIterator);
    }

    public void addIteratorOperation(IteratorOperation operation) {
        this.ownedIterator.put(operation.getName(), operation);
    }

    public IteratorOperation getIteratorOperation(String name) {
        return this.ownedIterator.get(name);
    }

    public Type getClassifier() {
        return Model.COLLECTION_TYPE;
    }

    public Property getProperty(String name) throws NoSuchFieldException {
        return this.elementType.getProperty(name);
    }

    public Type getElementType() {
        return this.elementType;
    }

    public Set<Tuple> getData(java.lang.Object object, MultiplicityElement multiplicity) {
        return null;
    }

    public java.lang.Object getObject(Set<Tuple> data, MultiplicityElement multiplicity) {
        return null;
    }

    protected Type getDeepType(Type type) {
        if (type instanceof CollectionType) return this.getDeepType(((CollectionType) type).getElementType());
        return type;
    }

    public Object get(Object instance, Property property) {
        List<Object> acc = new ArrayList<Object>();
        for (Object o : (Collection) instance) acc.add(this.elementType.get(o, property));
        return acc;
    }
}
