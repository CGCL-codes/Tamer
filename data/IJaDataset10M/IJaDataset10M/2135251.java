package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import compilador.analisadorLexico.AnalisadorLexico;
import compilador.analisadorLexico.AnalisadorLexicoImpl;
import compilador.analisadorSemantico.AnalisadorSemantico;
import compilador.analisadorSemantico.AnalisadorSemanticoImpl;
import compilador.analisadorSintatico.AnalisadorSintatico;
import compilador.analisadorSintatico.AnalisadorSintaticoImpl;
import compilador.geradorCodigo.GeradorCodigo;
import compilador.geradorCodigo.GeradorCodigoImpl;
import compilador.tratamentoDeErro.GerenciadorMensagens;
import compilador.tratamentoDeErro.GerenciadorMensagensImpl;

/**
 * 
 * Segunda fase do compilador: an�lise l�xica e sint�tica
 * 
 * @author Alfeu Buriti
 * @author Andrea Alves
 * @author Guarany Viana
 * @author Rodrigo Barbosa Lira
 * @author Samuel de Barros
 */
public class Compilador {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("N�o foram informados arquivos");
            System.out.println("Informe o caminho de cada arquivo entre aspas.");
            System.out.println("==> Ex: \"caminho arquivo 1\" \"caminho arquivo 2\" etc.");
            System.exit(0);
        }
        AnalisadorLexico lexico = null;
        AnalisadorSintatico sintatico = null;
        AnalisadorSemantico semantico = null;
        GeradorCodigo gerador = null;
        GerenciadorMensagens mensageiro = null;
        for (int i = 0; i < args.length; i++) {
            try {
                mensageiro = new GerenciadorMensagensImpl();
                lexico = new AnalisadorLexicoImpl(args[i], mensageiro);
                mensageiro.setAnalisadorLexico(lexico);
                semantico = new AnalisadorSemanticoImpl(mensageiro);
                gerador = new GeradorCodigoImpl();
                sintatico = new AnalisadorSintaticoImpl(lexico, semantico, gerador, mensageiro);
                sintatico.analisar();
                System.out.println();
                System.out.println("Analisando arquivo " + args[i]);
                if (mensageiro.hasErros()) {
                    System.out.println("Programa fonte possuir erros:");
                    System.out.println(mensageiro.getErros());
                } else {
                    System.out.println("Programa analisado com sucesso.");
                }
                if (mensageiro.hasErros()) {
                    System.out.println("O c�digo intermedi�rio n�o foi gerado porque o programa fonte possui erros.");
                    System.out.println();
                } else {
                    System.out.println("C�digo Gerado");
                    System.out.println(gerador.getCodigo());
                }
                System.out.println("Fim da compila��o.");
            } catch (FileNotFoundException e) {
                System.out.println("==> Advert�ncia: arquivo \"" + args[i] + "\" n�o encontrado.");
            } catch (IOException e) {
                System.out.println("Erro I/O. Saindo do sistema");
                System.exit(1);
            }
        }
    }
}
