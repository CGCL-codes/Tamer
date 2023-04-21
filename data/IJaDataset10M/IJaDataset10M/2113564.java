package beaver.game;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;
import org.newdawn.slick.Input;

public class GameServer {

    private ServerSocket server;

    private Player p1;

    private Player p2;

    private int connectedPlayers;

    private volatile Player currentPlayer;

    private float[][] terrainBitmap;

    private boolean shutDown = false;

    private Socket client1;

    private Socket client2;

    private volatile int movesVersion = 0;

    private volatile Queue<String> p1Moves = null;

    private volatile Queue<String> p2Moves = null;

    private DataOutputStream c1Out = null;

    private DataInputStream c1In = null;

    private ObjectOutputStream obj1Out = null;

    private ObjectInputStream obj1In = null;

    private DataOutputStream c2Out = null;

    private DataInputStream c2In = null;

    private ObjectOutputStream obj2Out = null;

    private ObjectInputStream obj2In = null;

    private volatile int whoReads = 2;

    private boolean canRead = false;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public GameServer() {
        p1Moves = new LinkedList<String>();
        p2Moves = new LinkedList<String>();
        connectedPlayers = 0;
        p1 = new Player("Player 1");
        p2 = new Player("Player 2");
        currentPlayer = p1;
        terrainBitmap = Noise.GenerateWhiteNoise(Constants.MAX_WIDTH, Constants.MAX_HEIGHT * 2);
        terrainBitmap = Noise.GenerateSmoothNoise(terrainBitmap, 7);
        terrainBitmap = Noise.GeneratePerlinNoise(terrainBitmap, 7);
        try {
            server = new ServerSocket(Constants.SERVER_PORT);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void run() {
        Socket newClient = null;
        while (!shutDown) {
            try {
                Thread.sleep(Constants.NETWORK_WAIT);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            try {
                newClient = server.accept();
                System.out.println("[INFO] NEW CLIENT CONNECTION");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Thread t;
            switch(connectedPlayers) {
                case 0:
                    client1 = newClient;
                    connectedPlayers++;
                    t = new Thread() {

                        public void run() {
                            handleClient1();
                        }
                    };
                    t.start();
                    break;
                case 1:
                    client2 = newClient;
                    connectedPlayers++;
                    t = new Thread() {

                        public void run() {
                            handleClient2();
                        }
                    };
                    t.start();
                    break;
                default:
                    try {
                        DataOutputStream output = new DataOutputStream(newClient.getOutputStream());
                        output.writeUTF("error");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    public void handleClient1() {
        System.out.println("[INFO] PLAYER 1 CONNECTED");
        try {
            c1Out = new DataOutputStream(client1.getOutputStream());
            c1In = new DataInputStream(client1.getInputStream());
            obj1Out = new ObjectOutputStream(client1.getOutputStream());
            obj1In = new ObjectInputStream(client1.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            c1Out.writeUTF("accepted");
            obj1Out.writeObject(p1);
            obj1Out.writeObject(p2);
            obj1Out.writeObject(terrainBitmap);
            c1Out.writeBoolean(true);
            while (true) {
                Thread.sleep(Constants.NETWORK_WAIT);
                if (c1In.available() == 0) {
                    continue;
                }
                int message = c1In.readInt();
                if (message == Constants.END_CONNECTION) {
                    break;
                }
                if (message == Constants.CAN_WRITE_MOVES) {
                    if (currentPlayer == p1) {
                        c1Out.writeInt(Constants.YES);
                    } else {
                        c1Out.writeInt(Constants.NO);
                    }
                }
                if (message == Constants.WRITE_MOVES) {
                    System.out.println("PLAYER 1 WRITING MOVES TO SERVER");
                    p1Moves = (Queue<String>) obj1In.readUnshared();
                    whoReads = 2;
                    canRead = true;
                    System.out.println("SERVER READ " + p1Moves.size() + " MOVES");
                }
                if (message == Constants.GET_MOVES) {
                    int tempMovesVersion = c1In.readInt();
                    if (whoReads == 1 && canRead) {
                        obj1Out.writeObject(p2Moves);
                        canRead = false;
                    } else {
                        obj1Out.writeObject(null);
                    }
                }
                if (message == Constants.OPPONENT_TURN_ENDED) {
                    System.out.println("PLAYER 1 FINISHED PLAYING PLAYER 2 MOVES");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void sendTestMoves() {
        System.out.println("PLAYER 2 WRITING MOVES TO SERVER");
        Queue test = new LinkedList<String>();
        for (int i = 0; i < 1000; i++) {
            test.add("down");
        }
        try {
            obj1Out.writeObject(test);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleClient2() {
        System.out.println("[INFO] PLAYER 2 CONNECTED");
        try {
            c2Out = new DataOutputStream(client2.getOutputStream());
            c2In = new DataInputStream(client2.getInputStream());
            obj2Out = new ObjectOutputStream(client2.getOutputStream());
            obj2In = new ObjectInputStream(client2.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            c2Out.writeUTF("accepted");
            obj2Out.writeObject(p2);
            obj2Out.writeObject(p1);
            obj2Out.writeObject(terrainBitmap);
            c2Out.writeBoolean(false);
            while (true) {
                Thread.sleep(Constants.NETWORK_WAIT);
                if (c2In.available() == 0) {
                    continue;
                }
                int message = c2In.readInt();
                if (message == Constants.END_CONNECTION) {
                    break;
                }
                if (message == Constants.WRITE_MOVES) {
                    System.out.println("PLAYER 2 WRITING MOVES TO SERVER");
                    p2Moves = (Queue<String>) obj2In.readUnshared();
                    whoReads = 1;
                    canRead = true;
                    System.out.println("SERVER READ " + p2Moves.size() + " MOVES");
                }
                if (message == Constants.GET_MOVES) {
                    int tempMovesVersion = c2In.readInt();
                    if (whoReads == 2 && canRead) {
                        obj2Out.writeObject(p1Moves);
                        canRead = false;
                    } else {
                        obj2Out.writeObject(null);
                    }
                }
                if (message == Constants.CAN_WRITE_MOVES) {
                    if (currentPlayer == p2) {
                        c2Out.writeInt(Constants.YES);
                    } else {
                        c2Out.writeInt(Constants.NO);
                    }
                }
                if (message == Constants.OPPONENT_TURN_ENDED) {
                    System.out.println("PLAYER 2 FINISHED PLAYING PLAYER 1 MOVES");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GameServer server = new GameServer();
        server.run();
    }
}
