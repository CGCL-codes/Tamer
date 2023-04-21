package com.gestioni.adoc.aps.system.services.documento.inoltro;

import com.gestioni.adoc.aps.system.services.inoltro.IInoltroManager;

public class InoltroDocumentoHelper {

    /**
	 * Quando un profilo inoltra un documento che gli è stato inoltrato,
	 * l'inoltro preesistente deve passare a STATUS_PRESO_IN_CARICO (o rimanere inalterato se è maggiore).
	 *
	 * Se l'inoltro preesistente è di tipo uo deve essere effettuata la rimozione degli inoltri per gli altri utenti
	 * se lo status dell'inoltro è inferiore STATUS_PRESO_IN_CARICO
	 *
	 * Prende in ingresso un inoltro in uscita e restituisce un bean che, nel caso esista, contiene
	 * l'dentificativo dell'unoltro alla uo e il valore dello status.
	 *
	 * Tenta di recuperare l'inoltro in base al doc e al sender.
	 * @param inoltroInUscita
	 * @param inoltroDocumentoManager
	 * @throws Throwable
	 */
    public InoltroDocumentoHelper(InoltroDocumento inoltroInUscita, InoltroDocumentoManager inoltroDocumentoManager) throws Throwable {
        InoltroDocumento inoltro = inoltroDocumentoManager.getLastInoltro(inoltroInUscita.getDocumentId(), inoltroInUscita.getSender());
        if (null != inoltro) {
            if (inoltro.getRecipientStatus() < IInoltroManager.STATUS_PRESO_IN_CARICO) {
                this.setIdInoltroUo(inoltro.getIdInoltroUo());
                this.setInoltroRecipientStatus(IInoltroManager.STATUS_PRESO_IN_CARICO);
            }
        }
    }

    public void setIdInoltroUo(Integer idInoltroUo) {
        this._idInoltroUo = idInoltroUo;
    }

    public Integer getIdInoltroUo() {
        return _idInoltroUo;
    }

    public void setInoltroRecipientStatus(Integer inoltroRecipientStatus) {
        this._inoltroRecipientStatus = inoltroRecipientStatus;
    }

    public Integer getInoltroRecipientStatus() {
        return _inoltroRecipientStatus;
    }

    private int _idInoltroUo;

    private int _inoltroRecipientStatus;
}
