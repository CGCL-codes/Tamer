package trstudio.blueboxalife.bos;

import java.util.ArrayList;
import trstudio.blueboxalife.boa.BOA;

/**
 * Configuration d'une machine virtuelle.
 * 
 * @author Sebastien Villemain
 */
public class BOSVirtualMachineConfiguration {

    /**
	 * Script courant.
	 */
    private BOS currentScript = null;

    /**
	 * L'agent qui retient actuellement l'attention d'une créature.
	 * (Variable _it_)
	 */
    private BOA subject = null;

    private BOA owner = null;

    /**
	 * Verrouillage de la machine.
	 */
    private boolean lock = false;

    /**
	 * Listes des variables.
	 */
    private ArrayList<BOSVariable> variables = null;

    private int timeSlice = 0;

    public BOA getSubject() {
        return subject;
    }

    public void setSubject(BOA subject) {
        this.subject = subject;
    }

    public int getTimeSlice() {
        return timeSlice;
    }

    public void setTimeSlice(int timeSlice) {
        this.timeSlice = timeSlice;
    }

    public BOA getOwner() {
        return owner;
    }

    public void setOwner(BOA owner) {
        this.owner = owner;
    }

    public BOS getCurrentScript() {
        return currentScript;
    }

    public void setCurrentScript(BOS currentScript) {
        this.currentScript = currentScript;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public ArrayList<BOSVariable> getVariables() {
        return variables;
    }
}
