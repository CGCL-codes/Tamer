package net.lateeye.search.thesissearch.acm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.parser.ParserDelegator;

class AcmSearchresultPageParser_2011Jan extends ParserCallback {

    static final String KEY_ENDPOS = "end";

    static final String KEY_BEGINPOS = "begin";

    private boolean atAllDocNum = false;

    private boolean atLoopOfEachResultSuite = false;

    private boolean atEachResultSuite = false;

    private boolean atAbstract = false;

    /**
	 * Indicates cursor is at the top of resultset area where number of all
	 * returned results are shown.
	 */
    private boolean atHeaderOfResultset = false;

    private LinkedList<HashMap<String, Integer>> listOfStartAndEndPosOfEachResult = new LinkedList<HashMap<String, Integer>>();

    private HashMap<String, Integer> startAndEndPosOfEachResult = null;

    private int allDocNum = -1;

    public LinkedList<HashMap<String, Integer>> getEachResultStartPositions() {
        return this.listOfStartAndEndPosOfEachResult;
    }

    public int getAllDocNum() {
        return this.allDocNum;
    }

    public AcmSearchresultPageParser_2011Jan() {
        super();
    }

    public void handleStartTag(HTML.Tag tag, MutableAttributeSet attr, int pos) {
        int attrCount = attr.getAttributeCount();
        String val_valign = (String) attr.getAttribute(HTML.Attribute.VALIGN);
        String val_border = (String) attr.getAttribute(HTML.Attribute.BORDER);
        String val_cellpadding = (String) attr.getAttribute(HTML.Attribute.CELLPADDING);
        String val_cellspacing = (String) attr.getAttribute(HTML.Attribute.CELLSPACING);
        String val_align = (String) attr.getAttribute(HTML.Attribute.ALIGN);
        String val_class = (String) attr.getAttribute(HTML.Attribute.CLASS);
        String val_style = (String) attr.getAttribute(HTML.Attribute.STYLE);
        String val_width = (String) attr.getAttribute(HTML.Attribute.WIDTH);
        if (tag.equals(HTML.Tag.TABLE)) {
            if (attrCount == 4 && "0".equals(val_border) && "100%".equals(val_width) && "left".equals(val_align) && "small-text".equals(val_class)) {
                this.atHeaderOfResultset = true;
            } else if (attrCount == 1 && "0".equals(val_border)) {
                this.atLoopOfEachResultSuite = true;
            }
        } else if (tag.equals(HTML.Tag.TR)) {
        } else if (tag.equals(HTML.Tag.TD)) {
            if (attrCount == 4 && "small-text".equals(val_class) && "center".equals(val_align) && "top".equals(val_valign) && "padding-left: 5px; padding-top: 10px;".equals(val_style)) {
                if (this.startAndEndPosOfEachResult != null) {
                    this.startAndEndPosOfEachResult.put("end", pos - 1);
                    this.listOfStartAndEndPosOfEachResult.add(this.startAndEndPosOfEachResult);
                    this.startAndEndPosOfEachResult = null;
                }
                this.atEachResultSuite = true;
                this.startAndEndPosOfEachResult = new HashMap<String, Integer>();
                this.startAndEndPosOfEachResult.put(KEY_BEGINPOS, pos);
                System.out.println("\t" + this.getClass().getName() + "#handleStartTag: result:begin: " + pos);
            } else if (attrCount == 1 && "padding-left: 5px".equals(val_style)) {
                this.atLoopOfEachResultSuite = false;
                if (this.startAndEndPosOfEachResult != null) {
                    this.startAndEndPosOfEachResult.put(KEY_ENDPOS, pos - 1);
                    this.listOfStartAndEndPosOfEachResult.add(this.startAndEndPosOfEachResult);
                    this.startAndEndPosOfEachResult = null;
                }
            } else if (attrCount == 0 && this.atHeaderOfResultset) {
                this.atAllDocNum = true;
            }
        } else if (tag.equals(HTML.Tag.DIV)) {
            if (attrCount == 1 && "abstract2".equals(val_class)) {
                this.atAbstract = true;
            }
        } else if (tag.equals(HTML.Tag.I)) {
            if (this.atAbstract) {
                this.atAbstract = true;
            }
        }
    }

    public void handleText(char[] data, int pos) {
        String text = new String(data);
        if (this.atAllDocNum) {
            try {
                this.allDocNum = this.extractResultsNum(text);
            } catch (IndexOutOfBoundsException e) {
            }
            this.atAllDocNum = false;
            this.atHeaderOfResultset = false;
        } else if (this.atAbstract) {
        }
    }

    private String extractAbstract(String text) {
        return text;
    }

    private int extractResultsNum(String text) {
        String ptnStr = "^Results [0-9]{1,} - [0-9]{1,} of [0-9]{1,}$";
        Pattern pattern = Pattern.compile(ptnStr);
        Matcher matcher_resultSize = pattern.matcher(text);
        String igniter = "of ";
        int n = -1;
        if (matcher_resultSize.find()) {
            n = Integer.parseInt(text.substring(text.indexOf(igniter) + igniter.length()));
        } else {
            throw new IndexOutOfBoundsException();
        }
        return n;
    }

    public static void main(String[] args) {
        String u = "http://portal.acm.org/results.cfm?query=%28Author%3A%22" + "Boehm%2C+Barry" + "%22%29&srt=score%20dsc&short=0&source_disp=&since_month=&since_year=&before_month=&before_year=&coll=ACM&dl=ACM&termshow=matchboolean&range_query=&CFID=22704101&CFTOKEN=37827144&start=1";
        URL url = null;
        AcmSearchresultPageParser_2011Jan cb = new AcmSearchresultPageParser_2011Jan();
        try {
            url = new URL(u);
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            uc.setUseCaches(false);
            InputStream is = uc.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            ParserDelegator pd = new ParserDelegator();
            pd.parse(br, cb, true);
            br.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("all doc num= " + cb.getAllDocNum());
        for (int i = 0; i < cb.getEachResultStartPositions().size(); i++) {
            HashMap<String, Integer> m = cb.getEachResultStartPositions().get(i);
            System.out.println(i + "pos= " + m);
        }
    }
}
