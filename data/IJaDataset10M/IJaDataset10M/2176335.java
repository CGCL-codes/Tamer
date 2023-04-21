package it.freax.fpm.core.solver.conf;

import it.freax.fpm.core.solver.dto.SrcFile;
import it.freax.fpm.core.types.InfoType;
import it.freax.fpm.core.types.MethodType;
import it.freax.fpm.core.types.WhereToParseType;
import it.freax.fpm.util.EntriesScorer;
import it.freax.fpm.util.ErrorHandler;
import it.freax.fpm.util.Strings;
import it.freax.fpm.util.exceptions.ExtensionDecodingException;
import java.util.List;
import java.util.Map.Entry;

public class Additive {

    private int id;

    private InfoType infoType;

    private WhereToParseType whereToParse;

    private String whatToParse;

    private String howToParse;

    public Additive(int id, InfoType infoType, WhereToParseType whereToParse, String whatToParse, String howToParse) {
        this.id = id;
        this.infoType = infoType;
        this.whereToParse = whereToParse;
        this.whatToParse = whatToParse;
        this.howToParse = howToParse;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public InfoType getInfoType() {
        return infoType;
    }

    public void setInfoType(InfoType infoType) {
        this.infoType = infoType;
    }

    public WhereToParseType getWhereToParse() {
        return whereToParse;
    }

    public void setWhereToParse(WhereToParseType whereToParse) {
        this.whereToParse = whereToParse;
    }

    public String getWhatToParse() {
        return whatToParse;
    }

    public void setWhatToParse(String whatToParse) {
        this.whatToParse = whatToParse;
    }

    public String getHowToParse() {
        return howToParse;
    }

    public void setHowToParse(String howToParse) {
        this.howToParse = howToParse;
    }

    public MethodParams getMethodParams() {
        MethodType type = MethodType.getMethodType(howToParse);
        return type.getParams(howToParse);
    }

    public String execMethod(SrcFile file) {
        String ret = "";
        if ((howToParse != null) && !howToParse.isEmpty()) {
            MethodType type = MethodType.getMethodType(howToParse);
            MethodParams mp = type.getParams(howToParse);
            String input = "";
            try {
                switch(whereToParse) {
                    case Content:
                        {
                            input = file.getContent();
                            ret = getValue(type, mp, input);
                            break;
                        }
                    case FileName:
                        {
                            input = file.getName();
                            ret = getValue(type, mp, input);
                            break;
                        }
                    case Nothing:
                        {
                            break;
                        }
                }
            } catch (ExtensionDecodingException e) {
                ErrorHandler.getOne(getClass()).handle(e);
            }
        } else {
            ret = file.getContent();
        }
        return ret;
    }

    private String getValue(MethodType type, MethodParams mp, String input) throws ExtensionDecodingException {
        String ret = "";
        String pattern = whatToParse + mp.getDivider();
        Strings strings = Strings.getOne();
        List<String> grepped = strings.grep(input, pattern + "\\p{Graph}+", false);
        EntriesScorer<String> es = new EntriesScorer<String>(true);
        for (String val : grepped) {
            String parsed = "";
            switch(type) {
                case Split:
                    {
                        parsed = strings.Split(val, mp.getDivider(), mp.getIndex(), mp.isExtension());
                        break;
                    }
                case KeyValue:
                    {
                        parsed = strings.KeyValue(val, mp.getDivider());
                        break;
                    }
            }
            if (!parsed.trim().replace(mp.getDivider(), "").isEmpty()) {
                es.add(parsed);
            }
        }
        if (grepped.size() > 0) {
            Entry<String, Integer> en = es.getBestScore();
            if (en != null) {
                ret = en.getKey();
            }
        }
        return ret;
    }
}
