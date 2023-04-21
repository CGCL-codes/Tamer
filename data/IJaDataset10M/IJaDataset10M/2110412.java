package br.com.danielnegri.nfe.negocio.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class MascaraDados {

    private static final char[] FIRST_CHAR = (" !'#$%&'()*+\\-./0123456789:;<->?@ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~ E ,f'.++^%S<O Z  ''''.-" + "-~Ts>o ZY !C#$Y|$'(a<--(_o+23'u .,1o>113?AAAAAAACEEEEIIIIDNOO" + "OOOXOUUUUyTsaaaaaaaceeeeiiiidnooooo/ouuuuyty").toCharArray();

    private static final char[] SECOND_CHAR = ("  '         ,                                               " + "\\                                   $  r'. + o  E      ''  " + "  M  e     #  =  'C.<  R .-..     ..>424     E E            " + "   E E     hs    e e         h     e e     h ").toCharArray();

    public MascaraDados() {
    }

    public static String unMaskAll(String all) {
        String retorno = "";
        try {
            if (all.length() > 0) retorno = all.replace(".", "").replace("-", "").replace(",", "").replace("/", "").replace(" ", "").trim(); else retorno = all;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }

    public static String maskCPF(String cpf) {
        String retorno = "";
        try {
            if (cpf == null) cpf = "";
            if (cpf.length() > 10) retorno = cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9, cpf.length()); else retorno = cpf;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }

    public String unMaskCPF(String cpf) {
        String retorno = "";
        try {
            if (cpf.length() > 12) retorno = cpf.replace(".", "").replace("-", "").replace(",", "").replace("/", "").replace(" ", "").trim(); else retorno = cpf;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }

    public static String maskCNPJ(String cnpj) {
        String retorno = "";
        try {
            if (cnpj == null) cnpj = "";
            if (cnpj.length() > 12) retorno = cnpj.substring(0, 2) + "." + cnpj.substring(2, 5) + "." + cnpj.substring(5, 8) + "/" + cnpj.substring(8, 12) + "-" + cnpj.substring(12, cnpj.length()); else retorno = cnpj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }

    public String unMaskCNPJ(String cnpj) {
        String retorno = "";
        try {
            if (cnpj.length() > 16) retorno = cnpj.replace(".", "").replace("-", "").replace(",", "").replace("/", "").replace(" ", "").trim(); else retorno = cnpj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }

    public String maskFone(String fone) {
        String retorno = "";
        try {
            if (fone.length() > 6) retorno = "(" + fone.substring(0, 2) + ")" + fone.substring(2, 6) + "-" + fone.substring(6, fone.length()); else retorno = fone;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }

    public String unMaskFone(String fone) {
        String retorno = "";
        try {
            if (fone.length() > 10) retorno = fone.replace("(", "").replace(")", "").replace("-", "").trim(); else retorno = fone;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }

    public String maskData(String data) {
        String retorno = "";
        try {
            if (data == null) data = "";
            if (data.length() > 5) {
                retorno = data.substring(0, 2) + "/" + data.substring(2, 4) + "/" + data.substring(4, data.length());
            } else {
                retorno = data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }

    public String maskDataSimples(String data) {
        String retorno = "";
        try {
            if (data == null) data = "";
            if (data.length() > 7) {
                retorno = data.substring(0, 2) + "/" + data.substring(2, 4) + "/" + data.substring(6, data.length());
            } else if (data.length() == 6) {
                retorno = data.substring(0, 2) + "/" + data.substring(2, 4) + "/" + data.substring(4, data.length());
            } else {
                retorno = data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }

    public String unMaskDataSimples(String data) {
        String retorno = "";
        try {
            if (data == null) data = "";
            data = unMaskAll(data);
            if (data.length() > 7) {
                retorno = data.substring(0, 2) + data.substring(2, 4) + data.substring(6, data.length());
            } else {
                retorno = data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }

    public String maskDataCompleta(String data) {
        String retorno = "";
        String dia = "";
        String mes = "";
        String ano = "";
        Integer iano = 0;
        try {
            if (data == null) data = "";
            if (data.length() > 5) {
                dia = data.substring(0, 2);
                mes = data.substring(2, 4);
                ano = data.substring(4, data.length());
                iano = Integer.parseInt(ano);
                if (iano < 100) iano = (iano < 50) ? (iano + 2000) : (iano + 1900);
                ano = String.valueOf(iano);
                retorno = dia + "/" + mes + "/" + ano;
            } else {
                retorno = data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }

    public String unMaskData(String data) {
        String retorno = "";
        try {
            if (data.length() > 5) retorno = data.replaceAll("/", "").trim(); else retorno = data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }

    public String maskValor(String valor) {
        String retorno = "";
        DecimalFormat formatter = new DecimalFormat();
        DecimalFormatSymbols df = new DecimalFormatSymbols();
        df.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(df);
        formatter.applyPattern("0.00");
        retorno = formatter.format(valor);
        return retorno;
    }

    public String maskValorDecimal(String valor) {
        String retorno = "";
        if (valor.contains(",")) {
            retorno = valor.replace(".", "").replace(" ", "").trim();
            retorno = retorno.replace(",", ".").replace(" ", "").trim();
        } else {
            retorno = valor;
        }
        return retorno;
    }

    /**
	 * Efetua as seguintes normaliza��es:
	 * - Elimina acentos e cedilhas dos nomes;
	 * - Converte aspas duplas em simples;
	 * - Converte algumas letras estrangeiras para seus equivalentes ASCII
	 * (como � = eszet, convertido para ss)
	 * - P�e um "\" antes de v�rgulas, permitindo nomes como
	 * "Verisign, Corp." e de "\", permitindo nomes como " a \ b ";
	 * - Converte os sinais de = para -
	 * - Alguns caracteres s�o removidos:
	 * -> os superiores a 255,
	 * mesmo que possam ser representados por letras latinas normais
	 * (como S, = U+015E = Latin Capital Letter S With Cedilla);
	 * -> os caracteres de controle (exceto tab, que � trocado por um espa�o)
	 * @param str A string a normalizar.
	 * @return A string normalizada.
	 */
    public static String normalize(String str) {
        String retorno = "";
        if ((str != null) && (!str.equals(""))) {
            str = str.replace("\"", "'");
            char[] chars = str.toCharArray();
            StringBuffer ret = new StringBuffer(chars.length * 2);
            for (int i = 0; i < chars.length; ++i) {
                char aChar = chars[i];
                if (aChar == ' ' || aChar == '\t') {
                    ret.append(' ');
                } else if (aChar > ' ' && aChar < 'Ā') {
                    if (FIRST_CHAR[aChar - ' '] != ' ') {
                        ret.append(FIRST_CHAR[aChar - ' ']);
                    }
                    if (SECOND_CHAR[aChar - ' '] != ' ') {
                        ret.append(SECOND_CHAR[aChar - ' ']);
                    }
                }
            }
            retorno = (ret.toString()).toUpperCase();
            retorno = retorno.replace("'", "�").replace("\\", "").trim();
        }
        return retorno;
    }
}
