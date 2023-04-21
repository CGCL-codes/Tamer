package playground.ou.singleintersectioncontrol;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import org.matsim.core.gbl.Gbl;
import org.matsim.core.network.NetworkLayer;
import playground.ou.signalcontrolnetwork.StandardIntersection;

public class CycleParameterWriter {

    private BufferedWriter out;

    public CycleParameterWriter(String filename) {
        try {
            out = new BufferedWriter(new FileWriter(filename));
        } catch (IOException e) {
            Gbl.errorMsg(e);
        }
    }

    public void write(NetworkLayer network, StandardIntersection intersection) {
        switch(intersection.getIntersectionType()) {
            case 1:
                TwoPhaseTShapeControl tshapetwophasecontrol = new TwoPhaseTShapeControl(network, intersection);
                tshapewrite(out, intersection, tshapetwophasecontrol);
                break;
            case 2:
                TwoPhaseCrossControl twophasecrosscontrol = new TwoPhaseCrossControl(network, intersection);
                crosswrite(out, intersection, twophasecrosscontrol);
                break;
            case 3:
                FiveormorearmControl fiveormorearmcontrol = new FiveormorearmControl(network, intersection);
                fiveormorearmwrite(out, intersection, fiveormorearmcontrol);
                break;
            default:
                break;
        }
    }

    public void endwrite() {
        try {
            out.close();
        } catch (IOException e) {
            Gbl.errorMsg(e);
        }
    }

    public void fiveormorearmwrite(BufferedWriter out, StandardIntersection intersection, FiveormorearmControl fiveormorearmcontrol) {
        try {
            out.write("intersection ");
            out.write(intersection.getId().toString());
            out.write("\t");
            for (int i = 0; i < fiveormorearmcontrol.getRoundaboutLink().size(); i++) {
                out.write("Link ");
                out.write(Integer.toString(i));
                out.write("=");
                out.write(fiveormorearmcontrol.getRoundaboutLink().get(i).getId().toString());
            }
            out.write("  CycleStart=");
            out.write(Integer.toString(intersection.gettimeDatum_CycleStart()));
            out.write("  CycleLen=");
            out.write(Integer.toString(intersection.getCycleLength()));
            out.write("  Offset=");
            out.write(Integer.toString(intersection.getCycleOffset()));
            out.write("  PhaseAGreen=");
            out.write(Integer.toString(intersection.getPhaseAGreen()));
            out.write("\n");
        } catch (IOException e) {
            Gbl.errorMsg(e);
        }
    }

    public void crosswrite(BufferedWriter out, StandardIntersection intersection, TwoPhaseCrossControl twophasecrosscontrol) {
        try {
            out.write("intersection ");
            out.write(intersection.getId().toString());
            out.write("\t");
            if (twophasecrosscontrol.getCrosslinks().getNorthLink() != null) {
                out.write("NorthLink=");
                out.write(twophasecrosscontrol.getCrosslinks().getNorthLink().getId().toString());
            }
            if (twophasecrosscontrol.getCrosslinks().getSouthLink() != null) {
                out.write("  SouthLink=");
                out.write(twophasecrosscontrol.getCrosslinks().getSouthLink().getId().toString());
            }
            if (twophasecrosscontrol.getCrosslinks().getWestLink() != null) {
                out.write("  WestLink=");
                out.write(twophasecrosscontrol.getCrosslinks().getWestLink().getId().toString());
            }
            if (twophasecrosscontrol.getCrosslinks().getEastLink() != null) {
                out.write("  EastLink=");
                out.write(twophasecrosscontrol.getCrosslinks().getEastLink().getId().toString());
            }
            out.write("  CycleStart=");
            out.write(Integer.toString(intersection.gettimeDatum_CycleStart()));
            out.write("  CycleLength=");
            out.write(Integer.toString(intersection.getCycleLength()));
            out.write("  Offset=");
            out.write(Integer.toString(intersection.getCycleOffset()));
            out.write("  PhaseAGreen=");
            out.write(Integer.toString(intersection.getPhaseAGreen()));
            out.write("\n");
        } catch (IOException e) {
            Gbl.errorMsg(e);
        }
    }

    public void tshapewrite(BufferedWriter out, StandardIntersection intersection, TwoPhaseTShapeControl tshapetwophasecontrol) {
        try {
            out.write("intersection ");
            out.write(intersection.getId().toString());
            if (tshapetwophasecontrol.getTShapeLink().getLeftLink() != null) {
                out.write("  LeftLink=");
                out.write(tshapetwophasecontrol.getTShapeLink().getLeftLink().getId().toString());
            }
            if (tshapetwophasecontrol.getTShapeLink().getRightLink() != null) {
                out.write("  RightLink=");
                out.write(tshapetwophasecontrol.getTShapeLink().getRightLink().getId().toString());
            }
            if (tshapetwophasecontrol.getTShapeLink().getDownLink() != null) {
                out.write("  DownLink=");
                out.write(tshapetwophasecontrol.getTShapeLink().getDownLink().getId().toString());
            }
            out.write("  CycleStart=");
            out.write(Integer.toString(intersection.gettimeDatum_CycleStart()));
            out.write("  CycleLength");
            out.write(Integer.toString(intersection.getCycleLength()));
            out.write("  Offset=");
            out.write(Integer.toString(intersection.getCycleOffset()));
            out.write("  PhaseAGreen=");
            out.write(Integer.toString(intersection.getPhaseAGreen()));
            out.write("\n");
        } catch (IOException e) {
            Gbl.errorMsg(e);
        }
    }
}
