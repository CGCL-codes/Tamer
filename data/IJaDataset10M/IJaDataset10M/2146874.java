package org.sourceforge.espro.gui;

import org.sourceforge.espro.elicitation.ElicitationMethod;
import org.sourceforge.espro.model.Item;
import org.sourceforge.espro.model.MethodProvider;
import org.sourceforge.espro.model.Questionnaire;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

/**
 * The MethodSwitcher supports a menu that allows the method to be switched
 * for a given item. This is primary used in the {@link MethodSettingsEditor}.
 * Normally you will not need this.
 *
 * @author (c) 2007 Martin Kaffanke
 * @version 2.0
 */
public class MethodSwitcher {

    private static MethodSwitcher instance = null;

    private static final Logger logger = Logger.getLogger(MethodSwitcher.class.getName());

    /** DOCUMENT ME! */
    PropertyChangeListener frozenListener = null;

    private final MethodProvider methodProvider = MethodProvider.getInstance();

    private final ArrayList<String> methods = methodProvider.getMethods();

    private final QuestionnaireManager questionnaireManager = QuestionnaireManager.getInstance();

    private MethodSwitcher() {
        super();
    }

    /**
    * The MethodSwitcher runs as a singleton class, so this is how you
    * get the instance.
    *
    * @return An instance of the MethodSwitcher.
    */
    public static MethodSwitcher getInstance() {
        if (instance == null) {
            instance = new MethodSwitcher();
        }
        return instance;
    }

    /**
    * Returns a JMenu which holds all the methods to choose.
    *
    * @return The menu.
    */
    public JMenu getMenu() {
        final JMenu menu = new JMenu();
        questionnaireManager.addPropertyChangeListener("actualQuestionnaire", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                Questionnaire n = (Questionnaire) evt.getNewValue();
                if (n.ItemCount() < 1) {
                    menu.setEnabled(false);
                }
                n.addPropertyChangeListener("current", new PropertyChangeListener() {

                    public void propertyChange(PropertyChangeEvent evt) {
                        if (evt.getNewValue() != null) {
                            menu.setEnabled(true);
                        } else {
                            menu.setEnabled(false);
                        }
                    }
                });
            }
        });
        menu.setEnabled(false);
        menu.setText("Method");
        menu.setName("Method");
        Collections.sort(methods);
        for (int i = 0; i < methods.size(); i++) {
            try {
                final ElicitationMethod m = (ElicitationMethod) Class.forName(MethodProvider.getMethodPrefix() + methods.get(i)).newInstance();
                final String categories[] = m.getCategory().split(">");
                JMenu parent = menu;
                for (final String element : categories) {
                    final Component items[] = parent.getMenuComponents();
                    boolean parentFound = false;
                    for (final Component element0 : items) {
                        if (element0.getName().equals(element)) {
                            parent = (JMenu) element0;
                            parentFound = true;
                        }
                    }
                    if (parentFound == false) {
                        final JMenu newParent = new JMenu();
                        newParent.setText(element);
                        newParent.setName(element);
                        parent.add(newParent);
                        parent = newParent;
                    }
                }
                final ChangeMethodAction action = new ChangeMethodAction(m);
                parent.add(action);
            } catch (final ClassNotFoundException e) {
                logger.log(Level.INFO, "Class not found: " + methods.get(i));
                e.printStackTrace();
                continue;
            } catch (final InstantiationException e) {
                e.printStackTrace();
            } catch (final IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        sortMenu(menu);
        return menu;
    }

    /**
    * Returns a MenuBar with only one menu, the method menu.
    *
    * @return The MenuBar
    */
    public JMenuBar getMenuBar() {
        final JMenuBar bar = new JMenuBar();
        bar.add(getMenu());
        final Questionnaire q = questionnaireManager.getActualQuestionnaire();
        bar.setEnabled(!q.isFrozen());
        if (frozenListener == null) {
            frozenListener = new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {
                    bar.setEnabled(!(Boolean) evt.getNewValue());
                }
            };
        }
        q.addPropertyChangeListener("actualQuestionnaire", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                Questionnaire q = (Questionnaire) evt.getOldValue();
                q.removePropertyChangeListener(frozenListener);
                q = (Questionnaire) evt.getNewValue();
                q.addPropertyChangeListener("frozen", frozenListener);
                bar.setEnabled(!q.isFrozen());
            }
        });
        return bar;
    }

    private Item getCurrentItem() {
        final Questionnaire q = questionnaireManager.getActualQuestionnaire();
        final Item i = q.getCurrent();
        return i;
    }

    private void sortMenu(JMenu menu) {
    }

    /**
    * The ChangeMethodAction is an action which can be used to change
    * the elicitation method for the current item.
    *
    * @author (c) 2007 Martin Kaffanke
    */
    protected class ChangeMethodAction extends AbstractAction {

        /**  */
        private static final long serialVersionUID = 2270885065683774738L;

        private ElicitationMethod method = null;

        /**
       * The Constructor needs to know which method to work on.
       *
       * @param method The elicitation method.
       */
        public ChangeMethodAction(final ElicitationMethod method) {
            super(method.getName());
            this.method = method;
        }

        /**
       * 
       * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
       */
        public void actionPerformed(final ActionEvent arg0) {
            final Item item = getCurrentItem();
            if (item == null) {
                logger.log(Level.INFO, "No Current Item to set in method swither.");
                return;
            }
            method = methodProvider.getMethod(method);
            item.setMethod(method);
        }
    }
}
