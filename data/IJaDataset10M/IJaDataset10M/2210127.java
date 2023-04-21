package br.eteg.curso.java.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DataUtil {

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd hh:mm:ss";

    public static final String DD_MM_YYYY_HH_MM_SS = "dd/MM/yyyy hh:mm:ss";

    @Deprecated
    public static String getDataAtualFormatada() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR) + " - " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
    }

    /**
	 * retorna a data formatada para extrato (dia e m�s)
	 * @param data a data a ser formatada
	 * @return a data formatada
	 */
    @Deprecated
    public static String getDataFormatadaParaExtrato(Calendar data) {
        String formato = "dd/MM";
        SimpleDateFormat sdf = new SimpleDateFormat(formato);
        return sdf.format(data.getTime());
    }

    /**
	 * retorna a data formatada para extrato (dia e m�s)
	 * @param data a data a ser formatada
	 * @return a data formatada
	 */
    public static String getDataFormatada(Calendar data) {
        return getDataFormatada(data, DD_MM_YYYY_HH_MM_SS);
    }

    public static Calendar obterDataDeString(String s) throws ParseException {
        return obterDataDeString(s, YYYY_MM_DD_HH_MM_SS);
    }

    /**
	 * m�todo que obtem uma data a partir de um formato.
	 * @param s o String que contem a data
	 * @param formato o formato a ser aplicado
	 * @return a data
	 * @throws ParseException
	 */
    public static Calendar obterDataDeString(String s, String formato) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(formato);
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(s));
        return c;
    }

    @Deprecated
    public static String getDataFormatadaParaArquivo(Calendar c) {
        return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH) + "-" + c.get(Calendar.HOUR_OF_DAY) + "-" + c.get(Calendar.MINUTE) + "-" + c.get(Calendar.SECOND);
    }

    /**
	 * retorna a data completa por extenso.
	 * @param c a data
	 * @return o String formatando
	 */
    public static String getDataCompletaFormatada(Calendar c) {
        SimpleDateFormat sdf = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.FULL);
        return sdf.format(c.getTime());
    }

    /**
	 * m�todo que formata uma data para String utilizando o formato.
	 * @param c a data
	 * @param formato o formato desejado (Ex: dd/MM/yyyy)
	 * @return a data formatada
	 */
    public static String getDataFormatada(Calendar c, String formato) {
        SimpleDateFormat sdf = new SimpleDateFormat(formato);
        return sdf.format(c.getTime());
    }

    /**
	 * retorna a formatacao da data de acordo com a localiza��o.
	 * @param data a data a ser formatada
	 * @param local a localizacao
	 * @return o String contendo a data.
	 */
    public static String obterDataPorExtenso(Date data, Locale local) {
        SimpleDateFormat sdf = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.FULL, local);
        return sdf.format(data);
    }

    public static void main(String[] args) {
        Calendar c = Calendar.getInstance();
        Locale localizacao = Locale.getDefault();
        Locale localizacaoUS = new Locale("en", "us");
        Locale localizacaoAR = new Locale("es", "ar");
        Locale localizacaoFR = new Locale("fr", "fr");
        System.out.println("Brasil: " + obterDataPorExtenso(c.getTime(), localizacao));
        System.out.println("United States: " + obterDataPorExtenso(c.getTime(), localizacaoUS));
        System.out.println("Argentina: " + obterDataPorExtenso(c.getTime(), localizacaoAR));
        System.out.println("France: " + obterDataPorExtenso(c.getTime(), localizacaoFR));
    }
}
