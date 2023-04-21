package gov.sns.apps.jeri.data;

/**
 * Provides a class to hold the data from the MAG_DVC table.
 * 
 * @author Chris Fowlkes
 */
public class Magnet extends Device {

    /**
   * Holds the <CODE>PowerSupply</CODE> to which this <CODE>Magnet</CODE> 
   * belongs.
   * @attribute 
   */
    private PowerSupply powerSupply;

    /**
   * Creates a new <CODE>Magnet</CODE>.
   */
    public Magnet() {
        super();
    }

    /**
   * Creates a new <CODE>Magnet</CODE>.
   * 
   * @param id The ID of the <CODE>Magnet</CODE>.
   */
    public Magnet(String id) {
        super(id);
    }

    /**
   * Creates a new <CODE>Magnet</CODE>.
   * 
   * @param id The ID of the <CODE>Magnet</CODE>.
   * @param description The description of the <CODE>Magnet</CODE>.
   */
    public Magnet(String id, String description) {
        super(id, description);
    }

    /**
   * Gets the <CODE>PowerSupply</CODE> to which this <CODE>Magnet</CODE> 
   * belongs.
   * 
   * @return The <CODE>PowerSupply</CODE> to which this <CODE>Magnet</CODE> belongs.
   */
    public PowerSupply getPowerSupply() {
        return powerSupply;
    }

    /**
   * Sets the <CODE>PowerSupply</CODE> to which this <CODE>Magnet</CODE> 
   * belongs.
   * 
   * @param powerSupply The <CODE>PowerSupply</CODE> to which this <CODE>Magnet</CODE> belongs.
   */
    public void setPowerSupply(PowerSupply powerSupply) {
        this.powerSupply = powerSupply;
        if (powerSupply.getMagnet(getID()) == null) powerSupply.addMagnet(this);
    }
}
