package sf.qof.mapping;

import sf.qof.mapping.AbstractDateTimeMapping.DateMapping;
import sf.qof.mapping.AbstractDateTimeMapping.TimeMapping;
import sf.qof.mapping.AbstractDateTimeMapping.TimestampMapping;

public interface DateTimeMappingVisitor {

    void visit(Mapper mapper, DateMapping mapping);

    void visit(Mapper mapper, TimeMapping mapping);

    void visit(Mapper mapper, TimestampMapping mapping);
}
