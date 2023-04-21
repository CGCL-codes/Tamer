package net.sf.webphotos.util.legacy;

import net.sf.webphotos.util.Util;
import java.io.File;
import java.util.List;

/**
 * Objeto que cont�m os dados do arquivo usados na tabela FtpClient
 * @author guilherme
 */
public class Arquivo {

    private String linhaComando = "";

    private int acao = -1;

    private String nomeAcao = "";

    private int albumID = -1;

    private int fotoID = -1;

    private String nmArquivo = "";

    private long tamanho = 0;

    private String status = "";

    private File arqFoto;

    /**
     * Cria uma nova inst�ncia para o Arquivo
     */
    public Arquivo() {
    }

    /**
     * Verifica qual opera��o dever� ser feita com o arquivo e caso seja Upload ele enviar� o arquivo para o local especificado
     * @param linha Linha de comando
     * @param operacao Tipo de a��o a ser seguida
     * @param album Identifica��o do album
     * @param foto Identifica��o da foto
     * @param nomeArquivo Nome do arqvuio
     */
    public Arquivo(String linha, int operacao, int album, int foto, String nomeArquivo) {
        linhaComando = linha;
        acao = operacao;
        albumID = album;
        fotoID = foto;
        nmArquivo = nomeArquivo;
        status = "??";
        if (operacao == CacheFTP.DELETE) {
            nomeAcao = "apagar";
        }
        if (operacao == CacheFTP.DOWNLOAD) {
            nomeAcao = "receber";
        }
        if (operacao == CacheFTP.UPLOAD) {
            nomeAcao = "enviar";
            arqFoto = new File(Util.getAlbunsRoot(), albumID + File.separator + nmArquivo);
            if (arqFoto.isFile() && arqFoto.canRead()) {
                tamanho = arqFoto.length();
            } else {
                tamanho = 0;
            }
        } else {
            tamanho = 0;
        }
    }

    /**
     * Verifica qual opera��o dever� ser acionada para o arquivo
     * @param linha Linha de comando
     * @param operacao Tipo de a��o a ser seguida
     * @param album Identifica��o do album
     * @param foto Identifica��o da foto
     * @param nomeArquivo Nome do arqvuio
     * @param tam Tamanho do arquivo do arqvuio
     */
    public Arquivo(String linha, int operacao, int album, int foto, String nomeArquivo, long tam) {
        linhaComando = linha;
        acao = operacao;
        albumID = album;
        fotoID = foto;
        nmArquivo = nomeArquivo;
        status = "??";
        switch(operacao) {
            case CacheFTP.DELETE:
                nomeAcao = "apagar";
                break;
            case CacheFTP.DOWNLOAD:
                nomeAcao = "receber";
                break;
            case CacheFTP.UPLOAD:
                nomeAcao = "enviar";
                break;
        }
        tamanho = tam;
    }

    /**
     * <pre>
     * Constructor thar loads the data from a {@link java.util.List List}
     * List Data Format
     * Position 0: Status
     * Position 1: nomeAcao
     * Position 2: albumID
     * Position 3: fotoID
     * Position 4: nmArquivo
     * Position 5: tamanho
     * </pre>
     * 
     * TODO: review
     * @param _data photo data
     */
    @SuppressWarnings("unchecked")
    public Arquivo(final List<String> data) {
        status = data.get(0).toString();
        nomeAcao = data.get(1).toString();
        albumID = Integer.parseInt(data.get(2).toString());
        fotoID = Integer.parseInt(data.get(3).toString());
        nmArquivo = data.get(4).toString();
        tamanho = Integer.parseInt(data.get(5).toString());
    }

    /**
     * Retorna a vari�vel status
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Retorna vari�vel linhaComando
     * @return Linha de comando
     */
    public String getLinhaComando() {
        return linhaComando;
    }

    /**
     * Passa a vari�vel valor para a vari�vel status
     * @param valor valor usado
     */
    public void setStatus(String valor) {
        status = valor;
    }

    /**
     * Retorna a vari�vel acao(indica qual opera��o dever� ser acionada)
     * @return retorna acao
     */
    public int getAcao() {
        return acao;
    }

    /**
     * Retorna vari�vel  acao(indica qual opera��o dever� ser acionada)
     * @return retorna nome da a��o
     */
    public String getNomeAcao() {
        return nomeAcao;
    }

    /**
     * Retorna vari�vel albumID(identifica��o do album)
     * @return retorna id ao album
     */
    public int getAlbumID() {
        return albumID;
    }

    /**
     * Retorna vari�vel fotoID
     * @return retorna id da foto
     */
    public int getFotoID() {
        return fotoID;
    }

    /**
     * Retorna vari�vel nmArquivo(Nome do arquivo)
     * @return retorna nome do arquivo
     */
    public String getNomeArquivo() {
        return nmArquivo;
    }

    /**
     * Retorna vari�vel tamanho(tamanho do arquivo no qual ser� utilizado)
     * @return retorna tamanho do arquivo
     */
    public long getTamanho() {
        return tamanho;
    }

    /**
     * Passa a vari�vel valor para a vari�vel tamanho
     * @param valor valor a ser usado
     */
    public void setTamanho(long valor) {
        tamanho = valor;
    }

    /**
     * Concatena e retorna as vari�veis acao,albumID,fotoID e tamanho
     * @return Retorna as vari�veis acao,albumID,fotoID e tamanho
     */
    @Override
    public String toString() {
        return status + " " + acao + " " + albumID + " " + fotoID + " " + tamanho;
    }
}
