package com.kitten.gui;

import java.awt.event.WindowListener;
import java.util.Observable;
import javax.swing.JFrame;
import com.kitten.IKittenListen2GuiObject;
import com.kitten.dao.IKittenLoginDetailsDao;
import com.kitten.dao.KittenDao;
import com.kitten.dao.KittenExportResultDao;
import com.kitten.dao.KittenImportDao;
import com.kitten.dao.KittenUserPrefsDao;

public interface IKittenView {

    public void windowListeners(WindowListener windowListener);

    public void addDatabaseUI(IKittenListen2GuiObject kittenController);

    public void removeDatabaseUI();

    public boolean isDatabaseUIRunning();

    public void initialize(IKittenListen2GuiObject kittenController);

    public void createNewConnectionDialog();

    public void cancelNewConnection();

    public IKittenLoginDetailsDao getConnectionDetails();

    public JFrame getParentFrame();

    public void writeOpenFile2Screen(String data);

    public String getScreenText2Save2File();

    public void showCellInSeparateWindow(IKittenListen2GuiObject kittenController);

    public void closeSeparateWindowOk();

    public void closeSeparateWindow();

    public void initializeGuiFields(KittenDao kittenDao);

    public void prepareGui4Running(KittenDao kittenDao);

    public void resetGuiAfterRunning(KittenDao kittenDao);

    public void fillDaoWithData(KittenDao kittenDao);

    public void copy(Object source);

    public void paste(Object source);

    public void clear(Object source);

    public void cut(Object source);

    public void selectAll(Object source);

    public void selectAllResults();

    public void closeSeparateInsertWindow();

    public void showAddRowsDialog(IKittenListen2GuiObject kittenController, KittenDao kittenDao);

    public KittenDao getNewRows();

    public void setEmptyDatabaseDisplay();

    public void setAutomaticQuery(String query, IKittenListen2GuiObject kittenController);

    public void test();

    public void aboutKitten();

    public void showChangeDatabaseType(IKittenListen2GuiObject kittenController);

    public void showSetUserPrefs(IKittenListen2GuiObject kittenController);

    public void closeUserPrefsDialog();

    public void closeSeparateChangeDatabaseTypeWindow();

    public String getChosenDatabaseType();

    public KittenExportResultDao getKittenExportResultDao();

    public void showWaiting4DisplayData();

    public void showDisplayDataReady();

    public KittenUserPrefsDao getUserPrefs();

    public void closeExportResultDialog();

    public void closeImportDialog();

    public void showExportResultDialog(IKittenListen2GuiObject kittenController);

    public void showImportDialog(IKittenListen2GuiObject kittenController);

    public KittenImportDao getKittenImportDao();

    public void transActionStatusObject2Listen2(Observable object);

    public void removeExistingConnection();

    public void setOpenedFile(String path);

    public String getOpenedFile();

    public void notifyObservers4Results(Observable observable);
}
