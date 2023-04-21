package tuner3d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import javax.swing.JOptionPane;
import tuner3d.ds.AvlTree;
import tuner3d.ds.Pair;
import tuner3d.genome.Genome;
import tuner3d.genome.Markable;
import tuner3d.genome.Parameter;
import tuner3d.graphics.LinkedLine;
import tuner3d.graphics.Palette;
import tuner3d.io.BLASTFiles;
import tuner3d.io.ColumnNo;
import tuner3d.io.FastaFile;
import tuner3d.io.GenbankFile;
import tuner3d.io.EMBLFile;
import tuner3d.io.AlignmentFile;
import tuner3d.io.MUMmerFiles;
import tuner3d.io.MauveFiles;
import tuner3d.io.TabbedTextFile;
import tuner3d.ui.dialogs.TaskBar;
import tuner3d.util.Misc;

public class Document extends Observable {

    public static final float BASE_LEVEL = 30.0f;

    private int num = 0;

    private int id = 0;

    private float level = BASE_LEVEL;

    private LinkedList<Genome> genomes;

    private ArrayList<Integer> genomeSizes;

    private ArrayList<String> genomeNames;

    private ArrayList<LinkedLine> links;

    private Pair<String> orthologsHistory;

    private ArrayList<Pair<String>> homologsHistory;

    public static final String gbFile = "I:\\samples\\AP009180.gb";

    public static final String emblFile = "I:\\samples\\AP009180.embl";

    private Parameter parameter;

    private Palette palette;

    private Object currentNode;

    private Object[] currentNodes;

    public Document() {
        parameter = new Parameter();
        palette = new Palette();
        genomes = new LinkedList<Genome>();
        links = new ArrayList<LinkedLine>();
        genomeNames = new ArrayList<String>(Parameter.MICROBIAL_MAX_GENOMES);
        genomeSizes = new ArrayList<Integer>(Parameter.MICROBIAL_MAX_GENOMES);
        orthologsHistory = new Pair<String>();
        homologsHistory = new ArrayList<Pair<String>>();
    }

    /**
	 * test if the given file has an extension indicating it is a sequence file
	 * @param type - the extension of the file
	 * @return true if it's sequence
	 */
    public static boolean isSequenceType(String type) {
        return type.equals("gb") || type.equals("gbk") || type.equals("embl") || type.equals("txt") || type.equals("fas") || type.equals("fasta");
    }

    public void openFile(String path, String type, TaskBar taskBar) {
        if (type.equals("gb") || type.equals("gbk")) new GenbankFile(path, new Genome(parameter), this, taskBar).run(); else if (type.equals("embl") || type.equals("txt")) new EMBLFile(path, new Genome(parameter), this, taskBar).run(); else if (type.equals("fas") || type.equals("fasta")) new FastaFile(path, new Genome(parameter), this, taskBar).run(); else JOptionPane.showMessageDialog(null, "Not readable sequence type", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void openFileAsync(String path, String type) {
        if (type.equals("gb") || type.equals("gbk")) new Thread(new GenbankFile(path, new Genome(parameter), this)).start(); else if (type.equals("embl") || type.equals("txt")) new Thread(new EMBLFile(path, new Genome(parameter), this)).start(); else if (type.equals("fas") || type.equals("fasta")) new Thread(new FastaFile(path, new Genome(parameter), this)).start(); else JOptionPane.showMessageDialog(null, "Not readable sequence type", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void openFile(String path, byte type, int[] ids) {
        try {
            switch(type) {
                case AlignmentFile.BLAST_FILE:
                    new Thread(new BLASTFiles(path, new Genome[] { getGenomeById(ids[0]), getGenomeById(ids[1]) }, this)).start();
                    break;
                case AlignmentFile.MUMMER_FILE:
                    new Thread(new MUMmerFiles(path, new Genome[] { getGenomeById(ids[0]), getGenomeById(ids[1]) }, this)).start();
                    break;
                case AlignmentFile.MAUVE_FILE:
                    new Thread(new MauveFiles(path, getGenomesByIds(ids), this)).start();
                    break;
                default:
                    break;
            }
        } catch (IndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "Genome not found", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void openFile(String path, byte type, int id, ColumnNo columnNo) {
        new Thread(new TabbedTextFile(type, columnNo, path, getGenomeById(id), this)).start();
    }

    public void openFilesAsync(String[] paths, String[] types) {
        if (paths.length == 0) return;
        if (paths.length == 1) {
            openFileAsync(paths[0], types[0]);
            return;
        }
        if (types[0].equals("gb") || types[0].equals("gbk")) new Thread(new GenbankFile(paths[0], Misc.stringcopy(paths, 1, paths.length - 1), Misc.stringcopy(types, 1, types.length - 1), new Genome(parameter), this)).start(); else if (types[0].equals("embl") || types[0].equals("txt")) new Thread(new EMBLFile(paths[0], Misc.stringcopy(paths, 1, paths.length - 1), Misc.stringcopy(types, 1, types.length - 1), new Genome(parameter), this)).start(); else if (types[0].equals("fas") || types[0].equals("fasta")) new Thread(new FastaFile(paths[0], Misc.stringcopy(paths, 1, paths.length - 1), Misc.stringcopy(types, 1, types.length - 1), new Genome(parameter), this)).start(); else JOptionPane.showMessageDialog(null, "Not readable sequence type", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void openFiles(String[] paths, String[] types, TaskBar taskBar) {
        if (paths.length == 0) return;
        if (paths.length == 1) {
            openFile(paths[0], types[0], taskBar);
            taskBar.exit();
            return;
        }
        if (types[0].equals("gb") || types[0].equals("gbk")) new GenbankFile(paths[0], Misc.stringcopy(paths, 1, paths.length - 1), Misc.stringcopy(types, 1, types.length - 1), new Genome(parameter), this, taskBar).run(); else if (types[0].equals("embl") || types[0].equals("txt")) new EMBLFile(paths[0], Misc.stringcopy(paths, 1, paths.length - 1), Misc.stringcopy(types, 1, types.length - 1), new Genome(parameter), this, taskBar).run(); else if (types[0].equals("fas") || types[0].equals("fasta")) new FastaFile(paths[0], Misc.stringcopy(paths, 1, paths.length - 1), Misc.stringcopy(types, 1, types.length - 1), new Genome(parameter), this, taskBar).run(); else JOptionPane.showMessageDialog(null, "Not readable sequence type", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void openFile(String path, byte type, String[] names) {
        openFile(path, type, getIdsByNames(names));
    }

    public void openFiles(String[] paths, byte[] types, int id, ColumnNo[] columnNos) {
        for (int i = 0; i < paths.length; i++) openFile(paths[i], types[i], id, columnNos[i]);
    }

    public Document getDocument() {
        return this;
    }

    public int getNumOfGenomes() {
        return this.num;
    }

    public LinkedList<Genome> getGenomes() {
        return genomes;
    }

    public int getId() {
        return id;
    }

    /**
	 * convert genome name to its id
	 * @param name genome name
	 * @return its id
	 */
    public int getIdByName(String name) {
        for (int i = 0; i < genomeNames.size(); i++) {
            if (name.equals(genomeNames.get(i))) return i;
        }
        return -1;
    }

    /**
	 * convert genome names to their ids
	 * @param names genome names
	 * @return an array holds their ids 
	 */
    public int[] getIdsByNames(String[] names) {
        int[] ids = new int[names.length];
        for (int i = 0; i < names.length; i++) {
            ids[i] = getIdByName(names[i]);
        }
        return ids;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Genome getCurrentGenome() {
        if (num == 0) return null;
        return genomes.get(id < 0 ? 0 : id);
    }

    public Genome getGenomeById(int id) throws IndexOutOfBoundsException {
        return genomes.get(id);
    }

    public Genome[] getGenomesByIds(int[] ids) throws IndexOutOfBoundsException {
        Genome[] genomes = new Genome[ids.length];
        for (int i = 0; i < ids.length; i++) {
            genomes[i] = getGenomeById(i);
        }
        return genomes;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public Palette getPalette() {
        return palette;
    }

    public Object getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(Object currentNode) {
        this.currentNode = currentNode;
        setChanged();
        notifyObservers();
    }

    public Object[] getCurrentNodes() {
        return currentNodes;
    }

    public void setCurrentNodes(Object[] currentNodes) {
        this.currentNodes = currentNodes;
        setChanged();
        notifyObservers();
    }

    /**
	 * make the node found in the list
	 * @param node - node to be marked
	 * @param list - list to be searched
	 */
    public void markNode(Markable node, ArrayList<? extends Comparable<? super Markable>> list) {
        node.setMark(true);
        final int check_range = 10;
        int vpos = Collections.binarySearch(list, node);
        int v1 = vpos > check_range ? vpos - check_range : 0;
        int v2 = vpos + check_range < list.size() ? vpos + check_range : list.size() - 1;
        for (int i = v1; i < v2; i++) {
            if (i == vpos) continue;
            Markable adjacent_node = (Markable) list.get(i);
            if (adjacent_node.isLongMarked()) node.setMark(true); else if (adjacent_node.isMarked()) node.setLongMark(true);
        }
        setCurrentNode(node);
    }

    public void markNode(Markable node) {
        Markable region = (Markable) AvlTree.getElement(node);
        if (AvlTree.hasLeftChild(node)) {
            if (((Markable) AvlTree.getLeftChild(node)).isMarked()) region.setLongMark(true);
        } else if (AvlTree.hasRightChild(node)) {
            if (((Markable) AvlTree.getRightChild(node)).isMarked()) region.setLongMark(true);
        } else region.setMark(true);
        setCurrentNode(region);
    }

    public String[] getGenomeNames() {
        return genomeNames.toArray(new String[genomeNames.size()]);
    }

    public int[] getGenomeSizes() {
        int[] sizes = new int[genomeSizes.size()];
        for (int i = 0; i < genomeSizes.size(); i++) sizes[i] = genomeSizes.get(i);
        return sizes;
    }

    public void clear() {
        links.clear();
        genomes.clear();
        genomeNames.clear();
        genomeSizes.clear();
        currentNode = null;
        currentNodes = null;
        orthologsHistory = new Pair<String>();
        homologsHistory = new ArrayList<Pair<String>>();
        id = 0;
        num = 0;
        level = BASE_LEVEL;
        setChanged();
        notifyObservers();
    }

    public Pair<String> getOrthologsHistory() {
        return orthologsHistory;
    }

    public ArrayList<Pair<String>> getHomologsHistory() {
        return homologsHistory;
    }

    public void clearOrthologsHistory() {
        orthologsHistory = new Pair<String>();
    }

    public void clearHomologsHistory() {
        homologsHistory = new ArrayList<Pair<String>>();
    }

    public void addLinkedLine(LinkedLine line) {
        links.add(line);
        setChanged();
        notifyObservers();
    }

    public void clearLinkedLines() {
        links.clear();
        setChanged();
        notifyObservers();
    }

    public ArrayList<LinkedLine> getLinkedLines() {
        return links;
    }

    /**
	 * send update to all observers
	 */
    public void updateAllViews() {
        setChanged();
        notifyObservers();
    }

    /**
	 * add a genome to the list and update document references
	 * @param genome
	 */
    public synchronized void addGenome(Genome genome) {
        genome.setYLevel(level);
        genome.setId(num);
        genome.mapSequences();
        genome.calcGcContent();
        genome.calcGcSkew();
        genome.setStatistics();
        genome.sort();
        genomes.add(genome);
        genomeNames.add(genome.getName());
        genomeSizes.add(genome.getSize());
        level -= parameter.getYIncrement();
        num++;
        for (Iterator<Genome> iterator = genomes.iterator(); iterator.hasNext(); ) {
            Genome genome2 = iterator.next();
            genome2.addOrthologs();
        }
    }

    /**
	 * adjst yLevel for each genome
	 */
    public void adjustYLevel() {
        level = BASE_LEVEL;
        Iterator<Genome> iterator = genomes.iterator();
        Genome genome1 = iterator.next();
        genome1.setYLevel(level);
        while (iterator.hasNext()) {
            level -= parameter.getYIncrement();
            Genome genome = (Genome) iterator.next();
            genome.setYLevel(level);
        }
        level -= parameter.getYIncrement();
    }

    public void analyzeGenomes(boolean[] checks, byte ci) {
        for (Iterator<Genome> iterator = genomes.iterator(); iterator.hasNext(); ) {
            Genome genome = iterator.next();
            genome.analyze(checks, ci, palette);
        }
        updateAllViews();
    }

    public void setOrthologsHistory(Pair<String> orthologsHistory) {
        this.orthologsHistory = orthologsHistory;
    }

    public void setHomologsHistory(ArrayList<Pair<String>> homologsHistory) {
        this.homologsHistory = homologsHistory;
    }

    public void addHomologsHistory(Pair<String> homologsHistory) {
        this.homologsHistory.add(homologsHistory);
    }
}
