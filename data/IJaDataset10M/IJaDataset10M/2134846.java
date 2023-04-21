package org.freedom.library.component;

import java.io.FileInputStream;

public class DadosImagem {

    private FileInputStream fisImagem;

    private int iTamanho = 0;

    private byte[] byImagem = null;

    public DadosImagem(FileInputStream fisIm, int iTam, byte[] byI) {
        byImagem = byI;
        fisImagem = fisIm;
        iTamanho = iTam;
    }

    public FileInputStream getInputStream() {
        return fisImagem;
    }

    public void setInputStream(FileInputStream fisIm) {
        fisImagem = fisIm;
    }

    public int getTamanho() {
        return iTamanho;
    }

    public void setTamanho(int iTam) {
        iTamanho = iTam;
    }

    public byte[] getBytes() {
        return byImagem;
    }

    public void setBytes(byte[] byI) {
        byImagem = byI;
    }
}
