package imp.cykparser;

import imp.ImproVisor;
import java.io.*;
import imp.brickdictionary.*;
import imp.util.ErrorLog;
import polya.*;
import java.util.ArrayList;
import java.util.LinkedList;

/** EquivalenceDictionary
 *
 * Handles equivalent ChordBlocks in interpretation of bricks as equivalence classes
 * of 2 or more ChordBlocks.
 * 
 * @author Xanda Schofield
 */
public class EquivalenceDictionary {

    private LinkedList<ArrayList<ChordBlock>> dict;

    /** Default constructor
     * Constructs a new EquivalenceDictionary with an empty dict.
     */
    public EquivalenceDictionary() {
        dict = new LinkedList<ArrayList<ChordBlock>>();
    }

    /** addRule / 1
     * Adds a single rule as an equivalence class to the dictionary
     * @param rule, an ArrayList of equivalent ChordBlocks.
     */
    public void addRule(ArrayList<ChordBlock> rule) {
        dict.add(rule);
    }

    /** checkEquivalences / 1
     * Takes in a string for a quality and checks its equivalence classes for 
     * an appropriate class.
     * 
     * @param c: a ChordBlock whose equivalence is to be checked
     * @return a SubstituteList of possible ChordBlocks equivalent to c, 
     * including c itself.
     */
    public SubstituteList checkEquivalence(ChordBlock c) {
        SubstituteList equivalences = new SubstituteList();
        for (ArrayList<ChordBlock> rule : dict) {
            for (ChordBlock eq : rule) {
                long diff = eq.matches(c);
                if (diff >= 0) {
                    for (ChordBlock sub : rule) {
                        if (sub.matches(c) < 0) equivalences.add(sub, diff);
                    }
                    break;
                }
            }
        }
        return equivalences;
    }

    /** loadDictionary / 1
     * Loads only the equivalences into the EquivalenceDictionary.
     * 
     * @param filename: a String describing the path to the source file for the 
     * equivalence rules.
     */
    public void loadDictionary(String filename) {
        FileInputStream fis = null;
        File file = new File(ImproVisor.getDictionaryDirectory(), filename);
        try {
            fis = new FileInputStream(file);
            Tokenizer in = new Tokenizer(fis);
            in.slashSlashComments(true);
            in.slashStarComments(true);
            Object token;
            while ((token = in.nextSexp()) != Tokenizer.eof) {
                if (token instanceof Polylist) {
                    Polylist contents = (Polylist) token;
                    if (contents.length() < 3) {
                        ErrorLog.log(ErrorLog.WARNING, "Error: incorrect equivalence: " + token, true);
                    } else {
                        String eqCategory = contents.first().toString();
                        contents = contents.rest();
                        if (eqCategory.equals("equiv")) {
                            ArrayList<ChordBlock> newEq = new ArrayList<ChordBlock>();
                            while (contents.nonEmpty()) {
                                String chordName = contents.first().toString();
                                contents = contents.rest();
                                ChordBlock nextChord = new ChordBlock(chordName, SubstitutionRule.NODUR);
                                newEq.add(nextChord);
                            }
                            addRule(newEq);
                        }
                    }
                } else {
                    ErrorLog.log(ErrorLog.WARNING, "Improper formatting for a token: " + token, true);
                }
            }
        } catch (FileNotFoundException ex) {
            ErrorLog.log(ErrorLog.SEVERE, "Dictionary file not found: " + file.getAbsolutePath(), true);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                ErrorLog.log(ErrorLog.FATAL, "Filestream cannot close", true);
            }
        }
    }
}
