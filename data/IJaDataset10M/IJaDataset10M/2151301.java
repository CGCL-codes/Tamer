package org.op.looks;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import org.op.FrameView;
import org.op.service.config.ConfigKeys;
import org.op.service.config.ConfigurationService;
import org.op.service.ui.SkinnableUiServiceImpl;
import org.op.service.ui.UiService;
import org.op.service.ui.UiServiceImpl;
import org.op.util.MessageController;

public class SkinLafSkinControllerImpl implements SkinController {

    private FrameView frameView;

    private ConfigurationService configService;

    private UiService uiService;

    private MessageController messageController;

    private UiServiceImpl skinnableService;

    public void configureSkin() {
        skinnableService = (SkinnableUiServiceImpl) uiService;
        String existingThemePath = configService.getProperty(ConfigKeys.KEY_SKIN);
        String userHome = System.getProperty("user.home");
        String fileSep = System.getProperty("file.separator");
        String appName = configService.getProperty(ConfigKeys.APP_NAME);
        String themeFolder = "themes";
        String themeFolderPath = userHome + fileSep + appName + fileSep + themeFolder;
        File themeFolderFile = new File(themeFolderPath);
        if (!themeFolderFile.exists()) {
            themeFolderFile.mkdir();
        }
        File[] themes = themeFolderFile.listFiles();
        if (themes.length <= 0) {
            String message = messageController.getMessage("no.themes.available");
            frameView.showInfoDialog(message);
        } else {
            Map<String, String> pathsForTheme = new HashMap<String, String>();
            String existingTheme = null;
            for (File themeFile : themes) {
                String themeName = themeFile.getName();
                pathsForTheme.put(themeName, themeFile.getAbsolutePath());
                if (existingThemePath.equalsIgnoreCase(themeFile.getAbsolutePath())) {
                    existingTheme = themeName;
                }
            }
            Object[] choices = pathsForTheme.keySet().toArray();
            String question = messageController.getMessage("set.skin.question");
            String label = messageController.getLabel("set.skin.popup.title");
            String choice = (String) JOptionPane.showInputDialog(frameView.getFrame(), question, label, JOptionPane.PLAIN_MESSAGE, null, choices, existingTheme);
            try {
                String themePath = pathsForTheme.get(choice);
                configService.updateProperty(ConfigKeys.KEY_SKIN, themePath);
                ((SkinnableUiServiceImpl) skinnableService).updateSkin(themePath);
                String message = messageController.getMessage("theme.available.restart");
                frameView.showInfoDialog(message);
            } catch (Exception e) {
                frameView.showExceptionDialog(e.getLocalizedMessage());
            }
        }
    }

    public FrameView getFrameView() {
        return frameView;
    }

    public void setFrameView(FrameView frameView) {
        this.frameView = frameView;
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

    public UiService getUiService() {
        return uiService;
    }

    public void setUiService(UiService uiService) {
        this.uiService = uiService;
    }
}
