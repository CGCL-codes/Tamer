package fi.uoma.scrummer.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

/**
 * A product backlog 
 * 
 * @author bertell
 */
@Entity
public class ProductBacklog extends IdentifiableEntity {

    private static final long serialVersionUID = 1L;

    public ProductBacklog(String productName) {
        this.setProductName(productName);
    }

    public ProductBacklog() {
    }

    @OneToMany
    @JoinColumn(name = "productbacklog_id")
    public List<Story> stories = new ArrayList<Story>();

    public void addStory(Story story) {
        stories.add(story);
    }

    public void removeStory(Story story) {
        stories.remove(story);
    }

    public List<Story> getStories() {
        return Collections.unmodifiableList(stories);
    }

    private String productName;

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public String toString() {
        return getClass().getSimpleName() + "#" + getId() + ": " + productName;
    }
}
