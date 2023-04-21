package net.sf.julie.library.io;

import net.sf.julie.Interpretable;
import net.sf.julie.SchemeSystem;
import net.sf.julie.types.Procedure;
import net.sf.julie.types.io.OutputPort;

public class Display extends Procedure {

    public Display(SchemeSystem environment) {
        super(environment);
    }

    @Override
    public Interpretable interpret() {
        Interpretable obj = arguments.get(0);
        OutputPort port;
        if (arguments.size() > 1) {
            port = (OutputPort) arguments.get(1);
        } else {
            port = schemeSystem.getDefaultOutputPort();
        }
        port.write(obj.getDisplayedRepresentation());
        return SchemeSystem.UNSPECIFIED;
    }
}
