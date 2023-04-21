package ru.adv.xml.transformer;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;
import ru.adv.cache.Includable;
import ru.adv.cache.Include;
import ru.adv.logger.TLogger;
import ru.adv.util.InputOutput;
import ru.adv.util.InputOutputPrefixMap;

/**
 * Используется трансформером для нахождения вовлеченных внешних includable файлов.
 * 
 */
public class MURIResolver implements URIResolver, Includable {

    private Set<Include> includes;

    private InputOutputPrefixMap prefixes;

    private TLogger logger = new TLogger(MURIResolver.class);

    private Source lastResolvedSource = null;

    /**
	* Constructor
	* @param base для ограничивает доступа из впарсиваемых файлов 
	* @see ru.adv.util.Path
	*/
    public MURIResolver(InputOutput base) {
        this.prefixes = base.getPrefixes();
        this.includes = new HashSet<Include>();
    }

    public synchronized Source resolve(String href, String base) throws TransformerException {
        StreamSource source;
        InputOutput newHref;
        logger.debug("href:" + href + " base:" + base);
        try {
            if (href == null || href.length() == 0) {
                return null;
            }
            if (base == null) {
                newHref = InputOutput.create(href, this.prefixes);
            } else {
                InputOutput baseIO = InputOutput.create(base, prefixes);
                newHref = baseIO.createRelative(href);
            }
            source = new StreamSource(newHref.getBufferedInputStream(), newHref.getSystemId());
            includes.add(newHref.createInclude());
        } catch (Throwable t) {
            final String message = "Cannot resolve href=\"" + href + "\"";
            logger.error(message);
            throw new TransformerException(message, t);
        }
        this.lastResolvedSource = source;
        return source;
    }

    public Source getLastResolvedSource() {
        return lastResolvedSource;
    }

    /**
	* empty public method
	*/
    public void addInclude(Include include) {
    }

    /**
	* empty public method
	*/
    public void addIncludes(Collection<Include> includes) {
    }

    /**
	* Get resolved includes
	* @return Collection of includes
	*/
    public Set<Include> getIncludes() {
        return this.includes;
    }
}
