package fr.ign.cogit.geoxygene.appli;

import fr.ign.cogit.geoxygene.util.console.GeOxygeneConsole;

/**
 * Run the GeOxygene console.
 * <p>
 * Lance la magnifique console GeOxygene
 * 
 * @author Thierry Badard & Arnaud Braun
 * @version 1.0
 */
public final class Console {

    /**
   * Private default constructor to prevent objects of this class to be
   * instanciated: this is an utilitary class.
   */
    private Console() {
    }

    /**
   * Run the GeOxygene console.
   * @param args console arguments
   */
    public static void main(final String[] args) {
        new GeOxygeneConsole();
    }
}
