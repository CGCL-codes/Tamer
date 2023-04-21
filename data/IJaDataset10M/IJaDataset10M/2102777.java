package jwebdownloader.vista.explorador;

import java.io.File;
import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import jwebdownloader.control.Constantes;

public class ArchivoTreeNode extends DefaultMutableTreeNode implements Comparable<ArchivoTreeNode> {

    public File file;

    private boolean descargado;

    private int nivel;

    private String contentType;

    public ArchivoTreeNode(File file, boolean descargado, int nivel, String contentType) {
        if (file == null) {
            throw new IllegalArgumentException("Null file not allowed");
        }
        this.file = file;
        this.descargado = descargado;
        if (!esDirectorio()) {
            this.nivel = nivel;
            this.contentType = contentType;
        }
    }

    public String toString() {
        String name = file.getName();
        if (!Constantes.isWindows) {
            return name;
        }
        if (name.length() == 0) {
            return file.getPath();
        }
        return name;
    }

    public void insert(final MutableTreeNode newChild, final int childIndex) {
        super.insert(newChild, childIndex);
    }

    public int compareTo(ArchivoTreeNode f2) {
        boolean f1IsDir = this.esDirectorio();
        boolean f2IsDir = f2.esDirectorio();
        if (f1IsDir == f2IsDir) {
            return file.compareTo(f2.file);
        }
        if (f1IsDir && !f2IsDir) {
            return -1;
        }
        return 1;
    }

    public boolean esDirectorio() {
        return file.isDirectory();
    }

    public boolean isDescargado() {
        return descargado;
    }

    public void setDescargado(boolean descargado) {
        this.descargado = descargado;
    }

    public Vector getHijos() {
        return this.children;
    }

    public boolean coincideFile(File archivo) {
        return file.equals(archivo);
    }

    public String getContentType() {
        return contentType;
    }

    public int getNivel() {
        return nivel;
    }
}
