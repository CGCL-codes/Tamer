package org.opennms.netmgt.model;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * This class rappresents the main class for Virtual Group.
 * A Group is formed by a collection of GroupItems.
 * Elements that can be added to a Group are:
 * - Nodes
 * - Services
 * - Other Groups
 *
 * @author Daniele Piras for Pronetics S.p.A
 */
@javax.persistence.Entity
@Table(name = "groups")
public class Group {

    private Integer m_id;

    private String m_description;

    private Set<GroupItem> m_items = new LinkedHashSet<GroupItem>();

    @Id
    @Column(name = "groupId")
    @SequenceGenerator(name = "groupNxtId", sequenceName = "groupNxtId")
    @GeneratedValue(generator = "groupNxtId")
    public Integer getId() {
        return m_id;
    }

    public void setId(Integer nodeid) {
        m_id = nodeid;
    }

    @Column(name = "description")
    public String getDescription() {
        return m_description;
    }

    public void setDescription(String description) {
        m_description = description;
    }

    /**
     * Return the array with all group items.
     *
     */
    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    @org.hibernate.annotations.Cascade({ org.hibernate.annotations.CascadeType.ALL })
    public Set<GroupItem> getItems() {
        return m_items;
    }

    public void setItems(Set<GroupItem> items) {
        m_items = items;
    }

    private void addItem(GroupItem item) {
        item.setOwner(this);
        getItems().add(item);
    }

    /**
     * Add a node to the Group
     *
     * @param node to add
     * @return the groupItem created.
     */
    public GroupItem addNode(OnmsNode node) {
        GroupItem item = new GroupItem(node);
        addItem(item);
        return item;
    }

    /**
     * Add a service to the Group
     * @param service to add
     * @return the groupItem created
     */
    public GroupItem addService(OnmsMonitoredService service) {
        GroupItem item = new GroupItem(service);
        addItem(item);
        return item;
    }

    /**
     * Method that check if it is possible to add a group inside another group.
     * A group couldn't be added in this case:
     * - The group is already added
     * - The group is inside another group that is already added
     * - The group containts this group directly or indirectly
     * @param group to verify
     * @return true if is be able to add the given group in this group
     */
    public boolean isAllowToAdd(Group group) {
        boolean res = true;
        int gId = group.getId();
        if (gId != 0) {
            for (GroupItem gItem : getItems()) {
                if (gItem.getType() == GroupItem.ItemType.GROUP) {
                    if (gItem.getId() == gId) {
                        res = false;
                        break;
                    }
                }
            }
        }
        return res;
    }

    /**
     * Add another Group to this Group
     * @param group to add
     * @return groupItem created
     */
    public GroupItem addGroup(Group group) {
        GroupItem item = new GroupItem(group);
        addItem(item);
        return item;
    }

    /**
     * Return the number of NODE contained in this group (not in a recursive
     * manner)
     * @return number of nodes
     */
    @Transient
    public int getNumberOfNodes() {
        int size = 0;
        if (m_items != null) {
            for (GroupItem g : m_items) {
                if (g.getType() == GroupItem.ItemType.NODE) {
                    size++;
                }
            }
        }
        return size;
    }

    /**
     * Return the number of SERVICES contained in this group (not in a recursive
     * manner)
     * @return number of nodes
     */
    @Transient
    public int getNumberOfServices() {
        int size = 0;
        if (m_items != null) {
            for (GroupItem g : m_items) {
                if (g.getType() == GroupItem.ItemType.SERVICE) {
                    size++;
                }
            }
        }
        return size;
    }

    /**
     * Return the number of groups contained in this group (not in a recursive
     * manner)
     * @return number of groups
     */
    @Transient
    public int getNumberOfGroups() {
        int size = 0;
        if (m_items != null) {
            for (GroupItem g : m_items) {
                if (g.getType() == GroupItem.ItemType.GROUP) {
                    size++;
                }
            }
        }
        return size;
    }
}
