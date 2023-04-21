package net.sf.timemanager.service.projects.dto;

import net.sf.hattori.annotations.ObjectPopulation;
import net.sf.hattori.repository.AbstractPersistentObjectDTO;
import net.sf.timemanager.domain.projects.Product;
import net.sf.timemanager.service.common.dto.DescribableDTO;

@ObjectPopulation(domainObjectClass = Product.class)
public class ProductDTO extends AbstractPersistentObjectDTO implements DescribableDTO {

    private String name;

    private String description;

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
}
