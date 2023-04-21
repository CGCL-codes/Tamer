package at.momberban2.me.game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import at.momberban2.me.gamecontext.M2Bomb;
import at.momberban2.me.gamecontext.M2Map;
import at.momberban2.me.gamecontext.M2Player;
import at.momberban2.me.gamecontext.M2TileInfo;
import at.syncme.framework.GameState;
import at.syncme.framework.legacy.WrapperCache;
import de.enough.polish.util.ArrayList;
import de.enough.polish.util.HashMap;

/**
 * the momberban game state. references all entities that can be serialized
 * 
 * @author Daniel Rudigier
 */
public class M2GameState extends GameState {

    private HashMap playerMap;

    private ArrayList players;

    private ArrayList bombs;

    private M2Map map;

    /**
     * default constructor
     * 
     * @deprecated
     */
    public M2GameState() {
    }

    /**
     * @return players
     */
    public ArrayList getPlayers() {
        return players;
    }

    /**
     * return a player according to the id
     * 
     * @param id
     * @return
     */
    public M2Player getPlayer(Byte id) {
        return (M2Player) this.playerMap.get(id);
    }

    /**
     * @return map
     */
    public M2Map getMap() {
        return map;
    }

    /**
     * @param map
     *            to set
     */
    public void setMap(M2Map map) {
        this.map = map;
    }

    /**
     * override
     * 
     * @see at.syncme.framework.GameState#updateGame()
     */
    public void updateGame() {
        for (int i = 0; i < this.players.size(); i++) {
            M2Player p = (M2Player) this.players.get(i);
            int oldX = p.getSprite().getX();
            int oldY = p.getSprite().getY();
            p.move();
            if (this.map.isCollided(p.getSprite())) {
                p.getSprite().setPosition(oldX, oldY);
            }
        }
        for (int i = 0; i < this.bombs.size(); i++) {
            M2Bomb bomb = (M2Bomb) this.bombs.get(i);
            if (bomb.isTriggered(getGvt())) {
                bomb.explode();
                M2Player player = bomb.getPlayer();
                player.setBombs((byte) (player.getBombs() + 1));
                this.bombs.remove(bomb);
            }
        }
        beLazy(30);
    }

    /**
     * setup the game state and the map
     * 
     * @param map
     * @param isServer
     */
    public void setup(M2Map map) {
        this.players = new ArrayList();
        this.playerMap = new HashMap();
        this.map = map;
        this.map.reset();
    }

    /**
     * move a player
     * 
     * @param id
     * @param move
     */
    public void playerMove(Byte id, byte move) {
        if (this.playerMap.containsKey(id)) {
            M2Player player = (M2Player) this.playerMap.get(id);
            player.setMove(move);
        }
    }

    /**
     * a new player has connected
     * 
     * @return new player id
     */
    public Byte newPlayer() {
        for (byte i = 1; i < 10; i++) {
            Byte key = WrapperCache.valueOf(i);
            if (!this.playerMap.containsKey(key)) {
                M2Player p = new M2Player(key.byteValue());
                this.playerMap.put(key, p);
                this.players.add(p);
                return key;
            }
        }
        return null;
    }

    /**
     * start the game, spawn players, etc.
     */
    public void prepare() {
        if (this.players == null) {
            throw new IllegalStateException("cannot start. players is null");
        } else if (this.map == null) {
            throw new IllegalStateException("cannot start. map is null");
        } else {
            this.bombs = new ArrayList();
            this.map.reset();
            for (int i = 0; i < this.players.size(); i++) {
                this.map.spawnPlayer((M2Player) this.players.get(i));
            }
        }
    }

    /**
     * may a player join?
     * 
     * @return
     */
    public boolean canJoin() {
        return this.players.size() < 10;
    }

    /**
     * remove a player
     * 
     * @param id
     */
    public void playerLeft(Byte id) {
        if (this.playerMap.containsKey(id)) {
            M2Player p = (M2Player) this.playerMap.get(id);
            this.playerMap.remove(id);
            this.players.remove(p);
        }
    }

    /**
     * plant a bomb on the screen
     * 
     * @param player
     *            the player who tried to plant the bomb
     * @return a valid bomb reference on success
     */
    protected M2Bomb plantBomb(Byte id) {
        M2Bomb bomb = null;
        if (this.playerMap.containsKey(id)) {
            M2Player player = (M2Player) this.playerMap.get(id);
            int posX = player.getSprite().getX();
            int posY = player.getSprite().getY();
            if (player.canPlant()) {
                M2TileInfo pos = this.map.decodePosition(posX, posY, M2Bomb.BOMB_WIDTH);
                System.out.println("bomb plant: " + pos.getId() + ":" + pos.getX() + ":" + pos.getY());
                if (pos.getState() == M2TileInfo.TILE_FREE) {
                    player.setBombs((byte) (player.getBombs() - 1));
                    bomb = new M2Bomb(player, pos.getX(), pos.getY(), getGvt(), player.getBombRange());
                    bomb.setPositionId(pos.getId());
                    this.bombs.add(bomb);
                } else {
                    System.out.println("cannot plant. not free");
                }
            } else {
                System.out.println("this player cannot plant!");
            }
        } else {
            System.out.println("plantBomb: i don't know this player: " + id);
        }
        return bomb;
    }

    /**
     * override
     * 
     * @see at.syncme.framework.Entity#read(java.io.DataInputStream)
     */
    public void read(DataInputStream in) throws IOException {
        this.map = new M2Map();
        this.map.read(in);
        byte playerCount = in.readByte();
        this.players = new ArrayList();
        this.playerMap = new HashMap();
        for (int i = 0; i < playerCount; i++) {
            M2Player p = new M2Player();
            p.read(in);
            this.players.add(p);
            this.playerMap.put(WrapperCache.valueOf(p.getId()), p);
        }
    }

    /**
     * override
     * 
     * @see at.syncme.framework.Entity#write(java.io.DataOutputStream)
     */
    public void write(DataOutputStream out) throws IOException {
        if (this.map == null) {
            throw new IllegalStateException("map is null");
        }
        this.map.write(out);
        out.writeByte((byte) this.players.size());
        for (int i = 0; i < this.players.size(); i++) {
            M2Player player = (M2Player) this.players.get(i);
            player.write(out);
        }
    }
}
