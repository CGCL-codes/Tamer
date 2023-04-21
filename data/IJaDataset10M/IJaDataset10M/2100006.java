package net.sourceforge.ondex.core.sql;

import net.sourceforge.ondex.core.EvidenceType;

public class SQLEvidenceType extends SQLMetaData implements EvidenceType {

    public SQLEvidenceType(int sid, String id, SQLGraph c) {
        super(sid, "evidenceType", id, c);
    }
}
