package be.vds.jtbdive.client.view.core.document;

import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import be.vds.jtb.swing.component.Thumbnail;
import be.vds.jtbdive.client.core.DocumentContentLoader;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Document;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.logging.Syslog;

public class ThumbnailDocumentPanel extends DocumentPanel {

    private static final long serialVersionUID = -4515983474451146582L;

    private static final Syslog LOGGER = Syslog.getLogger(ThumbnailDocumentPanel.class);

    private JLabel iconLabel;

    public ThumbnailDocumentPanel(Document document, DocumentContentLoader loader) {
        super(document);
        init(document, loader);
    }

    private void init(Document document, DocumentContentLoader loader) {
        this.setLayout(new BorderLayout(5, 5));
        iconLabel = new JLabel(UIAgent.getInstance().getIcon(UIAgent.ICON_INFO_16));
        this.add(iconLabel, BorderLayout.CENTER);
        loadContent(document, loader);
    }

    private void loadContent(final Document document, final DocumentContentLoader loader) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                byte[] content = null;
                if (document.getId() == -1) {
                    content = document.getContent();
                } else {
                    try {
                        content = loader.loadDocumentContent(document.getId(), document.getDocumentFormat());
                    } catch (DataStoreException e) {
                        LOGGER.error(e);
                    }
                }
                if (content != null) {
                    Thumbnail t = new Thumbnail(new ImageIcon(content));
                    t.fitToHeight(40);
                    iconLabel.setIcon(t);
                }
            }
        }).start();
    }

    public void reloadValues() {
    }
}
