package ces.platform.infoplat.utils.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Hits;
import ces.platform.infoplat.service.indexserver.Searcher;

/**
 * @author mysheros
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class HtmlDocSearch {

    private float hitScore = 0;

    private Document[] indexDocs = null;

    private String searchCriteria = null;

    private String sitePath = "0000000102";

    private String indexDbPath = "";

    private Hits hits = null;

    public String searchFileds = null;

    List vcDocs = new ArrayList();

    String[] fields = { "title", "docContent", "docAffixContent", "createrName", "FullPath", "publishDate", "createDate" };

    public String inputStr(String msg) {
        String str = "";
        try {
            System.out.print(msg + ":");
            byte[] bytes = new byte[100];
            System.in.read(bytes);
            str = new String(bytes).trim();
        } catch (Exception e) {
            e.printStackTrace();
            str = "error";
        }
        return str;
    }

    private void setIndexDbPath(String indexDbPath) {
        this.indexDbPath = indexDbPath;
    }

    private void setSitePath(String sitePath) {
        this.sitePath = sitePath;
    }

    private void search(String searchText) {
        String tempSearchCriteria = searchText;
        Searcher sh = null;
        if (searchFileds != null && !searchFileds.equals("")) {
            fields = new String[1];
            fields[0] = searchFileds;
        }
        if (tempSearchCriteria != null) {
            vcDocs = new ArrayList();
            searchCriteria = tempSearchCriteria;
            sh = new Searcher(sitePath);
            hits = sh.search(searchCriteria, fields);
            try {
                for (int i = 0; hits != null && i < hits.length(); i++) {
                    if (hits.score(i) > this.hitScore) {
                        vcDocs.add(hits.doc(i));
                    }
                }
            } catch (IOException ex2) {
                ex2.printStackTrace();
            }
        }
        if (sh != null) {
            sh.close();
        }
        indexDocs = new Document[vcDocs.size()];
        try {
            for (int i = 0; i < vcDocs.size(); i++) {
                indexDocs[i] = (Document) vcDocs.get(i);
            }
        } catch (Exception ex1) {
            ex1.printStackTrace();
        }
    }

    public static void main(String[] args) {
        HtmlDocSearch hsh = new HtmlDocSearch();
        String input = "y";
        try {
            while (input.equalsIgnoreCase("y")) {
                String sitePath = hsh.inputStr("�������վ��Ƶ��");
                String searchText = hsh.inputStr("�����������");
                String searchField = hsh.inputStr("��������ֶ�");
                Date start = new Date();
                hsh.setSitePath(sitePath);
                hsh.searchFileds = searchField;
                hsh.search(searchText);
                Date end = new Date();
                input = hsh.inputStr("����ִ��ȫ�ļ���(y) ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
}
