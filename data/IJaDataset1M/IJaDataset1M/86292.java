package wicket.examples.displaytag.export;

import wicket.markup.html.link.Link;
import java.util.List;

/**
 * Define action if Link is selected
 *
 * @author Juergen Donnerstag
 */
public class ExportLink extends Link {

    private final List data;

    private final BaseExportView exportView;

    /**
     * Constructor
     *
     * @param id
     * @param data
     * @param exportView
     */
    public ExportLink(final String id, final List data, final BaseExportView exportView) {
        super(id);
        this.data = data;
        this.exportView = exportView;
    }

    /**
     * @see wicket.markup.html.link.Link#onClick()
     */
    public void onClick() {
        setResponsePage(new Export(exportView, data));
    }
}
