package net.sf.jcgm.core;

import java.io.*;

/**
 * Class=5, Element=18
 * @author xphc (Philippe Cadé)
 * @author BBNT Solutions
 * @version $Id: TextAlignment.java 46 2011-12-14 08:26:44Z phica $
 */
public class TextAlignment extends Command {

    enum HorizontalAlignment {

        NORMAL_HORIZONTAL, LEFT, CENTRE, RIGHT, CONTINOUS_HORIZONTAL
    }

    enum VerticalAlignment {

        NORMAL_VERTICAL, TOP, CAP, HALF, BASE, BOTTOM, CONTINOUS_VERTICAL
    }

    private HorizontalAlignment horizontalAlignment;

    private VerticalAlignment verticalAlignment;

    private double continuousHorizontalAlignment;

    private double continuousVerticalAlignment;

    public TextAlignment(int ec, int eid, int l, DataInput in) throws IOException {
        super(ec, eid, l, in);
        int horiz = makeEnum();
        switch(horiz) {
            case 0:
                this.horizontalAlignment = HorizontalAlignment.NORMAL_HORIZONTAL;
                break;
            case 1:
                this.horizontalAlignment = HorizontalAlignment.LEFT;
                break;
            case 2:
                this.horizontalAlignment = HorizontalAlignment.CENTRE;
                break;
            case 3:
                this.horizontalAlignment = HorizontalAlignment.RIGHT;
                break;
            case 4:
                this.horizontalAlignment = HorizontalAlignment.CONTINOUS_HORIZONTAL;
                break;
            default:
                this.horizontalAlignment = HorizontalAlignment.NORMAL_HORIZONTAL;
                unsupported("unsupported horizontal alignment " + horiz);
        }
        int vert = makeEnum();
        switch(vert) {
            case 0:
                this.verticalAlignment = VerticalAlignment.NORMAL_VERTICAL;
                break;
            case 1:
                this.verticalAlignment = VerticalAlignment.TOP;
                break;
            case 2:
                this.verticalAlignment = VerticalAlignment.CAP;
                break;
            case 3:
                this.verticalAlignment = VerticalAlignment.HALF;
                break;
            case 4:
                this.verticalAlignment = VerticalAlignment.BASE;
                break;
            case 5:
                this.verticalAlignment = VerticalAlignment.BOTTOM;
                break;
            case 6:
                this.verticalAlignment = VerticalAlignment.CONTINOUS_VERTICAL;
                break;
            default:
                this.verticalAlignment = VerticalAlignment.NORMAL_VERTICAL;
                unsupported("unsupported vertical alignment " + vert);
        }
        this.continuousHorizontalAlignment = makeReal();
        this.continuousVerticalAlignment = makeReal();
        assert (this.currentArg == this.args.length);
    }

    @Override
    public void paint(CGMDisplay d) {
        d.setTextAlignment(this.horizontalAlignment, this.verticalAlignment, this.continuousHorizontalAlignment, this.continuousVerticalAlignment);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TextAlignment");
        sb.append(" horizontal=").append(this.horizontalAlignment);
        sb.append(" vertical=").append(this.verticalAlignment);
        sb.append(" continousHorizontalAlignment=").append(this.continuousHorizontalAlignment);
        sb.append(" continuousVerticalAlignment=").append(this.continuousVerticalAlignment);
        return sb.toString();
    }
}
