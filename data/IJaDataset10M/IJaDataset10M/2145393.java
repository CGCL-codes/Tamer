package uk.co.nimp.scard;

import com.atolsystems.atolutilities.ACommandLineUtilities;

/**
 *
 * @author sebastien.riou
 */
public interface Initializable {

    int initInstance(ACommandLineUtilities argContext, int nArgs);
}
