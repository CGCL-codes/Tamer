package biocreative.genenormalization.maxent;

import java.io.*;

public class CGFactory {

    String param = null;

    File home = null;

    File model = null;

    boolean logParam = true;

    public CGFactory(File home, File modelDir) {
        this.home = home;
        this.model = modelDir;
    }

    public CGFactory(File home, String mName) {
        this.home = home;
        model = new File(home.getAbsolutePath() + File.separatorChar + "models" + File.separatorChar + mName);
        model.mkdir();
    }

    public ArticleContextGenerator getCG() {
        logParam = false;
        String param = null;
        String[] fileList = model.list();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].indexOf(".mod") != -1) {
                param = fileList[i].substring(0, fileList[i].indexOf(".mod")).trim();
                break;
            }
        }
        return getCG(param);
    }

    /**
	 * m : MeSHeading.lex
	 * g : GOTokens.lex
	 * a : if append GID , a for appending
	 * 
	 * @param param m,g,a 
	 * @return
	 */
    public ArticleContextGenerator getCG(String param) {
        ArticleContextGenerator CG = new ArticleContextGenerator();
        if (param.indexOf("a") != -1) CG.setAppendGID(true); else CG.setAppendGID(false);
        if (param.indexOf("m") != -1) {
            File mesh = new File(home.getAbsolutePath() + File.separatorChar + "lexicon" + File.separatorChar + "MeSHeading.lex");
            LexiconEventDetector mDetector = new LexiconEventDetector("m", mesh);
            CG.register(mDetector);
        }
        if (param.indexOf("g") != -1) {
            File go = new File(home.getAbsolutePath() + File.separatorChar + "lexicon" + File.separatorChar + "GOTokens.lex");
            LexiconEventDetector gDetector = new LexiconEventDetector("g", go);
            CG.register(gDetector);
        }
        if (param.indexOf("b") != -1) {
            File umls = new File(home.getAbsolutePath() + File.separatorChar + "lexicon" + File.separatorChar + "BioThesaurus.lex");
            LexiconEventDetector bDetector = new LexiconEventDetector("b", umls);
            CG.register(bDetector);
        }
        if (param.contains("n")) {
            NEREventDetector nDetector = new NEREventDetector();
            CG.register(nDetector);
        }
        if (param.contains("l")) {
            File gm = new File(home.getAbsolutePath() + File.separatorChar + "lexicon" + File.separatorChar + "gene.lex");
            LexiconEventDetector gDetector = new LexiconEventDetector("l", gm);
            CG.register(gDetector);
        }
        return CG;
    }
}
