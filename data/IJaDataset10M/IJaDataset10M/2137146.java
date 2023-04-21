package mafiagame;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import mafiagame.Mark.MarkScope;
import mafiagame.Mark.MarkType;
import mafiagame.Player.Alignment;
import mafiagame.RoleDefinition.DefinitionType;
import mafiagame.roles.Detective;
import mafiagame.roles.Doctor;
import mafiagame.roles.Driver;
import mafiagame.roles.SexAddict;
import mafiagame.roles.Vigilante;
import mafiagame.Mark;
import mafiagame.RoleDefinition;

public class Game {

    public enum Stage {

        SETUP, NIGHT, WILL, DAY, GAMEOVER
    }

    ;

    public MafiaInterface communicator;

    public boolean gameStarted;

    private List<String> gameHistory;

    public Random r;

    public static final int maxMafiaRoles = 1;

    public Map<String, List<Action>> requiredActionsForUser;

    public Map<String, List<Action>> optionalActionsForUser;

    public List<RoleDefinition> selectedRoles;

    protected int numHeartbeats;

    public int numGaveWill;

    private List<Player> players;

    private List<Player> mafia;

    int playerCount;

    private int numMafia;

    public Stage currentStage;

    int dayCount;

    private int livingPlayers;

    public long seed;

    private int numSleeping;

    public Game(MafiaInterface wrapper) {
        selectedRoles = new ArrayList<RoleDefinition>();
        requiredActionsForUser = new HashMap<String, List<Action>>();
        optionalActionsForUser = new HashMap<String, List<Action>>();
        communicator = wrapper;
        players = new ArrayList<Player>();
        mafia = new ArrayList<Player>();
        gameHistory = new ArrayList<String>();
        seed = System.currentTimeMillis();
        r = new Random(seed);
        gameStarted = false;
    }

    public ArrayList<Player> getLivePlayers() {
        ArrayList<Player> list = new ArrayList<Player>();
        for (Player p : players) {
            if (p.living) {
                list.add(p);
            }
        }
        return list;
    }

    public void addAction(String user, Action a, boolean required) {
        if (required) {
            if (requiredActionsForUser.get(user) == null) {
                requiredActionsForUser.put(user, new ArrayList<Action>());
            }
            requiredActionsForUser.get(user).add(a);
        } else {
            if (optionalActionsForUser.get(user) == null) {
                optionalActionsForUser.put(user, new ArrayList<Action>());
            }
            optionalActionsForUser.get(user).add(a);
        }
    }

    public void clearActions() {
        requiredActionsForUser.clear();
        optionalActionsForUser.clear();
        numHeartbeats = 0;
    }

    private List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<Role>();
        return roles;
    }

    public Player getPlayer(String username) {
        for (Player p : players) {
            if (p.username.equals(username)) return p;
        }
        return null;
    }

    public Player getPlayer(int id) {
        for (Player p : players) {
            if (p.id == id) return p;
        }
        return null;
    }

    public int getMajorityNumber() {
        int livingPlayers = 0;
        for (Player i : players) {
            if (i.living && i.canVote()) livingPlayers++;
        }
        return (int) (Math.floor(livingPlayers / 2.0) + 1);
    }

    private static String setToString(List<Player> p) {
        String s = null;
        for (Player i : p) {
            if (s == null) s = i.username; else s = s + ", " + i.username;
        }
        return s;
    }

    private int heartbeats;

    private boolean sleepingTime;

    public void startGame() {
        players.clear();
        playerCount = 0;
        currentStage = Stage.SETUP;
        dayCount = 0;
        sleepingTime = false;
        heartbeats = 0;
        mafia.clear();
        gameHistory.clear();
        Date d = new Date();
        gameHistory.add("Game initialized at " + d.toString());
        heartbeats = 0;
    }

    public String getHistory() {
        String appended = "";
        for (String s : gameHistory) {
            appended = appended + s + "<br>";
        }
        return appended;
    }

    public String getTime() {
        if (currentStage == Stage.SETUP) {
            return "Setup Phase";
        }
        if (currentStage == Stage.DAY || currentStage == Stage.NIGHT) {
            return currentStage.toString() + " " + dayCount;
        }
        if (currentStage == Stage.GAMEOVER) {
            return "Game is Over!";
        }
        return "...?!";
    }

    public String getPublicPlayerList() {
        String messages = "== " + getTime() + " == <br>";
        for (Player p : players) {
            messages = messages + p.id + ") " + p.getMessage() + "<br>";
        }
        return messages;
    }

    public String getSpoilerPlayerList() {
        String messages = "== " + getTime() + " == <br>";
        for (Player p : players) {
            messages = messages + p.getSpoilerMessage() + "<br>";
        }
        return messages;
    }

    public void playerIn(String username) {
        if (getPlayer(username) != null) return;
        if (currentStage != Stage.SETUP) return;
        Player p = new Player(this);
        p.username = username;
        p.living = true;
        p.sleeping = false;
        p.alignment = Player.Alignment.INNOCENT;
        p.statusString = null;
        players.add(p);
        playerCount++;
        renumberPlayers();
        communicator.sendPublicMessage(getPublicPlayerList());
    }

    void renumberPlayers() {
        int id = 1;
        for (Player p : players) {
            p.id = id;
            id++;
        }
    }

    public void playerOut(String username) {
        if (getPlayer(username) == null) return;
        if (currentStage != Stage.SETUP) return;
        Player p = getPlayer(username);
        players.remove(p);
        playerCount--;
        renumberPlayers();
        communicator.sendPublicMessage(getPublicPlayerList());
    }

    public void heartbeat() {
        numHeartbeats++;
        if (currentStage == Stage.NIGHT) {
            boolean nightEnd = true;
            for (String u : requiredActionsForUser.keySet()) {
                if (requiredActionsForUser.get(u).size() > 0) nightEnd = false;
            }
            if (nightEnd && numHeartbeats > 4) {
                tryEndNight();
            }
        }
    }

    public void executePossibleActionsForUser(String username, String message, Boolean pub) {
        if (requiredActionsForUser.get(username) != null) {
            for (Action a : requiredActionsForUser.get(username)) {
                if (!(pub ^ a.isPublic()) && message.toLowerCase().startsWith(a.getCommand())) {
                    boolean delete = a.Execute(message);
                    if (delete && requiredActionsForUser.get(username) != null) {
                        requiredActionsForUser.get(username).remove(a);
                        return;
                    }
                }
            }
        }
        if (optionalActionsForUser.get(username) != null) {
            for (Action a : optionalActionsForUser.get(username)) {
                if (!(pub ^ a.isPublic()) && message.toLowerCase().startsWith(a.getCommand())) {
                    boolean delete = a.Execute(message);
                    if (delete && optionalActionsForUser.get(username) != null) {
                        optionalActionsForUser.get(username).remove(a);
                        return;
                    }
                }
            }
        }
    }

    public Map<DefinitionType, ArrayList<RoleDefinition>> roleSetup() {
        RoleDefinition[] rolepool = { new Detective(), new Doctor(), new Driver(), new SexAddict(), new Vigilante() };
        HashMap<DefinitionType, ArrayList<RoleDefinition>> organizedRoles = new HashMap<DefinitionType, ArrayList<RoleDefinition>>();
        for (RoleDefinition r : rolepool) {
            DefinitionType category = r.getDefinitionType();
            if (organizedRoles.get(category) == null) {
                ArrayList<RoleDefinition> list = new ArrayList<RoleDefinition>();
                list.add(r);
                organizedRoles.put(category, list);
            } else {
                ArrayList<RoleDefinition> oldRoles = organizedRoles.get(category);
                oldRoles.add(r);
                organizedRoles.put(category, oldRoles);
            }
        }
        return organizedRoles;
    }

    public void pickRoles() {
        Map<DefinitionType, ArrayList<RoleDefinition>> organizedRoles = roleSetup();
        selectedRoles.clear();
        for (DefinitionType type : DefinitionType.values()) {
            ArrayList<RoleDefinition> possibleRoles = organizedRoles.get(type);
            if (possibleRoles != null && possibleRoles.size() > 0) {
                int index = r.nextInt(possibleRoles.size());
                RoleDefinition picked = possibleRoles.get(index);
                selectedRoles.add(picked);
                communicator.sendPublicMessage("This game's " + type.toString() + " Role is:" + picked.getName());
            }
        }
        List<RoleDefinition> selectedRolesCopy = new ArrayList<RoleDefinition>(selectedRoles);
        int numRolesToAssign = numMafia + 2;
        int mafiaRoles = 0;
        while (numRolesToAssign > 0) {
            int index = r.nextInt(selectedRolesCopy.size());
            RoleDefinition role = selectedRolesCopy.get(index);
            Player availablePlayer = null;
            List<Player> possiblePlayers = new ArrayList<Player>(players);
            while (availablePlayer == null) {
                Player chosenPlayer = possiblePlayers.get(r.nextInt(possiblePlayers.size()));
                if (role.canBeAssigned(chosenPlayer) && chosenPlayer.roles.size() == 0) {
                    if (chosenPlayer.alignment != Alignment.MAFIA) {
                        availablePlayer = chosenPlayer;
                    } else if (mafiaRoles < maxMafiaRoles) {
                        availablePlayer = chosenPlayer;
                        mafiaRoles++;
                    } else possiblePlayers.remove(chosenPlayer);
                } else possiblePlayers.remove(chosenPlayer);
            }
            role.assignRole(this, availablePlayer);
            communicator.sendPrivateMessage(availablePlayer.username, role.explainRole());
            selectedRolesCopy.remove(role);
            numRolesToAssign--;
        }
    }

    public void playersGathered() {
        playerCount = players.size();
        if (playerCount < 8) {
            communicator.sendPublicMessage("<b>God Says</b>: Sorry, not enough players yet.");
            communicator.sendPublicMessage("<b>God Says</b>: Type 'in' if you haven't already!");
            return;
        }
        if (gameStarted) return;
        gameStarted = true;
        communicator.sendPublicMessage("<b>The game has been started. Nobody else may join.</b> :(");
        Date d = new Date();
        gameHistory.add("Game actually started at " + d.toString());
        communicator.sendPublicMessage("<b><font color='red'>Randomizing alignments</font></b>");
        numMafia = 0;
        if (playerCount < 13) numMafia = 2; else if (playerCount < 18) numMafia = 3; else if (playerCount < 23) numMafia = 4;
        if (numMafia == 0) {
            communicator.sendPublicMessage("Too many players! D:");
            return;
        }
        for (int i = 0; i < numMafia; i++) {
            Player p = players.get(r.nextInt(playerCount));
            while (mafia.contains(p)) p = players.get(r.nextInt(playerCount));
            p.alignment = Player.Alignment.MAFIA;
            mafia.add(p);
        }
        String mafiaWelcome = "Welcome to the dark side, and get ready to kill the poor suckers that moved here! The other mafia are: " + setToString(mafia);
        for (Player p : players) {
            if (p.alignment == Alignment.MAFIA) communicator.sendPrivateMessage(p.username, mafiaWelcome);
            if (p.alignment == Alignment.INNOCENT) communicator.sendPrivateMessage(p.username, "It is unfortunate that you have moved to this town. There are mafia about... [You're <b>innocent</b>.]");
        }
        communicator.sendPublicMessage("The mafia has been chosen. Beware those <b>" + numMafia + "</b> individuals!");
        pickRoles();
        startDay(0);
    }

    public void playerSleeps(Player p) {
        if (!p.sleeping && p.living && sleepingTime) {
            p.sleeping = true;
            numSleeping++;
            if (numSleeping >= getMajorityNumber()) {
                startNight(dayCount + 1);
            } else {
                communicator.sendPublicMessage(numSleeping + " players asleep.");
            }
        }
    }

    private void clearVotes() {
        heartbeats = 0;
        for (Player p : players) {
            p.playerVote = -1;
        }
    }

    private void createSleepActions() {
        numSleeping = 0;
        sleepingTime = true;
        for (Player p : players) {
            if (p.living) addAction(p.username, new SleepAction(this, p), false);
        }
        return;
    }

    private void createMafiaActions() {
        for (Player p : mafia) {
            if (p.living) {
                addAction(p.username, new MafiaAction(this, p), true);
            }
        }
        return;
    }

    private void createVoteActions() {
        for (Player p : players) {
            if (p.living) {
                addAction(p.username, new VoteAction(this, p), true);
            }
        }
        return;
    }

    private void startDay(int i) {
        if (currentStage == Stage.DAY) System.err.println("Something went wrong. Start day called during day?");
        currentStage = Stage.DAY;
        dayCount = i;
        for (Player p : players) {
            if (p.living && p.sleeping) {
                p.sleeping = false;
                p.statusString = null;
            }
        }
        clearActions();
        clearVotes();
        heartbeats = 0;
        Date d = new Date();
        gameHistory.add("Day " + dayCount + " started at " + d.toString());
        if (dayCount == 0) {
            communicator.sendPublicMessage("Welcome to day 0! Please type <b>!sleep</b> to go to sleep and get ready for day 0. Please do not talk afterwards.");
            createSleepActions();
            return;
        }
        for (Player p : players) {
            if (p.living) {
                p.startDay();
            }
        }
        communicator.sendPublicMessage(getPublicPlayerList());
        communicator.sendPublicMessage("Welcome to day phase. You may discuss what happened, and vote to execute someone. You may check the actions you have by privately IMing me the word 'actions'");
        createVoteActions();
    }

    private void startWill() {
        if (currentStage == Stage.WILL) System.err.println("Something went wrong. Start will called during will?");
        currentStage = Stage.WILL;
        for (Player p : players) {
            if (!p.living && !p.gaveWill) {
                WillAction will = new WillAction(this, p);
                addAction(p.username, will, true);
                communicator.sendPrivateMessage(p.username, will.getDescription());
            }
        }
    }

    public void tryEndWill() {
        int deadplayers = 0;
        for (Player p : players) {
            if (!p.living) {
                deadplayers++;
            }
        }
        if (deadplayers == numGaveWill) {
            for (Player p : players) {
                if (p.living) p.processWillMarks();
            }
            if (!sleepingTime) startDay(dayCount); else {
                currentStage = Stage.DAY;
                communicator.sendPublicMessage("Please type <b>!sleep</b> to go to sleep! Please do not talk afterwards.");
                clearActions();
                createSleepActions();
            }
        } else return;
    }

    private void startNight(int i) {
        if (currentStage == Stage.NIGHT) System.err.println("Something went wrong. Start night called during night?");
        clearActions();
        clearVotes();
        sleepingTime = false;
        currentStage = Stage.NIGHT;
        dayCount = i;
        heartbeats = 0;
        Date d = new Date();
        gameHistory.add("Night " + dayCount + " started at " + d.toString());
        communicator.sendPublicMessage("It is now night " + dayCount + ". Please do not speak and send your actions privately to me. Note that you can type <b>actions</b> to get a list of actions that you can make.");
        createMafiaActions();
        for (Player p : players) {
            if (p.living) {
                p.startNight();
            }
        }
    }

    public void tryEndDay() {
        Player execution = dayVoteWinner();
        if (execution == null) {
            return;
        }
        Mark executionMark = new Mark();
        executionMark.type = MarkType.EXECUTION;
        executionMark.scope = MarkScope.DAY;
        executionMark.duration = 1;
        executionMark.isBlockable = false;
        executionMark.isRedirectable = false;
        executionMark.isSaveable = true;
        execution.marks.add(executionMark);
        resolutionPhase();
        communicator.sendPublicMessage(getPublicPlayerList());
        updatePlayerCounts();
        for (Player p : players) {
            if (p.living) {
                p.endDay();
            }
        }
        if (checkWin()) return;
        sleepingTime = true;
        startWill();
    }

    private void resolutionPhase() {
        for (Player p : players) {
            p.prepareProcess();
        }
        for (Player p : players) {
            p.processRedirectMarks();
        }
        for (Player p : players) {
            p.processKillMarks();
        }
        for (Player p : players) {
            p.processInvestigationMarks();
        }
        for (Player p : players) {
            p.processDestinyMarks();
        }
        for (Player p : players) {
            p.postProcess();
        }
    }

    public void tryEndNight() {
        Player mafiaKill = mafiaVoteWinner();
        if (mafiaKill == null) {
            for (Player p : mafia) {
                communicator.sendPrivateMessage(p.username, "The mafia vote was inconclusive. Please discuss better and vote again.");
            }
            createMafiaActions();
            return;
        }
        for (Player p : mafia) {
            if (!p.living) continue;
            if (getPlayer(p.playerVote) != mafiaKill) continue;
            Mark mafiaMark = new Mark();
            mafiaMark.type = MarkType.KILL;
            mafiaMark.scope = MarkScope.NIGHT;
            mafiaMark.duration = 1;
            mafiaMark.isBlockable = true;
            mafiaMark.isRedirectable = true;
            mafiaMark.isSaveable = true;
            mafiaMark.source = p;
            mafiaKill.marks.add(mafiaMark);
        }
        resolutionPhase();
        updatePlayerCounts();
        for (Player p : players) {
            if (p.living) {
                p.endNight();
            }
        }
        if (checkWin()) return;
        startWill();
    }

    public boolean checkWin() {
        int numMafia = 0;
        int livingPlayers = 0;
        for (Player p : players) {
            if (p.living && p.alignment == Alignment.MAFIA) numMafia++;
            if (p.living) livingPlayers++;
        }
        if (numMafia == 0 && gameStarted) {
            clearVotes();
            clearActions();
            Date d = new Date();
            gameHistory.add("Innocent win at " + d.toString());
            currentStage = Stage.GAMEOVER;
            communicator.sendPublicMessage("Innocent win!");
            communicator.sendPublicMessage(getSpoilerPlayerList());
            return true;
        }
        if (numMafia * 2 >= livingPlayers && gameStarted) {
            clearVotes();
            clearActions();
            Date d = new Date();
            gameHistory.add("Mafia win at " + d.toString());
            currentStage = Stage.GAMEOVER;
            communicator.sendPublicMessage("Mafia win!");
            communicator.sendPublicMessage(getSpoilerPlayerList());
            return true;
        }
        return false;
    }

    private void updatePlayerCounts() {
        livingPlayers = 0;
        for (Player p : players) {
            if (p.living) livingPlayers++;
        }
    }

    private Player mafiaVoteWinner() {
        Map<Player, Integer> votes = new HashMap<Player, Integer>();
        for (Player p : mafia) {
            Player target = getPlayer(p.playerVote);
            if (target != null) {
                if (!votes.containsKey(target)) votes.put(target, 0);
                votes.put(target, votes.get(target) + 1);
            }
        }
        int max = 0;
        Player maxPlayer = null;
        for (Player p : votes.keySet()) {
            if (votes.get(p) > max) {
                max = votes.get(p);
                maxPlayer = p;
            }
        }
        return maxPlayer;
    }

    private Player dayVoteWinner() {
        updatePlayerCounts();
        Map<Player, Integer> votes = new HashMap<Player, Integer>();
        for (Player p : players) {
            if (!p.living) continue;
            if (!p.canVote()) continue;
            Player target = getPlayer(p.playerVote);
            if (target != null) {
                if (!votes.containsKey(target)) votes.put(target, 0);
                votes.put(target, votes.get(target) + 1);
            }
        }
        String voteString = "";
        int max = 0;
        Player maxPlayer = null;
        for (Player p : votes.keySet()) {
            voteString = voteString + "<b>" + p.username + "</b>: " + votes.get(p) + ", ";
            if (votes.get(p) > max) {
                max = votes.get(p);
                maxPlayer = p;
            }
        }
        if (voteString.length() > 1) communicator.sendPublicMessage("Votes: " + voteString.substring(0, voteString.length() - 2));
        communicator.sendPublicMessage(livingPlayers + " living, " + getMajorityNumber() + " to kill!");
        if (max < getMajorityNumber()) {
            return null;
        }
        return maxPlayer;
    }

    public Player parsePlayer(String string) {
        try {
            int i = Integer.parseInt(string);
            Player p = getPlayer(i);
            if (p != null) return p;
        } catch (NumberFormatException e) {
        }
        Player p = getPlayer(string);
        return p;
    }

    public void printVotes() {
        Map<Player, Integer> votes = new HashMap<Player, Integer>();
        for (Player p : players) {
            if (!p.living) continue;
            if (!p.canVote()) continue;
            Player target = getPlayer(p.playerVote);
            if (target != null) {
                if (!votes.containsKey(target)) votes.put(target, 0);
                votes.put(target, votes.get(target) + 1);
            }
        }
        String voteString = "";
        for (Player p : votes.keySet()) {
            voteString = voteString + "<b>" + p.username + "</b>: " + votes.get(p) + ", ";
        }
        if (voteString.length() > 1) communicator.sendPublicMessage("Votes: " + voteString.substring(0, voteString.length() - 2));
    }

    public void savePlayer(Player player) {
        player.statusString = "<font color='blue'>Saved</font>";
    }

    public void killPlayer(Player player) {
        player.statusString = "<font color='red'>Killed</font>";
        player.living = false;
    }

    public void executePlayer(Player player) {
        player.statusString = "<font color='red'>Executed</font>";
        player.living = false;
    }
}
