package net.sourceforge.filebot.web;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.util.Formatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import net.sourceforge.tuned.ByteBufferOutputStream;
import net.sublight.webservice.Subtitle;

public class SublightSubtitleDescriptor implements SubtitleDescriptor {

    private final Subtitle subtitle;

    private final SublightSubtitleClient source;

    private final String name;

    private final String languageName;

    public SublightSubtitleDescriptor(Subtitle subtitle, SublightSubtitleClient source) {
        this.subtitle = subtitle;
        this.source = source;
        this.name = getName(subtitle);
        this.languageName = source.getLanguageName(subtitle.getLanguage());
    }

    private String getName(Subtitle subtitle) {
        String releaseName = subtitle.getRelease();
        if (releaseName != null && !releaseName.isEmpty()) {
            boolean isValid = true;
            if (subtitle.getSeason() != null) {
                isValid &= releaseName.contains(subtitle.getSeason().toString());
            }
            if (subtitle.getEpisode() != null) {
                isValid &= releaseName.contains(subtitle.getEpisode().toString());
            }
            if (isValid) {
                return releaseName;
            }
        }
        Formatter builder = new Formatter(new StringBuilder(subtitle.getTitle()));
        if (subtitle.getSeason() != null || subtitle.getEpisode() != null) {
            builder.format(" - S%02dE%02d", subtitle.getSeason(), subtitle.getEpisode());
        }
        if (subtitle.getRelease() != null && !subtitle.getRelease().isEmpty()) {
            builder.format(" (%s)", subtitle.getRelease());
        }
        return builder.out().toString();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getLanguageName() {
        return languageName;
    }

    @Override
    public String getType() {
        return subtitle.getSubtitleType().value().toLowerCase();
    }

    @Override
    public long getLength() {
        return subtitle.getSize();
    }

    @Override
    public ByteBuffer fetch() throws Exception {
        byte[] archive = source.getZipArchive(subtitle);
        ZipInputStream stream = new ZipInputStream(new ByteArrayInputStream(archive));
        try {
            ZipEntry entry = stream.getNextEntry();
            ByteBufferOutputStream buffer = new ByteBufferOutputStream(entry.getSize());
            buffer.transferFully(stream);
            return buffer.getByteBuffer();
        } finally {
            stream.close();
        }
    }

    @Override
    public String getPath() {
        return String.format("%s.%s", getName(), getType());
    }

    @Override
    public int hashCode() {
        return subtitle.getSubtitleID().hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof SublightSubtitleDescriptor) {
            SublightSubtitleDescriptor other = (SublightSubtitleDescriptor) object;
            return subtitle.getSubtitleID().equals(other.subtitle.getSubtitleID());
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s [%s]", getName(), getLanguageName());
    }
}
