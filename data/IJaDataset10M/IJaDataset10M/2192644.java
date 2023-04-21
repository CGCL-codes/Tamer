package paraphrase;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class DictionarySearch implements Runnable {

    Word FinalWord;

    URL queryAddress;

    int progress = 0;

    /******
 * All the filters that are used in the FilterHTML() method.
 * 
 * TODO Change the Filters into an array
 */
    private boolean finished = false;

    private final String filter1 = "the_content";

    private final String filter2 = "result_copyright";

    private final String filter3 = "Main Entry:";

    private final String filter4 = "</span>";

    private final String filter5 = "<td><i>";

    private final String filter6 = "</i></td>";

    private final String filter7 = "Definition:";

    private final String filter8 = "Synonyms:";

    private final String filter9 = "<td>";

    private final String filter10 = "</td></tr>";

    private final String filter11 = "Synonyms:";

    private final String filter12 = "</span></td>";

    private final String filter13 = "Antonyms:";

    private final String filter14 = "\">";

    private final String filter15 = "</a>";

    private final String[] Filter = { "the_content", "result_copyright", "Main Entry:", "</span>", "<td><i>", "</i></td>", "Definition:", "Synonyms:", "<td>", "</td></tr>", "Synonyms:", "</span></td>", "Antonyms:", "\">", "</a>" };

    String rawHtml;

    private String Word;

    private String partOf;

    private String Def;

    private String Syn;

    private String Ant;

    ProgressBarDemo prog = new ProgressBarDemo();

    /****
 * 
 * Gets all the information about the word requested. Then creates a Word
 * class that you can get containing all the information.
 * 
 * @param word
 *            - The word you want to get
 */
    public DictionarySearch(String word) {
        Word = word;
        System.out.println("Dictionary Search Initialised");
    }

    public void run() {
        System.out.println("1");
        getHTML();
        System.out.println("2");
        try {
            FilterHTML();
        } catch (Exception e) {
            System.err.println("Such Word does not exist in the dictionary. \nSkiping Word");
            System.err.println("Debug Info:");
            System.err.println("Word = " + Word);
            System.err.println("URL:" + queryAddress.toString());
            rawHtml = "";
        }
        System.out.println("3");
        toWord();
        finished = true;
    }

    /*****
 * 
 * @return The Word class containing all the information gotten from the
 *         website.
 */
    public Word getWord() {
        return FinalWord;
    }

    /****
 * Creates a Word Class out of the collected data.
 */
    private void toWord() {
        FinalWord = new Word(Word, Def, Syn, Ant, partOf);
    }

    /********
 * 
 * Filters the raw HTML and gets all the information needed for the Word
 * class
 * 
 * @throws StringIndexOutOfBoundsException
 *             Due to Words it can not find in the dictionary.
 */
    private void FilterHTML() throws StringIndexOutOfBoundsException {
        prog.setProgress(80);
        int Start = rawHtml.indexOf(filter1);
        int End = rawHtml.indexOf(filter2);
        progress++;
        String temp = rawHtml.substring(Start, End);
        rawHtml = temp;
        Start = temp.indexOf(filter3);
        End = temp.indexOf(filter4);
        progress++;
        String temp2 = temp.substring(Start, End);
        Start = temp2.indexOf(filter5) + 7;
        End = temp2.indexOf(filter6);
        partOf = temp2.substring(Start, End);
        temp = temp2.substring(End);
        progress++;
        Start = temp.indexOf(filter7);
        End = temp.indexOf(filter8);
        progress++;
        temp2 = temp.substring(Start, End);
        Start = temp2.indexOf(filter9) + 4;
        End = temp2.indexOf(filter10);
        progress++;
        Def = temp2.substring(Start, End);
        Start = rawHtml.indexOf(filter11);
        End = rawHtml.indexOf(filter12);
        Syn = rawHtml.substring(Start, End);
        progress++;
        Start = rawHtml.indexOf(filter13);
        progress++;
        if (Start > 0) Ant = rawHtml.substring(Start);
        try {
            progress++;
            SynFilter();
        } catch (Exception e) {
            e.getStackTrace();
            System.err.println("LOL");
        }
        try {
            progress++;
            AntFilter();
        } catch (Exception e) {
            e.getStackTrace();
            System.err.println("LOL");
        }
        progress++;
        prog.setProgress(100);
    }

    /***
 * 
 * Filters Synonyms to human readable words
 * @deprecated No longer used, changed for SynFilterArray().
 */
    private void SynFilter() {
        boolean end = Syn == null;
        String tempSyn = "";
        while (end != true) {
            int Start = Syn.indexOf(filter14) + 2;
            int End = Syn.indexOf(filter15);
            if (End < 1) {
                break;
            }
            tempSyn += Syn.substring(Start, End) + ", ";
            Syn = Syn.substring(End + 4);
        }
        Syn = tempSyn.substring(0, tempSyn.length() - 2);
    }

    /***
 * 
 * Filters Synonyms to human readable words
 * 
 */
    private void SynFilterArray() {
        boolean end = Syn == null;
        String tempSyn = "";
        while (end != true) {
            int Start = Syn.indexOf(filter14) + 2;
            int End = Syn.indexOf(filter15);
            if (End < 1) {
                break;
            }
            tempSyn += Syn.substring(Start, End) + ", ";
            Syn = Syn.substring(End + 4);
        }
        Syn = tempSyn.substring(0, tempSyn.length() - 2);
    }

    /***
 * 
 * Filters the Antonyms into human readable words
 * 
 * @deprecated No longer used, changed for AntFilterArray().
 */
    private void AntFilter() {
        boolean end = Ant == null;
        String tempAnt = "";
        while (end != true) {
            int Start = Ant.indexOf(filter14) + 2;
            int End = Ant.indexOf(filter15);
            if (End < 1) {
                break;
            }
            tempAnt += Ant.substring(Start, End) + ", ";
            Ant = Ant.substring(End + 4);
        }
        if (Ant != null) Ant = tempAnt.substring(0, tempAnt.length() - 2);
    }

    /**
 * Filters the Antonyms into human readable words
 */
    private void AntFilterArray() {
        boolean end = Ant == null;
        String tempAnt = "";
        while (end != true) {
            int Start = Ant.indexOf(Filter[13]) + 2;
            int End = Ant.indexOf(Filter[14]);
            if (End < 1) {
                break;
            }
            tempAnt += Ant.substring(Start, End) + ", ";
            Ant = Ant.substring(End + 4);
        }
        if (Ant != null) Ant = tempAnt.substring(0, tempAnt.length() - 2);
    }

    /***
 * 
 * @return Raw HTML for the Dictionary entry of the word selected
 * 
 */
    private void getHTML() {
        System.out.println("Getting HTML....");
        System.out.println();
        try {
            String queryStr = "http://thesaurus.reference.com/browse/";
            queryStr += Word;
            queryAddress = new URL(queryStr);
        } catch (MalformedURLException ex) {
            System.out.println("Unable to connect with server");
        }
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(queryAddress.openStream(), "UTF-8"));
            queryAddress.openConnection();
            String str;
            str = in.readLine();
            int cntDown = 0;
            int divCnt = 0;
            int indx;
            while (str != null) {
                rawHtml += str;
                if (str.indexOf("<!-- end ahd4 -->") != -1) {
                    break;
                }
                if (str.indexOf("<td>Encyclopedia suggestions:") != -1) {
                    break;
                }
                str = in.readLine();
            }
            in.close();
        } catch (Exception e) {
            System.err.println("Unable to connect with server");
        }
    }

    private boolean Check(int Start, int End) {
        return Start > 0 && End > 0;
    }

    private void FilterHTMLArray() throws StringIndexOutOfBoundsException {
        int Start = rawHtml.indexOf(Filter[0]);
        int End = rawHtml.indexOf(Filter[1]);
        String temp = rawHtml.substring(Start, End);
        rawHtml = temp;
        Start = temp.indexOf(Filter[2]);
        End = temp.indexOf(Filter[3]);
        String temp2 = temp.substring(Start, End);
        Start = temp2.indexOf(Filter[4]) + 7;
        End = temp2.indexOf(Filter[5]);
        partOf = temp2.substring(Start, End);
        temp = temp2.substring(End);
        Start = temp.indexOf(Filter[6]);
        End = temp.indexOf(Filter[7]);
        temp2 = temp.substring(Start, End);
        Start = temp2.indexOf(Filter[8]) + 4;
        End = temp2.indexOf(Filter[9]);
        Def = temp2.substring(Start, End);
        Start = rawHtml.indexOf(Filter[10]);
        End = rawHtml.indexOf(Filter[11]);
        Syn = rawHtml.substring(Start, End);
        Start = rawHtml.indexOf(Filter[12]);
        if (Start > 0) Ant = rawHtml.substring(Start);
        try {
            SynFilter();
        } catch (Exception e) {
            e.getStackTrace();
            System.err.println("LOL");
        }
        try {
            AntFilter();
        } catch (Exception e) {
            e.getStackTrace();
            System.err.println("LOL");
        }
    }

    public boolean isFinished() {
        return finished;
    }
}
