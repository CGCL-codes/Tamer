package be.lassi.lanbox;

import static be.lassi.util.Util.newArrayList;
import java.util.List;
import be.lassi.cues.Cue;
import be.lassi.cues.CueCommandFactory;
import be.lassi.cues.Cues;
import be.lassi.cues.LightCueDetail;
import be.lassi.lanbox.commands.Command;
import be.lassi.lanbox.commands.layer.LayerGo;
import be.lassi.lanbox.cuesteps.Comment;
import be.lassi.lanbox.cuesteps.WaitLayer;
import be.lassi.lanbox.domain.CueList;
import be.lassi.lanbox.domain.Time;

public class Fade {

    private final int scratchPadCueListNumber = 20;

    /**
     * The state of the toggle between the two scratch pad cue lists.
     */
    private boolean toggle = false;

    public List<Command> go(final Cues cues) {
        List<Command> commands = newArrayList();
        Cue cue = cues.getCurrentCue();
        if (cue != null) {
            if (cue.isLightCue()) {
                int cueListNumber = scratchPadCueListNumber + (toggle ? 0 : 1);
                toggle = !toggle;
                LightCueDetail detail = (LightCueDetail) cue.getDetail();
                CueList cueList = new CueList(cueListNumber);
                cueList.add(new Comment("LASSI"));
                cueList.add(new Comment("SCRATCH" + (toggle ? 1 : 2)));
                cueList.add(detail.getCueSteps());
                cueList.add(new WaitLayer(Time.FOREVER));
                commands.addAll(new CueCommandFactory().getCommands(cueList));
                Command command = new LayerGo(Lanbox.ENGINE_SHEET, cueListNumber, 1);
                commands.add(command);
            }
        }
        return commands;
    }
}
