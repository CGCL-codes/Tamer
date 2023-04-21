package cn.edu.wuse.musicxml.parser;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;
import cn.edu.wuse.musicxml.sound.ChannelProgram;
import cn.edu.wuse.musicxml.sound.ChannelProgramEvent;
import cn.edu.wuse.musicxml.sound.ChannelTouchEvent;
import cn.edu.wuse.musicxml.sound.ControlChangeEvent;
import cn.edu.wuse.musicxml.sound.Key;
import cn.edu.wuse.musicxml.sound.KeyEvent;
import cn.edu.wuse.musicxml.sound.Note;
import cn.edu.wuse.musicxml.sound.NoteEvent;
import cn.edu.wuse.musicxml.sound.PitchBendEvent;
import cn.edu.wuse.musicxml.sound.PolyphonicEvent;
import cn.edu.wuse.musicxml.sound.ProgrammeChange;
import cn.edu.wuse.musicxml.sound.ProgrammeChangeEvent;
import cn.edu.wuse.musicxml.sound.SequenceEvent;
import cn.edu.wuse.musicxml.sound.SequenceWrap;
import cn.edu.wuse.musicxml.sound.Tempo;
import cn.edu.wuse.musicxml.sound.TempoEvent;
import cn.edu.wuse.musicxml.sound.Text;
import cn.edu.wuse.musicxml.sound.TextEvent;
import cn.edu.wuse.musicxml.sound.Tick;
import cn.edu.wuse.musicxml.sound.TickEvent;
import cn.edu.wuse.musicxml.sound.Time;
import cn.edu.wuse.musicxml.sound.TimeEvent;
import cn.edu.wuse.musicxml.sound.UnPitchChange;
import cn.edu.wuse.musicxml.sound.UnPitchChangeEvent;
import cn.edu.wuse.musicxml.sound.Velocity;
import cn.edu.wuse.musicxml.sound.VelocityEvent;

/**
 * will product the sequence,hold the complex data struct,let the customer
 * programmer not focus on the data processing,just get the sax element and 
 * fire the suitable event.
 */
public class BaseMidiRenderer extends MusicParserAdpter {

    /**
	 * product Java midi sequence.
	 */
    protected Sequence sequence;

    /**
	 * the resolution of the song.
	 */
    protected int resolution;

    /**
	 * the tick position of each track.
	 */
    protected long[][] ticks;

    /**
	 * some track may be precussion,store the midi precussion value for each track.
	 */
    protected int[] unpitched;

    /**
	 * the latest velocity of each track.
	 */
    protected int[] velocities;

    /**
	 * store the midi channel value of each track,the index of the array is the track index.
	 */
    protected int[] channels;

    /**
	 * store the divisions of each musicxml part.
	 */
    protected int[] divs;

    /**
	 * 一个sequence是MusicParserlistener具体实现类的产品.默认的抽象基类给出一个平庸的sequence，
	 * 由用户自己渲染具体的音符发音信息.
	 * @param Sequence sequence
	 * add a sequence to the listener,this sequence will be
	 * the product of this listener.
	 */
    public void addSequenceEvent(SequenceEvent se) throws InvalidMidiDataException {
        SequenceWrap sw = se.getSequenceWrap();
        this.sequence = sw.createSequence();
        this.ticks = new long[sw.getNumTracks()][2];
        this.velocities = new int[sw.getNumTracks()];
        this.channels = new int[sw.getNumTracks()];
        this.divs = sw.getDivs();
        this.resolution = sequence.getResolution();
        this.unpitched = new int[sw.getNumTracks()];
        for (int i = 0; i < ticks.length; i++) {
            unpitched[i] = 34;
            ticks[i][0] = Long.MIN_VALUE;
            ticks[i][1] = 0;
            velocities[i] = 96;
            channels[i] = i % 16;
        }
    }

    /** *************************************************************************
	 * Dynamics is define for each note,soore the current v to press the key
	 * 以下事件不产生midi事件，只是用来维护在实现类中的关键数组，本设计中在抽象的接口中维持了
	 * 转换xml时的复杂的计算，留给客户编程人员只需要找到相应的结点，发送适当的消息即可。
	 * **************************************************************************
	 * musicxml 中音符的力度可能没定音在note附近，用于临时修改按键力度信息，在
	 * MusicXmlParserListener实现类中用一维数组维护此信息
	 * **************************************************************************
	 * Velocity表现一个midi键盘按下的速度，在sound中的dynamics中给出,由于和dynamics结点表现的
	 * 控制器事件有混淆，给sound的属性结点dynamics对应的信息改名为Velocity，此消息只是维护实现类
	 * 中的重要数组，不发送midi消息。
	 */
    public void addVelocityEvent(VelocityEvent ve) throws IndexOutOfBoundsException {
        Velocity ds = ve.getVelocity();
        if (ds.getTrack() < 0 || ds.getTrack() > (velocities.length - 1)) throw new IndexOutOfBoundsException();
        this.velocities[ds.getTrack()] = ds.getDynamics();
    }

    /**
	 * 对应midi-program,默认情况下track按招顺序1-16轮流的排列，midi-program修改具体的
	 * track放到的channel号，Channel事件维护MusicXmlParserListener的trackChannel数组
	 * 此事件不发送midi消息，仅仅是维护实现类中的数组了
	 */
    public void addChannelProgramEvent(ChannelProgramEvent ce) throws IndexOutOfBoundsException {
        ChannelProgram c = ce.getChannel();
        if (c.getTrack() < 0 || c.getTrack() > (channels.length - 1)) throw new IndexOutOfBoundsException();
        channels[c.getTrack()] = c.getChannel();
    }

    /**
	 * 表明歌曲的基调，此事件将发送midi MetaMessage
	 * 59 01011001 02 sf mi key signature
	 * sf=sharps/flats (-7=7 flats, 0=key of c,7=7 sharps)
	 * mi=major/minor (0=major, 1=minor)
	 * to indicate the key,MetatMessage in java midi
	 * @param kwrapper
	 */
    public void addKeySignatureEvent(KeyEvent ke) throws InvalidMidiDataException {
        Key k = ke.getKey();
        Track t = sequence.getTracks()[k.getTrack()];
        t.add(new MidiEvent(k.createMessage()[0], ticks[k.getTrack()][1]));
    }

    /**
	 * 音符事件是对应midi中的note-on note-off的，此事件将在sequence上给midi发送2个ShortMessage消息
	 * @param noteWrapper a special note is a absolute pronouncing note,shortMessage
	 * @param tno tick number to fire the event
	 */
    public void addNoteEvent(NoteEvent ne) throws InvalidMidiDataException {
        Note nw = ne.getNote();
        if (nw.isUnpitch()) {
            nw.setChannel(9);
            nw.setKey(unpitched[nw.getTrack()]);
        } else nw.setChannel(channels[nw.getTrack()]);
        nw.setVelocity(velocities[nw.getTrack()]);
        nw.setDuration(resolution * nw.getDuration() / divs[nw.getTrack()]);
        MidiMessage[] m = nw.createMessage();
        Track t = sequence.getTracks()[nw.getTrack()];
        if (nw.isChord()) {
            t.add(new MidiEvent(m[0], ticks[nw.getTrack()][0]));
            t.add(new MidiEvent(m[1], ticks[nw.getTrack()][0] + nw.getDuration()));
        } else {
            t.add(new MidiEvent(m[0], ticks[nw.getTrack()][1]));
            ticks[nw.getTrack()][0] = ticks[nw.getTrack()][1];
            ticks[nw.getTrack()][1] += nw.getDuration();
            t.add(new MidiEvent(m[1], ticks[nw.getTrack()][1]));
        }
    }

    /**
	 * midi says:
	 * cx 1100xxxx pp program (patch) change
	 * pp=new program number
	 * @param pc
	 * @throws InvalidMidiDataException
	 */
    public void addProgramChange(ProgrammeChangeEvent pe) throws InvalidMidiDataException {
        ProgrammeChange pc = pe.getProgrammeChange();
        Track t = sequence.getTracks()[pc.getTrack()];
        pc.setChanel(channels[pc.getTrack()]);
        t.add(new MidiEvent(pc.createMessage()[0], ticks[pc.getTrack()][1]));
    }

    /**
	 * 一个tempo封装了一个midi MetaMessage用于给歌曲设置速度
	 * midi says:51 01010001 03 tttttt set tempo
	 * tttttt=microseconds/quarter note
	 * the meta message tempo wrapper,MetaMessage in java midi
	 * @param tpowrap
	 */
    public void addTempoEvent(TempoEvent te) throws InvalidMidiDataException {
        Tempo tw = te.getTempo();
        Track t = sequence.getTracks()[tw.getTrack()];
        t.add(new MidiEvent(tw.createMessage()[0], ticks[tw.getTrack()][1]));
    }

    /**
	 * 给midi发送MetaMessage文本信息
	 * add text to midi,metaMessage in java midi
	 * @param twrapper
	 */
    public void addTextEvent(TextEvent te) throws InvalidMidiDataException {
        Text tw = te.getText();
        Track t = sequence.getTracks()[tw.getTrack()];
        t.add(new MidiEvent(tw.createMessage()[0], ticks[tw.getTrack()][1]));
    }

    /**
	 * 58 01011000 04 nn dd ccbb time signature
	 * nn=numerator of time sig.
	 * dd=denominator of time sig. 2=quarter
	 * 3=eighth, etc.
	 * cc=number of ticks in metronome click
	 * bb=number of 32nd notes to the quarter
	 * note
	 * thos are MetaMessage in java midi.
	 */
    public void addTimeSignatureEvent(TimeEvent te) throws InvalidMidiDataException {
        Time t = te.getTime();
        t.setCc(resolution);
        Track tr = sequence.getTracks()[t.getTrack()];
        tr.add(new MidiEvent(t.createMessage()[0], ticks[t.getTrack()][1]));
    }

    /**
	 * midi says:
	 * bx 1011xxxx cc vv control change
	 * cc=controller numbervv=new value
	 * @param cc
	 * @throws InvalidMidiDataException
	 * @TODO
	 */
    public void addControlChange(ControlChangeEvent cc) throws InvalidMidiDataException {
        super.addControlChange(cc);
    }

    /**
	 * 此事件发生在触摸键盘后的事件,发送midi shortmessage
	 * those are ShortMessage in java midi based on track and channel.
	 */
    public void addPolyphonicEvent(PolyphonicEvent kt) throws InvalidMidiDataException {
        super.addPolyphonicEvent(kt);
    }

    /**
	 * dx 1101xxxx cc channel after-touch
	 * cc=channel number
	 * @param ct
	 * @throws InvalidMidiDataException
	 */
    public void addChannelTouch(ChannelTouchEvent ct) throws InvalidMidiDataException {
        super.addChannelTouch(ct);
    }

    /**
	 * 常说的弯音轮事件，
	 * ex 1110xxxx bb tt pitch wheel change (2000h is normal or no
	 * change)
	 * bb=bottom (least sig) 7 bits of value
	 * tt=top (most sig) 7 bits of value
	 * @param pb
	 * @throws InvalidMidiDataException
	 */
    public void addPitchWheelChangeEvent(PitchBendEvent pb) throws InvalidMidiDataException {
        super.addPitchWheelChangeEvent(pb);
    }

    /**
	 * musicxml结点如backup forward等只改变当前的tick值，不用向Sequence上发消息，包装
	 * 这类信息的事件
	 * 
	 */
    public void addTickEvent(TickEvent te) throws IndexOutOfBoundsException {
        Tick t = te.getTick();
        t.setDuration(t.getDuration() * resolution / divs[t.getTrack()]);
        ticks[t.getTrack()][0] = ticks[t.getTrack()][1];
        ticks[t.getTrack()][1] += t.getDuration();
    }

    /**
	 * UnPitchChange struct see UnPitchChange.class
	 * this message only fire a event to change the unpitched array,
	 * will not add message on sequence track.
	 */
    public void addUnPitchChangeEvent(UnPitchChangeEvent upce) throws IndexOutOfBoundsException {
        UnPitchChange upc = upce.getUnPitchChange();
        this.unpitched[upc.getTrack()] = upc.getValue();
    }

    /** *****************************************************************
	 * here is the product.
	 * *****************************************************************/
    public Sequence getSequence() {
        return this.sequence;
    }
}
