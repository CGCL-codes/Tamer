package relex.frame.index;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import relex.frame.Sentence;
import relex.frame.ewah.EWAHCompressedBitmap;
import relex.frame.indexgenerator.impl.IndexBuilder;
import relex.frame.linkset.DefaultListenerManager;
import relex.frame.linkset.HandlerMethod;

public class Index {

    private static final File inFile = new File(System.getProperty("user.dir") + "/data/index/index.txt");

    private ArrayList<EWAHCompressedBitmap> kbCombinations = new ArrayList<EWAHCompressedBitmap>();

    private EWAHCompressedBitmap[] index, index2;

    private IndexBuilder builder;

    private static Index indexIns = new Index();

    public static Index getInstance() {
        return (indexIns);
    }

    public Index() {
        try {
            ObjectInputStream obj = new ObjectInputStream(new FileInputStream(inFile));
            builder = (IndexBuilder) obj.readObject();
            obj.close();
            index = builder.getIndex();
        } catch (Exception e) {
            System.out.println("Index initializing error");
            e.printStackTrace();
        }
    }

    /**
	 * This method will be accessed by a Sentence to get the knowledge base
	 * claim.
	 * 
	 * @param relations
	 *            relations of a sentence
	 * @return knowledge base claim list
	 */
    @HandlerMethod(id = "query")
    public synchronized void query(Sentence aSentence, ArrayList<String> relations) {
        ArrayList<Integer> kbClaims = new ArrayList<Integer>();
        ArrayList<String> inputHeaders = new ArrayList<String>();
        ArrayList<String> uniqueRelationCounts;
        int queryResultCount = 0;
        String tempHeader = "";
        for (int i = 0; i < relations.size(); i++) {
            tempHeader = relations.get(i).split("\\(")[0];
            inputHeaders.add(tempHeader);
        }
        HashSet<String> tempInputHeaders = new HashSet<String>(inputHeaders);
        inputHeaders = new ArrayList<String>(tempInputHeaders);
        uniqueRelationCounts = builder.getUniqueRelCount();
        int co = 0;
        for (int ii = 0; ii < inputHeaders.size(); ii++) {
            ArrayList<String> temp = new ArrayList();
            temp.add(inputHeaders.get(ii));
            kbCombinations.add(query(temp, index, index2, builder.getUniqueRels(), uniqueRelationCounts));
        }
        EWAHCompressedBitmap uniqueKbs = new EWAHCompressedBitmap();
        try {
            for (EWAHCompressedBitmap map : kbCombinations) {
                uniqueKbs = uniqueKbs.or(map);
            }
            co = 0;
            for (int k : uniqueKbs) {
                kbClaims.add(k);
                co++;
            }
        } catch (Exception e) {
        }
        System.out.println("kbclaims " + co);
        DefaultListenerManager listner = new DefaultListenerManager();
        listner.add(aSentence, "setClaimList");
        try {
            listner.invokeAll(kbClaims);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized ArrayList<Integer> query(ArrayList<String> relations) {
        ArrayList<Integer> kbClaims = new ArrayList<Integer>();
        ArrayList<String> inputHeaders = new ArrayList<String>();
        ArrayList<String> uniqueRelationCounts;
        int queryResultCount = 0;
        String tempHeader = "";
        for (int i = 0; i < relations.size(); i++) {
            tempHeader = relations.get(i).split("\\(")[0];
            inputHeaders.add(tempHeader);
        }
        HashSet<String> tempInputHeaders = new HashSet<String>(inputHeaders);
        inputHeaders = new ArrayList<String>(tempInputHeaders);
        uniqueRelationCounts = builder.getUniqueRelCount();
        int co = 0;
        for (int ii = 0; ii < inputHeaders.size(); ii++) {
            ArrayList<String> temp = new ArrayList();
            temp.add(inputHeaders.get(ii));
            kbCombinations.add(query(temp, index, index2, builder.getUniqueRels(), uniqueRelationCounts));
        }
        EWAHCompressedBitmap uniqueKbs = new EWAHCompressedBitmap();
        try {
            for (EWAHCompressedBitmap map : kbCombinations) {
                uniqueKbs = uniqueKbs.or(map);
            }
            co = 0;
            for (int k : uniqueKbs) {
                kbClaims.add(k);
                co++;
            }
        } catch (Exception e) {
        }
        System.out.println("kbclaims " + co);
        return kbClaims;
    }

    /**
	 * Querying the index for a set of relation combination
	 * 
	 * @param relations
	 *            set of relation combination
	 * @param index
	 *            condition index
	 * @param uniqueRelations
	 *            unique headers list
	 * @return bitmap which represents KBs present in all input relations
	 */
    private EWAHCompressedBitmap query(ArrayList<String> relations, EWAHCompressedBitmap[] index, EWAHCompressedBitmap[] relationCountIndex, ArrayList<String> uniqueRelations, ArrayList<String> uniqueRelationCounts) {
        EWAHCompressedBitmap[] kbs = new EWAHCompressedBitmap[relations.size()];
        for (int i = 0; i < relations.size(); i++) {
            try {
                kbs[i] = index[uniqueRelations.indexOf(relations.get(i))];
            } catch (Exception e) {
                kbs[i] = new EWAHCompressedBitmap();
            }
        }
        EWAHCompressedBitmap kb = null;
        if (relations.size() > 1) {
            try {
                kb = kbs[0].and(kbs[1]);
                for (int i = 2; i < kbs.length; i++) {
                    kb = kb.and(kbs[i]);
                }
            } catch (Exception e) {
            }
        } else {
            kb = kbs[0];
        }
        return kb;
    }
}
