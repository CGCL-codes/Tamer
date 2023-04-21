package org.intellij.idea.plugins.xplanner.actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.ex.ComboBoxAction;
import com.intellij.openapi.project.Project;
import org.intellij.idea.plugins.xplanner.PluginConfiguration;
import org.intellij.idea.plugins.xplanner.config.ConfigurationException;
import org.intellij.idea.plugins.xplanner.model.Model;
import org.jetbrains.annotations.NotNull;
import org.xplanner.Person;
import org.xplanner.XPlanner;
import javax.swing.*;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Comparator;

/**
 * @author ekarpov
 */
public class PairChooseAction extends ComboBoxAction {

    private static final String NOBODY = "<nobody>";

    private Person[] persons;

    private PluginConfiguration config;

    public void update(final AnActionEvent e) {
        super.update(e);
        final Project project = PlatformDataKeys.PROJECT.getData(e.getDataContext());
        if (project != null) {
            try {
                final PluginConfiguration config = getConfiguration(project);
                if (persons == null) {
                    final XPlanner xplanner = config.getXPlanner();
                    persons = xplanner.getPeople();
                    Arrays.sort(persons, new Comparator<Person>() {

                        public int compare(final Person person, final Person person1) {
                            return person.getName().compareToIgnoreCase(person1.getName());
                        }
                    });
                }
                final int defaultPair = config.getDefaultPair();
                if (defaultPair == Model.NOBODY_PAIR_ID) {
                    e.getPresentation().setText(NOBODY);
                } else {
                    for (final Person person : persons) {
                        if (person.getId() == defaultPair) {
                            e.getPresentation().setText(person.getName());
                        }
                    }
                }
            } catch (ConfigurationException e1) {
            } catch (RemoteException e1) {
            }
        }
    }

    private PluginConfiguration getConfiguration(final Project project) {
        if (config == null && project != null) {
            config = PluginConfiguration.getInstance(project);
        }
        return config;
    }

    @NotNull
    protected DefaultActionGroup createPopupActionGroup(final JComponent button) {
        final PluginConfiguration config = getConfiguration(null);
        final String currentUserName;
        if (config != null) {
            currentUserName = config.getUserName();
        } else {
            currentUserName = null;
        }
        final DefaultActionGroup group = new DefaultActionGroup();
        group.add(new ChooseAction(Model.NOBODY_PAIR_ID, NOBODY));
        for (final Person person : persons) {
            if (!person.getUserId().equals(currentUserName)) {
                group.add(new ChooseAction(person.getId(), person.getName()));
            }
        }
        return group;
    }

    private class ChooseAction extends AnAction {

        private final int personID;

        public ChooseAction(final int personID, final String person) {
            super(person);
            this.personID = personID;
        }

        public void actionPerformed(final AnActionEvent e) {
            final Project project = PlatformDataKeys.PROJECT.getData(e.getDataContext());
            if (project != null) {
                getConfiguration(project).setDefaultPair(personID);
            }
        }
    }
}
