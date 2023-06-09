package jshm.gui.datamodels;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.tree.TreePath;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import jshm.Song.Sorting;
import jshm.gh.*;
import jshm.gui.GUI;
import jshm.gui.renderers.ScoresTreeCellRenderer;
import jshm.gui.renderers.TierHighlighter;

/**
 *
 * @author Tim Mullin
 */
public class GhSongDataTreeTableModel extends AbstractTreeTableModel implements Parentable, SongSortable {

    private class DataModel {

        List<Tier> tiers = new ArrayList<Tier>();

        public DataModel() {
            for (int i = 1; i <= game.getTierCount(sorting); i++) {
                tiers.add(new Tier(game.getTierName(sorting, i)));
            }
            for (GhSong song : songs) {
                tiers.get(song.getTierLevel(sorting) - 1).songs.add(song);
            }
        }
    }

    public class Tier implements ModelTier {

        public final String name;

        public final List<GhSong> songs = new ArrayList<GhSong>();

        public Tier(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return String.format("%s (%s song%s)", name, songs.size(), songs.size() == 1 ? "" : "s");
        }
    }

    private final GhGame game;

    private final List<GhSong> songs;

    private Sorting sorting;

    private DataModel model;

    public GhSongDataTreeTableModel(final GhGame game, final Sorting sorting, final List<GhSong> songs) {
        super("ROOT");
        this.game = game;
        this.songs = songs;
        setSorting(sorting);
    }

    public void setSorting(Sorting sorting) {
        Collections.sort(songs, game.getSortingComparator(sorting));
        TreePath tp = new TreePath(root);
        int[] indices = null;
        Object[] children = null;
        if (null != this.sorting) {
            indices = new int[getChildCount(root)];
            children = new Object[indices.length];
            for (int i = 0; i < indices.length; i++) {
                indices[i] = i;
                children[i] = getChild(root, i);
            }
            modelSupport.fireChildrenRemoved(tp, indices, children);
        }
        this.sorting = sorting;
        this.model = new DataModel();
        indices = new int[getChildCount(root)];
        children = new Object[indices.length];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = i;
            children[i] = getChild(root, i);
        }
        modelSupport.fireChildrenAdded(tp, indices, children);
    }

    public void setParent(GUI gui, JXTreeTable parent) {
        parent.setTreeCellRenderer(new ScoresTreeCellRenderer());
        parent.setHighlighters(HighlighterFactory.createSimpleStriping(), new TierHighlighter());
        parent.getColumnExt(1).setPrototypeValue("00000");
        for (int i = 2; i <= 7; i++) parent.getColumnExt(i).setPrototypeValue("00000000");
        parent.setAutoResizeMode(JXTreeTable.AUTO_RESIZE_OFF);
        parent.packAll();
    }

    public void removeParent(JXTreeTable parent) {
    }

    @Override
    public int getColumnCount() {
        return 8;
    }

    private static final String[] COLUMN_NAMES = { "Song", "Notes", "Base Score", "4* Cutoff", "5* Cutoff", "6* Cutoff", "7* Cutoff", "8* Cutoff" };

    @Override
    public String getColumnName(int arg0) {
        return COLUMN_NAMES[arg0];
    }

    private final DecimalFormat NUM_FMT = new DecimalFormat("#,###");

    @Override
    public Object getValueAt(Object node, int column) {
        if (node instanceof Tier) {
            if (column == 0) return node;
            return "";
        }
        if (!(node instanceof GhSong)) return "";
        GhSong song = (GhSong) node;
        Object ret = "";
        switch(column) {
            case 0:
                ret = song;
                break;
            case 1:
                ret = NUM_FMT.format(song.getNoteCount());
                break;
            case 2:
                ret = NUM_FMT.format(song.getBaseScore());
                break;
            case 3:
                ret = NUM_FMT.format(song.getFourStarCutoff());
                break;
            case 4:
                ret = NUM_FMT.format(song.getFiveStarCutoff());
                break;
            case 5:
                ret = NUM_FMT.format(song.getSixStarCutoff());
                break;
            case 6:
                ret = NUM_FMT.format(song.getSevenStarCutoff());
                break;
            case 7:
                ret = NUM_FMT.format(song.getEightStarCutoff());
                break;
            default:
                assert false;
        }
        if ("-1".equals(ret)) ret = "???";
        return ret;
    }

    @Override
    public Object getChild(Object parent, int index) {
        if (parent.equals(root)) {
            return model.tiers.get(index);
        }
        if (parent instanceof Tier) {
            return ((Tier) parent).songs.get(index);
        }
        return null;
    }

    @Override
    public int getChildCount(Object parent) {
        if (parent.equals(root)) {
            return model.tiers.size();
        }
        if (parent instanceof Tier) {
            return ((Tier) parent).songs.size();
        }
        return 0;
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if (parent.equals(root)) {
            return model.tiers.indexOf(child);
        }
        if (parent instanceof Tier) {
            if (!(child instanceof GhSong)) return -1;
            return ((Tier) parent).songs.indexOf(child);
        }
        return -1;
    }

    @Override
    public boolean isLeaf(Object node) {
        return node instanceof GhSong;
    }
}
