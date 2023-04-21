package gui.actions;

import com.google.inject.Inject;
import gui.ContinueDialog;
import gui.Controller;
import gui.ErrorDialog;
import gui.TourneyManView;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.widgets.Shell;
import tournamentmanager.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TournamentLauncher
 * <p/>
 *
 * @author Kunnar Klauks
 */
public class TournamentLauncher {

    private final Controller controller;

    private final TourneyManView view;

    private final Preferences model;

    private final RunTournamentButton runTournamentButton;

    private final ShellClosed shellClosed;

    private final TournamentObserver tournamentObserver;

    private final MatchRunner matchRunner;

    private Thread trnThread;

    @Inject
    public TournamentLauncher(Controller controller, TourneyManView view, Preferences model) {
        this.controller = controller;
        this.view = view;
        this.model = model;
        this.tournamentObserver = new TournamentObserverImpl();
        this.runTournamentButton = new RunTournamentButton();
        this.matchRunner = new XboardMatchRunner();
        this.shellClosed = new ShellClosed();
    }

    private class RunTournamentButton extends SelectionAdapter {

        public void widgetSelected(SelectionEvent e) {
            try {
                controller.saveModelFromView();
            } catch (IOException e1) {
                ErrorDialog.showError("saving failed", view.getShell());
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "saving failed", e1);
                return;
            }
            if (trnThread != null && trnThread.isAlive()) {
                trnThread.interrupt();
                view.btnRunTrn.setText("Run");
            } else {
                view.btnRunTrn.setText("Stop");
                Tournament trn = model.getCurrentTournament();
                ContinueDialog continueDialog = new ContinueDialog(view.getShell());
                if (trn.isEnded() || (trn.isInProgress() && !(continueDialog.doContinue()))) {
                    trn.resetGames();
                    controller.loadViewFromModel();
                }
                trnThread = new Thread(new TournamentRunner());
                trnThread.start();
            }
        }
    }

    private class ShellClosed extends ShellAdapter {

        public void shellClosed(org.eclipse.swt.events.ShellEvent e) {
            if (trnThread != null && trnThread.isAlive()) {
                trnThread.interrupt();
            }
        }
    }

    public class TournamentRunner implements Runnable {

        public void run() {
            try {
                model.getCurrentTournament().setMatchRunner(matchRunner);
                model.getCurrentTournament().registerObserver(tournamentObserver);
                model.getCurrentTournament().run();
            } catch (RuntimeException e) {
                if (!view.getShell().isDisposed()) {
                    ErrorDialog.showError(e.getMessage(), view.getShell());
                }
            } finally {
                if (!view.getShell().isDisposed()) {
                    view.getShell().getDisplay().asyncExec(new Runnable() {

                        public void run() {
                            view.btnRunTrn.setText("Run");
                            try {
                                model.save();
                            } catch (IOException e) {
                                ErrorDialog.showError("saving failed", view.getShell());
                                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "saving failed", e);
                            }
                        }
                    });
                }
            }
        }
    }

    public class TournamentObserverImpl implements TournamentObserver {

        public void update(Tournament tournament) {
            Shell shell = view.getShell();
            if (!shell.isDisposed()) {
                shell.getDisplay().asyncExec(new Runnable() {

                    public void run() {
                        controller.loadViewFromModel();
                    }
                });
            }
        }
    }

    public SelectionAdapter getRunTournamentButton() {
        return runTournamentButton;
    }

    public ShellAdapter getShellClosedListener() {
        return shellClosed;
    }
}
