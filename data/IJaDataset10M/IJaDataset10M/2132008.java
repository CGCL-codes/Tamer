package chess.net;

import java.util.Observable;
import java.util.Observer;
import org.json.simple.JSONObject;
import chess.business.BusinessController;
import chess.dtos.MoveDTO;
import chess.dtos.PlayerDTO;

public class NetworkGame implements Observer {

    private NetworkConnection cnx;

    private String localName;

    private String remoteName;

    private PlayerDTO localPlayer;

    private BusinessController controller;

    private int currentStatus;

    private static final int WAITING_RESULT = 1;

    private static final int WAITING_REMOTE_RESULT = 1;

    private static final int WAITING_MOVE = 2;

    public static NetworkGame createServer(String name, int port) {
        NetworkGame ng = null;
        NetworkConnection cnx = new NetworkServer(port);
        if (cnx.createSocket()) {
            ng = new NetworkGame(cnx);
            ng.setLocalName(name);
            JSONObject msg = new JSONObject();
            msg.put("player", name);
            cnx.sendMessage(msg);
            msg = cnx.waitMessage();
            if (msg != null && msg.get("player") != null) {
                ng.setRemoteName((String) msg.get("player"));
            } else {
                System.out.println("[NETWORK] Error in handshake");
                ng = null;
            }
        }
        return ng;
    }

    public static NetworkGame createClient(String name, String server, int port) {
        NetworkGame ng = null;
        NetworkConnection cnx = new NetworkClient(server, port);
        if (cnx.createSocket()) {
            ng = new NetworkGame(cnx);
            JSONObject msg;
            msg = cnx.waitMessage();
            if (msg != null && msg.get("player") != null) {
                ng.setLocalName(name);
                ng.setRemoteName((String) msg.get("player"));
                msg = new JSONObject();
                msg.put("player", name);
                cnx.sendMessage(msg);
            } else {
                System.out.println("[NETWORK] Error in handshake");
                ng = null;
            }
        }
        return ng;
    }

    private NetworkGame(NetworkConnection c) {
        cnx = c;
        currentStatus = WAITING_MOVE;
    }

    public void update(Observable o, Object arg) {
        if (o != null && o instanceof BusinessController) {
            final Integer i = (Integer) arg;
            if (currentStatus == WAITING_MOVE) {
                if (i.intValue() != BusinessController.ILEGALMOVE) {
                    MoveDTO move = controller.getLastMove();
                    if (move != null) {
                        JSONObject msg = new JSONObject();
                        msg.put("xsource", move.getOriginX());
                        msg.put("ysource", move.getOriginY());
                        msg.put("xdest", move.getPreviousX());
                        msg.put("ydest", move.getPreviousY());
                        msg.put("player", localPlayer.getColor() == 'w' ? 1 : 2);
                        currentStatus = WAITING_REMOTE_RESULT;
                        cnx.sendMessage(msg);
                    }
                }
            } else if (currentStatus == WAITING_RESULT) {
                JSONObject msg = new JSONObject();
                if (i.intValue() == BusinessController.ILEGALMOVE) {
                    msg.put("status", "404 FAIL");
                } else {
                    msg.put("status", "200 OK");
                }
                msg.put("result", i);
                msg.put("player", localPlayer.getColor() == 'w' ? 1 : 2);
                currentStatus = WAITING_MOVE;
                cnx.sendMessage(msg);
            }
        }
    }

    public void setLocalName(String name) {
        localName = name;
    }

    public String getLocalName() {
        return localName;
    }

    public void setRemoteName(String name) {
        remoteName = name;
    }

    public String getRemoteName() {
        return remoteName;
    }

    public void setLocalPlayer(PlayerDTO player) {
        localPlayer = player;
    }

    public void setController(BusinessController controller) {
        this.controller = controller;
        this.controller.addObserver(this);
    }

    public void startGame() {
        new Thread(new Runnable() {

            public void run() {
                JSONObject msg;
                int xs, xd, ys, yd;
                while (cnx.isOnline()) {
                    msg = cnx.waitMessage();
                    if (msg != null) {
                        if (currentStatus == WAITING_REMOTE_RESULT) {
                            currentStatus = WAITING_MOVE;
                            if (msg.get("result") != null) {
                                if (Integer.valueOf(msg.get("result").toString()) == BusinessController.ILEGALMOVE) {
                                    controller.clientMoveError();
                                }
                            } else {
                                controller.clientMoveError();
                            }
                        } else if (currentStatus == WAITING_MOVE) {
                            xs = Integer.valueOf(msg.get("xsource").toString());
                            ys = Integer.valueOf(msg.get("ysource").toString());
                            xd = Integer.valueOf(msg.get("xdest").toString());
                            yd = Integer.valueOf(msg.get("ydest").toString());
                            currentStatus = WAITING_RESULT;
                            controller.playerMove(localPlayer, xs, ys, xd, yd);
                        }
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }
}
