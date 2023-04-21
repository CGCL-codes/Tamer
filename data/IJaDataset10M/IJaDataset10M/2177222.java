package net.sourceforge.wildlife.core.environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.resources.IProject;
import net.sourceforge.wildlife.core.WildLifeCorePlugin;
import net.sourceforge.wildlife.core.components.IComponentFactory;
import net.sourceforge.wildlife.core.components.actor.Actor;
import net.sourceforge.wildlife.core.components.actor.Weather;
import net.sourceforge.wildlife.core.conf.env.XMLEnvGate;
import net.sourceforge.wildlife.core.conf.env.XMLEnvWeather;
import net.sourceforge.wildlife.core.conf.env.XMLEnvironment;
import net.sourceforge.wildlife.core.parser.EnvironmentParser;
import net.sourceforge.wildlife.core.types.Environment_background;
import net.sourceforge.wildlife.core.types.Gate;
import net.sourceforge.wildlife.core.types.Node;

/**
 * Environment class
 *
 * @author Jean Barata
 */
public class Environment {

    /**
	 *
	 */
    private World _world = null;

    private int _lignes;

    private int _colonnes;

    private int _progress;

    private String _name;

    private Node _node_matrix[][];

    private List<Gate> _gates;

    protected ThreadGroup _worldThreadGroup;

    private ThreadGroup _weatherThreadGroup;

    /**
	 * Environment class constructor
	 */
    public Environment(ThreadGroup worldThreadGroup_p, World world_p) {
        _lignes = 0;
        _colonnes = 0;
        _node_matrix = null;
        _worldThreadGroup = worldThreadGroup_p;
        _world = world_p;
    }

    /**
	 *
	 */
    protected void init(XMLEnvironment environment) {
        int height = Integer.parseInt(environment.get_height());
        int width = Integer.parseInt(environment.get_width());
        String name = environment.get_name();
        _gates = new ArrayList<Gate>();
        _weatherThreadGroup = new ThreadGroup(_worldThreadGroup, "Weather on '" + name + '\'');
        IProject proj = get_world().get_configuration_key().getProject();
        String projectPath = proj.getLocation().toOSString();
        String mapfile = projectPath + File.separator + environment.get_map();
        set_lignes(height);
        set_colonnes(width);
        set_name(name);
        load_matrix(EnvironmentParser.parse(mapfile, height, width));
        load_weather_matrices();
        for (XMLEnvGate env_gate : environment.get_all_gates()) {
            Gate gate = new Gate(env_gate);
            append_gates(gate);
        }
    }

    /**
	 *
	 */
    public void load_weather_matrices() {
        XMLEnvironment env = _world.getXMLEnvironment(_name);
        if (env != null) {
            Float[][] humidity = fill_matrix(env.get_weather("HUMIDITY"));
            Float[][] temperature = fill_matrix(env.get_weather("TEMPERATURE"));
            for (int i = 0; i < _lignes; i++) {
                for (int j = 0; j < _colonnes; j++) {
                    if ((_node_matrix[i][j].getLeftNode() != null) || (_node_matrix[i][j].getRightNode() != null) || (_node_matrix[i][j].getTopNode() != null) || (_node_matrix[i][j].getBottomNode() != null)) {
                        if (humidity != null) append_weather(_node_matrix[i][j], "HUMIDITY", humidity[i][j]);
                        if (temperature != null) append_weather(_node_matrix[i][j], "TEMPERATURE", temperature[i][j]);
                    }
                }
            }
        }
    }

    /**
	 * contructs a graph from a matrix passed in parameter
	 * returns the number of created nodes in the graph
	 */
    public void load_matrix(Integer matrix[][]) {
        if (matrix != null) {
            _node_matrix = new Node[_lignes][_colonnes];
            for (int i = 0; i < _lignes; i++) {
                for (int j = 0; j < _colonnes; j++) {
                    _node_matrix[i][j] = new Node();
                    _node_matrix[i][j].setLeftNode(null);
                    _node_matrix[i][j].setRightNode(null);
                    _node_matrix[i][j].setTopNode(null);
                    _node_matrix[i][j].setBottomNode(null);
                    switch(matrix[i][j]) {
                        case 0:
                            _node_matrix[i][j].setTypeFond(Environment_background.WALL);
                            break;
                        case 1:
                            _node_matrix[i][j].setTypeFond(Environment_background.PATH);
                            break;
                        case 2:
                            _node_matrix[i][j].setTypeFond(Environment_background.GRASS);
                            break;
                        case 3:
                            _node_matrix[i][j].setTypeFond(Environment_background.ANTHILL);
                            break;
                        case 4:
                            _node_matrix[i][j].setTypeFond(Environment_background.NEST);
                            break;
                        case 5:
                            _node_matrix[i][j].setTypeFond(Environment_background.GATE);
                            break;
                        default:
                            _node_matrix[i][j].setTypeFond(Environment_background.WALL);
                    }
                    for (int k = 0; k < Node.MAX_WEATHER_SLOTS; k++) _node_matrix[i][j].setWeatherSlot(k, null);
                    for (int k = 0; k < Node.MAX_ABSTRACT_SLOTS; k++) _node_matrix[i][j].setAbstractSlot(k, null);
                    _node_matrix[i][j].setPhysicalSlot(null);
                }
            }
            for (int i = 0; i < _lignes; i++) {
                for (int j = 0; j < _colonnes; j++) {
                    _node_matrix[i][j].setTopNode((i == 0) ? null : ((matrix[i - 1][j] != 0) ? _node_matrix[i - 1][j] : null));
                    _node_matrix[i][j].setBottomNode((i == _lignes - 1) ? null : ((matrix[i + 1][j] != 0) ? _node_matrix[i + 1][j] : null));
                    _node_matrix[i][j].setLeftNode((j == 0) ? null : ((matrix[i][j - 1] != 0) ? _node_matrix[i][j - 1] : null));
                    _node_matrix[i][j].setRightNode((j == _colonnes - 1) ? null : ((matrix[i][j + 1] != 0) ? _node_matrix[i][j + 1] : null));
                }
            }
        }
    }

    /**
	 *
	 */
    public boolean append_weather(Node p, String type, float value) {
        boolean bRes = false;
        if (p != null) {
            for (int i = 0; (i < Node.MAX_WEATHER_SLOTS) && (bRes == false); i++) {
                Actor actor = p.getAbstractSlot(i);
                if ((actor != null) && (actor.get_family().equals(Weather.WEATHER_FAMILY))) {
                    Weather weather = (Weather) actor;
                    if (weather.get_type().equals(type)) bRes = true;
                }
            }
            if (bRes == false) {
                IComponentFactory hFCT1 = WildLifeCorePlugin.getDefault().getFactory(Weather.WEATHER_FAMILY, type);
                if (hFCT1 != null) {
                    Weather hACT = (Weather) hFCT1.createNewComponent(_weatherThreadGroup, _world);
                    for (int i = 0; (i < Node.MAX_ABSTRACT_SLOTS) && (bRes == false); i++) {
                        if (p.getAbstractSlot(i) == null) {
                            hACT.set_value(value);
                            p.setWeatherSlot(i, hACT);
                            bRes = true;
                        }
                    }
                }
            }
        }
        return bRes;
    }

    /**
	 * cette methode permet de retrouver une position a partir de ses coordonnees
	 */
    public Node retrieve_placement(int x, int y) {
        Node p = null;
        if ((_node_matrix != null) && (x >= 0) && (x < _colonnes) && (y >= 0) && (y < _lignes)) p = _node_matrix[y][x];
        return p;
    }

    /**
	 *
	 */
    public int card_gates() {
        return _gates.size();
    }

    /**
	 *
	 */
    public int get_lignes() {
        return _lignes;
    }

    /**
	 *
	 */
    public int get_colonnes() {
        return _colonnes;
    }

    /**
	 *
	 */
    public int get_progress() {
        return _progress;
    }

    /**
	 *
	 */
    public String get_name() {
        return _name;
    }

    /**
	 *
	 */
    public World get_world() {
        return _world;
    }

    /**
	 *
	 */
    public Node get_gates(String name, Integer x, Integer y) {
        Node p = null;
        for (int i = 0; (i < _gates.size()) && (p == null); i++) {
            if ((_gates.get(i) != null) && (_gates.get(i).name.equals(name))) {
                if (x != null) x = _gates.get(i).x;
                if (y != null) y = _gates.get(i).y;
                p = retrieve_placement(_gates.get(i).x, _gates.get(i).y);
            }
        }
        return p;
    }

    /**
	 *
	 */
    public String get_gates(int x, int y) {
        String str = "";
        for (int i = 0; (i < _gates.size()) && (str.equals("")); i++) if ((_gates.get(i) != null) && (_gates.get(i).x == x) && (_gates.get(i).y == y)) str = _gates.get(i).name;
        return str;
    }

    /**
	 *
	 */
    public void set_lignes(int lignes) {
        _lignes = lignes;
    }

    /**
	 *
	 */
    public void set_colonnes(int colonnes) {
        _colonnes = colonnes;
    }

    /**
	 *
	 */
    public void set_name(String name) {
        _name = name;
    }

    /**
	 *
	 */
    public void append_gates(Gate g) {
        _gates.add(g);
    }

    /**
	 *
	 */
    public void write_all_matrix(String path) {
        print_matrix("HUMIDITY", path);
        print_matrix("TEMPERATURE", path);
    }

    /**
	 * renvoie le cout du chemin le plus rapide entre p1 et p2
	 */
    public int cout_rapidite_chemin(Node p1, Node p2) {
        return -1;
    }

    /**
	 * renvoie le cout du chemin le plus facile entre p1 et p2
	 */
    public int cout_facilite_chemin(Node p1, Node p2) {
        return -1;
    }

    /**
	 * effectue un parcours recursif du graphe
	 * @returns the number of nodes
	 */
    int parcours_graphe(Node node) {
        int n = 1;
        node.reached = true;
        if ((node.getTopNode() != null) && (!(node.getTopNode()).reached)) n += parcours_graphe(node.getTopNode());
        if ((node.getRightNode() != null) && (!(node.getRightNode()).reached)) n += parcours_graphe(node.getRightNode());
        if ((node.getBottomNode() != null) && (!(node.getBottomNode()).reached)) n += parcours_graphe(node.getBottomNode());
        if ((node.getLeftNode() != null) && (!(node.getLeftNode()).reached)) n += parcours_graphe(node.getLeftNode());
        return n;
    }

    private float DIFF_X(Float m[][], int y, int n1, int n2) {
        return (((m[y][n1]) < (m[y][n2 - 1])) ? (((m[y][n2 - 1]) - (m[y][n1])) / (n2 - n1 - 1)) : (((m[y][n1]) - (m[y][n2 - 1])) / (n2 - n1 - 1)));
    }

    private float DIFF_Y(Float m[][], int x, int n1, int n2) {
        return (((m[n1][x]) < (m[n2 - 1][x])) ? (((m[n2 - 1][x]) - (m[n1][x])) / (n2 - n1 - 1)) : (((m[n1][x]) - (m[n2 - 1][x])) / (n2 - n1 - 1)));
    }

    private float VAL_X(Float m[][], int x, int y, int n1, int n2) {
        return (((m[y][n1]) < (m[y][n2 - 1])) ? ((m[y][x - 1]) + DIFF_X(m, y, n1, n2)) : ((m[y][x - 1]) - DIFF_X(m, y, n1, n2)));
    }

    private float VAL_Y(Float m[][], int x, int y, int n1, int n2) {
        return (((m[n1][x]) < (m[n2 - 1][x])) ? ((m[y - 1][x]) + DIFF_Y(m, x, n1, n2)) : ((m[y - 1][x]) - DIFF_Y(m, x, n1, n2)));
    }

    private float MOY_XY(Float m[][], int x, int y, int nx1, int nx2, int ny1, int ny2) {
        return ((VAL_X(m, x, y, nx1, nx2) + VAL_Y(m, x, y, ny1, ny2)) / 2);
    }

    /**
	 *
	 */
    public Float[][] fill_matrix(List<XMLEnvWeather> weather) {
        Float m[][] = new Float[_lignes][_colonnes];
        for (int i = 0; i < _lignes; i++) {
            for (int j = 0; j < _colonnes; j++) {
                m[i][j] = Float.valueOf(.0f);
            }
        }
        for (XMLEnvWeather w : weather) {
            int x = Integer.parseInt(w.get_x_position());
            int y = Integer.parseInt(w.get_y_position());
            if ((x < _colonnes) && (y < _lignes)) {
                m[y][x] = Float.parseFloat(w.get_value());
            }
        }
        int x_moy = 0, y_moy = 0;
        for (int x = 1; (x < _colonnes - 1) && (x_moy == 0); x++) if (m[0][x] != 0) x_moy = x;
        for (int y = 1; (y < _lignes - 1) && (y_moy == 0); y++) if (m[y][0] != 0) y_moy = y;
        for (int x = 1; x < x_moy; x++) {
            m[0][x] = VAL_X(m, x, 0, 0, x_moy + 1);
            m[_lignes - 1][x] = VAL_X(m, x, _lignes - 1, 0, x_moy + 1);
        }
        for (int x = x_moy + 1; x < _colonnes - 1; x++) {
            m[0][x] = VAL_X(m, x, 0, x_moy, _colonnes);
            m[_lignes - 1][x] = VAL_X(m, x, _lignes - 1, x_moy, _colonnes);
        }
        for (int y = 1; y < y_moy; y++) {
            m[y][0] = VAL_Y(m, 0, y, 0, y_moy + 1);
            m[y][_colonnes - 1] = VAL_Y(m, _colonnes - 1, y, 0, y_moy + 1);
        }
        for (int y = y_moy + 1; y < _lignes - 1; y++) {
            m[y][0] = VAL_Y(m, 0, y, y_moy, _lignes);
            m[y][_colonnes - 1] = VAL_Y(m, _colonnes - 1, y, y_moy, _lignes);
        }
        for (int y = 1; y < _lignes - 1; y++) for (int x = 1; x < _colonnes - 1; x++) m[y][x] = MOY_XY(m, x, y, 0, _colonnes, 0, _lignes);
        return m;
    }

    /**
	 *
	 */
    void print_matrix(String type, String path) {
        String buffer = path + '/' + _name + '_' + type + ".dat";
        try {
            FileWriter f = new FileWriter(buffer);
            BufferedWriter out = new BufferedWriter(f);
            for (int i = 0; i < _lignes; i++) {
                for (int j = 0; j < _colonnes; j++) {
                    if (_node_matrix[i][j] != null) {
                        boolean bFound = false;
                        for (int k = 0; (k < Node.MAX_WEATHER_SLOTS) && (bFound == false); k++) {
                            Actor actor = (_node_matrix[i][j].getWeatherSlot(k));
                            if ((actor != null) && (actor.get_family().equals(Weather.WEATHER_FAMILY))) {
                                Weather weather = (Weather) actor;
                                if (weather.get_type().equals(type)) {
                                    f.write("%d %d ", j, i);
                                    f.write(weather.get_value().toString() + "\n");
                                    bFound = true;
                                }
                            }
                        }
                    }
                }
                out.write("\n");
            }
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
