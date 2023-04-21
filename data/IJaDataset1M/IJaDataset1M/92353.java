package org.eclipse.ui.internal.menus;

/**
 * <p>
 * The path describing a location. A path can be composed of different types of
 * elements, but is characterized by being a container. It is generally possible
 * to create a child path based on a location element.
 * </p>
 * <p>
 * Clients must not implement or extend.
 * </p>
 * <p>
 * <strong>PROVISIONAL</strong>. This class or interface has been added as
 * part of a work in progress. There is a guarantee neither that this API will
 * work nor that it will remain the same. Please do not use this API without
 * consulting with the Platform/UI team.
 * </p>
 * <p>
 * This class will eventually exist in <code>org.eclipse.jface.menus</code>.
 * </p>
 * 
 * @since 3.2
 * @see org.eclipse.ui.internal.menus.SBar
 * @see org.eclipse.ui.internal.menus.SPart
 * @see org.eclipse.ui.internal.menus.SPopup
 */
public interface LocationElement {

    /**
	 * Creates a child of this path. This child will have the given id appended.
	 * 
	 * @param id
	 *            The id of the element to append to this path; must not be
	 *            <code>null</code>.
	 * @return The child location element; never <code>null</code>.
	 */
    LocationElement createChild(String id);
}
