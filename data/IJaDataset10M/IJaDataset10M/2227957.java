package au.edu.uq.itee.eresearch.dimer.webapp.app.view.mets;

import org.jdom2.Namespace;

public class METSUtils {

    public static final Namespace mets = Namespace.getNamespace("mets", "http://www.loc.gov/METS/");

    public static final Namespace mods = Namespace.getNamespace("mods", "http://www.loc.gov/mods/v3");

    public static final Namespace premis = Namespace.getNamespace("premis", "http://www.loc.gov/standards/premis");

    public static final Namespace tardisDataset = Namespace.getNamespace("trdds", "http://www.tardis.edu.au/schemas/trdDataset/2");

    public static final Namespace tardisDatafile = Namespace.getNamespace("trddf", "http://www.tardis.edu.au/schemas/trdDatafile/1");
}
