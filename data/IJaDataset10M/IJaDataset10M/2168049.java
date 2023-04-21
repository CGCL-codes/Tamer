package ecosim;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.IndexOutOfBoundsException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *  Handles the input and output of Fasta formatted text files.
 *
 *  @author Jason Wood
 */
public class Fasta {

    /**
     *  Constructor for a Fasta object.
     */
    public Fasta() {
        length = 0;
        ids = new ArrayList<String>();
        descriptionHash = new HashMap<String, String>();
        sequenceHash = new HashMap<String, String>();
    }

    /**
     *  Constructor for a Fasta object using a user supplied file name.
     *
     *  @param fileName The path and file name of the file containing the Fasta data.
     */
    public Fasta(String fileName) {
        parseFasta(new File(fileName));
    }

    /**
     *  Constructor for a Fasta object using a user supplied File.
     *
     *  @param file A File containing the Fasta data.
     */
    public Fasta(File file) {
        parseFasta(file);
    }

    /**
     *  Constructor for a Fasta object, used for making a copy of a Fasta object.
     *
     *  @param fasta The Fasta object that contains the data for this new Fasta object.
     */
    public Fasta(Fasta fasta) {
        length = fasta.length();
        ids = new ArrayList<String>(fasta.getIds());
        descriptionHash = new HashMap<String, String>(fasta.getDescriptionHash());
        sequenceHash = new HashMap<String, String>(fasta.getSequenceHash());
    }

    /**
     *  Constructor for a Fasta object, used for making a copy of a Fasta object, using
     *  only the selected IDs.
     *
     *  @param fasta The Fasta object that contains the data for this new Fasta object.
     *  @param selectedIds The IDs to keep in this new Fasta.
     */
    public Fasta(Fasta fasta, ArrayList<String> selectedIds) {
        length = 0;
        ids = selectedIds;
        descriptionHash = new HashMap<String, String>();
        sequenceHash = new HashMap<String, String>();
        HashMap<String, String> desHash = new HashMap<String, String>(fasta.getDescriptionHash());
        HashMap<String, String> seqHash = new HashMap<String, String>(fasta.getSequenceHash());
        for (int i = 0; i < ids.size(); i++) {
            String seq = seqHash.get(ids.get(i));
            descriptionHash.put(ids.get(i), desHash.get(ids.get(i)));
            sequenceHash.put(ids.get(i), seq);
            if (seq.length() > length) {
                length = seq.length();
            }
        }
    }

    /**
     *  Check if this Fasta object is valid.
     *
     *  @return True if this is a valid Fasta object, False if not.
     */
    public boolean isValid() {
        boolean valid = false;
        if (ids.size() > 0) {
            valid = true;
        }
        return valid;
    }

    /**
     *  Puts an ID and Sequence into this Fasta object.
     *
     *  @param id The ID of the sequence to add.
     *  @param sequence The sequence to add.
     */
    public void put(String id, String sequence) {
        put(id, "", sequence);
    }

    /**
     *  Puts an ID and Sequence into this Fasta object.
     *
     *  @param id The ID of the sequence to add.
     *  @param description The descripiton of the sequence to add.
     *  @param sequence The sequence to add.
     */
    public void put(String id, String description, String sequence) {
        ids.add(id);
        descriptionHash.put(id, description);
        sequenceHash.put(id, sequence);
        if (sequence.length() > length) {
            length = sequence.length();
        }
    }

    /**
     *  Gets a sequence from this Fasta object that has the provided ID.
     *
     *  @param id The ID of the sequence to return.
     *  @return The sequence linked to the provided ID.
     */
    public String getSequence(String id) {
        String value = null;
        if (id != null) {
            value = sequenceHash.get(id);
        }
        return value;
    }

    /**
     *  Gets a sequence from this Fasta object that has the provided index.
     *
     *  @param index The index of the sequence to return.
     *  @return The sequence linked to the provided index.
     */
    public String getSequence(int index) {
        String value = null;
        try {
            String id = ids.get(index);
            value = getSequence(id);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     *  Gets a description from this Fasta object that has the provided ID.
     *
     *  @param id The ID of the description to return.
     *  @return The description linked to the provided ID.
     */
    public String getDescription(String id) {
        String value = null;
        if (id != null) {
            value = descriptionHash.get(id);
        }
        return value;
    }

    /**
     *  Gets a description from this Fasta object that has the provided index.
     *
     *  @param index The index of the description to return.
     *  @return The description linked to the provided index.
     */
    public String getDescription(int index) {
        String value = null;
        try {
            String id = ids.get(index);
            value = getDescription(id);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     *  Get the IDs stored in this Fasta.
     *
     *  @return ArrayList of Strings containing the IDs.
     */
    public ArrayList<String> getIds() {
        return ids;
    }

    /**
     *  Get the HashMap containing the IDs and Descriptions stored in this Fasta.
     *
     *  @return The HashMap containing the IDs and Descriptions stored in this Fasta.
     */
    public HashMap<String, String> getDescriptionHash() {
        return descriptionHash;
    }

    /**
     *  Get the HashMap containing the IDs and Sequences stored in this Fasta.
     *
     *  @return The HashMap containing the IDs and Sequences stored in this Fasta.
     */
    public HashMap<String, String> getSequenceHash() {
        return sequenceHash;
    }

    /**
     *  Get the sequences stored in this Fasta.
     *
     *  @return ArrayList of Strings containing the sequences.
     */
    public ArrayList<String> getSequences() {
        ArrayList<String> sequences = new ArrayList<String>();
        for (int i = 0; i < ids.size(); i++) {
            sequences.add(sequenceHash.get(ids.get(i)));
        }
        return sequences;
    }

    /**
     *  Removes a sequences from this Fasta object.
     *
     *  @param id The ID of the sequence to remove.
     *  @return True if the sequence was successfully removed, False if not.
     */
    public boolean remove(String id) {
        boolean success = false;
        if (id != null) {
            String value = sequenceHash.remove(id);
            ids.remove(id);
            if (value != null) {
                success = true;
            }
        }
        return success;
    }

    /**
     *  Removes a sequence from this Fasta object.
     *
     *  @param index The index of the sequence to remove.
     *  @return True if the sequences was successfully removed, False if not.
     */
    public boolean remove(int index) {
        boolean success = false;
        try {
            String id = ids.get(index);
            success = remove(id);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     *  Returns the length of the longest sequence stored in this Fasta object.
     */
    public int length() {
        return length;
    }

    /**
     *  Returns the number of sequences stored in this Fasta object.
     */
    public int size() {
        return ids.size();
    }

    /**
     *  Loads the given Fasta formatted file into this object.
     *
     *  @param fileName The path and file name to load.
     */
    public void load(String fileName) {
        parseFasta(new File(fileName));
    }

    /**
     *  Loads the given Fasta formatted file into this object.
     *
     *  @param file The file to load.
     */
    public void load(File file) {
        parseFasta(file);
    }

    /**
     *  Save the Fasta data in this object to an Ecotype Simulation formatted Fasta file.
     *
     *  @param fileName File name to write the Fasta data to.
     *  @return True if the save was a success, False otherwise.
     */
    public boolean save(String fileName) {
        return save(new File(fileName));
    }

    /**
     *  Save the Fasta data in this object to an Ecotype Simulation formatted Fasta file.
     *
     *  @param file File to write the Fasta data to.
     *  @return True if the save was a success, False otherwise.
     */
    public boolean save(File file) {
        boolean success = false;
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(file));
            for (int i = 0; i < ids.size(); i++) {
                String key = ids.get(i);
                out.write(">" + key + " ");
                out.write(descriptionHash.get(key) + "\n");
                out.write(sequenceHash.get(key) + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error writing to output file.");
        } finally {
            if (out != null) {
                try {
                    out.close();
                    success = true;
                } catch (IOException e) {
                    System.out.println("Error closing output file.");
                }
            }
        }
        return success;
    }

    /**
     *  Private method to parse through a Fasta formatted text file.
     */
    private void parseFasta(File fastaFile) {
        BufferedReader input = null;
        sequenceHash = new HashMap<String, String>();
        descriptionHash = new HashMap<String, String>();
        ids = new ArrayList<String>();
        length = 0;
        try {
            input = new BufferedReader(new FileReader(fastaFile));
            String id = "null";
            String description = "";
            String sequence = "";
            String line = null;
            while ((line = input.readLine()) != null) {
                if (!line.isEmpty() && line.charAt(0) == '>') {
                    if (!id.equals("null")) {
                        sequenceHash.put(id, sequence);
                        descriptionHash.put(id, description);
                        ids.add(id);
                        if (sequence.length() > length) {
                            length = sequence.length();
                        }
                        sequence = "";
                    }
                    String[] header = line.split("\\s+", 2);
                    id = header[0].substring(1);
                    if (header.length == 2) {
                        description = header[1];
                    }
                } else {
                    sequence = sequence + line;
                }
            }
            if (!id.equals("null")) {
                sequenceHash.put(id, sequence);
                descriptionHash.put(id, description);
                ids.add(id);
                if (sequence.length() > length) {
                    length = sequence.length();
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading from input file.");
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.println("Error closing input file.");
                }
            }
        }
    }

    private int length;

    private ArrayList<String> ids;

    private HashMap<String, String> descriptionHash;

    private HashMap<String, String> sequenceHash;
}
