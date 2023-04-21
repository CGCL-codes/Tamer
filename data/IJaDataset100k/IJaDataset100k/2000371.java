package org.identifylife.key.engine.harvest.model;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author dbarnier
 *
 */
public class TaxonName {

    private Long id;

    private String name;

    private Long taxonId;

    private int index;

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

    public Long getTaxonId() {
        return taxonId;
    }

    public void setTaxonId(Long taxonId) {
        this.taxonId = taxonId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String toString() {
        return new ToStringBuilder(this).append("id", getId()).append("name", getName()).toString();
    }
}
