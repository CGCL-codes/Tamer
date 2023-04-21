package com.umc.beans;

import java.util.Collection;

/**
 * Diese Klasse dient als reiner Datencontainer für die Abbildung einer Filmfassung.
 * Über Getter/Setter-Methoden kann auf die Attribute zugegriffen werden.
 * 
 * @author DonGyros
 * 
 * @version 0.1 29.03.2009
 * 
 */
public class MovieVersion {

    /**Version name*/
    private String name = null;

    /**The country in which this version was released*/
    private String country = null;

    /**The online movie database id*/
    private String id = null;

    private String title = null;

    private String type = null;

    private String lable = null;

    private String released = null;

    private String eanupc = null;

    private ImageFile[] covers = null;

    /**Freigabe*/
    private String passing = null;

    /**indiziert*/
    private boolean indicated = false;

    private String runnungtime = null;

    private String regioncode = null;

    private String dvdformat = null;

    private String tvnorm = null;

    /**Verpakung*/
    private String packaging = null;

    /**Bildformat*/
    private String format = null;

    private AudioTrack[] audioTracks = null;

    private Collection<String> subtitles = null;

    /**Hier können sonstige Extras eingetragen werden*/
    private String extras = null;

    private String comments = null;
}
