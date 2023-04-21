package org.gtdfree.model;

import java.util.Set;

/**
 * @author ikesan
 *
 */
public class FolderProjectActionFilter implements ActionFilter {

    private Set<Integer> folders;

    private Set<Integer> projects;

    private ActionFilter filter;

    private boolean includeWithoutProject;

    /**
	 * @param folders
	 * @param projects
	 * @param nullProjectAccepted
	 */
    public FolderProjectActionFilter(Set<Integer> folders, Set<Integer> projects, ActionFilter f, boolean includeWithoutProject) {
        super();
        this.folders = folders;
        this.projects = projects;
        this.filter = f;
        this.includeWithoutProject = includeWithoutProject;
        if (filter == null) {
            filter = new DummyFilter(true);
        }
    }

    @Override
    public boolean isAcceptable(Folder f, Action a) {
        if (f.isProject()) {
            if (!projects.contains(f.getId())) {
                return false;
            }
        } else if (!folders.contains(f.getId())) {
            return false;
        }
        if (a == null) {
            return filter.isAcceptable(f, null);
        }
        if (a.getProject() == null) {
            if (!includeWithoutProject) {
                return false;
            }
        } else if (!projects.contains(a.getProject())) {
            return false;
        }
        return filter.isAcceptable(f, a);
    }
}
