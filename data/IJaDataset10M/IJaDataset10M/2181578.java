package org.nightlabs.i18n.unit.resolution;

/**
 * This class is the implementation of the {@link Resolution}-interface
 * 
 * @author Daniel.Mazurek <at> NightLabs <dot> de
 */
public class ResolutionImpl implements Resolution {

    /**
	 * The serial version of this class.
	 */
    private static final long serialVersionUID = 1L;

    private IResolutionUnit resolutionUnit = new DPIResolutionUnit();

    private double resolutionX = DEFAULT_RESOLUTION_DPI;

    private double resolutionY = DEFAULT_RESOLUTION_DPI;

    public ResolutionImpl(IResolutionUnit unit, double resolutionX, double resolutionY) {
        this.resolutionUnit = unit;
        this.resolutionX = resolutionX;
        this.resolutionY = resolutionY;
    }

    public ResolutionImpl(IResolutionUnit unit, double resolution) {
        this.resolutionUnit = unit;
        this.resolutionX = resolution;
        this.resolutionY = resolution;
    }

    public ResolutionImpl() {
        this.resolutionUnit = new DPIResolutionUnit();
        this.resolutionX = DEFAULT_RESOLUTION_DPI;
        this.resolutionY = DEFAULT_RESOLUTION_DPI;
    }

    public IResolutionUnit getResolutionUnit() {
        return resolutionUnit;
    }

    /**
	 * {@inheritDoc}
	 * <p>
	 * For the default value see {@link #DEFAULT_RESOLUTION_DPI}
	 */
    public double getResolutionX() {
        return resolutionX;
    }

    public void setResolutionX(double value) {
        this.resolutionX = value;
    }

    /**
	 * {@inheritDoc}
	 * <p>
	 * For the default value see {@link #DEFAULT_RESOLUTION_DPI}
	 */
    public double getResolutionY() {
        return resolutionY;
    }

    public void setResolutionY(double value) {
        this.resolutionY = value;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("How the hell can clone fail?!", e);
        }
    }

    public double getResolutionX(IResolutionUnit unit) {
        if (unit.equals(resolutionUnit)) return getResolutionX(); else {
            double oldfactor = resolutionUnit.getUnit().getFactor();
            return (resolutionX * unit.getUnit().getFactor()) / oldfactor;
        }
    }

    public double getResolutionY(IResolutionUnit unit) {
        if (unit.equals(resolutionUnit)) return getResolutionY(); else {
            double oldfactor = resolutionUnit.getUnit().getFactor();
            return (resolutionY * unit.getUnit().getFactor()) / oldfactor;
        }
    }

    public void setResolutionUnit(IResolutionUnit unit) {
        if (unit.getUnit().getFactor() != this.resolutionUnit.getUnit().getFactor()) {
            double oldfactor = resolutionUnit.getUnit().getFactor();
            resolutionX = (resolutionX * unit.getUnit().getFactor()) / oldfactor;
            resolutionY = (resolutionY * unit.getUnit().getFactor()) / oldfactor;
        }
        this.resolutionUnit = unit;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("resolutionX = " + resolutionX);
        sb.append(", ");
        sb.append("resolutionY = " + resolutionY);
        sb.append(" ");
        if (resolutionUnit != null) {
            sb.append(resolutionUnit.getName().getText());
        }
        return sb.toString();
    }
}
