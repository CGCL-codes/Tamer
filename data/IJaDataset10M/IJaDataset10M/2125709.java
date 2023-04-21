package cn.edu.wuse.musicxml.sound;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;
import cn.edu.wuse.musicxml.util.MathTool;

public class SequenceWrap {

    private int numTracks;

    private float divisionType;

    private int divs[];

    public SequenceWrap(float divisionType, int[] divs, int numTracks) {
        super();
        this.divs = divs;
        this.numTracks = numTracks;
        this.divisionType = divisionType;
    }

    public Sequence createSequence() throws InvalidMidiDataException {
        if (divs.length != numTracks) throw new InvalidMidiDataException("缺少必要的divisions值,转换会产生错误.");
        return new Sequence(divisionType, MathTool.LCM(divs), numTracks);
    }

    public float getDivisionType() {
        return divisionType;
    }

    public void setDivisionType(float divisionType) {
        this.divisionType = divisionType;
    }

    public int getNumTracks() {
        return numTracks;
    }

    public void setNumTracks(int numTracks) {
        this.numTracks = numTracks;
    }

    public int[] getDivs() {
        return divs;
    }

    public void setDivs(int[] divs) {
        this.divs = divs;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("SEQUENCE PPQ(");
        buffer.append("TR");
        buffer.append(numTracks);
        buffer.append("R");
        buffer.append(MathTool.LCM(divs));
        buffer.append(")");
        return buffer.toString();
    }
}
