package net.sourceforge.javaboleto;

import java.io.Serializable;
import net.sourceforge.javaboleto.banco.GeradorBoletoBancoBrasil;
import net.sourceforge.javaboleto.banco.GeradorBoletoBradesco;
import net.sourceforge.javaboleto.banco.GeradorBoletoHSBC;
import net.sourceforge.javaboleto.banco.GeradorBoletoItau;

public final class BancoEnum implements Serializable {

    private int numero;

    private String nome;

    private Class classGerador;

    private BancoEnum(int numero, String nome, Class classGerador) {
        this.numero = numero;
        this.nome = nome;
        this.classGerador = classGerador;
    }

    public static final BancoEnum BANCO_BRASIL = new BancoEnum(1, "Banco do Brasil", GeradorBoletoBancoBrasil.class);

    public static final BancoEnum BRADESCO = new BancoEnum(237, "Bradesco", GeradorBoletoBradesco.class);

    public static final BancoEnum ITAU = new BancoEnum(341, "Ita�", GeradorBoletoItau.class);

    public static final BancoEnum HSBC = new BancoEnum(399, "HSBC", GeradorBoletoHSBC.class);

    public static final BancoEnum[] ALL = { BANCO_BRASIL, BRADESCO, ITAU, HSBC };

    /**
	 * @return Returns the classGerador.
	 */
    public Class getClassGerador() {
        return classGerador;
    }

    /**
	 * @return Returns the nome.
	 */
    public String getNome() {
        return nome;
    }

    /**
	 * @return Returns the numero.
	 */
    public int getNumero() {
        return numero;
    }

    public BancoEnum lookup(int numero) {
        BancoEnum banco = null;
        for (int i = 0; banco == null && i < ALL.length; i++) {
            if (ALL[i].numero == numero) banco = ALL[i];
        }
        return banco;
    }
}
