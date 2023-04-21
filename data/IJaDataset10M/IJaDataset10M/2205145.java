package progranet.ganesa.metamodel;

import java.io.Serializable;
import java.util.Date;
import progranet.omg.core.types.Type;
import progranet.ganesa.model.ElementImpl;
import progranet.ganesa.model.Ganesa;

public class CalendarImpl extends ElementImpl implements Serializable, Calendar {

    private static final long serialVersionUID = -5494835416063798377L;

    public CalendarImpl() {
        super();
    }

    public Type getClassifier() {
        return Ganesa.CALENDAR;
    }

    public Date getDate() {
        return (Date) this.get(Ganesa.CALENDAR_DATE);
    }

    public String getName() {
        return this.getDate().toString();
    }

    public String getQualifiedName() {
        return this.getName();
    }
}
