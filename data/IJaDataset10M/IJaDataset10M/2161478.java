package de.psychomatic.mp3db.porter.threads;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPOutputStream;
import messagesystem.MessageEvent;
import messagesystem.MessageEventIf;
import messagesystem.MessageSenderIf;
import org.apache.commons.codec.binary.Base64;
import org.apache.torque.util.Criteria;
import de.psychomatic.mp3db.Main;
import de.psychomatic.mp3db.dblayer.Album;
import de.psychomatic.mp3db.dblayer.AlbumPeer;
import de.psychomatic.mp3db.dblayer.Cd;
import de.psychomatic.mp3db.dblayer.CdPeer;
import de.psychomatic.mp3db.dblayer.Covers;
import de.psychomatic.mp3db.dblayer.CoversPeer;
import de.psychomatic.mp3db.dblayer.Mediafile;
import de.psychomatic.mp3db.dblayer.MediafilePeer;
import de.psychomatic.mp3db.utils.GuiStrings;

/**
 * Backupthread
 * @author Kykal
 */
public class BackupThread {

    /**
     * Creates the backupthread
     * @param messenger Messagesender
     * @param file File to backup data
     */
    public BackupThread(MessageSenderIf messenger, File file) {
        _messenger = messenger;
        _file = file;
    }

    /**
     * Starts the process
     */
    public void getData() {
        _break = false;
        Thread t = new Thread() {

            public void run() {
                try {
                    PrintStream pstream = new PrintStream(new GZIPOutputStream(new FileOutputStream(_file), 1024));
                    int offset = 0;
                    pstream.println("--" + Main.VERSION);
                    Criteria c = new Criteria();
                    c.setLimit(_limit);
                    c.setOffset(offset);
                    List export = new ArrayList();
                    if (!_break) export = MediafilePeer.doSelect(c);
                    exportData("MF", pstream, export);
                    while (export.size() == _limit && !_break) {
                        offset += _limit;
                        c.setOffset(offset);
                        export = MediafilePeer.doSelect(c);
                        if (export.size() > 0) exportData("MF", pstream, export);
                    }
                    c = new Criteria();
                    offset = 0;
                    c.setLimit(_limit);
                    c.setOffset(offset);
                    if (!_break) export = CdPeer.doSelect(c);
                    exportData("CD", pstream, export);
                    while (export.size() == _limit && !_break) {
                        offset += _limit;
                        c.setOffset(offset);
                        export = CdPeer.doSelect(c);
                        if (export.size() > 0) exportData("CD", pstream, export);
                    }
                    c = new Criteria();
                    offset = 0;
                    c.setLimit(2);
                    c.setOffset(offset);
                    if (!_break) export = CoversPeer.doSelect(c);
                    exportData("CO", pstream, export);
                    while (export.size() == 2 && !_break) {
                        offset += 2;
                        c.setOffset(offset);
                        export = CoversPeer.doSelect(c);
                        if (export.size() > 0) exportData("CO", pstream, export);
                    }
                    c = new Criteria();
                    offset = 0;
                    c.setLimit(_limit);
                    c.setOffset(offset);
                    if (!_break) export = AlbumPeer.doSelect(c);
                    exportData("AL", pstream, export);
                    while (export.size() == _limit && !_break) {
                        offset += _limit;
                        c.setOffset(offset);
                        export = AlbumPeer.doSelect(c);
                        if (export.size() > 0) exportData("AL", pstream, export);
                    }
                    pstream.flush();
                    pstream.close();
                    if (_break) _file.delete();
                } catch (Exception e) {
                    _messenger.fireMessageEvent(new MessageEvent(this, "BackupOK", MessageEventIf.HIDE));
                    _messenger.fireMessageEvent(new MessageEvent(this, "ERROR", MessageEventIf.ERROR, GuiStrings.getString("error.backup"), e));
                }
                _messenger.fireMessageEvent(new MessageEvent(this, "BackupOK", MessageEventIf.HIDE));
            }
        };
        t.start();
    }

    /**
     * Exports the data of the DB
     * @param type Datatype
     * @param pstream Printstream to file
     * @param data List of data
     */
    private void exportData(String type, PrintStream pstream, List data) {
        Iterator it = data.iterator();
        while (it.hasNext() && !_break) {
            Object bo = it.next();
            StringBuffer buffer = new StringBuffer(type + "|");
            if (bo instanceof Mediafile) {
                exportMediafile(buffer, (Mediafile) bo);
                pstream.println(buffer.toString());
            } else if (bo instanceof Cd) {
                exportCd(buffer, (Cd) bo);
                pstream.println(buffer.toString());
            } else if (bo instanceof Covers) {
                exportCovers(pstream, (Covers) bo);
            } else if (bo instanceof Album) {
                exportAlbum(buffer, (Album) bo);
                pstream.println(buffer.toString());
            }
        }
    }

    /**
     * Exports an Album
     * @param buffer StringBuffer to write the data
     * @param album Album to write
     */
    private void exportAlbum(StringBuffer buffer, Album album) {
        buffer.append(album.getAid());
        buffer.append("|");
        buffer.append(album.getAlbum());
        buffer.append("|");
        buffer.append(album.getIsSampler());
        buffer.append("|");
        buffer.append(album.getCover());
    }

    /**
     * Exports Covers
     * @param pstream PrintStream writing to
     * @param covers Covers to export
     */
    private void exportCovers(PrintStream pstream, Covers covers) {
        pstream.print("CO|" + covers.getCid() + "|");
        if (covers.getFront() != null) pstream.print(new String(Base64.encodeBase64(covers.getFront())));
        pstream.print("|");
        if (covers.getBack() != null) pstream.print(new String(Base64.encodeBase64(covers.getBack())));
        pstream.print("|");
        if (covers.getInlay() != null) pstream.print(new String(Base64.encodeBase64(covers.getInlay())));
        pstream.print("|");
        if (covers.getCd() != null) pstream.print(new String(Base64.encodeBase64(covers.getCd())));
        pstream.print("|");
        if (covers.getOther() != null) pstream.print(new String(Base64.encodeBase64(covers.getOther())));
        pstream.println();
    }

    /**
     * Exports a CD
     * @param buffer StringBuffer to write data
     * @param cd CD to export
     */
    private void exportCd(StringBuffer buffer, Cd cd) {
        buffer.append(cd.getCdid());
        buffer.append("|");
        buffer.append(cd.getCdMd5());
        buffer.append("|");
        buffer.append(cd.getCdName());
    }

    /**
     * Exports a mediafile
     * @param buffer StringBuffer to write data
     * @param mediafile Mediafile to export
     */
    private void exportMediafile(StringBuffer buffer, Mediafile mediafile) {
        buffer.append(mediafile.getId());
        buffer.append("|");
        buffer.append(mediafile.getArtist());
        buffer.append("|");
        buffer.append(mediafile.getTitle());
        buffer.append("|");
        buffer.append(mediafile.getBitrate());
        buffer.append("|");
        buffer.append(mediafile.getGroesse());
        buffer.append("|");
        buffer.append(mediafile.getLaenge());
        buffer.append("|");
        buffer.append(mediafile.getPath());
        buffer.append("|");
        buffer.append(mediafile.getAlbumnr());
        buffer.append("|");
        buffer.append(mediafile.getCdnr());
    }

    /**
     * Sets the break-flag to stop the process
     */
    public void stopProcess() {
        _break = true;
    }

    /**
     * File to write to
     */
    private File _file;

    /**
     * Messagesender
     */
    private MessageSenderIf _messenger;

    /**
     * Readlimit (except covers)
     */
    private int _limit = 300;

    /**
     * Break-flag
     */
    private boolean _break;
}
