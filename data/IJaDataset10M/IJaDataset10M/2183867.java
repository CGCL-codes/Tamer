package oxygen.wiki;

import java.util.regex.Pattern;
import oxygen.markup.MarkupLink;
import oxygen.markup.MarkupParserBase;
import oxygen.markup.MarkupRenderContext;
import oxygen.markup.MarkupUtils;

public class WikiParser2Base extends MarkupParserBase {

    private WikiCategoryEngine wce;

    public void setRenderContext(MarkupRenderContext _rc) throws Exception {
        super.setRenderContext(_rc);
        if (_rc != null && _rc instanceof WikiRenderContext) {
            wce = ((WikiRenderContext) _rc).getWikiCategoryEngine();
        } else {
            wce = WikiLocal.getWikiCategoryEngine();
        }
        WikiProvidedObject wp = (WikiProvidedObject) rc.get(WikiConstants.PAGE_KEY);
        setTextSourceName(wp.getName());
    }

    public Pattern getImagePattern() {
        return MarkupUtils.absoluteImageURLPattern;
    }

    public void link(MarkupLink mLink, boolean doPrint) throws Exception {
        do_link(mLink, doPrint);
    }

    public void word(String s, boolean isSlashSeperated, boolean doPrint) throws Exception {
        do_word(s, isSlashSeperated, doPrint);
    }

    protected WikiLinkHolder do_link(MarkupLink mLink, boolean doPrint) throws Exception {
        WikiLinkHolder lh = createWikiLinkHolder(mLink);
        if (lh != null && doPrint) {
            printLinkHTML(lh, mLink.image);
        }
        return lh;
    }

    protected WikiLinkHolder do_word(String s, boolean isSlashSeperated, boolean doPrint) throws Exception {
        s = s.trim();
        if (rc.isCensoredWord(s)) {
            s = rc.getCensoredWordReplacement(s);
        }
        String s2 = Character.toUpperCase(s.charAt(0)) + s.substring(1);
        int colidx = s.indexOf(":");
        WikiLinkHolder lh = null;
        boolean anImage = false;
        if (freeLinkSupported() && wce != null && getImagePattern().matcher(s).matches()) {
            lh = createWikiLinkHolder(s, false);
            anImage = true;
        } else if (freeLinkSupported() && wce != null && colidx != -1 && colidx < s.length() - 1) {
            lh = createWikiLinkHolder(s, false);
        } else if (freeLinkSupported() && wce != null && rc.isReferenced(s)) {
            lh = createWikiLinkHolder(s, false);
        } else if (freeLinkSupported() && wce != null && rc.isReferenced(s2)) {
            lh = createWikiLinkHolder(s + "|" + s2, false);
        } else if (freeLinkSupported() && isSlashSeperated && slashSeparatedWordIsLink()) {
            lh = createWikiLinkHolder(s, false);
        } else if (freeLinkSupported() && camelCaseWordIsLink() && MarkupUtils.isCamelCaseWord(s)) {
            lh = createWikiLinkHolder(s, false);
        } else {
            lh = null;
            write(s);
        }
        if (lh != null && doPrint) {
            printLinkHTML(lh, anImage);
        }
        return lh;
    }

    private WikiLinkHolder createWikiLinkHolder(String s, boolean b) throws Exception {
        return createWikiLinkHolder(new MarkupLink(s, b));
    }

    private WikiLinkHolder createWikiLinkHolder(MarkupLink mlink) throws Exception {
        WikiLinkHolder lh = new WikiLinkHolder();
        if (wce != null) {
            lh.setCategory(wce.getName());
        }
        lh.setAction(WikiConstants.ACTION_VIEW);
        lh.setContextPage(textSourceName);
        lh.parseWikiLinkstr(mlink);
        return lh;
    }

    private void printLinkHTML(WikiLinkHolder lh, boolean anImage) throws Exception {
        try {
            write(WikiViewUtils.getLinkHTML(lh, anImage, decorateExternalLinks()));
        } catch (ClassCastException cce) {
            write("__CANNOT_GET_URL_IN_THIS_CONTEXT__");
        }
    }
}
