package org.app.banco;

import org.ombu.model.TwoPhaseCommitParticipant;
import org.ombu.participant.at.TwoPhaseCommitParticipantManager;
import org.ombu.participant.at.Vote;

class ManagerDebito extends TwoPhaseCommitParticipantManager {

    @Override
    public Vote onPrepare(TwoPhaseCommitParticipant tpcp) {
        Banco.getInstance().setEstadoParticipante("preparando...");
        esperar(1200);
        Banco.getInstance().setEstadoParticipante("inactivo");
        return Vote.PREPARED;
    }

    @Override
    public void onCommit(TwoPhaseCommitParticipant tpcp) {
        Banco.getInstance().setEstadoParticipante("completando...");
        esperar(600);
        DatosParticipante datos = Banco.getInstance().getDatosParticipante(tpcp.getId());
        Cuenta cuenta = Banco.getInstance().getCuenta(datos.getIdCuenta());
        cuenta.confirmarPredebito(datos.getMonto());
        Banco.getInstance().notifyObservers();
        esperar(600);
        Banco.getInstance().setEstadoParticipante("inactivo");
    }

    @Override
    public void onRollback(TwoPhaseCommitParticipant tpcp) {
        Banco.getInstance().setEstadoParticipante("deshaciendo...");
        esperar(600);
        DatosParticipante datos = Banco.getInstance().getDatosParticipante(tpcp.getId());
        Cuenta cuenta = Banco.getInstance().getCuenta(datos.getIdCuenta());
        cuenta.anularPredebito(datos.getMonto());
        Banco.getInstance().notifyObservers();
        esperar(600);
        Banco.getInstance().setEstadoParticipante("inactivo");
    }

    private void esperar(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ex) {
        }
    }
}
