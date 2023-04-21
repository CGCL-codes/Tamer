package com.ordocalendarws.model.objects;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.IdentityType;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class OrdoDayObject {

    @PrimaryKey
    private String _date;

    @Persistent
    private String _feast;

    @Persistent
    private String _feast_class;

    @Persistent
    private String _color;

    @Persistent
    private String _epitre;

    @Persistent
    private String _evangile;

    public String getDate() {
        return _date;
    }

    public String getFeast() {
        return _feast;
    }

    public String getFeastClass() {
        return _feast_class;
    }

    public String getColor() {
        return _color;
    }

    public String getEpitre() {
        return _epitre;
    }

    public String getEvangile() {
        return _evangile;
    }

    /**
	 * Constructeur de la classe
	 * @param date Date du jour
	 * @param feast Titre du jour
	 * @param feast_class Classe du jour
	 * @param color Couleur liturgique
	 * @param epitre R�f�rence du texte de l'Epitre
	 * @param evangile R�f�rence du texte de l'Evangile
	 */
    public OrdoDayObject(String date, String feast, String feast_class, String color, String epitre, String evangile) {
        _date = date;
        _feast = feast;
        _feast_class = feast_class;
        _color = color;
        _epitre = epitre;
        _evangile = evangile;
    }
}
