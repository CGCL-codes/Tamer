package org.ecf4j.scanner.metrologic;

import org.ecf4j.utils.comm.exceptions.CommException;

/**
 * Classe Metrologic padrão
 * @author Pablo Fassina e Agner Munhoz
 * @version 1.0.0
 * @extends MetrologicAbstract
 * @see MetrologicAbstract
 */
public class MetrologicPadrao extends MetrologicAbstract {

    @Override
    public void finalizar() throws CommException {
        comm.fechar();
    }

    @Override
    public byte getDelimitador() {
        return 13;
    }

    @Override
    public void inicializar(String porta, Integer velocidade, Integer bitsDados, Integer paridade, Integer bitsParada) throws CommException {
        comm.abrirPorta(porta, 9600, 8, 0, 1);
    }

    @Override
    public byte[] lerBytes() throws CommException {
        return comm.readDireto();
    }

    @Override
    public String lerScanner() throws CommException {
        String s = comm.read();
        if (s != null) {
            return s.trim();
        } else {
            return "";
        }
    }
}
