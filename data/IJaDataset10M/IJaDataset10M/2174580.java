package net.sf.brightside.instantevents.tapestry.util;

import net.sf.brightside.instantevents.metamodel.City;
import net.sf.brightside.instantevents.service.queries.GetByIdCommand;
import org.apache.tapestry.ValueEncoder;

public class CityEncoderImpl implements ValueEncoder<Object>, CityEncoder {

    private GetByIdCommand<City> getByIdCommand;

    @Override
    public String toClient(Object object) {
        String str = "" + ((City) object).takeId();
        return str;
    }

    public GetByIdCommand<City> getGetByIdCommand() {
        return getByIdCommand;
    }

    public void setGetByIdCommand(GetByIdCommand<City> getByIdCommand) {
        this.getByIdCommand = getByIdCommand;
    }

    @Override
    public Object toValue(String string) {
        getByIdCommand.setType(City.class);
        try {
            getByIdCommand.setId(Long.parseLong(string));
            City city = (City) getByIdCommand.execute();
            return city;
        } catch (Exception e) {
            return null;
        }
    }
}
