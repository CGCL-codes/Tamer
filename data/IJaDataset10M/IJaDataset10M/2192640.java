package router.policy;

import java.io.Serializable;
import router.*;
import packet.flit.header.*;
import router.InputFifo;

public class Dump_Policy extends Policy implements Serializable {

    public Dump_Policy(Router r) {
        super(r);
    }

    public int getPolicyCode() {
        return 10;
    }

    public String getPolicyDescription() {
        return "Dump Ordered";
    }

    public String getPolicyName() {
        return "Dump";
    }

    public boolean route(InputFifo[][] input, String[] in_alias, String[] out_alias) {
        for (int i = 0; i < input.length; i++) {
            if (input[i][0].peek() != null) {
                input[i][0].peek().toXml();
                input[i][0].pull();
            }
        }
        return true;
    }
}
