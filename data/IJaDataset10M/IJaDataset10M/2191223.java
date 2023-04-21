package org.jcvi.vics.model.dma;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: tnabeel
 * Date: Sep 7, 2007
 * Time: 10:01:47 AM
 */
public class Classification {

    private Long id;

    private String name;

    private String description;

    private Set tags;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set getTags() {
        return tags;
    }

    public void setTags(Set tags) {
        this.tags = tags;
    }
}
