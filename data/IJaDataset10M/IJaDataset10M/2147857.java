package trstudio.blueboxalife.bos;

import java.awt.Graphics2D;
import java.io.Serializable;
import trstudio.blueboxalife.boa.BOA;
import trstudio.blueboxalife.io.InputManager;
import trstudio.blueboxalife.states.GameProcess;
import trstudio.classlibrary.drivers.Resource;

/**
 * BlueBox Object Script Virtual Machine.
 * 
 * @author Sebastien Villemain
 */
public class BOSVirtualMachine implements GameProcess, Serializable {

    public final BOSVirtualMachineConfiguration configuration;

    public BOSVirtualMachine(BOA agent) {
        configuration = new BOSVirtualMachineConfiguration();
    }

    public void setVariables(BOSVariable variable1, BOSVariable variable2) {
    }

    public boolean fireScript(BOS script, BOA from, boolean noInterruption) {
        return false;
    }

    public void resetScriptState() {
    }

    /**
	 * Vérifie si la VM est dans un état bloqué.
	 * 
	 * @return
	 */
    public boolean isBlocking() {
        return false;
    }

    /**
	 * Vérifie si la VM est arrêtée.
	 * 
	 * @return
	 */
    public boolean isStopped() {
        return configuration.getCurrentScript() == null;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        return builder.toString();
    }

    public void loadResources(Resource resourceManager) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void start(InputManager inputManager) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void stop() {
        configuration.setLock(false);
        configuration.setCurrentScript(null);
        configuration.getVariables().clear();
    }

    public void update(long elapsedTime) {
    }

    public void draw(Graphics2D g, int screenWidth, int screenHeight) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
