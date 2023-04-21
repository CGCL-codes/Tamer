package rpt.bova.desktop;

import rpt.bd.cenario.CGlobal;

/**
 *
 * @author Roberto
 */
public class CPbdcTpmercAnoMesDiaCotacaoCodneg extends CPbdcTpmercAnoMesDiaCotacao {

    public CPbdcTpmercAnoMesDiaCotacaoCodneg(CGlobal aGlobal) {
        super(aGlobal);
    }

    @Override
    public void preparar() {
        super.preparar();
        fNivelDetalhamento = 1;
        fPersistenciaBd.setNomeTabela("tbcot", "c");
        fPersistenciaBd.setNomeChavePrimaria("codneg");
        fPersistenciaBd.setDistinto(true);
    }
}
