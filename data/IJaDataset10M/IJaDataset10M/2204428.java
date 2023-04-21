package org.gdbi.db.genjdb;

import org.gdbi.api.*;
import genj.gedcom.Repository;

/**
 * Implementation of GdbiIntrRepo.
 */
public class GenjdbRepo extends GenjdbEntity implements GdbiIntrRepo {

    private GenjdbRepo(Repository repo) {
        super(repo);
    }

    public static GenjdbRepo newGenjdbRepo(Repository repo) {
        if (repo == null) return null;
        return new GenjdbRepo(repo);
    }

    public static GdbiRepo newGdbiRepo(Repository repo) {
        GenjdbRepo grepo = newGenjdbRepo(repo);
        if (grepo == null) return null;
        return new GdbiRepo(grepo);
    }
}
