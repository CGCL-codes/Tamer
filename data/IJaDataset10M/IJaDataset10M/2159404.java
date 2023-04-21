package org.pprun.hjpetstore.domain;

import org.pprun.hjpetstore.persistence.DomainObject;

/**
 *
 * @author <a href="mailto:quest.run@gmail.com">pprun</a>
 */
public class Banner extends DomainObject {

    private String bannerName;

    private Category category;

    /** Creates a new instance of Banner */
    public Banner() {
    }

    public String getBannerName() {
        return bannerName;
    }

    public void setBannerName(String bannerName) {
        this.bannerName = bannerName;
    }

    /**
     * @return the favCategory
     */
    public Category getCategory() {
        return category;
    }

    /**
     * @param favCategory the favCategory to set
     */
    public void setCategory(Category Category) {
        this.category = Category;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder(this.getClass().getSimpleName());
        s.append("[");
        s.append("id=").append(id);
        s.append(", ");
        s.append("version=").append(version);
        s.append(", ");
        s.append("bannerName=").append(bannerName);
        s.append("]");
        return s.toString();
    }
}
