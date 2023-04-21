package gomoku.NetServer;

import gomoku.Controller.GoMokuGameLogic;
import gomoku.Model.Point;
import java.io.IOException;
import java.net.*;

public class NetClient {

    private int m_clientID;

    private Socket m_Socket;

    private GoMokuServer m_topServer;

    private String m_Name;

    private NetGame m_gameSession;

    private boolean m_isInitiator;

    private boolean m_confimingProposal;

    private Thread m_Thread;

    private GomokuObjectSerializer gomokuSer;

    public NetClient(int clientID, Socket clientSocket, GoMokuServer topServer) throws IOException {
        m_clientID = clientID;
        m_Socket = clientSocket;
        m_topServer = topServer;
        m_Name = "";
        m_gameSession = null;
        m_confimingProposal = false;
        gomokuSer = new GomokuObjectSerializer(clientSocket);
    }

    public int getClientID() {
        return m_clientID;
    }

    public String getClientUsername() {
        return m_Name;
    }

    public String getClientAddress() {
        return m_Socket.getRemoteSocketAddress().toString();
    }

    public Socket getSocket() {
        return m_Socket;
    }

    public String getClientFullName() {
        String address = "";
        try {
            if (m_Socket != null) {
                address = m_Socket.getRemoteSocketAddress().toString();
            }
        } catch (Exception ex) {
        }
        return String.format("%d %s (%s)", m_clientID, m_Name, address);
    }

    public boolean confirmGameWith(NetClient requestingClient) {
        boolean clientConfirmed = false;
        m_confimingProposal = true;
        try {
            System.out.println("confirming client " + requestingClient.getClientUsername());
            gomokuSer.writeObject(requestingClient.getClientUsername());
            if (m_Thread != null) {
                m_Thread.join();
            }
            Boolean b = (Boolean) (gomokuSer.readObject());
            clientConfirmed = b.booleanValue();
        } catch (Exception e) {
            System.out.println("cofirm failed " + e.toString() + " " + e.getMessage());
        } finally {
            m_confimingProposal = clientConfirmed;
            return clientConfirmed;
        }
    }

    public void WaitForInitiation() {
        try {
            m_Name = gomokuSer.readObject().toString();
            final NetClient thisClient = this;
            m_Thread = new Thread(new Runnable() {

                public void run() {
                    try {
                        if (waitForInitiation()) {
                            playGame();
                        } else {
                            Object o = gomokuSer.readObject();
                            System.out.print("===================" + o.toString());
                            System.out.println("stopped waiting for oponents " + thisClient.getClientFullName());
                        }
                    } catch (Exception e) {
                        System.err.println("waiting failed " + e.getMessage() + " " + thisClient.getClientFullName() + " " + e.toString() + " " + e.getMessage());
                        m_topServer.disconnectClient(thisClient, e);
                        Terminate();
                    }
                }
            });
            m_Thread.start();
        } catch (Exception e) {
            m_topServer.disconnectClient(this, e);
            Terminate();
        }
    }

    protected boolean waitForInitiation() throws IOException, ClassNotFoundException {
        boolean oponentChosen = false;
        int oponentID = -1;
        if (!m_confimingProposal) {
            System.out.println(getClientID() + " waiting for oponent " + this.getClientFullName());
            String oponent = gomokuSer.readObject().toString();
            System.out.println(getClientID() + " got oponent details " + oponent);
            oponentID = parseOponentID(oponent);
            if (oponentID != -1) {
                oponentChosen = m_topServer.startGameWith(this, oponentID);
                System.out.println(getClientID() + " sending from server to game " + oponentChosen);
                gomokuSer.writeObject(new Boolean(oponentChosen));
                System.out.println(getClientID() + " sent from server to game " + oponentChosen);
                m_isInitiator = oponentChosen;
                if (oponentChosen) {
                    gomokuSer.writeObject(m_gameSession.getGameSession().getPlayer(GoMokuGameLogic.BLACK_PLAYER_INDEX).getName());
                } else {
                    oponentID = -1;
                }
            }
        }
        return oponentChosen;
    }

    protected void playGame() throws IOException, ClassNotFoundException {
        GoMokuGameLogic game = m_gameSession.getGameSession();
        System.out.println("starting game " + this.getClientFullName());
        while (m_gameSession != null && m_gameSession.isGameInSession()) {
            if ((m_isInitiator && game.getCurrPlayer().getName().compareToIgnoreCase("White") == 0) || (!m_isInitiator && game.getCurrPlayer().getName().compareToIgnoreCase("Black") == 0)) {
                System.out.println("turn of " + m_isInitiator + " " + this.getClientFullName());
                sendBoard(game);
                System.out.println("board sent to " + m_isInitiator + " " + this.getClientFullName());
                Point move = (Point) gomokuSer.readObject();
                Boolean moveWasPerformed = game.makeMove(move);
                System.out.println("got move " + (moveWasPerformed ? "legal " : "ilegal ") + move.x + " " + move.y + "from " + m_isInitiator + " " + this.getClientFullName());
                System.out.println("move " + move.toString() + "was made by " + m_isInitiator + " " + this.getClientFullName());
                sendBoard(game);
                System.out.println("board2 sent to " + m_isInitiator + " " + this.getClientFullName());
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
            }
        }
        m_gameSession.EndGame();
    }

    public void Terminate() {
        try {
            this.m_Thread.interrupt();
        } catch (Exception ex) {
        }
        try {
            m_Socket.close();
        } catch (Exception ex) {
        }
        if (m_gameSession != null) {
            NetGame gameSession = m_gameSession;
            m_gameSession = null;
            gameSession.Terminate();
        }
    }

    public void Send(String str) throws IOException {
        System.out.println("NetClient::Send: was called");
        gomokuSer.writeObject(str);
    }

    private void sendBoard(GoMokuGameLogic game) throws IOException {
        System.out.println("sendBoard: number of pawns " + game.getGameBoard().getPawnsCount());
        gomokuSer.writeObject(game.getGameBoard());
        sendGameStats(game);
    }

    private void sendGameStats(GoMokuGameLogic game) throws IOException {
        gomokuSer.writeObject(game.getCurrPlayer().getName());
        gomokuSer.writeObject(new Boolean(game.isGameOver()));
        boolean isGameOn = !game.isGameOver();
        m_gameSession.updateGameSession(isGameOn);
        if (!isGameOn) {
            gomokuSer.writeObject(new Boolean(game.getVictoryAchieved()));
            if (game.getVictoryAchieved()) {
                gomokuSer.writeObject(game.getWinner().getName());
            }
            NetClient otherClient = this.getClientID() == m_gameSession.getClient1().getClientID() ? m_gameSession.getClient2() : m_gameSession.getClient1();
            otherClient.sendBoard(game);
        }
    }

    public void StartGame(NetGame game) throws IOException {
        m_gameSession = game;
        if (this.getClientID() == game.getClient2().getClientID()) {
            gomokuSer.writeObject(game.getGameSession().getPlayer(GoMokuGameLogic.WHITE_PLAYER_INDEX).getName());
            final NetClient thisClient = this;
            m_Thread = new Thread(new Runnable() {

                public void run() {
                    try {
                        playGame();
                    } catch (Exception e) {
                        m_topServer.disconnectClient(thisClient, e);
                        Terminate();
                    }
                }
            });
            m_Thread.start();
        }
    }

    public void EndGame() {
        try {
            if (m_Thread != null) {
                m_Thread.join();
            }
        } catch (Exception e) {
        }
    }

    private int parseOponentID(String oponent) {
        int id = -1;
        try {
            id = Integer.parseInt(oponent.split(" ")[0]);
        } catch (Exception e) {
        } finally {
            return id;
        }
    }
}
