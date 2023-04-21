package org.tritonus.zuonics.sampled.aiff;

import org.tritonus.sampled.file.AiffTool;
import org.tritonus.sampled.file.AiffAudioOutputStream;
import org.tritonus.share.sampled.file.TDataOutputStream;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.tritonus.zuonics.sampled.AudioChunkGenerator;
import org.tritonus.zuonics.sampled.AudioChunkGenerator;
import org.tritonus.zuonics.sampled.*;
import org.tritonus.zuonics.sampled.ChunkTool;
import org.tritonus.zuonics.sampled.ChunkTool;

public class AiffAudioOutputStreamEx extends AbstractChunkyAudioOutputStream {

    public static final class AiffIFFChunkGenerator implements AudioChunkGenerator {

        public void generateChunk(AudioFileFormat.Type fileFormat, AudioFormat format, long lLength, TDataOutputStream dos) throws IOException {
            int nCommChunkSize = ChunkTool.COMM_CHUNK_LENGTH;
            boolean aifc = fileFormat.equals(AudioFileFormat.Type.AIFC);
            if (aifc) {
                nCommChunkSize += 6;
            }
            int nHeaderSize = 4 + 8 + nCommChunkSize + 8;
            if (aifc) {
                nHeaderSize += 12;
            }
            if (lLength != AudioSystem.NOT_SPECIFIED && lLength + nHeaderSize > 0x7FFFFFFFl) {
                lLength = 0x7FFFFFFFl - nHeaderSize;
            }
            long lSSndChunkSize = (lLength != AudioSystem.NOT_SPECIFIED) ? (lLength + (lLength % 2) + 8) : AudioSystem.NOT_SPECIFIED;
            dos.writeInt(AiffTool.AIFF_FORM_MAGIC);
            dos.writeInt((lLength != AudioSystem.NOT_SPECIFIED) ? ((int) (lSSndChunkSize + nHeaderSize)) : LENGTH_NOT_KNOWN);
            if (aifc) {
                dos.writeInt(AiffTool.AIFF_AIFC_MAGIC);
                dos.writeInt(AiffTool.AIFF_FVER_MAGIC);
                dos.writeInt(4);
                dos.writeInt(AiffTool.AIFF_FVER_TIME_STAMP);
            } else {
                dos.writeInt(AiffTool.AIFF_AIFF_MAGIC);
            }
        }
    }

    public static final class AiffCOMMChunkGenerator implements AudioChunkGenerator {

        public void generateChunk(AudioFileFormat.Type fileFormat, AudioFormat format, long lLength, TDataOutputStream dos) throws IOException {
            int nCommChunkSize = ChunkTool.COMM_CHUNK_LENGTH;
            boolean aifc = fileFormat.equals(AudioFileFormat.Type.AIFC);
            if (aifc) {
                nCommChunkSize += 6;
            }
            int nFormatCode = AiffTool.getFormatCode(format);
            dos.writeInt(AiffTool.AIFF_COMM_MAGIC);
            dos.writeInt(nCommChunkSize);
            dos.writeShort((short) format.getChannels());
            dos.writeInt((lLength != AudioSystem.NOT_SPECIFIED) ? ((int) (lLength / format.getFrameSize())) : LENGTH_NOT_KNOWN);
            if (nFormatCode == AiffTool.AIFF_COMM_ULAW) {
                dos.writeShort(16);
            } else {
                dos.writeShort((short) format.getSampleSizeInBits());
            }
            writeIeeeExtended(dos, format.getSampleRate());
            if (aifc) {
                dos.writeInt(nFormatCode);
                dos.writeShort(0);
            }
        }
    }

    public static final class AiffSSNDChunkGenerator implements AudioChunkGenerator {

        public void generateChunk(AudioFileFormat.Type fileFormat, AudioFormat audioFormat, long lLength, TDataOutputStream dos) throws IOException {
            dos.writeInt(AiffTool.AIFF_SSND_MAGIC);
            dos.writeInt((lLength != AudioSystem.NOT_SPECIFIED) ? ((int) (lLength + 8)) : LENGTH_NOT_KNOWN);
            dos.writeInt(0);
            dos.writeInt(0);
        }
    }

    public static final class AiffINSTChunkGenerator implements AudioChunkGenerator {

        public void generateChunk(AudioFileFormat.Type fileFormat, AudioFormat format, long lLength, TDataOutputStream dos) throws IOException {
            Object inst = format.properties().get(AiffINSTChunk.AUDIO_FORMAT_PROPERTIES_KEY);
            if (inst instanceof AiffINSTChunk) {
                AiffINSTChunk aic = (AiffINSTChunk) inst;
                dos.writeInt(ChunkTool.AIFF_INST_MAGIC);
                dos.writeInt(ChunkTool.INST_CHUNK_LENGTH);
                dos.writeByte(aic.getBaseNote());
                dos.writeByte(aic.getDetune());
                dos.writeByte(aic.getLowNote());
                dos.writeByte(aic.getHighNote());
                dos.writeByte(aic.getlowVelocity());
                dos.writeByte(aic.getHighVelocity());
                dos.writeShort(aic.getGain());
                dos.writeShort(aic.getSustainLoop().getPlayMode());
                dos.writeShort(aic.getSustainLoop().getBeginMarkerId());
                dos.writeShort(aic.getSustainLoop().getEndMarkerId());
                dos.writeShort(aic.getReleaseLoop().getPlayMode());
                dos.writeShort(aic.getReleaseLoop().getBeginMarkerId());
                dos.writeShort(aic.getReleaseLoop().getEndMarkerId());
            }
        }
    }

    public static final class AiffMARKChunkGenerator implements AudioChunkGenerator {

        public void generateChunk(AudioFileFormat.Type fileFormat, AudioFormat format, long lLength, TDataOutputStream dos) throws IOException {
            Object inst = format.properties().get(AiffMARKChunk.AUDIO_FORMAT_PROPERTIES_KEY);
            if (inst instanceof AiffMARKChunk) {
                AiffMARKChunk amc = (AiffMARKChunk) inst;
                dos.writeInt(ChunkTool.AIFF_MARK_MAGIC);
                int numMarks = amc.getNumMarkers();
                AiffMARKChunk.Marker[] markers = amc.getMarkers();
                if (numMarks != markers.length) throw new IllegalArgumentException("AiffMARKChunkGenerator: number of marks does not equal supplid number of markers");
                dos.writeInt(ChunkTool.MIN_MARK_CHUNK_LENGTH + getTotalMarkerStructureLength(markers));
                dos.writeShort(numMarks);
                for (int i = 0; i < numMarks; i++) {
                    dos.writeShort(markers[i].getID());
                    dos.writeInt(markers[i].getPosition());
                    if (markers[i].getName().length() > 255) throw new IllegalArgumentException("AiffMARKChunkGenerator: marker names cannot exceed 255 cahrs in length");
                    byte[] nameBytes = markers[i].getName().getBytes("US-ASCII");
                    dos.writeByte(nameBytes.length);
                    for (int j = 0; j < nameBytes.length; j++) dos.writeByte(nameBytes[j]);
                    if ((nameBytes.length + 1) % 2 != 0) dos.writeByte(0);
                }
            }
        }

        static int getTotalMarkerStructureLength(AiffMARKChunk.Marker[] markers) {
            int tot = 0;
            for (int i = 0; i < markers.length; i++) tot += getMarkerStructureLength(markers[i]);
            return tot;
        }

        static int getMarkerStructureLength(AiffMARKChunk.Marker m) {
            if (m.getName().length() > 255) throw new IllegalArgumentException("AiffMARKChunkGenerator: marker names cannot exceed 255 cahrs in length");
            int len = 1 + m.getName().length();
            if (len % 2 != 0) len++;
            return len + 6;
        }
    }

    public static final List<AudioChunkGenerator> chunkGeneratorList;

    static {
        List<AudioChunkGenerator> temp = new ArrayList<AudioChunkGenerator>();
        temp.add(new AiffIFFChunkGenerator());
        temp.add(new AiffCOMMChunkGenerator());
        temp.add(new AiffMARKChunkGenerator());
        temp.add(new AiffINSTChunkGenerator());
        temp.add(new AiffSSNDChunkGenerator());
        chunkGeneratorList = Collections.unmodifiableList(temp);
    }

    public AiffAudioOutputStreamEx(AudioFormat audioFormat, AudioFileFormat.Type fileType, long l, TDataOutputStream tDataOutputStream) {
        super(decideOnFileType(audioFormat, fileType), audioFormat, l, tDataOutputStream, chunkGeneratorList, (useBasic() ? new AiffAudioOutputStream(audioFormat, decideOnFileType(audioFormat, fileType), l, tDataOutputStream) : null));
        if (l != AudioSystem.NOT_SPECIFIED && l > 0x7FFFFFFFl) {
            throw new IllegalArgumentException("AIFF files cannot be larger than 2GB.");
        }
    }

    static AudioFileFormat.Type decideOnFileType(AudioFormat af, AudioFileFormat.Type fileType) {
        if (!af.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED) && !af.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED)) {
            return AudioFileFormat.Type.AIFC;
        }
        return AudioFileFormat.Type.AIFF;
    }

    static boolean useBasic() {
        return !ChunkTool.getAssumedTrueBooleanProperty(ChunkTool.ENHANCED_AIFF_WRITING_SYSTEM_PROPERTY);
    }
}
