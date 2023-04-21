package org.fudaa.dodico.crue.metier.emh;

import org.fudaa.dodico.crue.io.common.CrueFileType;

public class CommentaireItem implements ObjetWithID {

    /**
   * @param fileType
   * @param commentaire
   */
    public CommentaireItem(CrueFileType fileType, String commentaire) {
        super();
        this.fileType = fileType;
        this.commentaire = commentaire;
    }

    private final CrueFileType fileType;

    private final String commentaire;

    public CrueFileType getFileType() {
        return fileType;
    }

    public String getId() {
        return fileType.name();
    }

    public String getNom() {
        return getId();
    }

    public String getCommentaire() {
        return commentaire;
    }
}
