package bluffinmuffin.gui.game;

import java.awt.FlowLayout;
import java.util.TreeMap;
import javax.swing.JPanel;
import bluffinmuffin.game.entities.Card;

public class JPanelCard extends JPanel {

    private static final String PATH_IMAGE_CARD = "images/cards/";

    private static final String DEFAULT_CARD = "default.png";

    private String m_current = null;

    private final TreeMap<String, JLabelCard> m_labels = new TreeMap<String, JLabelCard>();

    private static final long serialVersionUID = 1L;

    public JPanelCard() {
        super(true);
        JLabelCard label = new JLabelCard(JPanelCard.PATH_IMAGE_CARD + JPanelCard.DEFAULT_CARD);
        m_labels.put("-2", label);
        this.add(label);
        for (int i = 0; i < 52; ++i) {
            final String code = Card.getInstance(i).toString();
            label = new JLabelCard(JPanelCard.PATH_IMAGE_CARD + code + ".png");
            m_labels.put(code, label);
            this.add(label);
        }
        this.getSize(m_labels.get("-2").getSize());
        this.setOpaque(false);
        final FlowLayout flowLayout1 = new FlowLayout();
        flowLayout1.setHgap(0);
        flowLayout1.setVgap(0);
        setLayout(flowLayout1);
    }

    public void setNoCard() {
        hideCurrent();
    }

    public void setCard(Card c) {
        hideCurrent();
        if (!c.isNoCard()) {
            m_current = c.toString();
            m_labels.get(m_current).setVisible(true);
        }
    }

    private void hideCurrent() {
        if (m_current != null) {
            m_labels.get(m_current).setVisible(false);
            m_current = null;
        }
    }
}
