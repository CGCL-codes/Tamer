package lv.webkursi.klucis.eim.utils.tex;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class DiacriticsConverter {

    public void convert(Reader in, Writer out) throws IOException {
        int c = 0;
        while ((c = in.read()) != -1) {
            switch(c) {
                case 'Ā':
                    out.write("\\={A}");
                    break;
                case 'ā':
                    out.write("\\={a}");
                    break;
                case 'Ē':
                    out.write("\\={E}");
                    break;
                case 'ē':
                    out.write("\\={e}");
                    break;
                case 'Ī':
                    out.write("\\={I}");
                    break;
                case 'Ō':
                    out.write("\\={O}");
                    break;
                case 'ō':
                    out.write("\\={o}");
                    break;
                case 'Ū':
                    out.write("\\={U}");
                    break;
                case 'ū':
                    out.write("\\={u}");
                    break;
                case 'ī':
                    out.write("\\={\\i}");
                    break;
                case 'Č':
                    out.write("\\v{C}");
                    break;
                case 'č':
                    out.write("\\v{c}");
                    break;
                case 'ģ':
                    out.write("\\v{g}");
                    break;
                case 'Š':
                    out.write("\\v{S}");
                    break;
                case 'š':
                    out.write("\\v{s}");
                    break;
                case 'Ž':
                    out.write("\\v{Z}");
                    break;
                case 'ž':
                    out.write("\\v{z}");
                    break;
                case 'Ģ':
                    out.write("\\c{G}");
                    break;
                case 'Ķ':
                    out.write("\\c{K}");
                    break;
                case 'ķ':
                    out.write("\\c{k}");
                    break;
                case 'Ļ':
                    out.write("\\c{L}");
                    break;
                case 'ļ':
                    out.write("\\c{l}");
                    break;
                case 'Ņ':
                    out.write("\\c{N}");
                    break;
                case 'ņ':
                    out.write("\\c{n}");
                    break;
                case 'Ŗ':
                    out.write("\\c{R}");
                    break;
                case 'ŗ':
                    out.write("\\c{r}");
                    break;
                default:
                    out.write(c);
                    break;
            }
        }
    }
}
