package cn.edu.dutir.test.unit.corpus.cwt;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import cn.edu.dutir.corpus.cwt.TWCorpus;
import cn.edu.dutir.corpus.cwt.TWPageHandler;
import cn.edu.dutir.corpus.cwt.TWPageParser;

public class TWCorpusParser {

    private static TWCorpus<TWPageHandler> mTWCorpus;

    private static String inDir = "E:/data_200";

    private static String outDir = "E:/release";

    public static void dohelp() {
        System.out.println("TWCorpuspParser <input_dir> <output_dir>");
        System.exit(1);
    }

    public static void main(String args[]) throws IOException {
        if (args.length != 2) {
            dohelp();
        }
        inDir = args[0];
        outDir = args[1];
        Date start = new Date();
        TWPageHandler handler = null;
        TWPageParser<TWPageHandler> parser = null;
        try {
            handler = new TWPageHandler(new File(outDir));
            parser = new TWPageParser<TWPageHandler>();
            parser.setHandler(handler);
            mTWCorpus = new TWCorpus<TWPageHandler>(parser);
            mTWCorpus.visitCorpus(new File(inDir));
            handler.close();
        } finally {
            handler.close();
        }
        Date end = new Date();
        System.out.println("Time Spent: " + (end.getTime() - start.getTime()) / 1000);
    }
}
