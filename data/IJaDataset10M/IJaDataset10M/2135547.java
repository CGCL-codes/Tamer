package nl.headspring.photoz.client;

import nl.headspring.photoz.common.Configuration;
import nl.headspring.photoz.common.ImageService;
import nl.headspring.photoz.common.eventbus.BusEvent;
import nl.headspring.photoz.common.eventbus.EventBus;
import nl.headspring.photoz.common.eventbus.Subscriber;
import nl.headspring.photoz.imagecollection.Annotation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Class CombiImageView.
 *
 * @author Eelco Sommer
 * @since Sep 19, 2010
 */
public class CombiImageView extends ImageView implements Subscriber {

    private static final Log LOG = LogFactory.getLog(CombiImageView.class);

    private static final String VIEW_AS_THUMBNAILS = "Thumbnails";

    private static final String VIEW_AS_LIST = "List";

    private final ImageService imageService;

    private final Configuration configuration;

    private final ContactSheetView contactSheetView;

    private final ImageListView imageListView;

    private final JPanel cards;

    private ImageView currentImageView;

    private final List<Annotation> selection = new ArrayList<Annotation>();

    public CombiImageView(EventBus eventBus, ImageService imageService, Configuration configuration, ResourceBundle rb) {
        setLayout(new BorderLayout());
        this.configuration = configuration;
        this.imageService = imageService;
        JPanel viewControls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JComboBox viewSelect = new JComboBox(new Object[] { VIEW_AS_THUMBNAILS, VIEW_AS_LIST });
        viewControls.add(new JLabel("View as:"));
        viewControls.add(viewSelect);
        viewSelect.addActionListener(new ViewSelectListener());
        add(viewControls, BorderLayout.NORTH);
        this.contactSheetView = new ContactSheetView(eventBus, imageService, configuration);
        this.imageListView = new ImageListView(eventBus, imageService, configuration);
        this.cards = new JPanel(new CardLayout());
        cards.add(new JScrollPane(contactSheetView, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), VIEW_AS_THUMBNAILS);
        cards.add(new JScrollPane(imageListView, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), VIEW_AS_LIST);
        currentImageView = contactSheetView;
        add(cards, BorderLayout.CENTER);
    }

    public void handleBusEvent(BusEvent e) {
    }

    public void setSelection(java.util.List<Annotation> selection) {
        if (currentImageView == null) {
            LOG.warn("No current image view");
        }
        this.selection.clear();
        this.selection.addAll(selection);
        this.currentImageView.setSelection(selection);
    }

    class ViewSelectListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            JComboBox viewSelect = (JComboBox) e.getSource();
            String view = (String) viewSelect.getSelectedItem();
            CardLayout layout = (CardLayout) (cards.getLayout());
            layout.show(cards, view);
            if (VIEW_AS_THUMBNAILS.equals(view)) {
                contactSheetView.setSelection(selection);
                imageListView.setSelection(Collections.<Annotation>emptyList());
                currentImageView = contactSheetView;
            } else if (VIEW_AS_LIST.equals(view)) {
                imageListView.setSelection(selection);
                contactSheetView.setSelection(Collections.<Annotation>emptyList());
                currentImageView = imageListView;
            }
        }
    }
}
