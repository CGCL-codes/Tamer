package br.com.brapi.data;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Classe utilitária para se trabalhar com datas.
 * @author Tiago Nogueira
 * 
 */
public class BRData implements Serializable {

    public static final long MSEC_PER_HOUR = 1000L * 60L * 60L;

    public static final long MSEC_PER_DAY = MSEC_PER_HOUR * 24L;

    private Locale localidade = new Locale("pt", "BR");

    private Calendar calendario = Calendar.getInstance(localidade);

    private Pattern padrao = Pattern.compile("dd/MM/yyyy");

    private DateFormat formatador;

    /**
     * Contrutor padrao
     */
    public BRData() {
    }

    /**
     * Contrutor recebendo uma data pre-definida
     * @param p_data Data pre-definida utilizada para instaciar BRData
     */
    public BRData(Date p_data) {
        calendario.setTime(p_data);
    }

    /**
     * Contrutor recebendo uma data pre-definida e uma localizacao
     * @param p_data Data pre-definida utilizada para instaciar BRData
     * @param p_localidade Locale do calendario interno de BRData
     */
    public BRData(Date p_data, Locale p_localidade) {
        calendario.setTime(p_data);
        localidade = p_localidade;
    }

    /**
     * Contrutor recebendo om calendario pre-definido
     * @param p_calendario Calendario pre-definido para ser utilizado instanciando BRData
     */
    public BRData(Calendar p_calendario) {
        calendario = p_calendario;
    }

    /**
     * Contrutor recebendo uma localidade pre-definida
     * @param p_localidade Locale pre-definido
     */
    public BRData(Locale p_localidade) {
        localidade = p_localidade;
    }

    /**
     * Contrutor recebendo localizacao e calendario pre-definido
     * @param p_localidade localidade pre-definida para instanciar BRData
     * @param p_calendario calendario pre-definido pra instanciar BRData
     */
    public BRData(Locale p_localidade, Calendar p_calendario) {
        calendario = p_calendario;
        localidade = p_localidade;
    }

    /**
     * Contrutor recebendo dia,mes e ano para montar a data
     * @param dia dia da data
     * @param mes mes da data
     * @param ano ano da data
     */
    public BRData(int dia, int mes, int ano) {
        calendario.set(Calendar.DAY_OF_MONTH, dia);
        calendario.set(Calendar.MONTH, mes - 1);
        calendario.set(Calendar.YEAR, ano);
        calendario.set(Calendar.HOUR, 0);
        calendario.set(Calendar.MINUTE, 0);
        calendario.set(Calendar.SECOND, 0);
        calendario.set(Calendar.MILLISECOND, 0);
    }

    /**
     * Metodo que retorno o padrao utilizado no BRData
     * @return Retorno o padrao(Pattern) utilizado por BRData
     */
    public Pattern getPadrao() {
        return padrao;
    }

    /**
     * Metodo que muda o pradrao atual de BRData
     * @param p_padrao Novo padrao para utilizacao
     */
    public void setPadrao(Pattern p_padrao) {
        this.padrao = p_padrao;
    }

    /**
     * Retorna o calendario utilizado atualmente por BRData
     * @return Calendario atual utilizado por BRData
     */
    public Calendar getCalendario() {
        return calendario;
    }

    /**
     * Muda o Calendario atual utilizado por BRData
     * @param p_calendario Novo calendario a ser utilizado
     */
    public void setCalendario(Calendar p_calendario) {
        calendario = p_calendario;
    }

    /**
     * Retorna o Locale utilizado atualmente
     * @return Localidade utilizada atualmente
     */
    public Locale getLocalidade() {
        return localidade;
    }

    /**
     * Muda o Locale utilizado atualmente
     * @param p_localidade Novo locale a ser utilizado
     */
    public void setLocalidade(Locale p_localidade) {
        localidade = p_localidade;
    }

    /**
     * Retorna a data atual de BRData
     * @return A data do calendario atual
     */
    public Date getData() {
        return calendario.getTime();
    }

    /**
     * Muda a data atual de BRData
     * @param p_date Nova data a ser utilizada
     */
    public void setData(Date p_date) {
        calendario.setTime(p_date);
    }

    /**
     * Retorna o dia da data atual do calendario
     * @return Dia do mês atual
     */
    public int getDia() {
        return calendario.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Muda o dia da data atual do calendario
     * @param dia Novo dia a ser utilizado
     */
    public void setDia(int dia) {
        calendario.set(Calendar.DAY_OF_MONTH, Math.abs(dia));
    }

    /**
     * Retorna o mês atual do calendario
     * @return O mês do calendario atual
     */
    public int getMes() {
        return calendario.get(Calendar.MONTH + 1);
    }

    /**
     * Muda o mês do calendario atual
     * @param mes O novo mes a ser utilizado
     */
    public void setMes(int mes) {
        calendario.set(Calendar.MONTH, Math.abs(mes - 1));
    }

    /**
     * Retorna o ano do calendario atual
     * @return O ano do calendario
     */
    public int getAno() {
        return calendario.get(Calendar.YEAR);
    }

    /**
     * Muda o ano do calendario atual
     * @param ano O novo ano a ser utilizado
     */
    public void setAno(int ano) {
        calendario.set(Calendar.YEAR, Math.abs(ano));
    }

    /**
     * Adiciona um numero positivo de dias a data atual do calendario
     * @param p_numeroDias Numero de dias que se deseja adicionar
     */
    public void adicionarDias(int p_numeroDias) {
        calendario.add(Calendar.DATE, Math.abs(p_numeroDias));
    }

    /**
     * Remove um numero positivo de dias da data atual do calendario
     * @param p_numeroDias Numero de dias que se deseja remover
     */
    public void removerDias(int p_numeroDias) {
        calendario.add(Calendar.DATE, -(Math.abs(p_numeroDias)));
    }

    /**
     * Retona a representacao da data atual do calendario no padrao escolhido<br>
     * Ex.: String hoje = paraPadrao("dd/MM/yyyy")<br>
     * @param p_padrao Padrao que se deseja utilizar
     * @return String contendo a data do calendario atual convertida para o padrao escolhido
     */
    public String paraPadrao(String p_padrao) {
        String saidaComPadrao = "";
        formatador = new SimpleDateFormat(p_padrao);
        saidaComPadrao = formatador.format(getData());
        return saidaComPadrao;
    }

    /**
     * Retona a representacao da data atual do calendario no padrao escolhido<br>
     * Ex.: String hoje = paraPadrao(Pattern.compile("dd/MM/yyyy")) <br>
     * @param p_padrao Padrao que se deseja utilizar
     * @return String contendo a data do calendario atual convertida para o padrao escolhido
     */
    public String paraPadrao(Pattern p_padrao) {
        String saidaComPadrao = "";
        formatador = new SimpleDateFormat(p_padrao.pattern());
        saidaComPadrao = formatador.format(getData());
        return saidaComPadrao;
    }

    /**
     * Retorna ANO-MES-DIA em formato inteiro <br>
     * Ex:. int hoje = paraRepresentacaoNumerica();
     * @return inteiro contendo ANO-MES-DIA 
     */
    public int paraRepresentacaoNumerica() {
        String str_data = calendario.get(Calendar.YEAR) + "" + calendario.get(Calendar.MONTH) + "" + calendario.get(Calendar.DAY_OF_MONTH);
        int representacao = Integer.parseInt(str_data.trim());
        return representacao;
    }

    /**
     * Retorna ANO-MES-DIA em formato inteiro da data do calendario
     * @param p_calendario Calendario para se extrair a data atual
     * @return inteiro contendo ANO-MES-DIA 
     */
    public int paraRepresentacaoNumerica(Calendar p_calendario) {
        String str_data = p_calendario.get(Calendar.YEAR) + "" + p_calendario.get(Calendar.MONTH) + "" + p_calendario.get(Calendar.DAY_OF_MONTH);
        int representacao = Integer.parseInt(str_data.trim());
        return representacao;
    }

    /**
     * Retorna ANO-MES-DIA em formato inteiro da data do calendario
     * @param p_data Data para se extrair a data atual
     * @return inteiro contendo ANO-MES-DIA
     */
    public int paraRepresentacaoNumerica(Date p_data) {
        Calendar tmp_calendario = Calendar.getInstance(getLocalidade());
        tmp_calendario.setTime(p_data);
        String str_data = tmp_calendario.get(Calendar.YEAR) + "" + tmp_calendario.get(Calendar.MONTH) + "" + tmp_calendario.get(Calendar.DAY_OF_MONTH);
        int representacao = Integer.parseInt(str_data.trim());
        return representacao;
    }

    /**
     * Retorna ANO-MES-DIA-HORA-MINUTO-SEGUNDO-MILISEGUNDO em formato long da data atual do calendario
     * @return long contendo ANO-MES-DIA-HORA-MINUTO-SEGUNDO-MILISEGUNDO
     */
    public long paraRepresentacaoNumericaCompleta() {
        String srt_data = calendario.get(Calendar.YEAR) + "" + calendario.get(Calendar.MONTH) + "" + calendario.get(Calendar.DAY_OF_MONTH) + "" + calendario.get(Calendar.HOUR) + "" + calendario.get(Calendar.MINUTE) + "" + calendario.get(Calendar.SECOND) + "" + calendario.get(Calendar.MILLISECOND);
        long representacao = Long.parseLong(srt_data.trim());
        return representacao;
    }

    /**
     * Retorna ANO-MES-DIA-HORA-MINUTO-SEGUNDO-MILISEGUNDO em formato long da data atual do calendario
     * @param p_calendario Calendario para se extrair a data atual
     * @return long contendo ANO-MES-DIA-HORA-MINUTO-SEGUNDO-MILISEGUNDO
     */
    public long paraRepresentacaoNumericaCompleta(Calendar p_calendario) {
        String srt_data = p_calendario.get(Calendar.YEAR) + "" + p_calendario.get(Calendar.MONTH) + "" + p_calendario.get(Calendar.DAY_OF_MONTH) + "" + p_calendario.get(Calendar.HOUR) + "" + p_calendario.get(Calendar.MINUTE) + "" + p_calendario.get(Calendar.SECOND) + "" + p_calendario.get(Calendar.MILLISECOND);
        long representacao = Long.parseLong(srt_data.trim());
        return representacao;
    }

    /**
     * Retorna ANO-MES-DIA-HORA-MINUTO-SEGUNDO-MILISEGUNDO em formato long da data atual do calendario
     * @param p_data Data para se extrair a data atual
     * @return long contendo ANO-MES-DIA-HORA-MINUTO-SEGUNDO-MILISEGUNDO
     */
    public long paraRepresentacaoNumericaCompleta(Date p_data) {
        Calendar tmp_calendario = Calendar.getInstance(getLocalidade());
        tmp_calendario.setTime(p_data);
        String srt_data = tmp_calendario.get(Calendar.YEAR) + "" + tmp_calendario.get(Calendar.MONTH) + "" + tmp_calendario.get(Calendar.DAY_OF_MONTH) + "" + tmp_calendario.get(Calendar.HOUR) + "" + tmp_calendario.get(Calendar.MINUTE) + "" + tmp_calendario.get(Calendar.SECOND) + "" + tmp_calendario.get(Calendar.MILLISECOND);
        long representacao = Long.parseLong(srt_data.trim());
        return representacao;
    }

    /**
     * Metodo para verificar se a data atual do calendario e maior que a data passada via parametro
     * @param p_data Data utilizada na comparacao.
     * @return true caso seja maior, false menor ou igual
     */
    public boolean souMaiorQueData(Date p_data) {
        boolean souMaior = false;
        int representacaoAtiva = paraRepresentacaoNumerica();
        int representacaoParametro = paraRepresentacaoNumerica(p_data);
        if (representacaoAtiva > representacaoParametro) {
            souMaior = true;
        }
        return souMaior;
    }

    /**
     * Metodo para verificar se a data atual do calendario equivalente da data passada via parametro
     * @param p_data Data utilizada na comparacao.
     * @return true caso seja equivalente, false quando nao for
     */
    public boolean souEquivalenteAData(Date p_data) {
        boolean souEquivalente = false;
        int representacaoAtiva = paraRepresentacaoNumerica();
        int representacaoParametro = paraRepresentacaoNumerica(p_data);
        if (representacaoAtiva == representacaoParametro) {
            souEquivalente = true;
        }
        return souEquivalente;
    }

    /**
     * Metodo para verificar se a data atual do calendario e igual a data passada via parametro
     * @param p_data Data utilizada na comparacao.
     * @return true caso seja igual, false diferente
     */
    public boolean souIgualAData(Date p_data) {
        boolean souIgual = false;
        long representacaoAtiva = paraRepresentacaoNumericaCompleta();
        long representacaoParametro = paraRepresentacaoNumericaCompleta(p_data);
        if (representacaoAtiva == representacaoParametro) {
            souIgual = true;
        }
        return souIgual;
    }

    /** 
    *  Metodo para verificar a diferenca em dias entre a data do calendario atual e uma data
    *  especificada via parametro.Um retorno positivo indica que a hora especificada é maior que
    *  a hora do calendario.Um retorno negativo informa que a hora especificada é menor que a do calendario.
    *  @param p_date Data que se deseja verificar a difereca
    *  @return long contendo o numero de dias entre as duas datas
    */
    public long diferencaEmDias(Date p_date) {
        Calendar tmp = Calendar.getInstance(new Locale("pt", "BR"));
        tmp.setTime(p_date);
        long diff = ((tmp.getTime().getTime() - getData().getTime()) / MSEC_PER_DAY);
        return diff;
    }

    /** 
    *  Metodo para verificar a diferenca em horas entre a data do calendario atual e uma data
    *  especificada via parametro
    *  @param p_date Data que se deseja verificar a difereca
    *  @return long contendo o numero de horas entre as duas datas
    */
    public long diferencaEmHoras(Date p_date) {
        Calendar tmp = Calendar.getInstance(new Locale("pt", "BR"));
        tmp.setTime(p_date);
        long diffDias = ((tmp.getTime().getTime() - getData().getTime()) / MSEC_PER_DAY);
        long diffHoras = diffDias * 24;
        return diffHoras;
    }
}
