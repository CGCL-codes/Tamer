package se.kth.cid.xml;

/** This interface represents an external XML entity.
 *  Such an entity has three characteristics:
 *  public ID, system ID and possibly document type (for DTDs).
 *
 *  @author Mikael Nilsson
 *  @version $Revision: 1.1.1.2 $
 */
public interface ExternalEntity {

    /** Returns the public ID of this entity.
   *
   *  @return the public ID of this entity. May be null.
   */
    String getPublicID();

    /** Returns the system ID of this entity.
   *
   *  @return the system ID of this entity. May be null.
   */
    String getSystemID();

    /** Returns the document type of this entity.
   *
   *  This is only reasonable for external DTDs. Other entities will return null here.
   *
   *  @return the document type of this entity. May be null.
   */
    String getDocType();

    /** Returns the actual entity.
   *
   *  The object returned must be a Reader or an InputStream.
   *
   *  @return a Reader or an InputStream containing the entity.
   */
    Object getEntity();
}
