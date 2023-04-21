package vydavky.client.ciselniky;

import java.io.Serializable;

/**
 * Abstraktny predok ciselnikov.
 */
public abstract class AbstractCiselnik implements Serializable {

    private static final long serialVersionUID = 117755l;

    /** ID zaznamu ciselniku. */
    protected Long id;

    /** Text zaznamu ciselniku. */
    protected String text;

    /** Poradie zaznamu pre triedenie pripadne rozhodovanie, ci zaznam pouzit. */
    protected Long poradie;

    /**
   * Bezparametricky konstruktor.
   */
    @SuppressWarnings("PMD")
    public AbstractCiselnik() {
    }

    /**
   * Plny konstruktor.
   */
    public AbstractCiselnik(final Long id, final String text, final Long poradie) {
        this.id = id;
        this.text = text;
        this.poradie = poradie;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public Long getPoradie() {
        return poradie;
    }

    public void setPoradie(final Long poradie) {
        this.poradie = poradie;
    }

    @Override
    public String toString() {
        return text;
    }
}
