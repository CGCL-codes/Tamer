package com.stromberglabs.index;

import java.io.File;
import java.io.IOException;
import com.stromberglabs.index.WordIndex.Document;
import com.stromberglabs.index.WordIndex.TermFrequency;
import com.stromberglabs.visual.search.FileList;
import com.stromberglabs.visual.search.L1ImageWordIndex;
import com.stromberglabs.visual.tree.VocabTreeManager;
import com.stromberglabs.tree.KMeansTree;
import com.stromberglabs.tree.query.QueryTree;

/**
 * The structure is:
 * Word<br> 
 * 	-> Weight<br>
 * 	-> List of objects<br>
 * 		-> File ID<br>
 * 		-> File Count<br>
 * 
 * @author Andrew
 *
 */
public class WordInvertedIndex {

    FileIndex mFidx;

    WordIndex mWidx;

    public WordInvertedIndex(L1ImageWordIndex objidx, File directory) {
        try {
            System.out.println("Starting file index");
            mFidx = new FileIndex(objidx, directory);
            System.out.println("File index created");
            mWidx = new WordIndex(objidx, mFidx.generateFileToIdMap(), directory);
            System.out.println("Word index created");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public WordInvertedIndex(String directory) throws IOException {
        System.out.println("Loading file index");
        mFidx = new FileIndex(directory);
        System.out.println("File index done, onto word index");
        mWidx = new WordIndex(directory);
        System.out.println("Word index done");
    }

    public FileList getFileList(int wordId) {
        FileList list = new FileList();
        Document doc = mWidx.getDocument(wordId);
        for (TermFrequency frequency : doc.mFrequencies) {
            try {
                list.addFile(mFidx.getFilename(frequency.mFile), 0, frequency.mCount);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        list.setTotalCount(doc.mTotalCount);
        list.setWeight(doc.mWeight);
        return list;
    }

    public int getNumFiles() {
        return mFidx.getNumFiles();
    }

    public static void main(String args[]) {
        if (args.length != 3) {
            System.out.println("Usage: ant web-index -Dargs=\"tree_file index_file index_directory\"");
            System.exit(0);
        }
        KMeansTree tree = VocabTreeManager.loadVocabTree(args[0]);
        tree.resetBreadthList();
        new QueryTree(tree, new File(args[2]));
        L1ImageWordIndex index = (L1ImageWordIndex) VocabTreeManager.loadIndex(args[1]);
        System.out.println("index loaded");
        new WordInvertedIndex(index, new File(args[2]));
        System.out.println("web index built");
    }
}
