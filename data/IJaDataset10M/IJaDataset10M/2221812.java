package net.sourceforge.entrainer.xml.program;

/**
 * This interface allows access to either the start or the end values as determined
 * by the implementations.  Adding a property change listener allows objects to listen
 * in on changes to the values.
 *  
 * @author burton
 *
 */
public interface UnitSetter {

    public void setAmplitude(double d);

    public double getAmplitude();

    public void setEntrainmentFrequency(double d);

    public double getEntrainmentFrequency();

    public void setFrequency(double d);

    public double getFrequency();

    public void setPinkEntrainerMultiple(double d);

    public double getPinkEntrainerMultiple();

    public void setPinkNoise(double d);

    public double getPinkNoise();

    public void setPinkPanAmplitude(double d);

    public double getPinkPanAmplitude();

    public EntrainerProgramUnit getUnit();
}
