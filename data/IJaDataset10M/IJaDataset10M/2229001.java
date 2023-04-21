package in.espirit.tracer.ext;

import in.espirit.tracer.database.dao.TicketDao;
import in.espirit.tracer.model.Requirement;
import java.util.Collection;
import java.util.Locale;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

public class RequirementTypeConverter implements TypeConverter<Requirement> {

    @Override
    public Requirement convert(String url, Class<? extends Requirement> arg1, Collection<ValidationError> arg2) {
        if (url.equalsIgnoreCase("new")) {
            Requirement ticket = new Requirement();
            ticket.setType("requirement");
            return ticket;
        } else {
            try {
                return (Requirement) TicketDao.getTicket("requirement", url);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public void setLocale(Locale arg0) {
    }
}
