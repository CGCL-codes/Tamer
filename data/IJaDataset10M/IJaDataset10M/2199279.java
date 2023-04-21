package net.rptools.inittool.model;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * A timer item in the encounter
 * 
 * @author jgorrell
 * @version $Revision$ $Date$ $Author$
 */
@XStreamAlias("timer")
@XStreamConverter(TimerInitItem.TimerInitItemConverter.class)
public class TimerInitItem extends InitItemImpl implements Cloneable {

    /**
   * The round that this timer will expire. A negative value indicates that the time will
   * never expire;
   */
    private int expirationRound;

    /**
   * The list of creatures that are attached to this timer.
   */
    private List<CreatureInitItem> creatures = new ArrayList<CreatureInitItem>();

    /**
   * The owner of this timer
   */
    private CreatureInitItem owner;

    /**
   * The type of this init item.
   */
    private String type;

    /**
   * Custom properties for use by scripts. 
   */
    private Map<String, String> properties = new HashMap<String, String>();

    /**
   * The name of the expiration round bound property. The value is an <code>Integer</code>.
   */
    public static final String EXPIRATION_ROUND_PROP_NAME = "expirationRound";

    /**
   * The name of the creatures bound property. The value is a <code>List<CreatureInitItem></code>.
   */
    public static final String CREATURES_PROP_NAME = "creatures";

    /**
   * The name of the owner bound property. The value is a <code>CreatureInitItem</code>.
   */
    public static final String OWNER_PROP_NAME = "owner";

    /**
   * Serialization id
   */
    public static final long serialVersionUID = -4762013030628715679L;

    /**
   * Create a new init item.
   * 
   * @param aName The name of this init item.
   * @param aType The type of this init item.
   */
    public TimerInitItem(String aName, TimerType aType) {
        super(aName, aType);
        type = aType.getName();
    }

    /**
   * Get the expirationRound for this TimerItem.
   *
   * @return Returns the current value of expirationRound.
   */
    public int getExpirationRound() {
        return expirationRound;
    }

    /**
   * Set the value of expirationRound for this TimerItem.
   *
   * @param aExpirationRound The expirationRound to set.
   */
    public void setExpirationRound(int aExpirationRound) {
        if (expirationRound == aExpirationRound) return;
        int old = expirationRound;
        expirationRound = aExpirationRound;
        pcs.firePropertyChange(EXPIRATION_ROUND_PROP_NAME, old, expirationRound);
    }

    /** @return Getter for creatures */
    public List<CreatureInitItem> getCreatures() {
        return creatures;
    }

    /** @param aCreatures Setter for creatures */
    public void setCreatures(List<CreatureInitItem> aCreatures) {
        if (creatures == aCreatures || (creatures != null && creatures.equals(aCreatures))) return;
        List<CreatureInitItem> old = creatures;
        creatures = aCreatures;
        if (creatures == null) creatures = new ArrayList<CreatureInitItem>();
        pcs.firePropertyChange(CREATURES_PROP_NAME, old, creatures);
    }

    /** @return Getter for owner */
    public CreatureInitItem getOwner() {
        return owner;
    }

    /** @param aOwner Setter for owner */
    public void setOwner(CreatureInitItem aOwner) {
        if (owner == aOwner || (owner != null && owner.equals(aOwner))) return;
        CreatureInitItem old = owner;
        owner = aOwner;
        pcs.firePropertyChange(OWNER_PROP_NAME, old, owner);
    }

    /**
   * Get a single property value 
   * 
   * @param name Name of the property
   * @return Value of the property
   */
    public String getProperty(String name) {
        return properties.get(name);
    }

    /**
   * Set a single property
   * 
   * @param name Name of the property
   * @param value New value of the property
   * @return Old value of the property
   */
    public String setProperty(String name, String value) {
        String old = properties.put(name, value);
        pcs.firePropertyChange(name, old, value);
        return old;
    }

    /**
   * @see java.lang.Object#clone()
   */
    @Override
    public Object clone() {
        try {
            TimerInitItem clone = (TimerInitItem) super.clone();
            clone.creatures = new ArrayList<CreatureInitItem>(creatures);
            clone.pcs = new PropertyChangeSupport(clone);
            return clone;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new IllegalStateException("Can't clone?", e);
        }
    }

    /**
   * @see net.rptools.inittool.model.InitItem#getType()
   */
    public ItemType getType() {
        ItemType value = type != null ? InitToolGameSettings.getInstance().getTimerType(type) : null;
        if (value == null) {
            type = TimerType.DEFAULT_TYPE.getName();
            value = TimerType.DEFAULT_TYPE;
        }
        return value;
    }

    /**
   * Set the value of type for this InitItem.
   *
   * @param aType The type to set.
   */
    public void setType(ItemType aType) {
        if (aType == null) throw new IllegalArgumentException("A type is required but null was passed.");
        if (!(aType instanceof TimerType)) throw new IllegalArgumentException("The passed type must be a timer type but a " + aType.getClass().getName() + " was passed");
        if (aType.getName().equals(aType)) return;
        ItemType old = type != null ? InitToolGameSettings.getInstance().getTimerType(type) : null;
        if (old == null) old = TimerType.DEFAULT_TYPE;
        type = aType.getName();
        pcs.firePropertyChange(TYPE_PROP_NAME, old, aType);
    }

    /**
   * @see net.rptools.inittool.model.InitItem#receiveInitiative(net.rptools.inittool.model.Encounter)
   */
    @Override
    public boolean receiveInitiative(Encounter aEncounter) {
        aEncounter.timerChange(this);
        boolean typeStop = getType().receiveInitiative(aEncounter, this);
        return typeStop && !isAutoSkip() && !isHidden();
    }

    /**
   * @see net.rptools.inittool.model.InitItem#loseInitiative(net.rptools.inittool.model.Encounter)
   */
    @Override
    public void loseInitiative(Encounter encounter) {
        getType().loseInitiative(encounter, this);
    }

    /**
   * The converter for the timer init item. Saves all of the data. The type is accessed by name
   * since it is saved elsewhere.
   * 
   * TODO: Icon Support is needed.
   * @author jgorrell
   * @version $Revision$ $Date$ $Author$
   */
    public static class TimerInitItemConverter extends InitItemConverter implements Converter {

        /**
     * @see com.thoughtworks.xstream.converters.ConverterMatcher#canConvert(java.lang.Class)
     */
        public boolean canConvert(Class aType) {
            return TimerInitItem.class == aType;
        }

        /**
     * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object, com.thoughtworks.xstream.io.HierarchicalStreamWriter, com.thoughtworks.xstream.converters.MarshallingContext)
     */
        public void marshal(Object aSource, HierarchicalStreamWriter aWriter, MarshallingContext aContext) {
            marshalInitItem(aSource, aWriter, aContext);
            TimerInitItem timer = (TimerInitItem) aSource;
            writeIntNode(aWriter, "expiration-round", timer.getExpirationRound());
            writeNode(aWriter, "type", timer.getType().getName());
            writeNode(aWriter, aContext, "owner", timer.getOwner());
            writeNode(aWriter, aContext, "combatants", timer.getCreatures());
            for (String key : timer.properties.keySet()) writeNode(aWriter, key, timer.properties.get(key));
        }

        /**
     * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader, com.thoughtworks.xstream.converters.UnmarshallingContext)
     */
        public Object unmarshal(HierarchicalStreamReader aReader, UnmarshallingContext aContext) {
            TimerInitItem timer = aContext.currentObject() == null ? new TimerInitItem("name", TimerType.DEFAULT_TYPE) : (TimerInitItem) aContext.currentObject();
            aContext.put(InitItem.class.getName(), timer);
            unmarshalInitItem(aReader, aContext);
            timer.setExpirationRound(readIntNode(aReader, "expiration-round"));
            TimerType type = InitToolGameSettings.getInstance().getTimerType(readNode(aReader, "type"));
            if (type == null) type = TimerType.DEFAULT_TYPE;
            timer.setType(type);
            timer.setOwner((CreatureInitItem) readNode(aReader, aContext, CreatureInitItem.class, "owner"));
            timer.setCreatures((List<CreatureInitItem>) readNode(aReader, aContext, ArrayList.class, "combatants"));
            timer.properties.clear();
            while (aReader.hasMoreChildren()) {
                aReader.moveDown();
                timer.properties.put(aReader.getNodeName(), aReader.getValue());
                aReader.moveUp();
            }
            return timer;
        }
    }
}
