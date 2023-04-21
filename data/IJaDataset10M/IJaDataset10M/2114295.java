package com.ufro.blackjack.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BlackJackServiceAsync {

    void generateCard(AsyncCallback<Integer> callback);

    void validarBlackJack(Integer iMano, AsyncCallback<Integer> callback);

    void validarManoCasino(Integer iManoCasino, AsyncCallback<Integer> callback);

    void addUsuario(String sNombre, String sPassword, String sDineroInicial, AsyncCallback<Integer> callback);

    void calcularScore(Integer iUserId, AsyncCallback<Integer> callback);

    void editGameLost(Integer iUserId, AsyncCallback<Integer> callback);

    void editGameWon(Integer iUserId, AsyncCallback<Integer> callback);

    void findUserId(String sNombre, String sPassword, AsyncCallback<Integer> callback);

    void logUser(String sNombre, String sPassword, AsyncCallback<Integer> callback);

    void updateDineroFinal(Integer iUserId, String sDineroFinal, AsyncCallback<Integer> callback);

    void updateUsuarioName(String sNombre, String sNewName, String sPassword, AsyncCallback<Integer> callback);

    void updateUsuarioPass(String sNombre, String sPassword, String sNewPass, AsyncCallback<Integer> callback);

    void verDineroActual(Integer iUserId, AsyncCallback<Integer> callback);

    void verificarMontoApuesta(Integer iUserId, String sApuestaActual, AsyncCallback<Integer> callback);

    void verificarPrimerJuego(Integer iUserId, AsyncCallback<Integer> callback);
}
