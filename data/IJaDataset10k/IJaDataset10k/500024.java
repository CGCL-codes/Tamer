package glaureano.uteis.xml;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public abstract class DateAdapter extends XmlAdapter<String, Date> {

    private SimpleDateFormat sdf;

    public DateAdapter() {
        sdf = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    public String marshal(Date date) throws Exception {
        return sdf.format(date);
    }

    @Override
    public Date unmarshal(String string) throws Exception {
        return sdf.parse(string);
    }
}
