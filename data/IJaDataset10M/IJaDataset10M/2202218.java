package edu.sfsu.powerrangers.jeopardy.server;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Vector;
import edu.sfsu.powerrangers.jeopardy.*;
import edu.sfsu.powerrangers.jeopardy.gamestate.*;

public class ServerController implements GeneralServer, HostServer, ContestantServer, AudienceServer {

    private Game[] games;

    private GameState dm;

    private ServerState state;

    private HostClient host;

    private ContestantCollection conts;

    private Vector<AudienceClient> audience;

    private Iterator<Round> rounds;

    public ServerController(Game[] games) {
        this.games = games;
        this.dm = new GameState();
        this.state = ServerState.WAITING_FOR_CONNECTION;
        this.host = null;
        conts = new ContestantCollection();
        this.audience = new Vector<AudienceClient>();
        this.rounds = null;
    }

    private void setState(ServerState state) {
        this.state = state;
        System.out.println(state.getDescription());
    }

    private void reset() {
        if (host != null) {
            try {
                host.gameEnded();
            } catch (RemoteException ignored) {
            }
        }
        for (ContestantClient contestant : conts.getClients()) {
            try {
                contestant.gameEnded();
            } catch (RemoteException ignored) {
            }
        }
        for (AudienceClient audience : this.audience) {
            try {
                audience.gameEnded();
            } catch (RemoteException ignored) {
            }
        }
        this.dm = new GameState();
        this.state = ServerState.WAITING_FOR_CONNECTION;
        this.host = null;
        conts = new ContestantCollection();
        this.audience = new Vector<AudienceClient>();
        this.rounds = null;
        System.out.println(ServerState.WAITING_FOR_CONNECTION.getDescription());
    }

    private void gameStarted() throws RemoteException {
        if (host != null) host.gameStarted();
        for (ContestantClient contestant : conts.getClients()) contestant.gameStarted();
        for (AudienceClient audience : this.audience) audience.gameStarted();
    }

    private void startingRound(Round round) throws RemoteException {
        if (host != null) host.startingRound(round);
        for (ContestantClient contestant : conts.getClients()) contestant.startingRound(round);
        for (AudienceClient audience : this.audience) audience.startingRound(round);
    }

    private void startingFinalRound(FinalRound round) throws RemoteException {
        if (host != null) host.startingFinalRound(round);
        for (ContestantClient contestant : conts.getClients()) contestant.startingFinalRound(round);
        for (AudienceClient audience : this.audience) audience.startingFinalRound(round);
    }

    private void displayBoard() throws RemoteException {
        if (host != null) host.displayBoard(dm);
        for (ContestantClient contestant : conts.getClients()) contestant.displayBoard(dm);
        for (AudienceClient audience : this.audience) audience.displayBoard(dm);
    }

    private void displayDouble() throws RemoteException {
        if (host != null) host.displayDouble();
        for (ContestantClient contestant : conts.getClients()) contestant.displayDouble();
        for (AudienceClient audience : this.audience) audience.displayDouble();
    }

    private void displayClue(String clue) throws RemoteException {
        if (host != null) host.displayClue(clue);
        for (ContestantClient contestant : conts.getClients()) contestant.displayClue(clue);
        for (AudienceClient audience : this.audience) audience.displayClue(clue);
    }

    private void displayResponse(String response) throws RemoteException {
        if (host != null) host.displayResponse(response);
        for (ContestantClient contestant : conts.getClients()) contestant.displayResponse(response);
        for (AudienceClient audience : this.audience) audience.displayResponse(response);
    }

    private void announceChampion(String name, int score) throws RemoteException {
        if (host != null) host.announceChampion(name, score);
        for (ContestantClient contestant : conts.getClients()) contestant.announceChampion(name, score);
        for (AudienceClient audience : this.audience) audience.announceChampion(name, score);
    }

    private Object nextRound() {
        if (rounds == null) {
            return null;
        } else if (rounds.hasNext()) {
            return rounds.next();
        } else {
            rounds = null;
            if (dm.getGame().containsFinalRound()) {
                return dm.getGame().getFinalRound();
            } else {
                return null;
            }
        }
    }

    private void startRound(Object o) throws RemoteException {
        if (o instanceof Round) {
            Round r = (Round) o;
            System.out.println("starting round " + (dm.getGame().indexOf(r) + 1));
            dm.setCurrentRound(r);
            startingRound(r);
            displayBoard();
            setState(ServerState.WAITING_FOR_ROUND_INTRO);
            host.waitingForRoundIntro(r.getCategoryNames());
            for (ContestantClient cc : conts.getClients()) cc.waitingForNothing();
        } else if (o instanceof FinalRound) {
            System.out.println("disconnecting contestants with <= $0...");
            for (Contestant c : dm.getContestants()) {
                if (c.getScore() <= 0) {
                    System.out.println("disconnecting \"" + c.getName() + "\"");
                    if (dm.getContestantCount() <= 1) {
                        reset();
                        return;
                    } else {
                        ContestantClient contestant = conts.getClient(c);
                        dm.removeContestant(c);
                        conts.remove(c);
                        contestant.gameEnded();
                    }
                }
            }
            FinalRound r = (FinalRound) o;
            System.out.println("starting final round");
            dm.setCurrentFinalRound(r);
            startingFinalRound(r);
            displayBoard();
            setState(ServerState.WAITING_FOR_FINAL_ROUND_INTRO);
            host.waitingForRoundIntro(new String[] { r.getCategoryName() });
            for (ContestantClient cc : conts.getClients()) cc.waitingForNothing();
        } else {
            Contestant maxc = dm.getContestant(0);
            int maxs = maxc.getScore();
            for (Contestant c : dm.getContestants()) {
                if (c.getScore() > maxs) {
                    maxc = c;
                    maxs = c.getScore();
                }
            }
            System.out.println("contestant \"" + maxc.getName() + "\" with " + maxs + " is the champion!");
            dm.setCurrentRound(null);
            announceChampion(maxc.getName(), maxs);
            setState(ServerState.WAITING_FOR_WRAPUP);
            host.waitingForWrapUp();
            for (ContestantClient cc : conts.getClients()) cc.waitingForNothing();
        }
    }

    private void startClueSelection() throws RemoteException {
        if (dm.getCurrentRound().allRespondedTo()) {
            startRound(nextRound());
        } else {
            setState(ServerState.WAITING_FOR_CLUE_SELECTION);
            host.waitingForNothing();
            for (ContestantClient cc : conts.getClients()) {
                if (conts.getDM(cc).equals(dm.getCurrentClueSelector())) cc.waitingForClueSelection(); else cc.waitingForNothing();
            }
        }
    }

    @Override
    public synchronized Game[] getGames() {
        return games;
    }

    @Override
    public synchronized GameState getGameState() {
        return dm;
    }

    @Override
    public synchronized Host getHost() {
        return dm.getHost();
    }

    @Override
    public synchronized Contestant[] getContestants() {
        return dm.getContestants();
    }

    @Override
    public synchronized Game getGame() {
        return dm.getGame();
    }

    @Override
    public synchronized Round getCurrentRound() {
        return dm.getCurrentRound();
    }

    @Override
    public synchronized FinalRound getCurrentFinalRound() {
        return dm.getCurrentFinalRound();
    }

    @Override
    public synchronized Contestant getCurrentClueSelector() {
        return dm.getCurrentClueSelector();
    }

    @Override
    public synchronized Contestant getCurrentResponder() {
        return dm.getCurrentResponder();
    }

    @Override
    public synchronized Category getCurrentCategory() {
        return dm.getCurrentCategory();
    }

    @Override
    public synchronized Clue getCurrentClue() {
        return dm.getCurrentClue();
    }

    @Override
    public synchronized void loginHost(HostClient host, String name) throws RemoteException {
        switch(state) {
            case WAITING_FOR_CONNECTION:
                System.out.println("-> host \"" + name + "\" connected");
                dm.setHost(new Host(name));
                this.host = host;
                setState(ServerState.WAITING_FOR_GAME_SELECTION);
                host.waitingForGameSelection(games);
                break;
            case WAITING_FOR_GAME_SELECTION:
            case WAITING_FOR_GAME_START:
                host.displayError("The host cannot log in because another host has already logged in.");
                break;
            default:
                host.displayError("The host cannot log in because a game is already in progress.");
                break;
        }
    }

    @Override
    public synchronized void loginContestant(ContestantClient contestant, String name, int startingScore) throws RemoteException {
        switch(state) {
            case WAITING_FOR_CONNECTION:
            case WAITING_FOR_GAME_SELECTION:
            case WAITING_FOR_GAME_START:
                System.out.println("-> contestant \"" + name + "\" connected");
                Contestant c = new Contestant(name, startingScore);
                dm.addContestant(c);
                if (dm.getCurrentClueSelector() == null) {
                    dm.setCurrentClueSelector(c);
                }
                conts.add(contestant, c);
                break;
            default:
                contestant.displayError("The contestant cannot log in because a game is already in progress.");
                break;
        }
    }

    @Override
    public synchronized void loginAudience(AudienceClient audience) throws RemoteException {
        switch(state) {
            case WAITING_FOR_CONNECTION:
            case WAITING_FOR_GAME_SELECTION:
            case WAITING_FOR_GAME_START:
                System.out.println("-> audience connected");
                this.audience.add(audience);
                break;
            default:
                System.out.println("-> audience connected");
                this.audience.add(audience);
                audience.gameStarted();
                if (dm.getCurrentFinalRound() != null) audience.startingFinalRound(dm.getCurrentFinalRound()); else if (dm.getCurrentRound() != null) audience.startingRound(dm.getCurrentRound());
                audience.displayBoard(dm);
                break;
        }
    }

    @Override
    public synchronized void selectGame(String name) throws RemoteException {
        switch(state) {
            case WAITING_FOR_CONNECTION:
                host.displayError("The host cannot select a game because the host has not logged in yet.");
                break;
            case WAITING_FOR_GAME_SELECTION:
                for (Game game : games) {
                    if (game.getName().equalsIgnoreCase(name)) {
                        System.out.println("-> host selected game \"" + name + "\"");
                        Game g = game.clone();
                        dm.setGame(g);
                        rounds = g.iterator();
                        setState(ServerState.WAITING_FOR_GAME_START);
                        host.waitingForGameStart();
                        return;
                    }
                }
                host.displayError("The host cannot select that game because there is no game by that name.");
                break;
            case WAITING_FOR_GAME_START:
                host.displayError("The host cannot select a game because a game has already been selected.");
                break;
            default:
                host.displayError("The host cannot select a game because a game is already in progress.");
                break;
        }
    }

    @Override
    public synchronized void startGame() throws RemoteException {
        switch(state) {
            case WAITING_FOR_CONNECTION:
                host.displayError("The host cannot start the game because the host has not logged in yet.");
                break;
            case WAITING_FOR_GAME_SELECTION:
                host.displayError("The host cannot start the game because the host has not selected a game yet.");
                break;
            case WAITING_FOR_GAME_START:
                System.out.println("-> host started game");
                gameStarted();
                displayBoard();
                setState(ServerState.WAITING_FOR_GAME_INTRO);
                host.waitingForGameIntro(dm.getContestantNames());
                for (ContestantClient cc : conts.getClients()) cc.waitingForNothing();
                break;
            default:
                host.displayError("The host cannot start the game because a game is already in progress.");
                break;
        }
    }

    @Override
    public synchronized void continueGame() throws RemoteException {
        switch(state) {
            case WAITING_FOR_CONNECTION:
            case WAITING_FOR_GAME_SELECTION:
            case WAITING_FOR_GAME_START:
                host.displayError("The host cannot continue the game because the game has not started yet.");
                break;
            case WAITING_FOR_GAME_INTRO:
                System.out.println("-> host finished introduction");
                for (Contestant c : dm.getContestants()) c.introduce();
                startRound(nextRound());
                break;
            case WAITING_FOR_ROUND_INTRO:
                System.out.println("-> host finished introduction");
                for (Category c : dm.getCurrentRound()) c.revealCategory();
                startClueSelection();
                break;
            case WAITING_FOR_DOUBLE_CLUE_INTRO:
                System.out.println("-> host finished reading");
                dm.getCurrentClue().revealClue();
                displayClue(dm.getCurrentClue().getClue());
                setState(ServerState.WAITING_FOR_DOUBLE_RESPONSE);
                host.waitingForNothing();
                for (ContestantClient cc : conts.getClients()) {
                    if (conts.getDM(cc).equals(dm.getCurrentResponder())) cc.waitingForResponse(dm.getCurrentCategory().getCategoryName(), dm.getCurrentClue().getClue()); else cc.waitingForNothing();
                }
                break;
            case WAITING_FOR_CLUE_INTRO:
                System.out.println("-> host finished reading");
                dm.getCurrentClue().revealClue();
                displayClue(dm.getCurrentClue().getClue());
                setState(ServerState.WAITING_FOR_BUZZ_IN);
                host.waitingForContinue();
                for (ContestantClient cc : conts.getClients()) {
                    cc.waitingForBuzzIn(dm.getCurrentCategory().getCategoryName(), dm.getCurrentClue().getClue());
                }
                break;
            case WAITING_FOR_BUZZ_IN:
                System.out.println("-> host continued game");
                setState(ServerState.WAITING_FOR_RESPONSE_INTRO);
                host.waitingForResponseIntro(dm.getCurrentClue().getResponse());
                for (ContestantClient cc : conts.getClients()) {
                    cc.waitingForNothing();
                }
                break;
            case WAITING_FOR_RESPONSE_INTRO:
                System.out.println("-> host announced correct response");
                dm.getCurrentClue().respondTo();
                dm.setCurrentResponder(null);
                dm.setCurrentClue(null, null);
                displayBoard();
                startClueSelection();
                break;
            case WAITING_FOR_FINAL_ROUND_INTRO:
                System.out.println("-> host finished introduction");
                dm.getCurrentFinalRound().revealCategory();
                displayBoard();
                setState(ServerState.WAITING_FOR_FINAL_WAGER);
                host.waitingForContinue();
                for (ContestantClient cc : conts.getClients()) {
                    cc.waitingForWager(dm.getCurrentFinalRound().getCategoryName());
                }
                break;
            case WAITING_FOR_FINAL_WAGER:
                System.out.println("-> host ended wagering time");
                setState(ServerState.WAITING_FOR_FINAL_CLUE_INTRO);
                host.waitingForClueIntro(dm.getCurrentFinalRound().getClue());
                for (ContestantClient cc : conts.getClients()) {
                    cc.waitingForNothing();
                }
                break;
            case WAITING_FOR_FINAL_CLUE_INTRO:
                System.out.println("-> host finished reading");
                dm.getCurrentFinalRound().revealClue();
                displayClue(dm.getCurrentFinalRound().getClue());
                setState(ServerState.WAITING_FOR_FINAL_RESPONSE);
                host.waitingForContinue();
                for (ContestantClient cc : conts.getClients()) {
                    cc.waitingForResponse(dm.getCurrentFinalRound().getCategoryName(), dm.getCurrentFinalRound().getClue());
                }
                break;
            case WAITING_FOR_FINAL_RESPONSE:
                System.out.println("-> host ended response time");
                setState(ServerState.WAITING_FOR_FINAL_RESPONSE_INTRO);
                host.waitingForResponseIntro(dm.getCurrentFinalRound().getResponse());
                for (ContestantClient cc : conts.getClients()) {
                    cc.waitingForNothing();
                }
                break;
            case WAITING_FOR_FINAL_RESPONSE_INTRO:
                System.out.println("-> host finished reading");
                startRound(nextRound());
                break;
            case WAITING_FOR_WRAPUP:
                System.out.println("-> host wrapped up");
                reset();
                break;
            default:
                host.displayError("The host cannot continue at this point in the game.");
                break;
        }
    }

    @Override
    public synchronized void revealContestant(String contestant) throws RemoteException {
        switch(state) {
            case WAITING_FOR_CONNECTION:
            case WAITING_FOR_GAME_SELECTION:
            case WAITING_FOR_GAME_START:
                host.displayError("The host cannot introduce contestants because the game has not started yet.");
                break;
            case WAITING_FOR_GAME_INTRO:
                System.out.println("-> host introduced contestant \"" + contestant + "\"");
                for (Contestant c : dm.getContestants()) {
                    if (c.getName().equalsIgnoreCase(contestant)) {
                        c.introduce();
                    }
                }
                displayBoard();
                break;
            default:
                host.displayError("The host cannot introduce contestants because the game has already started.");
                break;
        }
    }

    @Override
    public synchronized void revealCategory(String category) throws RemoteException {
        switch(state) {
            case WAITING_FOR_CONNECTION:
            case WAITING_FOR_GAME_SELECTION:
            case WAITING_FOR_GAME_START:
            case WAITING_FOR_GAME_INTRO:
                host.displayError("The host cannot introduce categories because the round has not started yet.");
                break;
            case WAITING_FOR_ROUND_INTRO:
                System.out.println("-> host introduced category \"" + category + "\"");
                for (Category c : dm.getCurrentRound()) {
                    if (c.getCategoryName().equalsIgnoreCase(category)) {
                        c.revealCategory();
                    }
                }
                displayBoard();
                break;
            case WAITING_FOR_FINAL_ROUND_INTRO:
                System.out.println("-> host introduced category \"" + category + "\"");
                dm.getCurrentFinalRound().revealCategory();
                displayBoard();
                break;
            default:
                host.displayError("The host cannot introduce categories because the round has already started.");
                break;
        }
    }

    @Override
    public synchronized void selectClue(ContestantClient contestant, String category, int value) throws RemoteException {
        switch(state) {
            case WAITING_FOR_CONNECTION:
            case WAITING_FOR_GAME_SELECTION:
            case WAITING_FOR_GAME_START:
            case WAITING_FOR_GAME_INTRO:
            case WAITING_FOR_ROUND_INTRO:
                contestant.displayError("The contestant cannot select clues because the round has not started yet.");
                break;
            case WAITING_FOR_CLUE_SELECTION:
                if (!contestant.equals(conts.getClient(dm.getCurrentClueSelector()))) {
                    contestant.displayError("The contestant cannot select clues because they are not the current selector.");
                    return;
                }
                for (Category cat : dm.getCurrentRound()) {
                    if (cat.getCategoryName().equalsIgnoreCase(category)) {
                        for (Clue c : cat) {
                            if (c.getValue() == value && !c.isRespondedTo()) {
                                System.out.println("-> contestant \"" + conts.getName(contestant) + "\" selected \"" + category + "\" for $" + value);
                                dm.setCurrentClue(cat, c);
                                if (c.isDouble()) {
                                    dm.setCurrentResponder(conts.getDM(contestant));
                                    c.revealDouble();
                                    displayDouble();
                                    setState(ServerState.WAITING_FOR_DOUBLE_WAGER);
                                    host.waitingForNothing();
                                    for (ContestantClient cc : conts.getClients()) {
                                        if (cc.equals(contestant)) cc.waitingForWager(category); else cc.waitingForNothing();
                                    }
                                } else {
                                    dm.setCurrentResponder(null);
                                    c.revealDouble();
                                    displayBoard();
                                    setState(ServerState.WAITING_FOR_CLUE_INTRO);
                                    host.waitingForClueIntro(c.getClue());
                                    for (ContestantClient cc : conts.getClients()) {
                                        cc.waitingForNothing();
                                    }
                                }
                                return;
                            }
                        }
                    }
                }
                contestant.displayError("The contestant cannot select that clue because there is no category by that name, there is no clue with that value, or the clue has already been chosen.");
                break;
            default:
                contestant.displayError("The contestant cannot select clues at this point in the game.");
        }
    }

    @Override
    public synchronized void revealClue(String category, int value) throws RemoteException {
        switch(state) {
            case WAITING_FOR_CONNECTION:
            case WAITING_FOR_GAME_SELECTION:
            case WAITING_FOR_GAME_START:
            case WAITING_FOR_GAME_INTRO:
            case WAITING_FOR_ROUND_INTRO:
            case WAITING_FOR_FINAL_ROUND_INTRO:
                host.displayError("The host cannot reveal clues because the round has not started yet.");
                break;
            case WAITING_FOR_DOUBLE_CLUE_INTRO:
            case WAITING_FOR_CLUE_INTRO:
                System.out.println("-> host revealed clue");
                dm.getCurrentClue().revealClue();
                displayClue(dm.getCurrentClue().getClue());
                break;
            case WAITING_FOR_FINAL_CLUE_INTRO:
                System.out.println("-> host revealed clue");
                dm.getCurrentFinalRound().revealClue();
                displayClue(dm.getCurrentFinalRound().getClue());
                break;
            default:
                host.displayError("The host cannot reveal clues at this point in the game.");
                break;
        }
    }

    @Override
    public synchronized void buzzIn(ContestantClient contestant) throws RemoteException {
        switch(state) {
            case WAITING_FOR_CONNECTION:
            case WAITING_FOR_GAME_SELECTION:
            case WAITING_FOR_GAME_START:
            case WAITING_FOR_GAME_INTRO:
            case WAITING_FOR_ROUND_INTRO:
                contestant.displayError("The contestant cannot buzz in because the round has not started yet.");
                break;
            case WAITING_FOR_BUZZ_IN:
                System.out.println("-> contestant \"" + conts.getName(contestant) + "\" buzzed in");
                dm.setCurrentResponder(conts.getDM(contestant));
                setState(ServerState.WAITING_FOR_RESPONSE);
                host.waitingForNothing();
                for (ContestantClient cc : conts.getClients()) {
                    if (cc.equals(contestant)) cc.waitingForResponse(dm.getCurrentCategory().getCategoryName(), dm.getCurrentClue().getClue()); else cc.waitingForNothing();
                }
                break;
            default:
                contestant.displayError("The contestant cannot buzz in at this point in the game.");
                break;
        }
    }

    @Override
    public synchronized void wager(ContestantClient contestant, int wager) throws RemoteException {
        switch(state) {
            case WAITING_FOR_CONNECTION:
            case WAITING_FOR_GAME_SELECTION:
            case WAITING_FOR_GAME_START:
            case WAITING_FOR_GAME_INTRO:
            case WAITING_FOR_ROUND_INTRO:
            case WAITING_FOR_FINAL_ROUND_INTRO:
                contestant.displayError("The contestant cannot wager because the round has not started yet.");
                break;
            case WAITING_FOR_DOUBLE_WAGER:
                if (!contestant.equals(conts.getClient(dm.getCurrentResponder()))) {
                    contestant.displayError("The contestant cannot wager because they are not the current responder.");
                    return;
                }
                int maxwager = Math.max(5, Math.max(conts.getDM(contestant).getScore(), dm.getCurrentRound().getMaxValue()));
                if (wager < 5 || wager > maxwager) {
                    contestant.displayError("The contestant must wager between $5 and $" + maxwager + ".");
                    return;
                }
                System.out.println("-> contestant \"" + conts.getName(contestant) + "\" wagered $" + wager);
                conts.getDM(contestant).setWager(wager);
                setState(ServerState.WAITING_FOR_DOUBLE_CLUE_INTRO);
                host.waitingForClueIntro(dm.getCurrentClue().getClue());
                for (ContestantClient cc : conts.getClients()) cc.waitingForNothing();
                break;
            case WAITING_FOR_FINAL_WAGER:
                int maxfwager = Math.max(0, conts.getDM(contestant).getScore());
                if (wager < 0 || wager > maxfwager) {
                    contestant.displayError("The contestant must wager between $0 and $" + maxfwager + ".");
                    return;
                }
                System.out.println("-> contestant \"" + conts.getName(contestant) + "\" wagered $" + wager);
                conts.getDM(contestant).setWager(wager);
                break;
            default:
                contestant.displayError("The contestant cannot wager at this point in the game.");
                break;
        }
    }

    @Override
    public synchronized void respond(ContestantClient contestant, String response) throws RemoteException {
        switch(state) {
            case WAITING_FOR_CONNECTION:
            case WAITING_FOR_GAME_SELECTION:
            case WAITING_FOR_GAME_START:
            case WAITING_FOR_GAME_INTRO:
            case WAITING_FOR_ROUND_INTRO:
            case WAITING_FOR_FINAL_ROUND_INTRO:
                contestant.displayError("The contestant cannot respond to clues because the round has not started yet.");
                break;
            case WAITING_FOR_DOUBLE_RESPONSE:
                if (!contestant.equals(conts.getClient(dm.getCurrentResponder()))) {
                    contestant.displayError("The contestant cannot respond because they are not the current responder.");
                    return;
                }
                System.out.println("-> contestant \"" + conts.getName(contestant) + "\" responded \"" + response + "\"");
                setState(ServerState.WAITING_FOR_DOUBLE_VERIFICATION);
                host.waitingForVerification(dm.getCurrentClue().getResponse(), response);
                for (ContestantClient cc : conts.getClients()) cc.waitingForNothing();
                break;
            case WAITING_FOR_RESPONSE:
                if (!contestant.equals(conts.getClient(dm.getCurrentResponder()))) {
                    contestant.displayError("The contestant cannot respond because they are not the current responder.");
                    return;
                }
                System.out.println("-> contestant \"" + conts.getName(contestant) + "\" responded \"" + response + "\"");
                setState(ServerState.WAITING_FOR_VERIFICATION);
                host.waitingForVerification(dm.getCurrentClue().getResponse(), response);
                for (ContestantClient cc : conts.getClients()) cc.waitingForNothing();
                break;
            case WAITING_FOR_FINAL_RESPONSE:
                System.out.println("-> contestant \"" + conts.getName(contestant) + "\" responded \"" + response + "\"");
                conts.getDM(contestant).setResponse(response);
                break;
            default:
                contestant.displayError("The contestant cannot respond to clues at this point in the game.");
                break;
        }
    }

    @Override
    public synchronized void revealResponse(String contestant) throws RemoteException {
        switch(state) {
            case WAITING_FOR_CONNECTION:
            case WAITING_FOR_GAME_SELECTION:
            case WAITING_FOR_GAME_START:
            case WAITING_FOR_GAME_INTRO:
            case WAITING_FOR_ROUND_INTRO:
            case WAITING_FOR_FINAL_ROUND_INTRO:
                host.displayError("The host cannot reveal responses because the round has not started yet.");
                break;
            case WAITING_FOR_FINAL_RESPONSE_INTRO:
                System.out.println("-> host revealed response from contestant \"" + contestant + "\"");
                for (Contestant c : dm.getContestants()) {
                    if (c.getName().equalsIgnoreCase(contestant)) {
                        dm.setCurrentResponder(c);
                        c.revealResponse();
                        c.revealWager();
                        displayResponse(c.getResponse());
                    }
                }
                break;
            default:
                host.displayError("The host cannot reveal responses at this point in the game.");
        }
    }

    @Override
    public synchronized void verifyResponse(boolean accepted) throws RemoteException {
        switch(state) {
            case WAITING_FOR_CONNECTION:
            case WAITING_FOR_GAME_SELECTION:
            case WAITING_FOR_GAME_START:
            case WAITING_FOR_GAME_INTRO:
            case WAITING_FOR_ROUND_INTRO:
            case WAITING_FOR_FINAL_ROUND_INTRO:
                host.displayError("The host cannot verify responses because the round has not started yet.");
                break;
            case WAITING_FOR_DOUBLE_VERIFICATION:
                System.out.println("-> host " + (accepted ? "accepted" : "rejected") + " the response");
                if (accepted) dm.getCurrentResponder().addScore(dm.getCurrentResponder().getWager()); else dm.getCurrentResponder().subtractScore(dm.getCurrentResponder().getWager());
                dm.getCurrentClue().respondTo();
                dm.setCurrentResponder(null);
                dm.setCurrentClue(null, null);
                displayBoard();
                startClueSelection();
                break;
            case WAITING_FOR_VERIFICATION:
                System.out.println("-> host " + (accepted ? "accepted" : "rejected") + " the response");
                if (accepted) {
                    dm.getCurrentResponder().addScore(dm.getCurrentClue().getValue());
                    dm.setCurrentClueSelector(dm.getCurrentResponder());
                    dm.getCurrentClue().respondTo();
                    dm.setCurrentResponder(null);
                    dm.setCurrentClue(null, null);
                    displayBoard();
                    startClueSelection();
                } else {
                    dm.getCurrentResponder().subtractScore(dm.getCurrentClue().getValue());
                    setState(ServerState.WAITING_FOR_BUZZ_IN);
                    host.waitingForContinue();
                    for (ContestantClient cc : conts.getClients()) {
                        cc.waitingForBuzzIn(dm.getCurrentCategory().getCategoryName(), dm.getCurrentClue().getClue());
                    }
                }
                break;
            case WAITING_FOR_FINAL_RESPONSE_INTRO:
                System.out.println("-> host " + (accepted ? "accepted" : "rejected") + " the response");
                if (accepted) {
                    dm.getCurrentResponder().addScore(dm.getCurrentResponder().getWager());
                } else {
                    dm.getCurrentResponder().subtractScore(dm.getCurrentResponder().getWager());
                }
                displayBoard();
                break;
            default:
                host.displayError("The host cannot verify responses at this point in the game.");
                break;
        }
    }

    @Override
    public synchronized void endGame() throws RemoteException {
        System.out.println("-> host ended game early");
        reset();
    }

    @Override
    public synchronized void leaveGame(ContestantClient contestant) throws RemoteException {
        switch(state) {
            case WAITING_FOR_CLUE_SELECTION:
                if (conts.getDM(contestant).equals(dm.getCurrentClueSelector())) {
                    contestant.displayError("The contestant cannot leave the game before selecting a clue.");
                    return;
                }
                break;
            case WAITING_FOR_DOUBLE_RESPONSE:
            case WAITING_FOR_RESPONSE:
            case WAITING_FOR_FINAL_RESPONSE:
                if (conts.getDM(contestant).equals(dm.getCurrentResponder())) {
                    contestant.displayError("The contestant cannot leave the game before giving a response.");
                    return;
                }
                break;
            case WAITING_FOR_DOUBLE_WAGER:
            case WAITING_FOR_FINAL_WAGER:
                if (conts.getDM(contestant).equals(dm.getCurrentResponder())) {
                    contestant.displayError("The contestant cannot leave the game before placing a wager.");
                    return;
                }
                break;
        }
        System.out.println("-> contestant \"" + conts.getName(contestant) + "\" left the game");
        if (dm.getContestantCount() <= 1) {
            reset();
        } else {
            dm.removeContestant(conts.getDM(contestant));
            conts.remove(contestant);
            contestant.gameEnded();
        }
    }

    @Override
    public synchronized void leaveGame(AudienceClient audience) throws RemoteException {
        System.out.println("-> audience left the game");
        this.audience.remove(audience);
        audience.gameEnded();
    }
}
