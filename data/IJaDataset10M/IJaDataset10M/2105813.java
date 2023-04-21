package test.multidoty;

import env3d.EnvObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.python.core.PyObject;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;
import sun.applet.AppletClassLoader;

/**
 * Describes a generic object in a room
 * @author jmadar
 */
public class RoomObject extends EnvObject {

    private String id;

    private PyObject pyObj;

    private boolean hasCode;

    private PythonInterpreter interp;

    private String parameters;

    private Game game;

    private Doty owner;

    public RoomObject(Game g, String id) throws OwnerNotFoundException {
        this.id = id;
        game = g;
        pyObj = null;
        hasCode = false;
        setScale(1);
        findOwner();
        PySystemState.initialize(null, null, null, new JythonClassLoader(getClass().getClassLoader()));
        interp = new PythonInterpreter();
        interp.set("this", this);
        interp.set("doty", g.getDoty1());
        interp.set("env", g.getEnv());
        interp.set("session", g.getSession());
        interp.set("others", g.getOthers());
        interp.set("owner", owner);
        interp.set("facebook", g.getFacebookClient());
        interp.set("game", g);
    }

    public void findOwner() throws OwnerNotFoundException {
        Doty owner = null;
        if (!id.equals("default")) {
            if (id.startsWith(game.getSession())) {
                owner = game.getDoty1();
            } else {
                owner = game.getOthers().get(id.substring(0, id.lastIndexOf("_")));
                if (owner == null) {
                    throw new OwnerNotFoundException("Cannot find owner for id " + id);
                }
            }
        }
        this.owner = owner;
    }

    public void setup() {
        try {
            if (hasCode) {
                PyObject method = interp.get("setup");
                if (method != null) {
                    method.__call__();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void move() {
        try {
            if (hasCode) {
                PyObject method = interp.get("move");
                if (method != null) {
                    method.__call__();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cleanup() {
        try {
            if (hasCode) {
                PyObject method = interp.get("cleanup");
                if (method != null) {
                    method.__call__();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) throws java.io.FileNotFoundException, java.io.IOException {
        java.net.URL codeURL = null;
        if (code.startsWith("http")) {
            try {
                codeURL = new java.net.URL(code);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        BufferedReader br;
        if (codeURL != null) {
            br = new BufferedReader(new InputStreamReader(codeURL.openStream()));
        } else {
            if (getClass().getClassLoader().getResource(code) == null) {
                FileReader fr = new FileReader(code);
                br = new BufferedReader(fr);
            } else {
                InputStreamReader fr = new InputStreamReader(new BufferedInputStream(getClass().getClassLoader().getResourceAsStream(code)));
                br = new BufferedReader(fr);
            }
        }
        StringBuffer codeString = new StringBuffer();
        while (true) {
            String line = br.readLine();
            if (line == null) {
                break;
            }
            codeString.append(line + "\n");
        }
        this.interp.exec(codeString.toString());
        hasCode = true;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    public void setParameters(String p) {
        parameters = p;
        interp.set("parameters", this.getParameters());
    }

    public String getParameters() {
        return parameters;
    }
}
