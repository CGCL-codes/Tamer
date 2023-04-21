package com.gft.larozanam.shared.exceptions;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class RegistroNaoEncontradoException extends ErroGenericoRuntimeException implements IsSerializable {

    public RegistroNaoEncontradoException() {
        super("Registro não encontrado");
    }
}
