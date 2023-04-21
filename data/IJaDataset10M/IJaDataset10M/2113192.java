package org.fao.fenix.domain.project;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import org.fao.fenix.domain.Resource;
import org.fao.fenix.domain.ResourceVisitor;
import org.fao.fenix.domain.exception.FenixException;
import org.fao.fenix.domain.filter.DataList;
import org.fao.fenix.domain.map.layer.ExternalWMSLayer;
import org.fao.fenix.domain.map.layer.InternalWMSLayer;
import org.fao.fenix.domain.perspective.ChartView;
import org.fao.fenix.domain.perspective.MapView;
import org.fao.fenix.domain.perspective.ReportView;
import org.fao.fenix.domain.perspective.TableView;
import org.fao.fenix.domain.perspective.TextView;

@Entity
public class Project extends Resource {

    /**
	 * Take also a look at this post: http://forum.hibernate.org/viewtopic.php?t=984962
	 * 
	 * TODO make this a bidirectional many to many relationship
	 * 
	 * 
	 */
    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    private List<ProjectObject> projectObjectList;

    public void addResource(Resource resource) {
        resource.accept(new ResourceVisitor() {

            private void unimplemented(Resource r) {
                throw new FenixException("UNIMPLEMENTED: The Resource " + r.getClass().getSimpleName() + " can't be added to a Project yet");
            }

            private void deny(Resource r) {
                throw new FenixException("The Resource " + r.getClass().getSimpleName() + " can't be added to a Project");
            }

            @Override
            public void visit(DataList r) {
                unimplemented(r);
            }

            @Override
            public void visit(Project r) {
                deny(r);
            }

            @Override
            public void visit(InternalWMSLayer r) {
                deny(r);
            }

            @Override
            public void visit(ExternalWMSLayer r) {
                deny(r);
            }

            @Override
            public void visit(TableView tableView) {
            }

            @Override
            public void visit(ChartView chartView) {
            }

            @Override
            public void visit(ReportView reportView) {
            }

            @Override
            public void visit(MapView mapView) {
            }

            @Override
            public void visit(TextView textView) {
            }
        });
    }

    public void accept(ResourceVisitor resourceVisitor) {
        resourceVisitor.visit(this);
    }

    public List<ProjectObject> getProjectObjectList() {
        return projectObjectList;
    }

    public void addProjectObject(ProjectObject projectObject) {
        if (this.projectObjectList == null) {
            this.projectObjectList = new ArrayList<ProjectObject>();
        }
        this.projectObjectList.add(projectObject);
    }

    public void removeProjectObject(ProjectObject projectObject) {
        boolean projectObjectDeleted = projectObjectList.remove(projectObject);
        if (!projectObjectDeleted) throw new FenixException("No projectObject found which could be deleted");
    }

    @SuppressWarnings("unchecked")
    public void setProjectObjectList(List<? extends ProjectObject> projectObjectList) {
        this.projectObjectList = (List<ProjectObject>) projectObjectList;
    }
}
