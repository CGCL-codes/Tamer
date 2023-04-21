package backend.parser.genericobo.goSpecific;

import backend.core.AbstractONDEXGraph;
import backend.core.security.Session;
import backend.event.type.CVMissing;
import backend.parser.genericobo.ReferenceContainer;
import backend.parser.genericobo.MetaData;
import backend.parser.genericobo.goSpecific.StringMod;

public class GOReferenceContainer extends ReferenceContainer {

    public GOReferenceContainer(AbstractONDEXGraph graph, Session s) {
        super(graph, s);
    }

    @Override
    public void analyseXRef() {
        if (xRefString.startsWith("EC:")) {
            ambigous = false;
            CVAccession = graph.getONDEXGraphData(s).getCV(s, MetaData.cvEC);
            write = true;
            xRef = xRefString.substring(3);
            xRef = StringMod.fillEC(xRef);
        } else if (xRefString.startsWith("TC:")) {
            ambigous = false;
            CVAccession = graph.getONDEXGraphData(s).getCV(s, MetaData.cvTC);
            write = true;
            xRef = xRefString.substring(3, xRefString.length());
        } else if (xRefString.startsWith("MetaCyc:")) {
            ambigous = false;
            CVAccession = graph.getONDEXGraphData(s).getCV(s, MetaData.cvMC);
            write = true;
            xRef = xRefString.substring(8, xRefString.length());
        } else if (xRefString.startsWith("RESID:")) {
            ambigous = true;
            CVAccession = graph.getONDEXGraphData(s).getCV(s, MetaData.cvRESID);
            write = true;
            xRef = xRefString.substring(6, xRefString.length());
        } else if (xRefString.startsWith("UM-BBD_enzymeID:")) {
            ambigous = false;
            CVAccession = graph.getONDEXGraphData(s).getCV(s, MetaData.cvUME);
            write = true;
            xRef = xRefString.substring(16, xRefString.length());
        } else if (xRefString.startsWith("UM-BBD_pathwayID:")) {
            ambigous = false;
            CVAccession = graph.getONDEXGraphData(s).getCV(s, MetaData.cvUMP);
            write = true;
            xRef = xRefString.substring(17, xRefString.length());
        } else if (xRefString.startsWith("Reactome:")) {
            ambigous = false;
            CVAccession = graph.getONDEXGraphData(s).getCV(s, MetaData.cvREAC);
            write = true;
            xRef = xRefString.substring(9, xRefString.length());
        } else {
            graph.fireEventOccurred(new CVMissing("Database \"" + xRefString + "\" referenced from xref field in GO file unknown!"));
        }
    }
}
