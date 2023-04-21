package Model;

/** 
* @author Tassyo Tchesco
* @version 3.0
* @since 3.0
*/
public class Nota {

    private int id;

    private Aluno aluno;

    private float valor;

    private int ano;

    private int semestre;

    private int marcador = 0;

    public static final int INSERT = 1;

    public static final int UPDATE = 2;

    /** 
    * @author Tassyo Tchesco
    * @version 3.0
    * @since 3.0
    * @return retorna o id da nota. 
    */
    public int getId() {
        return id;
    }

    /** 
    *  Modifica o id da nota.
    * 
    * @author Tassyo Tchesco
    * @version 3.0
    * @since 3.0
    * @param novo id. 
    */
    public void setId(int id) {
        this.id = id;
    }

    /** 
    * @author Tassyo Tchesco
    * @version 3.0
    * @since 3.0
    * @return retorna o valor da nota. 
    */
    public float getValor() {
        return valor;
    }

    /** 
    * Modifica o valor da nota e atualiza o marcador desta.
    * 
    * @author Tassyo Tchesco
    * @version 3.0
    * @since 3.0
    * @param nova nota. 
    */
    public void setValor(float valor) {
        this.valor = valor;
        if (marcador != INSERT) marcador = UPDATE;
    }

    /** 
    * @author Tassyo Tchesco
    * @version 3.0
    * @since 3.0
    * @return retorna o ano da nota. 
    */
    public int getAno() {
        return ano;
    }

    /** 
    * Modifica o ano da nota.
    * 
    * @author Tassyo Tchesco
    * @version 3.0
    * @since 3.0
    * @param Novo ano. 
    */
    public void setAno(int ano) {
        this.ano = ano;
    }

    /** 
    * @author Tassyo Tchesco
    * @version 3.0
    * @since 3.0
    * @return retorna o semestre da nota. 
    */
    public int getSemestre() {
        return semestre;
    }

    /** 
    * Modifica o semestre da nota.
    * 
    * @author Tassyo Tchesco
    * @version 3.0
    * @since 3.0
    * @param novo semestre. 
    */
    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    /** 
    * @author Tassyo Tchesco
    * @version 3.0
    * @since 3.0
    * @return retorna o período da nota. 
    */
    public String getPeriodo() {
        if (semestre != 0) {
            StringBuilder sb = new StringBuilder(6);
            sb.append(ano);
            sb.append("/");
            sb.append(semestre);
            return sb.toString();
        } else return String.valueOf(ano);
    }

    /** 
    * @author Tassyo Tchesco
    * @version 3.0
    * @since 3.0
    * @return retorna o marcador. 
    */
    public int getMarcador() {
        return marcador;
    }

    /** 
    * Modifica o marcador.
    * 
    * @author Tassyo Tchesco
    * @version 3.0
    * @since 3.0
    * @param novo marcador. 
    */
    public void setMarcador(int marcador) {
        this.marcador = marcador;
    }

    /** 
    * @author Tassyo Tchesco
    * @version 3.0
    * @since 3.0
    * @return retorna o aluno da nota. 
    */
    public Aluno getAluno() {
        return aluno;
    }

    /** 
    * Modifica o aluno da nota.
    * 
    * @author Tassyo Tchesco
    * @version 3.0
    * @since 3.0
    * @param Novo aluno. 
    */
    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }
}
