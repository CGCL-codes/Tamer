package de.berlin.fu.inf.gameai.go.client;

import game.Player;
import game.communication.GameEvent;
import game.communication.GameTriggerEventAction;
import interfaces.IGame;
import interfaces.IGameEvent;
import interfaces.IStateAction;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.log4j.Logger;
import de.berlin.fu.inf.gameai.go.goban.Goban;
import de.berlin.fu.inf.gameai.jgame.go.JGoAction;
import de.berlin.fu.inf.gameai.jgame.go.JGoGame;
import de.berlin.fu.inf.gameai.jgame.go.JGoban;

public class JGameAI extends Player {

    Logger log = Logger.getLogger(JGameAI.class);

    private final Parser parser;

    private final InputOutput io;

    private String gameStartTime;

    /**
     * 
     */
    private static final long serialVersionUID = 1889812763895070565L;

    /**
     * @author mbdecke
     * @throws IOException
     * @throws UnknownHostException
     * @category is the standard constructor, which will create a player which
     *           understands gtp protocol can be changed later, but it is
     *           advised to use the 3 Parameter constructor
     */
    public JGameAI(final String nam, final boolean cpuPlayer) throws UnknownHostException, IOException {
        super(nam, cpuPlayer);
        log.info("---Protocol--->Protocol");
        this.io = new InputOutput();
        this.parser = ParserProducer.getParser();
        new Thread(io).start();
    }

    public JGameAI(final String nam, final boolean cpuPlayer, final String protocol) throws UnknownHostException, IOException {
        super(nam, cpuPlayer);
        this.io = new InputOutput();
        this.parser = ParserProducer.getParser(protocol);
        new Thread(io).start();
        log.info("---Protocol--->Protocol");
    }

    @Override
    public void afterGamePhase() {
        log.info("---Protocol--->afterGamePhase");
        super.afterGamePhase();
    }

    @Override
    public void finish() {
        log.info("---Protocol--->finish");
        super.finish();
    }

    @Override
    public void generateNextAction() {
        log.info("---Protocol--->generateNextAction");
        if (this.playerPosition == theGame.getCurrentPlayerPosition()) {
            io.send(parser.generate(Parser.GENMOVE, String.valueOf(((JGoGame) theGame).getState().getCurrentPlayerPosition())));
            log.info("waiting for server");
            theGame.performGameAction(new GameTriggerEventAction(new GameEvent(GameEvent.GAME_EVENT_SEARCH_FINISHED)));
        }
    }

    @Override
    public long getLastSearchTimeMilliSecs() {
        log.info("---Protocol--->getLastSearchTimeMilliSecs");
        return super.getLastSearchTimeMilliSecs();
    }

    @Override
    public IStateAction getNextAction() {
        log.info("---Protocol--->getNextAction");
        try {
            return parser.parse(io.receive(), theGame.getState());
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void initialize() {
        log.info("---Protocol--->initalize");
        super.initialize();
    }

    @Override
    public void interruptSearch() {
        log.info("---Protocol--->interruptSearch");
        super.interruptSearch();
    }

    protected void handleException(String task, Exception e) {
        log.error("can't finish " + task, e);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void newGameEvent(final IGameEvent event) {
        log.info("receiced game event " + event);
        switch(event.getType()) {
            case GameEvent.GAME_EVENT_GAME_STARTED:
                startGame();
                break;
            case GameEvent.GAME_EVENT_STATEACTION_PERFORMED:
                final IStateAction stateaction = theGame.getLastMove();
                log.debug("got move " + stateaction);
                if (stateaction instanceof JGoAction && stateaction.getActionerId() != this.playerPosition) {
                    performMove((JGoAction) stateaction);
                }
                break;
            case GameEvent.GAME_EVENT_GAME_EXITED:
                finishGame();
                break;
            default:
                log.debug("Nothing todo on event " + event);
                break;
        }
    }

    private void finishGame() {
        final File file = new File("kifoo-GoTextProtocol-" + gameStartTime + ".game.log");
        try {
            final FileWriter filewriter = new FileWriter(file);
            filewriter.write(io.getGameprotocol());
            filewriter.close();
        } catch (IOException e) {
            log.error("can't wirte log file", e);
        }
    }

    private void performMove(final JGoAction goAction) {
        io.send(parser.parse(goAction));
        log.debug("waiting for ACK from server");
        try {
            io.receive();
        } catch (InterruptedException e1) {
            handleException("sending move " + goAction, e1);
        }
        log.debug("answer received ");
    }

    private void startGame() {
        final Calendar cal = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
        gameStartTime = sdf.format(cal.getTime());
        io.send(parser.generate(Parser.BOARDSIZE, String.valueOf(theGame.getState().getDimension())));
        try {
            io.receive();
        } catch (InterruptedException e2) {
            handleException("sending board size", e2);
        }
        io.send(parser.generate(Parser.KOMI, String.valueOf(extractGoban().getKomi())));
        try {
            io.receive();
        } catch (InterruptedException e2) {
            handleException("sending komi", e2);
        }
        io.send(parser.generate(Parser.CLEARBOARD, ""));
        try {
            io.receive();
        } catch (InterruptedException e2) {
            handleException("sending clearboard", e2);
        }
    }

    @SuppressWarnings("unchecked")
    public Goban extractGoban() {
        return ((JGoban) theGame.getState()).getGoban();
    }

    @Override
    public void pauseTimer() {
        log.info("---Protocol--->pauseTimer");
        super.pauseTimer();
    }

    @Override
    public void preGamePhase() {
        theGame.getState().getDimension();
        theGame.getCurrentPlayer();
        theGame.getCurrentPlayerPosition();
        log.info("---Protocol--->preGamePhase");
        super.preGamePhase();
    }

    @Override
    public void resumeTimer() {
        log.info("---Protocol--->resumeTimer");
        super.resumeTimer();
    }

    @Override
    public void setObservingGame(final IGame game) {
        log.info("---Protocol--->setObservingGame");
        super.setObservingGame(game);
    }

    @Override
    public void setPlayersPosition(final int position) {
        log.info("---Protocol--->setPlayersPosition");
        super.setPlayersPosition(position);
    }
}
