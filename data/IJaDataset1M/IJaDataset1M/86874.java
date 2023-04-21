package br.com.sinapp.tasker;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class DemandaPrazoTask implements ApplicationTask {

    private ProcessoDao dao;

    public DemandaPrazoTask(TaskScheduler scheduler, ProcessoDao dao) {
        this.dao = dao;
        this.schedule(scheduler);
    }

    public void schedule(TaskScheduler scheduler) {
        scheduler.schedule(this, new CronTrigger("0 0 23 * * *"));
    }

    public void run() {
    }
}
