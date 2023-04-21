package wotlas.client;

import wotlas.common.objects.ObjectManager;
import wotlas.common.objects.inventories.Inventory;

/** 
 * The ClientObjectManager.<br>
 * Used to handle Objects and Inventory client side.
 * @see wotlas.common.objects.ObjectManager
 * @see wotlas.server.ServerObjectManager
 * @author Elann
 */
public class ClientObjectManager implements ObjectManager {

    /** The owned Inventory object.
     */
    protected Inventory inventory;

    /** The class of WotCharacter who owns me.<br> 
     * Set by the owner Player, depending on the implementor of WotCharacter it owns.
     */
    protected String characterClassName;

    /** Default constructor.
     *
     */
    public ClientObjectManager() {
        this.inventory = null;
        this.characterClassName = "Undefined";
    }

    /** Parametric constructor.<br>
     * Sets the character class name.
     * @param characterClassName the character class name.
     */
    public ClientObjectManager(String characterClassName) {
        this.inventory = null;
        this.characterClassName = characterClassName;
    }

    /** Parametric constructor.<br>
     * Sets the inventory member.
     * @param inventory the player's inventory.
     */
    public ClientObjectManager(Inventory inventory) {
        this.inventory = inventory;
        this.characterClassName = "Undefined";
    }

    /** Full parametric constructor.<br>
     * Sets the character class name and the inventory member.
     * @param characterClassName the character class name.
     * @param inventory the player's inventory.
     */
    public ClientObjectManager(String characterClassName, Inventory inventory) {
        this.inventory = inventory;
        this.characterClassName = characterClassName;
    }

    /** Get the Inventory object owned by the Manager.
    	  @return the Inventory
     */
    public Inventory getInventory() {
        return this.inventory;
    }

    /** Set the Inventory of the Manager.
    	  @param inventory the new Inventory
     */
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    /** Get the owning player's character's className
    	  @return characterClassName
     */
    public String getCharacterClassName() {
        return this.characterClassName;
    }

    /** Set the owning player's character's className
    	  @param characterClassName the new player's character className
     */
    public void setPlayerClassName(String characterClassName) {
        this.characterClassName = characterClassName;
    }
}
