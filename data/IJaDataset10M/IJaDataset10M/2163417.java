package org.herac.tuxguitar.io.tg.v11;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import org.herac.tuxguitar.gm.GMChannelRoute;
import org.herac.tuxguitar.gm.GMChannelRouter;
import org.herac.tuxguitar.gm.GMChannelRouterConfigurator;
import org.herac.tuxguitar.io.base.TGFileFormat;
import org.herac.tuxguitar.io.base.TGFileFormatException;
import org.herac.tuxguitar.io.base.TGLocalFileExporter;
import org.herac.tuxguitar.song.factory.TGFactory;
import org.herac.tuxguitar.song.managers.TGSongManager;
import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGChannel;
import org.herac.tuxguitar.song.models.TGChord;
import org.herac.tuxguitar.song.models.TGColor;
import org.herac.tuxguitar.song.models.TGDuration;
import org.herac.tuxguitar.song.models.TGLyric;
import org.herac.tuxguitar.song.models.TGMarker;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGMeasureHeader;
import org.herac.tuxguitar.song.models.TGNote;
import org.herac.tuxguitar.song.models.TGNoteEffect;
import org.herac.tuxguitar.song.models.TGSong;
import org.herac.tuxguitar.song.models.TGString;
import org.herac.tuxguitar.song.models.TGStroke;
import org.herac.tuxguitar.song.models.TGTempo;
import org.herac.tuxguitar.song.models.TGText;
import org.herac.tuxguitar.song.models.TGTimeSignature;
import org.herac.tuxguitar.song.models.TGTrack;
import org.herac.tuxguitar.song.models.TGDivisionType;
import org.herac.tuxguitar.song.models.TGVoice;
import org.herac.tuxguitar.song.models.effects.TGEffectBend;
import org.herac.tuxguitar.song.models.effects.TGEffectGrace;
import org.herac.tuxguitar.song.models.effects.TGEffectHarmonic;
import org.herac.tuxguitar.song.models.effects.TGEffectTremoloBar;
import org.herac.tuxguitar.song.models.effects.TGEffectTremoloPicking;
import org.herac.tuxguitar.song.models.effects.TGEffectTrill;

/**
 * @author julian
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TGOutputStream extends TGStream implements TGLocalFileExporter {

    private TGFactory factory;

    private GMChannelRouter channelRouter;

    private DataOutputStream dataOutputStream;

    public boolean isSupportedExtension(String extension) {
        return (extension.toLowerCase().equals(TG_FORMAT_EXTENSION));
    }

    public String getExportName() {
        return "TuxGuitar 1.1";
    }

    public TGFileFormat getFileFormat() {
        return new TGFileFormat("TuxGuitar", "*.tg");
    }

    public boolean configure(boolean setDefaults) {
        return true;
    }

    public void init(TGFactory factory, OutputStream stream) {
        this.factory = factory;
        this.dataOutputStream = new DataOutputStream(stream);
    }

    public void exportSong(TGSong song) throws TGFileFormatException {
        try {
            this.configureChannelRouter(song);
            this.writeVersion();
            this.write(song);
            this.dataOutputStream.flush();
            this.dataOutputStream.close();
        } catch (Throwable throwable) {
            throw new TGFileFormatException(throwable);
        }
    }

    private void writeVersion() {
        writeUnsignedByteString(TG_FORMAT_VERSION);
    }

    private void write(TGSong song) {
        writeUnsignedByteString(song.getName());
        writeUnsignedByteString(song.getArtist());
        writeUnsignedByteString(song.getAlbum());
        writeUnsignedByteString(song.getAuthor());
        writeShort((short) song.countMeasureHeaders());
        TGMeasureHeader lastHeader = null;
        Iterator headers = song.getMeasureHeaders();
        while (headers.hasNext()) {
            TGMeasureHeader header = (TGMeasureHeader) headers.next();
            writeMeasureHeader(header, lastHeader);
            lastHeader = header;
        }
        writeByte(song.countTracks());
        for (int i = 0; i < song.countTracks(); i++) {
            TGTrack track = song.getTrack(i);
            writeTrack(track);
        }
    }

    private void writeTrack(TGTrack track) {
        int header = 0;
        if (track.isSolo()) {
            header |= TRACK_SOLO;
        }
        if (track.isMute()) {
            header |= TRACK_MUTE;
        }
        if (!track.getLyrics().isEmpty()) {
            header |= TRACK_LYRICS;
        }
        writeHeader(header);
        writeUnsignedByteString(track.getName());
        writeChannel(getChannel(track.getSong(), track));
        TGMeasure lastMeasure = null;
        Iterator measures = track.getMeasures();
        while (measures.hasNext()) {
            TGMeasure measure = (TGMeasure) measures.next();
            writeMeasure(measure, lastMeasure);
            lastMeasure = measure;
        }
        writeByte(track.getStrings().size());
        Iterator stringIt = track.getStrings().iterator();
        while (stringIt.hasNext()) {
            TGString string = (TGString) stringIt.next();
            writeInstrumentString(string);
        }
        writeByte(track.getOffset() - TGTrack.MIN_OFFSET);
        writeRGBColor(track.getColor());
        if (((header & TRACK_LYRICS) != 0)) {
            writeLyrics(track.getLyrics());
        }
    }

    private void writeMeasureHeader(TGMeasureHeader measureheader, TGMeasureHeader lastMeasureHeader) {
        int header = 0;
        if (lastMeasureHeader == null) {
            header |= MEASURE_HEADER_TIMESIGNATURE;
            header |= MEASURE_HEADER_TEMPO;
            if (measureheader.getTripletFeel() != TGMeasureHeader.TRIPLET_FEEL_NONE) {
                header |= MEASURE_HEADER_TRIPLET_FEEL;
            }
        } else {
            int numerator = measureheader.getTimeSignature().getNumerator();
            int value = measureheader.getTimeSignature().getDenominator().getValue();
            int prevNumerator = lastMeasureHeader.getTimeSignature().getNumerator();
            int prevValue = lastMeasureHeader.getTimeSignature().getDenominator().getValue();
            if (numerator != prevNumerator || value != prevValue) {
                header |= MEASURE_HEADER_TIMESIGNATURE;
            }
            if (measureheader.getTempo().getValue() != lastMeasureHeader.getTempo().getValue()) {
                header |= MEASURE_HEADER_TEMPO;
            }
            if (measureheader.getTripletFeel() != lastMeasureHeader.getTripletFeel()) {
                header |= MEASURE_HEADER_TRIPLET_FEEL;
            }
        }
        header = (measureheader.isRepeatOpen()) ? header |= MEASURE_HEADER_REPEAT_OPEN : header;
        header = (measureheader.getRepeatClose() > 0) ? header |= MEASURE_HEADER_REPEAT_CLOSE : header;
        header = (measureheader.getRepeatAlternative() > 0) ? header |= MEASURE_HEADER_REPEAT_ALTERNATIVE : header;
        header = (measureheader.hasMarker()) ? header |= MEASURE_HEADER_MARKER : header;
        writeHeader(header);
        if (((header & MEASURE_HEADER_TIMESIGNATURE) != 0)) {
            writeTimeSignature(measureheader.getTimeSignature());
        }
        if (((header & MEASURE_HEADER_TEMPO) != 0)) {
            writeTempo(measureheader.getTempo());
        }
        if (((header & MEASURE_HEADER_REPEAT_CLOSE) != 0)) {
            writeShort((short) measureheader.getRepeatClose());
        }
        if (((header & MEASURE_HEADER_REPEAT_ALTERNATIVE) != 0)) {
            writeByte(measureheader.getRepeatAlternative());
        }
        if (((header & MEASURE_HEADER_MARKER) != 0)) {
            writeMarker(measureheader.getMarker());
        }
        if (((header & MEASURE_HEADER_TRIPLET_FEEL) != 0)) {
            writeByte(measureheader.getTripletFeel());
        }
    }

    private void writeMeasure(TGMeasure measure, TGMeasure lastMeasure) {
        int header = 0;
        if (lastMeasure == null) {
            header |= MEASURE_CLEF;
            header |= MEASURE_KEYSIGNATURE;
        } else {
            if (measure.getClef() != lastMeasure.getClef()) {
                header |= MEASURE_CLEF;
            }
            if (measure.getKeySignature() != lastMeasure.getKeySignature()) {
                header |= MEASURE_KEYSIGNATURE;
            }
        }
        writeHeader(header);
        TGBeatData data = new TGBeatData(measure);
        writeBeats(measure, data);
        if (((header & MEASURE_CLEF) != 0)) {
            writeByte(measure.getClef());
        }
        if (((header & MEASURE_KEYSIGNATURE) != 0)) {
            writeByte(measure.getKeySignature());
        }
    }

    private void writeChannel(TGChannel channel) {
        GMChannelRoute gmChannelRoute = getChannelRoute(channel.getChannelId());
        writeByte(gmChannelRoute.getChannel1());
        writeByte(gmChannelRoute.getChannel2());
        writeByte(channel.getProgram());
        writeByte(channel.getVolume());
        writeByte(channel.getBalance());
        writeByte(channel.getChorus());
        writeByte(channel.getReverb());
        writeByte(channel.getPhaser());
        writeByte(channel.getTremolo());
    }

    private void writeBeats(TGMeasure measure, TGBeatData data) {
        int count = measure.countBeats();
        for (int i = 0; i < count; i++) {
            TGBeat beat = measure.getBeat(i);
            writeBeat(beat, data, (i + 1 < count));
        }
    }

    private void writeBeat(TGBeat beat, TGBeatData data, boolean hasNext) {
        int header = hasNext ? BEAT_HAS_NEXT : 0;
        for (int i = 0; i < TGBeat.MAX_VOICES; i++) {
            int shift = (i * 2);
            if (!beat.getVoice(i).isEmpty()) {
                header |= (BEAT_HAS_VOICE << shift);
                int flags = (beat.getVoice(i).isRestVoice() ? 0 : VOICE_HAS_NOTES);
                if (!beat.getVoice(i).getDuration().isEqual(data.getVoice(i).getDuration())) {
                    flags |= VOICE_NEXT_DURATION;
                    data.getVoice(i).setDuration(beat.getVoice(i).getDuration());
                }
                if (beat.getVoice(i).getDirection() != TGVoice.DIRECTION_NONE) {
                    if (beat.getVoice(i).getDirection() == TGVoice.DIRECTION_UP) {
                        flags |= VOICE_DIRECTION_UP;
                    } else if (beat.getVoice(i).getDirection() == TGVoice.DIRECTION_DOWN) {
                        flags |= VOICE_DIRECTION_DOWN;
                    }
                }
                if (data.getVoice(i).getFlags() != flags) {
                    header |= (BEAT_HAS_VOICE_CHANGES << shift);
                    data.getVoice(i).setFlags(flags);
                }
            }
        }
        if (beat.getStroke().getDirection() != TGStroke.STROKE_NONE) {
            header |= BEAT_HAS_STROKE;
        }
        if (beat.getChord() != null) {
            header |= BEAT_HAS_CHORD;
        }
        if (beat.getText() != null) {
            header |= BEAT_HAS_TEXT;
        }
        writeHeader(header);
        writeVoices(header, beat, data);
        if (((header & BEAT_HAS_STROKE) != 0)) {
            writeStroke(beat.getStroke());
        }
        if (((header & BEAT_HAS_CHORD) != 0)) {
            writeChord(beat.getChord());
        }
        if (((header & BEAT_HAS_TEXT) != 0)) {
            writeText(beat.getText());
        }
    }

    private void writeVoices(int header, TGBeat beat, TGBeatData data) {
        for (int i = 0; i < TGBeat.MAX_VOICES; i++) {
            int shift = (i * 2);
            if (((header & (BEAT_HAS_VOICE << shift)) != 0)) {
                if (((header & (BEAT_HAS_VOICE_CHANGES << shift)) != 0)) {
                    writeHeader(data.getVoice(i).getFlags());
                }
                if (((data.getVoice(i).getFlags() & VOICE_NEXT_DURATION) != 0)) {
                    writeDuration(beat.getVoice(i).getDuration());
                }
                if (((data.getVoice(i).getFlags() & VOICE_HAS_NOTES) != 0)) {
                    writeNotes(beat.getVoice(i), data);
                }
            }
        }
    }

    private void writeNotes(TGVoice voice, TGBeatData data) {
        for (int i = 0; i < voice.countNotes(); i++) {
            TGNote note = voice.getNote(i);
            int header = (i + 1 < voice.countNotes() ? NOTE_HAS_NEXT : 0);
            header = (note.isTiedNote()) ? header |= NOTE_TIED : header;
            if (note.getVelocity() != data.getVoice(voice.getIndex()).getVelocity()) {
                data.getVoice(voice.getIndex()).setVelocity(note.getVelocity());
                header |= NOTE_VELOCITY;
            }
            header = (note.getEffect().hasAnyEffect()) ? header |= NOTE_EFFECT : header;
            writeHeader(header);
            writeNote(header, note);
        }
    }

    private void writeNote(int header, TGNote note) {
        writeByte(note.getValue());
        writeByte(note.getString());
        if (((header & NOTE_VELOCITY) != 0)) {
            writeByte(note.getVelocity());
        }
        if (((header & NOTE_EFFECT) != 0)) {
            writeNoteEffect(note.getEffect());
        }
    }

    private void writeStroke(TGStroke stroke) {
        writeByte(stroke.getDirection());
        writeByte(stroke.getValue());
    }

    private void writeChord(TGChord chord) {
        writeByte(chord.countStrings());
        writeUnsignedByteString(chord.getName());
        writeByte(chord.getFirstFret());
        for (int string = 0; string < chord.countStrings(); string++) {
            writeByte(chord.getFretValue(string));
        }
    }

    private void writeText(TGText text) {
        writeUnsignedByteString(text.getValue());
    }

    private void writeInstrumentString(TGString string) {
        writeByte(string.getValue());
    }

    private void writeTempo(TGTempo tempo) {
        writeShort((short) tempo.getValue());
    }

    private void writeTimeSignature(TGTimeSignature timeSignature) {
        writeByte(timeSignature.getNumerator());
        writeDuration(timeSignature.getDenominator());
    }

    private void writeDuration(TGDuration duration) {
        int header = 0;
        header = (duration.isDotted()) ? header |= DURATION_DOTTED : header;
        header = (duration.isDoubleDotted()) ? header |= DURATION_DOUBLE_DOTTED : header;
        header = (!duration.getDivision().isEqual(TGDivisionType.NORMAL)) ? header |= DURATION_NO_TUPLET : header;
        writeHeader(header);
        writeByte(duration.getValue());
        if (((header & DURATION_NO_TUPLET) != 0)) {
            writeDivisionType(duration.getDivision());
        }
    }

    private void writeDivisionType(TGDivisionType divisionType) {
        writeByte(divisionType.getEnters());
        writeByte(divisionType.getTimes());
    }

    private void writeNoteEffect(TGNoteEffect effect) {
        int header = 0;
        header = (effect.isBend()) ? header |= EFFECT_BEND : header;
        header = (effect.isTremoloBar()) ? header |= EFFECT_TREMOLO_BAR : header;
        header = (effect.isHarmonic()) ? header |= EFFECT_HARMONIC : header;
        header = (effect.isGrace()) ? header |= EFFECT_GRACE : header;
        header = (effect.isTrill()) ? header |= EFFECT_TRILL : header;
        header = (effect.isTremoloPicking()) ? header |= EFFECT_TREMOLO_PICKING : header;
        header = (effect.isVibrato()) ? header |= EFFECT_VIBRATO : header;
        header = (effect.isDeadNote()) ? header |= EFFECT_DEAD : header;
        header = (effect.isSlide()) ? header |= EFFECT_SLIDE : header;
        header = (effect.isHammer()) ? header |= EFFECT_HAMMER : header;
        header = (effect.isGhostNote()) ? header |= EFFECT_GHOST : header;
        header = (effect.isAccentuatedNote()) ? header |= EFFECT_ACCENTUATED : header;
        header = (effect.isHeavyAccentuatedNote()) ? header |= EFFECT_HEAVY_ACCENTUATED : header;
        header = (effect.isPalmMute()) ? header |= EFFECT_PALM_MUTE : header;
        header = (effect.isStaccato()) ? header |= EFFECT_STACCATO : header;
        header = (effect.isTapping()) ? header |= EFFECT_TAPPING : header;
        header = (effect.isSlapping()) ? header |= EFFECT_SLAPPING : header;
        header = (effect.isPopping()) ? header |= EFFECT_POPPING : header;
        header = (effect.isFadeIn()) ? header |= EFFECT_FADE_IN : header;
        writeHeader(header, 3);
        if (((header & EFFECT_BEND) != 0)) {
            writeBendEffect(effect.getBend());
        }
        if (((header & EFFECT_TREMOLO_BAR) != 0)) {
            writeTremoloBarEffect(effect.getTremoloBar());
        }
        if (((header & EFFECT_HARMONIC) != 0)) {
            writeHarmonicEffect(effect.getHarmonic());
        }
        if (((header & EFFECT_GRACE) != 0)) {
            writeGraceEffect(effect.getGrace());
        }
        if (((header & EFFECT_TRILL) != 0)) {
            writeTrillEffect(effect.getTrill());
        }
        if (((header & EFFECT_TREMOLO_PICKING) != 0)) {
            writeTremoloPickingEffect(effect.getTremoloPicking());
        }
    }

    private void writeBendEffect(TGEffectBend effect) {
        writeByte(effect.getPoints().size());
        Iterator it = effect.getPoints().iterator();
        while (it.hasNext()) {
            TGEffectBend.BendPoint point = (TGEffectBend.BendPoint) it.next();
            writeByte(point.getPosition());
            writeByte(point.getValue());
        }
    }

    private void writeTremoloBarEffect(TGEffectTremoloBar effect) {
        writeByte(effect.getPoints().size());
        Iterator it = effect.getPoints().iterator();
        while (it.hasNext()) {
            TGEffectTremoloBar.TremoloBarPoint point = (TGEffectTremoloBar.TremoloBarPoint) it.next();
            writeByte(point.getPosition());
            writeByte((point.getValue() + TGEffectTremoloBar.MAX_VALUE_LENGTH));
        }
    }

    private void writeHarmonicEffect(TGEffectHarmonic effect) {
        writeByte(effect.getType());
        if (effect.getType() != TGEffectHarmonic.TYPE_NATURAL) {
            writeByte(effect.getData());
        }
    }

    private void writeGraceEffect(TGEffectGrace effect) {
        int header = 0;
        header = (effect.isDead()) ? header |= GRACE_FLAG_DEAD : header;
        header = (effect.isOnBeat()) ? header |= GRACE_FLAG_ON_BEAT : header;
        writeHeader(header);
        writeByte(effect.getFret());
        writeByte(effect.getDuration());
        writeByte(effect.getDynamic());
        writeByte(effect.getTransition());
    }

    private void writeTremoloPickingEffect(TGEffectTremoloPicking effect) {
        writeByte(effect.getDuration().getValue());
    }

    private void writeTrillEffect(TGEffectTrill effect) {
        writeByte(effect.getFret());
        writeByte(effect.getDuration().getValue());
    }

    private void writeMarker(TGMarker marker) {
        writeUnsignedByteString(marker.getTitle());
        writeRGBColor(marker.getColor());
    }

    private void writeRGBColor(TGColor color) {
        writeByte(color.getR());
        writeByte(color.getG());
        writeByte(color.getB());
    }

    private void writeLyrics(TGLyric lyrics) {
        writeShort((short) lyrics.getFrom());
        writeIntegerString(lyrics.getLyrics());
    }

    public void writeByte(int v) {
        try {
            this.dataOutputStream.write(v);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeUnsignedByteString(String v) {
        try {
            String byteString = (v == null ? new String() : ((v.length() > 0xFF) ? v.substring(0, 0xFF) : v));
            this.dataOutputStream.write(byteString.length());
            this.dataOutputStream.writeChars(byteString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeIntegerString(String v) {
        try {
            this.dataOutputStream.writeInt(v.length());
            this.dataOutputStream.writeChars(v);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeHeader(int v) {
        try {
            this.dataOutputStream.write(v);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeHeader(int v, int bCount) {
        for (int i = bCount; i > 0; i--) {
            writeHeader((v >>> ((8 * i) - 8)) & 0xFF);
        }
    }

    public void writeShort(short v) {
        try {
            this.dataOutputStream.writeShort(v);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TGChannel getChannel(TGSong song, TGTrack track) {
        TGSongManager tgSongManager = new TGSongManager(this.factory);
        tgSongManager.setSong(song);
        TGChannel tgChannel = tgSongManager.getChannel(track.getChannelId());
        if (tgChannel == null) {
            tgChannel = this.factory.newChannel();
        }
        return tgChannel;
    }

    private GMChannelRoute getChannelRoute(int channelId) {
        GMChannelRoute gmChannelRoute = this.channelRouter.getRoute(channelId);
        if (gmChannelRoute == null) {
            gmChannelRoute = new GMChannelRoute(GMChannelRoute.NULL_VALUE);
            gmChannelRoute.setChannel1(15);
            gmChannelRoute.setChannel2(15);
        }
        return gmChannelRoute;
    }

    private void configureChannelRouter(TGSong song) {
        this.channelRouter = new GMChannelRouter();
        GMChannelRouterConfigurator gmChannelRouterConfigurator = new GMChannelRouterConfigurator(this.channelRouter);
        gmChannelRouterConfigurator.configureRouter(song);
    }
}
