package co.com.uniandes.creditscore.mundo;

public class FicoPersona {

    /**
	 * Contiene el score de la persona
	 */
    private double score;

    /**
	 * Contiene la persona 
	 */
    private Persona persona;

    /**
	 * Constructor con parametros
	 * @param persona
	 */
    public FicoPersona(Persona persona, double score) {
        this.persona = persona;
        this.score = score;
    }

    /**
	 * Constructor de Fico persona sin par�metros
	 */
    public FicoPersona() {
        this.persona = new Persona();
        this.score = 0;
    }

    /**
	 * Retorna el score
	 * @return
	 */
    public double getScore() {
        return score;
    }

    /**
	 * Cambia el score
	 * @return
	 */
    public void setScore(double score) {
        this.score = score;
    }

    /**
	 * Calcula el riesgo crediticio de la persona
	 */
    public void calcularRiesgoCrediticio() {
    }

    /**
	 * Retorna la persona de FicoEvaluacion
	 * @return persona
	 */
    public Persona getPersona() {
        return persona;
    }

    /**
	 * Cambia la persona de FicoEvaluacion
	 * @param persona
	 */
    public void setPersona(Persona persona) {
        this.persona = persona;
    }
}
