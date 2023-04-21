package apollo.dataadapter.ensj.controller;

import java.util.*;
import apollo.dataadapter.ensj.*;
import apollo.dataadapter.ensj.model.*;
import apollo.dataadapter.ensj.view.*;
import java.util.regex.*;

/**
 * The user has selected a seq-region from the location history dropdown. We
 * populate the coordsystem & seq-region & start=stop fields accordingly.
**/
public class LoadSeqRegionByHistoryHandler extends EventHandler {

    public static final String PATTERN = "([^--]*)--(.*):(.*):(.*)-(.*)";

    Pattern p = Pattern.compile("(\\S+)\\.(\\S+)");

    public LoadSeqRegionByHistoryHandler(Controller controller, String key) {
        super(controller, key);
    }

    public void doAction(Model model) {
        doUpdate();
        LocationModel myModel = model.getLocationModel();
        log("Loading seq region from history: selected" + myModel.getSelectedHistoryLocation());
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(myModel.getSelectedHistoryLocation());
        String version;
        String coordSystem;
        String seqRegion;
        String start;
        String end;
        if (!matcher.matches()) {
            throw new NonFatalException("The history item doesn't correspond to the allowed pattern: " + "<version>--<coordsystem>:<seqregion>:<start>-<end>");
        }
        version = matcher.group(1);
        coordSystem = matcher.group(2);
        seqRegion = matcher.group(3);
        start = matcher.group(4);
        end = matcher.group(5);
        log("Matched to version : " + version + ", and coord system: " + coordSystem + " seq region: " + seqRegion + " start: " + start + " end: " + end);
        if (isNull(version) || isNull(coordSystem) || isNull(seqRegion)) {
            throw new NonFatalException("The chosen version and coord system and seq region " + myModel.getSelectedHistoryLocation() + " can't be resolved " + " into a separate, non-null version and coord system string");
        }
        if (!myModel.isCoordSystemInitialised()) {
            log("Coord systems not initialised - finding and populating");
            myModel.setCoordSystems(findCoordinateSystems(getConnectionForModel(model)));
            myModel.setCoordSystemInitialised(true);
            myModel.setSeqRegions(new ArrayList());
            myModel.setSeqRegionInitialised(false);
        }
        myModel.setSelectedCoordSystem(version + "--" + coordSystem);
        if (!myModel.isSeqRegionInitialised()) {
            myModel.setSeqRegions(findSeqRegions(model));
        }
        myModel.setSelectedSeqRegion(seqRegion);
        myModel.setSeqRegionInitialised(true);
        myModel.setStart(start);
        myModel.setEnd(end);
        myModel.setStableIDLocation(false);
        doRead();
    }
}
