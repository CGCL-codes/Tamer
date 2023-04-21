package com.googlecode.maratische.google.listeners;

import java.util.List;
import com.googlecode.maratische.google.BaseException;
import com.googlecode.maratische.google.model.Credentials;
import com.googlecode.maratische.google.model.Item;
import com.googlecode.maratische.google.model.Label;
import com.googlecode.maratische.google.model.Spam;

public interface MainFrameListener {

    void showError(Exception e);

    void showError(String text, Exception e);

    void updateFeedMenu();

    void updateItemsPanel();

    void itemsPanelClearSelection();

    Credentials loginForm(String errorMessage);

    void updateOfflineButton() throws BaseException;

    void setItemsToItemList(List<Item> items);

    void doFullScreenMode();

    void doExpandWindow();

    void doExpandItems(boolean expand);

    void viewSourceCodeFrame(String title, String description);

    Label openLabelForm(Item selectedItem);

    void openSpamForm();

    boolean openNotLabelForm(Item selectedItem);
}
