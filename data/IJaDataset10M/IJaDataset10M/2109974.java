package atan.model;

public abstract class Team {

    private SServerPlayer[] players = new SServerPlayer[11];

    private String teamName;

    private int port = 6000;

    private String hostname = "localhost";

    public Team(String teamName) {
        this(teamName, 6000, "localhost");
    }

    public Team(String teamName, int port, String hostname) {
        this.teamName = teamName;
        this.port = port;
        this.hostname = hostname;
        createNewPlayers();
    }

    public String getTeamName() {
        return teamName;
    }

    public abstract Controller getNewController(int i);

    public void createNewPlayers() {
        for (int i = 0; i < size(); i++) {
            players[i] = new SServerPlayer(teamName, getNewController(i), port, hostname);
        }
    }

    public void connectAll() {
        for (int i = 0; i < size(); i++) {
            try {
                players[i].connect();
            } catch (Exception ex) {
                players[i].handleError(ex.getMessage());
            }
            pause(500);
        }
    }

    public void connect(int index) {
        try {
            players[index].connect();
        } catch (Exception ex) {
            players[index].handleError(ex.getMessage());
        }
        pause(500);
    }

    public SServerPlayer getPlayer(int i) {
        return players[i];
    }

    public int size() {
        return 11;
    }

    private synchronized void pause(int ms) {
        try {
            this.wait(ms);
        } catch (InterruptedException ex) {
        }
    }
}
