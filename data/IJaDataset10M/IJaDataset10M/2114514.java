package vydavky.client.ciselniky;

/**
 * Konstanty pre niektore ciselniky pouzivane v aplikacii.
 */
public final class Ciselniky {

    /**
   * Nezverejneny konstruktor - tato trieda je kniznicna.
   */
    private Ciselniky() {
    }

    /** Verzia aplikacie - velke cislo. */
    public static final int VERSION_MAJOR = 0;

    /** Verzia aplikacie - male cislo. */
    public static final int VERSION_MINOR = 6;

    /** Priznak, ci je aplikacia v debugovacom rezime. */
    public static final boolean DEBUG = false;

    /** Retazec pre formatovanie meny - 2 desatinne miesta. */
    public static final String FORMAT_MENA = "#0.00";

    /** Retazec pre formatovanie kurzu - 3 desatinne miesta. */
    public static final String FORMAT_KURZ = "#0.000";

    /** Retazec pre formatovanie datumu. */
    public static final String FORMAT_DATUM = "d.M.yyyy";

    /** Typ bilancie - bilancia projektu. */
    public static final byte BILANCIA_PROJEKT = 1;

    /** Typ bilancie - bilancia cloveka. */
    public static final byte BILANCIA_CLOVEK_PROJEKT = 2;

    /**
   * Riesenie odpovedou ANO.
   * Tyka sa vztahov 7 a 8.
   *
   * @see TypVztahu#VZTAH_7_ZMENA_NA_SERVERI_STORNO
   * @see TypVztahu#VZTAH_8_STORNO_POTVRDIT
   */
    public static final int RIESENIE_ANO = 1;

    /**
   * Riesenie odpovedou NIE.
   * Tyka sa vztahov 7 a 8.
   *
   * @see TypVztahu#VZTAH_7_ZMENA_NA_SERVERI_STORNO
   * @see TypVztahu#VZTAH_8_STORNO_POTVRDIT
   */
    public static final int RIESENIE_NIE = 2;

    /**
   * Riesenie obnovenim stornovaneho objektu.
   * Tyka sa vztahu c. 1 - storno na serveri, zmena na klientovi.
   *
   * @see TypVztahu#VZTAH_1_STORNO_NA_SERVERI_ZMENIT
   */
    public static final int RIESENIE_OBNOVIT_STORNOVANE = 3;

    /**
   * Riesenie zalozenim noveho objektu.
   * Tyka sa vztahu c. 1 - storno na serveri, zmena na klientovi.
   *
   * @see TypVztahu#VZTAH_1_STORNO_NA_SERVERI_ZMENIT
   */
    public static final int RIESENIE_ZALOZIT_NOVE = 4;

    /**
   * Riesenie nahradou stornovaneho objektu.
   * Tyka sa vztahu c. 1 - storno na serveri, zmena na klientovi.
   *
   * @see TypVztahu#VZTAH_1_STORNO_NA_SERVERI_ZMENIT
   */
    public static final int RIESENIE_NAHRADIT_STORNOVANE = 5;

    /**
   * Riesenie ponechanim objektu stornovaneho.
   * Tyka sa vztahu c. 1 - storno na serveri, zmena na klientovi.
   *
   * @see TypVztahu#VZTAH_1_STORNO_NA_SERVERI_ZMENIT
   */
    public static final int RIESENIE_NECHAT_STORNOVANE = 6;

    /**
   * Riesenie ponechanim serverovej verzie.
   * Tyka sa vztahu c. 2 - zmena na serveri, zmena na klientovi.
   *
   * @see TypVztahu#VZTAH_2_ZMENA_NA_SERVERI_ZMENIT
   */
    public static final int RIESENIE_NECHAT_SERVER = 7;

    /**
   * Riesenie nahradou za klientsky objekt.
   * Tyka sa vztahu c. 2 - zmena na serveri, zmena na klientovi.
   *
   * @see TypVztahu#VZTAH_2_ZMENA_NA_SERVERI_ZMENIT
   */
    public static final int RIESENIE_NAHRADIT_KLIENT = 8;

    /** Identifikator, od ktoreho vyssie su ID specialnych typov vydavkov. */
    public static final long SPECIALNE_TYPY_BASE_ID = 10000L;

    /** Specialny typ vydavku - prevod. */
    public static final CTypVydavku TYP_PREVOD = new CTypVydavku(SPECIALNE_TYPY_BASE_ID, "Prevod", SPECIALNE_TYPY_BASE_ID);

    /** Specialny typ vydavku - buduci. */
    public static final CTypVydavku TYP_BUDUCI = new CTypVydavku(SPECIALNE_TYPY_BASE_ID + 1L, "Buduci", SPECIALNE_TYPY_BASE_ID + 1L);
}
