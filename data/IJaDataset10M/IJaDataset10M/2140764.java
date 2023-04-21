package com.aivik.wordspell.engine;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author Kevin Ewig
 */
public class BKTreeTest {

    @Test
    public void test01() {
        URL url = KSpellCheckEngine.class.getResource("../dict/engchar05.dic");
        Assert.assertTrue(url != null);
        try {
            BKTree tree = null;
            List<String> list = readFile(url.getPath());
            Collections.shuffle(list);
            for (String w : list) {
                if (tree == null) {
                    tree = new BKTree();
                } else {
                    BKTreeNode node = new BKTreeNode(w);
                    tree.insertDictionaryWord(w, false, false);
                }
            }
            List<BKSuggestion> nodes1 = tree.findSuggestion("water");
            Assert.assertTrue(!nodes1.isEmpty());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private List<String> readFile(String aFileName) {
        List<String> listOfWords = new ArrayList<String>();
        File file = new File(aFileName);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            while (dis.available() != 0) {
                String line = dis.readLine();
                if (line.endsWith("/S") || line.endsWith("/SX")) {
                    int index = line.indexOf("/S");
                    listOfWords.add(new String(line.substring(0, index)));
                } else {
                    listOfWords.add(new String(line));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception ex) {
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (Exception ex) {
                }
            }
            if (dis != null) {
                try {
                    dis.close();
                } catch (Exception ex) {
                }
            }
        }
        return listOfWords;
    }
}
