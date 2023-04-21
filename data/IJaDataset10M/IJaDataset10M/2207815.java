package de.psychomatic.mp3db.plugin.type.vorbis;

import java.io.File;
import java.util.Map;
import javazoom.spi.vorbis.sampled.file.VorbisAudioFileReader;
import org.apache.commons.io.FilenameUtils;
import org.tritonus.share.sampled.file.TAudioFileFormat;
import de.psychomatic.applicationtools.Version;
import de.psychomatic.mp3db.core.interfaces.MediafileIf;
import de.psychomatic.mp3db.core.plugins.impl.TypePluginImpl;

public class VorbisType extends TypePluginImpl {

    private static final Version VERSION = new Version(0, 2, 0);

    private static final String NAME = "OGG Vorbis type plugin";

    private static final VorbisAudioFileReader VORBIS_READER = new VorbisAudioFileReader();

    @Override
    public String[] getSupportedExtensions() {
        return new String[] { "ogg" };
    }

    @Override
    public MediafileIf readFile(final File f, final MediafileIf mf) throws Exception {
        final TAudioFileFormat taf = (TAudioFileFormat) VORBIS_READER.getAudioFileFormat(f);
        final Map tag = taf.properties();
        if (tag == null || tag.size() == 0) {
            handleNoTag(FilenameUtils.getBaseName(f.getName()), mf);
        } else {
            final String artist = (String) tag.get("author");
            final String title = (String) tag.get("title");
            if ((artist == null || artist.trim().equals("")) && (title == null || title.trim().equals(""))) {
                handleNoTag(FilenameUtils.getBaseName(f.getName()), mf);
            } else {
                mf.setArtist(artist == null ? null : artist.trim());
                mf.setTitle(title == null ? null : title.trim());
            }
        }
        mf.setPlaytime((int) ((Long) tag.get("duration")).longValue() / 1000000);
        mf.setBitrate(((Integer) tag.get("ogg.bitrate.nominal.bps")).intValue() / 1000);
        return mf;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Version getVersion() {
        return VERSION;
    }
}
