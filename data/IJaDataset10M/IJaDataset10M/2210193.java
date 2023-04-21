package org.neblipedia.wiki.mediawiki.extension.etiquetas;

import org.neblipedia.wiki.mediawiki.Articulo;
import org.neblipedia.wiki.mediawiki.extension.EVENTOS;

/**
 * http://en.wikipedia.org/wiki/Wikipedia:Transclusion
 * 
 * @author juan
 * 
 */
public class ProcesarIncludes extends ProcesarExtensionTag {

    private boolean directo;

    public ProcesarIncludes(Articulo articulo, boolean directo) {
        super(articulo);
        this.directo = directo;
        try {
            this.registrarTag("onlyinclude", "onlyinclude", false);
            this.registrarTag("includeonly", "includeonly", false);
            this.registrarTag("noinclude", "noinclude", false);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public EVENTOS getEvento() {
        return null;
    }

    /**
	 * el texto entre <includeonly> e </includeonly> sólo será visible en las
	 * páginas donde la plantilla se incluya, y no cuando se visualice la
	 * plantilla como página independiente.
	 */
    public String includeonly(String attr, String cont) {
        if (!directo) {
            return cont;
        } else {
            return "";
        }
    }

    /**
	 * En una plantilla, el texto entre <noinclude> y </noinclude> no se
	 * incluirá dentro de otras páginas. será visible cuando se visualize
	 * directamente
	 */
    public String noinclude(String attr, String cont) {
        if (directo) {
            return cont;
        } else {
            return "";
        }
    }

    /**
	 * 
	 * onlyinclude. The markup <onlyinclude>...</onlyinclude> indicates that
	 * only text surrounded by "onlyinclude" markup should be transcluded onto
	 * another page. This is the most subtle of the partial transclusion tags
	 * because it often overrules the others. If there is at least one pair of
	 * "onlyinclude" tags on a page, then whenever this page is transcluded, it
	 * is only the material within the "onlyinclude" tags which gets
	 * transcluded. There can be several such sections, and within each such
	 * section, some material might be further excluded by "noinclude" tags, and
	 * might also be surrounded by "includeonly" tags so that it does not appear
	 * on the original page itself. But material outside the "onlyinclude" tags
	 * will be ignored when the page is transcluded onto another page. This can
	 * be useful, for example, to repeat a small part of one page on a second
	 * one: just surround the small part by onlyinclude tags, and transclude it
	 * onto the second page.
	 * 
	 * @param attr
	 * @param cont
	 * @return
	 * @throws ExceptionExtensionTag
	 */
    public String onlyinclude(String attr, String cont) throws ExceptionExtensionTag {
        if (!directo) {
            contenido.delete(0, contenido.length()).append(cont);
            throw new ExceptionExtensionTag("se detiene para q no tome nada más");
        } else {
            return cont;
        }
    }
}
