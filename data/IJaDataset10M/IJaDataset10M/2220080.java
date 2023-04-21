package org.gbif.ipt.model.converter;

import org.gbif.ipt.model.Organisation;
import org.gbif.ipt.service.admin.RegistrationManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

@Singleton
public class OrganisationKeyConverter implements Converter {

    private final RegistrationManager registrationManager;

    @Inject
    public OrganisationKeyConverter(RegistrationManager registrationManager) {
        this.registrationManager = registrationManager;
    }

    public boolean canConvert(Class clazz) {
        return clazz.equals(Organisation.class);
    }

    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
        Organisation u = (Organisation) value;
        writer.setValue(u.getKey().toString());
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        return registrationManager.get(reader.getValue());
    }
}
