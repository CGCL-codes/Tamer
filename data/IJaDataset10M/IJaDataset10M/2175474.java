package rgik;

import mil.army.usace.ehlschlaeger.rgik.core.GISLattice;
import oms3.annotations.Description;
import oms3.annotations.Execute;
import oms3.annotations.In;
import oms3.annotations.Out;
import oms3.annotations.Role;

public class GISLatticeLoader {

    @Description("Path and name of ESRI ASCII map file. (MANDATORY)")
    @Role(Role.PARAMETER)
    @In
    public String mapFile;

    @Description("Map data loaded from mapFile.")
    @Out
    public GISLattice latticeMap = null;

    @Execute
    public void execute() throws Exception {
        this.latticeMap = GISLattice.loadEsriAscii(mapFile);
        System.out.println(getClass() + ": map=" + latticeMap);
    }
}
