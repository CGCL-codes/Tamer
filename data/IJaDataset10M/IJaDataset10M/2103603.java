package net.rptools.maptool.server;

import java.io.IOException;
import java.net.Socket;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.model.Player;
import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

/**
 * @author trevor
 */
public class Handshake {

    public interface Code {

        public static final int UNKNOWN = 0;

        public static final int OK = 1;

        public static final int ERROR = 2;
    }

    /**
	 * Server side of the handshake
	 */
    public static Player receiveHandshake(MapToolServer server, Socket s) throws IOException {
        ServerConfig config = server.getConfig();
        HessianInput input = new HessianInput(s.getInputStream());
        HessianOutput output = new HessianOutput(s.getOutputStream());
        output.findSerializerFactory().setAllowNonSerializable(true);
        Request request = (Request) input.readObject();
        Response response = new Response();
        response.code = Code.OK;
        boolean passwordMatches = Player.Role.valueOf(request.role) == Player.Role.GM ? config.gmPasswordMatches(request.password) : config.playerPasswordMatches(request.password);
        if (!passwordMatches) {
            response.code = Code.ERROR;
            response.message = "Wrong password";
        } else if (server.isPlayerConnected(request.name)) {
            response.code = Code.ERROR;
            response.message = "That name is already in use";
        } else if (!MapTool.getVersion().equals(request.version)) {
            response.code = Code.ERROR;
            response.message = "Invalid version.  Client:" + request.version + " Server:" + MapTool.getVersion();
        }
        response.policy = server.getPolicy();
        output.writeObject(response);
        return response.code == Code.OK ? new Player(request.name, Player.Role.valueOf(request.role), request.password) : null;
    }

    /**
	 * Client side of the handshake
	 */
    public static Response sendHandshake(Request request, Socket s) throws IOException {
        HessianInput input = new HessianInput(s.getInputStream());
        HessianOutput output = new HessianOutput(s.getOutputStream());
        output.findSerializerFactory().setAllowNonSerializable(true);
        output.writeObject(request);
        return (Response) input.readObject();
    }

    public static class Request {

        public String name;

        public String password;

        public String role;

        public String version;

        public Request() {
        }

        public Request(String name, String password, Player.Role role, String version) {
            this.name = name;
            this.password = password;
            this.role = role.name();
            this.version = version;
        }
    }

    public static class Response {

        public int code;

        public String message;

        public ServerPolicy policy;
    }
}
