package org.broadleafcommerce.profile.core.domain.listener;

import org.broadleafcommerce.openadmin.time.SystemTime;
import org.broadleafcommerce.profile.core.domain.annotation.AutoPopulate;
import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

public class TemporalTimestampListener {

    @PrePersist
    @PreUpdate
    public void setTimestamps(Object entity) throws Exception {
        if (entity.getClass().isAnnotationPresent(Entity.class)) {
            Field[] fields = entity.getClass().getDeclaredFields();
            setTimestamps(fields, entity);
        }
    }

    private void setTimestamps(Field[] fields, Object entity) throws Exception {
        Calendar cal = null;
        for (Field field : fields) {
            Class<?> type = field.getType();
            Temporal temporalAnnotation = field.getAnnotation(Temporal.class);
            if (temporalAnnotation != null) {
                if (field.isAnnotationPresent(Column.class)) {
                    field.setAccessible(true);
                    try {
                        if (TemporalType.TIMESTAMP.equals(temporalAnnotation.value()) && (field.isAnnotationPresent(AutoPopulate.class))) {
                            if (field.get(entity) == null || field.getAnnotation(AutoPopulate.class).autoUpdateValue()) {
                                if (type.isAssignableFrom(Date.class)) {
                                    if (cal == null) {
                                        cal = SystemTime.asCalendar();
                                    }
                                    field.set(entity, cal.getTime());
                                } else if (type.isAssignableFrom(Calendar.class)) {
                                    if (cal == null) {
                                        cal = SystemTime.asCalendar();
                                    }
                                    field.set(entity, cal);
                                }
                            }
                        }
                    } finally {
                        field.setAccessible(false);
                    }
                }
            } else if (field.isAnnotationPresent(Embedded.class)) {
                field.setAccessible(true);
                try {
                    setTimestamps(field.getType().getDeclaredFields(), field.get(entity));
                } finally {
                    field.setAccessible(false);
                }
            }
        }
    }
}
