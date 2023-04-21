package org.hardtokenmgmt.ws.objects;

/**
 * Class containing a web service representation
 * of a PIN data such as type, PIN and PUK
 * 
 * 
 * @author Philip Vendil
 *
 */
public class PINDataWS {

    private int type = 0;

    private String initialPIN = null;

    private String pUK = null;

    public PINDataWS() {
    }

    /**
	 * Default constructor
	 * 
	 * @param type pnt of the PINTYPE_ constants
	 * @param initialPIN the initial pin of the token
	 * @param puk the puk of the token
	 */
    public PINDataWS(int type, String initialPIN, String puk) {
        super();
        this.type = type;
        this.initialPIN = initialPIN;
        pUK = puk;
    }

    /**
	 * @return the initial pin of the token
	 */
    public String getInitialPIN() {
        return initialPIN;
    }

    /**
	 * @param initialPIN the initial pin of the token
	 */
    public void setInitialPIN(String initialPIN) {
        this.initialPIN = initialPIN;
    }

    /**
	 * 
	 * @return  the puk of the token
	 */
    public String getPUK() {
        return pUK;
    }

    /**
	 * 
	 * @param puk the puk of the token
	 */
    public void setPUK(String puk) {
        pUK = puk;
    }

    /**
	 * 
	 * @return the type of PIN one of the PINTTYPE_ constants
	 */
    public int getType() {
        return type;
    }

    /**
	 * 
	 * @param type type of PIN one of the PINTTYPE_ constants
	 */
    public void setType(int type) {
        this.type = type;
    }
}
