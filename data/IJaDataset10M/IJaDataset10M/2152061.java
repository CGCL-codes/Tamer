package prequips.inclusionListBuilder.export;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import prequips.inclusionListBuilder.features.InclusionList;
import prequips.inclusionListBuilder.segments.Segment;

public interface InclusionListExporter {

    public void setInclusionList(InclusionList inclusionList);

    public InclusionList getInclusionList();

    public void setSegmentSelection(ArrayList<Segment> selection);

    public ArrayList<Segment> getSegmentSelection();

    public void setWriteLog(boolean writeLog);

    public boolean doesWriteLog();

    public void setListUri(URI uri);

    public URI getListUri();

    public void setLogUri(URI uri);

    public URI getLogUri();

    public int export() throws IOException;
}
