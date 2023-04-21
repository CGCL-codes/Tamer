package mud;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.Timer;

import mud.events.Event;
import mud.events.TurnEvent;
import mud.unit.Action;
import mud.unit.PassEvent;

/** Facilitates turn-based combat between two or more combatants */
public class Battle implements ActionListener {

    /** The maximum length of each player's turn, in milliseconds */
    private static final int TURN_LENGTH = 10000;
    
    /** the participants in the battle */
    private final Set<Combatable> combatants;

    /** timer used to schedule the end of the next turn */
    private Timer turnTimer;
    
    /** the system time, in milliseconds, at which the next turn begins */
    private long nextTurn;
    
    /**
     * Constructs a new Battle with the given combatants
     * @param combatants the initial combatants in the battle
     */
    public Battle(Combatable ... combatants) {
        this.combatants = new LinkedHashSet<>();
        for (Combatable combatant : combatants) {
            addCombatant(combatant);
        }
        
        turnTimer = new Timer(TURN_LENGTH, this);
        nextTurn = System.currentTimeMillis() + TURN_LENGTH;
    }
    
    /**
     * Adds the given combatant to the battle, merging battles if the combatant
     * is already engaged in a separate battle
     * @param combatant the new combatant that is entering the battle
     */
    public void addCombatant(Combatable combatant) {
        assert !combatants.contains(combatant);
        
        if (combatant.isInCombat()) {
            merge(combatant.getBattle());
        } else {
            this.combatants.add(combatant);
            combatant.setBattle(this);
        }
    }
    
    /**
     * @param combatant the existing combatant that is leaving the battle
     */
    public void removeCombatant(Combatable combatant) {
        assert combatants.contains(combatant);
        
        this.combatants.remove(combatant);
        combatant.setBattle(null);
    }
    
    /**
     * Executes as many turns as possible, then schedules the timer for the end 
     * of the current turn
     */
    public void takeTurns() {
        
        boolean newTurn = false;
        for (;;) {
            Combatable combatant = combatants.iterator().next();
            Action nextAction = combatant.getNextAction();
            if (nextAction != null) {
                nextAction.perform();
                
                // Move current combatant to the end of the queue
                combatants.remove(combatant);
                combatants.add(combatant);
                newTurn = true;
            } else if (newTurn) {
                nextTurn = System.currentTimeMillis() + TURN_LENGTH;
                turnTimer.restart();
                fireEvent(new TurnEvent(combatant));
                break;
            } else if (System.currentTimeMillis() > nextTurn) {

                // Move current combatant to the end of the queue
                combatants.remove(combatant);
                combatants.add(combatant);
                newTurn = true;
                
                fireEvent(new PassEvent(combatant));
            } else {
                break;
            }
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        takeTurns();
    }
    
    /**
     * Combines two separate battles together
     * @param that the battle with which to merge
     */
    private void merge(Battle that) {
        assert !this.equals(that);
        
        for (Combatable combatant : that.combatants) {
            combatant.setBattle(this);
        }
        this.combatants.addAll(that.combatants);
    }

    /** 
     * Notifies the combatants that a combat-based event has just occurred 
     * @param event the details of the occurrence
     */
    protected void fireEvent(Event event) {
        for (Combatable combatant : combatants) {
            if (combatant instanceof Observer) {
                ((Observer) combatant).observe(event);
            }
        }
    }
}
