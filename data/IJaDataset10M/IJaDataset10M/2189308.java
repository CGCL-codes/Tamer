package org.transdroid.desktop.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.AbstractTableModel;
import org.transdroid.daemon.Torrent;
import org.transdroid.daemon.TorrentStatus;
import org.transdroid.daemon.util.FileSizeConverter;
import org.transdroid.daemon.util.TimespanConverter;
import org.transdroid.desktop.gui.Transdroid;

public class TorrentsModel extends AbstractTableModel {

    private static final long serialVersionUID = -7118232097555641598L;

    private List<Torrent> torrents;

    private Map<String, Integer> labels;

    private String[] columns = new String[] { "Name", "Status", "Size", "Done", "Downloaded", "Uploaded", "Ratio", "Down speed", "Up speed", "ETA", "Label", "Peers", "Availability" };

    public TorrentsModel() {
        this.torrents = new ArrayList<Torrent>();
        this.labels = new HashMap<String, Integer>();
    }

    public void resetTorrents(List<Torrent> torrents) {
        this.torrents = torrents;
        for (Torrent torrent : this.torrents) {
            if (labels.containsKey(torrent.getLabelName())) {
                labels.put(torrent.getLabelName(), labels.get(torrent.getLabelName()) + 1);
            } else {
                labels.put(torrent.getLabelName(), 1);
            }
        }
        fireTableDataChanged();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int col) {
        return columns[col];
    }

    @Override
    public int getRowCount() {
        return torrents.size();
    }

    @Override
    public Object getValueAt(int row, int col) {
        Torrent tor = torrents.get(row);
        switch(col) {
            case 0:
                return tor.getName();
            case 1:
                return tor.getStatusCode();
            case 2:
                return FileSizeConverter.getSize(tor.getTotalSize());
            case 3:
                return String.format(Transdroid.DECIMAL_FORMATTER, tor.getDownloadedPercentage() * 100) + "%";
            case 4:
                return FileSizeConverter.getSize(tor.getDownloadedEver());
            case 5:
                return FileSizeConverter.getSize(tor.getUploadedEver());
            case 6:
                return getRatioString(tor);
            case 7:
                return FileSizeConverter.getSize(tor.getRateDownload()) + "/s";
            case 8:
                return FileSizeConverter.getSize(tor.getRateUpload()) + "/s";
            case 9:
                return (tor.getStatusCode() == TorrentStatus.Downloading ? getRemainingTimeString(tor, true) : "");
            case 10:
                return tor.getLabelName();
            case 11:
                if (tor.getStatusCode() == TorrentStatus.Downloading) {
                    return tor.getPeersSendingToUs() + " of " + tor.getPeersConnected();
                } else if (tor.getStatusCode() == TorrentStatus.Seeding) {
                    return tor.getPeersGettingFromUs() + " of " + tor.getPeersConnected();
                } else {
                    return tor.getPeersKnown();
                }
            case 12:
                return String.format(Transdroid.DECIMAL_FORMATTER, tor.getAvailability() * 100) + "%";
        }
        return null;
    }

    public int getColumnPreferredSize(int col) {
        switch(col) {
            case 0:
                return 220;
            case 1:
            case 9:
            case 10:
            case 11:
                return 110;
            default:
                return 70;
        }
    }

    private String getRatioString(Torrent tor) {
        long baseSize = tor.getTotalSize();
        if (tor.getStatusCode() == TorrentStatus.Downloading) {
            baseSize = tor.getDownloadedEver();
        }
        if (baseSize <= 0) {
            return String.format(Transdroid.DECIMAL_FORMATTER, 0d);
        } else if (tor.getRatio() == Double.POSITIVE_INFINITY) {
            return "∞";
        } else {
            return String.format(Transdroid.DECIMAL_FORMATTER, tor.getRatio());
        }
    }

    private String getRemainingTimeString(Torrent tor, boolean inDays) {
        if (tor.getEta() == -1 || tor.getEta() == -2) {
            return "Unknown";
        }
        return TimespanConverter.getTime(tor.getEta(), inDays);
    }

    public Torrent getTorrent(int row) {
        return torrents.get(row);
    }

    public Map<String, Integer> getExistingLabels() {
        return labels;
    }

    public void mimicPauseTorrent(Torrent torrent) {
        torrent.mimicPause();
        fireTableDataChanged();
    }

    public void mimicResumeTorrent(Torrent torrent) {
        torrent.mimicResume();
        fireTableDataChanged();
    }

    public void mimicStopTorrent(Torrent torrent) {
        torrent.mimicStop();
        fireTableDataChanged();
    }

    public void mimicStartTorrent(Torrent torrent) {
        torrent.mimicStart();
        fireTableDataChanged();
    }

    public void mimicNewLabel(Torrent torrent, String newLabel) {
        torrent.mimicNewLabel(newLabel);
        fireTableDataChanged();
    }

    public void mimicRemoveTorrent(Torrent remove) {
        torrents.remove(remove);
        fireTableDataChanged();
    }

    public void mimicNewDownloadLocation(Torrent torrent, String newLocation) {
        torrent.mimicNewDownloadLocation(newLocation);
        fireTableDataChanged();
    }
}
