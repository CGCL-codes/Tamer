package org.chordman.modelo.acordes;

import java.util.ArrayList;
import java.util.List;
import org.chordman.modelo.escalas.Escala;
import org.chordman.modelo.escalas.EscalasManager;
import org.chordman.modelo.notas.Nota;

public class AcordeMayor extends Acorde {

    private final List<Nota> notas;

    private final int NOTA_0 = 0;

    private final int NOTA_1 = 4;

    private final int NOTA_2 = 7;

    private Nota tono;

    private static final Escala escalaCromatica = EscalasManager.getInstance().getEscalaByCodigo("Chromatic");

    /**
	 * Construye un acorde mayor para el tono dado.
	 * 
	 * @param tono
	 *            Tono
	 */
    public AcordeMayor(Nota tono) {
        this.tono = tono;
        this.notas = new ArrayList<Nota>(3);
        List<Nota> notasEscala = escalaCromatica.getNotas(tono);
        this.notas.add(0, notasEscala.get(NOTA_0));
        this.notas.add(1, notasEscala.get(NOTA_1));
        this.notas.add(2, notasEscala.get(NOTA_2));
    }

    /**
	 * Construye una copia del {@link AcordeMayor}
	 * @param acordeMayor
	 */
    public AcordeMayor(Acorde acorde) {
        this(acorde.getTono());
    }

    public int getCantidadNotas() {
        return notas.size();
    }

    public String getNombre() {
        return this.tono.getCodigoIngles();
    }

    public List<Nota> getNotas() {
        return notas;
    }

    public void aumentar(int cantidadSemitonos) {
        this.tono = this.tono.getNotaRelativa(cantidadSemitonos);
        List<Nota> notasEscala = escalaCromatica.getNotas(tono);
        this.notas.set(0, notasEscala.get(NOTA_0));
        this.notas.set(1, notasEscala.get(NOTA_1));
        this.notas.set(2, notasEscala.get(NOTA_2));
    }

    public Nota getTono() {
        return this.tono;
    }
}
