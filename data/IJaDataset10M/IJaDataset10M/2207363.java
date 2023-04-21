package fr.graphit.web.client;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.sitebricks.client.transport.Text;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.http.Get;

/**
 * A service to display current configuration
 * This service is only for developement and not activated in WebModule
 * @author Thomas Clarisse
 *
 */
public class DisplayConfigService {

    private String databaseUrl;

    @Inject
    public DisplayConfigService(@Named("host") String host, @Named("user") String user, @Named("password") String password, @Named("port") String port, @Named("schema") String schema) {
        this.databaseUrl = "jdbc:mysql://" + host + ":" + port + "/" + schema + "?user=" + user + "&password=" + password + "&useEncoding=true&characterEncoding=UTF-8";
    }

    @Get
    public Reply<String> get() {
        return Reply.with(this.databaseUrl).as(Text.class).type("text/html; charset=utf-8");
    }
}
