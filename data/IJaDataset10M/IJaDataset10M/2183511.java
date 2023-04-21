package net.taylor.fitnesse.samples;

import net.taylor.fitnesse.EnumConverter;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import fitnesse.slim.Slim;

@Name("qualityConverter")
@Scope(ScopeType.APPLICATION)
@Startup
public class QualityConverter extends EnumConverter<Quality> {

    static {
        Slim.addConverter(Quality.class, new QualityConverter());
    }
}
