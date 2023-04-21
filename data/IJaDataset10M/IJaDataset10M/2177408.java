package org.nextframework.progress;

import org.nextframework.core.standard.Next;

/**
 * Classe para criar trabalhos com ProgressMonitors
 * 
 * @author rogel
 * @since 3.5.3
 */
public class ProgressTaskFactory {

    /**
	 * Executa a tarefa definida por ProgressTask em uma outra thread e retorna o monitor de progresso relacionado (ser� criado um monitor padr�o)
	 * @param task
	 * @return
	 */
    public static IProgressMonitor startTask(final ProgressTask task) {
        return startTask(new ProgressMonitor(), task);
    }

    /**
	 * Executa a tarefa definida por ProgressTask em uma outra thread.
	 * O monitor usado deve ser passado como par�metro. 
	 * @param task
	 * @return
	 */
    public static IProgressMonitor startTask(final IProgressMonitor monitor, final ProgressTask task) {
        new Thread() {

            public void run() {
                monitor.subTask("Inicializando trabalho...");
                try {
                    task.run(monitor);
                    if (monitor.getError() == null) {
                        monitor.done();
                    }
                } catch (Exception e) {
                    monitor.setError("Erro: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            ;
        }.start();
        return monitor;
    }

    /**
	 * Executa uma tarefa e salva o progressMonitor no escopo de requisi��o com o nome informado
	 * @param requestAttribute
	 * @param task
	 * @return
	 */
    public static IProgressMonitor startTask(String requestAttribute, final ProgressTask task) {
        IProgressMonitor pm = startTask(task);
        Next.getRequestContext().setAttribute(requestAttribute, pm);
        return pm;
    }
}
