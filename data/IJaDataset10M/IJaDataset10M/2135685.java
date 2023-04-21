package plugin_example;

import genomancer.trellis.das2.model.Das2CoordinatesI;
import genomancer.trellis.das2.model.Das2LocationI;
import genomancer.trellis.das2.model.Das2SegmentI;
import genomancer.trellis.das2.model.Das2SegmentsCapabilityI;
import genomancer.trellis.das2.model.Das2SegmentsResponseI;
import genomancer.trellis.das2.model.Das2VersionI;
import genomancer.vine.das2.client.modelimpl.Das2GenericCapability;
import genomancer.vine.das2.client.modelimpl.Das2Segment;
import genomancer.vine.das2.client.modelimpl.Das2SegmentsResponse;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class SegmentsCapabilityExample extends Das2GenericCapability implements Das2SegmentsCapabilityI {

    Das2SegmentsResponse response = null;

    public SegmentsCapabilityExample(Das2VersionI version, Das2CoordinatesI coords) {
        super(version.getBaseURI(), (version.getLocalURIString() + "/segments"), "segments", version, coords);
        URI seq_base_uri = this.getAbsoluteURI().resolve("./");
        List<Das2SegmentI> segments = new ArrayList<Das2SegmentI>();
        for (int i = 1; i <= 5; i++) {
            int chrom_length = i * 100000;
            Das2Segment segment = new Das2Segment(seq_base_uri, "chrom" + i, "Chromosome " + i, null, chrom_length, null);
            segments.add(segment);
        }
        response = new Das2SegmentsResponse(seq_base_uri, segments, null, null);
    }

    public Das2SegmentsResponseI getSegments() {
        return response;
    }

    public Das2SegmentI getSegment(URI segment_uri) {
        return response.getSegment(segment_uri);
    }

    public String getResidues(Das2LocationI location) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getResidues(List<Das2LocationI> locations) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
