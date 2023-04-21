package soccer.common;

import java.util.*;

/**
 * Provides visual data for server informing player type clients.
 *
 * @author Yu Zhang
 */
public class SeeData implements Data {

    /**
	* status NO_OFFSIDE indicates no players are at Offside position.
	*/
    public static final int NO_OFFSIDE = 0;

    /**
	* status OFFSIDE indicates I'm at Offside position.
	*/
    public static final int OFFSIDE = 1;

    /**
	* status T_OFFSIDE indicates my teammates are at Offside position.
	*/
    public static final int T_OFFSIDE = 2;

    /**
	* the current simulation step.
	*/
    public int time;

    /**
	* the player who percieves this information
	*/
    public Player player;

    /**
	* the player's Offside position status. 0 means no players in player's team 
	* are at Offside position. 1 means the player is at Offside position. 
	* 2 means the player's teammates are at Offside position.
	*/
    public int status;

    /**
	* the ball.
	*/
    public Ball ball;

    /**
	* a list of positions for left team. 
	*/
    public Vector leftTeam;

    /**
	* a list of positions for right team. 
	*/
    public Vector rightTeam;

    /**
	* Constructs an empty SeeData for reading from an UDP packet.
	*/
    public SeeData() {
        this.time = 0;
        this.player = new Player();
        this.status = 0;
        this.ball = new Ball();
        this.leftTeam = new Vector();
        this.rightTeam = new Vector();
    }

    /** 
	* Constructs a SeeData for writing to an UDP packet.
	*
	* @param time the current simulation step.
	* @param player the player.
	* @param status the player's Offside position status.
	* @param ball the ball.
	* @param leftTeam a list of positions for the left team.
	* @param rightTeam a list of positions for the right team.
	*/
    public SeeData(int time, Player player, int status, Ball ball, Vector leftTeam, Vector rightTeam) {
        this.time = time;
        this.player = player;
        this.status = status;
        this.ball = ball;
        this.leftTeam = leftTeam;
        this.rightTeam = rightTeam;
    }

    public void readData(StringTokenizer st) {
        double x, y;
        int dataid = 0;
        try {
            time = Integer.parseInt(st.nextToken());
            dataid++;
            st.nextToken();
            player.side = st.nextToken().charAt(0);
            dataid++;
            st.nextToken();
            player.id = Integer.parseInt(st.nextToken());
            dataid++;
            st.nextToken();
            x = Double.parseDouble(st.nextToken()) / 100;
            dataid++;
            st.nextToken();
            y = Double.parseDouble(st.nextToken()) / 100;
            dataid++;
            player.position.setXY(x, y);
            st.nextToken();
            player.direction = Double.parseDouble(st.nextToken());
            dataid++;
            st.nextToken();
            status = Integer.parseInt(st.nextToken());
            dataid++;
            st.nextToken();
            x = Double.parseDouble(st.nextToken()) / 100;
            dataid++;
            st.nextToken();
            y = Double.parseDouble(st.nextToken()) / 100;
            dataid++;
            ball.position.setXY(x, y);
            st.nextToken();
            ball.controllerType = st.nextToken().charAt(0);
            dataid++;
            st.nextToken();
            ball.controllerId = Integer.parseInt(st.nextToken());
            dataid++;
            st.nextToken();
            char grabbed = st.nextToken().charAt(0);
            ball.isGrabbed = (grabbed == 'g');
            dataid++;
            st.nextToken();
            while (st.nextToken().charAt(0) == Packet.OPEN_TOKEN) {
                Player obj = new Player();
                obj.side = st.nextToken().charAt(0);
                dataid++;
                st.nextToken();
                obj.id = Integer.parseInt(st.nextToken());
                dataid++;
                st.nextToken();
                x = Double.parseDouble(st.nextToken()) / 100;
                dataid++;
                st.nextToken();
                y = Double.parseDouble(st.nextToken()) / 100;
                obj.position.setXY(x, y);
                dataid++;
                st.nextToken();
                obj.direction = Double.parseDouble(st.nextToken());
                dataid++;
                if (obj.side == 'l') leftTeam.addElement(obj); else rightTeam.addElement(obj);
                st.nextToken();
                st.nextToken();
                st.nextToken();
            }
        } catch (Exception e) {
            System.out.println("Error in SeeData.readData(" + e);
            System.out.println("dataid = " + dataid + "  st = ");
            System.out.println();
        }
    }

    public void writeData(StringBuffer sb) {
        sb.append(Packet.SEE);
        sb.append(' ');
        sb.append(time);
        sb.append(' ');
        sb.append(player.side);
        sb.append(' ');
        sb.append(player.id);
        sb.append(' ');
        sb.append((int) (player.position.getX() * 100));
        sb.append(' ');
        sb.append((int) (player.position.getY() * 100));
        sb.append(' ');
        sb.append((int) player.direction);
        sb.append(' ');
        sb.append(status);
        sb.append(' ');
        sb.append((int) (ball.position.getX() * 100));
        sb.append(' ');
        sb.append((int) (ball.position.getY() * 100));
        sb.append(' ');
        sb.append(ball.controllerType);
        sb.append(' ');
        sb.append(ball.controllerId);
        sb.append(' ');
        char grabbed = 'n';
        if (ball.isGrabbed) grabbed = 'g';
        sb.append(grabbed);
        sb.append(' ');
        if (time % 2 == 0) {
            addTeamInfo(leftTeam, sb);
        }
        addTeamInfo(rightTeam, sb);
        if (time % 2 != 0) {
            addTeamInfo(leftTeam, sb);
        }
    }

    private void addTeamInfo(Vector team, StringBuffer sb) {
        Enumeration players = team.elements();
        while (players.hasMoreElements()) {
            Player obj = (Player) players.nextElement();
            sb.append('(');
            sb.append(obj.side);
            sb.append(' ');
            sb.append(obj.id);
            sb.append(' ');
            sb.append((int) (obj.position.getX() * 100));
            sb.append(' ');
            sb.append((int) (obj.position.getY() * 100));
            sb.append(' ');
            sb.append((int) obj.direction);
            sb.append(' ');
            sb.append(")");
            sb.append(" ");
        }
    }
}
