package net.jetrix;

import static net.jetrix.GameState.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;
import java.net.*;
import net.jetrix.config.*;
import net.jetrix.filter.*;
import net.jetrix.messages.*;
import net.jetrix.messages.channel.*;
import net.jetrix.messages.channel.specials.*;
import net.jetrix.winlist.*;
import net.jetrix.clients.TetrinetClient;

/**
 * Game channel.
 *
 * @author Emmanuel Bourg
 * @version $Revision: 868 $, $Date: 2010-08-27 07:41:18 -0400 (Fri, 27 Aug 2010) $
 */
public class Channel extends Thread implements Destination {

    /** The maximum number of players per channel. */
    public static final int MAX_PLAYERS = 6;

    private ChannelConfig channelConfig;

    private ServerConfig serverConfig;

    private Logger log = Logger.getLogger("net.jetrix");

    private BlockingQueue<Message> queue = new LinkedBlockingQueue<Message>();

    private boolean open;

    private GameState gameState = STOPPED;

    private boolean running = true;

    private GameResult result;

    /** The start time of the channel */
    private long startTime;

    /** set of clients connected to this channel */
    private Set<Client> clients = new HashSet<Client>();

    /** slot/player mapping */
    private List<Client> slots = Arrays.asList(new Client[MAX_PLAYERS]);

    private Field[] fields = new Field[MAX_PLAYERS];

    private List<MessageFilter> filters = new ArrayList<MessageFilter>();

    public Channel() {
        this(new ChannelConfig());
    }

    public Channel(ChannelConfig channelConfig) {
        this.channelConfig = channelConfig;
        this.serverConfig = Server.getInstance().getConfig();
        for (int i = 0; i < MAX_PLAYERS; i++) {
            fields[i] = new Field();
        }
        if (serverConfig != null) {
            Iterator<FilterConfig> globalFilters = serverConfig.getGlobalFilters();
            while (globalFilters.hasNext()) {
                addFilter(globalFilters.next());
            }
        }
        Iterator<FilterConfig> channelFilters = channelConfig.getFilters();
        while (channelFilters.hasNext()) {
            addFilter(channelFilters.next());
        }
    }

    /**
     * Enable a new filter for this channel.
     */
    public void addFilter(FilterConfig filterConfig) {
        FilterManager filterManager = FilterManager.getInstance();
        MessageFilter filter;
        try {
            if (filterConfig.getClassname() != null) {
                filter = filterManager.getFilter(filterConfig.getClassname());
            } else {
                filter = filterManager.getFilterByName(filterConfig.getName());
            }
            filter.setChannel(this);
            filter.setConfig(filterConfig);
            filter.init();
            filters.add(filter);
        } catch (FilterException e) {
            log.log(Level.WARNING, e.getMessage(), e);
        }
    }

    public void removeFilter(String filterName) {
    }

    public Iterator<MessageFilter> getFilters() {
        return filters.iterator();
    }

    /**
     * Returns the filters applied to this channel not defined at the server level.
     */
    public Collection<MessageFilter> getLocalFilters() {
        Collection<MessageFilter> localFilters = new ArrayList<MessageFilter>();
        for (MessageFilter filter : filters) {
            if (!filter.getConfig().isGlobal()) {
                localFilters.add(filter);
            }
        }
        return localFilters;
    }

    /**
     * Main loop. The channel listens for incoming messages until the server
     * or the channel closes. Every message is first passed through the
     * registered filters and then handled by the channel.
     */
    public void run() {
        log.info("Channel " + channelConfig.getName() + " opened");
        startTime = System.currentTimeMillis();
        while (running && serverConfig.isRunning() && (getConfig().isPersistent() || !clients.isEmpty() || (System.currentTimeMillis() - startTime < 200))) {
            LinkedList<Message> list = new LinkedList<Message>();
            try {
                list.add(queue.take());
                for (MessageFilter filter : filters) {
                    int size = list.size();
                    for (int i = 0; i < size; i++) {
                        filter.process(list.removeFirst(), list);
                    }
                }
                while (!list.isEmpty()) {
                    process(list.removeFirst());
                }
            } catch (Exception e) {
                log.log(Level.WARNING, e.getMessage(), e);
            }
        }
        if (serverConfig.isRunning()) {
            ChannelManager.getInstance().removeChannel(this);
        }
        log.info("Channel " + channelConfig.getName() + " closed");
    }

    /**
     * Stop the channel.
     */
    public void close() {
        running = false;
        queue.add(new ShutdownMessage());
    }

    private void process(CommandMessage m) {
        Server.getInstance().send(m);
    }

    private void process(TeamMessage m) {
        Client client = (Client) m.getSource();
        if (client.getUser().isPlayer()) {
            int slot = m.getSlot();
            getPlayer(slot).setTeam(m.getName());
            sendAll(m, slot);
        }
    }

    private void process(GmsgMessage m) {
        sendAll(m);
    }

    private void process(PlineMessage m) {
        int slot = m.getSlot();
        String text = m.getText();
        if (!text.startsWith("/")) {
            sendAll(m, slot);
        }
    }

    private void process(PlineActMessage m) {
        int slot = m.getSlot();
        if (m.getSource() == null) {
            sendAll(m);
        } else {
            sendAll(m, slot);
        }
    }

    private void process(SmsgMessage m) {
        if (m.isPrivate()) {
            for (Client client : clients) {
                if (client.getUser().isSpectator() && client != m.getSource()) {
                    client.send(m);
                }
            }
        } else {
            sendAll(m);
        }
    }

    private void process(PauseMessage m) {
        gameState = PAUSED;
        if (m.getSource() instanceof Client) {
            Client client = (Client) m.getSource();
            sendAll(new PlineMessage("channel.game.paused-by", client.getUser().getName()));
        }
        sendAll(m);
    }

    private void process(ResumeMessage m) {
        gameState = STARTED;
        if (m.getSource() instanceof Client) {
            Client client = (Client) m.getSource();
            sendAll(new PlineMessage("channel.game.resumed-by", client.getUser().getName()));
        }
        sendAll(m);
    }

    private void process(PlayerWonMessage m) {
        if (m.getSource() == null) {
            sendAll(m);
        }
    }

    private void process(PlayerLostMessage m) {
        int slot = m.getSlot();
        Client client = getClient(slot);
        User user = client.getUser();
        sendAll(m);
        boolean wasPlaying = user.isPlaying();
        user.setPlaying(false);
        if (wasPlaying) {
            result.update(user, false);
        }
        if (wasPlaying && countRemainingTeams() <= 1) {
            Message endgame = new EndGameMessage();
            send(endgame);
            result.setEndTime(new Date());
            slot = 0;
            for (int i = 0; i < slots.size(); i++) {
                client = slots.get(i);
                if (client != null && client.getUser().isPlaying()) {
                    slot = i + 1;
                    result.update(client.getUser(), true);
                }
            }
            if (slot != 0) {
                PlayerWonMessage playerwon = new PlayerWonMessage();
                playerwon.setSlot(slot);
                send(playerwon);
                User winner = getPlayer(slot);
                PlineMessage announce = new PlineMessage();
                if (winner.getTeam() == null) {
                    announce.setKey("channel.player_won", winner.getName());
                } else {
                    announce.setKey("channel.team_won", winner.getTeam());
                }
                send(announce);
            }
            Winlist winlist = WinlistManager.getInstance().getWinlist(channelConfig.getWinlistId());
            if (winlist != null) {
                winlist.saveGameResult(result);
                List<Score> topScores = winlist.getScores(0, 10);
                WinlistMessage winlistMessage = new WinlistMessage();
                winlistMessage.setScores(topScores);
                sendAll(winlistMessage);
            }
            serverConfig.getStatistics().increaseGameCount();
        }
    }

    private void process(SpecialMessage m) {
        if (channelConfig.getSettings().getLinesPerSpecial() > 0) {
            int slot = m.getFromSlot();
            sendAll(m, slot);
        }
    }

    private void process(LevelMessage m) {
        sendAll(m);
    }

    private void process(FieldMessage m) {
        int slot = m.getSlot();
        fields[slot - 1].update(m);
        if (m.getSource() != null) {
            sendAll(m, slot);
        } else {
            if (m.isFullUpdate()) {
                m.setField(fields[slot - 1].getFieldString());
            }
            sendAll(m);
        }
    }

    private void process(StartGameMessage m) {
        if (gameState == STOPPED) {
            gameState = STARTED;
            if (m.getSource() instanceof Client) {
                Client client = (Client) m.getSource();
                sendAll(new PlineMessage("channel.game.started-by", client.getUser().getName()));
            }
            result = new GameResult();
            result.setStartTime(new Date());
            result.setChannel(this);
            for (Client client : slots) {
                if (client != null && client.getUser().isPlayer()) {
                    client.getUser().setPlaying(true);
                }
            }
            for (int i = 0; i < MAX_PLAYERS; i++) {
                fields[i].clear();
            }
            NewGameMessage newgame = new NewGameMessage();
            newgame.setSlot(m.getSlot());
            newgame.setSettings(channelConfig.getSettings());
            if (channelConfig.getSettings().getSameBlocks()) {
                Random random = new Random();
                newgame.setSeed(random.nextInt());
            }
            sendAll(newgame);
        }
    }

    private void process(StopGameMessage m) {
        EndGameMessage end = new EndGameMessage();
        end.setSlot(m.getSlot());
        end.setSource(m.getSource());
        send(end);
    }

    private void process(EndGameMessage m) {
        if (gameState != STOPPED) {
            if (m.getSource() instanceof Client) {
                Client client = (Client) m.getSource();
                sendAll(new PlineMessage("channel.game.stopped-by", client.getUser().getName()));
            }
            gameState = STOPPED;
            sendAll(m);
            for (Client client : slots) {
                if (client != null) {
                    client.getUser().setPlaying(false);
                }
            }
        }
    }

    private void process(DisconnectedMessage m) {
        Client client = m.getClient();
        removeClient(client);
        sendAll(new PlineMessage("channel.disconnected", client.getUser().getName()));
    }

    private void process(LeaveMessage m) {
        removeClient((Client) m.getSource());
    }

    private void process(AddPlayerMessage m) {
        Client client = m.getClient();
        Channel previousChannel = client.getChannel();
        client.setChannel(this);
        if (previousChannel != null && !client.supportsMultipleChannels()) {
            for (int j = 1; j <= MAX_PLAYERS; j++) {
                if (previousChannel.getPlayer(j) != null) {
                    LeaveMessage clear = new LeaveMessage();
                    clear.setSlot(j);
                    client.send(clear);
                }
            }
            previousChannel.removeClient(client);
            PlineMessage announce = new PlineMessage();
            announce.setKey("channel.join_notice", client.getUser().getName(), channelConfig.getName());
            previousChannel.send(announce);
            if (client.getUser().isPlaying()) {
                client.getUser().setPlaying(false);
                client.send(new EndGameMessage());
            }
        }
        clients.add(client);
        if (client.getUser().isSpectator()) {
            JoinMessage mjoin = new JoinMessage();
            mjoin.setName(client.getUser().getName());
            sendAll(mjoin);
            PlayerNumMessage mnum = new PlayerNumMessage(1);
            client.send(mnum);
        } else {
            int slot = 0;
            for (slot = 0; slot < slots.size() && slots.get(slot) != null; slot++) ;
            if (slot >= MAX_PLAYERS) {
                log.warning("[" + getConfig().getName() + "] Panic, no slot available for " + client);
                client.getUser().setSpectator();
            } else {
                slots.set(slot, client);
                JoinMessage mjoin = new JoinMessage();
                mjoin.setSlot(slot + 1);
                mjoin.setName(client.getUser().getName());
                sendAll(mjoin, client);
                PlayerNumMessage mnum = new PlayerNumMessage(slot + 1);
                client.send(mnum);
            }
            updateChannelOperator();
        }
        if (client.getUser().isSpectator()) {
            sendSpectatorList(client);
        }
        for (int i = 0; i < slots.size(); i++) {
            Client resident = slots.get(i);
            if (resident != null && resident != client) {
                JoinMessage mjoin2 = new JoinMessage();
                mjoin2.setChannelName(getConfig().getName());
                mjoin2.setSlot(i + 1);
                mjoin2.setName(resident.getUser().getName());
                client.send(mjoin2);
                TeamMessage mteam = new TeamMessage();
                mteam.setChannelName(getConfig().getName());
                mteam.setSource(resident);
                mteam.setSlot(i + 1);
                mteam.setName(resident.getUser().getTeam());
                client.send(mteam);
            }
        }
        for (int i = 0; i < MAX_PLAYERS; i++) {
            if (!fields[i].isEmpty() || previousChannel != null) {
                FieldMessage message = new FieldMessage(i + 1, fields[i].getFieldString());
                client.send(message);
            }
        }
        Winlist winlist = WinlistManager.getInstance().getWinlist(channelConfig.getWinlistId());
        if (winlist != null) {
            List<Score> topScores = winlist.getScores(0, 10);
            WinlistMessage winlistMessage = new WinlistMessage();
            winlistMessage.setScores(topScores);
            client.send(winlistMessage);
        }
        PlineMessage mwelcome = new PlineMessage();
        mwelcome.setKey("channel.welcome", client.getUser().getName(), channelConfig.getName());
        client.send(mwelcome);
        if (channelConfig.getTopic() != null) {
            BufferedReader topic = new BufferedReader(new StringReader(channelConfig.getTopic()));
            try {
                String line;
                while ((line = topic.readLine()) != null) {
                    PlineMessage message = new PlineMessage();
                    message.setText("<kaki>" + line);
                    client.send(message);
                }
                topic.close();
            } catch (Exception e) {
                log.log(Level.WARNING, e.getMessage(), e);
            }
        }
        if (client.getUser().isPlayer()) {
            sendSpectatorList(client);
        }
        if (gameState != STOPPED) {
            IngameMessage ingame = new IngameMessage();
            ingame.setChannelName(getConfig().getName());
            client.send(ingame);
            if (gameState == PAUSED) {
                client.send(new PauseMessage());
            }
        }
        if (client instanceof TetrinetClient) {
            int timeout = getConfig().isIdleAllowed() ? 0 : serverConfig.getTimeout() * 1000;
            try {
                ((TetrinetClient) client).getSocket().setSoTimeout(timeout);
            } catch (SocketException e) {
                log.log(Level.WARNING, "Unable to change the timeout", e);
            }
        }
    }

    /**
     * Send the list of spectators in this channel to the specified client.
     */
    private void sendSpectatorList(Client client) {
        List<String> specnames = new ArrayList<String>();
        for (Client c : clients) {
            if (c.getUser().isSpectator()) {
                specnames.add(c.getUser().getName());
            }
        }
        if (!specnames.isEmpty()) {
            SpectatorListMessage spectators = new SpectatorListMessage();
            spectators.setDestination(client);
            spectators.setChannel(getConfig().getName());
            spectators.setSpectators(specnames);
            client.send(spectators);
        }
    }

    public void process(PlayerSwitchMessage m) {
        if (gameState == STOPPED) {
            Client player1 = getClient(m.getSlot1());
            Client player2 = getClient(m.getSlot2());
            slots.set(m.getSlot1() - 1, player2);
            slots.set(m.getSlot2() - 1, player1);
            if (player1 != null) {
                LeaveMessage leave1 = new LeaveMessage();
                leave1.setSlot(m.getSlot1());
                sendAll(leave1);
            }
            if (player2 != null) {
                LeaveMessage leave2 = new LeaveMessage();
                leave2.setSlot(m.getSlot2());
                sendAll(leave2);
            }
            if (player1 != null) {
                JoinMessage mjoin = new JoinMessage();
                mjoin.setSlot(m.getSlot2());
                mjoin.setName(player1.getUser().getName());
                sendAll(mjoin);
                PlayerNumMessage mnum = new PlayerNumMessage(m.getSlot2());
                player1.send(mnum);
            }
            if (player2 != null) {
                JoinMessage mjoin = new JoinMessage();
                mjoin.setSlot(m.getSlot1());
                mjoin.setName(player2.getUser().getName());
                sendAll(mjoin);
                PlayerNumMessage mnum = new PlayerNumMessage(m.getSlot1());
                player2.send(mnum);
            }
            updateChannelOperator();
        }
    }

    public void process(Message m) {
        if (log.isLoggable(Level.FINEST)) {
            log.finest("[" + channelConfig.getName() + "] Processing " + m.getClass().getSimpleName() + " from " + m.getSource());
        }
        if (m instanceof CommandMessage) process((CommandMessage) m); else if (m instanceof FieldMessage) process((FieldMessage) m); else if (m instanceof SpecialMessage) process((SpecialMessage) m); else if (m instanceof LevelMessage) process((LevelMessage) m); else if (m instanceof PlayerLostMessage) process((PlayerLostMessage) m); else if (m instanceof PlayerWonMessage) process((PlayerWonMessage) m); else if (m instanceof TeamMessage) process((TeamMessage) m); else if (m instanceof PlineMessage) process((PlineMessage) m); else if (m instanceof GmsgMessage) process((GmsgMessage) m); else if (m instanceof SmsgMessage) process((SmsgMessage) m); else if (m instanceof PlineActMessage) process((PlineActMessage) m); else if (m instanceof PauseMessage) process((PauseMessage) m); else if (m instanceof ResumeMessage) process((ResumeMessage) m); else if (m instanceof StartGameMessage) process((StartGameMessage) m); else if (m instanceof StopGameMessage) process((StopGameMessage) m); else if (m instanceof EndGameMessage) process((EndGameMessage) m); else if (m instanceof DisconnectedMessage) process((DisconnectedMessage) m); else if (m instanceof PlayerSwitchMessage) process((PlayerSwitchMessage) m); else if (m instanceof LeaveMessage) process((LeaveMessage) m); else if (m instanceof AddPlayerMessage) process((AddPlayerMessage) m); else {
            if (log.isLoggable(Level.FINEST)) {
                log.finest("[" + channelConfig.getName() + "] Message not processed " + m);
            }
        }
    }

    /**
     * Remove the specified client from the channel.
     */
    public void removeClient(Client client) {
        if (client != null) {
            clients.remove(client);
            LeaveMessage leave = new LeaveMessage();
            leave.setName(client.getUser().getName());
            int slot = slots.indexOf(client);
            if (slot != -1) {
                slots.set(slot, null);
                leave.setSlot(slot + 1);
            }
            if (gameState != STOPPED && client.getUser().isPlaying()) {
                result.update(client.getUser(), false);
            }
            sendAll(leave);
        }
        if (!isEmpty()) {
            updateChannelOperator();
        }
        if (isEmpty() && running) {
            gameState = STOPPED;
        }
        if (clients.isEmpty() && !getConfig().isPersistent()) {
            send(new ShutdownMessage());
        }
    }

    /**
     * Add a message to the channel message queue.
     *
     * @param message message to add
     */
    public void send(Message message) {
        queue.add(message);
    }

    /**
     * Send a message to all players in this channel.
     *
     * @param message the message to send
     */
    private void sendAll(Message message) {
        if (message.getDestination() == null) {
            message.setDestination(this);
        }
        for (Client client : clients) {
            client.send(message);
        }
    }

    /**
     * Send a message to all players but the one in the specified slot.
     *
     * @param message the message to send
     * @param slot    the slot to exclude
     */
    private void sendAll(Message message, int slot) {
        Client client = getClient(slot);
        sendAll(message, client);
    }

    /**
     * Send a message to all players but the specified client.
     *
     * @param message the message to send
     * @param c       the client to exclude
     */
    private void sendAll(Message message, Client c) {
        if (message.getDestination() == null) {
            message.setDestination(this);
        }
        for (Client client : clients) {
            if (client != c) {
                client.send(message);
            }
        }
    }

    /**
     * Tell if the channel can accept more players.
     *
     * @return <tt>true</tt> if the channel is full, <tt>false</tt> if not
     */
    public boolean isFull() {
        return getPlayerCount() >= channelConfig.getMaxPlayers();
    }

    public boolean isEmpty() {
        return getPlayerCount() == 0;
    }

    /**
     * Returns the number of players currently in this channel.
     *
     * @return player count
     */
    public int getPlayerCount() {
        int count = 0;
        for (Client client : slots) {
            if (client != null) {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns the channel configuration.
     */
    public ChannelConfig getConfig() {
        return channelConfig;
    }

    /**
     * Returns the game state.
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Finds the slot used in the channel by the specified client.
     */
    public int getClientSlot(Client client) {
        return (slots.indexOf(client) + 1);
    }

    /**
     * Returns the client in the specified slot.
     *
     * @param slot slot number between 1 and 6
     *
     * @return <tt>null</tt> if there is no client in the specified slot, or if the number is out of range
     */
    public Client getClient(int slot) {
        Client client = null;
        if (slot >= 1 && slot <= slots.size()) {
            client = slots.get(slot - 1);
        }
        return client;
    }

    /**
     * Returns the client in the specified slot.
     *
     * @param slot slot number between 1 and 6
     *
     * @return <tt>null</tt> if there is no client in the specified slot, or if the number is out of range
     */
    public User getPlayer(int slot) {
        Client client = getClient(slot);
        return (client != null) ? client.getUser() : null;
    }

    /**
     * Return an iterator of players in this channel.
     */
    public Iterator<Client> getPlayers() {
        return slots.iterator();
    }

    /**
     * Return an iterator of spectators observing this channel.
     */
    public Iterator<Client> getSpectators() {
        List<Client> spectators = new ArrayList<Client>();
        for (Client client : clients) {
            if (client.getUser().isSpectator()) {
                spectators.add(client);
            }
        }
        return spectators.iterator();
    }

    /**
     * Count how many teams are still fighting for victory. A teamless player
     * is considered as a separate team. The game ends when there is one team
     * left in game OR when the last player loose if only one team took part
     * in the game.
     *
     * @return number of teams still playing
     */
    private int countRemainingTeams() {
        Map<String, String> playingTeams = new HashMap<String, String>();
        int nbTeamsLeft = 0;
        for (Client client : slots) {
            if (client != null && client.getUser().isPlaying()) {
                String team = client.getUser().getTeam();
                if (team == null) {
                    nbTeamsLeft++;
                } else {
                    playingTeams.put(team, team);
                }
            }
        }
        return nbTeamsLeft + playingTeams.size();
    }

    /**
     * Return the field of the specified slot.
     */
    public Field getField(int slot) {
        return fields[slot];
    }

    /**
     * Promote the first player in the channel to the channel operator access
     * level if necessary, and demote the former channel operator to the
     * player access level.
     *
     * @since 0.3
     */
    private void updateChannelOperator() {
        boolean firstFound = false;
        for (Client client : slots) {
            if (client != null) {
                User user = client.getUser();
                if (user.getAccessLevel() == AccessLevel.PLAYER && !firstFound) {
                    user.setAccessLevel(AccessLevel.CHANNEL_OPERATOR);
                } else if (user.getAccessLevel() == AccessLevel.CHANNEL_OPERATOR && firstFound) {
                    user.setAccessLevel(AccessLevel.PLAYER);
                }
                firstFound = true;
            }
        }
    }
}
