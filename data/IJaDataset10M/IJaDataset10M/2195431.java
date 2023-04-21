package tudresden.ocl20.pivot.modelinstancetype.types.base;

import java.util.Collection;
import java.util.LinkedList;
import tudresden.ocl20.pivot.essentialocl.expressions.CollectionKind;
import tudresden.ocl20.pivot.essentialocl.types.BagType;
import tudresden.ocl20.pivot.essentialocl.types.CollectionType;
import tudresden.ocl20.pivot.essentialocl.types.OrderedSetType;
import tudresden.ocl20.pivot.essentialocl.types.SequenceType;
import tudresden.ocl20.pivot.essentialocl.types.SetType;
import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceCollection;
import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceElement;
import tudresden.ocl20.pivot.pivotmodel.Type;

/**
 * <p>
 * Implements the interface {@link IModelInstanceCollection} abstractly for
 * <code>IModelInstance</code> {@link Collection}s.
 * </p>
 * 
 * @author Claas Wilke
 */
public abstract class AbstractModelInstanceCollection<T extends IModelInstanceElement> extends AbstractModelInstanceElement implements IModelInstanceCollection<T> {

    public String getName() {
        StringBuffer resultBuffer;
        resultBuffer = new StringBuffer();
        if (this.myName != null) {
            resultBuffer.append(this.myName);
        } else if (this.myId != null) {
            resultBuffer.append(this.myId);
        } else {
            if (this.isOrdered()) {
                if (this.isUnique()) {
                    resultBuffer.append("MIOrderedSet");
                } else {
                    resultBuffer.append("MISet");
                }
            } else {
                if (this.isUnique()) {
                    resultBuffer.append("MISequence");
                } else {
                    resultBuffer.append("MIBag");
                }
            }
            resultBuffer.append("[");
            boolean firstElement;
            firstElement = true;
            for (T element : this.getCollection()) {
                if (firstElement) {
                    firstElement = false;
                } else {
                    resultBuffer.append(", ");
                }
                if (element instanceof IModelInstanceElement) {
                    resultBuffer.append(((IModelInstanceElement) element).getName());
                } else {
                    resultBuffer.append(element.toString());
                }
            }
            resultBuffer.append("]");
        }
        return resultBuffer.toString();
    }

    @Override
    public boolean equals(Object object) {
        boolean result;
        if (object == null) {
            result = false;
        } else if (this == object) {
            result = true;
        }
        if (object instanceof AbstractModelInstanceCollection<?>) {
            AbstractModelInstanceCollection<?> other;
            other = (AbstractModelInstanceCollection<?>) object;
            if (this.isUndefined() || other.isUndefined()) {
                result = false;
            } else {
                result = true;
                result &= this.isOrdered() == other.isOrdered();
                result &= this.isUnique() == other.isUnique();
                result &= this.getType().equals(other.getType());
                if (this.getType() instanceof BagType) {
                    if (this.getCollection().size() == other.getCollection().size()) {
                        LinkedList<Object> copy = new LinkedList<Object>(this.getCollection());
                        for (Object element : other.getCollection()) {
                            if (copy.contains(element)) {
                                copy.remove(element);
                            } else {
                                result = false;
                            }
                        }
                        result &= copy.isEmpty();
                    } else {
                        result = false;
                    }
                } else if (this.getType() instanceof OrderedSetType) {
                    result &= this.getCollection().equals(other.getCollection());
                } else if (this.getType() instanceof SequenceType) {
                    result &= this.getCollection().equals(other.getCollection());
                } else if (this.getType() instanceof SetType) {
                    result &= this.getCollection().containsAll((other.getCollection()));
                } else {
                    result &= this.getCollection().equals(other.getCollection());
                }
            }
        } else {
            result = false;
        }
        return result;
    }

    @Override
    public int hashCode() {
        int result;
        int prime = 31;
        result = 0;
        if (this.isOrdered()) {
            result += 1231;
        } else {
            result += 1237;
        }
        if (this.isUnique()) {
            result = prime * result + 1231;
        } else {
            result = prime * result + 1237;
        }
        if (this.getCollection() == null) {
            result = prime * result;
        } else {
            result = prime * result + this.getCollection().hashCode();
        }
        if (this.myType == null) {
            result = prime * result;
        } else {
            result = prime * result + this.myType.hashCode();
        }
        return result;
    }

    public boolean isOrdered() {
        boolean result;
        CollectionType collectionType = (CollectionType) this.myType;
        if (collectionType.getKind() == CollectionKind.SEQUENCE || collectionType.getKind() == CollectionKind.ORDERED_SET) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    public boolean isUndefined() {
        return (this.getCollection() == null);
    }

    public boolean isKindOf(Type type) {
        return this.getType().conformsTo(type);
    }

    public boolean isUnique() {
        boolean result;
        CollectionType collectionType = (CollectionType) this.myType;
        if (collectionType.getKind() == CollectionKind.ORDERED_SET || collectionType.getKind() == CollectionKind.SET) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }
}
