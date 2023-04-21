package whiteoak.tools.openjdk.compiler.util;

import static com.sun.tools.javac.util.JCDiagnostic.DiagnosticType.FRAGMENT;
import com.sun.tools.javac.util.JCDiagnostic;
import com.sun.tools.javac.util.Messages;

public class WhiteoakJCDiagnostic extends JCDiagnostic {

    protected WhiteoakJCDiagnostic(Messages messages, DiagnosticType dt, boolean mandatory, DiagnosticSource source, DiagnosticPosition pos, String key, Object[] args) {
        super(messages, dt, mandatory, source, pos, key, args);
    }

    public static JCDiagnostic fragment(String key, Object... args) {
        return new WhiteoakJCDiagnostic(WhiteoakMessages.getDefaultMessages(), FRAGMENT, false, null, null, "compiler." + FRAGMENT.key + "." + key, args);
    }
}
