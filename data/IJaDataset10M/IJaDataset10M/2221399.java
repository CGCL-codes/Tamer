package net.sf.buildbox.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import net.sf.buildbox.devmodel.Hint;
import net.sf.buildbox.devmodel.HintSeverity;
import net.sf.buildbox.strictlogging.api.*;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public abstract class LiveFileWithHints extends LiveFile {

    private static final Catalog CAT = StrictCatalogFactory.getCatalog(Catalog.class);

    protected final Collection<LogMessage> parsingHints = new LinkedList<LogMessage>();

    protected LiveFileWithHints(File file) {
        super(file);
        if (file == null) {
            parsingHints.add(CAT.noFileSpecified());
        }
    }

    protected final void clearData() {
        parsingHints.clear();
        clear();
    }

    protected void clear() {
    }

    protected void fileDoesNotExist() {
        parsingHints.add(CAT.fileNotFound(relPath(getFile())));
    }

    protected Iterable<LogMessage> referenceHints() {
        return null;
    }

    protected void parseFileWithSax(ContentHandler handler) {
        try {
            super.parseFileWithSax(handler);
        } catch (ParserConfigurationException e) {
            parsingHints.add(CAT.exception(getFile(), e.getClass().getName(), e));
        } catch (SAXException e) {
        } catch (IOException e) {
            parsingHints.add(CAT.exception(getFile(), e.getClass().getName(), e));
        }
    }

    protected File getBasePath() {
        return null;
    }

    public final List<Hint> getHints() {
        System.out.println("this = " + this);
        updateCache();
        final ArrayList<Hint> hints = new ArrayList<Hint>();
        final Iterable<LogMessage> refHints = referenceHints();
        if (refHints != null) {
            Iterables.addAllTo(hints, new Iterables.Deref<LogMessage, Hint>(refHints) {

                public Hint get(LogMessage logMessage) {
                    return logMessageToHint(logMessage);
                }
            });
        }
        Iterables.addAllTo(hints, new Iterables.Deref<LogMessage, Hint>(parsingHints) {

            public Hint get(LogMessage logMessage) {
                return logMessageToHint(logMessage);
            }
        });
        return hints;
    }

    protected final URI relPath(File file) {
        return FileUtils.relativeURI(getBasePath(), file);
    }

    public static Hint logMessageToHint(LogMessage logMessage) {
        final Hint hint = new Hint();
        hint.setCode(logMessage.getId());
        final HintSeverity hintSeverity;
        switch(logMessage.getSeverity()) {
            case FATAL:
                hintSeverity = HintSeverity.FATAL;
                break;
            case ERROR:
                hintSeverity = HintSeverity.ERROR;
                break;
            case WARN:
                hintSeverity = HintSeverity.WARNING;
                break;
            case INFO:
            case DEBUG:
            case TRACE:
                hintSeverity = HintSeverity.INFO;
                break;
            default:
                hintSeverity = HintSeverity.ERROR;
        }
        hint.setSeverity(hintSeverity);
        hint.setDescription(logMessage.toString());
        final Object[] logParams = logMessage.getParameters();
        for (int i = 0; i < logParams.length; i++) {
            final Object logParam = logParams[i];
            hint.addParameter("p" + (i + 1), logParam == null ? null : logParam.toString());
        }
        return hint;
    }

    private static interface Catalog extends StrictCatalog {

        @StrictCatalogEntry(severity = Severity.ERROR, format = "File not found: '%s'")
        LogMessage fileNotFound(URI relPath);

        @StrictCatalogEntry(severity = Severity.ERROR, format = "No file specified")
        LogMessage noFileSpecified();

        @StrictCatalogEntry(severity = Severity.ERROR, format = "%s: %s - %s")
        LogMessage exception(File file, String exceptionClass, Throwable exception);
    }
}
