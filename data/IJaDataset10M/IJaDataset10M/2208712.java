package update5.scanner;

import java.util.Locale;
import java.util.Scanner;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;

/**
 * As vers�es anteriores de Java 1.5 era extremamente complicado, ou quase
 * imposs�vel criar programas que recebiam valores de vari�veis pelo terminal.
 * Agora no novo Java a solu��o at� que enfim apareceu.
 * 
 * Se voc� fosse criar um programa de Java para receber par�metros de vari�veis
 * pelo console, tinha obrigatoriamente que dominar Streams. Al�m de criar um
 * pacote de classes muito grande para atender todos os tipos de vari�veis. Ou
 * ent�o desistia da id�ia de receber dados pelo console e utilizava da
 * interface gr�fica de Java.
 * 
 * @author Fabr�cio Silva Epaminondas
 * 
 */
public class TestScanner {

    public static void test_Old_Java() {
        System.out.println("TestScanner.test_Old_Java()");
        String temp = JOptionPane.showInputDialog("Digite o n�mero:");
        int number = Integer.parseInt(temp);
        int sum = number * number;
        JOptionPane.showMessageDialog(null, "O quadrado de " + number + " �: " + sum);
    }

    public static void test_New_Java() {
        System.out.println("TestScanner.test_New_Java()");
        Scanner input = new Scanner(System.in);
        System.out.print("Digite o n�mero: ");
        int number = input.nextInt();
        System.out.println("O quadrado de " + number + " �: " + number * number);
    }

    public static void test_Tokenizer_Java() {
        System.out.println("TestScanner.test_Tokenizer_Java()");
        StringTokenizer st = new StringTokenizer("minha_idade: 23 salario: 100.99 estudante: true");
        System.out.print(st.nextToken());
        int idade = Integer.parseInt(st.nextToken());
        System.out.print(idade + " " + st.nextToken());
        float salario = Float.valueOf(st.nextToken());
        System.out.print(salario + " " + st.nextToken());
        boolean estudante = Boolean.valueOf(st.nextToken());
        System.out.println(estudante);
    }

    public static void test_Scanner_Localized_Java() {
        System.out.println("TestScanner.test_Scanner_Localized_Java()");
        System.out.println(Locale.getDefault());
        Scanner sc = new Scanner("minha_idade: 23 salario: 100,99 estudante: true");
        System.out.print(sc.next());
        int idade = sc.nextInt();
        System.out.print(idade + " " + sc.next());
        float salario = sc.nextFloat();
        System.out.print(salario + " " + sc.next());
        boolean estudante = sc.nextBoolean();
        System.out.println(estudante);
    }

    public static void main(String[] args) {
        test_Old_Java();
        test_New_Java();
        test_Tokenizer_Java();
        test_Scanner_Localized_Java();
    }
}
