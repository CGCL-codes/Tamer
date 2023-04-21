package net.sf.rcpforms.experimenting.permission.s;

/**
 * This permission type is intended to control the permission policy of a
 * <b><i>business object type</i></b>.
 * But normally
 * {@link ISimplePermission} or {@link IWidgetPermission} is the better permission typer for this.
 * 
 * C.R.U.D. ~= create, read, update, delete
 * 
 * @author feuzand
 * @author Chris Memory   (22.02.2011)
 */
public interface ICRUDPermission {

    /**
	 * @return the BO type identifier that is controlled by this permission policy
	 */
    public String getTypeID();

    /**
	 * @return if creating a new instance of this <i>business object type</i> is allowed
	 */
    public boolean isCreate();

    /**
	 * @return if reading data from a BO of this <i>business object type</i> is allowed
	 */
    public boolean isRead();

    /**
	 * 
	 * @return if altering data in a BO of this <i>business object type</i> is allowed
	 */
    public boolean isUpdate();

    /**
	 * 
	 * @return if deleting such a <i>business object type</i> is allowed
	 */
    public boolean isDelete();
}
