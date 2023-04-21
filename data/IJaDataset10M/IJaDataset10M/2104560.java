package parser;

public class ASTModalOperator extends ASTModalFormula {

    /**
	 * Modale Operatoren (au�er Negation) besitzen einen Index, die
	 * die jeweilige Gruppe bzw. den jeweiligen Agenten angeben.
	 */
    private String index;

    /**
	 * Wird f�r die Auswertung verwendet.
	 */
    private String image;

    public ASTModalOperator(int id) {
        super(id);
    }

    public ASTModalOperator(ModalFormulaParser p, int id) {
        super(p, id);
    }

    /** Accept the visitor. * */
    public Object jjtAccept(ModalFormulaParserVisitor visitor, data.Data data) {
        return visitor.visit(this, data);
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
