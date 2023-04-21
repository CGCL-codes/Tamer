package de.psisystems.dmachinery.jobs.adapter;

import java.net.URL;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import de.psisystems.dmachinery.jobs.SimpleTemplate;
import de.psisystems.dmachinery.jobs.Template;

public class TemplateAdapter extends XmlAdapter<String[], Template> {

    @Override
    public String[] marshal(Template arg0) throws Exception {
        return new String[] { arg0.getUrl().toExternalForm() };
    }

    @Override
    public Template unmarshal(String[] blockData) throws Exception {
        return new SimpleTemplate(new URL(blockData[0]));
    }
}
