package Mapeamento.Atributos;

import java.util.ArrayList;
import java.lang.Exception;

/**
 *Classe de Atributos do tipo Serial ou BigSerial. 
 *
 *<BR>Possui: 
 * Nome, 
 * Tipo. 
 * 
 *<BR>Implementa: 
 * Unico. 
 *
 *@author Tiago Falcao
 *@version 1.0b 15/05/07
 */
public class AtributoSerial extends Atributo {

    /**
     * Boolean que determina se para o Atributo cada valor deve aparecer somente uma vez
     */
    protected boolean Unico;

    /**
     * Cria uma instancia do Atributo Serial
     */
    public AtributoSerial() {
        super();
        Unico = false;
    }

    public void loadArray(Object[] o) {
        if (o.length != 13) return;
        Nome = (String) o[0];
        Tipo = (String) o[1];
        Unico = ((Boolean) o[2]).booleanValue();
        MultiValorado = ((Boolean) o[12]).booleanValue();
    }

    public Object[] getArray() {
        Object[] o = new Object[13];
        o[0] = Nome;
        o[1] = Tipo;
        o[2] = new Boolean(Unico);
        o[3] = new String();
        o[4] = new Boolean(false);
        o[5] = new Boolean(false);
        o[6] = new Boolean(false);
        o[7] = new Integer(0);
        o[8] = new Integer(0);
        o[9] = new String();
        o[10] = new String();
        o[11] = new String();
        o[12] = new Boolean(MultiValorado);
        return o;
    }

    /**
     * Atribui o Tipo ao Atributo
     * @param Tipo Tipo do atributo
     * @throws java.lang.Exception Se Tipo for diferente de Serial e BigSerial
     */
    public void setTipo(String Tipo) throws Exception {
        Tipo = Tipo.toLowerCase();
        if ((!Tipo.equals("serial")) && (!Tipo.equals("bigserial"))) throw new java.lang.Exception("Este e Atributo somente do Tipo [Big]Serial");
        this.Tipo = Tipo;
    }

    /**
     * Atribui que diz se o atributo em questao eh UNICO ou nao
     * @param Unico true se o elemento eh unicamente instanciado
     * @throws java.lang.Exception Nunca
     */
    public void setUnico(boolean Unico) {
        this.Unico = Unico;
    }

    /**
     * Retorna Unico formatado para o SGBD
     * @param SGBD Nome do SGBD a ser utilizado
     * @throws java.lang.Exception Nunca
     * @return String com Unico formatado para o SGBD
     */
    protected String getUnicoSQL(String SGBD) {
        if (this.Unico) return "UNIQUE"; else return "";
    }

    /**
     * Retorna parte do SQL para criacao do Atributo para o SGBD especificado, 
     * sem o Nome do Atributo.
     * @param SGBD Nome do SGBD para o qual sera gerado o SQL
     * @return String SQL para cofiguracao do Atributo
     * @throws java.lang.Exception Se Atributo n�o possuir Nome ou Tipo
     */
    public String getInfoSQL(String SGBD) throws Exception {
        String Temp;
        String Retorno;
        Retorno = "\t" + this.getTipoSQL(SGBD);
        Temp = this.getUnicoSQL(SGBD);
        if (Temp.length() > 0) Retorno += "\t" + Temp;
        return Retorno;
    }
}
