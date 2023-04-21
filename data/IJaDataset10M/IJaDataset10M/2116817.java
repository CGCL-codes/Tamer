package gov.nih.niaid.bcbb.nexplorer3.server.config;

import gov.nih.niaid.bcbb.nexplorer3.server.datamodel.InputDataInfo;
import gov.nih.niaid.bcbb.nexplorer3.server.datamodels.CDAOHistoryData;
import gov.nih.niaid.bcbb.nexplorer3.server.datamodels.CDAOViewData;
import gov.nih.niaid.bcbb.nexplorer3.server.datamodels.CDAOViewInitData;
import gov.nih.niaid.bcbb.nexplorer3.server.datamodels.NodeObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

@Provider
public final class JAXBContextResolver implements ContextResolver<JAXBContext> {

    private final JAXBContext context;

    private final Set<Class> types;

    private final Class[] cTypes = { CDAOViewData.class, NodeObject.class, InputDataInfo.class, CDAOViewInitData.class, ArrayList.class, CDAOHistoryData.class };

    public JAXBContextResolver() throws Exception {
        this.types = new HashSet(Arrays.asList(cTypes));
        JSONConfiguration.mapped().rootUnwrapping(true);
        JSONConfiguration.mapped().arrays("inputData").build();
        this.context = new JSONJAXBContext(JSONConfiguration.natural().build(), cTypes);
    }

    public JAXBContext getContext(Class<?> objectType) {
        return (types.contains(objectType)) ? context : null;
    }
}
