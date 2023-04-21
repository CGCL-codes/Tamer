package gomoku.Model;

/**
 * Player is an abstract class. 
 * The derived class is required to implement the makeMove method. 
 */
public abstract class Player {

    protected GameBoard m_board;

    /** player's name */
    protected String m_name;

    private String m_userName;

    Player(GameBoard board, String name) {
        m_board = board;
        m_name = name;
    }

    /**
	 * 
	 * @return a move received from the player.
	 * @throws UserAbortException any input operations done by the view 
	 * 		 	object might throw this exception.
	 */
    public abstract Point makeMove();

    public String getName() {
        return m_name;
    }

    /**
     * @return the m_userName
     */
    public String getUserName() {
        return m_userName;
    }

    /**
     * @param m_userName the m_userName to set
     */
    public void setUserName(String m_userName) {
        this.m_userName = m_userName;
    }
}
