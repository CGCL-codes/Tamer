package br.gov.serpro.bolaum.persistence;

import static br.gov.frameworkdemoiselle.annotation.Startup.MAX_PRIORITY;
import static br.gov.frameworkdemoiselle.annotation.Startup.MIN_PRIORITY;
import javax.enterprise.context.ApplicationScoped;
import org.hsqldb.Server;
import br.gov.frameworkdemoiselle.annotation.Shutdown;
import br.gov.frameworkdemoiselle.annotation.Startup;
import br.gov.serpro.bolaum.domain.Usuario;

@ApplicationScoped
public class DatabaseServer {

    private final Server server;

    public DatabaseServer() {
        this.server = new Server();
        server.setDatabaseName(0, "bolaum");
        server.setDatabasePath(0, "database/bolaum");
        server.setPort(9001);
        server.setSilent(true);
    }

    @Startup(priority = MAX_PRIORITY)
    public void start() {
        this.server.start();
    }

    @Shutdown(priority = MIN_PRIORITY)
    public void stop() {
        this.server.shutdown();
    }
}
