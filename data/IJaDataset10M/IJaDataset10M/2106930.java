package org.uasd.jalgor.model;

/**
 *
 * @author Edwin Bratini <edwin.bratini@gmail.com>
 */
public class ComentarioToken extends Token {

    public ComentarioToken() {
        super(Token.TipoToken.COMENTARIO);
    }

    public ComentarioToken(TipoToken tipoToken, String value) {
        super(tipoToken, value);
    }

    @Override
    public String toString() {
        return getValue();
    }
}
