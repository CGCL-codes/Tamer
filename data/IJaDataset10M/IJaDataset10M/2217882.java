package org.esfinge.aom.example.bankingsystem.person;

import java.util.ArrayList;
import java.util.List;
import org.esfinge.aom.model.rolemapper.metadata.annotations.CreateEntityMethod;
import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityType;
import org.esfinge.aom.model.rolemapper.metadata.annotations.Name;
import org.esfinge.aom.model.rolemapper.metadata.annotations.PropertyType;

@EntityType
public class PersonType {

    @Name
    private String typeName;

    @PropertyType
    private List<PersonPropertyType> propertyTypes = new ArrayList<PersonPropertyType>();

    @CreateEntityMethod
    public Person createPerson() {
        Person person = new Person();
        person.setPersonType(this);
        return person;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<PersonPropertyType> getPropertyTypes() {
        return propertyTypes;
    }

    public void setPropertyTypes(List<PersonPropertyType> propertyTypes) {
        this.propertyTypes = propertyTypes;
    }

    public void addPropertyTypes(PersonPropertyType propertyType) {
        propertyTypes.add(propertyType);
    }

    public void removePropertyTypes(PersonPropertyType propertyType) {
        propertyTypes.remove(propertyType);
    }
}
