package org.fudaa.dodico.hydraulique1d.metier;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import org.fudaa.dodico.boony.BoonyDeserializerHandler;
import org.fudaa.dodico.boony.BoonyXgzDeserializer;
import org.fudaa.dodico.boony.BoonyXgzSerializer;
import org.fudaa.dodico.boony.BoonyXmlDeserializer;
import org.fudaa.dodico.boony.BoonyXmlSerializer;
import org.fudaa.dodico.hydraulique1d.metier.calageauto.MetierCalageAuto;
import org.fudaa.dodico.hydraulique1d.metier.MetierHydraulique1d;
import org.fudaa.dodico.hydraulique1d.metier.calageauto.*;
import org.fudaa.dodico.hydraulique1d.metier.evenement.Notifieur;
import org.fudaa.dodico.hydraulique1d.metier.qualitedeau.MetierParametresQualiteDEau;
import com.memoire.yapod.YapodSerializer;

/**
 * Impl�mentation de l'objet m�tier g�n�ral "�tude 1D".
 * Contient entre autre les donn�es hydrauliques, les param�tres g�n�raux,
 * les param�tres temporels, le r�seau hydraulique, les r�sultats g�n�raux
 * d'une �tude.
 * @version      $Revision: 1.5 $ $Date: 2008-03-07 13:12:39 $ by $Author: jm_lacombe $
 * @author       Axel von Arnim
 */
public class MetierEtude1d extends MetierHydraulique1d {

    public static final String FORMAT_VERSION = "0.6";

    public static final String FORMAT_VERSION_0_5 = "0.5";

    public static final String FORMAT_VERSION_0_4 = "0.4";

    public static final String FORMAT_VERSION_0_3 = "0.3";

    public void initialise(MetierHydraulique1d _o) {
        super.initialise(_o);
        if (_o instanceof MetierEtude1d) {
            MetierEtude1d q = (MetierEtude1d) _o;
            description(q.description());
            donneesHydro((MetierDonneesHydrauliques) q.donneesHydro().creeClone());
            paramTemps((MetierParametresTemporels) q.paramTemps().creeClone());
            paramGeneraux((MetierParametresGeneraux) q.paramGeneraux().creeClone());
            paramResultats((MetierParametresResultats) q.paramResultats().creeClone());
            reseau((MetierReseau) q.reseau().creeClone());
            resultatsGeneraux((MetierResultatsGeneraux) q.resultatsGeneraux().creeClone());
            etatMenu(q.etatMenu());
            versionFormat(q.versionFormat());
            identifieurs(q.identifieurs());
            if (q.qualiteDEau() != null) qualiteDEau((MetierParametresQualiteDEau) q.qualiteDEau().creeClone());
            if (q.calageAuto() != null) calageAuto((MetierCalageAuto) q.calageAuto().creeClone());
        }
    }

    public final MetierHydraulique1d creeClone() {
        MetierEtude1d p = new MetierEtude1d();
        p.initialise(this);
        return p;
    }

    public final String toString() {
        String s = "etude1d";
        return s;
    }

    public MetierEtude1d() {
        super();
        description_ = "Etude hydraulique1d";
        donneesHydro_ = new MetierDonneesHydrauliques();
        paramTemps_ = new MetierParametresTemporels();
        paramGeneraux_ = new MetierParametresGeneraux();
        paramResultats_ = new MetierParametresResultats();
        reseau_ = new MetierReseau();
        resultatsGeneraux_ = new MetierResultatsGeneraux();
        calageAuto_ = new MetierCalageAuto();
        etatMenu_ = 0;
        versionFormat_ = FORMAT_VERSION;
        identifieurs_ = new SMetierIdentifieur[0];
        qualiteDEau_ = new MetierParametresQualiteDEau();
        notifieObjetCree();
    }

    public void dispose() {
        description_ = null;
        if (donneesHydro_ != null) {
            donneesHydro_.dispose();
            donneesHydro_ = null;
        }
        if (paramTemps_ != null) {
            paramTemps_.dispose();
            paramTemps_ = null;
        }
        if (paramGeneraux_ != null) {
            paramGeneraux_.dispose();
            paramGeneraux_ = null;
        }
        if (paramResultats_ != null) {
            paramResultats_.dispose();
            paramResultats_ = null;
        }
        if (reseau_ != null) {
            reseau_.dispose();
            reseau_ = null;
        }
        if (resultatsGeneraux_ != null) {
            resultatsGeneraux_.dispose();
            resultatsGeneraux_ = null;
        }
        if (qualiteDEau_ != null) {
            qualiteDEau_.dispose();
            qualiteDEau_ = null;
        }
        if (calageAuto_ != null) {
            calageAuto_.dispose();
            calageAuto_ = null;
        }
        etatMenu_ = 0;
        versionFormat_ = null;
        identifieurs_ = null;
        Identifieur.IDENTIFIEUR.table_ = new Hashtable();
        super.dispose();
        System.gc();
    }

    private String description_;

    public String description() {
        return description_;
    }

    public void description(String s) {
        if (description_.equals(s)) return;
        description_ = s;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "description");
    }

    private MetierDonneesHydrauliques donneesHydro_;

    public MetierDonneesHydrauliques donneesHydro() {
        return donneesHydro_;
    }

    public void donneesHydro(MetierDonneesHydrauliques s) {
        if (donneesHydro_ == s) return;
        donneesHydro_ = s;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "donneesHydro");
    }

    private MetierParametresTemporels paramTemps_;

    public MetierParametresTemporels paramTemps() {
        return paramTemps_;
    }

    public void paramTemps(MetierParametresTemporels s) {
        if (paramTemps_ == s) return;
        paramTemps_ = s;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "paramTemps");
    }

    private MetierParametresGeneraux paramGeneraux_;

    public MetierParametresGeneraux paramGeneraux() {
        return paramGeneraux_;
    }

    public void paramGeneraux(MetierParametresGeneraux s) {
        if (paramGeneraux_ == s) return;
        paramGeneraux_ = s;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "paramGeneraux");
    }

    private MetierParametresResultats paramResultats_;

    public MetierParametresResultats paramResultats() {
        return paramResultats_;
    }

    public void paramResultats(MetierParametresResultats s) {
        if (paramResultats_ == s) return;
        paramResultats_ = s;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "paramResultats");
    }

    private MetierReseau reseau_;

    public MetierReseau reseau() {
        return reseau_;
    }

    public void reseau(MetierReseau s) {
        if (reseau_ == s) return;
        reseau_ = s;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "reseau");
    }

    private MetierResultatsGeneraux resultatsGeneraux_;

    public MetierResultatsGeneraux resultatsGeneraux() {
        return resultatsGeneraux_;
    }

    public void resultatsGeneraux(MetierResultatsGeneraux s) {
        if (resultatsGeneraux_ == s) return;
        resultatsGeneraux_ = s;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "resultatsGeneraux");
    }

    private int etatMenu_;

    public int etatMenu() {
        return etatMenu_;
    }

    public void etatMenu(int s) {
        if (etatMenu_ == s) return;
        etatMenu_ = s;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "etatMenu");
    }

    private String versionFormat_;

    public String versionFormat() {
        return versionFormat_;
    }

    public void versionFormat(String s) {
        if (versionFormat_.equals(s)) return;
        versionFormat_ = s;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "versionFormat");
    }

    private SMetierIdentifieur[] identifieurs_;

    public SMetierIdentifieur[] identifieurs() {
        return identifieurs_;
    }

    public void identifieurs(SMetierIdentifieur[] s) {
        identifieurs_ = s;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "identifieurs");
    }

    private MetierCalageAuto calageAuto_;

    public MetierCalageAuto calageAuto() {
        return calageAuto_;
    }

    public void calageAuto(MetierCalageAuto _calageAuto) {
        if (calageAuto_ == _calageAuto) return;
        calageAuto_ = _calageAuto;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "calageAuto");
    }

    private MetierParametresQualiteDEau qualiteDEau_;

    public MetierParametresQualiteDEau qualiteDEau() {
        return qualiteDEau_;
    }

    public void qualiteDEau(MetierParametresQualiteDEau s) {
        if (qualiteDEau_ == s) return;
        qualiteDEau_ = s;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "qualiteDEau");
    }

    /**
   * Serialise sous forme XML et ecrit l'etude dans un flux.
   * @param _out Le flux de sortie.
   */
    public void writeTo(OutputStream _out) {
        SMetierIdentifieur[] ids = new SMetierIdentifieur[Identifieur.IDENTIFIEUR.table_.size()];
        Enumeration cles = Identifieur.IDENTIFIEUR.table_.keys();
        int i = 0;
        while (cles.hasMoreElements()) {
            SMetierIdentifieur id = new SMetierIdentifieur();
            id.nom = (String) cles.nextElement();
            id.numero = ((Integer) Identifieur.IDENTIFIEUR.table_.get(id.nom)).intValue();
            ids[i] = id;
            i++;
        }
        identifieurs(ids);
        Object[] infoEtude = new Object[2];
        infoEtude[0] = FORMAT_VERSION;
        infoEtude[1] = this;
        try {
            BoonyXmlSerializer fluxEcrit = new BoonyXmlSerializer(false, true);
            fluxEcrit.startWriting(_out);
            fluxEcrit.write(infoEtude);
            fluxEcrit.endWriting();
        } catch (Exception ex) {
            System.err.println("$$$ " + ex);
            ex.printStackTrace();
        }
    }

    /**
   * Cr�e l'objet metier etude a partir d'un flux contenant une serialisation XML.
   * @param _ins Le flux d'entr�e.
   * @param compresse true : Le flux est compress�
   * @param _version La version de la s�rialisation.
   * @return L'objet metier etude.
   */
    public MetierEtude1d readFrom(InputStream _ins, boolean compresse, String _version) {
        MetierEtude1d etude = null;
        if (FORMAT_VERSION_0_3.compareTo(_version) >= 0) {
            System.err.println(" Le format de fichier n'est plus support�.");
            System.err.println(" Version fichier Etude hydraulique1d : 0.3");
            System.err.println(" Version actuel  Etude hydraulique1d : " + FORMAT_VERSION);
            return null;
        } else if (FORMAT_VERSION.compareTo(_version) < 0) {
            System.err.println("le fichier est incorrect (n� version impossible) : ");
            System.err.println(" Version fichier Etude hydraulique1d : " + _version);
            System.err.println(" Version actuel  Etude hydraulique1d : " + FORMAT_VERSION);
            System.err.println("Contacter le service de maintenance.");
            return null;
        }
        BoonyXmlDeserializer fluxLu = null;
        if (compresse) fluxLu = new BoonyXgzDeserializer(); else fluxLu = new BoonyXmlDeserializer();
        try {
            fluxLu.open(_ins);
            if (FORMAT_VERSION.compareTo(_version) == 0) {
                System.out.println("=> Format " + _version);
                fluxLu.setHandler(new BoonyDeserializerHandler(true));
                Object[] infos = (Object[]) fluxLu.read();
                etude = (MetierEtude1d) infos[1];
            } else if (FORMAT_VERSION_0_4.compareTo(_version) == 0) {
                System.out.println("=> Conversion format " + _version);
                fluxLu.setHandler(new DeserializerHandlerVersion04(true));
                Object ret = fluxLu.read();
                if (ret instanceof MetierEtude1d) {
                    etude = (MetierEtude1d) ret;
                    etude.versionFormat(FORMAT_VERSION);
                }
            } else if (FORMAT_VERSION_0_5.compareTo(_version) == 0) {
                System.out.println("=> Conversion format " + _version);
                fluxLu.setHandler(new DeserializerHandlerVersion05(true));
                Object[] infos = (Object[]) fluxLu.read();
                etude = (MetierEtude1d) infos[1];
                etude.versionFormat(FORMAT_VERSION);
            }
            if (etude == null) return etude;
            SMetierIdentifieur[] ids = etude.identifieurs();
            Hashtable table = new Hashtable();
            for (int i = 0; i < ids.length; i++) {
                table.put(ids[i].nom, new Integer(ids[i].numero));
            }
            Identifieur.IDENTIFIEUR.table_ = table;
            etude.reseau().initIndiceNumero();
            etude.donneesHydro().initIndiceLois();
        } catch (Exception ex) {
            System.err.println("$$$ " + ex);
            ex.printStackTrace();
        }
        return etude;
    }
}
