package alocador.util;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Classe que engloba todos os metodos de entrada do programa
 * 
 */
public class MetodoEntrada {

    private static Scanner entrada;

    /**
	 * Metodo de entrada de valores <code>int</code>.<br>
	 * <b>Importante:</b> O metodo tem comportamento recursivo caso o valor
	 * informado seja invalido
	 * 
	 * @return Valor inteiro digitado pelo usuario
	 */
    public static int recebeInt() {
        entrada = new Scanner(System.in);
        if (!entrada.hasNextInt()) {
            entrada.next();
            promptError();
            return recebeInt();
        }
        return entrada.nextInt();
    }

    /**
	 * Metodo de entrada de valores <code>boolean</code>.<br>
	 * <b>Importante:</b> O metodo tem comportamento recursivo caso o valor
	 * informado seja invalido
	 * 
	 * @return Valor booleano inserido pelo usuario
	 */
    public static boolean recebeBoolean() {
        entrada = new Scanner(System.in);
        if (!entrada.hasNextBoolean()) {
            entrada.next();
            promptError();
            return recebeBoolean();
        }
        return entrada.nextBoolean();
    }

    /**
	 * Metodo de entrada de <code>String</code>.<br>
	 * <b>Importante:</b> O metodo tem comportamento recursivo caso o valor
	 * informado seja invalido
	 * 
	 * @return A string inserida pelo usuario
	 * 
	 */
    public static String recebeString() {
        entrada = new Scanner(System.in);
        String palavra = null;
        try {
            palavra = entrada.nextLine();
        } catch (NoSuchElementException e) {
            entrada.next();
            promptError();
            return recebeString();
        }
        return palavra;
    }

    /**
	 * Metodo de entrada de valores <code>double</code>.<br>
	 * <b>Importante:</b> O metodo tem comportamento recursivo caso o valor
	 * informado seja invalido
	 * 
	 * @return Valor inserido pelo usuario<br>
	 */
    public static double recebeDouble() {
        entrada = new Scanner(System.in);
        if (!entrada.hasNextDouble()) {
            entrada.next();
            promptError();
            return recebeDouble();
        }
        return entrada.nextDouble();
    }

    /**
	 * Prompt de erro mostrado nos metodos de entrada de dados
	 */
    private static void promptError() {
        System.out.println(">>> ENTRADA INVALIDA!");
        System.out.print(">>> Digite novamente: ");
    }

    /**
	 * Recebe data e hora seguindo o seguinte formato:
	 * <code>DD/MM/AAAA hh:mm</code><br>
	 * O metodo tem um comportamento recursivo, caso a cadeia informada pelo <br>
	 * usuario nao seja valida.<br>
	 * <br>
	 * 
	 * <b>Importante:</b> Esse metodo nao garante restricao para entradas do
	 * tipo "<code>99/99/9999 99:99</code>"
	 * 
	 * @return Data e Hora informada
	 */
    public static String recebeDataHora() {
        String opcao = recebeString();
        if (Pattern.matches("[0-9]{2}/[0-9]{2}/[0-9]{4} [0-9]{2}:[0-9]{2}", opcao)) {
            return opcao;
        } else {
            promptError();
            return recebeDataHora();
        }
    }

    /**
	 * Recebe um valor inteiro e retorna ao cliente a finalidade de uma
	 * determinada sala de acordo com o mapeamento: <blockquote>
	 * <code>1: Sala de Aula<br>
	 * 2: Sala de Conferencia<br>
	 * 3: Laboratorio<br>
	 * 4: Escritorio</blockquote></code>
	 * 
	 * @return String representando a finalidade da sala
	 */
    public static String recebeFinalidade() {
        int op = recebeInt();
        if (op == 1) return "sala de aula"; else if (op == 2) return "sala de conferencia"; else if (op == 3) return "laboratorio"; else if (op == 4) return "escritorio"; else {
            promptError();
            return recebeFinalidade();
        }
    }

    /**
	 * Recebe um valor inteiro e retorna ao cliente o TIPO de um determinado
	 * LABORATORIO de acordo com o mapeamento: <blockquote> <code>1: Quimica<br>
	 * 2: Fisica<br>
	 * 3: Biologia<br>
	 * 4: Computacao</blockquote></code>
	 * 
	 * @return String representando o tipo de laboratorio
	 */
    public static String tiposLaboratorio() {
        int op = recebeInt();
        if (op == 1) return "quimica"; else if (op == 2) return "fisica"; else if (op == 3) return "biologia"; else if (op == 4) return "computacao"; else {
            promptError();
            return tiposLaboratorio();
        }
    }

    /**
	 * Recebe um valor inteiro e retorna ao cliente o TIPO de um determinada
	 * sala de CONFERENCIA de acordo com o mapeamento: <blockquote>
	 * <code>1: Normal<br>
	 *  2: Videoconferencia</blockquote></code>
	 * 
	 * @return String representando o tipo de sala de conferencia
	 */
    public static String tiposSalaConferencia() {
        int op = recebeInt();
        if (op == 1) return "normal"; else if (op == 2) return "videoconferencia"; else {
            promptError();
            return tiposSalaConferencia();
        }
    }

    /**
	 * Recebe um valor inteiro e retorna ao cliente o TIPO de um determinada
	 * SALA de acordo com o mapeamento: <blockquote> <code>1: Normal<br>
	 * 2: Videoconferencia<br>
	 * 3: Inteligente</blockquote></code>
	 * 
	 * @return String representando o tipo de sala de aula
	 */
    public static String tiposSalaAula() {
        int op = recebeInt();
        if (op == 1) return "normal"; else if (op == 2) return "videoconferencia"; else if (op == 3) return "inteligente"; else {
            promptError();
            return tiposSalaAula();
        }
    }
}
