package yaw.core.uml.metamodel.uml;

public interface IUMLTypeMember {

    String getName();

    void setName(String name);

    void setMinOccur(Integer i);

    void setMaxOccur(Integer i);

    void setTaggedValue(String name, String value);

    void renameTaggedValue(String oldName, String newName);

    String getTaggedValue(String name);

    void deleteTaggedValue(String name);

    boolean isMandatory();
}
