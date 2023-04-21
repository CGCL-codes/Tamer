package de.fuh.xpairtise.plugin.core;

import de.fuh.xpairtise.common.LogConstants;
import de.fuh.xpairtise.common.XPLog;
import de.fuh.xpairtise.plugin.ui.xpviews.chat.ChatController;
import de.fuh.xpairtise.plugin.ui.xpviews.chat.IChatView;
import de.fuh.xpairtise.plugin.ui.xpviews.chat.SessionChatController;
import de.fuh.xpairtise.plugin.ui.xpviews.editor.EditorController;
import de.fuh.xpairtise.plugin.ui.xpviews.editor.IEditorWrapper;
import de.fuh.xpairtise.plugin.ui.xpviews.sessiongallery.IXPSessionGalleryController;
import de.fuh.xpairtise.plugin.ui.xpviews.sessiongallery.IXPSessionGalleryView;
import de.fuh.xpairtise.plugin.ui.xpviews.sessiongallery.XPSessionGalleryController;
import de.fuh.xpairtise.plugin.ui.xpviews.usergallery.IUserGalleryView;
import de.fuh.xpairtise.plugin.ui.xpviews.usergallery.UserGalleryController;
import de.fuh.xpairtise.plugin.ui.xpviews.whiteboard.IWhiteboardController;
import de.fuh.xpairtise.plugin.ui.xpviews.whiteboard.IWhiteboardView;
import de.fuh.xpairtise.plugin.ui.xpviews.whiteboard.WhiteboardController;

/**
 * This class implements the IClientApplicationFactory interface which declares
 * methods for creating the controller part of the application.
 */
public class ClientApplicationFactory implements IClientApplicationFactory {

    ClientApplicationFactory() {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_CLIENTAPPLICATION_FACTORY + "started.");
        }
    }

    public ChatController createGlobalChatController(IChatView view) throws Exception {
        return new ChatController(view);
    }

    public ChatController createSessionChatController(IChatView view) throws Exception {
        return new SessionChatController(view);
    }

    public UserGalleryController createUserGalleryController(String channelId, IUserGalleryView view) throws Exception {
        return new UserGalleryController(channelId, view);
    }

    public IXPSessionGalleryController createXPSessionGalleryController(IXPSessionGalleryView sessionView) throws Exception {
        return new XPSessionGalleryController(sessionView);
    }

    public IWhiteboardController createWhiteboardController(String channelId, IWhiteboardView view) throws Exception {
        return new WhiteboardController(channelId, view);
    }

    public EditorController createEditorController(String editorId, IEditorWrapper wrapper) throws Exception {
        return new EditorController(editorId, wrapper);
    }
}
