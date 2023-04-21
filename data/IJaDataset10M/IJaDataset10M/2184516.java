package pl.edu.mimuw.loxim.protocol.packages_data;

import java.util.EnumSet;
import pl.edu.mimuw.loxim.protogen.lang.java.template.ptools.Package;
import pl.edu.mimuw.loxim.protogen.lang.java.template.exception.ProtocolException;
import pl.edu.mimuw.loxim.protogen.lang.java.template.streams.PackageInputStreamReader;
import pl.edu.mimuw.loxim.protogen.lang.java.template.streams.PackageOutputStreamWriter;
import pl.edu.mimuw.loxim.protocol.enums.*;

public class BindingPackage extends Package {

    public static final long ID = (long) 130;

    /**
**/
    private String bindingName = null;

    /**
**/
    private Long type = new Long(0);

    /**
**/
    private Package value = null;

    public BindingPackage() {
    }

    public BindingPackage(String a_bindingName, Long a_type, Package a_value) {
        bindingName = a_bindingName;
        type = a_type;
        value = a_value;
    }

    @Override
    public long getPackageType() {
        return (long) ID;
    }

    @Override
    protected void deserializeW(PackageInputStreamReader reader) throws ProtocolException {
        super.deserializeW(reader);
        try {
            bindingName = reader.readSString();
            type = reader.readVaruint();
            value = pl.edu.mimuw.loxim.protocol.packages_data.DataPackagesFactory.getInstance().createPackage(type);
            value.deserializeContent(reader);
        } catch (Exception e) {
            throw new ProtocolException(e);
        }
    }

    @Override
    protected void serializeW(PackageOutputStreamWriter writer) throws ProtocolException {
        super.serializeW(writer);
        try {
            writer.writeSString(bindingName);
            writer.writeVaruint(type);
            value.serializeContent(writer);
        } catch (Exception e) {
            throw new ProtocolException(e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        BindingPackage p = (BindingPackage) obj;
        if (!(((bindingName == p.getBindingName()) || ((bindingName != null) && (p.getBindingName() != null) && ((bindingName).equals(p.getBindingName())))))) return false;
        if (!(((type == p.getType()) || ((type != null) && (p.getType() != null) && ((type).equals(p.getType())))))) return false;
        if (!(((value == p.getValue()) || ((value != null) && (p.getValue() != null) && ((value).equals(p.getValue())))))) return false;
        return true;
    }

    public String getBindingName() {
        return bindingName;
    }

    ;

    public void setBindingName(String a_bindingName) {
        bindingName = a_bindingName;
    }

    ;

    public Long getType() {
        return type;
    }

    ;

    public void setType(Long a_type) {
        type = a_type;
    }

    ;

    public Package getValue() {
        return value;
    }

    ;

    public void setValue(Package a_value) {
        value = a_value;
    }

    ;
}
