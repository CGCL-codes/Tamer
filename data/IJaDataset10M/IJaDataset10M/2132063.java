package br.com.qotsa.mobilepcremotecontrolprotocol.listener;

/**
 *
 * @author francisco
 */
public interface ErrorListener {

    /**
     * Trata um evento de Erro.
     * @param msg Mensagem de Erro.
     */
    public void onError(String msg);
}
