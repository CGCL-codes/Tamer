package de.bugbusters.cdoptimizer;

import de.bugbusters.cdoptimizer.gui.Mainframe;

/**
 * The main class for the cd optimizer demo app. Usage
 * <ul>
 * <li>select some files</li>
 * <li>choose a cd/dvd capacity</li>
 * <li>select the algorithm to use</li>
 * <li>see packing result as a bar chart</li>
 * <li>select the cd to show it's desired content</li>
 * </ul>
 *
 * @author Sven Kiesewetter
 */
public class Main {

    public static void main(String[] args) throws Exception {
        new Mainframe().setVisible(true);
    }
}
