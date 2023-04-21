package hu.schmidtsoft.parser.test;

import hu.schmidtsoft.parser.language.ITerm;
import hu.schmidtsoft.parser.language.ITermCompound;
import hu.schmidtsoft.parser.language.ITermMore;
import hu.schmidtsoft.parser.language.ITermReference;
import hu.schmidtsoft.parser.language.impl.LanguageParserXML;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class LanguageWriter {

    public String write(List<? extends ITerm> l) {
        StringBuilder out = new StringBuilder();
        for (ITerm t : l) {
            if (t.getName().indexOf(LanguageParserXML.uniqueDiv) < 0) {
                String w = write(t, new TreeSet<String>());
                out.append(w);
                out.append("\n");
            }
        }
        return out.toString();
    }

    public String write(ITerm t) {
        return write(t, new TreeSet<String>());
    }

    String write(ITerm t, Set<String> alreadyWritten) {
        StringBuilder out = new StringBuilder();
        if (t == null) {
            throw new RuntimeException();
        }
        out.append(t.getName() + "\t:" + t.getType());
        if (!alreadyWritten.contains(t.getName())) {
            alreadyWritten.add(t.getName());
            switch(t.getType()) {
                case and:
                case or:
                    {
                        ITermCompound c = (ITermCompound) t;
                        out.append("(");
                        for (ITerm s : c.getSubs()) {
                            out.append(write(s, alreadyWritten));
                            out.append(",");
                        }
                        out.append(")");
                        break;
                    }
                case reference:
                    {
                        ITermReference m = (ITermReference) t;
                        out.append("(");
                        if (m.getSub() == null) {
                            throw new RuntimeException("error in language: no child: " + t.getName() + "\t:" + t.getType());
                        }
                        out.append(write(m.getSub(), alreadyWritten));
                        out.append(")");
                        break;
                    }
                case oneormore:
                case zeroormore:
                    {
                        ITermMore m = (ITermMore) t;
                        out.append("(");
                        out.append(write(m.getSub(), alreadyWritten));
                        out.append(")");
                        break;
                    }
            }
        }
        return out.toString();
    }
}
