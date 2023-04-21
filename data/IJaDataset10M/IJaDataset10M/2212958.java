package wotlas.common.objects.interfaces;

import wotlas.common.objects.BaseObject;

/** 
 * The interface implemented by all things that may contain an object.
 * 
 * @author Elann
 */
public interface ContainerInterface {

    /** Empty the container on the ground.
   */
    public void empty();

    /** Add an object to the container.
   * @param o the object to add
   */
    public void addObject(BaseObject o);

    /** Remove an object from the container.
   * @param o the object to remove. Can be found by getObjectByName() or getObjectAt()
   */
    public void removeObject(BaseObject o);

    /** Retrieve an object from the container.
   * @param pos the position of the object in the container
   * @return the object required 
   */
    public BaseObject getObjectAt(short pos) throws ArrayIndexOutOfBoundsException;

    /** Retrieve an object from the container by name.
   * @param name the name of the object wanted
   * @return the object required 
   */
    public BaseObject getObjectByName(String name);
}
