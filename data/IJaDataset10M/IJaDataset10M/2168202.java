package com.groovytagger.ui.frames.model.listener;

import com.groovytagger.mp3.MP3Collection;
import com.groovytagger.ui.frames.Frame_Edit_Popup;
import com.groovytagger.ui.frames.Frame_Single_Edit_Popup;
import com.groovytagger.utils.ImageUtils;
import com.groovytagger.utils.LogManager;
import com.groovytagger.utils.StaticObj;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTable;

public class SongPropertyRowsListener implements ActionListener {

    JTable table;

    JFrame parent;

    JFrame load = null;

    int[] idxFileList;

    public void actionPerformed(ActionEvent e) {
        try {
            int rowCount = table.getSelectedRowCount();
            idxFileList = table.getSelectedRows();
            parent.setEnabled(false);
            if (rowCount > 1) {
                setPopUpValues();
            } else if (rowCount == 1) {
                setSinglePopUpValue();
            }
            load.setAlwaysOnTop(true);
            load.setLocationRelativeTo(parent);
            load.setVisible(true);
        } catch (Exception evv) {
            LogManager.getInstance().getLogger().error(evv);
            if (StaticObj.DEBUG) {
                evv.printStackTrace();
            }
            parent.setEnabled(true);
        }
    }

    private void setSinglePopUpValue() {
        load = new Frame_Single_Edit_Popup(parent, new int[] { idxFileList[0] });
        String _album = "";
        String _title = "";
        String _artist = "";
        String _year = "";
        String _genre = "";
        String _comment = "";
        ImageIcon _picture = null;
        String _resPicture = "DXX";
        ImageIcon _tmpPicture = null;
        _title = (String) table.getValueAt(idxFileList[0], 6);
        _artist = (String) table.getValueAt(idxFileList[0], 2);
        _album = (String) table.getValueAt(idxFileList[0], 3);
        _year = (String) table.getValueAt(idxFileList[0], 7);
        _genre = (String) table.getValueAt(idxFileList[0], 8);
        _picture = (ImageIcon) table.getValueAt(idxFileList[0], 9);
        if ((_picture.getDescription() != null && _picture.getDescription().equalsIgnoreCase("EmptyImg"))) {
            ((Frame_Single_Edit_Popup) load).setPicture(null);
        } else {
            byte[] _picBytes = MP3Collection.infoBean[idxFileList[0]].aPictures[0].getPictureData();
            ImageIcon _ico = new ImageIcon(_picBytes);
            Dimension d = new Dimension(180, 180);
            ((Frame_Single_Edit_Popup) load).setPicture(ImageUtils.scale(_ico, d));
        }
        ((Frame_Single_Edit_Popup) load).setTitle(_title);
        ((Frame_Single_Edit_Popup) load).setAlbum(_album);
        ((Frame_Single_Edit_Popup) load).setArtist(_artist);
        ((Frame_Single_Edit_Popup) load).setYear(_year);
        ((Frame_Single_Edit_Popup) load).setGenre(_genre);
    }

    private void setPopUpValues() {
        load = new Frame_Edit_Popup(parent, idxFileList);
        String _album = "";
        String _artist = "";
        String _year = "";
        String _genre = "";
        String _comment = "";
        ImageIcon _picture = null;
        String _tmpAlbum = "";
        String _tmpArtist = "";
        String _tmpYear = "";
        String _tmpGenre = "";
        String _tmpComment = "";
        String _resPicture = "DXX";
        ImageIcon _tmpPicture = null;
        _artist = (String) table.getValueAt(idxFileList[0], 2);
        _album = (String) table.getValueAt(idxFileList[0], 3);
        _year = (String) table.getValueAt(idxFileList[0], 7);
        _genre = (String) table.getValueAt(idxFileList[0], 8);
        _picture = (ImageIcon) table.getValueAt(idxFileList[0], 9);
        for (int i = 1; i < idxFileList.length; i++) {
            _tmpArtist = (String) table.getValueAt(idxFileList[i], 2);
            _tmpAlbum = (String) table.getValueAt(idxFileList[i], 3);
            _tmpYear = (String) table.getValueAt(idxFileList[i], 7);
            _tmpGenre = (String) table.getValueAt(idxFileList[i], 8);
            _tmpPicture = (ImageIcon) table.getValueAt(idxFileList[i], 9);
            if (!_tmpArtist.equalsIgnoreCase(_artist)) {
                _artist = "";
            }
            if (!_tmpAlbum.equalsIgnoreCase(_album)) {
                _album = "";
            }
            if (!_tmpYear.equalsIgnoreCase(_year)) {
                _year = "";
            }
            if (!_tmpGenre.equalsIgnoreCase(_genre)) {
                _genre = "";
            }
            if ((_tmpPicture.getDescription() != null && _tmpPicture.getDescription().equalsIgnoreCase("EmptyImg")) || (!_tmpPicture.equals(_picture))) {
                _resPicture = "";
            }
            String _final = _album + _artist + _year + _genre + _comment + _resPicture;
            if (_final.equalsIgnoreCase("")) {
                break;
            }
        }
        ((Frame_Edit_Popup) load).setAlbum(_album);
        ((Frame_Edit_Popup) load).setArtist(_artist);
        ((Frame_Edit_Popup) load).setYear(_year);
        ((Frame_Edit_Popup) load).setGenre(_genre);
        ((Frame_Edit_Popup) load).uncheckGenre();
        if (!_resPicture.equalsIgnoreCase("")) {
            byte[] _picBytes = MP3Collection.infoBean[idxFileList[0]].aPictures[0].getPictureData();
            ImageIcon _ico = new ImageIcon(_picBytes);
            Dimension d = new Dimension(180, 180);
            ((Frame_Edit_Popup) load).setPicture(ImageUtils.scale(_ico, d));
        } else {
            ((Frame_Edit_Popup) load).setPicture(null);
        }
    }

    public SongPropertyRowsListener(JTable table, JFrame parent) {
        this.table = table;
        this.parent = parent;
        this.idxFileList = table.getSelectedRows();
    }
}
