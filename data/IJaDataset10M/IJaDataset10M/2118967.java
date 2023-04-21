package de.fau.cs.dosis.acceptance;

import java.io.IOException;
import java.net.MalformedURLException;
import org.restlet.Client;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Protocol;
import org.restlet.engine.Engine;
import org.restlet.ext.jackson.JacksonConverter;
import org.restlet.ext.json.JsonConverter;
import org.restlet.resource.ClientResource;
import utils.ServerConfiguration;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import de.fau.cs.dosis.dto.DrugDto;
import de.fau.cs.dosis.dto.DrugRetrieveSet;
import de.fau.cs.dosis.model.PersonalNote;
import de.fau.cs.dosis.restlet.DrugIdsResource;
import de.fau.cs.dosis.restlet.DrugListResource;

public class RestletBot {

    private static final String RESTLET_BASE = "restapi/";

    public static final String DRUG_LIST_RESOURCE = "update/drug/";

    public static final String DRUG_IDS_RESOURCE = "update/drugIds";

    private Engine engine;

    private ServerConfiguration config;

    private String user;

    private String password;

    public RestletBot(ServerConfiguration config, String user, String password) {
        this.config = config;
        this.user = user;
        this.password = password;
        engine = Engine.getInstance();
        engine.getRegisteredClients().add(new org.restlet.engine.http.connector.HttpClientHelper(new Client(Protocol.HTTP)));
        engine.getRegisteredConverters().add(new JacksonConverter());
        engine.getRegisteredConverters().add(new JsonConverter());
        Engine.setInstance(engine);
    }

    public <T> T getClientResource(String uri, Class<T> class_) {
        String finalUri = config.getBaseUrl() + RESTLET_BASE + uri;
        ClientResource resourceProxy = new ClientResource(finalUri);
        ChallengeResponse cr = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, user, password);
        resourceProxy.setChallengeResponse(cr);
        T resource = resourceProxy.wrap(class_);
        return resource;
    }

    private String join(int[] numbers, String split) {
        StringBuilder builder = new StringBuilder();
        for (int n : numbers) {
            builder.append(split);
            builder.append(n);
        }
        if (numbers.length > 0) builder.replace(0, split.length(), "");
        return builder.toString();
    }

    public int[] getDrugIds() {
        return getClientResource(DRUG_IDS_RESOURCE, DrugIdsResource.class).getAll();
    }

    public DrugRetrieveSet getDrugList(int[] ids) {
        return getClientResource(DRUG_LIST_RESOURCE + join(ids, "-"), DrugListResource.class).retrieve();
    }

    public PersonalNote getPersonalNote(String activeIngredient) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
        return null;
    }

    public String joinIds(DrugDto[] dto) {
        StringBuilder builder = new StringBuilder();
        for (DrugDto n : dto) {
            builder.append("-");
            builder.append(n.getId());
        }
        if (dto.length > 0) builder.replace(0, 1, "");
        return builder.toString();
    }
}
