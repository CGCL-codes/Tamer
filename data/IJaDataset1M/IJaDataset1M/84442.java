package vydavky.client.utils;

import java.io.Serializable;

/**
 * Objekt s konfiguraciou filtra nad zoznamom transakcii.
 */
public class TransakcieFilter implements Serializable {

    private static final long serialVersionUID = 1555L;

    /**
   * Enum sposobov filtrovania.
   */
    public enum TypFiltrovania {

        ZIADNE((byte) 0), PROJEKT((byte) 1), UCASTNIK((byte) 2), MESIAC((byte) 3), SUMA((byte) 4), TYP((byte) 5), MENA((byte) 6), POPIS((byte) 7), ID((byte) 255);

        private final byte poradie;

        private TypFiltrovania(final byte poradie) {
            this.poradie = poradie;
        }

        public static final TypFiltrovania getByPoradie(final byte poradie) {
            switch(poradie) {
                case 0:
                    return ZIADNE;
                case 1:
                    return PROJEKT;
                case 2:
                    return UCASTNIK;
                case 3:
                    return MESIAC;
                case 4:
                    return SUMA;
                case 5:
                    return TYP;
                case 6:
                    return MENA;
                case 7:
                    return POPIS;
                case (byte) 255:
                    return ID;
                default:
                    throw new IllegalStateException("Neplatny typ filtrovania");
            }
        }

        public byte getPoradie() {
            return this.poradie;
        }
    }

    public TransakcieFilter() {
        super();
    }

    public TransakcieFilter(final TypFiltrovania typFiltrovania) {
        super();
        this.typFiltrovania = typFiltrovania;
    }

    public TypFiltrovania typFiltrovania = TypFiltrovania.ZIADNE;

    /** ID projektu, z ktoreho maju byt transakcie. */
    public long projektId = -1;

    /** ID ucastnika, ktory ma v transakciach figurovat. */
    public long ucastnikId = -1;

    /** Rola ucastnika, ktory ma v transakciach figurovat. 1 - nakup, 2 - oboje, 3 - spotreba */
    public byte rola = 2;

    /** Mesiac, z ktoreho maju byt transakcie. */
    public byte mesiac = (byte) 255;

    /** Rok, z ktoreho maju byt transakcie. */
    public int rok = 0;

    /** Minimalna suma hladanych transakcii. */
    public float sumaOd = 0.0f;

    /** Maximalna suma hladanych transakcii. */
    public float sumaDo = 0.0f;

    /** Typ vydavku, ktory maju mat transakcie. */
    public long typId = -1;

    /** Mena, v ktorej maju byt transakcie. */
    public long menaId = -1;

    /** Substring vyhladavany v popise transakcii. */
    public String popis = "";

    /** ID konkretnej hladanej transakcie. */
    public long transakciaId = -1;
}
