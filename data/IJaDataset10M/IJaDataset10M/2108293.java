package org.arastreju.api.terminology;

import org.arastreju.api.terminology.attributes.AdjectiveComparison;
import org.arastreju.api.terminology.attributes.Casus;
import org.arastreju.api.terminology.attributes.Flavor;
import org.arastreju.api.terminology.attributes.Genus;
import org.arastreju.api.terminology.attributes.Modus;
import org.arastreju.api.terminology.attributes.Numerus;
import org.arastreju.api.terminology.attributes.Person;
import org.arastreju.api.terminology.attributes.Tempus;

/**
 * representation of a flected word, i.e. a special form of a word definition.
 * 
 * Created: 11.07.2008 
 *
 * @author Oliver Tigges
 */
public interface FlectedWord {

    public abstract long getId();

    public abstract WordDefinition getDefinition();

    public abstract String getName();

    public abstract void setName(String name);

    public abstract Numerus getNumerus();

    public abstract void setNumerus(Numerus numerus);

    public abstract Casus getCasus();

    public abstract void setCasus(Casus kasus);

    public abstract Genus getGenus();

    public abstract void setGenus(Genus genus);

    public abstract Person getPerson();

    public abstract void setPerson(Person person);

    public abstract Tempus getTempus();

    public abstract void setTempus(Tempus tempus);

    public abstract Modus getModus();

    public abstract void setModus(Modus modus);

    public abstract Casus getRequiredKasus();

    public abstract void setRequiredCasus(Casus requiredKasus);

    public abstract AdjectiveComparison getAdjectiveComparision();

    public abstract void setAdjectiveComparison(AdjectiveComparison komparation);

    public abstract Flavor getFlavor();

    public abstract void setFlavor(Flavor flavor);
}
