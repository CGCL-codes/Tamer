package uk.org.sgj.OHCApparatus.Names;

import java.util.*;

public final class CanonicalNamesEditorsAsAuthors extends CanonicalNamesEditorsAbstract {

    public CanonicalNamesEditorsAsAuthors(String s) {
        super(s);
    }

    private String getEditorBiblio() {
        String ret = new String();
        if ((CNs != null) && (numberOfNames > 0) && !CNs.isEmpty()) {
            Iterator<CNAbstract> list = CNs.iterator();
            switch(numberOfNames) {
                case 1:
                    {
                        ret = list.next().getBiblio();
                        break;
                    }
                default:
                    {
                        ret = list.next().getBiblio();
                        for (int ii = 2; ii < numberOfNames; ii++) {
                            ret = ret.concat(", ");
                            ret = ret.concat(list.next().getFirstRef());
                        }
                        if (numberOfNames > 2) {
                            ret = ret.concat(",");
                        }
                        ret = ret.concat(" and ");
                        ret = ret.concat(list.next().getFirstRef());
                        break;
                    }
            }
        }
        return (ret);
    }

    @Override
    public final String getNamesBiblio() {
        return (getEditorBiblio() + getEds() + " ");
    }

    private String getEds() {
        String eds = "";
        if (numberOfNames > 1) {
            eds = ", eds.";
        } else {
            eds = ", ed.";
        }
        return (eds);
    }

    @Override
    public final String getNamesFirstRef() {
        return (getVanillaNamesFirstRef() + getEds());
    }

    @Override
    public final String getNamesSubsRefs() {
        return (getVanillaNamesSubsRefs());
    }

    public final String getNamesSubsRefsMultiVol() {
        return (getVanillaNamesSubsRefs() + getEds());
    }
}
