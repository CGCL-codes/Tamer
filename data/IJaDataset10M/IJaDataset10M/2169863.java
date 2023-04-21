package parma_polyhedra_library;

public class Linear_Expression_Coefficient extends Linear_Expression {

    protected Coefficient coeff;

    public Linear_Expression_Coefficient(Coefficient c) {
        coeff = new Coefficient(c);
    }

    public Coefficient argument() {
        return coeff;
    }

    public Linear_Expression_Coefficient clone() {
        return new Linear_Expression_Coefficient(new Coefficient(coeff));
    }
}
