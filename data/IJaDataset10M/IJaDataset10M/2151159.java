package jsimulator.src.br.ufrj.dcc.arquitetura.controller;

import java.io.File;
import jsimulator.src.br.ufrj.dcc.arquitetura.view.Janela;

/***
 * Controller para se comunicar com a Janela
 */
public abstract class JanelaController {

    /***
	 * Inst�ncia da Janela
	 */
    private static Janela view;

    /***
	 * Chamada pelo Main, inicializa a View e os outros componentes
	 */
    public static void inicia() {
        JanelaController.view = new Janela();
    }

    /***
	 * Retorna o c�digo digitado na View
	 * @return String com o c�digo
	 */
    public static String getCodigoFonte() {
        String texto = null;
        texto = view.getCodigoFonte().trim();
        return texto;
    }

    /***
	 * Escreve no console debug
	 * @param texto texto para colocar no console
	 */
    public static void escreveConsole(String texto) {
        view.escreveConsole(texto);
    }

    /***
	 * Passa o conte�do e o �ltimo arquivo lido para a View
	 * @param buffer String com o conte�do do arquivo
	 * @param dir Objeto com o a configura��o do �ltimo arquivo lido
	 */
    public static void abrirArquivo(String buffer, File dir) {
        view.abrirArquivo(buffer, dir);
    }

    /***
	 * Atualiza os valores desses registradores na View
	 * @param R0 Registrador 0
	 * @param R1 Registrador 1
	 * @param R2 Registrador 2
	 * @param R3 Registrador 3
	 * @param R4 Registrador 4
	 * @param RX Registrador de Trabalho
	 * @param PC Registrador Program Counter
	 */
    public static void setRegistrador(char R0, char R1, char R2, char R3, char R4, char RX, char PC) {
        view.setRegistradores(R0, R1, R2, R3, R4, RX, PC);
    }

    /***
	 * Atualiza os valores desses registradores na View
	 * @param rEnd Registrador de Endere�o
	 * @param ir Registrador de Instru��o
	 * @param rDados Registrador de dados lidos da mem�ria
	 */
    public static void setRegistrador(char rEnd, char ir, char rDados) {
        view.setRegistradores(rEnd, ir, rDados);
    }

    /***
	 * Atualiza os pontos de teste da ULA na View
	 * @param zero
	 * @param signal
	 * @param neg
	 * @param carry
	 */
    public static void setPontosControle(boolean zero, boolean signal, boolean carry) {
        view.setPontos(zero, signal, carry);
    }

    /***
	 * Avisa a View que acabaram as instru��es carregadas na mem�ria
	 */
    public static void halt() {
        view.halt();
    }

    /***
	 * Apaga o console
	 */
    public static void resetaConsole() {
        view.resetaConsole();
    }
}
