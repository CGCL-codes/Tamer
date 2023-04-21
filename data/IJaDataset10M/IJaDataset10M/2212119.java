package br.com.klis.batendoumabola.shared;

/**
 * <p>
 * FieldVerifier validates that the name the user enters is valid.
 * </p>
 * <p>
 * This class is in the <code>shared</code> packing because we use it in both
 * the client code and on the server. On the client, we verify that the name is
 * valid before sending an RPC request so the user doesn't have to wait for a
 * network round trip to get feedback. On the server, we verify that the name is
 * correct to ensure that the input is correct regardless of where the RPC
 * originates.
 * </p>
 * <p>
 * When creating a class that is used on both the client and the server, be sure
 * that all code is translatable and does not use native JavaScript. Code that
 * is note translatable (such as code that interacts with a database or the file
 * system) cannot be compiled into client side JavaScript. Code that uses native
 * JavaScript (such as Widgets) cannot be run on the server.
 * </p>
 */
public class BateBolaVerifier {

    public static String isValid(Pelada pelada) {
        String retorno = "";
        if (pelada == null) {
            retorno = "Comportamento inesperado. Valor nulo para o Bate-Bola.";
            return retorno;
        }
        if (pelada.getNome() == null || pelada.getNome().equals("")) {
            retorno = "O nome deve ser preenchido.";
            return retorno;
        } else {
            if (pelada.getNome().length() > 50) {
                retorno = "O nome deve ter no máximo 50 caracteres.";
                return retorno;
            }
        }
        if (pelada.getDescricao() == null || pelada.getDescricao().equals("")) {
            retorno = "A descrição deve ser preenchida.";
            return retorno;
        } else {
            System.out.println("TAMANHO DESCRICAO: " + pelada.getDescricao().length());
            if (pelada.getDescricao().length() > 500) {
                retorno = "A descrição deve conter no máximo 300 caracteres.";
                return retorno;
            }
        }
        if (pelada.getData() == null) {
            retorno = "Por favor, preencha o campo data.";
            return retorno;
        }
        if (pelada.getLocal() == null || pelada.getLocal().equals("")) {
            retorno = "O local deve ser preenchido.";
            return retorno;
        } else {
            if (pelada.getLocal().length() > 50) {
                retorno = "O local deve conter no máximo 50 caracteres.";
                return retorno;
            }
        }
        if (pelada.getLogradouro() == null || pelada.getLogradouro().equals("")) {
            retorno = "O logradouro deve ser preenchido.";
            return retorno;
        } else {
            if (pelada.getLogradouro().length() > 100) {
                retorno = "O logradouro deve conter no máximo 100 caracteres.";
                return retorno;
            }
        }
        if (pelada.getNumero() == null || pelada.getNumero().equals("")) {
            retorno = "Por favor, preencha o número.";
            return retorno;
        } else {
            if (pelada.getNumero().length() > 6) {
                retorno = "O número deve conter no máximo 6 caracteres.";
                return retorno;
            }
        }
        if (pelada.getBairro() == null || pelada.getBairro().equals("")) {
            retorno = "Por favor, preencha o bairro.";
            return retorno;
        } else {
            if (pelada.getBairro().length() > 50) {
                retorno = "O bairro deve conter no máximo 50 caracteres.";
                return retorno;
            }
        }
        if (pelada.getCidade() == null || pelada.getCidade().equals("")) {
            retorno = "Por favor, preencha a cidade.";
            return retorno;
        } else {
            if (pelada.getCidade().length() > 50) {
                retorno = "A cidade deve conter no máximo 50 caracteres.";
                return retorno;
            }
        }
        if (pelada.getEstado() == null || pelada.getEstado().equals("")) {
            retorno = "Por favor, preencha o estado.";
            return retorno;
        } else {
            if (pelada.getEstado().length() != 2) {
                retorno = "O estado deve conter 2 caracteres.";
                return retorno;
            }
        }
        if (pelada.getTemVagas() == null) {
            pelada.setTemVagas(false);
        }
        if (pelada.getGratuito() == null) {
            pelada.setGratuito(false);
        }
        return retorno;
    }
}
