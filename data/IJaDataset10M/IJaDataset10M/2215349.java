package gov.ca.modeling.timeseries.map.shared.service;

import gov.ca.modeling.timeseries.map.shared.data.MapTextAnnotation;
import gov.ca.modeling.timeseries.map.shared.data.TimeSeriesData;
import gov.ca.modeling.timeseries.map.shared.data.TimeSeriesReferenceData;
import java.util.List;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.googlecode.objectify.Key;

@RemoteServiceRelativePath("service/data")
public interface DataService extends RemoteService {

    public List<TimeSeriesReferenceData> getReferencesForSourceLocationType(String source, String location, String type) throws ApplicationException;

    public TimeSeriesData getDataForReference(TimeSeriesReferenceData reference) throws ApplicationException;

    public List<TimeSeriesData> getDataForReferences(List<Key<TimeSeriesReferenceData>> references);

    public MapTextAnnotation addMapTextAnnotation(MapTextAnnotation annotation) throws ApplicationException;

    public void removeMapTextAnnotation(Key<MapTextAnnotation> key) throws ApplicationException;

    MapTextAnnotation getMapTextAnnotation(Key<MapTextAnnotation> key);

    List<MapTextAnnotation> getMapTextAnnotations(List<Key<MapTextAnnotation>> keyList);
}
