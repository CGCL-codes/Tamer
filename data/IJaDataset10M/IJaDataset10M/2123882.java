package Domini;

import GDisc.*;
import Errors.*;

public class contdiaFestiu {

    private GDBdiaFestiu gdbdf;

    public contdiaFestiu() throws excepcio {
        gdbdf = new GDBdiaFestiu();
    }

    public int quants() throws excepcio {
        return (int) gdbdf.quants();
    }

    public diaFestiu getPrimer() throws excepcio {
        return ((diaFestiu) gdbdf.getFirst());
    }

    public void nouDF(diaFestiu df) throws excepcio {
        gdbdf.guarda(df);
    }

    public diaFestiu getDF() throws excepcio {
        return ((diaFestiu) gdbdf.getObjecte());
    }

    public boolean seguent() throws excepcio {
        return gdbdf.seguent();
    }

    public boolean noEsta(data d) throws excepcio {
        if (gdbdf.quantsData(d) == 0) return true; else return false;
    }
}
