package net.sf.mogbox.chocobo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.mogbox.pol.ffxi.DataFile;
import net.sf.mogbox.pol.ffxi.loader.DataBlock;
import net.sf.mogbox.pol.ffxi.loader.DataBlockLoader;
import net.sf.mogbox.pol.ffxi.loader.Loader;
import net.sf.mogbox.renderer.FFXIModel;
import net.sf.mogbox.renderer.FFXIMotion;
import net.sf.mogbox.renderer.FFXISkeleton;
import net.sf.mogbox.renderer.FFXITexture;
import net.sf.mogbox.renderer.engine.scene.Node;
import net.sf.mogbox.renderer.engine.scene.SkeletonNode;
import net.sf.mogbox.renderer.engine.scene.SkinNode;
import net.sf.mogbox.renderer.engine.scene.state.TextureState;

public class Chocobo extends Node {

    private static Logger log = Logger.getLogger(Chocobo.class.getName());

    public static final int BODY = 0;

    public static final int FEET = 1;

    public static final int HEAD = 2;

    public static final int TAIL = 3;

    public static final int ARMOR = 4;

    private static final int[] MAX = { 8, 8, 8, 4, 4 };

    private static final int[] SKELETONS = { 0xD7FF, 0xD821, 0xD843, 0xD865, 0xD887 };

    private static final int[][] OFFSETS = { { 0xD801, 0xD809, 0xD811, 0xD819, 0xD81D }, { 0xD823, 0xD82B, 0xD833, 0xD83B, 0xD83F }, { 0xD845, 0xD84D, 0xD855, 0xD85D, 0xD861 }, { 0xD867, 0xD86F, 0xD877, 0xD87F, 0xD883 }, { 0xD889, 0xD891, 0xD899, 0xD8A1, 0xD8A5 } };

    private static final int[][] DUMMIES = { { 0x548C, 0x548E, 0x5490, 0x5492, 0x5492 }, { 0x5495, 0x5497, 0x5499, 0x549B, 0x549B }, { 0x549E, 0x54A0, 0x54A2, 0x54A4, 0x54A4 }, { 0x5529, 0x552B, 0x552D, 0x552F, 0x552F }, { 0x5531, 0x5533, 0x5535, 0x5537, 0x5537 } };

    private int breed = -1;

    private int[] ids = { -1, -1, -1, -1, -1 };

    private int[] tmp = { -1, -1, -1, -1, -1 };

    private SkeletonNode skeleton;

    private Node[] nodes = new Node[5];

    private Field[] fields = new Field[5];

    private StateListener listener = new ControlStateListener();

    public void setBreed(final int breed) {
        if (breed < -1 || breed > 5) throw new IndexOutOfBoundsException();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i] != null) fields[i].setBreed(breed);
        }
        applyOverrides(ids);
        this.breed = breed;
        preUpdateExec(new Callable<Object>() {

            public Object call() {
                try {
                    performSetBreed(breed);
                } catch (Throwable t) {
                    log.log(Level.SEVERE, null, t);
                }
                return null;
            }
        });
    }

    public void set(int slot, int id) {
        if (id < 0 || id > getMax(slot)) throw new IndexOutOfBoundsException();
        if (fields[slot] != null) {
            fields[slot].setSelection(id);
            id = fields[slot].getSelection();
        }
        for (int i = 0; i < ids.length; i++) {
            if (i == slot) tmp[i] = id; else tmp[i] = ids[i];
        }
        applyOverrides(tmp);
        preUpdateExec(new Callable<Object>() {

            public Object call() throws IOException {
                for (int i = 0; i < ids.length; i++) {
                    if (tmp[i] >= -1) {
                        ids[i] = tmp[i];
                        performSet(i, ids[i]);
                    }
                }
                return null;
            }
        });
    }

    public void enable(final int slot, final boolean enabled) {
        if (fields[slot] != null) {
            fields[slot].setEnabled(enabled);
        } else if (!enabled) {
            preUpdateExec(new Callable<Object>() {

                public Object call() {
                    try {
                        performSet(slot, -1);
                    } catch (Throwable t) {
                        log.log(Level.SEVERE, null, t);
                    }
                    return null;
                }
            });
        }
    }

    public void setField(final int slot, final Field field) {
        if (fields[slot] != null) fields[slot].removeStateListener(listener);
        if (field != null) {
            field.addStateListener(listener);
            field.setBreed(breed);
            field.setSelection(ids[slot]);
        }
        fields[slot] = field;
    }

    public static int getMax(int slot) {
        return MAX[slot];
    }

    public static boolean isUsed(int race, int slot, int id) {
        int file = DataFile.lookupFileNumber(getDatID(race, slot, id));
        if (file < 0) return false;
        if (id == 0) return true;
        return (file & 0xFFFF) != DUMMIES[race][slot];
    }

    private void performSetBreed(int breed) throws IOException {
        if (skeleton != null) {
            remove(skeleton);
            skeleton = null;
        }
        if (breed < 0) return;
        DataFile dat = new DataFile(SKELETONS[breed]);
        log.log(Level.INFO, "[CHOCOBO] " + dat);
        if (!dat.exists()) return;
        FileChannel c = dat.getFileChannel();
        Loader<?> loader = Loader.newInstance(c);
        SkeletonNode temp = null;
        if (loader instanceof DataBlockLoader) {
            for (DataBlock b : (DataBlockLoader) loader) {
                if (b.getType() == DataBlock.SKELETON) {
                    temp = new FFXISkeleton(b.getData());
                    break;
                }
            }
        }
        if (temp == null) return;
        c.position(0);
        if (loader instanceof DataBlockLoader) {
            for (DataBlock b : (DataBlockLoader) loader) {
                switch(b.getType()) {
                    case DataBlock.MOTION:
                        if (temp != null) {
                            int n = b.getID();
                            StringBuilder name = new StringBuilder();
                            name.append((char) (n & 0xFF)).append((char) (n >> 8 & 0xFF)).append((char) (n >> 16 & 0xFF));
                            temp.addMotion(name.toString(), n >> 24 & 0xFF, new FFXIMotion(b.getData()));
                        }
                        break;
                }
            }
        }
        while (c.isOpen()) {
            try {
                c.close();
            } catch (IOException e) {
                log.log(Level.WARNING, null, e);
            }
        }
        if (temp != null) {
            skeleton = temp;
            skeleton.setMotion("chi");
            add(skeleton);
        }
        for (int i = 0; i < ids.length; i++) performSet(i, ids[i]);
    }

    private void performSet(int slot, int id) throws IOException {
        if (breed < 0) return;
        if (skeleton == null) return;
        if (nodes[slot] != null) {
            skeleton.remove(nodes[slot]);
            nodes[slot] = null;
        }
        if (id != -1) {
            DataFile dat = new DataFile(getDatID(breed, slot, id));
            nodes[slot] = loadModel(dat);
            if (nodes[slot] != null) skeleton.add(nodes[slot]);
        }
    }

    private static int getDatID(int breed, int slot, int id) {
        if (id < 0 || id > getMax(slot)) throw new IndexOutOfBoundsException();
        return OFFSETS[breed][slot] + id;
    }

    private Node loadModel(DataFile dat) throws IOException, FileNotFoundException {
        log.log(Level.INFO, "[CHOCOBO] " + dat);
        if (!dat.exists()) return null;
        SkinNode node = new SkinNode();
        FileChannel c = dat.getFileChannel();
        Loader<?> loader = Loader.newInstance(c);
        Map<String, TextureState> textures = new HashMap<String, TextureState>();
        if (loader instanceof DataBlockLoader) {
            for (DataBlock b : (DataBlockLoader) loader) {
                try {
                    switch(b.getType()) {
                        case DataBlock.TEXTURE:
                            FFXITexture t = new FFXITexture(b.getData());
                            textures.put(t.getName(), t);
                            break;
                        case DataBlock.MODEL:
                            node.add(new FFXIModel(b.getData(), textures));
                            break;
                    }
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }
        while (c.isOpen()) {
            try {
                c.close();
            } catch (IOException e) {
                log.log(Level.WARNING, null, e);
            }
        }
        return node;
    }

    private void applyOverrides(int[] ids) {
        int[] overrides = new int[fields.length];
        for (int i = 0; i < fields.length; i++) {
            overrides[i] = -1;
        }
        for (int i = 0; i < fields.length; i++) {
            if (overrides[i] >= 0) continue;
            int[] o = fields[i].getOverrides(fields[i].getSelection());
            if (o == null) continue;
            for (int j = 0; j < o.length; j++) {
                overrides[j] = o[j];
            }
        }
        for (int i = 0; i < fields.length; i++) {
            fields[i].setOverride(overrides[i]);
            if (overrides[i] >= 0) ids[i] = overrides[i]; else if (fields[i].isEnabled()) ids[i] = fields[i].getSelection(); else ids[i] = -1;
        }
    }

    private class ControlStateListener implements StateListener {

        @Override
        public void stateChanged(StateEvent e) {
            Field source = (Field) e.getSource();
            int slot = source.getSlot();
            final int[] tmp = new int[10];
            for (int i = 0; i < ids.length; i++) {
                if (i == slot) tmp[i] = source.getSelection(); else tmp[i] = ids[i];
            }
            applyOverrides(tmp);
            preUpdateExec(new Callable<Object>() {

                public Object call() throws IOException {
                    for (int i = 0; i < ids.length; i++) {
                        if (ids[i] != tmp[i]) {
                            ids[i] = tmp[i];
                            performSet(i, tmp[i]);
                        }
                    }
                    return null;
                }
            });
        }
    }
}
