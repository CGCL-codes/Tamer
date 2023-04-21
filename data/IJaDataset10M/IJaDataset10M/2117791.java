package mongo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.tika.parser.AutoDetectParser;
import com.bugull.mongo.fs.BuguFS;
import extract.DocVO;
import extract.ExtractDoc;
import extract.FileUtils;

public class Main {

    private static ArrayList<File> fileList = new ArrayList<File>();

    private static ExtractDoc extDoc;

    private static BuguFS fs;

    public static void main(String[] args) {
        String srcDir = "E://apart";
        long tStart = 0;
        try {
            FileUtils.acceptFiles(new File(srcDir), fileList);
            System.out.println("Hits: " + fileList.size());
            tStart = System.currentTimeMillis();
            extDoc = new ExtractDoc(new AutoDetectParser());
            extDoc.openFile(fileList);
            long tEnd = System.currentTimeMillis();
            System.out.println(tEnd - tStart + "millions");
            System.out.println("-----------------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ConnFactory factory = ConnFactory.getInstance();
        factory.createConnManager(Preference.DB_HOST, Preference.DB_PORT, Preference.DB_NAME, Preference.DB_USERNAME, Preference.DB_PWD);
        fs = BuguFS.getInstance();
        DocDAO dao = new DocDAO();
        List<DocVO> list = extDoc.docList;
        int count = 1;
        for (DocVO docVO : list) {
            dao.insert(docVO);
            fs.save(docVO.getOriginalFile(), docVO.getMetadataVO().getResourceName(), docVO.getId());
            System.out.println("[ " + count++ + " ] " + docVO.getMetadataVO().getResourceName() + " => inserted.");
        }
    }
}
