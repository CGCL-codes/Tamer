package playground.ou.intersectiongroup;

import java.util.Map;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import org.matsim.core.gbl.Gbl;
import playground.ou.singleintersectioncontrol.Shift;
import playground.ou.singleintersectioncontrol.TwoPhaseSchedule;

public class GroupSignalWriter {

    static final int RIGHTOFWAY = 0;

    static final int LOSINGRIGHTOFWAY = 1;

    static final int NORIGHTOFWAY = 2;

    static final int YELLOWBLINK = 1;

    static final int YELLOW = 3;

    static final int ALLRED = 0;

    private BufferedWriter out;

    private BufferedWriter outGtfs;

    public GroupSignalWriter(String filename, String filenameGtfs) {
        try {
            out = new BufferedWriter(new FileWriter(filename));
        } catch (IOException e) {
            Gbl.errorMsg(e);
        }
        try {
            outGtfs = new BufferedWriter(new FileWriter(filenameGtfs));
        } catch (IOException e) {
            Gbl.errorMsg(e);
        }
    }

    public BufferedWriter getFileHandle() {
        return this.out;
    }

    public BufferedWriter getFileHandleGtfs() {
        return this.outGtfs;
    }

    public void write(BigClusterSignalTiming bigClusterSignalTiming) {
        int cyclestart = bigClusterSignalTiming.time;
        int baseOffset = bigClusterSignalTiming.offset;
        int cyclelength = bigClusterSignalTiming.getCycleLength();
        Map<String, int[]> linksgreen = bigClusterSignalTiming.getLinksGreen();
        int[] time_para;
        for (String linkId : linksgreen.keySet()) {
            time_para = linksgreen.get(linkId);
            int phaseSignal[];
            phaseSignal = new TwoPhaseSchedule(cyclelength, baseOffset + time_para[1], time_para[0]).getPhaseASchedule();
            writelink(out, linkId, cyclestart, cyclelength, phaseSignal);
            writelinkGtfs(outGtfs, linkId, cyclestart, cyclelength, phaseSignal);
        }
    }

    public void write(SmallClusterSignalTiming groupSignalTiming) {
        int phaseSignal[];
        int cyclestart = groupSignalTiming.time;
        int cyclelength = groupSignalTiming.getCycleLength();
        Map<Integer, String> groupPhase = groupSignalTiming.getGroupLinkPhase().getGroupLinkPhaseNumber();
        for (int i : groupPhase.keySet()) {
            phaseSignal = getPhaseSignal(cyclelength, groupPhase, groupSignalTiming.getPhasesGreen(), i, groupSignalTiming.getPhaseCode());
            String linkId = groupPhase.get(i);
            if (linkId != null) {
                if (linkId.indexOf(",") >= 0) {
                    String[] sslink = linkId.split(",");
                    for (int x = 0; x < sslink.length; x++) {
                        writelink(out, sslink[x], cyclestart, cyclelength, phaseSignal);
                        writelinkGtfs(outGtfs, sslink[x], cyclestart, cyclelength, phaseSignal);
                    }
                } else {
                    writelink(out, linkId, cyclestart, cyclelength, phaseSignal);
                    writelinkGtfs(outGtfs, linkId, cyclestart, cyclelength, phaseSignal);
                }
            }
        }
    }

    public void startwrite() {
        try {
            out.write("LINKID\t");
            out.write("STARTTIME\t");
            out.write("DURATION\t");
            out.write("RightOfWay(0:green,1:yellow/yellowblink,2:red)");
            out.write("\t");
            out.write("\n");
        } catch (IOException e) {
            Gbl.errorMsg(e);
        }
        try {
            outGtfs.write("LINKID\t");
            outGtfs.write("FROMTIME\t");
            outGtfs.write("TOTIME\t\t");
            outGtfs.write("GTFS VAL");
            outGtfs.write("\t");
            outGtfs.write("\n");
        } catch (IOException e) {
            Gbl.errorMsg(e);
        }
    }

    public void endwrite() {
        try {
            out.close();
        } catch (IOException e) {
            Gbl.errorMsg(e);
        }
        try {
            outGtfs.close();
        } catch (IOException e) {
            Gbl.errorMsg(e);
        }
    }

    private int existExtendedPhase(int phaseIndex, Map<Integer, String> groupPhase) {
        int extendedPhase = 0;
        switch(phaseIndex) {
            case 1:
            case 3:
                if ((groupPhase.get(phaseIndex + 5) != null) && (groupPhase.get(phaseIndex + 1) == null) && (groupPhase.get(phaseIndex + 11) == null)) extendedPhase = phaseIndex + 1;
                break;
            case 5:
            case 7:
                if ((groupPhase.get(phaseIndex - 3) != null) && (groupPhase.get(phaseIndex + 1) == null) && (groupPhase.get(phaseIndex + 11) == null)) extendedPhase = phaseIndex + 1;
                break;
            case 2:
            case 4:
                if ((groupPhase.get(phaseIndex + 3) != null) && (groupPhase.get(phaseIndex - 1) == null) && (groupPhase.get(phaseIndex + 9) == null)) extendedPhase = phaseIndex - 1;
                break;
            case 6:
            case 8:
                if ((groupPhase.get(phaseIndex - 5) != null) && (groupPhase.get(phaseIndex - 1) == null) && (groupPhase.get(phaseIndex + 9) == null)) extendedPhase = phaseIndex - 1;
                break;
            default:
                break;
        }
        return extendedPhase;
    }

    private int[] getPhaseSignal(int cyclelength, Map<Integer, String> groupPhase, int phaseGreen[], int index, int phasecode[]) {
        int[] phase;
        phase = new int[cyclelength];
        if (phaseGreen[0] == cyclelength) {
            for (int i = 0; i < cyclelength; i++) phase[i] = YELLOWBLINK;
        } else {
            int greenStart = 0;
            int greenDuration = 0;
            int phasenum = 0;
            int j = index;
            if (index >= 20) j = index - 20; else if (index >= 10) j = index - 10;
            for (int x = 0; x < phaseGreen.length; x++) {
                if ((j == phasecode[x]) || (j == phasecode[x] + 4)) {
                    phasenum = x;
                    greenDuration += phaseGreen[phasenum];
                }
            }
            int extendPhase = existExtendedPhase(j, groupPhase);
            if (extendPhase > 0) {
                for (int x = 0; x < phaseGreen.length; x++) {
                    if ((extendPhase == phasecode[x]) || (extendPhase == phasecode[x] + 4)) {
                        if (extendPhase > 4) {
                            if (extendPhase > j) phasenum = j - 5; else if (extendPhase < j) phasenum = extendPhase - 5;
                        } else {
                            if (extendPhase > j) phasenum = j - 1; else if (extendPhase < j) phasenum = extendPhase - 1;
                        }
                        greenDuration += phaseGreen[x];
                    }
                }
            }
            if (phasenum >= 0) for (int i = 0; i < phasenum; i++) greenStart += phaseGreen[i] + YELLOW + ALLRED;
            for (int i = 0; i < greenDuration; i++) {
                phase[i] = RIGHTOFWAY;
            }
            for (int i = greenDuration; i < greenDuration + YELLOW + ALLRED; i++) {
                phase[i] = LOSINGRIGHTOFWAY;
            }
            for (int i = greenDuration + YELLOW + ALLRED; i < cyclelength; i++) {
                phase[i] = NORIGHTOFWAY;
            }
            phase = new Shift().shift(greenStart, phase);
        }
        return phase;
    }

    private void writelink(BufferedWriter out, String linkId, int cyclestart, int cyclelength, int[] phase) {
        int value = phase[0];
        int duration = 0;
        int time = 0;
        int starttime = cyclestart;
        while (time < cyclelength) {
            if ((value != phase[time]) || (time == (cyclelength - 1))) {
                if (time == (phase.length - 1)) duration++;
                writeline(out, linkId, starttime, duration, value);
                duration = 1;
                starttime = cyclestart + time;
                value = phase[time];
            } else {
                duration++;
            }
            time++;
        }
    }

    private void writelinkGtfs(BufferedWriter out, String linkId, int cyclestart, int cyclelength, int[] phase) {
        int duration = 0;
        int starttime = cyclestart;
        int inteval = 0;
        for (int x = 0; x < phase.length; x++) {
            if (phase[x] == 0) duration++;
            if (phase[x] == 1) inteval++;
        }
        duration = duration + inteval;
        try {
            out.write(linkId + "\t");
            out.write(Integer.toString(starttime) + "\t");
            out.write(Integer.toString(starttime + phase.length - 1) + "\t");
            out.write(Double.toString(duration / (double) phase.length));
            out.write("\n");
        } catch (IOException e) {
            Gbl.errorMsg(e);
        }
    }

    private void writeline(BufferedWriter out, String linkId, int starttime, int duration, int rightofway) {
        try {
            out.write(linkId);
            out.write("\t");
            out.write(Integer.toString(starttime));
            out.write("\t");
            out.write(Integer.toString(duration));
            out.write("\t");
            out.write(Integer.toString(rightofway));
            out.write("\n");
        } catch (IOException e) {
            Gbl.errorMsg(e);
        }
    }
}
