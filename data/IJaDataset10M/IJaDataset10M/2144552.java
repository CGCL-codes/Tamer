package net.cepra.timecard.common.meta;

import java.util.Date;
import net.cepra.core.domain.Audit;
import com.gwtaf.common.meta.DateFieldInfo;
import com.gwtaf.common.meta.IDateFieldInfo;
import com.gwtaf.common.meta.IStringFieldInfo;
import com.gwtaf.common.meta.MetaClass;
import com.gwtaf.common.meta.StringFieldInfo;

public class AuditMeta extends MetaClass {

    public static final AuditMeta meta = new AuditMeta();

    private AuditMeta() {
        add(modifiedBy);
        add(modifiedAt);
        add(createdBy);
        add(createdAt);
    }

    public final IDateFieldInfo<Audit> createdAt = new DateFieldInfo<Audit>("Erstellt am") {

        public Date getValue(Audit model) {
            return model.getCreatedAt();
        }

        public void setValue(Audit model, Date value) {
        }
    }.setReadOnly(true);

    public final IStringFieldInfo<Audit> createdBy = new StringFieldInfo<Audit>("Erstellt von") {

        public String getValue(Audit model) {
            return model.getCreatedBy();
        }

        public void setValue(Audit model, String value) {
        }
    }.setReadOnly(true);

    public final IDateFieldInfo<Audit> modifiedAt = new DateFieldInfo<Audit>("Geaendert am") {

        public Date getValue(Audit model) {
            return model.getModifiedAt();
        }

        public void setValue(Audit model, Date value) {
        }
    }.setReadOnly(true);

    public final IStringFieldInfo<Audit> modifiedBy = new StringFieldInfo<Audit>("Geaendert von") {

        public String getValue(Audit model) {
            return model.getModifiedBy();
        }

        public void setValue(Audit model, String value) {
        }
    }.setReadOnly(true);
}
