package ciarlini.utils;

import java.io.File;
import java.io.FileOutputStream;
import javax.swing.JFileChooser;

public class IOUtils {

    private static File arquivoEscolhido;

    public static File escolheArquivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.removeChoosableFileFilter(fileChooser.getAcceptAllFileFilter());
        fileChooser.setMultiSelectionEnabled(false);
        int option = fileChooser.showSaveDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            arquivoEscolhido = fileChooser.getSelectedFile();
            return arquivoEscolhido;
        } else {
            return null;
        }
    }

    public static void salvaEmArquivo(String corpo) throws Exception {
        if (arquivoEscolhido == null) {
            salvaEmArquivo(corpo, escolheArquivo());
        } else {
            salvaEmArquivo(corpo, arquivoEscolhido);
        }
    }

    public static void salvaEmArquivo(String corpo, File arquivo) throws Exception {
        FileOutputStream fileOutputStream = null;
        fileOutputStream = new FileOutputStream(arquivo, true);
        fileOutputStream.write(corpo.replaceAll("\n", "\r\n").getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();
    }
}
