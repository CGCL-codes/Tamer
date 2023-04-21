package org.freedom.library.business.component;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Timer;
import org.freedom.acao.Processo;

public class ProcessoSec implements Processo {

    private Thread th = null;

    private Timer tim = null;

    private Processo pPros = this;

    private Processo pTimer = this;

    public ProcessoSec(int iTempo, Processo pTim, Processo proc) {
        pPros = proc;
        pTimer = pTim;
        tim = new Timer(iTempo, new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                pTimer.run();
            }
        });
        th = new Thread(proc);
    }

    public void iniciar() {
        if (th == null) th = new Thread(pPros);
        th.start();
        tim.start();
    }

    public void parar() {
        th.interrupt();
        th = null;
        tim.stop();
    }

    public int getTempo() {
        return tim.getDelay();
    }

    public void run() {
    }
}
