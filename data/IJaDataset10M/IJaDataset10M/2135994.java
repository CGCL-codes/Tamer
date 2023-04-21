package wotlas.common.objects.containers;

import wotlas.common.objects.interfaces.*;
import wotlas.common.objects.valueds.ValuedObject;
import wotlas.common.Player;

/** 
 * The purse. Special Container that may only contain ValuedObjects.
 * 
 * @author Elann
 * @see wotlas.common.objects.containers.ContainerObject
 * @see wotlas.common.objects.valueds.ValuedObject
 * @see wotlas.common.objects.interfaces.TransportableInterface
 */
public class Purse extends ContainerObject implements TransportableInterface {

    private ValuedObject content;

    /** The only constructor. Calls ContainerObject's constructor.
  * @param capacity the number of objects that can be contained
  */
    public Purse(short capacity) {
        super(capacity);
        this.className = "Purse";
        this.objectName = "standard purse";
    }

    /** Add a valued object to the purse.
   * @param o the object to add
   */
    public void addObject(ValuedObject o) {
        super.addObject(o);
    }

    /** Remove a valued object from the purse.
   * @param o the object to remove. Can be found by getObjectByName() or getObjectAt()
   */
    public void removeObject(ValuedObject o) {
        super.removeObject(o);
    }

    /** Retrieve a valued object from the purse. This method does not check validity.
   * @param pos the position of the valued object in the container
   * @return the valued object required 
   */
    public ValuedObject getObjectAt(short pos) throws ArrayIndexOutOfBoundsException {
        return (ValuedObject) super.getObjectAt(pos);
    }

    /** Retrieve a valued object from the purse by name.
   * @param name the name of the object wanted
   * @return the object required 
   */
    public ValuedObject getObjectByName(String name) {
        return (ValuedObject) super.getObjectByName(name);
    }

    /** Gets rid of the object. The object is dropped on the ground.
   */
    public void discard() {
    }

    /** Sells the object to somebody.
  	  @param buyer The Player who buy the object. 
  	  @return the prize paid.
   */
    public ValuedObject sellTo(Player buyer) {
        return new ValuedObject();
    }

    /** Gives the object to somebody.
  	  @param receiver The Player who receive the object.
   */
    public void giveTo(Player receiver) {
    }
}
