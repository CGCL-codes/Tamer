package org.op.editor;

import java.io.File;
import java.util.List;
import javax.swing.text.Document;
import org.op.FrameView;
import org.op.nav.NavigationController;
import org.op.service.WwDocumentInfo;
import org.op.service.config.ConfigurationService;
import org.op.service.doc.DocumentConversionService;
import org.op.service.fileio.FileRWService;
import org.op.service.log.LoggerService;
import org.op.util.MessageController;

public class SnipToControllerImpl implements SnipToController {

    private EditorComponentView editorView;

    private DocumentConversionService docConverter;

    private FileRWService fileRWService;

    private NavigationController navController;

    private OpenCloseController openCloseController;

    private ConfigurationService configService;

    private MessageController messageController;

    private FrameView frameView;

    private LoggerService logger;

    private static final String ARCHIVE_FILE_NAME = "archive.txt";

    public void snipToArchive() {
        try {
            String text = cutSelectedTextFromEditor();
            List<String> snippedLines = docConverter.textToList(text);
            String folderPath = getFolderPathOfCurrentNote();
            String archivePath = folderPath + ConfigurationService.FILE_SEP + ARCHIVE_FILE_NAME;
            File file = new File(archivePath);
            if (file.exists()) {
                List<String> existingLines = fileRWService.readFileText(archivePath);
                snippedLines.addAll(existingLines);
            }
            fileRWService.writeFileText(archivePath, snippedLines);
            navController.reloadTree();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void snipToNew() {
        try {
            String text = cutSelectedTextFromEditor();
            List<String> snippedLines = docConverter.textToList(text);
            Document doc = docConverter.textToDocument(snippedLines);
            openCloseController.newDocument();
            editorView.getCurrentPane().setDocument(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getFolderPathOfCurrentNote() {
        WwDocumentInfo docInfo = editorView.getCurrentDocumentInfo();
        String filePath = docInfo.getFilepath();
        int folderIdx = filePath.lastIndexOf(ConfigurationService.FILE_SEP);
        String folderPath = filePath.substring(0, folderIdx);
        return folderPath;
    }

    private String cutSelectedTextFromEditor() {
        String text = editorView.getSelectedText();
        editorView.getCurrentPane().replaceSelection("");
        editorView.setCurrentDirty();
        return text;
    }

    public EditorComponentView getEditorView() {
        return editorView;
    }

    public void setEditorView(EditorComponentView editorView) {
        this.editorView = editorView;
    }

    public DocumentConversionService getDocConverter() {
        return docConverter;
    }

    public void setDocConverter(DocumentConversionService docConverter) {
        this.docConverter = docConverter;
    }

    public FileRWService getFileRWService() {
        return fileRWService;
    }

    public void setFileRWService(FileRWService fileRWService) {
        this.fileRWService = fileRWService;
    }

    public NavigationController getNavController() {
        return navController;
    }

    public void setNavController(NavigationController navController) {
        this.navController = navController;
    }

    public OpenCloseController getOpenCloseController() {
        return openCloseController;
    }

    public void setOpenCloseController(OpenCloseController openCloseController) {
        this.openCloseController = openCloseController;
    }

    public ConfigurationService getConfigService() {
        return configService;
    }

    public void setConfigService(ConfigurationService configService) {
        this.configService = configService;
    }

    public MessageController getMessageController() {
        return messageController;
    }

    public void setMessageController(MessageController messageController) {
        this.messageController = messageController;
    }

    public FrameView getFrameView() {
        return frameView;
    }

    public void setFrameView(FrameView frameView) {
        this.frameView = frameView;
    }

    public LoggerService getLogger() {
        return logger;
    }

    public void setLogger(LoggerService logger) {
        this.logger = logger;
    }
}
