package genomancer.tengcha;

import genomancer.trellis.das2.model.*;
import genomancer.vine.das2.client.modelimpl.Das2FeaturesResponse;
import genomancer.vine.das2.client.modelimpl.Das2GenericCapability;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.gmod.gbol.simpleObject.Analysis;
import org.gmod.gbol.simpleObject.CVTerm;
import org.gmod.gbol.simpleObject.Feature;
import org.gmod.gbol.simpleObject.FeatureLocation;
import org.gmod.gbol.simpleObject.Organism;
import org.gmod.gbol.simpleObject.io.impl.HibernateHandler;

public class FeaturesCapability extends Das2GenericCapability implements Das2FeaturesCapabilityI {

    URI response_base_uri = null;

    HibernateHandler handler = null;

    Das2TypesCapabilityI types_cap;

    Das2SegmentsCapabilityI segs_cap;

    List<Analysis> analyses = new ArrayList();

    Das2TypesResponseI types_response;

    Das2SegmentsResponseI segs_response;

    URI base_uri = this.getAbsoluteURI().resolve("./");

    List<Das2FeatureI> features;

    long last_modified_time;

    public FeaturesCapability(Das2VersionI version, Das2CoordinatesI coords) {
        super(version.getBaseURI(), (version.getLocalURIString() + "/features"), "features", version, coords);
        System.out.println("called genomancer.tengcha.FeaturesCapability() constructor");
        response_base_uri = this.getAbsoluteURI().resolve("./");
        Das2VersionI das2_version = version;
        handler = ((TengchaDas2Version) this.getVersion()).getHibernateHandler();
        System.out.println("finished call to  genomancer.tengcha.FeaturesCapability() constructor");
    }

    protected Das2SegmentsCapabilityI getSegmentsCapability() {
        if (segs_cap == null) {
            segs_cap = (Das2SegmentsCapabilityI) version.getCapability("segments");
        }
        return segs_cap;
    }

    protected Das2TypesCapabilityI getTypesCapability() {
        if (types_cap == null) {
            types_cap = (Das2TypesCapabilityI) version.getCapability("types");
        }
        return types_cap;
    }

    protected List<Analysis> getAllAnalyses() {
        if (analyses.isEmpty()) {
            String orgName = getOrganismFromURI(base_uri);
            Organism thisOrg = handler.getOrganismByAbbreviation(orgName);
            for (Iterator<? extends Analysis> analysesIterator = handler.getAnalysesForOrganism(thisOrg); analysesIterator.hasNext(); ) {
                Analysis thisAnalysis = analysesIterator.next();
                analyses.add(thisAnalysis);
            }
        }
        return analyses;
    }

    protected Analysis getAnalysisFromType(Das2TypeI type) {
        Analysis thisAnalysis = null;
        List<Analysis> allAnalyses = this.getAllAnalyses();
        for (int i = 0; i < allAnalyses.size(); i++) {
            Analysis thisA = allAnalyses.get(i);
            String thisAURI;
            try {
                thisAURI = URLEncoder.encode(thisA.getProgram() + "_" + thisA.getProgramVersion() + "_" + thisA.getSourceName(), "UTF-8");
                if (thisAURI.equals(type.getLocalURIString())) {
                    thisAnalysis = thisA;
                    break;
                }
            } catch (Exception E) {
            }
        }
        return thisAnalysis;
    }

    protected String getOrganismFromURI(URI thisUri) {
        String[] items = thisUri.toString().split("/");
        String lastItem = items[items.length - 1];
        return lastItem;
    }

    protected String getSegmentNameFromURI(URI thisUri) {
        String[] items = thisUri.toString().split("/");
        String lastItem = items[items.length - 1];
        return lastItem;
    }

    @Override
    public long getLastModified(Das2FeaturesQueryI query) {
        return last_modified_time;
    }

    /**
     *  Assumes one (and only one) segment in query
     *  Assumes one (and only one) type in query
     */
    @Override
    public Das2FeaturesResponseI getFeatures(Das2FeaturesQueryI query) {
        System.out.println("called FeaturesCapability.getFeatures()");
        features = new ArrayList<Das2FeatureI>();
        URI typeuri;
        if (query.getTypes().size() > 0) {
            typeuri = query.getTypes().get(0);
            TypesCapability typescap = (TypesCapability) version.getCapability("types");
            Das2TypeI type = typescap.getType(typeuri);
            System.out.println("in FeaturesCapability.getFeatures(), querying for feature type: " + type.getLocalURIString());
            Das2LocationRefI loc = query.getOverlaps().get(0);
            URI requestedSegmentUri = loc.getSegmentURI();
            Das2SegmentI segment = (Das2SegmentI) this.getSegmentsCapability().getSegment(requestedSegmentUri);
            Feature segmentFeature = null;
            FeatureLocation floc = new FeatureLocation();
            try {
                String orgName = getOrganismFromURI(base_uri);
                Organism thisOrg = handler.getOrganismByAbbreviation(orgName);
                String refSoTermString = Config.REFERENCE_SEQUENCE_SO_TERM;
                String refCvTermString = Config.REFERENCE_SEQUENCE_CV_NAME;
                CVTerm refCvTerm = handler.getCVTerm(refSoTermString, refCvTermString);
                URI refURI = requestedSegmentUri;
                String refUniquename = this.getSegmentNameFromURI(refURI);
                segmentFeature = handler.getFeature(thisOrg, refCvTerm, refUniquename);
                floc.setFmin(loc.getMin());
                floc.setFmax(loc.getMax());
                floc.setSourceFeature(segmentFeature);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            int feat_count = 0;
            int child_count = 0;
            Analysis thisAnalysis = getAnalysisFromType(type);
            System.out.println("+++ in types loop, analysis item: " + thisAnalysis.getName());
            System.out.println("=== typesloop type: " + type.getTitle());
            try {
                for (Iterator<? extends Feature> featureIter = handler.getTopLevelFeaturesByOverlappingRangeAndAnalysis(floc, thisAnalysis); featureIter.hasNext(); ) {
                    Feature gbolFeat = featureIter.next();
                    Das2FeatureI f = GbolFeatureConverter.convertGbolFeature(gbolFeat, segmentFeature, segment, type);
                    feat_count++;
                    features.add(f);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.out.println("/// found " + feat_count + " features so far");
        }
        Das2FeaturesResponse response = new Das2FeaturesResponse(response_base_uri, features, null, true, true);
        return response;
    }

    @Override
    public int getFeaturesCount(Das2FeaturesQueryI query) {
        return -1;
    }

    @Override
    public boolean supportsCountFormat() {
        return false;
    }

    @Override
    public List<String> getFeaturesURI(Das2FeaturesQueryI query) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean supportsUriFormat() {
        return false;
    }

    @Override
    public InputStream getFeaturesAlternateFormat(Das2FeaturesQueryI query) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Class getFeatureClassForType(Das2TypeI type) {
        return Das2FeatureI.class;
    }

    @Override
    public int getMaxHierarchyDepth(Das2TypeI type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean supportsFullQueryFilters() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
