package pl.edu.mimuw.loxim.protogen.lang.cpp.type_helper;

import org.codehaus.plexus.PlexusContainerException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import pl.edu.mimuw.loxim.protogen.api.CodeHelper;
import pl.edu.mimuw.loxim.protogen.api.GeneratorException;
import pl.edu.mimuw.loxim.protogen.api.jaxb.FieldType;
import pl.edu.mimuw.loxim.protogen.api.wrappers.EnumTypeWrapper;
import pl.edu.mimuw.loxim.protogen.api.wrappers.PackageGroupTypeWrapper;
import pl.edu.mimuw.loxim.protogen.api.wrappers.ProtocolTypeWrapper;

public class PackageMapCppTypeHelper extends AbstractCppTypeHelper {

    private PackageGroupTypeWrapper packageGroupWrapper;

    private String type_src;

    public void fieldInitialize(FieldType field, ProtocolTypeWrapper protocol) {
        packageGroupWrapper = protocol.findGroupByName(field.getObjectRef());
        type_src = field.getObjectRefId();
    }

    public String getTypeDeclaration() {
        return packageGroupWrapper.getSuperclassName() + "*";
    }

    public String getInitValue() {
        return "NULL";
    }

    public String getInitCode(String name) {
        return name + "=" + getInitValue() + ";";
    }

    public String destroyCode(String name) {
        return "if(" + name + ") delete " + name + ";\n" + name + "=NULL;\n";
    }

    public String readAssignCode(String name) {
        StringBuffer sb = new StringBuffer();
        sb.append(name + "=" + CodeHelper.upperFirstChar(packageGroupWrapper.getGroupName() + "PackagesFactory") + "::createPackageContent(" + type_src + ",reader);");
        return sb.toString();
    }

    public String writeAssignCode(String name) {
        return writeCode(name);
    }

    public String writeCode(String name) {
        return name + "->serializeContent(writer);";
    }

    public static CppTypeHelper enumTypeWrapperToTypeHelper(EnumTypeWrapper enum_wrapper) throws GeneratorException {
        try {
            return CppTypeHelperFactory.getCppTypeHelper(enum_wrapper.getEnum_type().getAsType());
        } catch (PlexusContainerException e) {
            throw new GeneratorException(e);
        } catch (ComponentLookupException e) {
            throw new GeneratorException(e);
        }
    }

    public String getMapType() {
        return null;
    }

    public String getTypeName() {
        return packageGroupWrapper.getSuperclassName();
    }

    public String isInCode(String value, String string) {
        return null;
    }

    public boolean isMappable() {
        return false;
    }

    public String getIncludeCode() {
        String dirName = packageGroupWrapper.isMainGroup() ? "packages" : "packages_" + packageGroupWrapper.getGroupName();
        return dirName + "/" + CodeHelper.upperFirstChar(packageGroupWrapper.getGroupName() + "PackagesFactory");
    }

    public String compareCode(String val1, String val2) {
        return "((!" + val1 + " && ! " + val2 + ")" + "||(" + val1 + " && " + val2 + " && " + "(" + val1 + "->equals(" + val2 + "))))";
    }
}
