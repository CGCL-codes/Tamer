package oracle.toplink.essentials.internal.indirection;

import oracle.toplink.essentials.indirection.*;

/**
 * Used as the backup value holder in the unit of work for transparent indirection.
 * This ensure that a reference to the original value holder is held in case the
 * transparent collection or proxy is replace without first instantiating the original.
 *
 * @since 10.1.3
 * @author James Sutherland
 */
public class BackupValueHolder extends ValueHolder {

    /** Stores the original uow clone's value holder. */
    protected ValueHolderInterface unitOfWorkValueHolder;

    public BackupValueHolder(ValueHolderInterface unitOfWorkValueHolder) {
        this.unitOfWorkValueHolder = unitOfWorkValueHolder;
    }

    /**
     * If the original value holder was not instantiated,
     * then first instantiate it to obtain the backup value.
     */
    public Object getValue() {
        getUnitOfWorkValueHolder().getValue();
        return value;
    }

    /**
     * Return the original uow clone's value holder.
     */
    public ValueHolderInterface getUnitOfWorkValueHolder() {
        return unitOfWorkValueHolder;
    }
}
