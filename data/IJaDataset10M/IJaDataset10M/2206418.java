package pl.edu.mimuw.loxim.protocol.packages;

import java.util.EnumSet;
import pl.edu.mimuw.loxim.protogen.lang.java.template.ptools.Package;
import pl.edu.mimuw.loxim.protogen.lang.java.template.exception.ProtocolException;
import pl.edu.mimuw.loxim.protogen.lang.java.template.streams.PackageInputStreamReader;
import pl.edu.mimuw.loxim.protogen.lang.java.template.streams.PackageOutputStreamWriter;
import pl.edu.mimuw.loxim.protocol.enums.*;

public class Q_s_executingPackage extends Package {

    public static final short ID = (short) 67;

    public Q_s_executingPackage() {
    }

    @Override
    public long getPackageType() {
        return (long) ID;
    }

    @Override
    protected void deserializeW(PackageInputStreamReader reader) throws ProtocolException {
        super.deserializeW(reader);
    }

    @Override
    protected void serializeW(PackageOutputStreamWriter writer) throws ProtocolException {
        super.serializeW(writer);
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        Q_s_executingPackage p = (Q_s_executingPackage) obj;
        return true;
    }
}
