package net.sourceforge.genericnuisance;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.prefs.Preferences;
import javax.swing.*;

/**
 * A Nuisance Options window.
 * 
 * @author Matt
 */
public class NuisanceOptions extends JFrame {

    /**
	 * The serial version UID.
	 */
    private static final long serialVersionUID = -3672432274727426943L;

    /**
	 * Creates a Nuisance Options window.
	 */
    public NuisanceOptions() {
        super("Nuisance Options");
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.gridy++;
        final ArrayList<JCheckBox> boxes = new ArrayList<JCheckBox>();
        for (Class<? extends NuisanceItem> theirClass : NuisanceFactory.potential) {
            JCheckBox check;
            String name = theirClass.getSimpleName();
            name = pruneClassName(name);
            try {
                String title = theirClass.newInstance().getTitle();
                if (!title.equals(name)) {
                    name = name + " - " + title;
                }
            } catch (IllegalArgumentException e1) {
                e1.printStackTrace();
            } catch (SecurityException e1) {
                e1.printStackTrace();
            } catch (InstantiationException e1) {
                e1.printStackTrace();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }
            System.out.println(name + " read in as " + NuisanceOptions.enabled(theirClass));
            check = new JCheckBox(name, NuisanceOptions.enabled(theirClass));
            check.setName(theirClass.getSimpleName());
            boxes.add(check);
            this.add(check, c);
            c.gridy++;
        }
        JButton ok = new JButton("OK");
        final NuisanceOptions me = this;
        ok.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Preferences prefs = Preferences.userNodeForPackage(NuisanceOptions.class);
                for (JCheckBox check : boxes) {
                    System.out.println(check.getName() + " should be stored as " + check.isSelected());
                    prefs.putBoolean(check.getName(), check.isSelected());
                    System.out.println("was stored as " + prefs.getBoolean(check.getName(), false));
                }
                me.dispose();
            }
        });
        this.add(ok, c);
    }

    /**
	 * Trims "Nuisance" off the end of the given class name, adds spaces in
	 * front of capital letters followed by lowercase letters, and trims leading
	 * spaces. Nuisance titles should be equal to the result of this string
	 * unless you have a good reason (like {@link CatalanPasswordNuisance}).
	 * 
	 * @param mame
	 *            the class name given
	 * @return the class name modified as above
	 */
    public static String pruneClassName(String mame) {
        String name = mame;
        if (name.endsWith("Nuisance")) {
            name = name.substring(0, name.length() - "Nuisance".length());
        }
        for (char ch = 'A'; ch < 'Z'; ch++) {
            for (char cha = 'a'; cha < 'z'; cha++) {
                name = name.replace("" + ch + cha, " " + ch + cha);
            }
        }
        if (name.startsWith(" ")) {
            name = name.substring(1);
        }
        return name;
    }

    /**
	 * Shows this NuisanceOptions window.
	 */
    public void shoe() {
        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    /**
	 * @param c
	 *            the name of the given class
	 * @return if the given class is enabled (true if not stored)
	 */
    public static boolean enabled(Class<? extends NuisanceItem> c) {
        Preferences prefs = Preferences.userNodeForPackage(NuisanceOptions.class);
        return prefs.getBoolean(c.getSimpleName(), false);
    }
}
