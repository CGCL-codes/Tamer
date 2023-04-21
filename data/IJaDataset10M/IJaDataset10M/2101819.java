package br.unisinos.cs.gp.stream;

import java.io.*;

/**
 * Leitor de Streams em Formato Little Endian
 * 
 * Trabalha invertendo os bytes da leitura conforme são solicitados
 * Baseado na classe com.elharo.io.LittleEndianInputStream
 * 
 * @author Wanderson Henrique Camargo Rosa
 */
public class LittleEndianInputStream extends FilterInputStream {

    /**
     * Construtor da Classe
     * @param in Stream de Entrada
     */
    public LittleEndianInputStream(InputStream in) {
        super(in);
    }

    /**
     * Leitura de um byte da entrada de dados
     * @return Valor inteiro do byte solicitado
     * @throws IOException Erro de IO
     */
    public int readByte() throws IOException {
        int buffer = in.read();
        return buffer;
    }

    /**
     * Leitura de dois bytes da entrada de dados
     * @return Valor inteiro dos bytes concatenados
     * @throws IOException Erro de IO
     */
    public int readWord() throws IOException {
        int buffer0 = in.read();
        int buffer1 = in.read();
        int result = buffer1 << 8 | buffer0;
        return result;
    }
}
