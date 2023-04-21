package model.data;

import java.io.File;

public class TitrePerso extends Titre {

    File file;

    public TitrePerso(Integer id, String nom, Integer duree, Integer annee, File file) {
        super(id, nom, duree, annee);
        this.file = file;
    }

    public File getFile() {
        return file;
    }
}
