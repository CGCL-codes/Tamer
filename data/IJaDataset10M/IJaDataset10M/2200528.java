package com.showdown.settings;

import java.io.File;
import java.util.Date;
import com.showdown.api.IShow;

/**
 * Implementation of {@link IDownloadDateStore} which extends {@link ShowDownSettings} to
 * make use of its storing/retrieving code.
 * @author Mat DeLong
 */
public class DownloadDateStore extends ShowDownSettings implements IDownloadDateStore {

    /**
    * Constructor
    * @param propFile the properties file to save to and load from
    */
    public DownloadDateStore(File propFile) {
        super(propFile);
    }

    /**
    * {@inheritDoc}
    */
    public Date getLastDownloadAirDate(IShow show) {
        return super.getDate(new DownloadDateSettingsItem(show));
    }

    /**
    * {@inheritDoc}
    */
    public void setLastDownloadedAirDate(IShow show, Date date) {
        super.setDate(new DownloadDateSettingsItem(show), date);
    }

    /**
    * Item wrapping a show to store/load the last downloaded air date for
    */
    private class DownloadDateSettingsItem implements ISettingsItem {

        private String key;

        /**
       * Specifies the show and date to wrap by this item.
       * @param show the show this item is for
       */
        public DownloadDateSettingsItem(IShow show) {
            this.key = "lastdownloaded_" + show.getName().replaceAll(" ", "").toLowerCase();
        }

        /**
       * {@inheritDoc}
       */
        public String getKey() {
            return key;
        }

        /**
       * {@inheritDoc}
       */
        public Object getDefaultValue() {
            return null;
        }

        /**
       * {@inheritDoc}
       */
        public SettingsItemType getType() {
            return SettingsItemType.DATE;
        }
    }
}
