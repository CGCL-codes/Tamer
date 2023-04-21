package transacao;

public class Fechar extends Transacao {

    private String tecnicoResponsavel;

    public Fechar() {
    }

    public Fechar(String tecnicoResponsavel) {
        super(FECHADO, true);
        setTecnicoResponsavel(tecnicoResponsavel);
    }

    /**
	 * @return the tecnicoResponsavel
	 */
    public String getTecnicoResponsavel() {
        return tecnicoResponsavel;
    }

    /**
	 * @param tecnicoResponsavel the tecnicoResponsavel to set
	 */
    public void setTecnicoResponsavel(String tecnicoResponsavel) {
        this.tecnicoResponsavel = tecnicoResponsavel;
    }
}
