package us.wthr.jdem846.gis;

import us.wthr.jdem846.exception.WKTParseException;
import us.wthr.jdem846.gis.exceptions.ProjectionInformationParseException;
import us.wthr.jdem846.wkt.WKTReader;

public class ProjectionInformationFileLoader {

    protected ProjectionInformationFileLoader(WKTReader wktReader) {
    }

    public static void load(String url) throws ProjectionInformationParseException {
        WKTReader wktReader = null;
        try {
            wktReader = WKTReader.load(url);
        } catch (WKTParseException ex) {
            throw new ProjectionInformationParseException("Failed to parse projection information file: " + ex.getMessage(), ex);
        }
        ProjectionInformationFileLoader projLoader = new ProjectionInformationFileLoader(wktReader);
    }
}
