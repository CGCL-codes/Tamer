package plecotus.models.bwin.extension;

import plecotus.exception.IsDeadException;
import plecotus.extension.TreeExtension;
import plecotus.unit.Length;
import plecotus.unit.Time;

public interface BwinTreeExtension extends TreeExtension {

    public abstract Length calcHPotRel(Time duration);

    public abstract Length calcNewHeight(Time duration) throws IsDeadException;

    public abstract Length calcNewDiameter(Time duration) throws IsDeadException;

    public abstract void checkMortality();

    public double calcEKL();

    public void initMaxCrownWidth();

    public void initCrownBase();

    public abstract double getActiveMort();

    public abstract void setActiveMort(double d);

    public abstract double getC66();

    public abstract void setC66(double c66);

    public abstract double getC66c();

    public abstract void setC66c(double d);

    public Length getHbon();

    public void setHbon(Length hbon);

    public String getNumber();

    public void setNumber(String number);
}
