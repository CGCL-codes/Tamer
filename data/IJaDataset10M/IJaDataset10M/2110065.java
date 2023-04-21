package net.hypotenubel.jaicwain.options;

import java.awt.Color;
import java.util.*;
import org.apache.log4j.Logger;
import org.hibernate.*;
import net.hypotenubel.jaicwain.*;

/**
 * Provides the means necessary to mess with options. Upon creation,
 * the options manager starts up Hibernate and connects to the
 * database in the configuration directory.
 * <p>
 * The {@code OptionsManager} keeps a list of
 * {@code OptionsChangedEventListener}s which are notified at option
 * changes. To limit the number of generated events, the
 * {@link #beginUpdate(String set)} method can be used to indicate that some
 * update is about to occur on the options set with the specified name. After
 * having completed the update,
 * {@link #endUpdate(String set, boolean triggerEvent)} must be called.
 *
 * @author Christoph Daniel Schulze
 * @version $Id: OptionsManager.java 144 2006-10-03 00:20:14Z captainnuss $
 */
public class OptionsManager implements AppTerminationListener {

    /**
     * The Hibernate session used to do stuff.
     */
    private Session session;

    /**
     * Map mapping names to their {@link OptionsSet}s.
     */
    private HashMap<String, OptionsSet> sets = new HashMap<String, OptionsSet>(10);

    /**
     * {@code ArrayList} of {@code OptionsChangedListener}s.
     */
    private ArrayList<OptionsChangedListener> listeners = new ArrayList<OptionsChangedListener>(50);

    /**
     * Creates a new instance and connects to the database.
     */
    public OptionsManager() {
        Logger.getLogger(OptionsManager.class).info("Starting options subsystem...");
        session = App.factory.openSession();
        try {
            List options = session.createQuery("from OptionsSet").list();
            Iterator it = options.iterator();
            while (it.hasNext()) {
                OptionsSet set = (OptionsSet) it.next();
                this.sets.put(set.getName(), set);
            }
        } catch (HibernateException ex) {
            Logger.getLogger(OptionsManager.class).fatal("Unable to load options sets.", ex);
            throw ex;
        }
        Logger.getLogger(OptionsManager.class).debug("Found " + sets.size() + " options sets");
    }

    /**
     * Makes sure that an {@code OptionsSet} with the specified name
     * actually exists. If it doesn't, a new one is created. Nothing's loaded
     * from any file, though.
     *
     * @param set {@code String} containing the {@code OptionSet}'s
     *            name.
     */
    private void ensureSetExistence(String set) {
        if (set == null) {
            return;
        }
        if (sets.get(set.toLowerCase()) == null) {
            OptionsSet oset = new OptionsSet(set);
            persistSet(oset);
            sets.put(set.toLowerCase(), oset);
        }
    }

    /**
     * Saves or updates the given options set.
     * 
     * @param set the set to be saved or updated.
     */
    private void persistSet(OptionsSet set) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(set);
            tx.commit();
        } catch (HibernateException e) {
            Logger.getLogger(OptionsManager.class).error("Error saving OptionsSet " + set.getName() + ".", e);
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    /**
     * Returns the value mapped to the specified key in the specified set. If
     * the set doesn't exist, it's created.
     *
     * @param set {@code String} containing the {@code OptionSet}'s
     *            name.
     * @param key {@code String} containing the key.
     * @param deflt {@code String} containing default value which is
     *              returned when there's no value mapped to the specified key.
     * @return {@code String} containing either the option's value or
     *         {@code default} if non exists.
     */
    public String getStringOption(String set, String key, String deflt) {
        if (set == null) {
            return deflt;
        }
        if (key == null) {
            return deflt;
        }
        ensureSetExistence(set);
        OptionsSet options = sets.get(set);
        if (options.getOption(key) == null) {
            setStringOption(set, key, deflt);
            return deflt;
        } else {
            return options.getOption(key);
        }
    }

    /**
     * Returns the value mapped to the specified key in the specified set. If
     * the set doesn't exist, it's created.
     *
     * @param set {@code String} containing the {@code OptionSet}'s
     *            name.
     * @param key {@code String} containing the key.
     * @param deflt {@code boolean} containing default value which is
     *              returned when there's no value mapped to the specified key.
     * @return {@code boolean} containing either the option's value or
     *         {@code default} if non exists.
     */
    public boolean getBooleanOption(String set, String key, boolean deflt) {
        if (set == null) {
            return deflt;
        }
        if (key == null) {
            return deflt;
        }
        ensureSetExistence(set);
        OptionsSet options = sets.get(set);
        if (options.getOption(key) == null) {
            setBooleanOption(set, key, deflt);
            return deflt;
        } else {
            return Boolean.valueOf(options.getOption(key)).booleanValue();
        }
    }

    /**
     * Returns the value mapped to the specified key in the specified set. If
     * the set doesn't exist, it's created.
     *
     * @param set {@code String} containing the {@code OptionSet}'s
     *            name.
     * @param key {@code String} containing the key.
     * @param deflt {@code double} containing default value which is
     *              returned when there's no value mapped to the specified key.
     * @return {@code double} containing either the option's value or
     *         {@code default} if non exists.
     */
    public double getDoubleOption(String set, String key, double deflt) {
        if (set == null) {
            return deflt;
        }
        if (key == null) {
            return deflt;
        }
        ensureSetExistence(set);
        OptionsSet options = sets.get(set);
        if (options.getOption(key) == null) {
            setDoubleOption(set, key, deflt);
            return deflt;
        } else {
            try {
                return Double.parseDouble(options.getOption(key));
            } catch (NumberFormatException e) {
                return deflt;
            }
        }
    }

    /**
     * Returns the value mapped to the specified key in the specified set. If
     * the set doesn't exist, it's created.
     *
     * @param set {@code String} containing the {@code OptionSet}'s
     *            name.
     * @param key {@code String} containing the key.
     * @param deflt {@code int} containing default value which is
     *              returned when there's no value mapped to the specified key.
     * @return {@code int} containing either the option's value or
     *         {@code default} if non exists.
     */
    public int getIntOption(String set, String key, int deflt) {
        if (set == null) {
            return deflt;
        }
        if (key == null) {
            return deflt;
        }
        ensureSetExistence(set);
        OptionsSet options = sets.get(set);
        if (options.getOption(key) == null) {
            setIntOption(set, key, deflt);
            return deflt;
        } else {
            try {
                return Integer.parseInt(options.getOption(key));
            } catch (NumberFormatException e) {
                return deflt;
            }
        }
    }

    /**
     * Returns the value mapped to the specified key in the specified set. If
     * the set doesn't exist, it's created.
     *
     * @param set {@code String} containing the {@code OptionSet}'s
     *            name.
     * @param key {@code String} containing the key.
     * @param deflt {@code Color} containing default value which is
     *              returned when there's no value mapped to the specified key.
     * @return {@code Color} containing either the option's value or
     *         {@code default} if non exists.
     */
    public Color getColorOption(String set, String key, Color deflt) {
        if (getIntOption(set, key, -1) == -1) {
            setColorOption(set, key, deflt);
            return deflt;
        } else {
            return new Color(getIntOption(set, key, 0));
        }
    }

    /**
     * Resets the given options set.
     *
     * @param set {@code String} containing the {@code OptionSet}'s
     *            name.
     */
    public void resetSet(String set) {
        if (set == null) {
            return;
        }
        ensureSetExistence(set);
        sets.get(set).clear();
    }

    /**
     * Maps the specified to the given key in the specified set. If the set
     * doesn't exist, it's created. If any parameter is {@code null},
     * the method just returns.
     *
     * @param set {@code String} containing the {@code OptionSet}'s
     *            name.
     * @param key {@code String} containing the key.
     * @param value {@code String} containing the value.
     */
    public void setStringOption(String set, String key, String value) {
        if (set == null) {
            return;
        }
        if (key == null) {
            return;
        }
        if (value == null) {
            return;
        }
        ensureSetExistence(set);
        OptionsSet options = sets.get(set);
        options.setOption(key, value);
        persistSet(options);
        fireOptionsChangedEvent(set);
    }

    /**
     * Maps the specified to the given key in the specified set. If the set
     * doesn't exist, it's created. If any parameter is {@code null},
     * the method just returns.
     *
     * @param set {@code String} containing the {@code OptionSet}'s
     *            name.
     * @param key {@code String} containing the key.
     * @param value {@code boolean} containing the value.
     */
    public void setBooleanOption(String set, String key, boolean value) {
        if (set == null) {
            return;
        }
        if (key == null) {
            return;
        }
        ensureSetExistence(set);
        OptionsSet options = sets.get(set);
        options.setOption(key, new Boolean(value).toString());
        persistSet(options);
        fireOptionsChangedEvent(set);
    }

    /**
     * Maps the specified to the given key in the specified set. If the set
     * doesn't exist, it's created. If any parameter is {@code null},
     * the method just returns.
     *
     * @param set {@code String} containing the {@code OptionSet}'s
     *            name.
     * @param key {@code String} containing the key.
     * @param value {@code double} containing the value.
     */
    public void setDoubleOption(String set, String key, double value) {
        if (set == null) {
            return;
        }
        if (key == null) {
            return;
        }
        ensureSetExistence(set);
        OptionsSet options = sets.get(set);
        options.setOption(key, new Double(value).toString());
        persistSet(options);
    }

    /**
     * Maps the specified to the given key in the specified set. If the set
     * doesn't exist, it's created. If any parameter is {@code null},
     * the method just returns.
     *
     * @param set {@code String} containing the {@code OptionSet}'s
     *            name.
     * @param key {@code String} containing the key.
     * @param value {@code int} containing the value.
     */
    public void setIntOption(String set, String key, int value) {
        if (set == null) {
            return;
        }
        if (key == null) {
            return;
        }
        ensureSetExistence(set);
        OptionsSet options = sets.get(set);
        options.setOption(key, new Integer(value).toString());
        persistSet(options);
    }

    /**
     * Maps the specified to the given key in the specified set. If the set
     * doesn't exist, it's created. If any parameter is {@code null},
     * the method just returns.
     *
     * @param set {@code String} containing the {@code OptionSet}'s
     *            name.
     * @param key {@code String} containing the key.
     * @param value {@code Color} containing the value.
     */
    public void setColorOption(String set, String key, Color value) {
        if (value == null) {
            return;
        }
        setIntOption(set, key, value.getRGB());
    }

    /**
     * Adds an {@code OptionsChangedListener}.
     *
     * @param l {@code OptionsChangedListener} to be added.
     */
    public void addOptionsChangedListener(OptionsChangedListener l) {
        listeners.add(l);
    }

    /**
     * Removes an {@code OptionsChangedListener}.
     *
     * @param l {@code OptionsChangedListener} to be removed.
     */
    public void removeOptionsChangedListener(OptionsChangedListener l) {
        listeners.remove(l);
    }

    /**
     * Notifies all {@code OptionsChangedListener}s of a changed options
     * set if that set isn't being updated currently.
     *
     * @param set {@code String} containing the {@code OptionSet}'s
     *            name.
     */
    private void fireOptionsChangedEvent(final String set) {
        if (sets.get(set).isUpdating()) {
            return;
        }
        new Thread(new Runnable() {

            public void run() {
                Object[] list = listeners.toArray();
                for (int i = list.length - 1; i >= 0; i--) {
                    if (list[i] instanceof OptionsChangedListener) {
                        ((OptionsChangedListener) list[i]).optionsChanged(set);
                    }
                }
            }
        }).start();
    }

    /**
     * Flags the specified set as currently being updated, resulting in no
     * events to be fired on that set. Call this method before updating a set
     * to avert many events to be fired.
     *
     * @param set {@code String} containing the {@code OptionSet}'s
     *            name.
     * @see #endUpdate(String set, boolean fireEvent)
     */
    public void beginUpdate(String set) {
        if (set == null) {
            return;
        }
        ensureSetExistence(set);
        sets.get(set).setUpdating(true);
    }

    /**
     * Removes the flag set by {@link #beginUpdate(String set)} so that events
     * may be fired again. Optionally, an event can be fired immediately, which
     * is logical after updating a set.
     *
     * @param set {@code String} containing the {@code OptionSet}'s
     *            name.
     * @param fireEvent {@code boolean} value indicating whether an event
     *                  shall be fired or not.
     * @see #beginUpdate(String set)
     */
    public void endUpdate(String set, boolean fireEvent) {
        if (set == null) {
            return;
        }
        ensureSetExistence(set);
        sets.get(set).setUpdating(false);
        if (fireEvent) {
            fireOptionsChangedEvent(set);
        }
    }

    public String queryShutdown() {
        return null;
    }

    public void shutdown() {
        Logger.getLogger(OptionsManager.class).info("Shutting down options subsystem...");
        session.flush();
        session.close();
    }
}
