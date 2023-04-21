package com.doculibre.intelligid.utils.delta;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.beanutils.PropertyUtils;
import com.doculibre.intelligid.entites.FicheMetadonnees;
import com.doculibre.intelligid.entites.UniteAdministrative;
import com.doculibre.intelligid.entites.UtilisateurIFGD;
import com.doculibre.intelligid.entites.ddv.ElementDomaineValeurs;
import com.doculibre.intelligid.entites.ddv.RoleIFGD;
import com.doculibre.intelligid.utils.FGDDateUtils;

public abstract class AbstractDelta {

    public static final String LIBELLE_VALEUR_NULLE = "Valeur nulle";

    public static final String SEP = "\n\r";

    protected List<DeltaPropriete> deltaProprietes = new ArrayList<DeltaPropriete>();

    private Object avant;

    private Object apres;

    public AbstractDelta(Object avant, Object apres) {
        this.avant = avant;
        this.apres = apres;
    }

    protected void ajouterSiDifferent(String libellePropriete, Object propertyValueAvant, Object propertyValueApres) {
        if (propertyValueAvant == null && propertyValueApres == null) {
        } else if (propertyValueAvant == null || propertyValueApres == null || !propertyValueAvant.equals(propertyValueApres)) {
            this.deltaProprietes.add(new DeltaPropriete(libellePropriete, convertObjectToString(libellePropriete, propertyValueAvant, avant), convertObjectToString(libellePropriete, propertyValueApres, apres)));
        }
    }

    protected void ajouterSiDifferent(String libellePropriete, String propriete) {
        try {
            ajouterSiDifferent(libellePropriete, PropertyUtils.getProperty(avant, propriete), PropertyUtils.getProperty(apres, propriete));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    protected String convertObjectToString(String libellePropriete, Object o, Object comparedObject) {
        String valeur;
        if (o == null) {
            valeur = LIBELLE_VALEUR_NULLE;
        } else if (o instanceof Date) {
            valeur = FGDDateUtils.getInstance().format((Date) o);
        } else if (o instanceof Calendar) {
            valeur = FGDDateUtils.getInstance().format(((Calendar) o).getTime());
        } else if (o instanceof ElementDomaineValeurs) {
            ElementDomaineValeurs e = (ElementDomaineValeurs) o;
            valeur = e.getId() + " - " + e.getDescription() + " (" + e.getCode() + ")";
        } else if (o instanceof UniteAdministrative) {
            UniteAdministrative u = (UniteAdministrative) o;
            valeur = u.getCode() + " - " + u.getNom();
        } else if (o instanceof UtilisateurIFGD) {
            UtilisateurIFGD u = (UtilisateurIFGD) o;
            valeur = u.getId() + " - " + u.getNomPrenom() + " (" + u.getNomUtilisateur() + ")";
        } else if (o instanceof FicheMetadonnees) {
            FicheMetadonnees f = (FicheMetadonnees) o;
            valeur = f.getId() + " - " + f.getTitre();
        } else if (o instanceof RoleIFGD) {
            RoleIFGD r = (RoleIFGD) o;
            valeur = r.getDescription();
        } else if (o instanceof List) {
            List listeValeurs = (List) o;
            StringBuffer sb = new StringBuffer("[");
            for (Object valeurListe : listeValeurs) {
                String libelleValeurListe = convertObjectToString(libellePropriete, valeurListe, comparedObject);
                sb.append(libelleValeurListe);
                sb.append("; ");
            }
            sb.append("]");
            valeur = sb.toString();
        } else {
            valeur = o.toString();
        }
        return valeur;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (DeltaPropriete deltaPropriete : deltaProprietes) {
            sb.append(deltaPropriete.getLibellePropriete());
            sb.append(" : { Valeur avant : ");
            sb.append(deltaPropriete.getValeurAvant());
            sb.append(" } > { Valeur après : ");
            sb.append(deltaPropriete.getValeurApres());
            sb.append(" }");
            sb.append(SEP);
        }
        if (sb.length() == 0) {
            sb.append("Aucune différence");
        }
        String stringValue = sb.toString();
        if (stringValue.length() > 2000) {
            stringValue = stringValue.substring(0, 2000);
        }
        return stringValue;
    }
}
