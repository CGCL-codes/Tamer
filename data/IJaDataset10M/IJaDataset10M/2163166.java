package pokeglobal.server.mechanics.moves;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.Root;

/**
 *
 * @author Colin
 */
@Root
public class MoveSetData {

    /**
     * Set of all move sets.
     */
    @ElementArray
    private MoveSet[] m_movesets = null;

    /**
     * Save the move sets to a file.
     */
    public void saveToFile(File f) {
        try {
            FileOutputStream file = new FileOutputStream(f);
            saveToFile(file);
            file.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    /**
     * Save the move sets to an arbitrary output stream.
     */
    public void saveToFile(OutputStream output) {
        try {
            ObjectOutputStream obj = new ObjectOutputStream(output);
            obj.writeObject(m_movesets);
            obj.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Prune the move set of null entries.
     * This is slow and shoddy.
     */
    public void pruneMoveSet() {
        for (int i = 0; i < m_movesets.length; ++i) {
            if (m_movesets[i] == null) {
                continue;
            }
            String[][] categories = m_movesets[i].getMoves();
            for (int j = 0; j < categories.length; ++j) {
                ArrayList moves = new ArrayList(Arrays.asList(categories[j]));
                Iterator k = moves.iterator();
                while (k.hasNext()) {
                    if (k.next() == null) {
                        k.remove();
                    }
                }
                categories[j] = (String[]) moves.toArray(new String[moves.size()]);
            }
        }
    }

    /**
     * Load the move sets in from a URL.
     */
    public void loadFromFile(URL url) {
        try {
            InputStream input = url.openStream();
            loadFromFile(input);
            input.close();
        } catch (IOException e) {
        }
    }

    /**
     * Load the move sets in from a file.
     */
    public void loadFromFile(String str) {
        try {
            File f = new File(str);
            InputStream file = new FileInputStream(f);
            loadFromFile(file);
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Load the move sets from an input stream.
     */
    public void loadFromFile(InputStream file) {
        try {
            ObjectInputStream obj = new ObjectInputStream(file);
            m_movesets = (MoveSet[]) obj.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return the number of move sets.
     */
    public int getMoveSetCount() {
        return m_movesets.length;
    }

    /**
     * Merge the full advance and d/p move databases together.
     */
    public static void main(String[] args) throws Exception {
        class Pair {

            public String first, second;

            Pair(String f, String s) {
                first = f;
                second = s;
            }
        }
        Pair[] changes = { new Pair("Ancient Power", "Ancientpower"), new Pair("Bubble Beam", "Bubblebeam"), new Pair("Double Edge", "Double-edge"), new Pair("Self Destruct", "Selfdestruct"), new Pair("Sketch", null), new Pair("Snore Swagger", "Swagger"), new Pair("Will-O-Wisp", "Will-o-wisp"), new Pair("Lock-On", "Lock-on"), new Pair("X-scissor", "X-Scissor"), new Pair("Sand-attack", "Sand-Attack"), new Pair("Fly~ Surf + Reversal", null), new Pair("Roar of Time", "Roar Of Time"), new Pair("Mud-slap", "Mud-Slap") };
        pokeglobal.server.battle.ModData data = pokeglobal.server.battle.ModData.getDefaultData();
        data.applyPatch(new FileInputStream(new File("movesetfixes.patch")), 1);
        MoveSetData moves = data.getMoveSetData();
        MoveSetData dp = new MoveSetData();
        dp.loadFromFile("dpmovesets.db");
        moves.pruneMoveSet();
        dp.pruneMoveSet();
        data.getMoveSetData().pruneMoveSet();
        int count = data.getMoveSetData().getMoveSetCount();
        for (int i = 0; i < count; ++i) {
            MoveSet set = data.getMoveSetData().getMoveSet(i);
            for (int j = 0; j < 5; ++j) {
                if (j == 3) continue;
                String[] strs = set.getMoves()[j];
                if (strs == null) continue;
                for (int k = 0; k < strs.length; ++k) {
                    for (int l = 0; l < changes.length; ++l) {
                        if (strs[k].equals(changes[l].first)) {
                            strs[k] = changes[l].second;
                            break;
                        }
                    }
                }
            }
        }
        data.getMoveSetData().saveToFile(new File("dpmovesets.db"));
    }

    /**
     * Get the move set identified by the parameter.
     */
    public MoveSet getMoveSet(int i) throws IllegalArgumentException {
        if ((i < 0) || (i >= m_movesets.length)) {
            throw new IllegalArgumentException("Index out of range.");
        }
        return m_movesets[i];
    }
}
