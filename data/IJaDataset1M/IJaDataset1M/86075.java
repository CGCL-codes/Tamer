package servidor.catalog.tipo;

import servidor.tabla.Campo;

/**
 * Convierte de un tipo de datos a un arreglo de caracteres.
 */
public final class CharArrayConversor extends Conversor {

    /**
     * @see servidor.catalog.tipo.Conversor#convertir(servidor.tabla.Campo, java.lang.Object)
     */
    @Override
    public Object convertir(Campo campo, Object valor) {
        switch(campo.tipo()) {
            case INTEGER:
                {
                    Integer integer = (Integer) valor;
                    char[] cadena = integer.toString().toCharArray();
                    return cadena;
                }
            case CHAR:
                {
                    char[] cadena = (char[]) valor;
                    return cadena;
                }
            default:
                throw new RuntimeException("Imposible de realizar la conversion");
        }
    }
}
