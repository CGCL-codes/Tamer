package org.spantus.work.ui.cmd;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Set;
import org.spantus.exception.ProcessingException;
import org.spantus.work.ui.dto.SpantusWorkInfo;

/**
 * 
 * @author mondhs
 * @since 2010 04 11
 */
public class AppendNoiseCmd extends OpenCmd {

    public AppendNoiseCmd(CommandExecutionFacade executionFacade) {
        super(executionFacade);
    }

    @Override
    public Set<String> getExpectedActions() {
        return createExpectedActions(GlobalCommands.tool.appendNoise);
    }

    @Override
    protected void setSelectedFile(SpantusWorkInfo ctx, File selectedFile) {
        try {
            ctx.getProject().getSample().setNoiseFile(selectedFile.toURI().toURL());
        } catch (MalformedURLException e1) {
            throw new ProcessingException(e1);
        }
    }
}
