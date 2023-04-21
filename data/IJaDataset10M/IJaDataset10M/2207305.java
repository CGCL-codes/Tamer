package org.eyrene.smile3d.ui;

import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import javax.vecmath.Vector3d;
import ncsa.j3d.loaders.ModelLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eyrene.javaj.io.IOMgr;
import org.eyrene.javaj.io.ResourceMgr;
import com.sun.j3d.loaders.Loader;
import com.sun.j3d.loaders.Scene;

/**
 * <p>Title: View3DManager.java</p>
 * <p>Description: </p> 
 * The user supplies the name of a 3D object file to be loaded.
 * Its bounding sphere is automatically scaled to have a radius 
 *  of 1 unit, and rotated -90 around x-axis if it is a 3ds model.
 * A large range of different 3D object formats can be loaded
 *  since we are using the Portfolio loaders.
 * 
 * Once loaded, the image can be moved and rotated along the
 *  X, Y, and Z axes, and scaled. The resulting position, 
 *  rotation, and scaling information can be stored in a 
 *  'status' file (which has the same name as the 3D file + STATUS_SUFFIX).
 * The rotation information is stored as a series of rotation numbers 
 *  which must be executed in order to get to the curent overall rotation:
 *  1 = positive ROT_INCR around x-axis
 *  2 = negative ROT_INCR around x-axis
 *  3 = positive ROT_INCR around y-axis
 *  4 = negative ROT_INCR around y-axis
 *  5 = positive ROT_INCR around z-axis
 *  6 = negative ROT_INCR around z-axis
 *  
 * This approach is used to try to avoid the problem that a mix of rotations 
 *  about different axes do not produce the same result if carried out in different orders. 
 * 
 *  The loaded object is hung off several TGs, and the top one can be accessed 
 *   by calling getRoot() on the View3D element created.
 *   
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: eyrene</p>
 * @author Francesco Vadicamo
 * @version 1.0
 * 
 * TODO i metodi load e store realizzati per ObjectView3D vanno fatti per RoomView3D
 * TODO potrebbe esistere un metodo di attach tra View3D 
 */
public class View3DManager {

    public static final Log log = LogFactory.getLog(View3DManager.class);

    public static final String DEFAULT_STATUS_SUFFIX = ".txt";

    public static final String AVATARS_PATH = "avatars/";

    private static View3DManager instance;

    /**
     * Default Contructor
     */
    private View3DManager() {
    }

    public static View3DManager getInstance() {
        if (instance == null) instance = new View3DManager();
        return instance;
    }

    /**
     * Loads the speficied 3D object using the specified loader
     * 
     * The loaded object has 4 transform groups above it -- boundsTG is
     * for adjusting the object's bounded sphere so it is centered at (0,0,0) and has unit radius 
     * The other TGs are for doing separate moves, rotations, and scaling of the object
     * moveTG --> rotTG --> scaleTG --> boundsTG --> sceneBG -> object
     * 
     * @param loader the loader to use
     * @param filename model filename
     * @param resume true if exist a status file associated to model
     * 
     * @return the View3D loaded from the specified model file
     */
    public View3D loadView3D(Loader loader, String filename, boolean resume) throws View3DLoadingException {
        if (loader == null || filename == null) throw new NullPointerException("PRE-CONDIZIONE VIOLATA!");
        log.info("Loading '" + filename + "'...");
        Scene s = null;
        try {
            s = loader.load(filename);
        } catch (Exception e1) {
            try {
                s = loader.load(new URL(filename));
            } catch (Exception e2) {
                try {
                    e2.initCause(e1);
                    s = loader.load(new InputStreamReader(ResourceMgr.loadResourceAsStream(getClass(), filename)));
                } catch (Exception e3) {
                    try {
                        e3.initCause(e2);
                        s = loader.load(new InputStreamReader(ResourceMgr.loadResourceAsStream(getClass(), AVATARS_PATH + filename)));
                    } catch (Exception e4) {
                        try {
                            e4.initCause(e3);
                            s = loader.load(new InputStreamReader(ResourceMgr.loadResource(getClass(), filename).openStream()));
                        } catch (Exception e5) {
                            try {
                                e5.initCause(e4);
                                s = loader.load(new InputStreamReader(ResourceMgr.loadResource(getClass(), AVATARS_PATH + filename).openStream()));
                            } catch (Exception e6) {
                                e6.initCause(e5);
                                throw new View3DLoadingException("An error occourred while loading the model: " + e5, e5);
                            }
                        }
                    }
                }
            }
        }
        ObjectView3D view3d = new ObjectView3D(s.getSceneGroup());
        if (IOMgr.getExtension(filename).equalsIgnoreCase("3ds")) view3d.rotate(ObjectView3D.X_AXIS, -Math.PI / 2.0);
        if (resume) loadView3DStatus(view3d, IOMgr.getPath(filename) + IOMgr.getName(filename) + DEFAULT_STATUS_SUFFIX);
        log.info("...'" + filename + "' loaded!");
        return view3d;
    }

    /**
     * Loads the speficied 3D object using the model loader
     * 
     * The loaded object has 4 transform groups above it -- boundsTG is
     * for adjusting the object's bounded sphere so it is centered at (0,0,0) and has unit radius 
     * The other TGs are for doing separate moves, rotations, and scaling of the object
     * moveTG --> rotTG --> scaleTG --> boundsTG --> sceneBG -> object
     * 
     * @param filename model filename
     * @param resume true if exist a status file associated to model
     * 
     * @return the View3D loaded from the specified model file
     */
    public View3D loadModel3D(String filename, boolean resume) throws View3DLoadingException {
        return loadView3D(new ModelLoader(), filename, resume);
    }

    /**
     * Loads the speficied 3D object using the model loader
     * 
     * The loaded object has 4 transform groups above it -- boundsTG is
     * for adjusting the object's bounded sphere so it is centered at (0,0,0) and has unit radius 
     * The other TGs are for doing separate moves, rotations, and scaling of the object
     * moveTG --> rotTG --> scaleTG --> boundsTG --> sceneBG -> object
     * 
     * @param filename model filename
     * 
     * @return the View3D loaded from the specified model file
     */
    public View3D loadModel3D(String filename) throws View3DLoadingException {
        return loadModel3D(filename, false);
    }

    /**
     * Obtain status info from the status file associated to filename
     * The status file has the format:
     *  <3D object filename>
     *  [-p px py pz]
     *  [-r sequence of numbers]
     *  [-s scale]
     *  
     *  @param statusFile the status file to load
     */
    public void loadView3DStatus(ObjectView3D view3d, String statusFile) {
        String ext = IOMgr.getExtension(statusFile);
        if (ext.equalsIgnoreCase("txt")) {
            int num_line = 0;
            try {
                BufferedReader br = new BufferedReader(new FileReader(statusFile));
                br.readLine();
                num_line++;
                String line;
                char ch;
                while ((line = br.readLine()) != null) {
                    num_line++;
                    ch = line.charAt(1);
                    if (ch == 'p') setCurrentPosition(view3d, line); else if (ch == 'r') setCurrentRotation(view3d, line); else if (ch == 's') setCurrentScale(view3d, line); else log.error(statusFile + ": did not recognise line " + num_line + ": " + line);
                }
                br.close();
                log.info("Status file '" + statusFile + "' readed (" + num_line + " lines imported!)");
            } catch (IOException e) {
                log.error("Error reading status file '" + statusFile + "' (" + num_line + " lines imported!)");
            }
        } else if (ext.equalsIgnoreCase("xml")) {
            throw new UnsupportedOperationException("Operation not yet implemented!");
        } else {
            throw new IllegalArgumentException("Invalid status file name: '" + statusFile + "'");
        }
    }

    /**
     * Extract the (x,y,z) position info from the status file, then apply it to the loaded object
     */
    private void setCurrentPosition(ObjectView3D view3d, String line) {
        String[] values = line.split("\\s+");
        double vals[] = new double[3];
        int count = 0;
        for (int i = 1; i < values.length && i < vals.length; i++) {
            try {
                vals[count] = Double.parseDouble(values[i]);
                count++;
            } catch (NumberFormatException ex) {
                log.error("Skipping invalid position coordinate (double is required!): " + values[i]);
            }
        }
        if (count != 3) log.error("Invalid position data in the status file (3D coordinates required!): " + line);
        view3d.move(new Vector3d(vals[0], vals[1], vals[2]));
    }

    /**
     * Extract the rotation info from the status file, and apply it to the loaded object
     */
    private void setCurrentRotation(ObjectView3D view3d, String line) {
        String[] values = line.split("\\s+");
        if (values.length < 2) return;
        int rot;
        for (int i = 0; i < values[1].length(); i++) {
            try {
                rot = Character.digit(values[1].charAt(i), 10);
            } catch (NumberFormatException ex) {
                log.error("Skipping invalid rotation value: " + values[1].charAt(i));
                continue;
            }
            switch(rot) {
                case 1:
                    view3d.rotate(ObjectView3D.X_AXIS, ObjectView3D.INCR);
                    break;
                case 2:
                    view3d.rotate(ObjectView3D.X_AXIS, ObjectView3D.DECR);
                    break;
                case 3:
                    view3d.rotate(ObjectView3D.Y_AXIS, ObjectView3D.INCR);
                    break;
                case 4:
                    view3d.rotate(ObjectView3D.Y_AXIS, ObjectView3D.DECR);
                    break;
                case 5:
                    view3d.rotate(ObjectView3D.Z_AXIS, ObjectView3D.INCR);
                    break;
                case 6:
                    view3d.rotate(ObjectView3D.Z_AXIS, ObjectView3D.DECR);
                    break;
                default:
                    log.error("Skipping invalid rotation value: " + values[1].charAt(i));
                    break;
            }
        }
    }

    /**
     * Extract the scale info from the status file, and apply it to the loaded object
     */
    private void setCurrentScale(ObjectView3D view3d, String line) {
        String[] values = line.split("\\s+");
        if (values.length < 2) return;
        double startScale;
        try {
            startScale = Double.parseDouble(values[1]);
        } catch (NumberFormatException ex) {
            log.error("Skipping invalid scale value: " + values[1]);
            startScale = 1.0;
        }
        if (startScale != 1.0) {
            view3d.scale(startScale);
        }
    }

    /**
     * Saves the object status to the specified file
     */
    public void saveView3DStatus(ObjectView3D view3d, String statusFile) {
        if (statusFile == null) throw new NullPointerException("PRE-CONDIZIONE VIOLATA!");
        if (statusFile.trim().equals("") || statusFile.trim().startsWith(".") || IOMgr.getExtension(statusFile).trim().endsWith("")) throw new IllegalArgumentException("PRE-CONDIZIONE VIOLATA!");
        DecimalFormat df = new DecimalFormat("0.###");
        try {
            PrintWriter out = new PrintWriter(new FileWriter(statusFile));
            out.println(view3d.getName());
            Vector3d currLoc = view3d.getPosition();
            out.println("-p " + df.format(currLoc.x) + " " + df.format(currLoc.y) + " " + df.format(currLoc.z));
            out.print("-r ");
            for (int i = 0; i < view3d.info.rotHistory.size(); i++) out.print("" + view3d.info.rotHistory.get(i));
            out.println("");
            out.println("-s " + df.format(view3d.info.scale));
            out.close();
            log.info("Status saved on file: " + statusFile);
        } catch (IOException e) {
            log.error("Error writing to status file: " + statusFile, e);
        }
    }
}

class View3DLoadingException extends Exception {

    public View3DLoadingException() {
        super();
    }

    public View3DLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public View3DLoadingException(String message) {
        super(message);
    }

    public View3DLoadingException(Throwable cause) {
        super(cause);
    }
}
