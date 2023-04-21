package info.bliki.blikiinajar.helpers.wiki.render.filters;

/**
 * Makes double single quotes to italic tags.
 * @author rico_g AT users DOT sourceforge DOT net
 *
 */
public class StrongFilter implements Filter {

    public String filter(String text) {
        return text.replaceAll("'{2}(.*?)'{2}", "<i>$1</i>");
    }
}
