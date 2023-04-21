package domini.programa;

import java.util.Calendar;

public class PeliculaN extends Normal {

    private String director;

    private int any;

    private boolean vo;

    public PeliculaN(String Nnom, Calendar NdataCaducitat, String Ndescripcio, float NpreuBase, int Nduracio, String Ndirector, int Nany, boolean Nvo) {
        super(Nnom, NdataCaducitat, Ndescripcio, NpreuBase, Nduracio);
        director = Ndirector;
        any = Nany;
        vo = Nvo;
    }

    public PeliculaN(String Nnom, Calendar NdataCaducitat, String Ndescripcio, float NpreuBase, int Nduracio) {
        super(Nnom, NdataCaducitat, Ndescripcio, NpreuBase, Nduracio);
        director = "";
        any = 2008;
        vo = false;
    }

    /**
     *  Consultora de l'atribut Director.
     *  @return El nom del director de la Pelicula.
     */
    public String getDirector() {
        return this.director;
    }

    /**
     *  Consultora de l'atribut Any.
     *  @return L'any en que es va rodar la pelicula.
     */
    public int getAny() {
        return this.any;
    }

    /**
     *  Consultora de l'atribut VO.
     *  @return Un boolea que diu si la pelicula esta en versio original.
     */
    public boolean getVo() {
        return this.vo;
    }

    /**
     *  Modificadora de l'atribut Director.
     *  @param nouDirector es el nom del nou director
     *  @pre -
     *  @post S'ha modificat l'atribut Director amb el nou valor
     */
    public void setDirector(String nouDirector) {
        this.director = nouDirector;
    }

    /**
     *  Modificadora de l'atribut Any.
     * @param nouAny Es el nou any a canviar
     *  @pre  -
     *  @post S'ha modificat l'any en que es va rodar la pelicula.
     */
    public void setAny(int nouAny) {
        this.any = nouAny;
    }

    /**
     *  Modificadora de l'atribut VO.
     *  @param nouVo es el bolea que posara a cert o fals el valor
     *  @pre  -
     *  @post S'ha modificat l'atribut.
     */
    public void setVo(boolean nouVo) {
        this.vo = nouVo;
    }
}
