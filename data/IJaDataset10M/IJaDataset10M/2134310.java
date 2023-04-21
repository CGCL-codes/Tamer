package jfreerails.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import jfreerails.world.common.GameTime;
import jfreerails.world.player.FreerailsPrincipal;
import jfreerails.world.top.KEY;
import jfreerails.world.top.ReadOnlyWorld;
import jfreerails.world.track.TrackSection;
import jfreerails.world.train.TrainModel;

public class OccupiedTracks {

    public Map<TrackSection, Integer> occupiedTrackSections;

    public Map<Integer, List<TrackSection>> trainToTrackList;

    public OccupiedTracks(FreerailsPrincipal principal, ReadOnlyWorld w) {
        occupiedTrackSections = new HashMap<TrackSection, Integer>();
        trainToTrackList = new HashMap<Integer, List<TrackSection>>();
        for (int i = 0; i < w.size(principal, KEY.TRAINS); i++) {
            TrainModel train = (TrainModel) w.get(principal, KEY.TRAINS, i);
            if (null == train) continue;
            TrainAccessor ta = new TrainAccessor(w, principal, i);
            GameTime gt = w.currentTime();
            if (ta.isMoving(gt.getTicks())) {
                HashSet<TrackSection> sections = ta.occupiedTrackSection(gt.getTicks());
                List<TrackSection> trackList = new ArrayList<TrackSection>(sections);
                trainToTrackList.put(i, trackList);
                for (TrackSection section : sections) {
                    Integer count = occupiedTrackSections.get(section);
                    if (count == null) {
                        occupiedTrackSections.put(section, 1);
                    } else {
                        count++;
                        occupiedTrackSections.put(section, count);
                    }
                }
            }
        }
    }

    public void stopTrain(int i) {
        List<TrackSection> trackList = trainToTrackList.remove(i);
        if (trackList == null) {
            return;
        }
        for (TrackSection section : trackList) {
            Integer count = occupiedTrackSections.get(section);
            if (count > 0) {
                count--;
                occupiedTrackSections.put(section, count);
            }
        }
    }
}
