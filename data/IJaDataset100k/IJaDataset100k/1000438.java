package com.increg.game.client;

/**
 * @author Manu
 *
 * Classe d�finissant un smiley
 */
public class Smiley extends PerfoMeter {

    /**
     * Code du smiley
     */
    protected String code;

    /**
     * Image pour les machines ayant des performances suffisantes
     */
    protected String imageHautePerf;

    /**
     * Images pour les machines ayant de mauvaises performances
     */
    protected String imageFaiblePerf;

    /**
     * Constructeur
     * @param aCode Code du smiley
     * @param aImageHaute Image pour les bonnes machines
     * @param aImageBasse Image pour les autres machines
     */
    public Smiley(String aCode, String aImageHaute, String aImageBasse) {
        super();
        code = aCode;
        imageHautePerf = aImageHaute;
        imageFaiblePerf = aImageBasse;
    }

    /**
     * @return Code du smiley
     */
    public String getCode() {
        return code;
    }

    /**
     * @return Image pour les machines ayant des performances suffisantes
     */
    public String getImageFaiblePerf() {
        return imageFaiblePerf;
    }

    /**
     * @return Images pour les machines ayant de mauvaises performances
     */
    public String getImageHautePerf() {
        return imageHautePerf;
    }

    /**
     * @param string Code du smiley
     */
    public void setCode(String string) {
        code = string;
    }

    /**
     * @param url Image pour les machines ayant des performances suffisantes
     */
    public void setImageFaiblePerf(String url) {
        imageFaiblePerf = url;
    }

    /**
     * @param url Images pour les machines ayant de mauvaises performances
     */
    public void setImageHautePerf(String url) {
        imageHautePerf = url;
    }

    /**
     * Donne l'image en fonction de la performance de la machine
     * @return image
     */
    public String getImage() {
        if (isHautePerf()) {
            return getImageHautePerf();
        } else {
            return getImageFaiblePerf();
        }
    }
}
