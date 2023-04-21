package br.ufes.xpflow.core.async.engine.memory;

import br.ufes.xpflow.core.main.Logger;
import br.ufes.xpflow.core.main.XPFlow;
import br.ufes.xpflow.core.main.XPFlowException;
import br.ufes.xpflow.core.flow.execution.ExecutionContext;

/**
 * Created by IntelliJ IDEA.
 * User: Welton
 * Date: 25/07/2007
 * Time: 17:22:29
 * Daemon thread que fica observando
 * uma fila especifica
 */
public class Daemon extends Thread {

    private final Queue queue;

    private final Logger logger;

    private final XPFlow xpFlow;

    public Daemon(Queue queue) throws XPFlowException {
        assert queue != null;
        this.xpFlow = XPFlow.instance();
        this.queue = queue;
        this.setDaemon(true);
        logger = XPFlow.instance().newLogger(this.getClass().getName() + "(" + queue.getId() + ")");
    }

    public void run() {
        logger.info("Daemon para a fila " + queue.getId() + " foi iniciado.");
        try {
            ExecutionContext ctxt = queue.take();
            xpFlow.invoke(ctxt);
        } catch (InterruptedException e) {
        } catch (Exception e) {
            logger.error("Erro ao tentar processar mensagens da fila: " + queue.getId(), e);
        }
    }

    public void interrupt() {
        logger.info("Daemon para a fila " + queue.getId() + " sendo interrompido.");
        super.interrupt();
    }
}
