package engine.util;

import com.sun.lwuit.Dialog;

/**
 *
 * @author Bruno Naponiello
 *
 */
public class Util {

    public static final int NULL = 0, DATA = 1, NUMERICO = 2, VAZIO = 3;

    public static String validacao(int v) {
        if (v == NULL) {
            return "NULL";
        } else if (v == DATA) {
            return "DATA";
        } else if (v == NUMERICO) {
            return "NUMERICO";
        } else if (v == VAZIO) {
            return "VAZIO";
        }
        return "NULL";
    }

    public static void msg(String mensagem) {
        Dialog.show("Msg:", mensagem, "Ok", null);
    }

    public static void msgErro(String msg, Throwable e) {
        if (e != null) {
            Dialog.show("Erro:", msg + e.getMessage(), "Ok", null);
        }
    }
}
