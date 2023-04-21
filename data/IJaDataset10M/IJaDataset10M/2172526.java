package be.devijver.wikipedia.parser.ast;

/**
 * Created by IntelliJ IDEA.
 * User: steven
 * Date: 5-nov-2006
 * Time: 1:25:53
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractContentHolder implements ContentHolder {

    private final Content[] content;

    public AbstractContentHolder(Content[] content) {
        this.content = content;
    }

    public Content[] getContent() {
        return content;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < content.length; i++) {
            sb.append(content[i].toString());
        }
        return sb.toString();
    }
}
