package org.openscience.cdk.debug;

import java.util.Hashtable;
import org.openscience.cdk.interfaces.IChemObject;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IChemObjectChangeEvent;
import org.openscience.cdk.interfaces.IChemObjectListener;
import org.openscience.cdk.interfaces.IReaction;
import org.openscience.cdk.interfaces.ISetOfReactions;
import org.openscience.cdk.tools.LoggingTool;

/**
 * Debugging data class.
 * 
 * @author     egonw
 * @cdk.module data-debug
 */
public class DebugSetOfReactions extends org.openscience.cdk.SetOfReactions implements ISetOfReactions {

    LoggingTool logger = new LoggingTool(DebugSetOfReactions.class);

    public void addListener(IChemObjectListener col) {
        logger.debug("Adding listener: ", col);
        super.addListener(col);
    }

    public int getListenerCount() {
        logger.debug("Getting listener count: ", super.getListenerCount());
        return super.getListenerCount();
    }

    public void removeListener(IChemObjectListener col) {
        logger.debug("Removing listener: ", col);
        super.removeListener(col);
    }

    public void notifyChanged() {
        logger.debug("Notifying changed");
        super.notifyChanged();
    }

    public void notifyChanged(IChemObjectChangeEvent evt) {
        logger.debug("Notifying changed event: ", evt);
        super.notifyChanged(evt);
    }

    public void setProperty(Object description, Object property) {
        logger.debug("Setting property: ", description + "=" + property);
        super.setProperty(description, property);
    }

    public void removeProperty(Object description) {
        logger.debug("Removing property: ", description);
        super.removeProperty(description);
    }

    public Object getProperty(Object description) {
        logger.debug("Getting property: ", description + "=" + super.getProperty(description));
        return super.getProperty(description);
    }

    public Hashtable getProperties() {
        logger.debug("Getting properties");
        return super.getProperties();
    }

    public String getID() {
        logger.debug("Getting ID: ", super.getID());
        return super.getID();
    }

    public void setID(String identifier) {
        logger.debug("Setting ID: ", identifier);
        super.setID(identifier);
    }

    public void setFlag(int flag_type, boolean flag_value) {
        logger.debug("Setting flag: ", flag_type + "=" + flag_value);
        super.setFlag(flag_type, flag_value);
    }

    public boolean getFlag(int flag_type) {
        logger.debug("Setting flag: ", flag_type + "=" + super.getFlag(flag_type));
        return super.getFlag(flag_type);
    }

    public void setProperties(Hashtable properties) {
        logger.debug("Setting properties: ", properties);
        super.setProperties(properties);
    }

    public void setFlags(boolean[] flagsNew) {
        logger.debug("Setting flags:", flagsNew.length);
        super.setFlags(flagsNew);
    }

    public boolean[] getFlags() {
        logger.debug("Getting flags:", super.getFlags().length);
        return super.getFlags();
    }

    public Object clone() {
        Object clone = null;
        try {
            clone = super.clone();
        } catch (Exception exception) {
            logger.error("Could not clone DebugAtom: " + exception.getMessage(), exception);
            logger.debug(exception);
        }
        return clone;
    }

    public IChemObjectBuilder getBuilder() {
        return DebugChemObjectBuilder.getInstance();
    }

    public void addReaction(IReaction reaction) {
        logger.debug("Adding reaction: ", reaction);
        super.addReaction(reaction);
    }

    public IReaction getReaction(int number) {
        logger.debug("Getting reaction at: ", number);
        return super.getReaction(number);
    }

    public IReaction[] getReactions() {
        logger.debug("Getting reactions: ", super.getReactions().length);
        return super.getReactions();
    }

    public int getReactionCount() {
        logger.debug("Getting reaction count: ", super.getReactionCount());
        return super.getReactionCount();
    }

    public void removeAllReactions() {
        logger.debug("Removing all reactions");
        super.removeAllReactions();
    }
}
