package ch.jester.ui.tournament.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import messages.Messages;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.progress.UIJob;
import ch.jester.common.ui.handlers.AbstractCommandHandler;
import ch.jester.common.ui.utility.UIUtility;
import ch.jester.common.utility.ExceptionUtility;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.Category;
import ch.jester.model.Ranking;
import ch.jester.model.RankingSystem;
import ch.jester.model.Round;
import ch.jester.model.Tournament;
import ch.jester.model.util.RankingReportInput;
import ch.jester.system.api.ranking.IRankingSystem;
import ch.jester.system.api.ranking.IRankingSystemEntry;
import ch.jester.system.api.ranking.IRankingSystemManager;
import ch.jester.system.exceptions.NotAllResultsException;
import ch.jester.ui.round.editors.RankingViewEditor;
import ch.jester.ui.tournament.cnf.TournamentNavigator;

/**
 * Handler zum Erzeugen von Ranglisten
 *
 */
public class RankingHandler extends AbstractCommandHandler implements IHandler {

    private List<IRankingSystem> rankingSystems = new ArrayList<IRankingSystem>();

    private Category cat;

    private Tournament tournament;

    private IRankingSystem primaryRankingSystem;

    private Round round;

    @Override
    public Object executeInternal(ExecutionEvent event) {
        Object selection = mSelUtility.getAsStructuredSelection().getFirstElement();
        if (selection instanceof Tournament) {
            tournament = (Tournament) selection;
            cat = null;
            round = null;
        } else if (selection instanceof Category) {
            cat = (Category) selection;
            tournament = cat.getTournament();
            round = null;
        } else if (selection instanceof Round) {
            round = (Round) selection;
            cat = round.getCategory();
            tournament = cat.getTournament();
        }
        List<RankingSystem> tournamentRankingSystems = tournament.getRankingSystems();
        ServiceUtility su = new ServiceUtility();
        IRankingSystemManager rankingSystemManager = su.getService(IRankingSystemManager.class);
        List<IRankingSystemEntry> registredEntries = rankingSystemManager.getRegistredEntries();
        for (IRankingSystemEntry rankingSystemEntry : registredEntries) {
            for (RankingSystem rankingSystem : tournamentRankingSystems) {
                if (rankingSystemEntry.getImplementationClass().equals(rankingSystem.getImplementationClass())) {
                    rankingSystems.add(rankingSystemEntry.getService());
                    if (rankingSystemEntry.getImplementationClass().equals(tournament.getPrimaryRankingSystem().getImplementationClass())) {
                        primaryRankingSystem = rankingSystemEntry.getService();
                    }
                }
            }
        }
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
        final Shell shell = window.getShell();
        Job job = new Job("Ranking") {

            @Override
            protected IStatus run(IProgressMonitor monitor) {
                try {
                    final RankingReportInput helpentity;
                    if (cat == null && round == null) {
                        Map<Category, Ranking> rankingMap = primaryRankingSystem.calculateRanking(tournament, monitor);
                        helpentity = new RankingReportInput(rankingMap);
                    } else if (cat != null && round == null) {
                        Ranking ranking = primaryRankingSystem.calculateRanking(cat, monitor);
                        helpentity = new RankingReportInput(cat, ranking);
                    } else {
                        Ranking ranking = primaryRankingSystem.calculateRanking(cat, round, monitor);
                        helpentity = new RankingReportInput(cat, round, ranking);
                    }
                    UIUtility.syncExecInUIThread(new Runnable() {

                        @Override
                        public void run() {
                            openEditor(helpentity, RankingViewEditor.ID);
                        }
                    });
                } catch (Exception e) {
                    Throwable exception = ExceptionUtility.getRealException(e);
                    if (exception != e) {
                        if (exception instanceof NotAllResultsException) {
                            final String messageTitel = Messages.PairingHandler_msg_NotAllResults_title;
                            final String errorMessage = Messages.PairingHandler_msg_NotAllResults_text;
                            showErrorDialog(shell, messageTitel, errorMessage);
                            return Status.CANCEL_STATUS;
                        } else {
                            showErrorDialog(shell, Messages.RankingHandler_msg_UnknownError_title, Messages.RankingHandler_msg_UnknownError_text);
                            mLogger.error(e);
                            return Status.CANCEL_STATUS;
                        }
                    } else {
                        return Status.CANCEL_STATUS;
                    }
                }
                return Status.OK_STATUS;
            }

            private void showErrorDialog(final Shell shell, final String messageTitel, final String errorMessage) {
                UIJob uiJob = new UIJob("Error-Message") {

                    @Override
                    public IStatus runInUIThread(IProgressMonitor monitor) {
                        MessageDialog.openError(shell, messageTitel, errorMessage);
                        return Status.OK_STATUS;
                    }
                };
                uiJob.schedule();
            }
        };
        job.schedule();
        CommonNavigator cn = (CommonNavigator) getView(TournamentNavigator.ID);
        cn.getCommonViewer().refresh();
        return null;
    }
}
