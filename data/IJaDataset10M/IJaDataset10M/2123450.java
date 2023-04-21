package net.sf.hattori.example.general;

import java.util.List;

public class DeepComplexCollectionsDomainReferencedEntity extends AbstractTestIdentifiableObject {

    private List<DeepComplexCollectionsDomainReferencedEntity> elements;

    private String name;

    private Long deep;

    public DeepComplexCollectionsDomainReferencedEntity(String name, Long deep) {
        this.name = name;
        this.deep = deep;
    }

    public Long getDeep() {
        return deep;
    }

    public void setDeep(Long deep) {
        this.deep = deep;
    }

    public List<DeepComplexCollectionsDomainReferencedEntity> getElements() {
        return elements;
    }

    public void setElements(List<DeepComplexCollectionsDomainReferencedEntity> elements) {
        this.elements = elements;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
