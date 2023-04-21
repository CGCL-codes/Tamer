package com.guanda.swidgex.widgets.mp3player;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.net.MalformedURLException;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

class Mp3ListTransferHandler extends TransferHandler {

    private static final long serialVersionUID = 1L;

    private MP3Player player;

    private int fromIndex;

    public Mp3ListTransferHandler(MP3Player player) {
        this.player = player;
    }

    public boolean canImport(TransferHandler.TransferSupport info) {
        if (!canAccept(info)) {
            return false;
        }
        JList.DropLocation dl = (JList.DropLocation) info.getDropLocation();
        if (dl.getIndex() == -1) {
            return false;
        }
        return true;
    }

    private boolean canAccept(TransferHandler.TransferSupport info) {
        return info.isDataFlavorSupported(DataFlavor.javaFileListFlavor) || info.isDataFlavorSupported(DataFlavor.stringFlavor);
    }

    public boolean importData(TransferHandler.TransferSupport info) {
        if (!info.isDrop()) {
            return false;
        }
        if (!canAccept(info)) {
            return false;
        }
        if (info.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            return acceptString(info);
        } else {
            return acceptFile(info);
        }
    }

    @SuppressWarnings("unchecked")
    private boolean acceptFile(TransferHandler.TransferSupport info) {
        Transferable transferable = info.getTransferable();
        try {
            List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
            if (files != null && !files.isEmpty()) {
                File file = files.get(0);
                if (file.isDirectory()) {
                    player.readListFromDir(file);
                    player.jump2(0);
                    return true;
                } else if (file.isFile()) {
                    String name = file.getName();
                    if (name.endsWith(".m3u")) {
                        player.setM3URL(file.toURI().toURL());
                        return true;
                    } else if (name.endsWith(".mp3")) {
                        acceptAsMp3(info, file);
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void acceptAsMp3(TransferHandler.TransferSupport info, File mp3File) throws MalformedURLException {
        JList.DropLocation dl = (JList.DropLocation) info.getDropLocation();
        int index = dl.getIndex();
        player.addMp32List(mp3File, index);
    }

    private boolean acceptString(TransferHandler.TransferSupport info) {
        List<M3UEntry> songs = player.getSongs();
        JList.DropLocation dl = (JList.DropLocation) info.getDropLocation();
        JList mp3List = (JList) info.getComponent();
        int index = dl.getIndex();
        DefaultListModel listModel = (DefaultListModel) mp3List.getModel();
        synchronized (songs) {
            if (fromIndex < index) {
                M3UEntry o = songs.get(fromIndex);
                songs.add(index, o);
                songs.remove(fromIndex);
                o = (M3UEntry) listModel.getElementAt(fromIndex);
                listModel.add(index, o);
                listModel.remove(fromIndex);
            } else if (fromIndex > index) {
                M3UEntry o = songs.remove(fromIndex);
                songs.add(index, o);
                o = (M3UEntry) listModel.remove(fromIndex);
                listModel.add(index, o);
            }
        }
        return true;
    }

    public int getSourceActions(JComponent c) {
        return COPY;
    }

    protected Transferable createTransferable(JComponent c) {
        JList list = (JList) c;
        Object[] values = list.getSelectedValues();
        fromIndex = list.getSelectedIndex();
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < values.length; i++) {
            Object val = values[i];
            buff.append(val == null ? "" : val.toString());
            if (i != values.length - 1) {
                buff.append("\n");
            }
        }
        return new StringSelection(buff.toString());
    }
}
