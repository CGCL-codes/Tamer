package ingenias2contractcodegenerator;

import java.util.Vector;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Ignasi Gomez-Sebastia
 * 
 * Representation of an Ingenias protocol (Interaction Model) keeping the information
 * meaningfull to generate contract code
 * 
 * Class is static, all parameters are set on constructor, and they don't change
 * Class provides getters for all parameters.
 */
public class Ingenias2ContractInteractionModel {

    protected static HashMap InteractionUnits;

    protected static Vector Iterators;

    protected static Vector Roles;

    private static String ProtocolName;

    private static String FirstInteraction;

    public Ingenias2ContractInteractionModel(HashMap my_InteractionUnits, Vector my_Iterators, Vector my_Roles, String my_ProtocolName) {
        InteractionUnits = my_InteractionUnits;
        Iterators = my_Iterators;
        Roles = my_Roles;
        ProtocolName = my_ProtocolName;
        this.FirstInteraction = "";
        Iterator it = this.InteractionUnits.values().iterator();
        while (it.hasNext()) {
            Ingenias2ContractInteractionUnit unit = (Ingenias2ContractInteractionUnit) it.next();
            System.out.println(unit.getName() + "|" + unit.getPreviousInteraction().size());
            if (unit.getPreviousInteraction().size() == 0) {
                this.FirstInteraction = unit.getName();
                break;
            }
        }
    }

    public HashMap getInteractionUnits() {
        return InteractionUnits;
    }

    public Vector getIterators() {
        return Iterators;
    }

    public Vector getRoles() {
        return Roles;
    }

    public static String getProtocolName() {
        return ProtocolName;
    }

    /**
        *
     * @author Ignasi Gomez-Sebastia
     * @return String name of the first interaction
         * 
         * Returns the interaction that starts the protocol
     * 
     */
    public static String getFirstInteraction() {
        return FirstInteraction;
    }

    /**
        *
     * @author Ignasi Gomez-Sebastia
     * @param String name of a role
     * @return Iterator Performatives of the interactions     
         * 
         * Returns the interactions where the given role is assigned as participant
     * 
     */
    public static Iterator getInteractionsOfRole(String role) {
        Vector res = new Vector();
        Iterator it = InteractionUnits.values().iterator();
        while (it.hasNext()) {
            Ingenias2ContractInteractionUnit unit = (Ingenias2ContractInteractionUnit) it.next();
            if (unit.getCollaborator().contains(role)) {
                if (!res.contains(unit.getSpeachAct())) {
                    res.add(unit.getSpeachAct());
                }
            }
        }
        return res.iterator();
    }

    /**
        *
     * @author Ignasi Gomez-Sebastia
     * @param String name of a role
     * @param String name of a performative
     * @return Iterator Names of the interactions     
         * 
         * Returns the interactions where the given role is assigned as participant
     *     that are of the specified performative type 
     * 
     */
    public static Iterator getInteractionsOfRolePerf(String role, String perf) {
        Vector res = new Vector();
        Iterator it = InteractionUnits.values().iterator();
        while (it.hasNext()) {
            Ingenias2ContractInteractionUnit unit = (Ingenias2ContractInteractionUnit) it.next();
            if (unit.getCollaborator().contains(role)) {
                if (unit.getSpeachAct().equalsIgnoreCase(perf)) {
                    res.add(unit.getName());
                }
            }
        }
        return res.iterator();
    }

    public static boolean isProtocolInitiator(String role) {
        Ingenias2ContractInteractionUnit unit = (Ingenias2ContractInteractionUnit) InteractionUnits.get(FirstInteraction);
        return unit.getInitiator().contains(role);
    }
}
