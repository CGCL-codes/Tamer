package org.threadswarm.reversi.bot;

import java.awt.Point;
import java.lang.reflect.InvocationTargetException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.threadswarm.reversi.GameSession;
import org.threadswarm.reversi.Main;
import org.threadswarm.reversi.ReversiGame;
import org.threadswarm.reversi.ReversiGameImp;
import org.threadswarm.reversi.Status;
import org.threadswarm.reversi.gui.MessageGlassPane;
import org.threadswarm.reversi.rater.InsertionQueryResult;
import org.threadswarm.reversi.rater.NegamaxInsertionSeeker;

/**
 *
 * @author steve
 */
public class LocalReversiBot extends ReversiBot {

    private final Status botStatus;

    private final ReversiGame game;

    private final ExecutorService executor;

    private final CompletionService<InsertionQueryResult> completionService;

    private final MessageGlassPane glassPane;

    private volatile Thread botThread;

    public LocalReversiBot(Status botStatus, ReversiGame game, MessageGlassPane glassPane) {
        this.botStatus = botStatus;
        this.game = game;
        int cpuCount = Runtime.getRuntime().availableProcessors();
        executor = Executors.newFixedThreadPool(cpuCount + 1);
        completionService = new ExecutorCompletionService<InsertionQueryResult>(executor);
        this.glassPane = glassPane;
    }

    @Override
    public void run() {
        botThread = Thread.currentThread();
        try {
            while (!Thread.currentThread().isInterrupted()) {
                SwingUtilities.invokeAndWait(new Runnable() {

                    public void run() {
                        glassPane.setVisible(true);
                    }
                });
                queue.takeFirst();
                List<Point> availableInsertions = game.getAvailableInsertions();
                for (Point insertionPoint : availableInsertions) {
                    int searchDepth = GameSession.getInstance().getDepth();
                    completionService.submit(new NegamaxInsertionSeeker(new ReversiGameImp(game), insertionPoint, botStatus, searchDepth));
                }
                int insertionCount = availableInsertions.size();
                List<InsertionQueryResult> insertionQueryResults = new ArrayList<InsertionQueryResult>(insertionCount);
                for (int x = 0; x < insertionCount; x++) {
                    try {
                        InsertionQueryResult result = completionService.take().get();
                        insertionQueryResults.add(result);
                    } catch (ExecutionException ex) {
                    }
                }
                if (insertionQueryResults.isEmpty()) {
                    if (game.isPreviousPassed()) {
                        Map<Status, Integer> countMap = game.getCountMap();
                        Integer cellsWhite = countMap.get(Status.WHITE);
                        Integer cellsBlack = countMap.get(Status.BLACK);
                        final Status winner;
                        if (cellsWhite == null) {
                            winner = Status.BLACK;
                        } else if (cellsBlack == null) {
                            winner = Status.WHITE;
                        } else {
                            winner = (cellsWhite > cellsBlack) ? Status.WHITE : Status.BLACK;
                        }
                        SwingUtilities.invokeAndWait(new Runnable() {

                            public void run() {
                                JFrame frame = (JFrame) SwingUtilities.getRoot(glassPane);
                                JOptionPane.showMessageDialog(frame, winner + " won the game!");
                                Main.getGameContentPanel(frame);
                            }
                        });
                        return;
                    } else {
                        game.pass();
                    }
                } else {
                    InsertionQueryResult chosenResult = null;
                    if (insertionQueryResults.size() > 1) {
                        SortedMap<Integer, List<InsertionQueryResult>> resultMap = new TreeMap<Integer, List<InsertionQueryResult>>();
                        for (InsertionQueryResult result : insertionQueryResults) {
                            Integer score = result.getValue();
                            List<InsertionQueryResult> scoreList = resultMap.get(score);
                            if (scoreList == null) {
                                scoreList = new LinkedList<InsertionQueryResult>();
                                resultMap.put(score, scoreList);
                            }
                            scoreList.add(result);
                        }
                        List<InsertionQueryResult> bestResults = resultMap.get(resultMap.firstKey());
                        if (bestResults.size() == 1) {
                            chosenResult = bestResults.get(0);
                        } else {
                            Random prng = new SecureRandom();
                            int randomIndex = prng.nextInt(bestResults.size());
                            chosenResult = bestResults.get(randomIndex);
                        }
                    } else {
                        chosenResult = insertionQueryResults.get(0);
                    }
                    game.insert(chosenResult.getInsertion(), botStatus);
                }
                SwingUtilities.invokeAndWait(new Runnable() {

                    public void run() {
                        glassPane.setVisible(false);
                    }
                });
            }
        } catch (InterruptedException ex) {
        } catch (InvocationTargetException ex) {
        } finally {
            executor.shutdownNow();
        }
    }

    public void shutdown() {
        if (botThread != null) {
            botThread.interrupt();
        }
    }
}
