package pl.taab.scrachi.gwt.client;

import pl.taab.scrachi.common.BallSet;
import pl.taab.scrachi.common.ScrachiBasicLogic;
import pl.taab.scrachi.common.ScrachiComputerAI;
import pl.taab.scrachi.common.ScrachiGame;
import pl.taab.scrachi.common.ScrachiInteractiveAI;
import pl.taab.scrachi.common.ScrachiTable;
import pl.taab.scrachi.common.XY;
import pl.taab.scrachi.gwt.client.dynamicLocale.LocaleModel;
import pl.taab.scrachi.gwt.client.dynamicLocale.ui.ButtonLocale;
import pl.taab.scrachi.gwt.client.dynamicLocale.ui.LabelLocale;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ScrachiApp implements EntryPoint {

    protected static final int COMP_PLAYER = 1;

    protected static final int USER_PLAYER = 0;

    private final int sizeX = 25;

    private final int sizeY = 25;

    private ScrachiTable scrachiTable;

    private ScrachiBoard scrachiBoard;

    private ScrachiBasicLogic scrachiBasicLogic;

    private ScrachiGame scrachiGame;

    private ScrachiInteractiveAI scrachiPlayerAI;

    private Timer playTimer;

    private Label compScoreLabel;

    private Label userScoreLabel;

    private int gamePhase;

    private int gameType;

    private int blinkMoveIter;

    private XY nextMoveXY;

    private ListBox cleanUpSelectGame;

    private ListBox cleanUpSelectSize;

    private static final int SHOW_MOVE_GP = 0;

    private static final int DO_MOVE_GP = 1;

    private static final int ENDGAME_GP = 2;

    private static final int CHALLANGE_GT = 0;

    private static final int CLEANUP_GT = 1;

    /**
   * This is the entry point method.
   */
    public void onModuleLoad() {
        final LocaleModel localeModel = new LocaleModel();
        ScrachiTable scrachiTable = new ScrachiTable(sizeX, sizeY);
        this.scrachiBasicLogic = new ScrachiBasicLogic();
        createBoard(scrachiTable);
        createNextButton();
        createNewGameButton();
        createNewCleanUpGameButton();
        createScoreDisplay();
        newCleanUpGame("10x10", "1");
        createPlayTimer();
        localeModel.setLocale("EN");
    }

    private void createNewCleanUpGameButton() {
        cleanUpSelectGame = new ListBox();
        cleanUpSelectGame.setVisibleItemCount(1);
        for (int i = 1; i < 100; i++) {
            String s = new Integer(i).toString();
            cleanUpSelectGame.addItem(s, s);
        }
        cleanUpSelectSize = new ListBox();
        cleanUpSelectSize.setVisibleItemCount(1);
        cleanUpSelectSize.addItem("10x10", "10x10");
        HorizontalPanel hp = new HorizontalPanel();
        hp.add(new LabelLocale("lCleanUpSelectSize"));
        hp.add(cleanUpSelectSize);
        hp.add(new Label("     "));
        hp.add(new LabelLocale("lCleanUpSelectGame"));
        hp.add(cleanUpSelectGame);
        RootPanel.get("cleanUpSelectGame").add(hp);
        Button newGameButton = new ButtonLocale("bNewCleanUpGameButton");
        newGameButton.addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                newCleanUpGame(cleanUpSelectSize.getItemText(cleanUpSelectSize.getSelectedIndex()), cleanUpSelectGame.getItemText(cleanUpSelectGame.getSelectedIndex()));
            }
        });
        RootPanel.get("newCleanUpGameButton").add(newGameButton);
    }

    protected void newCleanUpGame(String size, String id) {
        disableChallangePanel(true);
        String set = id;
        String lpad = "0000";
        set = lpad.substring(set.length()) + set;
        String purl = "" + set + ".txt";
        String url = GWT.getModuleBaseURL() + "cleanup/" + size + "/" + purl;
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
        try {
            builder.sendRequest(null, new RequestCallback() {

                private static final int STATUS_CODE_OK = 200;

                public void onError(Request request, Throwable exception) {
                    Window.alert(exception.toString());
                }

                public void onResponseReceived(Request request, Response response) {
                    if (STATUS_CODE_OK == response.getStatusCode()) {
                        String text = response.getText();
                        ScrachiTable st = ScrachiTable.tableFromString(text, 10, 10);
                        createBoard(st);
                        gamePhase = DO_MOVE_GP;
                        gameType = CLEANUP_GT;
                    } else {
                        Window.alert("status: " + response.getStatusCode());
                    }
                }
            });
        } catch (RequestException e) {
            Window.alert(e.toString());
        }
    }

    private void createScoreDisplay() {
        compScoreLabel = new Label();
        userScoreLabel = new Label();
        HorizontalPanel hp = new HorizontalPanel();
        hp.add(new LabelLocale("lScoreComp"));
        hp.add(compScoreLabel);
        RootPanel.get("lComp").add(hp);
        HorizontalPanel hp2 = new HorizontalPanel();
        hp2.add(new LabelLocale("lScoreUser"));
        hp2.add(userScoreLabel);
        RootPanel.get("lUser").add(hp2);
        disableChallangePanel(true);
    }

    private void disableChallangePanel(boolean b) {
        RootPanel.get("challangePanel").setVisible(!b);
    }

    private void createBoard(ScrachiTable scrachiTable) {
        this.scrachiTable = scrachiTable;
        scrachiBoard = new ScrachiBoard(scrachiTable, this);
        RootPanel.get("board").clear();
        RootPanel.get("board").add(scrachiBoard);
    }

    private void createNextButton() {
        Button nextButton = new ButtonLocale("bNextButton");
        nextButton.addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                nextMove();
            }
        });
        RootPanel.get("nextButton").add(nextButton);
        nextButton.setVisible(false);
    }

    private void createNewGameButton() {
        Button newGameButton = new ButtonLocale("bNewGameButton");
        newGameButton.addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                newGame();
            }
        });
        RootPanel.get("newGameButton").add(newGameButton);
    }

    protected void newGame() {
        ScrachiTable st = scrachiBasicLogic.randomTable(sizeX, sizeY, BallSet.FOUR_COLOR_SET);
        createBoard(st);
        this.scrachiGame = new ScrachiGame(st);
        scrachiGame.setNrMovesInTurn(2);
        scrachiPlayerAI = new ScrachiInteractiveAI();
        scrachiGame.setPlayer(0, scrachiPlayerAI);
        scrachiGame.setPlayer(1, new ScrachiComputerAI());
        gamePhase = DO_MOVE_GP;
        gameType = CHALLANGE_GT;
        updateScores();
        disableChallangePanel(false);
    }

    protected void nextMove() {
        nextMoveXY = scrachiGame.nextMove();
        blinkMoveIter = 11;
        gamePhase = SHOW_MOVE_GP;
    }

    private void updateScores() {
        compScoreLabel.setText(new Integer(scrachiGame.getScore(COMP_PLAYER)).toString());
        userScoreLabel.setText(new Integer(scrachiGame.getScore(USER_PLAYER)).toString());
    }

    public void playerMove(int x, int y) {
        if (gamePhase == DO_MOVE_GP) {
            if (gameType == CHALLANGE_GT) {
                if (scrachiGame.currentPlayer() == USER_PLAYER) {
                    scrachiPlayerAI.setNextMove(x, y);
                    nextMove();
                }
            } else if (gameType == CLEANUP_GT) {
                nextMoveXY = new XY(x, y);
                scrachiBasicLogic.reduce(scrachiTable, nextMoveXY.x, nextMoveXY.y);
                blinkMoveIter = 11;
                gamePhase = SHOW_MOVE_GP;
            }
        }
    }

    private void createPlayTimer() {
        playTimer = new Timer() {

            public void run() {
                if (gameType == CHALLANGE_GT) {
                    if (gamePhase == DO_MOVE_GP) {
                        if (scrachiGame.currentPlayer() == COMP_PLAYER) {
                            nextMove();
                        }
                    } else if (gamePhase == SHOW_MOVE_GP) {
                        blinkMoveIter--;
                        if (blinkMoveIter <= 0) {
                            scrachiBoard.update();
                            updateScores();
                            if (scrachiGame.gameFinished()) {
                                gamePhase = ENDGAME_GP;
                                showWinner();
                            } else {
                                gamePhase = DO_MOVE_GP;
                            }
                        } else {
                            blinkMove();
                        }
                    }
                } else if (gameType == CLEANUP_GT) {
                    if (gamePhase == DO_MOVE_GP) {
                    } else if (gamePhase == SHOW_MOVE_GP) {
                        blinkMoveIter--;
                        if (blinkMoveIter <= 0) {
                            scrachiBoard.update();
                            gamePhase = DO_MOVE_GP;
                            if (scrachiTable.isEmpty()) {
                                showCleanUpResult(true);
                            }
                        } else {
                            blinkMove();
                        }
                    }
                }
            }
        };
        playTimer.scheduleRepeating(150);
    }

    protected void showCleanUpResult(boolean b) {
        String str;
        if (b) {
            str = "Winner";
        } else {
            str = "Looser";
        }
        Window.alert(str);
    }

    protected void showWinner() {
        String winner = "The winner is: ";
        if (scrachiGame.getScore(USER_PLAYER) > scrachiGame.getScore(COMP_PLAYER)) {
            winner = winner + " Player";
        } else if (scrachiGame.getScore(USER_PLAYER) < scrachiGame.getScore(COMP_PLAYER)) {
            winner = winner + " Computer";
        } else {
            winner = "Draw";
        }
        Window.alert(winner);
    }

    protected void blinkMove() {
        scrachiBoard.blink(nextMoveXY);
    }
}
