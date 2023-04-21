package de.psychomatic.mp3db.core.threads.backup.restorer;

import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.input.CountingInputStream;
import org.apache.torque.om.BaseObject;
import de.psychomatic.mp3db.core.dblayer.Album;
import de.psychomatic.mp3db.core.dblayer.Cd;
import de.psychomatic.mp3db.core.dblayer.Coveritem;
import de.psychomatic.mp3db.core.dblayer.Mediafile;
import de.psychomatic.mp3db.core.threads.backup.AbstractRestorer;
import de.psychomatic.mp3db.core.threads.backup.TypeConstants;
import de.psychomatic.mp3db.core.threads.interfaces.StatusEvent;

public class Restorer04 extends AbstractRestorer {

    private final Pattern _albumPattern = Pattern.compile("(\\d*)\\|([^|]*)\\|(\\d*)");

    private final Map<Integer, Album> _albums;

    private final Pattern _cdPattern = Pattern.compile("(\\d*)\\|([^|]*)\\|([^|]*)");

    private final Pattern _coveritemPattern = Pattern.compile("(\\d*)\\|([^|]*)\\|([^|]*)");

    private final LineNumberReader _lineReader;

    private final Pattern _linkPattern = Pattern.compile("([^|]*)\\|(\\d*)");

    private final Pattern _mediafilePattern = Pattern.compile("(\\d*)\\|([^|]*)\\|([^|]*)\\|(\\d*)\\|(\\d*)\\|(\\d*)\\|([^|]*)");

    public Restorer04(final CountingInputStream cis, final LineNumberReader lineReader, final long size) {
        super(cis, size);
        _lineReader = lineReader;
        _albums = new HashMap<Integer, Album>();
    }

    @Override
    public void doRestore() throws Exception {
        String line = _lineReader.readLine();
        fireStatusEvent(new StatusEvent(this, StatusEvent.NEW_VALUE, _posStream.getByteCount()));
        while (line != null && !_stop && line.startsWith(TypeConstants.CD)) {
            final Cd cd = importCD(line.substring(3));
            cd.save();
            line = _lineReader.readLine();
            fireStatusEvent(new StatusEvent(this, StatusEvent.NEW_VALUE, _posStream.getByteCount()));
            final List<BaseObject> mfiles = new ArrayList<BaseObject>();
            while (line != null && !_stop && line.startsWith(TypeConstants.MEDIAFILE)) {
                final Mediafile mf = importMediafile(line.substring(3));
                mf.setCd(cd);
                line = _lineReader.readLine();
                fireStatusEvent(new StatusEvent(this, StatusEvent.NEW_VALUE, _posStream.getByteCount()));
                while (line != null && !_stop && (line.startsWith(TypeConstants.ALBUM) || line.startsWith("#" + TypeConstants.ALBUM))) {
                    Album al = null;
                    if (line.startsWith("#")) {
                        al = (Album) importLink(line.substring(1));
                        mf.setAlbum(al);
                        line = _lineReader.readLine();
                    } else {
                        final String data = line.substring(3);
                        al = importAlbum(data);
                        al.save();
                        _albums.put(getId(data), al);
                        mf.setAlbum(al);
                        line = _lineReader.readLine();
                        fireStatusEvent(new StatusEvent(this, StatusEvent.NEW_VALUE, _posStream.getByteCount()));
                        final List<BaseObject> covers = new ArrayList<BaseObject>();
                        while (line != null && !_stop && line.startsWith(TypeConstants.COVERITEM)) {
                            final Coveritem ci = importCoveritem(line.substring(3));
                            ci.setAlbumid(al.getAid());
                            covers.add(ci);
                            line = _lineReader.readLine();
                            fireStatusEvent(new StatusEvent(this, StatusEvent.NEW_VALUE, _posStream.getByteCount()));
                        }
                        if (covers.size() > 0) {
                            saveList(covers);
                            cleanUp(false);
                        }
                    }
                }
                mfiles.add(mf);
            }
            if (mfiles.size() > 0) {
                saveList(mfiles);
            }
        }
        cleanUp(true);
    }

    private Integer getId(final String content) {
        return Integer.valueOf(content.substring(0, content.indexOf("|")));
    }

    /**
     * Imports an album
     * @param content String with content of album
     * @return Album object
     */
    private Album importAlbum(final String content) {
        final Matcher m = _albumPattern.matcher(content);
        if (m.matches()) {
            final Album a = new Album();
            a.setAlbum(m.group(2));
            a.setAlbumType(Short.parseShort(m.group(3)));
            return a;
        }
        return null;
    }

    /**
     * Imports a CD
     * @param content String with content of cd
     * @return Cd object
     */
    private Cd importCD(final String content) {
        final Matcher m = _cdPattern.matcher(content);
        if (m.matches()) {
            final Cd cd = new Cd();
            cd.setCdName(m.group(2));
            cd.setCdMd5(m.group(3));
            return cd;
        }
        return null;
    }

    private Coveritem importCoveritem(final String line) throws IOException {
        final Matcher m = _coveritemPattern.matcher(line);
        if (m.matches()) {
            final Coveritem ci = new Coveritem();
            final String type = m.group(2);
            if (type.equals("")) {
                ci.setCitype("???");
            } else {
                ci.setCitype(type);
            }
            ci.setCidata(Base64.decodeBase64(m.group(3).getBytes("ASCII")));
            return ci;
        }
        return null;
    }

    private BaseObject importLink(final String content) {
        final Matcher m = _linkPattern.matcher(content);
        if (m.matches()) {
            final Integer id = Integer.valueOf(m.group(2));
            final String type = m.group(1);
            if (type.equals(TypeConstants.ALBUM)) {
                return _albums.get(id);
            }
        }
        return null;
    }

    /**
     * Imports a mediafile
     * @param content String with content of mediafile
     * @return Mediafile object
     */
    private Mediafile importMediafile(final String content) {
        final Matcher m = _mediafilePattern.matcher(content);
        if (m.matches()) {
            final Mediafile mf = new Mediafile();
            mf.setArtist(m.group(2));
            mf.setTitle(m.group(3));
            mf.setBitrate(Integer.parseInt(m.group(4)));
            mf.setFilesize(Long.parseLong(m.group(5)));
            mf.setPlaytime(Integer.parseInt(m.group(6)));
            mf.setPath(m.group(7));
            return mf;
        }
        return null;
    }
}
