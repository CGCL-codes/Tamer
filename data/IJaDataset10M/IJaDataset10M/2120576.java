package pl.edu.mimuw.loxim.protocol.packages;

import java.util.EnumSet;
import pl.edu.mimuw.loxim.protogen.lang.java.template.ptools.Package;
import pl.edu.mimuw.loxim.protogen.lang.java.template.exception.ProtocolException;
import pl.edu.mimuw.loxim.protogen.lang.java.template.streams.PackageInputStreamReader;
import pl.edu.mimuw.loxim.protogen.lang.java.template.streams.PackageOutputStreamWriter;
import pl.edu.mimuw.loxim.protocol.enums.*;

public class W_s_helloPackage extends Package {

    public static final short ID = (short) 11;

    /**
**/
    private short protocol_major = 0;

    /**
**/
    private short protocol_minor = 0;

    /**
**/
    private short system_major = 0;

    /**
**/
    private short system_minor = 0;

    /**
**/
    private long max_package_size = 0;

    /**
**/
    private EnumSet<FeaturesEnum> features = EnumSet.noneOf(FeaturesEnum.class);

    /**
**/
    private EnumSet<Auth_methodsEnum> auth_methods = EnumSet.noneOf(Auth_methodsEnum.class);

    /**
**/
    private byte[] salt = new byte[0];

    public W_s_helloPackage() {
    }

    public W_s_helloPackage(short a_protocol_major, short a_protocol_minor, short a_system_major, short a_system_minor, long a_max_package_size, EnumSet<FeaturesEnum> a_features, EnumSet<Auth_methodsEnum> a_auth_methods, byte[] a_salt) {
        protocol_major = a_protocol_major;
        protocol_minor = a_protocol_minor;
        system_major = a_system_major;
        system_minor = a_system_minor;
        max_package_size = a_max_package_size;
        features = a_features;
        auth_methods = a_auth_methods;
        salt = a_salt;
    }

    @Override
    public long getPackageType() {
        return (long) ID;
    }

    @Override
    protected void deserializeW(PackageInputStreamReader reader) throws ProtocolException {
        super.deserializeW(reader);
        try {
            protocol_major = reader.readUint8();
            protocol_minor = reader.readUint8();
            system_major = reader.readUint8();
            system_minor = reader.readUint8();
            max_package_size = reader.readUint32();
            java.math.BigInteger tmp_features = reader.readUint64();
            features = FeaturesEnum.createEnumMapByValue(tmp_features);
            java.math.BigInteger tmp_auth_methods = reader.readUint64();
            auth_methods = Auth_methodsEnum.createEnumMapByValue(tmp_auth_methods);
            salt = reader.readBytes();
        } catch (Exception e) {
            throw new ProtocolException(e);
        }
    }

    @Override
    protected void serializeW(PackageOutputStreamWriter writer) throws ProtocolException {
        super.serializeW(writer);
        try {
            writer.writeUint8(protocol_major);
            writer.writeUint8(protocol_minor);
            writer.writeUint8(system_major);
            writer.writeUint8(system_minor);
            writer.writeUint32(max_package_size);
            writer.writeUint64(FeaturesEnum.toEnumMapValue(features));
            writer.writeUint64(Auth_methodsEnum.toEnumMapValue(auth_methods));
            writer.writeBytes(salt);
        } catch (Exception e) {
            throw new ProtocolException(e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        W_s_helloPackage p = (W_s_helloPackage) obj;
        if (!((protocol_major == p.getProtocol_major()))) return false;
        if (!((protocol_minor == p.getProtocol_minor()))) return false;
        if (!((system_major == p.getSystem_major()))) return false;
        if (!((system_minor == p.getSystem_minor()))) return false;
        if (!((max_package_size == p.getMax_package_size()))) return false;
        if (!(((features == p.getFeatures()) || ((features != null) && (p.getFeatures() != null) && ((features).equals(p.getFeatures())))))) return false;
        if (!(((auth_methods == p.getAuth_methods()) || ((auth_methods != null) && (p.getAuth_methods() != null) && ((auth_methods).equals(p.getAuth_methods())))))) return false;
        if (!(java.util.Arrays.equals(salt, p.getSalt()))) return false;
        return true;
    }

    public short getProtocol_major() {
        return protocol_major;
    }

    ;

    public void setProtocol_major(short a_protocol_major) {
        protocol_major = a_protocol_major;
    }

    ;

    public short getProtocol_minor() {
        return protocol_minor;
    }

    ;

    public void setProtocol_minor(short a_protocol_minor) {
        protocol_minor = a_protocol_minor;
    }

    ;

    public short getSystem_major() {
        return system_major;
    }

    ;

    public void setSystem_major(short a_system_major) {
        system_major = a_system_major;
    }

    ;

    public short getSystem_minor() {
        return system_minor;
    }

    ;

    public void setSystem_minor(short a_system_minor) {
        system_minor = a_system_minor;
    }

    ;

    public long getMax_package_size() {
        return max_package_size;
    }

    ;

    public void setMax_package_size(long a_max_package_size) {
        max_package_size = a_max_package_size;
    }

    ;

    public EnumSet<FeaturesEnum> getFeatures() {
        return features;
    }

    ;

    public void setFeatures(EnumSet<FeaturesEnum> a_features) {
        features = a_features;
    }

    ;

    public EnumSet<Auth_methodsEnum> getAuth_methods() {
        return auth_methods;
    }

    ;

    public void setAuth_methods(EnumSet<Auth_methodsEnum> a_auth_methods) {
        auth_methods = a_auth_methods;
    }

    ;

    public byte[] getSalt() {
        return salt;
    }

    ;

    public void setSalt(byte[] a_salt) {
        salt = a_salt;
    }

    ;
}
