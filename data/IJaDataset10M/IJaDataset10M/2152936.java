package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import webWof.WebMessage;
import webWof.WebPlayer;

public class StartGameServlet extends HttpServlet {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7138184628829581896L;

    private int IDGenerator = 0;

    private synchronized int generateId() {
        return ++IDGenerator;
    }

    /**
	 * @see HttpServlet#HttpServlet()
	 */
    public StartGameServlet() {
        super();
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            response.setContentType("application/x-java-serialized-object");
            InputStream in = request.getInputStream();
            ObjectInputStream inputFromApplet = new ObjectInputStream(in);
            java.lang.Object o = inputFromApplet.readObject();
            String[] newPlayers = o.toString().split(";");
            int result = WebGamesManager.getInstance().SetUpGame(newPlayers);
            OutputStream outstr = response.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(outstr);
            oos.writeObject(result);
            oos.flush();
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean CheckIfExsists(String newPlayer) {
        return true;
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
