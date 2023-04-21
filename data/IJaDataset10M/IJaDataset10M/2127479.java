package update5.varargs;

import java.text.MessageFormat;
import java.util.Date;

/**
 * 
 * Varargs: argumentos vari�veis (Method declarations JLS 8.4.1)
 * � Permite a passagem de um n�mero previamente indeterminado de argumentos nos m�todos
 * � Antes era preciso ter uma quantidade fixa, e passar um array
 * � Hoje, o sistema aceita o array ou seus argumentos �soltos�
 * � Regras
 * � Tem que ser o �ltimo argumento declarado no m�todo
 * � Argumentos t�m que ser de mesmo tipo ou tipo convers�vel
 * (equivalente a um array)
 * � Exemplo de sintaxe:
 * � declara��o
 * 		void metodo (char op, String arg2, double... args) {}
 * � uso
 * 		obj.metodo(�s�, �Soma�, 13, 3L, 4.5f, 1,2,3,4,5);
 * 
 * � Observa��es:
 * � N�o abuse
 * � Em APIs use apenas quando houver um benef�cio real
 * � Pode dificultar e tornar mais complexo o reuso (sobrecarga e sobreposi��o)
*/
public class TestVarargs {

    public static String format1(String pattern, Object[] args) {
        return MessageFormat.format(pattern, args);
    }

    public static String format2(String pattern, Object... args) {
        return MessageFormat.format(pattern, args);
    }

    public static void main(String[] args) {
        Object[] arg = { new Date(), 7 };
        System.out.println(format1("Hora {0,time} Dia {0,date}, valor {1}", arg));
        System.out.println(format2("Hora {0,time} Dia {0,date}, valor {1}", new Date(), 8));
    }
}
