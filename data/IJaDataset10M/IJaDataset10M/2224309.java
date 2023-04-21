package au.gov.nla.aons.repository.dto;

import java.io.Serializable;
import java.util.List;

public class CollectionPathInfo implements Serializable {

    private static final long serialVersionUID = -506285342710612917L;

    private ShortRepositoryDto shortRepositoryDto;

    private List<ShortCollectionDto> parents;

    private List<ShortCollectionDto> children;

    public ShortRepositoryDto getShortRepositoryDto() {
        return shortRepositoryDto;
    }

    public void setShortRepositoryDto(ShortRepositoryDto shortRepositoryDto) {
        this.shortRepositoryDto = shortRepositoryDto;
    }

    public List<ShortCollectionDto> getParents() {
        return parents;
    }

    public void setParents(List<ShortCollectionDto> parents) {
        this.parents = parents;
    }

    public List<ShortCollectionDto> getChildren() {
        return children;
    }

    public void setChildren(List<ShortCollectionDto> children) {
        this.children = children;
    }
}
