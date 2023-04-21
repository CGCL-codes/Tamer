package core.util;

import org.apache.commons.codec.net.QuotedPrintableCodec;

/**
 * @author Glauber Magalh�es Pires
 *
 */
public final class ConverteNomeArquivo {

    private static final String[] extensoes = { ".p2m" };

    public static final String retiraSufixoP2M(String nomeArquivo) {
        nomeArquivo = converteEncoding(nomeArquivo);
        if (nomeArquivo.endsWith(".doc")) {
            String nomeArquivoSemExtensao = nomeArquivo.substring(0, nomeArquivo.length() - 4);
            for (int i = 0; i < extensoes.length; i++) {
                if (nomeArquivoSemExtensao.endsWith(extensoes[i])) return nomeArquivo.substring(0, nomeArquivo.length() - 4); else {
                    String extensao = nomeArquivoSemExtensao.substring(nomeArquivoSemExtensao.length() - 3);
                    if (Character.isDigit(extensao.charAt(0)) && Character.isDigit(extensao.charAt(1)) && Character.isDigit(extensao.charAt(2))) return nomeArquivoSemExtensao.substring(0, nomeArquivo.length() - 4); else if (extensao.charAt(0) == 'c' && Character.isDigit(extensao.charAt(1)) && Character.isDigit(extensao.charAt(2))) return nomeArquivoSemExtensao.substring(0, nomeArquivo.length() - 4);
                }
            }
        }
        return nomeArquivo;
    }

    public static final String converteEncoding(String texto) {
        if (texto.indexOf("=?") != -1) {
            String encoding = texto.substring(texto.indexOf("=?") + 2);
            encoding = encoding.substring(0, encoding.indexOf("?"));
            texto = texto.replaceAll("\\=\\?" + encoding + "\\?Q\\?", "");
            texto = texto.replaceAll("\\?\\=", "");
            try {
                QuotedPrintableCodec decoder = new QuotedPrintableCodec(encoding);
                texto = decoder.decode(texto);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return texto;
    }

    public static final String cleanSubject(String subject) {
        subject = converteEncoding(subject);
        boolean foiAlterado;
        do {
            foiAlterado = false;
            if (subject.toLowerCase().startsWith("fwd:")) {
                subject = subject.substring(4).trim();
                foiAlterado = true;
            }
            if (subject.toLowerCase().startsWith("enc:")) {
                subject = subject.substring(4).trim();
                foiAlterado = true;
            }
            if (subject.toLowerCase().startsWith("en:")) {
                subject = subject.substring(3).trim();
                foiAlterado = true;
            }
        } while (foiAlterado);
        return subject;
    }
}
