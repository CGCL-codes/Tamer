package org.enhydra.shark;

import java.util.Properties;
import javax.transaction.Status;
import javax.transaction.UserTransaction;
import manager.Configuracao;
import org.enhydra.shark.api.client.wfservice.SharkConnection;

/**
 * Classe abstrata com metodos gerais para o controle
 * do sistema sobre o motor, simboliza as operações que
 * as interfaces do modelo da WfMC possuem em comum
 * @author luciano
 */
public abstract class WfMCInterface {

    /**
     * Verifica de o usuario está autorizado a acessar o sistema
     * @throws notLoggedException
     */
    protected void checkLogged() throws notLoggedException {
        if (logged == false) {
            throw new notLoggedException();
        }
    }

    /**
     * Verifica se o shark está instânciado
     * Se estiver não faz nada
     * se não estiver o configura e cria a transação
     */
    protected void checkSharkConfigured() throws Exception {
        if (!Shark.isConfigured()) {
            EngineStarter.initContext();
            Configuracao confSistema = new Configuracao(Configuracao.getConfiguracaoDoSistema());
            String arquivoConfEngine = confSistema.getProperty("engine.confFile");
            Configuracao confMotor = new Configuracao(arquivoConfEngine);
            Properties todasConfiguracoes = Configuracao.concatena(confSistema, confMotor);
            try {
                ut = EngineStarter.getUserTransaction();
                ut.setTransactionTimeout(15 * 60);
                ut.begin();
                Shark.configure(todasConfiguracoes);
                ut.commit();
            } catch (Throwable ex) {
                try {
                    if (ut.getStatus() != Status.STATUS_NO_TRANSACTION) {
                        ut.rollback();
                    }
                } catch (Exception _) {
                }
                ex.printStackTrace();
                System.exit(1);
            }
        }
    }

    public SharkConnection getSc() {
        return sc;
    }

    /**
     * Obtem o objeto responsavel pela manipulação de transaçoes com
     * o sistema, garantindo a integridade dos dados.
     * Se o objeto não existir ele é criado.
     *
     * @return UserTransaction - transação do usuario
     * @throws Exception
     */
    public UserTransaction getUserTransaction() throws Exception {
        return ut;
    }

    protected boolean logged;

    protected static SharkEngineManager engine = SharkEngineManager.getInstance();

    protected SharkConnection sc;

    protected static UserTransaction ut;
}
