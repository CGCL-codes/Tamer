package net.sf.brightside.dentalwizard.metamodel;

import java.util.List;
import net.sf.brightside.dentalwizard.core.beans.Identifiable;
import net.sf.brightside.dentalwizard.datatype.Money;

public interface DentalService extends Identifiable {

    public String getName();

    public void setName(String name);

    public String getDescription();

    public void setDescription(String description);

    public Money getCurrentPrice();

    public void setCurrentPrice(Money currentPrice);

    public boolean isActive();

    public void setActive(boolean active);

    public List<Appointment> getAppointments();

    public List<Remedy> getRemedies();

    public void addAppointment(Appointment appointemnt);

    public void removeAppointment(Appointment appointemnt);

    public void addRemedy(Remedy remedy);

    public void removeRemedy(Remedy remedy);

    public String descToolTip();
}
