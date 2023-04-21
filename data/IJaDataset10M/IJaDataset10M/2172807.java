package com.dashboard.service;

import android.content.Context;
import com.dashboard.dao.HistoryProvider;
import com.dashboard.dao.IHistoryDAO;
import com.dashboard.dao.ISettingsDAO;
import com.dashboard.dao.SettingsProvider;
import com.dashboard.dto.HistoryDTO;
import com.dashboard.dto.SettingsDTO;

/**
 * Service which provides access to the integration and persistence logic.
 * @author Joe Rains
 *
 */
public class DashboardService implements IDashboardService {

    private IHistoryDAO historyDAO;

    private ISettingsDAO settingsDAO;

    /**
	 * The constructor for a DashboardService requires Context to access the database.
	 * @param context the calling Activity
	 */
    public DashboardService(Context context) {
        historyDAO = new HistoryProvider(context);
        settingsDAO = new SettingsProvider(context);
    }

    @Override
    public int getCurrentDistance() {
        int distance = -1;
        int min = 9, max = 499;
        distance = min + (int) (Math.random() * ((max - min) + 1));
        return distance;
    }

    @Override
    public int getCurrentSpeed() {
        int speed = -1;
        int min = 9, max = 99;
        speed = min + (int) (Math.random() * ((max - min) + 1));
        return speed;
    }

    @Override
    public HistoryDTO getHistory() throws Exception {
        return this.historyDAO.getHistory();
    }

    @Override
    public void updateHistory(HistoryDTO history) throws Exception {
        getHistoryDAO().updateHistory(history);
    }

    @Override
    public void resetHistory() throws Exception {
        getHistoryDAO().resetHistory();
    }

    @Override
    public SettingsDTO getSettings() throws Exception {
        return getSettingsDAO().getSettings();
    }

    @Override
    public void updateSettings(SettingsDTO settings) throws Exception {
        getSettingsDAO().updateSettings(settings);
    }

    /**
	 * Private accessor for the history DAO class.
	 * @return IHistoryDAO
	 */
    private IHistoryDAO getHistoryDAO() {
        return historyDAO;
    }

    /**
	 * Private accessor for the settings DAO class.
	 * @return ISettingsDAO
	 */
    private ISettingsDAO getSettingsDAO() {
        return settingsDAO;
    }
}
