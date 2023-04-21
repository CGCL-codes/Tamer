package net.sourceforge.exclusive.client.gui.filetransferstatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import net.sourceforge.exclusive.client.ADTListener;
import net.sourceforge.exclusive.client.ActiveDownloadInfo;
import net.sourceforge.exclusive.client.config.ClientConf;
import net.sourceforge.exclusive.util.SpeedCalculator;
import net.sourceforge.exclusive.util.Util;

public class FileTransferTableModel extends AbstractTableModel {

    private static final long serialVersionUID = -3452419126341977836L;

    public enum Status {

        started, downloading, paused, error, finished
    }

    ;

    private String[] cnames = new String[] { "filename", "status", "finished", "speed", "remaining time" };

    private List<FileTransfer> fts;

    public FileTransferTableModel() {
        super();
        fts = Collections.synchronizedList(new LinkedList<FileTransfer>());
    }

    public void reset() {
        fts.clear();
    }

    public ADTListener add(String filename, String hash, int piecesTotal) {
        FileTransfer ft = new FileTransfer(filename, hash, piecesTotal);
        fts.add(ft);
        fireTableDataChanged();
        return ft.getListener();
    }

    public String getColumnName(int col) {
        return cnames[col];
    }

    public void remove(String hash) {
        for (int i = 0; i < fts.size(); i++) {
            if (fts.get(i).hash.equals(hash)) {
                fts.remove(i);
                this.fireTableRowsDeleted(i, i);
                break;
            }
        }
    }

    public Status getStatus(String hash) {
        for (int i = 0; i < fts.size(); i++) {
            if (fts.get(i).hash.equals(hash)) return fts.get(i).status;
        }
        return null;
    }

    public List<String> getHashes() {
        List<String> ret = new LinkedList<String>();
        for (FileTransfer ft : fts) {
            ret.add(ft.hash);
        }
        return ret;
    }

    public int getColumnCount() {
        return cnames.length;
    }

    public int getRowCount() {
        return fts.size();
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex >= 0 && rowIndex < fts.size()) {
            FileTransfer ft = fts.get(rowIndex);
            double speed = ft.speed.getDownloadSpeed();
            switch(columnIndex) {
                case 0:
                    return ft.filename;
                case 1:
                    return ft.status.toString();
                case 2:
                    return ft.piecesDown;
                case 3:
                    return Util.formatByte(speed) + "/s";
                case 4:
                    return getETA(ft.piecesToGo, speed);
            }
        }
        return "";
    }

    private String getETA(int toDownload, double speed) {
        if (speed <= 50.0) return "--";
        long secondsToGo = new Double(new Double(toDownload) * new Double(ClientConf.PACKETSIZE) / speed).longValue();
        long days = secondsToGo / 86400;
        long hours = (secondsToGo - (days * 86400)) / 3600;
        long minutes = (secondsToGo - (days * 3600)) / 60;
        long seconds = (secondsToGo - (days * 60));
        if (days > 0) return "~ " + days + " days   ";
        if (hours > 0) return "~ " + hours + " hours  ";
        if (minutes > 0) return "~ " + minutes + " minutes";
        if (seconds > 30) return "<  1 minute ";
        if (seconds > 20) return "< 30 seconds";
        if (seconds > 5 && seconds <= 10) return "< 10 seconds"; else return "< 5 seconds";
    }

    private int getRow(FileTransfer ft) {
        return fts.indexOf(ft);
    }

    public String getHash(int row) {
        return fts.get(row).hash;
    }

    private class FileTransfer {

        public String filename;

        public Status status;

        public String hash;

        public boolean[] piecesDown;

        public int piecesToGo;

        public SpeedCalculator speed;

        public FileTransfer(String filename, String hash, int piecesTotal) {
            this.filename = filename;
            this.hash = hash;
            this.status = Status.started;
            this.piecesToGo = piecesTotal;
            this.piecesDown = new boolean[piecesTotal];
            Arrays.fill(this.piecesDown, false);
            this.speed = new SpeedCalculator();
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) return false;
            if (!(o instanceof FileTransfer)) return false;
            return ((FileTransfer) o).hash.equals(this.hash);
        }

        public ADTListener getListener() {
            return new ADTListener() {

                @Override
                public void errorHappened(String reason) {
                    status = Status.error;
                    int row = FileTransferTableModel.this.getRow(FileTransfer.this);
                    if (row != -1) FileTransferTableModel.this.fireTableRowsUpdated(row, row);
                }

                @Override
                public void pieceTransfered() {
                    piecesToGo--;
                    for (int i = 0; i < ClientConf.PACKETSIZE; i++) speed.addDownload();
                    if (status != Status.downloading) status = Status.downloading;
                    int row = FileTransferTableModel.this.getRow(FileTransfer.this);
                    if (row != -1) FileTransferTableModel.this.fireTableRowsUpdated(row, row);
                }

                @Override
                public void byteTransfered() {
                    speed.addDownload();
                }

                @Override
                public void statusChanged(ActiveDownloadInfo.Status state) {
                    if (state == ActiveDownloadInfo.Status.Paused) {
                        status = Status.paused;
                        speed.clear();
                    } else if (state == ActiveDownloadInfo.Status.Running) status = Status.downloading; else if (state == ActiveDownloadInfo.Status.Aborted) {
                        status = Status.error;
                        Arrays.fill(piecesDown, false);
                        speed.clear();
                    } else {
                        status = Status.finished;
                        Arrays.fill(piecesDown, true);
                        speed.clear();
                    }
                    int row = FileTransferTableModel.this.getRow(FileTransfer.this);
                    if (row != -1) FileTransferTableModel.this.fireTableRowsUpdated(row, row);
                }
            };
        }
    }
}
