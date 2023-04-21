package totalpos;

import com.sun.jna.Structure;

/**
 *
 * @author Saul Hidalgo.
 */
public class TQueryPrnTransaction extends Structure {

    public byte[] RptDate;

    public byte[] RptTime;

    public byte[] RptAMPM;

    public int VoucherVta;

    public int VoucherDev;

    public byte[] VtaA;

    public byte[] VtaB;

    public byte[] VtaC;

    public byte[] VtaE;

    public byte[] IvaVtaA;

    public byte[] IvaVtaB;

    public byte[] IvaVtaC;

    public byte[] DscVtaA;

    public byte[] DscVtaB;

    public byte[] DscVtaC;

    public byte[] DscVtaE;

    public byte[] RcgVtaA;

    public byte[] RcgVtaB;

    public byte[] RcgVtaC;

    public byte[] RcgVtaE;

    public byte[] DevA;

    public byte[] DevB;

    public byte[] DevC;

    public byte[] DevE;

    public byte[] IvaDevA;

    public byte[] IvaDevB;

    public byte[] IvaDevC;

    public byte[] DscDevA;

    public byte[] DscDevB;

    public byte[] DscDevC;

    public byte[] DscDevE;

    public byte[] RcgDevA;

    public byte[] RcgDevB;

    public byte[] RcgDevC;

    public byte[] RcgDevE;

    public byte[] FPago1;

    public byte[] FPago2;

    public byte[] FPago3;

    public byte[] FPago4;

    public byte[] FPago5;

    public byte[] FPago6;

    public TQueryPrnTransaction() {
        RptDate = new byte[10];
        RptTime = new byte[10];
        RptAMPM = new byte[4];
        VtaA = new byte[14];
        VtaB = new byte[14];
        VtaC = new byte[14];
        VtaE = new byte[14];
        IvaVtaA = new byte[14];
        IvaVtaB = new byte[14];
        IvaVtaC = new byte[14];
        DscVtaA = new byte[14];
        DscVtaB = new byte[14];
        DscVtaC = new byte[14];
        DscVtaE = new byte[14];
        RcgVtaA = new byte[14];
        RcgVtaB = new byte[14];
        RcgVtaC = new byte[14];
        RcgVtaE = new byte[14];
        DevA = new byte[14];
        DevB = new byte[14];
        DevC = new byte[14];
        DevE = new byte[14];
        IvaDevA = new byte[14];
        IvaDevB = new byte[14];
        IvaDevC = new byte[14];
        DscDevA = new byte[14];
        DscDevB = new byte[14];
        DscDevC = new byte[14];
        DscDevE = new byte[14];
        RcgDevA = new byte[14];
        RcgDevB = new byte[14];
        RcgDevC = new byte[14];
        RcgDevE = new byte[14];
        FPago1 = new byte[14];
        FPago2 = new byte[14];
        FPago3 = new byte[14];
        FPago4 = new byte[14];
        FPago5 = new byte[14];
        FPago6 = new byte[14];
    }
}
