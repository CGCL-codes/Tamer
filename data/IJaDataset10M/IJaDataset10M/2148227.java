package saga.event;

/**
 * Combat listener interface.
 * @author  Klaus Rennecke
 * @version $Revision: 1.1 $
 */
public interface CombatListener extends java.util.EventListener {

    /** Signals a battle cry or other informative message from sender. */
    public void combatMessage(CombatMessageEvent ev);

    /** Signals that <var>caster</var> starts to cast a spell. */
    public void combatSpell(CombatSpellEvent ev);

    /** Signals that <var>villain</var> hit <var>victim</var> for
     * <var>amount</var> damage. */
    public void combatDamage(CombatDamageEvent ev);
}
