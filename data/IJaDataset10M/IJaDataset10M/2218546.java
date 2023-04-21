package saadadb.vo.request.formator.votable;

import cds.savot.model.SavotField;

public class VOTableField extends SavotField {

    private int saadaType;

    public static final int T_USER_DEFINED = 1, T_CLASS = 2, T_SAADA = 3;

    public VOTableField(int saadaType) {
        super();
        this.saadaType = saadaType;
    }

    public int getSaadaType() {
        return saadaType;
    }
}
