package onepoint.project.modules.project;

import java.sql.Date;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class OpActivityFilter {

    public abstract static class PostFilter {

        public boolean keep(OpActivityIfc actIfc) {
            return true;
        }

        public boolean keep(OpAssignmentIfc assIfc) {
            return true;
        }
    }

    public static final int ALL = -1;

    private boolean templates = false;

    private boolean dependencies = false;

    private boolean workPhases = false;

    private boolean exportedOnly = false;

    private boolean doNotFlatten = false;

    private int maxOutlineLevel = ALL;

    private Collection<Long> projectNodeIds = null;

    private Collection<Long> resourceIds = null;

    private Collection<Long> activityIds = null;

    private Collection<Long> activityVersionIds = null;

    private Set<Byte> types = null;

    private Map<Byte, Set<Integer>> completedStatuses = null;

    private Date startFrom = null;

    private Date startTo = null;

    private Boolean completed = null;

    private Boolean assignmentCompleted = null;

    private Boolean active = null;

    private PostFilter postFilter = null;

    private OpFilteredActivityFactory filteredActivityFactory = null;

    public final void setTemplates(boolean templates) {
        this.templates = templates;
    }

    public final boolean getTemplates() {
        return templates;
    }

    public final void setDependencies(boolean dependencies) {
        this.dependencies = dependencies;
    }

    public final boolean getDependencies() {
        return dependencies;
    }

    public final void setWorkPhases(boolean workPhases) {
        this.workPhases = workPhases;
    }

    public boolean isExportedOnly() {
        return exportedOnly;
    }

    public void setExportedOnly(boolean exportedOnly) {
        this.exportedOnly = exportedOnly;
    }

    public boolean doNotFlatten() {
        return doNotFlatten;
    }

    public void setDoNotFlatten(boolean doNotFlatten) {
        this.doNotFlatten = doNotFlatten;
    }

    public int getMaxOutlineLevel() {
        return maxOutlineLevel;
    }

    public void setMaxOutlineLevel(int maxOutlineLevel) {
        this.maxOutlineLevel = maxOutlineLevel;
    }

    public final boolean getWorkPhases() {
        return workPhases;
    }

    public final void setProjectNodeIds(Collection<Long> projectNodeIds) {
        this.projectNodeIds = projectNodeIds;
    }

    public final void addProjectNodeID(long projectNodeId) {
        if (projectNodeIds == null) {
            projectNodeIds = new HashSet<Long>();
        }
        projectNodeIds.add(new Long(projectNodeId));
    }

    public final Collection<Long> getProjectNodeIds() {
        return projectNodeIds;
    }

    public final void setResourceIds(Collection<Long> resourceIds) {
        this.resourceIds = resourceIds;
    }

    public final void addResourceID(long resourceId) {
        if (resourceIds == null) {
            resourceIds = new HashSet<Long>();
        }
        resourceIds.add(new Long(resourceId));
    }

    public final Collection<Long> getResourceIds() {
        return resourceIds;
    }

    public final void setTypes(Set<Byte> types) {
        this.types = types;
    }

    public final void addType(byte type) {
        if (types == null) {
            types = new HashSet<Byte>();
        }
        types.add(new Byte(type));
    }

    public final Set<Byte> getTypes() {
        return types;
    }

    public final void setStartFrom(Date startFrom) {
        this.startFrom = startFrom;
    }

    public final Date getStartFrom() {
        return startFrom;
    }

    public final void setStartTo(Date startTo) {
        this.startTo = startTo;
    }

    public final Date getStartTo() {
        return startTo;
    }

    public final void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public final Boolean getCompleted() {
        return completed;
    }

    public void setAssignmentCompleted(Boolean assignmentComplete) {
        assignmentCompleted = assignmentComplete;
    }

    public Boolean getAssignmentComplete() {
        return assignmentCompleted;
    }

    public Set<Integer> getCompletedStatuses(byte type) {
        if (completedStatuses == null) {
            return null;
        }
        return completedStatuses.get(new Byte(type));
    }

    public void setCompletedStatuses(byte type, Set<Integer> completedStatuses) {
        if (this.completedStatuses == null) {
            this.completedStatuses = new HashMap<Byte, Set<Integer>>();
        }
        this.completedStatuses.put(new Byte(type), completedStatuses);
    }

    /**
    * @param id
    * @return
    * @pre
    * @post
    */
    public boolean containsResourceID(long id) {
        return resourceIds.contains(id);
    }

    public boolean isEmptyFilter() {
        return (projectNodeIds != null && projectNodeIds.isEmpty()) || (resourceIds != null && resourceIds.isEmpty()) || (types != null && types.isEmpty()) || ((activityIds != null && activityIds.isEmpty()) && (activityVersionIds != null && activityVersionIds.isEmpty()));
    }

    public void setActivityIds(Collection<Long> activityIds) {
        this.activityIds = activityIds;
    }

    public Collection<Long> getActivityIds() {
        return activityIds;
    }

    public void setActivityVersionIds(Collection<Long> activityVersionIds) {
        this.activityVersionIds = activityVersionIds;
    }

    public Collection<Long> getActivityVersionIds() {
        return activityVersionIds;
    }

    public void setFilteredActivityFactory(OpFilteredActivityFactory filteredActivityFactory) {
        this.filteredActivityFactory = filteredActivityFactory;
    }

    public OpFilteredActivityFactory getFilteredActivityFactory() {
        return filteredActivityFactory;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getActive() {
        return active;
    }

    public void setPostFilter(PostFilter postFilter) {
        this.postFilter = postFilter;
    }

    public PostFilter getPostFilter() {
        return postFilter;
    }
}
