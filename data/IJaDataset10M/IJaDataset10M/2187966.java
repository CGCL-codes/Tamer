package edu.ucla.cs.rpc.multicast.util.token;

import java.util.concurrent.locks.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;

/**
 * The abstract template for a TokenManager. A TokenManager will recieve and
 * send tokens accross some sort of communication channel. The token class also
 * has an internal Lock and Condition variable.
 * 
 * @author Philip Russell Chase Covello
 *  
 */
public abstract class TokenManager implements Runnable {

    protected Token token = null;

    protected Lock tokenLock = null;

    public static final long TOKENHOLD_TIMEOUT = 1000;

    protected Condition tokenCondition = null;

    /**
	 * Primary constructor.
	 */
    public TokenManager() {
        this.tokenLock = new ReentrantLock();
        this.tokenCondition = this.tokenLock.newCondition();
    }

    /**
	 * 
	 * @return The Lock associated with this token.
	 */
    public Lock getLock() {
        return this.tokenLock;
    }

    /**
	 * 
	 * @return The Condition variable associated with this Token.
	 */
    public Condition getCondition() {
        return this.tokenCondition;
    }

    protected boolean running;

    /**
	 * 
	 * @return The token held by this TokenManager.
	 */
    public Token getToken() {
        return this.token;
    }

    /**
	 * 
	 * @param tk
	 *            The token to own.
	 */
    public void setToken(Token tk) {
        this.tokenLock.lock();
        this.token = tk;
        this.tokenLock.unlock();
    }

    protected static void sendToken(Token token, SocketAddress addr) throws IOException {
        Socket socket = new Socket();
        socket.connect(addr);
        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
        output.writeObject(token);
        output.close();
        socket.close();
    }

    /**
	 * Clears the token held by this object.
	 * 
	 */
    public void clearToken() {
        this.tokenLock.lock();
        this.token = null;
        this.tokenLock.unlock();
    }

    /**
	 * 
	 * @return True if this object holds a Token, false otherwise.
	 */
    public boolean hasToken() {
        return (this.token != null);
    }

    public void shutdown() {
        this.running = false;
    }
}
