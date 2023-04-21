package apollo.dataadapter;

import java.util.*;
import apollo.datamodel.*;

public interface KaryotypeAdapterI {

    public String getName();

    public String getType();

    public Vector getKaryotypes();

    public Karyotype getKaryotypeBySpeciesName(String name);
}
