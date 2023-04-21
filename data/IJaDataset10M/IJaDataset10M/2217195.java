package lexicon.jaxb;

/**
 * Class Target_type.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Target_type implements java.io.Serializable {

    /**
     * Field _form.
     */
    private java.lang.String _form;

    /**
     * Field _lexicalisation.
     */
    private Lexicalisation _lexicalisation;

    /**
     * Field _fs.
     */
    private Fs _fs;

    public Target_type() {
        super();
    }

    /**
     * Returns the value of field 'form'.
     * 
     * @return the value of field 'Form'.
     */
    public java.lang.String getForm() {
        return this._form;
    }

    /**
     * Returns the value of field 'fs'.
     * 
     * @return the value of field 'Fs'.
     */
    public Fs getFs() {
        return this._fs;
    }

    /**
     * Returns the value of field 'lexicalisation'.
     * 
     * @return the value of field 'Lexicalisation'.
     */
    public Lexicalisation getLexicalisation() {
        return this._lexicalisation;
    }

    /**
     * Sets the value of field 'form'.
     * 
     * @param form the value of field 'form'.
     */
    public void setForm(final java.lang.String form) {
        this._form = form;
    }

    /**
     * Sets the value of field 'fs'.
     * 
     * @param fs the value of field 'fs'.
     */
    public void setFs(final Fs fs) {
        this._fs = fs;
    }

    /**
     * Sets the value of field 'lexicalisation'.
     * 
     * @param lexicalisation the value of field 'lexicalisation'.
     */
    public void setLexicalisation(final Lexicalisation lexicalisation) {
        this._lexicalisation = lexicalisation;
    }
}
