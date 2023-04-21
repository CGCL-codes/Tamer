package org.netbeans.module.flexbean.project.ui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.openide.util.Utilities;

/**
 *
 * @author arnaud
 * @see ProjectInformation
 */
public class FlexProjectInformation implements ProjectInformation {

    private static final String FLEX_PROJECT_ICON_PATH = "org/netbeans/module/flexbean/project/ui/resources/flexProjectIcon.png";

    private final Project project;

    private final String name;

    private final String displayName;

    private final PropertyChangeSupport eventsDispatcher;

    public FlexProjectInformation(String name, String displayName, Project project) {
        this.displayName = displayName;
        this.name = name;
        this.project = project;
        eventsDispatcher = new PropertyChangeSupport(this);
    }

    /**
     * 
     * @return
     * @see ProjectInformation#getName() 
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @return
     * @see ProjectInformation#getDisplayName() 
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * 
     * @return
     * @see ProjectInformation#getIcon() 
     */
    public Icon getIcon() {
        return new ImageIcon(Utilities.loadImage(FLEX_PROJECT_ICON_PATH));
    }

    /**
     * 
     * @return
     * @see ProjectInformation#getProject() 
     */
    public Project getProject() {
        return project;
    }

    /**
     * 
     * @param propertyChangeListener
     * @see ProjectInformation#addPropertyChangeListener(java.beans.PropertyChangeListener) 
     */
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        eventsDispatcher.addPropertyChangeListener(propertyChangeListener);
    }

    /**
     * 
     * @param propertyChangeListener
     * @see ProjectInformation#removePropertyChangeListener(java.beans.PropertyChangeListener) 
     */
    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        eventsDispatcher.removePropertyChangeListener(propertyChangeListener);
    }
}
