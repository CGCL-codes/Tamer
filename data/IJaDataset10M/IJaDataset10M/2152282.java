package br.ita.autowidget.metadata;

import java.security.InvalidParameterException;

/**
 * <p>MetadataRepositoryProperty � uma implementa��o do padr�o Metadata Container, que nada mais �
 * uma estrutura para armazenar informa��es pertinentes para a gera��o da interface com
 * o usu�rio e evitar que seja realizado o procedimento de reflection sobre o objeto
 * sempre que for necess�rio obter informa��es sobre ele.</p>
 * 
 * <p>Essa classe � respons�vel por armazenar metadados sobre as propriedades de uma classe
 * para persist�ncia no reposit�rio</p>
 * @author Nando
 *
 */
@SuppressWarnings("rawtypes")
public class MetadataRepositoryProperty extends MetadataContainer {

    private Class type;

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        if (type == null) throw new InvalidParameterException();
        this.type = type;
    }
}
