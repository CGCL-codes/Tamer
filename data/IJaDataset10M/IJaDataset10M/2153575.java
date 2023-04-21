package GUI.GUIReviewers.bidding.search.title;

import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import GUI.GUIReviewers.bidding.search.AbstractsSearches;
import GUI.GUIReviewers.utilities.backButton;
import Repository.Entities.IAbstract;

/**
 * Frame that uses all titleFrame to show abstracts using the iterator given to the constructor.
 * @author G01
 * @version 0.5.1
 * @since 0.4
 *
 *
 */
public class titlesList extends JPanel {

    private static final long serialVersionUID = 3293509297134518009L;

    private LinkedList<titleFrame> titleFrames;

    /**
	 * Class constructor.
	 * @param it - Iterator<IAbstract>
	 * @author G01
	 */
    public titlesList(Iterator<IAbstract> it) {
        super();
        titleFrames = new LinkedList<titleFrame>();
        if (it.hasNext()) DisplayTitles(it); else {
            addBackButton();
            JOptionPane.showMessageDialog(this, "No Abstracts Present Yet.");
        }
    }

    /**
	 * Creates and adds a back button.
	 * @author G01
	 */
    private void addBackButton() {
        backButton bb = new backButton(AbstractsSearches.titles, AbstractsSearches.titlesTopics);
        bb.setText("GoBack");
        this.add(bb);
    }

    /**
	 * Displays all abstract titles contained in the iterator.
	 * @param it - Iterator<IAbstract>
	 * @author G01
	 */
    private void DisplayTitles(Iterator<IAbstract> it) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        titleFrame tf;
        addBackButton();
        while (it.hasNext()) {
            tf = new titleFrame(it.next());
            titleFrames.add(tf);
            this.add(tf);
        }
    }
}
