package mochila;

public class NoDaArvore implements Cloneable, Comparable<NoDaArvore> {

    private double valorDeZ;

    private ValorXi[] valores;

    private boolean noCandidato = false;

    private boolean existeInalteravel = false;

    private ObjetoDaMochila[] itensInalteraveis;

    private int indiceItem = 0;

    private int variavelDeDecisao;

    /**
	 * @param valorDeZ
	 * @param valores
	 */
    public NoDaArvore(double valorDeZ, ValorXi[] valores) {
        super();
        this.valorDeZ = valorDeZ;
        this.valores = valores;
    }

    public NoDaArvore(ValorXi[] valores) {
        super();
        this.valores = valores;
        itensInalteraveis = new ObjetoDaMochila[valores.length];
    }

    public NoDaArvore(boolean existeInalteravel, int variavelDeDecisao, int i) {
        super();
        this.existeInalteravel = existeInalteravel;
        this.variavelDeDecisao = variavelDeDecisao;
        itensInalteraveis = new ObjetoDaMochila[i];
        valores = new ValorXi[i];
    }

    /**
	 * @return the valorDeZ
	 */
    public double getValorDeZ() {
        return valorDeZ;
    }

    /**
	 * @param valorDeZ the valorDeZ to set
	 */
    public void setValorDeZ(double valorDeZ) {
        this.valorDeZ = valorDeZ;
    }

    /**
	 * @return the valores
	 */
    public ValorXi[] getValores() {
        return valores;
    }

    /**
	 * @param valores the valores to set
	 */
    public void setValores(ValorXi[] valores) {
        this.valores = valores;
    }

    /**
	 * @param indice
	 * @param valor
	 */
    public void alterarXi(int indice, double valor) {
        valores[indice].setValor(valor);
    }

    public void geraFilho(int valor) {
        if (valor == 0 || valor == 1) {
            alterarXi(variavelDeDecisao, valor);
            valores[variavelDeDecisao].setAlteravel(false);
        }
    }

    public int compareTo(NoDaArvore o) {
        if (valorDeZ == o.valorDeZ) return 0;
        if (valorDeZ < o.valorDeZ) return -1; else return 1;
    }

    public int getVariavelDeDecisao() {
        return variavelDeDecisao;
    }

    public void setVariavelDeDecisao(int variavelDeDecisao) {
        this.variavelDeDecisao = variavelDeDecisao;
    }

    public boolean isNoCandidato() {
        return noCandidato;
    }

    public void setNoCandidato(boolean noCandidato) {
        this.noCandidato = noCandidato;
    }

    public void setItensInalteraveis(ObjetoDaMochila item) {
        itensInalteraveis[indiceItem] = item;
        indiceItem++;
    }

    public ObjetoDaMochila[] getItensInalteraveis() {
        return (itensInalteraveis);
    }

    public boolean isExisteInalteravel() {
        return existeInalteravel;
    }

    public void setExisteInalteravel(boolean existeInalteravel) {
        if (this.existeInalteravel == false) this.existeInalteravel = existeInalteravel;
    }

    public void setValoresXi(ValorXi[] valores) {
        for (int i = 0; i < valores.length; i++) {
            ValorXi valor = new ValorXi(valores[i].getValor());
            valor.setAlteravel(valores[i].isAlteravel());
            this.valores[i] = valor;
            this.valores[i].setValor(0);
        }
    }

    public void setItensInalteraveisXi(ObjetoDaMochila[] objetos) {
        for (int i = 0; i < objetos.length; i++) {
            if (objetos[i] == null) break;
            ObjetoDaMochila objeto = new ObjetoDaMochila(objetos[i].getPeso(), objetos[i].getValor());
            this.itensInalteraveis[indiceItem] = objeto;
            indiceItem++;
        }
    }
}
