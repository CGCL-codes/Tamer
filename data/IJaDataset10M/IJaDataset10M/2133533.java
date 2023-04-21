package edu.washington.starbus;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import edu.washington.assist.animation.EventSeries;
import edu.washington.assist.animation.MutuallyExclusiveIntervalSeries;
import edu.washington.assist.animation.TimeInterval;
import edu.washington.assist.animation.TimedEvent;
import edu.washington.assist.audio.AudioInterval;
import edu.washington.assist.audio.AudioStream;
import edu.washington.assist.audio.AudioStreamFactory;
import edu.washington.assist.report.ActivityEvent;
import edu.washington.assist.report.AudioEvent;
import edu.washington.assist.report.ClusterEvent;
import edu.washington.assist.report.CommentEvent;
import edu.washington.assist.report.CompassEvent;
import edu.washington.assist.report.Conversation;
import edu.washington.assist.report.GPSTrace;
import edu.washington.assist.report.KeywordEvent;
import edu.washington.assist.report.PhotoEvent;
import edu.washington.assist.report.RawDataSeries;
import edu.washington.assist.report.Report;
import edu.washington.assist.report.VideoEvent;
import edu.washington.mysms.server.sample.starbus.GPSPoint;
import edu.washington.mysms.server.sample.starbus.RouteArchetype;

public class RouteArchetypeReport implements Report {

    private long archetype_id;

    private RouteArchetype archetype;

    public RouteArchetypeReport(long archetype_id, RouteArchetype archetype) {
        this.archetype_id = archetype_id;
        this.archetype = archetype;
    }

    @Override
    public TimeInterval getTimeInterval() {
        return new TimeInterval(0, archetype.get(archetype.size() - 1).getTime().getTime());
    }

    @Override
    public GPSTrace getTrace() {
        GPSTrace trace = new GPSTrace();
        for (GPSPoint p : archetype) {
            trace.addEvent(StarbusDataLoader.convertGPSPoint(p));
        }
        return trace;
    }

    @Override
    public long getID() {
        return archetype_id;
    }

    @Override
    public void saveComment(TimedEvent event, String text) {
    }

    @Override
    public EventSeries<ActivityEvent> getActivities() {
        return new EventSeries<ActivityEvent>();
    }

    @Override
    public AudioStreamFactory getAudio(TimeInterval interval) {
        return new AudioStreamFactory() {

            @Override
            public AudioStream getCopyOfStream() {
                return new DummyAudioStream();
            }

            class DummyAudioStream extends AudioStream {

                public DummyAudioStream() {
                    super(new ByteArrayInputStream(new byte[0]), 0);
                }
            }
        };
    }

    @Override
    public byte[] getAudioData(TimeInterval interval) {
        return new byte[0];
    }

    @Override
    public byte[] getAudioData(long when) {
        return new byte[0];
    }

    @Override
    public EventSeries<AudioEvent> getAudioEvents() {
        return new EventSeries<AudioEvent>();
    }

    @Override
    public File getAudioFile() {
        return null;
    }

    @Override
    public MutuallyExclusiveIntervalSeries<AudioInterval> getAudioTimes() {
        return new MutuallyExclusiveIntervalSeries<AudioInterval>();
    }

    @Override
    public String getComment(TimedEvent event) {
        return "";
    }

    @Override
    public EventSeries<CommentEvent> getComments() {
        return new EventSeries<CommentEvent>();
    }

    @Override
    public EventSeries<CompassEvent> getCompass() {
        return new EventSeries<CompassEvent>();
    }

    @Override
    public EventSeries<Conversation> getConversations() {
        return new EventSeries<Conversation>();
    }

    @Override
    public String getDescription() {
        StringBuffer buf = new StringBuffer();
        buf.append("Archetype \"");
        buf.append(archetype.getArchetypeName());
        buf.append("\" for route #");
        buf.append(archetype.getRoute().getRouteNumber());
        buf.append(": ");
        buf.append(archetype.getRoute().getRouteName());
        return buf.toString();
    }

    @Override
    public Map<String, Integer> getFeatureSet() {
        return new HashMap<String, Integer>();
    }

    @Override
    public Image getImage(long when) {
        return new BufferedImage(0, 0, BufferedImage.TYPE_CUSTOM);
    }

    @Override
    public byte[] getImageData(long when) {
        return new byte[0];
    }

    @Override
    public List<EventSeries<ClusterEvent>> getKClusters() {
        return new ArrayList<EventSeries<ClusterEvent>>(0);
    }

    @Override
    public EventSeries<ClusterEvent> getKClusters(int value, int featureSet) {
        return new EventSeries<ClusterEvent>();
    }

    @Override
    public EventSeries<KeywordEvent> getKeywords() {
        return new EventSeries<KeywordEvent>();
    }

    @Override
    public int getMaxKValue(int featureSet) {
        return 0;
    }

    @Override
    public Long getMissionID() {
        return new Long(0);
    }

    @Override
    public long getMsbStartTime() {
        return 0;
    }

    @Override
    public EventSeries<PhotoEvent> getPhotoTimes(boolean internal) {
        return new EventSeries<PhotoEvent>();
    }

    @Override
    public RawDataSeries getRawData(String seriesName) {
        return new RawDataSeries();
    }

    @Override
    public EventSeries<VideoEvent> getVideoTimes() {
        return new EventSeries<VideoEvent>();
    }
}
