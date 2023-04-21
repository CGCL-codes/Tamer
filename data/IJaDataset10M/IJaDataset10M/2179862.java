package org.larozanam.arq.component.consultadinamica;

public class ConsultaNumericaInteira extends ConsultaRelacional<Integer> {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2648860092980938900L;

    public ConsultaNumericaInteira(String desc, String atr) {
        super(desc, atr);
    }

    @Override
    public OperadorRelacional getOperadorConsulta() {
        return super.getOperadorConsulta();
    }

    @Override
    public void setOperadorConsulta(OperadorRelacional op) {
        super.setOperadorConsulta(op);
    }
}
