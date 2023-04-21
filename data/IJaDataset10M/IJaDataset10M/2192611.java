package mysterychess.network;

import java.awt.Point;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mysterychess.model.Advisor;
import mysterychess.model.Cannon;
import mysterychess.model.Chariot;
import mysterychess.model.ChessType;
import mysterychess.model.Elephant;
import mysterychess.model.General;
import mysterychess.model.Horse;
import mysterychess.model.Match;
import mysterychess.model.Piece;
import mysterychess.model.PieceName;
import mysterychess.model.Role;
import mysterychess.model.Soldier;
import mysterychess.model.SuperAdvisor;
import mysterychess.model.SuperElephant;
import mysterychess.model.Team;
import mysterychess.model.Team.TeamColor;
import mysterychess.network.dto.PieceDto;
import mysterychess.network.dto.TableDto;
import mysterychess.util.Util;

/**
 *
 * @author Tin Bui-Huy
 */
public class RmiClient extends AbstractHost implements ClientCallback {

    private static final int MAX_RECONNECT_TIME = 30;

    private int port;

    private String host;

    private Match match;

    private Chatter chater;

    private int reconnectTime = 0;

    private RemoteServer server;

    public RmiClient(String host, int port, Chatter chater) {
        this.host = host;
        this.port = port;
        this.chater = chater;
    }

    public Match startup() throws RemoteException, java.net.UnknownHostException {
        register();
        TableDto table = server.joinGame(InetAddress.getLocalHost().getHostAddress());
        if (table != null) {
            match = new Match();
            setTimeLimits();
            setMatchData(table);
            setReady();
            super.init(match, chater);
        }
        return match;
    }

    private void setMatchData(TableDto data) {
        Team black = createTeam(data.blackTeam, data.chessType);
        Team white = createTeam(data.whiteTeam, data.chessType);
        if (data.myTeam == Team.TeamColor.BLACK) {
            black.setPosition(Team.TeamPosition.BOTTOM);
            white.setPosition(Team.TeamPosition.TOP);
        } else {
            black.setPosition(Team.TeamPosition.TOP);
            white.setPosition(Team.TeamPosition.BOTTOM);
        }
        black.setColor(Team.TeamColor.BLACK);
        white.setColor(Team.TeamColor.WHITE);
        Team activeTeam = data.activeTeam == Team.TeamColor.WHITE ? white : black;
        match.setData(data.chessType, white, black, activeTeam);
    }

    private Team createTeam(PieceDto[] ps, ChessType chessType) {
        Team team = new Team();
        List<Piece> pieces = new ArrayList<Piece>();
        boolean mystery = (chessType == ChessType.MYSTERY_CHESS) ? true : false;
        for (PieceDto p : ps) {
            Piece p1 = new Piece(team, Util.transform(p.pos), createRole(p.currentType, false), createRole(p.actualType, mystery), p.turnUped);
            pieces.add(p1);
        }
        team.setPieces(pieces);
        return team;
    }

    private Role createRole(PieceName name, Boolean mysteryRole) {
        Role r = null;
        switch(name) {
            case advisor:
                if (mysteryRole) {
                    r = new SuperAdvisor();
                } else {
                    r = new Advisor();
                }
                break;
            case cannon:
                r = new Cannon();
                break;
            case chariot:
                r = new Chariot();
                break;
            case elephant:
                if (mysteryRole) {
                    r = new SuperElephant();
                } else {
                    r = new Elephant();
                }
                break;
            case general:
                r = new General();
                break;
            case horse:
                r = new Horse();
                break;
            case soldier:
                r = new Soldier();
                break;
        }
        return r;
    }

    protected void createNewGame(ChessType type, TeamColor bottomTeam) {
        try {
            server.setGameLimitTime(Util.GAME_EXPIRE_TIME);
            server.setPieceMoveLimitTime(Util.PIECE_MOVE_EXPIRE_TIME);
            server.createTable(type, bottomTeam);
        } catch (RemoteException ex) {
            Logger.getLogger(RmiClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setTimeLimits() {
        try {
            Util.PIECE_MOVE_EXPIRE_TIME = server.getPieceMoveLimitTime();
            Util.GAME_EXPIRE_TIME = server.getGameLimitTime();
        } catch (RemoteException ex) {
            Logger.getLogger(RmiClient.class.getName()).log(Level.SEVERE, "Fail to get time limit constants", ex);
        }
    }

    private void register() throws RemoteException {
        try {
            Logger.getLogger(RmiClient.class.getName()).entering(this.getClass().getName(), "Connecting to server: " + host);
            Registry registry = LocateRegistry.getRegistry(host, new Integer(port));
            server = (RemoteServer) (registry.lookup(Util.RMI_SERVER_NAME));
            UnicastRemoteObject.exportObject(this, 0);
            server.registerClientCallback(this, Util.getVersion());
            Logger.getLogger(RmiClient.class.getName()).exiting(this.getClass().getName(), "Connected to server: " + host);
        } catch (RemoteException re) {
            throw re;
        } catch (Exception e) {
            System.out.println("" + e);
            Logger.getLogger(RmiClient.class.getName()).severe(e.getMessage());
            throw new RemoteException("Fail to connect to server");
        }
    }

    /**
     * This method is called remotely.
     *
     * @param table
     * @throws RemoteException
     */
    public void setTable(TableDto table) throws RemoteException {
        setTimeLimits();
        setMatchData(table);
        setReady();
    }

    /**
     * This method is called remotely.
     *
     * @throws RemoteException
     */
    public void ready() throws RemoteException {
        ready = true;
        match.setEnabled(true);
    }

    private void setReady() {
        ready = true;
        match.setEnabled(true);
        try {
            server.setClientReady();
        } catch (RemoteException ex) {
            Logger.getLogger(RmiClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public CommonRemote getOtherSide() {
        return server;
    }

    @Override
    public void removeOtherSide() {
        server = null;
    }

    @Override
    protected Point transform(Point p) {
        return Util.transform(p);
    }

    public void loadTable(TableDto t) throws RemoteException {
        t.myTeam = TeamColor.getOtherTeam(t.myTeam);
        server.loadTable(t);
    }

    /**
     * This method will be called from server to check if the client is still alive.
     *
     * @return
     * @throws RemoteException
     */
    public boolean isAlive() throws RemoteException {
        return true;
    }
}
