package Composite;

import Visitables.*;
import java.awt.Image;
import java.awt.datatransfer.*;
import java.io.*;
import java.util.*;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import support.*;

public abstract class Element extends Observable implements Serializable, Visitable, Transferable {

    private File fichierCourant;

    private String nom;

    private Element parent;

    public static final DataFlavor INFO_FLAVOR = new DataFlavor(Element.class, "Element Information");

    static DataFlavor flavors[] = { INFO_FLAVOR };

    private String type;

    private ArrayList tags;

    private boolean estDropable;

    public static final ImageIcon album = new ImageIcon("albums.png");

    public static final ImageIcon dossier = new ImageIcon("dossier.png");

    public static final ImageIcon pdt = new ImageIcon("pdt.png");

    public static final ImageIcon lecteur = new ImageIcon("lecteur.png");

    public static final ImageIcon png = new ImageIcon("png.png");

    public static final ImageIcon bmp = new ImageIcon("bmp.png");

    public static final ImageIcon jpeg = new ImageIcon("jpeg.png");

    public static final ImageIcon gif = new ImageIcon("gif.png");

    public static final ImageIcon flickr = new ImageIcon("flickr.png");

    public static final ImageIcon recherche = new ImageIcon("recherche.png");

    public static final ImageIcon mesAlbums = new ImageIcon("mesAlbums.png");

    public Element(Element pere, File courant, String nom) {
        this.tags = new ArrayList();
        this.nom = nom;
        this.fichierCourant = courant;
        this.parent = pere;
        this.type = "";
        this.estDropable = true;
    }

    public abstract ImageIcon getRepresentation();

    public abstract Image getVignette();

    public abstract boolean allowsChildren();

    public abstract boolean estUnePhoto();

    public boolean addTag(String tag) {
        return this.tags.add(tag);
    }

    public boolean removeTag(String tag) {
        return this.tags.remove(tag);
    }

    public boolean hasThisTag(String tag) {
        Iterator it = this.tags.iterator();
        while (it.hasNext()) {
            String suiv = (String) it.next();
            if (suiv.equalsIgnoreCase(tag)) return true;
        }
        return false;
    }

    public void setDropable(boolean estDropable) {
        this.estDropable = estDropable;
    }

    public Element getSousElement(String nomDossier) {
        throw new UnsupportedOperationException();
    }

    public boolean isDropable() {
        return this.estDropable;
    }

    public ArrayList getTags() {
        return this.tags;
    }

    public Image getImage() {
        throw new UnsupportedOperationException();
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExtension() {
        return "";
    }

    public int getChildCount() {
        throw new UnsupportedOperationException();
    }

    public boolean addChild(Element e) {
        throw new UnsupportedOperationException();
    }

    public boolean removeChild(Element e) {
        throw new UnsupportedOperationException();
    }

    public void removeChilds() {
        throw new UnsupportedOperationException();
    }

    public Element getChild(int index) {
        throw new UnsupportedOperationException();
    }

    public boolean addChild(Element e, int index) {
        throw new UnsupportedOperationException();
    }

    public int getIndex(Element e) {
        throw new UnsupportedOperationException();
    }

    public File getFichierCourant() {
        return this.fichierCourant;
    }

    public void setFichierCourant(File f) {
        this.fichierCourant = f;
    }

    public Element getPere() {
        return this.parent;
    }

    public void setPere(Element e) {
        this.parent = e;
    }

    public boolean hasPere() {
        if (this.parent == null) return false; else return true;
    }

    public boolean estUnNomValide(String nom) {
        throw new UnsupportedOperationException();
    }

    public String toString() {
        return this.nom;
    }

    public String toStringReduit() {
        if (this.nom.length() > 14) {
            return new String(this.nom.substring(0, 14) + "...");
        } else {
            return this.nom;
        }
    }

    public void setNom(String nom) {
        this.nom = nom;
        this.setChanged();
        this.notifyObservers();
        this.clearChanged();
    }

    public Element getElement(String path) {
        return this.recupererFichierRecursive(path, null);
    }

    protected abstract Element recupererFichierRecursive(String path, Element retour);

    public boolean isDataFlavorSupported(DataFlavor df) {
        return df.equals(INFO_FLAVOR);
    }

    /** implements Transferable interface */
    public Object getTransferData(DataFlavor df) throws UnsupportedFlavorException, IOException {
        if (df.equals(INFO_FLAVOR)) {
            return this.getFichierCourant().getAbsolutePath();
        } else throw new UnsupportedFlavorException(df);
    }

    /** implements Transferable interface */
    public DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }
}
