package de.lema.filter;

import org.apache.log4j.spi.LoggingEvent;
import com.google.inject.Inject;
import de.lema.bo.LogEventString;
import de.lema.bo.idtext.IdTextEnum;
import de.lema.config.TypeMappingRules;

public class EventTunerExtractType implements IEventTuner {

    private final TypeMappingRules rules;

    @Inject
    public EventTunerExtractType(TypeMappingRules rules) {
        this.rules = rules;
    }

    public LogEventString tune(LoggingEvent event, LogEventString pe) {
        if (pe.get(IdTextEnum.Klassifizierung) != null) {
            return pe;
        } else {
            String klassifizierung = rules.getKlassifizierung(pe);
            pe.setKlassifizierung(klassifizierung);
            return pe;
        }
    }
}
