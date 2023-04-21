package org.xteam.cs.gui.views;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.xteam.cs.gui.IWorkbench;
import org.xteam.cs.gui.ResourceAdapterManager;
import org.xteam.cs.jobs.IJob;
import org.xteam.cs.model.IProgressMonitor;
import org.xteam.cs.model.IProjectListener;
import org.xteam.cs.model.Project;
import org.xteam.cs.model.ProjectEvent;
import org.xteam.cs.model.ProjectResource;

public class ProjectPanel extends JPanel {

    private static final long serialVersionUID = 6457032223741771501L;

    private IWorkbench workbench;

    private JPopupMenu popup;

    private JTree tree;

    public ProjectPanel(Project project, IWorkbench workbench) {
        this.workbench = workbench;
        setLayout(new BorderLayout());
        popup = new JPopupMenu();
        popup.setOpaque(true);
        tree = new JTree(new ProjectTreeAdapter(project));
        tree.setShowsRootHandles(true);
        tree.setToggleClickCount(0);
        tree.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    JTree tree = (JTree) evt.getSource();
                    TreePath path = tree.getPathForLocation(evt.getX(), evt.getY());
                    if (path != null) {
                        Object selection = path.getLastPathComponent();
                        if (selection instanceof ProjectResource) {
                            ProjectPanel.this.workbench.openEditor((ProjectResource) selection);
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    openPopupMenu(e.getPoint());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    openPopupMenu(e.getPoint());
                }
            }
        });
        add(new JScrollPane(tree), BorderLayout.CENTER);
    }

    private void openPopupMenu(Point p) {
        fillPopupMenu();
        if (popup.getComponentCount() > 0) popup.show(this, (int) p.getX(), (int) p.getY());
    }

    private void fillPopupMenu() {
        popup.removeAll();
        final Object selection = (Object) tree.getLastSelectedPathComponent();
        if (selection instanceof Project) {
            JMenuItem mi = new JMenuItem("Generate");
            mi.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    ProjectPanel.this.workbench.runJob(new IJob() {

                        @Override
                        public void run(IProgressMonitor monitor) {
                            ((Project) selection).generate(monitor);
                        }
                    });
                }
            });
            popup.add(mi);
            popup.add(new JSeparator());
            mi = new JMenuItem("Properties");
            mi.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    ProjectPanel.this.workbench.openPropertyEditor("Project Properties", ((Project) selection).getProperties());
                }
            });
            popup.add(mi);
        } else if (selection instanceof ProjectResource) {
            JMenuItem mi = new JMenuItem("Open");
            mi.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    ProjectPanel.this.workbench.openEditor((ProjectResource) selection);
                }
            });
            popup.add(mi);
            IResourceAdapter adapter = ResourceAdapterManager.getDefault().getAdapter((ProjectResource) selection);
            if (adapter != null) {
                adapter.fillMenu(popup, workbench);
            }
            popup.add(new JSeparator());
            mi = new JMenuItem("Properties");
            mi.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    ProjectResource res = (ProjectResource) selection;
                    ProjectPanel.this.workbench.openPropertyEditor(res.getName() + " Properties", res.getProperties());
                }
            });
            popup.add(mi);
        }
    }

    private class ProjectTreeAdapter implements TreeModel, IProjectListener {

        private List<TreeModelListener> listeners = new ArrayList<TreeModelListener>();

        private Project project;

        public ProjectTreeAdapter(Project project) {
            this.project = project;
            project.addProjectListener(this);
        }

        @Override
        public void addTreeModelListener(TreeModelListener l) {
            listeners.add(l);
        }

        @Override
        public void removeTreeModelListener(TreeModelListener l) {
            listeners.remove(l);
        }

        @Override
        public Object getChild(Object parent, int index) {
            if (parent instanceof Project) {
                return ((Project) parent).getResources().get(index);
            }
            return null;
        }

        @Override
        public int getChildCount(Object parent) {
            if (parent instanceof Project) {
                return ((Project) parent).getResources().size();
            }
            return 0;
        }

        @Override
        public int getIndexOfChild(Object parent, Object child) {
            if (parent instanceof Project) {
                return ((Project) parent).getResources().indexOf(child);
            }
            return 0;
        }

        @Override
        public Object getRoot() {
            return project;
        }

        @Override
        public boolean isLeaf(Object node) {
            if (node instanceof Project) return false;
            return true;
        }

        @Override
        public void valueForPathChanged(TreePath path, Object newValue) {
            System.out.println("valueForPathChanged(" + path + ", " + newValue + ")");
        }

        @Override
        public void projectChanged(ProjectEvent event) {
            TreeModelEvent te = new TreeModelEvent(this, new Object[] { project });
            for (TreeModelListener l : listeners) {
                if (event.getKind() == ProjectEvent.CHANGE_STATE) {
                    l.treeNodesChanged(te);
                } else if (event.getKind() == ProjectEvent.CHANGE_STRUCTURE || event.getKind() == ProjectEvent.ADD_FILE) {
                    l.treeStructureChanged(te);
                }
            }
        }
    }
}
