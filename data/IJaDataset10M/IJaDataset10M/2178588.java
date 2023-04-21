package de.miethxml.hawron.gui.context;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import de.miethxml.hawron.gui.project.PasswordDialog;
import de.miethxml.hawron.gui.project.ProjectViewComponent;
import de.miethxml.hawron.net.PublishTarget;
import de.miethxml.hawron.project.Project;
import de.miethxml.hawron.project.Task;
import de.miethxml.toolkit.gui.LocaleSeparator;
import de.miethxml.toolkit.ui.event.ComboBoxModelWrapper;

/**
*This is the ProjectViewComponent implementation
* @author <a href="mailto:simon.mieth@gmx.de">Simon Mieth </a>
 */
public class ProjectContextViewComponent extends ContextViewComponentImpl implements ProjectViewComponent {

    private Project project;

    private JPanel dockComponent;

    public void setProject(Project project) {
        String contextPath = project.getCocoonBeanConfiguration().getContextDir();
        this.project = project;
        if (initialized) {
            destinationList.setModel(new ComboBoxModelWrapper(project.getTargetsListModel()));
            taskBuildDir.setModel(new ComboBoxModelWrapper(project.getTaskModel()));
            if (contextPath != null) {
                this.contextBase = contextPath;
                setBaseLocation(contextPath);
                searchEngine.setSearchRootPath(contextPath);
                searchEngine.setConfigLocation(project.getConfigLocation());
                bookmarkView.setConfigLocation(project.getConfigLocation());
                bookmarkView.setBaseURL(contextPath);
            }
        }
    }

    private JPanel buildDockComponent() {
        dockComponent = new JPanel();
        FormLayout panellayout = new FormLayout("3dlu,74dlu:grow,3dlu", "3dlu,top:pref,6dlu,fill:10dlu:grow,fill:3dlu:grow(0.25),pref,2dlu,pref,6dlu,pref,2dlu,pref,3dlu");
        dockComponent.setLayout(panellayout);
        CellConstraints ccp = new CellConstraints();
        JLabel iconlabel = new JLabel(new ImageIcon("icons/editing.png"));
        dockComponent.add(iconlabel, ccp.xy(2, 2));
        bookmarkView.setContextViewComponent(this);
        bookmarkView.initialize();
        dockComponent.add(bookmarkView.getBookmarkView(), ccp.xy(2, 4));
        taskBuildDir = new JComboBox();
        taskBuildDir.setEditable(false);
        taskBuildDir.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        int index = taskBuildDir.getSelectedIndex();
                        if (index >= 0) {
                            Task t = (Task) project.getTasks().get(index);
                            setBaseLocation(t.getBuildDir());
                        }
                    }
                });
            }
        });
        dockComponent.add(new LocaleSeparator("view.context.list.task.buildir"), ccp.xy(2, 6));
        dockComponent.add(taskBuildDir, ccp.xy(2, 8));
        dockComponent.add(new LocaleSeparator("view.context.list.uris"), ccp.xy(2, 10));
        destinationList = new JComboBox();
        destinationList.setEditable(false);
        destinationList.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        int index = destinationList.getSelectedIndex();
                        if (index >= 0) {
                            PublishTarget pt = project.getPublishTarget(index);
                            if ((pt.getUsername() != null) && (pt.getUsername().length() > 0)) {
                                char[] pass = PasswordDialog.showDialog(pt);
                                setBaseLocation(pt.getProtocol() + pt.getUsername() + ":" + new String(pass) + "@" + pt.getURI());
                            } else {
                                setBaseLocation(pt.getProtocol() + pt.getURI());
                            }
                        }
                    }
                });
            }
        });
        dockComponent.add(destinationList, ccp.xy(2, 12));
        return dockComponent;
    }

    public JComponent getDockComponent() {
        if (dockComponent == null) {
            return buildDockComponent();
        }
        return dockComponent;
    }

    public String getKey() {
        return "panel.contextview";
    }
}
