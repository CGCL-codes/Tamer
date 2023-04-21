package chartlib;

import javax.swing.*;
import java.awt.*;

/**
 * @author sulmanj
 *
 * Manages and displays the key. 
 * 
 */
public class ChartKey extends JPanel {

    private int numElements;

    private JLabel[] keyTitles;

    private JPanel[] colorBoxes;

    public ChartKey() {
        numElements = 0;
    }

    /**
	 * Make a key with a number of sets with default colors and titles.
	 * @param numSets The number of sets.
	 */
    public ChartKey(int numSets) {
        numElements = numSets;
        keyTitles = new JLabel[numSets];
        colorBoxes = new JPanel[numSets];
        setupLayout();
    }

    /**
	 * Sets the title of a data set.
	 * @param index The index of the data title to change.
	 * @param title The new title.
	 */
    public void setTitle(int index, String title) {
        keyTitles[index].setText(title);
        repaint();
    }

    /**
	 * Sets the color of a dataset.
	 * @param index The index of the dataset on the key.
	 * @param c The new color.
	 */
    public void setColor(int index, Color c) {
        colorBoxes[index].setBackground(c);
        repaint();
    }

    /**
	 * Sets up the key with default attributes for the current number of sets.
	 *
	 */
    private void setupLayout() {
        if (numElements == 0) return;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Key"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        JPanel datasetPanel = new JPanel();
        for (int count = 0; count < numElements; count++) {
            datasetPanel = new JPanel();
            datasetPanel.setLayout(new BoxLayout(datasetPanel, BoxLayout.X_AXIS));
            datasetPanel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
            colorBoxes[count] = new JPanel();
            colorBoxes[count].setMaximumSize(new Dimension(10, 10));
            colorBoxes[count].setMinimumSize(new Dimension(10, 10));
            colorBoxes[count].setPreferredSize(new Dimension(10, 10));
            colorBoxes[count].setBackground(Chart.DATA_COLORS[count % Chart.DATA_COLORS.length]);
            datasetPanel.add(colorBoxes[count]);
            datasetPanel.add(Box.createRigidArea(new Dimension(10, 10)));
            keyTitles[count] = new JLabel("Dataset " + (count + 1));
            keyTitles[count].setFont(new Font("Geneva", Font.PLAIN, 10));
            datasetPanel.add(keyTitles[count]);
            add(datasetPanel);
            add(Box.createRigidArea(new Dimension(10, 10)));
        }
        setMaximumSize(getPreferredSize());
    }
}
