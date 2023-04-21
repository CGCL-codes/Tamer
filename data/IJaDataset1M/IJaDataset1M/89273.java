package lzss;

import static java.lang.Math.pow;
import org.apache.commons.lang.StringUtils;

public class DescompressorLZSS {

    private static final int TAMANHO_CABECALHO_INICIO = 2;

    private static final int TAMANHO_CABECALHO = 1;

    private static final int TAMANHO_JANELA_DESLIZANTE = (int) pow(2, TAMANHO_CABECALHO + 2);

    private static final Boolean HEADER_LITTLE_ENDIAN = Boolean.TRUE;

    public static int[] descomprimeDados(int[] dados) {
        int tamanhoDataDecomp = 0;
        for (int i = 0; i < TAMANHO_CABECALHO_INICIO; i++) {
            int index = HEADER_LITTLE_ENDIAN ? i : (TAMANHO_CABECALHO_INICIO - 1 - i);
            tamanhoDataDecomp += (dados[i] * (int) pow(16, 2 * index));
        }
        int indexDataDecomp = 0;
        int[] dataDecomp = new int[tamanhoDataDecomp];
        boolean lendoCabecalho = true;
        int indexDados = 0;
        int indexCabecalho = 0;
        int indexDecomp = 0;
        int valorCabecalho = 0;
        int tamanhoDados = 0;
        String cabecalho = null;
        for (int i = TAMANHO_CABECALHO_INICIO; i < dados.length; i++) {
            if (lendoCabecalho) {
                valorCabecalho = (dados[i] * (int) pow(16, 2 * (indexCabecalho)));
                indexCabecalho++;
                if (indexCabecalho >= TAMANHO_CABECALHO) {
                    cabecalho = StringUtils.leftPad(Integer.toBinaryString(valorCabecalho), TAMANHO_JANELA_DESLIZANTE, "0");
                    lendoCabecalho = false;
                    indexDados = 0;
                    for (int j = 0; j < cabecalho.length(); j++) {
                        tamanhoDados += cabecalho.charAt(j) == '0' ? 2 : 1;
                    }
                }
            } else {
                if (cabecalho.charAt(indexDecomp) == '0') {
                    int retorno = dados[i];
                    int avancar = dados[++i];
                    int indexRetorno = indexDataDecomp - retorno;
                    for (int j = 0; j < avancar; j++) {
                        dataDecomp[indexDataDecomp++] = dataDecomp[indexRetorno + j];
                    }
                } else {
                    dataDecomp[indexDataDecomp++] = dados[i];
                }
                indexDecomp++;
                indexDados++;
                if (indexDados >= tamanhoDados) {
                    lendoCabecalho = true;
                    valorCabecalho = 0;
                    indexCabecalho = 0;
                    tamanhoDados = 0;
                    indexDecomp = 0;
                    cabecalho = null;
                }
            }
        }
        return dataDecomp;
    }
}
