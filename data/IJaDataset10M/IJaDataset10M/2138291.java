package jahuwaldt.tools.units;

/**
*  <p>  The SI speed unit: kilometers/hour.
*       Definition: (km/hr)/(m/s) = 3.6  </p>
*
*  <p>  Is Exact?:  Yes  </p>
*
*  <p>  Modified by:  Joseph A. Huwaldt  </p>
*
*  @author   Joseph A. Huwaldt   Date:  October 13, 1998
*  @version  November 19, 1999
**/
public final class KilometersPerHour extends SpeedUnit {

    private static KilometersPerHour example = new KilometersPerHour();

    private KilometersPerHour() {
        name = "kilometers per hour";
        abv = "km/hr";
        exact = true;
    }

    static final double theFactor = 3.6;

    protected double factor() {
        return theFactor;
    }

    /**
	*  Return a reference to the one and only instance of this class.
	**/
    public static KilometersPerHour getInstance() {
        return example;
    }
}
