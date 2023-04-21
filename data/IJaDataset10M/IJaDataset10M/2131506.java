package au.gov.nla.aons.format.domain;

import java.io.Serializable;

public class FormatShort implements Serializable {

    private static final long serialVersionUID = 5106296165057482525L;

    private Long id;

    private Long registryId;

    private String registryName;

    private String type;

    private String name;

    private Float relevance;

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

    public Long getRegistryId() {
        return registryId;
    }

    public void setRegistryId(Long registryId) {
        this.registryId = registryId;
    }

    public String getRegistryName() {
        return registryName;
    }

    public void setRegistryName(String registryName) {
        this.registryName = registryName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Float getRelevance() {
        return relevance;
    }

    public void setRelevance(Float relevance) {
        this.relevance = relevance;
    }
}
