package compilador.analisadorLexico;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import compilador.tratamentoDeErro.GerenciadorMensagens;

/**
 * Impl. do Analisador L�xico para o subconjunto da linguagem VB
 * 
 * @author Rodrigo Barbosa Lira
 * @author Alfeu Buriti
 * @author Andrea Alves
 * @author Guarany Viana
 * @author Samuel de Barros
 * 
 */
public class AnalisadorLexicoImpl implements AnalisadorLexico {

    public static final int TAMANHO_MAXIMO_IDENTIFICADOR = 30;

    public static final int VALOR_MININO_INTEIRO = 0;

    public static final int VALOR_MAXIMO_INTEIRO = 30000;

    public static final int TAMANHO_MAXIMO_CONSTANTE_CADEIA = 30000;

    public static final char ASPA = '"';

    private BufferedInputStream stream;

    private int numCaracter;

    private char charAtual;

    private String palavraSimbolo;

    private Simbolo simbolo;

    private boolean obteveSimbolo;

    private boolean obteveCaracter = false;

    public int linha = 1;

    public int coluna = 0;

    private GerenciadorMensagens mensageiro;

    /**
	 * Construtor padr�o para a implementa��o do analisador l�xico
	 * 
	 * @param arquivo
	 * @param mensageiro
	 * @throws FileNotFoundException
	 */
    public AnalisadorLexicoImpl(String arquivo, GerenciadorMensagens mensageiro) throws FileNotFoundException {
        this.stream = new BufferedInputStream(new FileInputStream(arquivo));
        this.mensageiro = mensageiro;
    }

    @Override
    public int getColuna() {
        return this.coluna - (simbolo != null ? simbolo.getLexema().length() + 1 : 0);
    }

    @Override
    public int getLinha() {
        return this.linha;
    }

    /**
	 * Verifica se um dado caracter � uma letra
	 * 
	 * @param ch
	 * @return
	 */
    public static boolean isLetra(char ch) {
        return Character.isLetter(ch);
    }

    /**
	 * Verifica se um dado caracter � um d�gito
	 * 
	 * @param ch
	 * @return
	 */
    public static boolean isDigito(char ch) {
        return Character.isDigit(ch);
    }

    /**
	 * Verfica se um dado caracter � um espa�o em branco
	 * @param ch
	 * @return
	 */
    public static boolean isEspacoEmBranco(char ch) {
        return Character.isWhitespace(ch);
    }

    public char getProximoCaracter() throws IOException {
        numCaracter = stream.read();
        this.coluna++;
        if (isQuebraLinha()) {
            this.linha++;
            this.coluna = 1;
            stream.read();
            return Character.LINE_SEPARATOR;
        }
        if (isFinalArquivo()) {
            return Simbolo.LEXEMA_FINAL_ARQUIVO;
        }
        return (char) numCaracter;
    }

    private boolean isFinalArquivo() {
        return numCaracter == -1;
    }

    private boolean isQuebraLinha() {
        return (char) numCaracter == '\r';
    }

    public char getProximoCaracterNaoBranco() throws IOException {
        char ch = getProximoCaracter();
        while (isEspacoEmBranco(ch)) {
            ch = getProximoCaracter();
        }
        return ch;
    }

    public Simbolo getProximoSimbolo() throws IOException {
        obteveSimbolo = false;
        simbolo = null;
        while (!obteveSimbolo) {
            if (!obteveCaracter || isEspacoEmBranco(charAtual)) {
                charAtual = getProximoCaracterNaoBranco();
            }
            obteveCaracter = false;
            palavraSimbolo = "";
            if (isLetra(charAtual)) {
                reconhecimentoIdentificador();
            } else if (isDigito(charAtual)) {
                reconhecerNumero();
            } else {
                reconhecerOperador();
            }
        }
        return simbolo;
    }

    private void reconhecimentoIdentificador() throws IOException {
        while (isDigito(charAtual) || isLetra(charAtual)) {
            palavraSimbolo += charAtual;
            charAtual = getProximoCaracter();
        }
        obteveCaracter = true;
        if (palavraSimbolo.length() >= TAMANHO_MAXIMO_IDENTIFICADOR) {
            palavraSimbolo = new StringBuffer(palavraSimbolo).substring(0, TAMANHO_MAXIMO_IDENTIFICADOR - 1);
            mensageiro.addAdvertencia("Tamanho de identificador superior ao permitido. Atualizando identificador para " + palavraSimbolo);
        }
        if (Simbolo.isPalavraReservada(palavraSimbolo)) {
            simbolo = Simbolo.getInstanceOfSimboloPalavraReservada(palavraSimbolo);
        } else {
            simbolo = Simbolo.getInstanceOfIdentificador(palavraSimbolo);
        }
        obteveSimbolo = true;
    }

    private void reconhecerNumero() throws IOException {
        while (isDigito(charAtual)) {
            palavraSimbolo += charAtual;
            charAtual = getProximoCaracter();
        }
        obteveCaracter = true;
        int valor = new Integer(palavraSimbolo).intValue();
        if (valor < VALOR_MININO_INTEIRO) {
            mensageiro.addAdvertencia("Valor de numero inferior ao permitido. Atualizando para valor " + String.valueOf(VALOR_MININO_INTEIRO));
            valor = VALOR_MININO_INTEIRO;
        }
        if (valor > VALOR_MAXIMO_INTEIRO) {
            mensageiro.addAdvertencia("Valor de numero superior ao permitido. Atualizando valor para " + String.valueOf(VALOR_MAXIMO_INTEIRO));
            valor = VALOR_MAXIMO_INTEIRO;
        }
        simbolo = Simbolo.getInstanceOfNumero(String.valueOf(valor));
        obteveSimbolo = true;
    }

    private void reconhecerOperador() throws IOException {
        obteveSimbolo = true;
        switch(charAtual) {
            case ',':
                simbolo = Simbolo.getInstanceOfOperadorVirgula();
                break;
            case '+':
                simbolo = Simbolo.getInstanceOfOperadorAdicao();
                break;
            case '-':
                simbolo = Simbolo.getInstanceOfOperadorSubtracao();
                break;
            case '*':
                simbolo = Simbolo.getInstanceOfOperadorMultiplicacao();
                break;
            case '/':
                simbolo = Simbolo.getInstanceOfOperadorDivisao();
                break;
            case '=':
                charAtual = getProximoCaracter();
                switch(charAtual) {
                    case '=':
                        simbolo = Simbolo.getInstanceOfOperadorIgualdade();
                        break;
                    default:
                        simbolo = Simbolo.getInstanceOfOperadorAtribuicao();
                        break;
                }
                break;
            case '>':
                charAtual = getProximoCaracter();
                switch(charAtual) {
                    case '=':
                        simbolo = Simbolo.getInstanceOfOperadorMaiorIgual();
                        break;
                    default:
                        simbolo = Simbolo.getInstanceOfOperadorMaiorQue();
                        break;
                }
                break;
            case '<':
                charAtual = getProximoCaracter();
                switch(charAtual) {
                    case '%':
                        simbolo = Simbolo.getInstanceOfInicioPrograma();
                        break;
                    case '=':
                        simbolo = Simbolo.getInstanceOfOperadorMenorIgual();
                        break;
                    case '>':
                        simbolo = Simbolo.getInstanceOfOperadorDiferenca();
                        break;
                    default:
                        simbolo = Simbolo.getInstanceOfOperadorMenorQue();
                        break;
                }
                break;
            case '%':
                charAtual = getProximoCaracter();
                switch(charAtual) {
                    case '>':
                        simbolo = Simbolo.getInstanceOfTerminoPrograma();
                        break;
                    default:
                        if (isEspacoEmBranco(charAtual)) {
                            mensageiro.addErro("Caracter % inesperado.");
                        } else {
                            mensageiro.addErro("Caracteres % e " + charAtual + " inesperados.");
                        }
                }
                break;
            case '(':
                simbolo = Simbolo.getInstanceOfParentisacaoAbreParentese();
                break;
            case ')':
                simbolo = Simbolo.getInstanceOfParentisacaoFechaParentese();
                break;
            case '[':
                simbolo = Simbolo.getInstanceOfParentisacaoAbreColchete();
                break;
            case ']':
                simbolo = Simbolo.getInstanceOfParentisacaoFechaColchete();
                break;
            case ASPA:
                charAtual = getProximoCaracter();
                while (charAtual != ASPA) {
                    if (!isQuebraLinha() && !isFinalArquivo()) {
                        palavraSimbolo += charAtual;
                        charAtual = getProximoCaracter();
                    } else {
                        mensageiro.addErro("Cadeia de caracteres mal formada.");
                        break;
                    }
                }
                simbolo = Simbolo.getInstanceOfCadeiaCaracteres(palavraSimbolo);
                break;
            case '#':
                simbolo = Simbolo.getInstanceOfFinalArquivo();
                break;
            default:
                obteveSimbolo = false;
                mensageiro.addErro("Caracter " + charAtual + " inesperado.");
                charAtual = getProximoCaracter();
                break;
        }
    }

    public boolean hasProximoSimbolo() {
        return charAtual != Simbolo.LEXEMA_FINAL_ARQUIVO;
    }
}
