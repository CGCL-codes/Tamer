package org.spantus.work.ui.cmd;

import java.io.File;
import java.text.MessageFormat;
import java.util.Set;
import javax.swing.JOptionPane;
import org.spantus.core.marker.Marker;
import org.spantus.core.marker.MarkerSet;
import org.spantus.core.marker.MarkerSetHolder.MarkerSetHolderEnum;
import org.spantus.logger.Logger;
import org.spantus.work.ui.dto.SpantusWorkInfo;
import org.spantus.core.wav.AudioManagerFactory;

/**
 * 
 * @author Mindaugas Greibus
 * @since 0.0.1
 * Created on Feb 22, 2009
 */
public class SaveSegmentCmd extends AbsrtactCmd {

    public static final String segmentSavedPanelMessageHeader = "segmentSavedPanelMessageHeader";

    public static final String segmentSavedPanelMessageBody = "segmentSavedPanelMessageBody";

    protected Logger log = Logger.getLogger(getClass());

    public SaveSegmentCmd(CommandExecutionFacade executionFacade) {
        super(executionFacade);
    }

    public Set<String> getExpectedActions() {
        return createExpectedActions(GlobalCommands.tool.saveSegments);
    }

    @Override
    public String execute(SpantusWorkInfo ctx) {
        String pathToSaveFormat = ctx.getProject().getFeatureReader().getWorkConfig().getAudioPathOutput() + "/{0}_{1}.wav";
        MarkerSet words = ctx.getProject().getSample().getMarkerSetHolder().getMarkerSets().get(MarkerSetHolderEnum.word.name());
        for (Marker marker : words.getMarkers()) {
            String path = MessageFormat.format(pathToSaveFormat, ctx.getProject().getExperimentId(), marker.getLabel());
            AudioManagerFactory.createAudioManager().save(ctx.getProject().getSample().getCurrentFile(), marker.getStart() / 1000f, marker.getLength() / 1000f, path);
        }
        String pathToSave = new File(ctx.getProject().getFeatureReader().getWorkConfig().getAudioPathOutput()).getAbsolutePath();
        pathToSave += "/" + ctx.getProject().getExperimentId() + "_*.wav";
        showMessage(words, pathToSave, ctx);
        return null;
    }

    protected void showMessage(MarkerSet words, String pathToSave, SpantusWorkInfo ctx) {
        String messageFormat = getMessage(segmentSavedPanelMessageBody);
        String messageBody = MessageFormat.format(messageFormat, words.getMarkers().size(), pathToSave);
        log.info(messageBody);
        if (Boolean.TRUE.equals(ctx.getEnv().getPopupNotifications())) {
            JOptionPane.showMessageDialog(null, messageBody, getMessage(segmentSavedPanelMessageHeader), JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
