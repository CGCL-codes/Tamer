package org.gloudy.main.model.admin;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * @author pcorne
 */
public class AdminSectionEntry {

    private Long sortOrder;

    private String name;

    private AdminSection adminSection;

    private String action;

    public AdminSectionEntry(String newName, Long newSortOrder, String newAction) {
        name = newName;
        sortOrder = newSortOrder;
        action = newAction;
    }

    public AdminSection getAdminSection() {
        return adminSection;
    }

    public void setAdminSection(AdminSection adminSection) {
        this.adminSection = adminSection;
    }

    /**
   * @return the sortOrder
   */
    public Long getSortOrder() {
        return sortOrder;
    }

    /**
   * @param sortOrder the sortOrder to set
   */
    public void setSortOrder(Long sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
   * @return the name
   */
    public String getName() {
        return name;
    }

    /**
   * @param name the name to set
   */
    public void setName(String name) {
        this.name = name;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(13, 5381).append(getAdminSection().hashCode()).append(getName()).append(getSortOrder()).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof AdminSectionEntry)) {
            return false;
        }
        AdminSectionEntry as = (AdminSectionEntry) obj;
        return new EqualsBuilder().append(this.getAdminSection().hashCode(), as.getAdminSection().hashCode()).append(this.getName(), as.getName()).append(this.getSortOrder(), as.getSortOrder()).isEquals();
    }
}
