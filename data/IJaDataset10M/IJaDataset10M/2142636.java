package pf;

import basic.*;

/**
 * Cette classe permet d'initialiser et d'obtenir
 * les information sur l'entete d'un fichier pagine a savoir
 * la premiere page contenant de lespace libre et le nombre de page du fichier.
 * Le bitmap permet de localiser les pages vides et non vide d'un fichier
 */
public class PF_FileHdr {

    private PageNum firstFree;

    private PageNum numPages;

    public PF_FileHdr() {
        firstFree = null;
        numPages = null;
    }

    public PF_FileHdr(PageNum firstFree, PageNum numPages) {
        this.firstFree = firstFree;
        this.numPages = numPages;
    }

    public PageNum getFirstFree() {
        return firstFree;
    }

    public PageNum getNumPages() {
        return numPages;
    }

    public String toString() {
        String ret = "( First free page: ";
        ret += firstFree + ", Number of pages: ";
        ret += numPages;
        ret += ")";
        return ret;
    }

    public void setNumPages(PageNum _numPages) {
        numPages = _numPages;
    }

    public void setFirstFree(PageNum _firstFree) {
        firstFree = _firstFree;
    }
}
