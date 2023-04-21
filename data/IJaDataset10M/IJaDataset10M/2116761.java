package de.objectcode.time4u.server.ejb.seam.api.report;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import de.objectcode.time4u.server.api.data.EntityType;

@XmlType(name = "todo-report")
@XmlRootElement(name = "todo-report")
public class TodoReportDefinition extends BaseReportDefinition {

    private static final long serialVersionUID = -4185237334545181394L;

    /** List of projections to project the todo data to report columns. */
    List<IProjection> m_projections;

    /** Group-by definitions. */
    List<GroupByDefinition> m_groupByDefinitions;

    boolean m_aggregate;

    public TodoReportDefinition() {
        m_projections = new ArrayList<IProjection>();
        m_groupByDefinitions = new ArrayList<GroupByDefinition>();
    }

    @XmlElementWrapper(name = "projections", namespace = "http://objectcode.de/time4u/ejb/seam/report")
    @XmlElements({ @XmlElement(name = "project", namespace = "http://objectcode.de/time4u/ejb/seam/report", type = ProjectProjection.class), @XmlElement(name = "task", namespace = "http://objectcode.de/time4u/ejb/seam/report", type = TaskProjection.class), @XmlElement(name = "reporter", namespace = "http://objectcode.de/time4u/ejb/seam/report", type = PersonProjection.class), @XmlElement(name = "todo", namespace = "http://objectcode.de/time4u/ejb/seam/report", type = TodoProjection.class) })
    public List<IProjection> getProjections() {
        return m_projections;
    }

    public void setProjections(final List<IProjection> projections) {
        m_projections = projections;
    }

    public void addProjection(final IProjection projection) {
        m_projections.add(projection);
    }

    @XmlElementWrapper(name = "group-bys", namespace = "http://objectcode.de/time4u/ejb/seam/report")
    @XmlElementRef
    public List<GroupByDefinition> getGroupByDefinitions() {
        return m_groupByDefinitions;
    }

    public void setGroupByDefinitions(final List<GroupByDefinition> groupByDefinitions) {
        m_groupByDefinitions = groupByDefinitions;
    }

    public void addGroupByDefinition(final GroupByDefinition groupBy) {
        m_groupByDefinitions.add(groupBy);
    }

    @XmlAttribute
    public boolean isAggregate() {
        return m_aggregate;
    }

    public void setAggregate(final boolean aggregate) {
        m_aggregate = aggregate;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public EntityType getEntityType() {
        return EntityType.TODO;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public IReportDataCollector createDataCollector() {
        return new ListReportDataCollector(m_name, m_projections, m_aggregate, m_groupByDefinitions);
    }
}
