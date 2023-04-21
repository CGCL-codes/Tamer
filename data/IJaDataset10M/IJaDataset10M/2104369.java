package net.sf.poormans.model;

/**
 * All {@link net.sf.poormans.model.domain.pojo database objects} have to implement this interface.
 *
 * @version $Id: IPoorMansObject.java 2017 2010-08-30 16:33:49Z th-schwarz $
 * @author <a href="mailto:th-schwarz@users.sourceforge.net">Thilo Schwarz</a>
 */
public interface IPoorMansObject {

    /**
	 * @return The id of the database object.
	 */
    public Long getId();

    /**
	 * Set the id of the database object. (It's needed by hibernate only.)
	 */
    public void setId(Long id);

    /**
	 * @return A title to view.
	 */
    public String getDecorationString();

    /**
	 * @see Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object obj);

    /**
	 * @see Object#hashCode()
	 */
    @Override
    public int hashCode();

    /**
	 * @see Object#toString()
	 */
    @Override
    public String toString();

    /**
	 * @return The parent object.
	 */
    public IPoorMansObject getDirectParent();

    /**
	 * Set the parent of this {@link IPoorMansObject}.
	 */
    public void setDirectParent(final IPoorMansObject po);
}
