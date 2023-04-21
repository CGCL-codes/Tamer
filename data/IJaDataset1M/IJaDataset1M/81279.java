package net.sf.jcgm.core;

import java.io.*;

/**
 * Class=1, Element=11
 * @author xphc (Philippe Cadé)
 * @author BBNT Solutions
 * @version $Id: MetafileElementList.java 46 2011-12-14 08:26:44Z phica $
 */
public class MetafileElementList extends Command {

    String[] metaFileElements;

    public MetafileElementList(int ec, int eid, int l, DataInput in) throws IOException {
        super(ec, eid, l, in);
        int nElements = makeInt();
        this.metaFileElements = new String[nElements];
        for (int i = 0; i < nElements; i++) {
            int code1 = makeIndex();
            int code2 = makeIndex();
            if (code1 == -1) {
                switch(code2) {
                    case 0:
                        this.metaFileElements[i] = "DRAWING SET";
                        break;
                    case 1:
                        this.metaFileElements[i] = "DRAWING PLUS CONTROL SET";
                        break;
                    case 2:
                        this.metaFileElements[i] = "VERSION 2 SET";
                        break;
                    case 3:
                        this.metaFileElements[i] = "EXTENDED PRIMITIVES SET";
                        break;
                    case 4:
                        this.metaFileElements[i] = "VERSION 2 GKSM SET";
                        break;
                    case 5:
                        this.metaFileElements[i] = "VERSION 3 SET";
                        break;
                    case 6:
                        this.metaFileElements[i] = "VERSION 4 SET";
                        break;
                    default:
                        unsupported("unsupported meta file elements set " + code2);
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(" (").append(code1).append(",").append(code2).append(")");
                this.metaFileElements[i] = sb.toString();
            }
        }
        assert (this.currentArg == this.args.length);
    }

    @Override
    public String toString() {
        String s = "MetafileElementList ";
        for (String element : this.metaFileElements) {
            s += element + " ";
        }
        return s;
    }
}
