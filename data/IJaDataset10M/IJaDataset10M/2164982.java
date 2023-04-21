package net.jtools.util;

import java.io.PrintWriter;
import javax.tools.DiagnosticCollector;
import org.apache.tools.ant.types.DataType;
import org.jtools.meta.meta_inf.antlib.AntDef;
import org.jtools.meta.meta_inf.antlib.AntLib;
import org.jtools.meta.meta_inf.antlib.DefType;
import org.jtools.util.Reportable;
import org.jtools.util.diagnostic.DiagnosticUtils;

@AntLib(@AntDef(type = DefType.TYPE, value = "diagnostics"))
public class DiagnosticContainer extends DataType implements Reportable {

    private final DiagnosticCollector listener = new DiagnosticCollector();

    public DiagnosticCollector<?> getDiagnosticListener() {
        return listener;
    }

    public void report(PrintWriter dest) {
        if (isReference()) {
            ((Reportable) getRefid().getReferencedObject()).report(dest);
            return;
        }
        DiagnosticUtils.println(dest, listener.getDiagnostics());
    }
}
