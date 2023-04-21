package model.business;

public class MaterialNecessario {

    private Integer id;

    /**
	 * @uml.property  name="Texto"
	 */
    private String texto = "";

    /**
	 * Getter of the property <tt>Texto</tt>
	 * @return  Returns the texto.
	 * @uml.property  name="Texto"
	 */
    public String getTexto() {
        return texto;
    }

    /**
	 * Setter of the property <tt>Texto</tt>
	 * @param Texto  The texto to set.
	 * @uml.property  name="Texto"
	 */
    public void setTexto(String texto) {
        this.texto = texto;
    }

    /**
	 * @uml.property   name="ordem"
	 * @uml.associationEnd   inverse="materialNecessario:model.business.Ordem"
	 */
    private Ordem ordem;

    /**
	 * Getter of the property <tt>ordem</tt>
	 * @return  Returns the ordem.
	 * @uml.property  name="ordem"
	 */
    public Ordem getOrdem() {
        return ordem;
    }

    /**
	 * Setter of the property <tt>ordem</tt>
	 * @param ordem  The ordem to set.
	 * @uml.property  name="ordem"
	 */
    public void setOrdem(Ordem ordem) {
        this.ordem = ordem;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
