package com.bjSoft.regressionTestTool.util;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileReader;
import java.io.IOException;

public class ArquivoEntrada {

    private BufferedReader in;

    private String[] buffer;

    private int nextChar;

    private int nextTokenLin, nextTokenCol;

    private int primLin, contLin;

    /**
    * Construtor da classe Arquivo. Abre o arquivo de entrada no modo leitura,
    * e o arquivo de sa�da no modo grava��o. Se o arquivo de sa�da j� existir,
    * seu conte�do � descartado.
    *
    * @param in     nome do arquivo de entrada de dados
    * @param out    nome do arquivo de sa�da de dados
    *
    */
    public ArquivoEntrada(String in) {
        try {
            this.in = new BufferedReader(new FileReader(in));
            this.initBuffer();
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
    }

    /** Fecha o arquivo quando o garbage collector � chamado */
    protected void finalize() {
        this.close();
    }

    /** Fecha o arquivo, ap�s o seu uso. */
    public void close() {
        try {
            if (this.in != null) {
                this.in.close();
                this.in = null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
    }

    /** Indica se foi encontrado o fim do arquivo. */
    public boolean isEndOfFile() {
        return (this.nextTokenLin < 0);
    }

    /** Indica se foi encontrado o fim da linha. */
    public boolean isEndOfLine() {
        return (this.nextTokenLin != this.primLin);
    }

    /**
    * L� uma linha do arquivo. Se parte da linha j� foi lida, ent�o o restante
    * � retornado, mesmo que seja uma linha em branco (String de tamanho zero).
    *
    * @return   a pr�xima linha lida do arquivo, ou <code>null</code> se o fim
    *           do arquivo for encontrado
    *
    */
    public String readLine() {
        if (this.contLin <= 0) return null;
        String line = this.buffer[this.primLin];
        if (this.nextChar > 0) if (this.nextChar >= line.length()) line = ""; else line = line.substring(this.nextChar, line.length() - 1);
        this.buffer[this.primLin] = null;
        this.nextChar = 0;
        this.primLin++;
        this.contLin--;
        if (this.nextTokenLin >= 0 && this.nextTokenLin < this.primLin) this.findNext();
        return line;
    }

    /**
    * L� o pr�ximo caractere do arquivo, incluindo espa�os (' ') e quebras de
    * linha ('\n'). Se o fim do arquivo for alcan�ado, o caractere nulo ('\0')
    * � retornado.
    *
    * @return   o caractere lido
    */
    public char readChar() {
        if (this.contLin <= 0) return '\0';
        char newChar;
        String line = this.buffer[this.primLin];
        if (this.nextChar >= line.length()) {
            newChar = '\n';
            this.readLine();
        } else {
            newChar = line.charAt(this.nextChar++);
            if (newChar != ' ' && this.nextTokenLin >= 0) this.findNext();
        }
        return newChar;
    }

    /**
    * L� uma string do arquivo.
    *
    * @return   a string lida
    *
    */
    public String readString() {
        String next = null;
        try {
            this.checkEOF();
            String line = this.buffer[this.nextTokenLin];
            for (int i = this.primLin; i < this.contLin; i++) this.buffer[i] = null;
            this.buffer[0] = line;
            this.nextTokenLin = this.primLin = 0;
            this.contLin = 1;
            int i, size = line.length();
            for (i = this.nextTokenCol; i < size; i++) if (line.charAt(i) == ' ') break;
            next = line.substring(this.nextTokenCol, i);
            this.nextChar = i;
            this.findNext();
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
        return next;
    }

    /**
    * L� um inteiro do arquivo.
    *
    * @return   o n�mero lido
    *
    */
    public int readInt() {
        return Integer.valueOf(this.readString()).intValue();
    }

    /**
    * L� um double do arquivo.
    *
    * @return   o n�mero lido
    *
    */
    public double readDouble() {
        return Double.valueOf(this.readString()).doubleValue();
    }

    /** Prepara o buffer de entrada para ser usado */
    private void initBuffer() throws IOException {
        this.buffer = new String[5];
        this.nextChar = 0;
        this.nextTokenLin = 0;
        this.primLin = this.contLin = 0;
        String line = this.in.readLine();
        if (line == null) {
            this.nextTokenLin = -1;
        } else {
            this.buffer[0] = line;
            this.contLin++;
            this.findNext();
        }
    }

    /** Verifica se o fim do arquivo foi encontrado */
    private void checkEOF() throws EOFException {
        if (this.isEndOfFile()) throw new EOFException();
    }

    /** Acrescenta uma linha lida do arquivo no buffer */
    private int appendLine(String str) {
        if (this.contLin == 0) this.primLin = 0;
        if (this.primLin + this.contLin >= this.buffer.length) {
            String[] src = this.buffer;
            if (this.contLin >= this.buffer.length) this.buffer = new String[2 * this.buffer.length];
            System.arraycopy(src, this.primLin, this.buffer, 0, this.contLin);
            this.nextTokenLin -= this.primLin;
            this.primLin = 0;
        }
        buffer[this.primLin + this.contLin] = str;
        this.contLin++;
        return (this.primLin + this.contLin - 1);
    }

    /** Encontra a posi��o do pr�ximo token a ser lido */
    private void findNext() {
        try {
            String line = this.buffer[this.primLin];
            if (line != null) {
                int size = line.length();
                for (int i = this.nextChar; i < size; i++) if (line.charAt(i) != ' ') {
                    this.nextTokenCol = i;
                    return;
                }
            }
            this.nextTokenLin = this.nextTokenCol = -1;
            while ((line = this.in.readLine()) != null) {
                int size = line.length();
                for (int i = 0; i < size; i++) if (line.charAt(i) != ' ') {
                    this.nextTokenCol = i;
                    this.nextTokenLin = this.appendLine(line);
                    return;
                }
                this.appendLine(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
    }
}
