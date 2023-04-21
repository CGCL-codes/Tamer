package org.fudaa.dodico.crue.metier.helper;

import org.fudaa.dodico.crue.config.CrueConfigMetier;
import org.fudaa.dodico.crue.metier.emh.CatEMHCasier;
import org.fudaa.dodico.crue.metier.emh.DonCalcSansPrtCasierProfil;

/**
 * @author deniger
 */
public class FactoryInfoEMH {

    public static DonCalcSansPrtCasierProfil getDCSPCasierProfil(final CatEMHCasier casier, final CrueConfigMetier cruePropertyDefinitionContainer) {
        DonCalcSansPrtCasierProfil dcsp = EMHHelper.selectFirstOfClass(casier.getInfosEMH(), DonCalcSansPrtCasierProfil.class);
        if (dcsp == null) {
            dcsp = new DonCalcSansPrtCasierProfil(cruePropertyDefinitionContainer);
            casier.addInfosEMH(dcsp);
        }
        return dcsp;
    }
}
