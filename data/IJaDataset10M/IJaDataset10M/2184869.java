package galaxiia.configuration;

public class Module {

    static final String DEFAUT_NOM_INTERNE = "";

    static final String DEFAUT_AUTEUR = "Inconnu";

    static final String DEFAUT_DESCRIPTION = "Non disponible";

    static final int DEFAUT_VERSION = 0;

    public static String getNom(Class<?> module) {
        Identification identification = module.getAnnotation(Identification.class);
        if (identification == null) {
            return module.getCanonicalName();
        } else {
            return identification.nom();
        }
    }

    public static String getNomInterne(Class<?> module) {
        Identification identification = module.getAnnotation(Identification.class);
        if (identification == null || identification.nomInterne().length() == 0) {
            return module.getCanonicalName();
        } else {
            return identification.nomInterne();
        }
    }

    public static String getAuteur(Class<?> module) {
        Identification identification = module.getAnnotation(Identification.class);
        if (identification == null) {
            return DEFAUT_AUTEUR;
        } else {
            return identification.auteur();
        }
    }

    public static String getDescription(Class<?> module) {
        Identification identification = module.getAnnotation(Identification.class);
        if (identification == null) {
            return DEFAUT_DESCRIPTION;
        } else {
            return identification.description();
        }
    }

    public static int getVersion(Class<?> module) {
        Identification identification = module.getAnnotation(Identification.class);
        if (identification == null) {
            return DEFAUT_VERSION;
        } else {
            return identification.version();
        }
    }
}
