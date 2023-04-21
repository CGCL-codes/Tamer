package com.ivis.xprocess.ui.view.providers;

import java.util.HashSet;
import java.util.Set;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import com.ivis.xprocess.core.Organization;
import com.ivis.xprocess.core.Person;
import com.ivis.xprocess.ui.datawrappers.DataCacheManager;
import com.ivis.xprocess.ui.datawrappers.IElementWrapper;
import com.ivis.xprocess.ui.datawrappers.OrganizationWrapper;
import com.ivis.xprocess.ui.datawrappers.PersonWrapper;
import com.ivis.xprocess.ui.datawrappers.project.ProjectWrapper;
import com.ivis.xprocess.ui.util.ElementUtil;
import com.ivis.xprocess.ui.widgets.AvailabilityTable;

public class ViewByProjectContentProvider implements ITreeContentProvider {

    public Object[] getChildren(Object parentElement) {
        Set<Object> children = new HashSet<Object>();
        if (parentElement instanceof AvailabilityTable) {
            AvailabilityTable availabilityTable = (AvailabilityTable) parentElement;
            IElementWrapper elementWrapper = (IElementWrapper) availabilityTable.getElements();
            if (elementWrapper instanceof OrganizationWrapper) {
                if (availabilityTable.getViewBy() == AvailabilityTable.VIEW_BY_PERSON) {
                    OrganizationWrapper organizationWrapper = (OrganizationWrapper) elementWrapper;
                    children.add(organizationWrapper);
                    Organization organization = (Organization) organizationWrapper.getElement();
                    for (Person person : organization.getPersons()) {
                        PersonWrapper personWrapper = (PersonWrapper) DataCacheManager.getWrapperByElement(person);
                        children.add(personWrapper);
                    }
                } else {
                    for (IElementWrapper projectWrapper : ElementUtil.getProjects()) {
                        children.add(projectWrapper);
                    }
                }
            }
        }
        if (parentElement instanceof ProjectWrapper) {
            ProjectWrapper projectWrapper = (ProjectWrapper) parentElement;
            for (Object personWrapper : projectWrapper.getPeopleOnProject()) {
                children.add(personWrapper);
            }
        }
        return children.toArray();
    }

    public Object getParent(Object element) {
        return null;
    }

    public boolean hasChildren(Object element) {
        return getChildren(element).length > 0;
    }

    public Object[] getElements(Object inputElement) {
        return getChildren(inputElement);
    }

    public void dispose() {
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }
}
