package com.apachetune.core.ui.editors;

import javax.swing.*;
import java.net.URI;

/**
 * FIXDOC
 *
 * @author <a href="mailto:progmonster@gmail.com">Aleksey V. Katorgin</a>
 * @version 1.0
 */
public interface EditorInput {

    URI getDocumentUri();

    String getWorkItemId();

    String getContentPaneTitle();

    String getContentPaneId();

    String getPrintTitle();

    String getSaveTitle();

    String getContentType();

    String loadContent();

    void saveContent(String content);

    Icon getContentPaneIcon();
}
