package basys.eib;

import basys.eib.exceptions.EIBAddressFormatException;
import java.util.StringTokenizer;

/**
 * EIBgrpaddr.java
 *
 * Realization of the EIBAddress class for group addresses
 *
 *
 * @author  oalt
 * @version $Id: EIBGrpaddress.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 */
public class EIBGrpaddress extends EIBAddress {

    /**
   * Default constructor
   * Sets all bits in the address to 0
   */
    public String EIBGRoup;

    public EIBGrpaddress() {
        this.addr_h = 0;
        this.addr_l = 0;
    }

    /**
   * Constructor
   * Sets EIB group address with a string
   *
   * @param s string with an EIB group address
   */
    public EIBGrpaddress(String s) throws EIBAddressFormatException {
        setAddress(s);
    }

    /**
   * Constructor
   * Set ERIB group address with main and subgroup
   *
   * @param maingroup main group number
   * @param subgrp subgroup number
   */
    public EIBGrpaddress(int maingrp, int subgrp) {
        setAddress(maingrp, subgrp);
    }

    public EIBGrpaddress(int groupAddressCount) {
        int addr = 0x100 + groupAddressCount;
        this.addr_h = addr >> 8;
        this.addr_l = addr & 0xFF;
    }

    /**
   * Set address with a string
   *
   * @param s string with the group address (e.g. 1/1/1)
   */
    public void setAddress(String s) throws EIBAddressFormatException {
        StringTokenizer token = new StringTokenizer(s, "/");
        if (token.countTokens() > 2) {
            setAddress2(s);
        } else {
            try {
                int idx = s.indexOf('/');
                Integer mgrp = new Integer(s.substring(0, idx));
                this.addr_h = 0;
                this.addr_h = (mgrp.intValue() << 3);
                mgrp = new Integer(s.substring(idx + 1, s.length()));
                this.addr_h |= (mgrp.intValue() >> 8);
                this.addr_l = mgrp.intValue() & 0xFF;
            } catch (Exception e) {
                throw new EIBAddressFormatException("Wrong EIB group address format: " + s);
            }
        }
    }

    /**
   * Set address with a string
   *
   * @param s string with the group address (e.g. 1/1/1)
   */
    public void setAddress2(String s) throws EIBAddressFormatException {
        StringTokenizer token = new StringTokenizer(s, "/");
        String m1, m2, m3;
        m1 = token.nextToken();
        m2 = token.nextToken();
        m3 = token.nextToken();
        Integer n1, n2, n3;
        n1 = new Integer(m1);
        n2 = new Integer(m2);
        n3 = new Integer(m3);
        try {
            this.setAddress("" + n1 + "/" + ((n2 << 8) | n3));
        } catch (Exception e) {
            e.printStackTrace();
            throw new EIBAddressFormatException("Wrong EIB group address format: " + s);
        }
    }

    /**
   * Set address with main and subgroup
   *
   * @param maingrp main group number
   * @param subgrp subgroup number
   */
    public void setAddress(int maingrp, int subgrp) {
        this.addr_h = 0;
        this.addr_h = (maingrp << 3) | (subgrp >> 8);
        this.addr_l = (subgrp & 0xFF);
    }

    /**
   * Get a string representation of the address
   *
   * @return the address as string
   */
    public String toString() {
        String s = "";
        s += (this.addr_h >> 3) + "/" + (((this.addr_h & 0x07) << 8) + this.addr_l);
        System.out.println(addr_h);
        System.out.println(addr_l);
        return s;
    }

    public String toString2() {
        String s = "";
        s += (this.addr_h >> 3) + "/" + (((this.addr_h & 0x07) << 8) + this.addr_l);
        int aux = (((this.addr_h & 0x07) << 8) + this.addr_l);
        int zone = (aux & 0xFF);
        int line = (aux >> 8);
        return "" + (addr_h >> 3) + "/" + line + "/" + zone + "";
    }
}
