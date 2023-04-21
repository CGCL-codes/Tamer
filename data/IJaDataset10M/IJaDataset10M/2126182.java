package myriadempires.userData;

/**
 *
 * @author Richard
 */
public class StringTable {

    public static class StatusMessages {

        public static final String[] TurnPhase = { "Garrsion your recruits by dragging the command to a territory you own.", "You may cash in your cards for additional armies.", "Garrsion your recruits by dragging the command to a territory. Right-click to break down a command.", "Select a territory from which you wish to attack. Then select the territory to engage.", "Decide where you wish to deploy the armies from the attacking territory.", "Select a territory from which to fortify. Then drag commands to the relevant territories.", "Turn ended." };

        public static final String CANNOT_ATTACK_FROM_FOREIGN = "You must select a country you own to attack from.";

        public static final String CANNOT_ATTACK_ZERO_GARRISON = "You cannot attack from a territory with no mobile armies.";

        public static final String CANNOT_ATTACK_REMOTE = "You cannot attack a territory which does not border the territory from which you are attacking";

        public static final String CANNOT_FORTIFY_FOREIGN = "You cannot fortify to / from a territory you do not own";

        public static final String CANNOT_FORTIFY_ZERO_GARRISON = "You cannot fortify from a territory with no mobile armies";

        public static final String CANNOT_FORTIFY_REMOTE = "You cannot fortify across more than one border.";

        public static final String CANNOT_CASH = "Please select three cards of the same type or one of each type.";
    }
}
