package ch.skyguide.tools.requirement.hmi.action;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import ch.skyguide.tools.requirement.hmi.RepositoryManager;
import ch.skyguide.tools.requirement.hmi.openoffice.FilterEnum;

@SuppressWarnings("serial")
public class ExportToPdfAction extends AbstractExportAction {

    public ExportToPdfAction(RepositoryManager _repositoryManager) {
        super("export", FilterEnum.PDF, _repositoryManager);
    }

    @Override
    protected String getIconFileName() {
        return "PDF.png";
    }

    @Override
    protected KeyStroke getAccelerator() {
        return KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK);
    }

    @Override
    protected String getFormatExtension() {
        return "pdf";
    }
}
