package business.dados;

import org.hibernate.classic.Session;

public interface IBancoConnection {

    public Session factorySession() throws Exception;
}
