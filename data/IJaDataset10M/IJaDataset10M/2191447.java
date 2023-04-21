package de.ueppste.ljb.client.tablemodel;

import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import de.ueppste.ljb.client.LJBUtility;
import de.ueppste.ljb.share.Mp3AndFavorite;

public class TableFAV extends DefaultTableModel {

    private static final long serialVersionUID = 4221821344819095952L;

    private String[] columnNames = { "Artist", "Title", "Album", "Genre", "Länge", "Anzahl", "Spielbar" };

    private Vector<Mp3AndFavorite> favVektor;

    public void fillTable(Vector<Mp3AndFavorite> favVektor) {
        this.favVektor = new Vector<Mp3AndFavorite>();
        this.favVektor = favVektor;
    }

    public int getColumnCount() {
        if (this.favVektor != null && getRowCount() != 0) return this.columnNames.length; else return 0;
    }

    public int getRowCount() {
        if (this.favVektor != null) return this.favVektor.size(); else return 0;
    }

    public Object getValueAt(int zeile, int spalte) {
        Mp3AndFavorite mp3 = (Mp3AndFavorite) this.favVektor.get(zeile);
        String[] time = new String[2];
        if (spalte == 4) time = LJBUtility.getTimeFromStamp(mp3.getLength());
        switch(spalte) {
            case 0:
                return mp3.getArtist();
            case 1:
                return mp3.getTitle();
            case 2:
                return mp3.getAlbum();
            case 3:
                return mp3.getGenre();
            case 4:
                return time[0] + ":" + time[1];
            case 5:
                return mp3.getCount();
            case 6:
                if (mp3.getPlayable()) return "Ja"; else return "Nein";
            default:
                return "NA";
        }
    }

    public String getColumnName(int spalte) {
        return this.columnNames[spalte];
    }

    public boolean isCellEditable(int zeile, int spalte) {
        return false;
    }
}
