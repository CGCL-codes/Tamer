package it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.strong.params;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import java.io.Serializable;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAStrongKeyParameters extends AsymmetricKeyParameter implements Serializable {

    private UTMAStrongPublicParameters publicParameters;

    public UTMAStrongKeyParameters(boolean isPrivate, UTMAStrongPublicParameters publicParameters) {
        super(isPrivate);
        this.publicParameters = publicParameters;
    }

    public UTMAStrongPublicParameters getParameters() {
        return publicParameters;
    }
}
