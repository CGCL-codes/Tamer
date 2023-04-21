package exfist;

import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

public class Player extends JComponent implements Runnable {

    protected Player opponent;

    private String name, rb;

    private int score, ai;

    protected boolean key_held;

    private BufferedImage[] movementf_imgs, movementb_imgs, movementdk_imgs;

    private BufferedImage[] puncheswk_imgs, punchesmed_imgs, punchesstr_imgs;

    private BufferedImage[] kickswk_imgs, kicksmed_imgs, kicksstr_imgs;

    private BufferedImage[] jumpf_imgs, jumpb_imgs, hop_imgs;

    private BufferedImage[] block_high_imgs, fall_imgs;

    private BufferedImage temp;

    private int curr_x, curr_y;

    private State_Tree state;

    protected Node current;

    private Control_Node base_state, block_high_loop;

    private Node[] movef_nodes, moveb_nodes, movedk_nodes;

    private Node[] punchwk_nodes, punchmed_nodes, punchstr_nodes;

    private Node[] kickwk_nodes, kickmed_nodes, kickstr_nodes;

    private Node[] jumpf_nodes, jumpb_nodes, hop_nodes;

    private Node[] block_high_nodes, fall_nodes;

    private Point movef_vec, moveb_vec, jumpfu_vec, jumpfd_vec, jumpbu_vec, jumpbd_vec, hopu_vec, hopd_vec, dont_move;

    SpriteCache sc = new SpriteCache();

    private Moves moves;

    protected Player() {
    }

    /**
	 * default constructor for the player
	 * @param pnum player 1 or 2
	 */
    protected Player(int pnum) {
        moves = new Moves();
        curr_x = 150;
        curr_y = 450;
        movef_vec = new Point(4, 0);
        moveb_vec = new Point(-4, 0);
        jumpfu_vec = new Point(3, -8);
        jumpfd_vec = new Point(3, 8);
        jumpbu_vec = new Point(-3, -8);
        jumpbd_vec = new Point(-3, 8);
        hopu_vec = new Point(0, -8);
        hopd_vec = new Point(0, 8);
        dont_move = new Point(0, 0);
        name = "player";
        this.setScore(0);
        state = new State_Tree();
        if (pnum == 1) rb = "R"; else rb = "B";
        temp = sc.getSprite("Anims/wait" + rb + "/1.gif");
        base_state = new Control_Node("base_state", temp, new Point(curr_x, curr_y));
        base_state.setBusy(false);
        state = new State_Tree(base_state);
        current = base_state;
        setMovement();
        setPunches();
        setKicks();
        setBlock();
        setJumps();
        setWinLose();
        key_held = false;
    }

    /**
	 * AI method,randomly picks a move to do
	 */
    protected void AI() {
        ai = (int) (20 * Math.random());
        if (ai < 14) state.traverseState(base_state.getChild(ai), this);
    }

    /**
	 * 
	 * @return the sprite for the current node
	 */
    protected BufferedImage getCurrentSprite() {
        return current.sprite;
    }

    /**
	 * checks if the player is left of the other one
	 * @param p the other player
	 * @return true/false
	 */
    protected boolean isLeftOf(Player p) {
        if (this.getX() < p.getX()) return true; else return false;
    }

    /**
	  * 
	  * @return the current score
	  */
    private int getScore() {
        return score;
    }

    /**
	 * 
	 * @return the player name
	 */
    private String getPlayerName() {
        return name;
    }

    /**
	 * if the key is being held this will be true,otherwise false
	 * @return true/false
	 */
    protected boolean isKeyHeld() {
        return key_held;
    }

    /**
	 * return the players x coord
	 */
    public int getX() {
        return curr_x;
    }

    /**
	 * 
	 * @return the opponent of the player
	 */
    protected Player getOpponent() {
        return opponent;
    }

    /**
	 * returns the players y coord
	 */
    public int getY() {
        return curr_y;
    }

    /**
	 * traverses the move forward branch of the tree
	 *
	 */
    protected void movef() {
        state.traverseState(base_state.getChild(1), this);
    }

    /**
	 * traverses the move back branch
	 *
	 */
    protected void moveb() {
        state.traverseState(base_state.getChild(2), this);
    }

    /**
	 * traverses the duck branch
	 *
	 */
    protected void duck() {
        state.traverseState(base_state.getChild(3), this);
    }

    /**
	 * traverses the strong punch branch
	 *
	 */
    protected void punchstr() {
        state.traverseState(base_state.getChild(4), this);
    }

    /**
	 * traverses the medium punch branch
	 *
	 */
    protected void punchmed() {
        state.traverseState(base_state.getChild(5), this);
    }

    /**
	 * traverses the weak punch branch
	 *
	 */
    protected void punchwk() {
        state.traverseState(base_state.getChild(6), this);
    }

    /**
	 * traverses the strong kick branch
	 *
	 */
    protected void kickstr() {
        state.traverseState(base_state.getChild(7), this);
    }

    /**
	 * traverses the medium punch branch
	 *
	 */
    protected void kickmed() {
        state.traverseState(base_state.getChild(8), this);
    }

    /**
	 * traverses the weak kick branch
	 *
	 */
    protected void kickwk() {
        state.traverseState(base_state.getChild(9), this);
    }

    /**
	 * traverses the block branch
	 *
	 */
    protected void high_block() {
        state.traverseState(base_state.getChild(10), this);
    }

    /**
	 * traverses the jump forward branch
	 *
	 */
    protected void jumpf() {
        state.traverseState(base_state.getChild(11), this);
    }

    /**
	 * traverses the jump back branch
	 *
	 */
    protected void jumpb() {
        state.traverseState(base_state.getChild(12), this);
    }

    /**
	 * traverses the jump up branch
	 *
	 */
    protected void hop() {
        state.traverseState(base_state.getChild(13), this);
    }

    /**
	 * traverses the fall down branch
	 *
	 */
    protected void fall() {
        state.traverseState(base_state.getChild(14), this);
    }

    /**
	 * sets the score
	 * @param x the score
	 */
    private void setScore(int x) {
        score = x;
    }

    /**
	 * increments the score by a given amount
	 * @param x the amount to increment by
	 */
    private void incScore(int x) {
        score += x;
    }

    /**
	 * set whether the key is set
	 * @param b true/false
	 */
    protected void setKeyHeld(boolean b) {
        key_held = b;
        System.out.println("is key held? - " + key_held);
    }

    /**
	 * set the players name
	 * @param new_name
	 */
    protected void setPlayerName(String new_name) {
        name = new_name;
    }

    /**
	 * set up the movement branches (forward,backward & duck)
	 *
	 */
    private void setMovement() {
        movef_nodes = new Node[20];
        movementf_imgs = new BufferedImage[20];
        moves.set_Imgs(movementf_imgs, "Anims/step", rb);
        moves.set_Nodes(movef_nodes, movementf_imgs, "moveF", movef_vec, true);
        state.addNewState(base_state, movef_nodes[0]);
        moves.populate_Branch(state, movef_nodes);
        state.addNode(movef_nodes[19], base_state);
        moveb_nodes = new Node[20];
        movementb_imgs = new BufferedImage[20];
        moves.set_ImgsRev(movementb_imgs, "Anims/step", rb);
        moves.set_Nodes(moveb_nodes, movementb_imgs, "moveB", moveb_vec, true);
        state.addNewState(base_state, moveb_nodes[0]);
        moves.populate_Branch(state, moveb_nodes);
        state.addNode(moveb_nodes[19], base_state);
        movedk_nodes = new Node[10];
        movementdk_imgs = new BufferedImage[10];
        moves.set_Imgs(movementdk_imgs, "Anims/duck", rb);
        moves.set_Nodes(movedk_nodes, movementdk_imgs, "duck", dont_move, true);
        state.addNewState(base_state, movedk_nodes[0]);
        moves.populate_Branch(state, movedk_nodes);
        state.addNode(movedk_nodes[9], base_state);
    }

    /**
	 * set the punches (strong medium and weak)
	 *
	 */
    private void setPunches() {
        punchstr_nodes = new Node[25];
        punchesstr_imgs = new BufferedImage[25];
        moves.set_Imgs(punchesstr_imgs, "Anims/CPunch", rb);
        moves.set_Nodes(punchstr_nodes, punchesstr_imgs, "PunchStr", dont_move, false);
        punchstr_nodes[12].check_collision = true;
        state.addNewState(base_state, punchstr_nodes[0]);
        moves.populate_Branch(state, punchstr_nodes);
        state.addNode(punchstr_nodes[24], base_state);
        punchmed_nodes = new Node[20];
        punchesmed_imgs = new BufferedImage[20];
        moves.set_Imgs(punchesmed_imgs, "Anims/BPunch", rb);
        moves.set_Nodes(punchmed_nodes, punchesmed_imgs, "PunchMed", dont_move, false);
        punchmed_nodes[7].check_collision = true;
        state.addNewState(base_state, punchmed_nodes[0]);
        moves.populate_Branch(state, punchmed_nodes);
        state.addNode(punchmed_nodes[19], base_state);
        punchwk_nodes = new Node[15];
        puncheswk_imgs = new BufferedImage[15];
        moves.set_Imgs(puncheswk_imgs, "Anims/APunch", rb);
        moves.set_Nodes(punchwk_nodes, puncheswk_imgs, "PunchWeak", dont_move, false);
        punchwk_nodes[8].check_collision = true;
        state.addNewState(base_state, punchwk_nodes[0]);
        moves.populate_Branch(state, punchwk_nodes);
        state.addNode(punchwk_nodes[14], base_state);
    }

    /**
	 * set the kicks (strong medium and weak)
	 *
	 */
    private void setKicks() {
        kickstr_nodes = new Node[25];
        kicksstr_imgs = new BufferedImage[25];
        moves.set_Imgs(kicksstr_imgs, "Anims/CKick", rb);
        moves.set_Nodes(kickstr_nodes, kicksstr_imgs, "KickStr", dont_move, false);
        kickstr_nodes[16].check_collision = true;
        state.addNewState(base_state, kickstr_nodes[0]);
        moves.populate_Branch(state, kickstr_nodes);
        state.addNode(kickstr_nodes[24], base_state);
        kickmed_nodes = new Node[20];
        kicksmed_imgs = new BufferedImage[20];
        moves.set_Imgs(kicksmed_imgs, "Anims/BKick", rb);
        moves.set_Nodes(kickmed_nodes, kicksmed_imgs, "KickMed", dont_move, false);
        kickmed_nodes[13].check_collision = true;
        state.addNewState(base_state, kickmed_nodes[0]);
        moves.populate_Branch(state, kickmed_nodes);
        state.addNode(kickmed_nodes[19], base_state);
        kickwk_nodes = new Node[20];
        kickswk_imgs = new BufferedImage[20];
        moves.set_Imgs(kickswk_imgs, "Anims/AKick", rb);
        moves.set_Nodes(kickwk_nodes, kickswk_imgs, "KickWeak", dont_move, false);
        kickwk_nodes[10].check_collision = true;
        state.addNewState(base_state, kickwk_nodes[0]);
        moves.populate_Branch(state, kickwk_nodes);
        state.addNode(kickwk_nodes[19], base_state);
    }

    /**
	 * set the block branch
	 *
	 */
    private void setBlock() {
        block_high_nodes = new Node[10];
        block_high_imgs = new BufferedImage[10];
        moves.set_Imgs(block_high_imgs, "Anims/HighBlock", rb);
        moves.set_Nodes(block_high_nodes, block_high_imgs, "Block_High", dont_move, true);
        for (int i = 0; i <= 9; i++) {
            block_high_nodes[i].setHighBlocking(true);
        }
        state.addNewState(base_state, block_high_nodes[0]);
        moves.populate_Branch(state, block_high_nodes);
        state.addNode(block_high_nodes[9], base_state);
    }

    /**
	 * set the jump branches
	 *
	 */
    protected void setJumps() {
        jumpf_nodes = new Node[76];
        jumpf_imgs = new BufferedImage[75];
        moves.set_Imgs(jumpf_imgs, "Anims/ssault", rb);
        moves.set_Nodes(jumpf_nodes, jumpf_imgs, "JumpF", jumpfu_vec, false, 0, 37);
        moves.set_Nodes(jumpf_nodes, jumpf_imgs, "JumpF", jumpfd_vec, false, 38, 74);
        jumpf_nodes[75] = new Node("jumpf75", temp, jumpfd_vec, false);
        state.addNewState(base_state, jumpf_nodes[0]);
        moves.populate_Branch(state, jumpf_nodes);
        state.addNode(jumpf_nodes[75], base_state);
        jumpb_nodes = new Node[76];
        jumpb_imgs = new BufferedImage[75];
        moves.set_ImgsRev(jumpb_imgs, "Anims/ssault", rb);
        moves.set_Nodes(jumpb_nodes, jumpb_imgs, "JumpB", jumpbu_vec, false, 0, 37);
        moves.set_Nodes(jumpb_nodes, jumpb_imgs, "JumpB", jumpbd_vec, false, 38, 74);
        jumpb_nodes[75] = new Node("jumpb75", temp, jumpbd_vec, false);
        state.addNewState(base_state, jumpb_nodes[0]);
        moves.populate_Branch(state, jumpb_nodes);
        state.addNode(jumpb_nodes[75], base_state);
        hop_nodes = new Node[20];
        hop_imgs = new BufferedImage[20];
        moves.set_Imgs(hop_imgs, "Anims/Hop", rb);
        moves.set_Nodes(hop_nodes, hop_imgs, "hop", hopu_vec, false, 0, 9);
        moves.set_Nodes(hop_nodes, hop_imgs, "hop", hopd_vec, false, 10, 19);
        state.addNewState(base_state, hop_nodes[0]);
        moves.populate_Branch(state, hop_nodes);
        state.addNode(hop_nodes[19], base_state);
    }

    /**
	 * set the win/fall branches
	 *
	 */
    protected void setWinLose() {
        fall_nodes = new Node[35];
        fall_imgs = new BufferedImage[35];
        moves.set_Imgs(fall_imgs, "Anims/FallBack", rb);
        moves.set_Nodes(fall_nodes, fall_imgs, "fall", dont_move, false);
        state.addNewState(base_state, fall_nodes[0]);
        moves.populate_Branch(state, fall_nodes);
    }

    /**
	 * set the players position
	 * @param x the x coord
	 * @param y the y coord
	 */
    protected void setPos(int x, int y) {
        curr_x = x;
        curr_y = y;
    }

    /**
	 * increment the position by the vector in the node
	 * @param n the node
	 */
    protected void updatePos(Node n) {
        if (curr_x + n.getPos_X() >= 0 && curr_x + n.getPos_X() <= 782) curr_x += n.getPos_X();
        curr_y += n.getPos_Y();
    }

    /**
	 * sets the current node to be the given node
	 * @param n the new node
	 */
    protected void setCurrentNode(Node n) {
        current = n;
    }

    /**
	 * sets the oppenent
	 * @param p the player who is the opponent
	 */
    protected void setOpponent(Player p) {
        opponent = p;
    }

    /**
	 * detects if the player is colliding with another player
	 * @param p the other player
	 * @param dist the distance between the two players
	 * @param pos the position of the player
	 * @return
	 */
    public boolean collision(Player p, int dist, int pos) {
        BufferedImage a = getCurrentSprite();
        BufferedImage b = p.getCurrentSprite();
        int s, y, z;
        int t = 0;
        for (int i = 0; i < a.getWidth(); i++) {
            if (pos == 1) y = i - dist; else y = i + dist;
            for (int j = 0; j < a.getHeight(); j++) {
                s = a.getRGB(i, j);
                if (y > 0 && y < 300) t = b.getRGB(y, j);
                if (s != 0 && t != 0 && p.current.isHighBlocking() == false) {
                    return true;
                }
            }
        }
        return false;
    }

    public void run() {
        this.AI();
    }
}
