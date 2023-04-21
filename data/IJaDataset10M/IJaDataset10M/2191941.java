package br.ufmg.lcc.eid.dto.arg;

import br.ufmg.lcc.arangi.dto.BasicDTO;
import br.ufmg.lcc.eid.dto.ClassDef;

/**
 * 
 * @author salazar
 *
 */
public class EidObjectArg extends BasicDTO {

    private String eidObecjtType;

    private ClassDef classDefinition;

    private Long classDefinitionId;

    private String eidObecjtAttribute;

    private String attributeValue;

    public String getEidObecjtType() {
        return eidObecjtType;
    }

    public void setEidObecjtType(String eidObecjtType) {
        this.eidObecjtType = eidObecjtType;
    }

    public ClassDef getClassDefinition() {
        return classDefinition;
    }

    public void setClassDefinition(ClassDef classDefinition) {
        this.classDefinition = classDefinition;
    }

    public String getEidObecjtAttribute() {
        return eidObecjtAttribute;
    }

    public void setEidObecjtAttribute(String eidObecjtAttribute) {
        this.eidObecjtAttribute = eidObecjtAttribute;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public Long getClassDefinitionId() {
        return classDefinitionId;
    }

    public void setClassDefinitionId(Long classDefinitionId) {
        this.classDefinitionId = classDefinitionId;
    }
}
