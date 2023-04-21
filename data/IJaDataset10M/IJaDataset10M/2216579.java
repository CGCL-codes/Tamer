package org.omg.uml.behavioralelements.collaborations;

/**
 * A_interactionInstanceSet_participatingStimulus association proxy interface.
 */
public interface AInteractionInstanceSetParticipatingStimulus extends javax.jmi.reflect.RefAssociation {

    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param interactionInstanceSet Value of the first association end.
     * @param participatingStimulus Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.collaborations.InteractionInstanceSet interactionInstanceSet, org.omg.uml.behavioralelements.commonbehavior.Stimulus participatingStimulus);

    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param interactionInstanceSet Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getInteractionInstanceSet(org.omg.uml.behavioralelements.commonbehavior.Stimulus participatingStimulus);

    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param participatingStimulus Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getParticipatingStimulus(org.omg.uml.behavioralelements.collaborations.InteractionInstanceSet interactionInstanceSet);

    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param interactionInstanceSet Value of the first association end.
     * @param participatingStimulus Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.collaborations.InteractionInstanceSet interactionInstanceSet, org.omg.uml.behavioralelements.commonbehavior.Stimulus participatingStimulus);

    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param interactionInstanceSet Value of the first association end.
     * @param participatingStimulus Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.collaborations.InteractionInstanceSet interactionInstanceSet, org.omg.uml.behavioralelements.commonbehavior.Stimulus participatingStimulus);
}
