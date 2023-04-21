package com.proyecto.tropero.core.application;

import org.jasypt.util.text.BasicTextEncryptor;
import com.proyecto.tropero.core.domain.arquitectura.TroperoConstants;

public class DesEncriptarPassword {

    private String passEncriptada;

    private String passDesencriptada;

    private BasicTextEncryptor textEncryptor = new BasicTextEncryptor();

    public DesEncriptarPassword(String passEncriptada) {
        this.passEncriptada = passEncriptada;
        desEncriptarPass();
    }

    private void desEncriptarPass() {
        textEncryptor.setPassword(TroperoConstants.CLAVE_DESENCRIPTACION);
        this.passDesencriptada = textEncryptor.decrypt(passEncriptada);
    }

    public String getPasswordDesencriptada() {
        return this.passDesencriptada;
    }
}
