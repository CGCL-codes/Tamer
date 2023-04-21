package net.sf.jtonic.core;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Timer;
import javax.swing.UnsupportedLookAndFeelException;
import net.sf.jtonic.gui.GUI;

public class Master {

    private Properties prop;

    private GUI gui;

    private PriceCalculator pricecalc;

    private Timer timer;

    private Drink[] drinks = null;

    private boolean started = false;

    public static void main(String args[]) {
        new Master();
    }

    public Master() {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        prop = new Properties();
        gui = new GUI(this);
    }

    public Drink[] generateDrinks() {
        int aod = prop.getAmountOfDrinks();
        drinks = new Drink[aod];
        for (int i = 0; i < aod; i++) {
            drinks[i] = new Drink(prop.getInitPrice(i), prop.getMinPrice(i), prop.getMaxPrice(i), prop.getName(i));
        }
        return drinks;
    }

    public Drink[] getDrinks() {
        return drinks;
    }

    AbstractAction a = new AbstractAction() {

        /**
		 * 
		 */
        private static final long serialVersionUID = 1L;

        public void actionPerformed(ActionEvent e) {
            pricecalc.calculate();
            for (int i = 0; i < drinks.length; i++) {
                drinks[i].tick();
            }
            gui.updateGUI();
        }
    };

    public void startSimulation() {
        started = true;
        gui.start();
        generateDrinks();
        pricecalc = new PriceCalculator(getDrinks(), prop);
        timer = new Timer(prop.getTicktime(), a);
        timer.start();
    }

    public Properties getProperties() {
        return prop;
    }
}
