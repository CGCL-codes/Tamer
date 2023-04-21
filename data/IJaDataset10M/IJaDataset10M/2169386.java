package org.jcompany.control.entity;

/**
 * POJO que representa um bot�o de destaque para home (quiosque)
 */
public class PlcFeatured {

    private String url;

    private String urlImage;

    private String textoI18n;

    private Boolean indUrlAbsolute = false;

    private Boolean indI18n = true;

    private Boolean indDesconect = false;

    private Boolean indHome = false;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        if (url.startsWith("http") || url.startsWith("www")) indUrlAbsolute = true;
        if (url.equals("desconectar")) indDesconect = true;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        if (urlImage.equals("/f/t/inicial")) indHome = true;
        this.urlImage = urlImage;
    }

    public String getTextoI18n() {
        return textoI18n;
    }

    public void setTextoI18n(String textoI18n) {
        if (textoI18n.startsWith("#")) {
            this.textoI18n = textoI18n.substring(1);
            indI18n = false;
        } else this.textoI18n = textoI18n;
    }

    public Boolean getIndUrlAbsolute() {
        return indUrlAbsolute;
    }

    public Boolean getIndI18n() {
        return indI18n;
    }

    public Boolean getIndDesconect() {
        return indDesconect;
    }

    public Boolean getIndHome() {
        return indHome;
    }
}
