package net.sf.hattori.inheritance.generalization;

import net.sf.hattori.annotations.ObjectPopulation;
import net.sf.hattori.repository.AbstractPersistentObjectDTO;

@ObjectPopulation(domainObjectClass = Specialization.class)
public class SpecializationDTO extends AbstractPersistentObjectDTO {

    private String name;

    private String detail;

    private String protectedText;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getProtectedText() {
        return protectedText;
    }

    public void setProtectedText(String protectedText) {
        this.protectedText = protectedText;
    }
}
