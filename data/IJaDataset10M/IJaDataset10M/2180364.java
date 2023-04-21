package br.usp.es;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Classe que cuida da entrada dos dados de um arquivo de entrada 
 * especificado através do método construtor.
 * @author Bruno Grisi
 */
public class Entrada {

    private String nomeArquivo;

    private File file;

    private FileInputStream fileInputStream;

    private BufferedInputStream bufferedInputStream;

    private BufferedReader bufferedReader;

    /**
     * Cria um novo arquivo de entrada.
     * @param _nomeArquivo caminho do arquivo de entrada
     */
    public Entrada(String _nomeArquivo) {
        nomeArquivo = _nomeArquivo;
        initParameters();
    }

    private void initParameters() {
        file = new File(nomeArquivo);
        try {
            fileInputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream));
        } catch (Exception e) {
        }
    }

    /**
     * Verifica se o arquivo já existe.
     * @return <B>true</B>: arquivo j� existe<BR>
     * <B>false</B>: arquivo n�o existe
     */
    public boolean exists() {
        return file.exists();
    }

    /**
     * Verifica se o arquivo pode ser lido.
     * @return <B>true</B>: pode ser lido<BR>
     * <B>false</B>: n�o pode ser lido
     */
    public boolean canRead() {
        return file.canRead();
    }

    /**
     * Verifica se o arquivo pode ser escrito.
     * @return <B>true</B>: pode ser escrito<BR>
     * <B>false</B>: não pode ser escrito
     */
    public boolean canWrite() {
        return file.canWrite();
    }

    /**
     * Deleta o arquivo.
     * @return <B>true</B>: arquivo deletado com sucesso<BR>
     * <B>false</B>: arquivo não deletado
     */
    public boolean delete() {
        return file.delete();
    }

    /**
     * Reinicia o processo de leitura do arquivo.
     */
    public void masterReset() {
        try {
            initParameters();
        } catch (Exception e) {
        }
    }

    /**
     * Verifica se existem mais caracteres a serem lidos no arquivo.
     * @return <B>true</B>: há mais caracteres<BR>
     * <B>false</B>: não há mais caracteres (fim de arquivo)
     */
    public boolean hasMoreChars() {
        try {
            return bufferedReader.ready();
        } catch (IOException ex) {
            return false;
        }
    }

    /**
     * Retorna o próximo caractere do arquivo de entrada.
     * @return retorna o próximo caractere do arquivo
     */
    public char getNextChar() {
        int character = ' ';
        try {
            if (bufferedReader.ready()) {
                character = bufferedReader.read();
            }
        } catch (Exception e) {
        }
        return (char) character;
    }

    /**
     * Marca a posição do caractere atual (último que foi lido)
     * no arquivo de entrada. Deve ser utilizado juntamente com o 
     * método resetToMark();
     */
    public void mark() {
        try {
            bufferedReader.mark(1);
        } catch (Exception e) {
        }
    }

    /**
     * Volta a leitura do arquivo para a posição marcada pelo método
     * mark().
     */
    public void resetToMark() {
        try {
            bufferedReader.reset();
        } catch (Exception e) {
        }
    }
}
