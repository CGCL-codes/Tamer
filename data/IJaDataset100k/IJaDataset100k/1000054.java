package netgest.bo.ql;

/**
 *
 * Classe muito simples que apenas armazena 3 objectos
 */
public class Triple {

    private Object p1;

    private Object p2;

    private Object p3;

    /**Consutrutor por defeito que inicializa todos os objectos a <code>null</code>*/
    public Triple() {
        p1 = null;
        p2 = null;
        p3 = null;
    }

    /**Construtor que inicia os objectos com os respectivos valores
     * @param one   objecto a ser guardado na primeira posição
     * @param two   objecto a ser guardado na segunda posição
     * @param three   objecto a ser guardado na terceira posição
     */
    public Triple(Object one, Object two, Object three) {
        p1 = one;
        p2 = two;
        p3 = three;
    }

    /**Método de comparação
     * @param p   objecto a comparar
     * */
    public boolean equals(Triple p) {
        if (p1.equals(p.p1) && p2.equals(p.p2) && p3.equals(p.p3)) {
            return true;
        } else {
            return false;
        }
    }

    /**Retorna o primeiro objecto
     * @return objecto na primeira posição
     */
    public Object getFirst() {
        return p1;
    }

    /**Retorna o segundo objecto
     * @return objecto na segunda posição
     */
    public Object getSecond() {
        return p2;
    }

    /**Retorna o terceiro objecto
     * @return objecto na terceira posição
     */
    public Object getThird() {
        return p3;
    }

    /**Define o primeiro objecto
     * @param p   objecto a ser guardado
     */
    public void setFirst(Object p) {
        p1 = p;
    }

    /**Define o segundo objecto
     * @param p   objecto a ser guardado
     */
    public void setSecond(Object p) {
        p2 = p;
    }

    /**Define o terceiro objecto
     * @param p   objecto a ser guardado
     */
    public void setThird(Object p) {
        p3 = p;
    }
}
