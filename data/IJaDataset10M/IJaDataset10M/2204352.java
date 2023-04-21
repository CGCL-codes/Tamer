package Mapper;

public class GrammarMock extends Grammar {

    public GrammarMock() {
    }

    public GrammarMock(Grammar g) {
        super(g);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean genotype2Phenotype() {
        return true;
    }

    public Object getGenotype() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getPhenotype() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean phenotype2Genotype() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void setGenotype(Object g) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setPhenotype(Object p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getDerivationString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
