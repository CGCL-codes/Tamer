package net.sourceforge.ondex.core.sql.metadata;

import net.sourceforge.ondex.core.EvidenceType;
import net.sourceforge.ondex.core.sql.SQLGraph;

public class SQLEvidenceType extends SQLMetaData implements EvidenceType {

    public SQLEvidenceType(int sid, String id, SQLGraph c) {
        super(sid, "evidenceType", id, c);
    }
}
