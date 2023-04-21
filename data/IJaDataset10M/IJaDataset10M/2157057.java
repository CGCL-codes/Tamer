package org.jcryptool.visual.PairingBDII.ui;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Vector;
import org.jcryptool.visual.PairingBDII.algorithm.ECBDII;
import org.jcryptool.visual.PairingBDII.basics.DHECKeyPair;
import org.jcryptool.visual.PairingBDII.basics.PolynomialMod;
import de.flexiprovider.common.math.FlexiBigInt;
import de.flexiprovider.common.math.finitefields.GFPElement;

public class ECBDIIUserData {

    private PrivateKey SK;

    private PublicKey PK;

    private FlexiBigInt nonce;

    private DHECKeyPair DHKeyPair;

    private PolynomialMod Xvalue, key;

    private int i;

    public ECBDIIUserData(ECBDII protocol, Vector<PrivateKey> SKV, Vector<PublicKey> PKV, Vector<FlexiBigInt> nonceV, int myi) {
        int nmax = protocol.GetNUsers();
        i = myi;
        if (i > nmax) {
            System.out.println("There are only " + nmax + "users!");
        } else {
            SK = SKV.get(i - 1);
            PK = PKV.get(i - 1);
            nonce = nonceV.get(i - 1);
            if (i < 4) {
                Xvalue = new PolynomialMod(true, protocol.Getp());
            }
        }
    }

    public void setDHECKeyPair(DHECKeyPair mykeypair) {
        DHKeyPair = mykeypair;
    }

    public void setX(PolynomialMod Xpers) {
        Xvalue = Xpers;
    }

    public void setKey(PolynomialMod mykey) {
        key = mykey;
    }

    public PrivateKey GetSK() {
        return SK;
    }

    public PublicKey GetPK() {
        return PK;
    }

    public FlexiBigInt GetNonce() {
        return nonce;
    }

    public DHECKeyPair GetStep1KeyPair() {
        return DHKeyPair;
    }

    public PolynomialMod GetX() {
        return Xvalue;
    }

    public PolynomialMod GetKey() {
        return key;
    }

    public String UserData() {
        String s = "";
        s = "This is the user data for user " + i + ": " + "\n" + "The user's secret authentication key S is: " + SK.toString() + "\n";
        s = s + "The user's public authentication key P is: " + PK.toString() + "\n" + "The user's nonce is: " + nonce.toString() + "\n";
        s = s + "The user's secret key s is: " + DHKeyPair.getSK().toString() + "\n" + "The user's public key Z = sP is: " + "\n";
        s = s + DHKeyPair.getPK().GetPrintP() + "\n" + "The user's second public key X is: " + "\n";
        s = s + Xvalue.PrintP() + "\n" + "The user's conference key K is: " + "\n" + key.PrintP() + "\n";
        return s;
    }

    public void PrintUserData() {
        System.out.println("This is the user data for user " + i);
        System.out.println("The user's secret authentication key S is: " + SK.toString());
        System.out.println("The user's public authentication key P is: " + PK.toString());
        System.out.println("The user's nonce is: " + nonce.toString());
        System.out.println("The user's secret key s is: " + DHKeyPair.getSK().toString());
        System.out.println("The user's public key Z = sP is: ");
        DHKeyPair.getPK().PrintPoint();
        System.out.println("The user's second public key X is: ");
        Xvalue.PrintPol();
        System.out.println("The user's conference key K is: ");
        key.PrintPol();
    }
}
