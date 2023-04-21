package za.org.meraka.dictionarymaker.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import za.org.meraka.dictionarymaker.model.WordData;

/**
 * 2006/02/21
 * 
 * @author avrensbu
 * 
 * Custom cell renderer to display only word from WordData in list
 */
public class WordListCellRenderer extends JLabel implements ListCellRenderer {

    /**
	 * 
	 */
    private List visibleStatus;

    private boolean visibleErrorLevel;

    private Dimension preferedSize;

    private String filter;

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public void setVisibleStatus(List visibleStatus) {
        this.visibleStatus = visibleStatus;
    }

    public void setVisibleErrorLevel(boolean errorLevel) {
        this.visibleErrorLevel = errorLevel;
    }

    private static final long serialVersionUID = 8988411063689808067L;

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof WordData) {
            if ((visibleErrorLevel) && ((WordData) value).getErrorLevel() >= 1) setText(((WordData) value).getWord() + " .  .  .  :" + readWordStatus(((WordData) value).getStatus()) + " " + "E" + ((WordData) value).getErrorLevel()); else setText(((WordData) value).getWord() + " .  .  .  :" + readWordStatus(((WordData) value).getStatus()));
            if (isSelected) {
                setOpaque(true);
                setBackground(list.getSelectionBackground());
            } else {
                setBackground(list.getBackground());
            }
            this.setEnabled(list.isEnabled());
            if (getPreferredSize().height != 0) preferedSize = getPreferredSize();
            boolean wordContainsErrors;
            if (((WordData) value).getErrorLevel() >= 1) wordContainsErrors = true; else wordContainsErrors = false;
            if ((visibleStatus != null) && ((visibleStatus.contains(new Integer(((WordData) value).getStatus())) && (filter == null)) || (visibleStatus.contains(new Integer(((WordData) value).getStatus())) && (filter != null) && ((WordData) value).getWord().startsWith(filter))) || ((wordContainsErrors && visibleErrorLevel) && (filter == null)) || ((wordContainsErrors && visibleErrorLevel) && (filter != null) && (((WordData) value)).getWord().startsWith(filter))) {
                this.setPreferredSize(preferedSize);
            } else {
                this.setPreferredSize(new Dimension(0, 0));
            }
        }
        return this;
    }

    /**
	 * Displays the Status of a WordData object in human-readable format.
	 * 
	 * @param wordStatus
	 *            Integer used to define status within WordData.
	 * 
	 * @author Marius Pech?? <mpeche@csir.co.za>
	 * @date 2006/07/28
	 */
    public String readWordStatus(int wordStatus) {
        switch(wordStatus) {
            case WordData.STATUS_AMBIGUOUS:
                return "AMBIGUOUS";
            case WordData.STATUS_CORRECT:
                return "CORRECT";
            case WordData.STATUS_INVALID:
                return "INVALID";
            case WordData.STATUS_UNCERTAIN:
                return "UNCERTAIN";
            case WordData.STATUS_UNVERIFIED:
                return "UNVERIFIED";
            case WordData.STATUS_PROPER_NOUN:
                return "PROPER NOUN";
            case WordData.STATUS_FOREIGN:
                return "FOREIGN";
            case WordData.STATUS_PARTIAL:
                return "PARTIAL";
            default:
                return "UNDEFINED";
        }
    }
}
