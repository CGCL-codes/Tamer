package br.eti.fgsl.java.samples;

import java.io.IOException;
import java.util.IllegalFormatCodePointException;
import br.eti.fgsl.io.Reader;
import br.eti.fgsl.io.Writer;

public class ExemploDeLeituraEEscrita {

    public static void main(String[] args) {
        String entrada = new String();
        Integer unicode;
        while (true) {
            Writer.set("Para sair, digite 'sair'");
            Writer.set("Digite o número do caráter unicode:'");
            try {
                entrada = Reader.get();
                if (entrada.equals("sair")) break;
                unicode = new Integer(entrada);
                Writer.set("O código unicode " + unicode + " representa o caráter " + String.format("%c", unicode));
            } catch (IOException e) {
                Writer.set("Houve um erro de leitura. A mensagem do Java é " + e.getMessage());
            } catch (NumberFormatException e) {
                Writer.set("Houve um erro de formatação. A mensagem do Java é " + e.getMessage());
            } catch (IllegalFormatCodePointException e) {
                Writer.set("Você digitou um código que não existe");
            }
        }
    }
}
