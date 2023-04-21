package carrancao.gui;

/**
 *
 * @author Lubnnia
 */
public class DataMarcone {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        String data = "25-12-1986";
        String dia = "", mes = "", ano = "";
        if (data != null && data.length() > 0) {
            for (int i = 0; i < data.length(); i++) {
                if (i >= 0 && i < 2) {
                    dia = dia + "" + data.charAt(i);
                }
                if (i > 2 && i < 5) {
                    mes = mes + "" + data.charAt(i);
                }
                if (i > 5) {
                    ano = ano + "" + data.charAt(i);
                }
            }
        }
        System.out.println("DIA: " + dia);
        System.out.println("Mes: " + mes);
        System.out.println("ano: " + ano);
    }
}
