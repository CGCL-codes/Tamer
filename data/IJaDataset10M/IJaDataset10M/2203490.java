package se.jayway.millionaire.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import se.jayway.millionaire.dao.Solution;
import se.jayway.millionaire.dao.SolutionDAO;
import se.jayway.millionaire.listener.GameListener;
import se.jayway.millionaire.listener.GameRoundFinishedException;
import se.jayway.millionaire.util.GameUtil;

public class GameManager implements Runnable {

    private static GameManager instance;

    private boolean running;

    private final List<GameListener> gameListeners = new ArrayList<GameListener>();

    private final Map<GameListener, PlayerScore> scores = new HashMap<GameListener, PlayerScore>();

    private boolean isFinished;

    private int noAnswerCount;

    private int correctAnswerCount;

    private int wrongAnswerCount;

    private GameManager() {
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public void start() {
        Thread thread = new Thread(this);
        running = true;
        thread.start();
    }

    public void stop() {
        running = false;
    }

    public void run() {
        while (running) {
            System.out.println();
            System.out.println("------------------------");
            Solution solution = SolutionDAO.getInstance().nextSolution();
            System.out.println("GAME: Starting new game round");
            PlayerChoice[] playerChoices = createPlayerChoices();
            isFinished = false;
            GameUtil.printQuestion(solution);
            notifyNewGameRound(playerChoices, solution);
            System.out.println("GAME: Wait for all players to answer or game round to end");
            waitUntilFinished();
            updateScoreCard(playerChoices, solution);
            System.out.println("GAME: Notify correct answer");
            notifyGameRoundFinished(playerChoices, solution);
            notifyWinners(playerChoices);
        }
    }

    public void waitUntilFinished() {
        checkIfFinished();
        try {
            Thread.sleep(Configuration.getInstance().getGameRoundTimeOut());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isFinished = true;
    }

    public void updateScoreCard(PlayerChoice[] playerChoices, Solution solution) {
        for (PlayerChoice playerChoice : playerChoices) {
            PlayerScore score = (PlayerScore) playerChoice.getScore();
            score.updateScoreCard(playerChoice, solution.getCorrectAnswer());
            String playerAnswer = playerChoice.getPlayerAnswer();
            if (playerAnswer == null) {
                noAnswerCount++;
            } else if (playerAnswer.equals(solution.getCorrectAnswer())) {
                correctAnswerCount++;
            } else {
                wrongAnswerCount++;
            }
        }
    }

    private void checkIfFinished() {
        if (isGameRoundFinished()) {
            throw new GameRoundFinishedException("Sorry, this game round is finished");
        }
    }

    public boolean isGameRoundFinished() {
        return isFinished;
    }

    protected void notifyNewGameRound(PlayerChoice[] playerChoices, Solution solution) {
        for (PlayerChoice playerChoice : playerChoices) {
            PlayerScore score = playerChoice.getScore();
            score.getPlayer().onNewGameRound(solution.getQuestion(), solution.getAnswerAlternatives(), playerChoice);
        }
    }

    protected void notifyGameRoundFinished(PlayerChoice[] playerChoices, Solution solution) {
        for (PlayerChoice playerChoice : playerChoices) {
            PlayerScore score = playerChoice.getScore();
            score.getPlayer().onFinishedGameRound(playerChoice, solution.getCorrectAnswer());
        }
    }

    protected void notifyWinners(PlayerChoice[] playerChoices) {
        for (PlayerChoice playerChoice : playerChoices) {
            PlayerScore score = playerChoice.getScore();
            if (playerChoice.getScore().countCorrectAnswersInRow() == Configuration.getInstance().getNumberOfAnswersInARowToWin()) {
                score.getPlayer().onWin(playerChoice);
            }
        }
    }

    private PlayerChoice[] createPlayerChoices() {
        int len = scores.size();
        PlayerChoice[] playerChoices = new PlayerChoice[len];
        int index = 0;
        for (PlayerScore score : scores.values()) {
            playerChoices[index] = new PlayerChoice(score);
            index++;
        }
        return playerChoices;
    }

    public void addGameListener(GameListener listener) {
        System.out.println(listener + " joins the game");
        PlayerScore score = new PlayerScore(listener);
        gameListeners.add(listener);
        scores.put(listener, score);
    }

    public void removeGameListener(GameListener listener) {
        System.out.println(listener + " leaves the game");
        gameListeners.remove(listener);
        scores.remove(listener);
    }
}
