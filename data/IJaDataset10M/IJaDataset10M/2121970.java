package be.devijver.wikipedia.parser.ast;

/**
 * Created by IntelliJ IDEA.
 * User: steven
 * Date: 4-nov-2006
 * Time: 18:14:11
 * To change this template use File | Settings | File Templates.
 */
public interface ContentHolder extends Content {

    Content[] getContent();
}
