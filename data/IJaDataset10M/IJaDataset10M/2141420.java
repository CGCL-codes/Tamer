package ee.webAppToolkit.json;

import com.google.inject.AbstractModule;
import ee.webAppToolkit.core.expert.ActionArgumentResolver;
import ee.webAppToolkit.json.annotations.Json;
import ee.webAppToolkit.json.expert.impl.JsonActionArgumentResolver;

public class JsonModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ActionArgumentResolver.class).annotatedWith(Json.class).to(JsonActionArgumentResolver.class);
    }
}
